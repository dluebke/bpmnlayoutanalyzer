package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges;

import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.WayPoint;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.util.DoubleUtil;

public class EdgeDirectionEvaluator {

	public EdgeDirection evaluateArcType(List<WayPoint> waypoints) {
		WayPoint wp1 = waypoints.get(0);
		WayPoint wpLast = waypoints.get(waypoints.size() - 1);
		
		String direction = null;
		if(DoubleUtil.equals(wp1.getY(), wpLast.getY())) {
			if(wp1.getX() < wpLast.getX()) {
				direction = "LEFT_RIGHT";				
			} else if(wp1.getX() > wpLast.getX()) {
				direction = "RIGHT_LEFT"; 
			} else {
				direction = "ORIGIN"; // TODO Return to origin
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
			return EdgeDirection.valueOf("DIRECT_" + direction);
		} else if(waypoints.size() == 3) {
			WayPoint wp2 = waypoints.get(1);
			if(wp1.getX() == wp2.getX() && wp2.getY() == wpLast.getY()) {
				return EdgeDirection.valueOf("PERPENDICULAR_" + direction);
			} else if(wp1.getY() == wp2.getY() && wp2.getX() == wpLast.getX()) {
				return EdgeDirection.valueOf("PERPENDICULAR_" + direction);
			} else {
				return EdgeDirection.valueOf("BEND_" + direction);
			}
		} else if(waypoints.size() == 4) {
			WayPoint wp2 = waypoints.get(1);
			WayPoint wp3 = waypoints.get(2);
			if(wp1.getX() == wp2.getX() && wp2.getY() == wp3.getY() && wp3.getX() == wpLast.getX()) {
				return EdgeDirection.valueOf("ZAGGED_" + direction);
			} else if(wp1.getY() == wp2.getY() && wp2.getX() == wp3.getX() && wp3.getY() == wpLast.getY()) {
				return EdgeDirection.valueOf("ZAGGED_" + direction);
			} else {
				return EdgeDirection.valueOf("BEND_" + direction);
			}
		} else {
			return EdgeDirection.valueOf("BEND_" + direction);
		}
	}

}
