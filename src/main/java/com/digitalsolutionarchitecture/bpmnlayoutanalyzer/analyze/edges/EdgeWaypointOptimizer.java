package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.WayPoint;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.util.DoubleUtil;

public class EdgeWaypointOptimizer {

	public boolean optimize(List<WayPoint> waypoints) {
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
	
}
