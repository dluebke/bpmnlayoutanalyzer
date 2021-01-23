package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.layouttrace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.util.CounterMap;

public class TraceToDiagramLayoutCalulator {

	public static class LayoutPair {
		private Layout l1;
		private Layout l2;

		LayoutPair(Layout l1, Layout l2) {
			this.l1 = l1;
			this.l2 = l2;
		}

		@Override
		public boolean equals(Object obj) {
			if(obj == this) {
				return true;
			}
			if(obj.getClass() != getClass()) {
				return false;
			}
			
			LayoutPair other = (LayoutPair)obj;
			
			if(
				(other.l1 == l1 && other.l2 == l2) ||
				(other.l1 == l2 && other.l2 == l1)
			) {
				return true;
			}
			
			return false;
		}
		
		@Override
		public int hashCode() {
			return l1.hashCode() + l2.hashCode();
		}

		public boolean isAllEast() {
			return l1.isEast() && l2.isEast();
		}
		
		public boolean isAllWest() {
			return l1.isWest() && l2.isWest();
		}
		
		public boolean isAllSouth() {
			return l1.isSouth() && l2.isSouth();
		}
		
		public boolean isAllNorth() {
			return l1.isNorth() && l2.isNorth();
		}

		@Override
		public String toString() {
			return "(" + l1.name() + "," + l2.name() + ")";
		}
		
	}

	private Map<LayoutPair, Layout> layoutRules = Stream.of(new Object[][] {
		  { new LayoutPair(Layout.LEFT_RIGHT_DIRTY, Layout.LEFT_RIGHT_PURE), Layout.LEFT_RIGHT_DIRTY }, 
		  { new LayoutPair(Layout.LEFT_RIGHT_DIRTY, Layout.LEFT_RIGHT_GATEWAY_VERTICAL_ALLOWED), Layout.LEFT_RIGHT_DIRTY }, 
		  { new LayoutPair(Layout.LEFT_RIGHT_PURE, Layout.LEFT_RIGHT_GATEWAY_VERTICAL_ALLOWED), Layout.LEFT_RIGHT_GATEWAY_VERTICAL_ALLOWED },
		  
		  { new LayoutPair(Layout.TOP_DOWN_DIRTY, Layout.TOP_DOWN_PURE), Layout.TOP_DOWN_DIRTY }, 
		  { new LayoutPair(Layout.TOP_DOWN_DIRTY, Layout.TOP_DOWN_GATEWAY_HORIZONTAL_ALLOWED), Layout.TOP_DOWN_DIRTY }, 
		  { new LayoutPair(Layout.TOP_DOWN_PURE, Layout.TOP_DOWN_GATEWAY_HORIZONTAL_ALLOWED), Layout.TOP_DOWN_GATEWAY_HORIZONTAL_ALLOWED},
		  
		  { new LayoutPair(Layout.TOP_DOWN_PURE, Layout.SNAKE_SOUTH), Layout.SNAKE_SOUTH},
		  { new LayoutPair(Layout.TOP_DOWN_GATEWAY_HORIZONTAL_ALLOWED, Layout.SNAKE_SOUTH), Layout.SNAKE_SOUTH},
		  { new LayoutPair(Layout.TOP_DOWN_PURE, Layout.MULTILINE_SOUTH), Layout.MULTILINE_SOUTH},
		  { new LayoutPair(Layout.TOP_DOWN_GATEWAY_HORIZONTAL_ALLOWED, Layout.MULTILINE_SOUTH), Layout.MULTILINE_SOUTH},
		  
		  { new LayoutPair(Layout.LEFT_RIGHT_PURE, Layout.SNAKE_EAST), Layout.SNAKE_EAST},
		  { new LayoutPair(Layout.LEFT_RIGHT_GATEWAY_VERTICAL_ALLOWED, Layout.SNAKE_EAST), Layout.SNAKE_EAST},
		  { new LayoutPair(Layout.LEFT_RIGHT_PURE, Layout.MULTILINE_EAST), Layout.MULTILINE_EAST},
		  { new LayoutPair(Layout.LEFT_RIGHT_GATEWAY_VERTICAL_ALLOWED, Layout.MULTILINE_EAST), Layout.MULTILINE_EAST}
		  
		}).collect(Collectors.toMap(data -> (LayoutPair)data[0], data -> (Layout)data[1]));;
	
	public Layout calculateDiagramLayout(CounterMap<Layout> traceLayouts) {
		if(traceLayouts.isEmpty()) {
			return Layout.EMPTY;
		}
		
		List<Layout> layouts = new ArrayList<>(traceLayouts.keySet());
		Layout result = layouts.get(0);
		for(int i = 1; i < layouts.size(); i++) {
			Layout nextLayout = layouts.get(i);
			result = combineLayout(result, nextLayout);
		}
		
		return result;
	}

	public Layout combineLayout(Layout l1, Layout l2) {
		if(l1 == l2) {
			return l1;
		}
		if(l1 == Layout.OTHER || l2 == Layout.OTHER) {
			return Layout.OTHER;
		}
		for(Layout l : new Layout[] { Layout.EVENT_SUBPROCESS, Layout.BOUNDARY_EVENT, Layout.SINGLE_ACTIVITY }) {
			if(l1 == l) {
				return l2;
			}
			if(l2 == l) {
				return l1;
			}
		}
		
		LayoutPair p = new LayoutPair(l1, l2);
		Layout layout = layoutRules.get(p);
		if(layout != null) {
			return layout;
		}
		
		if(p.isAllEast()) {
			return Layout.SOMEHOW_EAST;
		} else if(p.isAllWest()) {
			return Layout.SOMEHOW_WEST;
		} else if(p.isAllNorth()) {
			return Layout.SOMEHOW_NORTH;
		} else if(p.isAllSouth()) {
			return Layout.SOMEHOW_SOUTH;
		} else {
			return Layout.OTHER;
		}
	}
}
