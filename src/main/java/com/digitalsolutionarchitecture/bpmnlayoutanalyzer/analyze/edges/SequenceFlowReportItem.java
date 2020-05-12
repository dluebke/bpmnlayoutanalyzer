package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.Result;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;

class SequenceFlowReportItem extends Result {

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
}
