package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges;

import java.util.Arrays;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.Result;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;

class SequenceFlowReportItem extends Result {

	static final String[] HEADERS = new String[] {
		"Id", "SourceId", "SourceType", "TargetId", "TargetType", "WayPoints", "IsOptimizable", "Direction"
	};

	public SequenceFlowReportItem(BpmnProcess p) {
		super(p);
	}

	String id;
	String sourceId;
	String sourceType;
	String targetId;
	String targetType;
	boolean isOptimizable;
	int wayPoints;
	EdgeDirection sequenceFlowDirection;
	
	@Override
	public List<Object> getValues() {
		return Arrays.asList(
			this.id, 
			this.sourceId, 
			this.sourceType, 
			this.targetId, 
			this.targetType, 
			this.wayPoints, 
			this.isOptimizable, 
			this.sequenceFlowDirection
		);
	}
}
