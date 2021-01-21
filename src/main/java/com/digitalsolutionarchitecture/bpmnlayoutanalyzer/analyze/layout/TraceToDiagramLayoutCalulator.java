package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.layout;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	}

	Map<LayoutPair, Layout> layoutRules = Stream.of(new Object[][] {
		  { new LayoutPair(Layout.DIRTY_LEFT_RIGHT, Layout.PURE_LEFT_RIGHT), Layout.DIRTY_LEFT_RIGHT }, 
		  { new LayoutPair(Layout.DIRTY_LEFT_RIGHT, Layout.LEFT_RIGHT_GATEWAY_VERTICAL_ALLOWED), Layout.DIRTY_LEFT_RIGHT }, 
		  { new LayoutPair(Layout.PURE_LEFT_RIGHT, Layout.LEFT_RIGHT_GATEWAY_VERTICAL_ALLOWED), Layout.LEFT_RIGHT_GATEWAY_VERTICAL_ALLOWED },
		  
		  { new LayoutPair(Layout.DIRTY_TOP_DOWN, Layout.PURE_TOP_DOWN), Layout.DIRTY_TOP_DOWN }, 
		  { new LayoutPair(Layout.DIRTY_TOP_DOWN, Layout.TOP_DOWN_GATEWAY_HORIZONTAL_ALLOWED), Layout.DIRTY_TOP_DOWN }, 
		  { new LayoutPair(Layout.PURE_TOP_DOWN, Layout.TOP_DOWN_GATEWAY_HORIZONTAL_ALLOWED), Layout.TOP_DOWN_GATEWAY_HORIZONTAL_ALLOWED},
		  
		  { new LayoutPair(Layout.PURE_TOP_DOWN, Layout.SNAKE_SOUTH), Layout.SNAKE_SOUTH},
		  { new LayoutPair(Layout.TOP_DOWN_GATEWAY_HORIZONTAL_ALLOWED, Layout.SNAKE_SOUTH), Layout.SNAKE_SOUTH},
		  { new LayoutPair(Layout.PURE_TOP_DOWN, Layout.MULTILINE_SOUTH), Layout.MULTILINE_SOUTH},
		  { new LayoutPair(Layout.TOP_DOWN_GATEWAY_HORIZONTAL_ALLOWED, Layout.MULTILINE_SOUTH), Layout.MULTILINE_SOUTH},
		  
		  { new LayoutPair(Layout.PURE_LEFT_RIGHT, Layout.SNAKE_EAST), Layout.SNAKE_EAST},
		  { new LayoutPair(Layout.LEFT_RIGHT_GATEWAY_VERTICAL_ALLOWED, Layout.SNAKE_EAST), Layout.SNAKE_EAST},
		  { new LayoutPair(Layout.PURE_LEFT_RIGHT, Layout.MULTILINE_EAST), Layout.MULTILINE_EAST},
		  { new LayoutPair(Layout.LEFT_RIGHT_GATEWAY_VERTICAL_ALLOWED, Layout.MULTILINE_EAST), Layout.MULTILINE_EAST},
		  
		}).collect(Collectors.toMap(data -> (LayoutPair)data[0], data -> (Layout)data[1]));;
	
	public Layout calculateDiagramLayout(List<Layout> layouts) {
		if(layouts.isEmpty()) {
			return Layout.OTHER;
		}
		
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
		if(l1 == Layout.EVENT_SUBPROCESS) {
			return l2;
		}
		if(l2 == Layout.EVENT_SUBPROCESS) {
			return l1;
		}
		
		LayoutPair p = new LayoutPair(l1, l2);
		Layout layout = layoutRules.get(p);
		
		if(layout != null) {
			return layout;
		} else {
			return Layout.OTHER;
		}
	}
}
