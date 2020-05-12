package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.util.DoubleUtil;

public class WayPointList {

	private List<WayPoint> waypoints = new ArrayList<>();
	private EdgeDirection edgeDirection = null;
	
	public WayPointList() {
	}
	
	public WayPointList(List<WayPoint> waypoints) {
		this.waypoints.addAll(waypoints);
	}

	public void add(WayPoint wp) {
		waypoints.add(wp);
		edgeDirection = null;
	}
	
	public List<WayPoint> getWaypoints() {
		return Collections.unmodifiableList(waypoints);
	}
	
	public boolean optimize() {
		Set<WayPoint> toRemove = new HashSet<>();
		
		for(int i = 0; i < waypoints.size() - 2; i++) {
			WayPoint wp1 = waypoints.get(i);
			WayPoint wp2 = waypoints.get(i + 1);
			WayPoint wp3 = waypoints.get(i + 2);
			
			// calculate linear equation y = m * x + b
			double m = (wp3.getY() - wp1.getY())  / (wp3.getX() - wp1.getX());
			double b = wp3.getY() - m * wp3.getX();
			
			double y2 = m * wp2.getX() + b;
			if(DoubleUtil.equals(wp2.getY(), y2)) {
				toRemove.add(wp2);
			}
		}
		
		waypoints.removeAll(toRemove);
		return toRemove.size() > 0;
	}
	
	public EdgeDirection evaluateEdgeDirection() {
		if(edgeDirection != null) {
			return edgeDirection;
		}
		
		WayPoint wp1 = waypoints.get(0);
		WayPoint wpLast = waypoints.get(waypoints.size() - 1);
		
		String direction = null;
		if(DoubleUtil.equals(wp1.getY(), wpLast.getY())) {
			if(wp1.getX() < wpLast.getX()) {
				direction = "LEFT_RIGHT";				
			} else if(wp1.getX() > wpLast.getX()) {
				direction = "RIGHT_LEFT"; 
			} else {
				direction = ""; // TODO Return to origin
			}
		} else if(DoubleUtil.equals(wp1.getX(), wpLast.getX())) {
			if(wp1.getY() < wpLast.getY()) {
				direction = "TOP_BOTTOM";				
			} else if(wp1.getY() > wpLast.getY()) {
				direction = "BOTTOM_TOP"; 
			}
		} else if(wp1.getX() < wpLast.getX()) {
			if(wp1.getY() < wpLast.getY()) {
				direction = "LEFT_LOWERRIGHT";
			} else {
				direction = "LEFT_UPPERRIGHT";				
			}
		} else {
			if(wp1.getY() < wpLast.getY()) {
				direction = "RIGHT_LOWERLEFT";
			} else {
				direction = "RIGHT_UPPERLEFT";				
			}
		}
		
		if(waypoints.size() == 2) {
			edgeDirection = EdgeDirection.valueOf("DIRECT_" + direction);
		} else if(waypoints.size() == 3) {
			WayPoint wp2 = waypoints.get(1);
			if(wp1.getX() == wp2.getX() && wp2.getY() == wpLast.getY()) {
				edgeDirection = EdgeDirection.valueOf("PERPENDICULAR_" + direction);
			} else if(wp1.getY() == wp2.getY() && wp2.getX() == wpLast.getX()) {
				edgeDirection = EdgeDirection.valueOf("PERPENDICULAR_" + direction);
			} else {
				edgeDirection = EdgeDirection.valueOf("BEND_" + direction);
			}
		} else if(waypoints.size() == 4) {
			WayPoint wp2 = waypoints.get(1);
			WayPoint wp3 = waypoints.get(2);
			if(wp1.getX() == wp2.getX() && wp2.getY() == wp3.getY() && wp3.getX() == wpLast.getX()) {
				edgeDirection = EdgeDirection.valueOf("ZAGGED_" + direction);
			} else if(wp1.getY() == wp2.getY() && wp2.getX() == wp3.getX() && wp3.getY() == wpLast.getY()) {
				edgeDirection = EdgeDirection.valueOf("ZAGGED_" + direction);
			} else {
				edgeDirection = EdgeDirection.valueOf("BEND_" + direction);
			}
		} else {
			edgeDirection = EdgeDirection.valueOf("BEND_" + direction);
		}
		return edgeDirection;
	}
	
}
