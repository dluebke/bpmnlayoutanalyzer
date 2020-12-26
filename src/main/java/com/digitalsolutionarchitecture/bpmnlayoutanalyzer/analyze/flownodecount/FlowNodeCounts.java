package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.flownodecount;

import java.util.Arrays;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.Result;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;

class FlowNodeCounts extends Result {

	static final String[] HEADERS = new String[] { "Total" }; 
	
	private int total;

	public FlowNodeCounts(BpmnProcess p, int total) {
		super(p);
		this.total = total;
	}

	@Override
	public List<Object> getValues() {
		return Arrays.asList(total);
	}

}
