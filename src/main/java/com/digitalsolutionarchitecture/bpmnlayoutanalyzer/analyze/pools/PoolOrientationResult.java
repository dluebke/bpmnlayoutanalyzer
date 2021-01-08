package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.pools;

import java.util.Arrays;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.Result;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;

class PoolOrientationResult extends Result {
	
	final static String[] HEADERS = new String[] {
		"Horizontal", "Vertical", "Unknown", "LaneCount"
	};
	
	private int poolOrientationUnknown;
	private int poolOrientationVertical;
	private int poolOrientationHorizontal;
	private int laneCount;

	public PoolOrientationResult(BpmnProcess process) {
		super(process);
	}

	public void incPoolOrientationHorizontal() {
		poolOrientationHorizontal++;
	}

	public void incPoolOrientationVertical() {
		poolOrientationVertical++;
	}

	public void incPoolOrientationUnknown() {
		poolOrientationUnknown++;
	}
	
	public int getPoolOrientationHorizontal() {
		return poolOrientationHorizontal;
	}
	
	public int getPoolOrientationVertical() {
		return poolOrientationVertical;
	}
	
	public int getPoolOrientationUnknown() {
		return poolOrientationUnknown;
	}

	public int getLaneCount() {
		return laneCount;
	}

	public void incLaneCount(int incBy) {
		laneCount += incBy;
	}
	
	@Override
	public List<Object> getValues() {
		return Arrays.asList(
			getPoolOrientationHorizontal(), 
			getPoolOrientationVertical(), 
			getPoolOrientationUnknown(),
			getLaneCount()
		);
	}
}
