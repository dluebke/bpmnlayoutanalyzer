package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.debug;

import java.util.Arrays;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.Result;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;

public class AllFlowNodeTypesResult extends Result {

	private String flowNodeType;

	public AllFlowNodeTypesResult(BpmnProcess p, String flowNodeType) {
		super(p);
		this.flowNodeType = flowNodeType;
	}

	@Override
	public List<Object> getValues() {
		return Arrays.asList(flowNodeType);
	}

	public String getFlowNodeType() {
		return flowNodeType;
	}
}
