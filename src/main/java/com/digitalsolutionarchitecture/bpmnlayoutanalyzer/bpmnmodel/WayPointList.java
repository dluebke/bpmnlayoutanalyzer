package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WayPointList {

	private List<WayPoint> waypoints = new ArrayList<>();
	
	public WayPointList() {
	}
	
	public WayPointList(List<WayPoint> waypoints) {
		this.waypoints.addAll(waypoints);
	}

	public void add(WayPoint wp) {
		waypoints.add(wp);
	}
	
	public List<WayPoint> getWaypoints() {
		return Collections.unmodifiableList(waypoints);
	}
}
