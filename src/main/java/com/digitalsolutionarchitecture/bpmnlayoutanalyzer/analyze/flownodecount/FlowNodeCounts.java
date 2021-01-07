package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.flownodecount;

import java.util.Arrays;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.Result;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;

class FlowNodeCounts extends Result {

	static final String[] HEADERS = new String[] { 
		"Total", 
		"Activities", 
		"NonEmptySubProcesses", 
		"EmptySubProcesses", 
		"NonEmptyEventProcesses", 
		"EmptyEventProcesses"
	}; 
	
	private int totalFlowNodeCount;
	private int activityCount;
	private int nonEmptySubProcessCount;
	private int nonEmptyEventProcessCount;
	private int emptySubProcessCount;
	private int emptyEventProcessCount;

	public FlowNodeCounts(
		BpmnProcess p, 
		int totalFlowNodeCount, 
		int activityCount, 
		int nonEmptySubProcessCount,
		int emptySubProcessCount,
		int nonEmptyEventProcessCount,
		int emptyEventProcessCount
		) {
		
		super(p);
		this.totalFlowNodeCount = totalFlowNodeCount;
		this.activityCount = activityCount;
		this.nonEmptySubProcessCount = nonEmptySubProcessCount;
		this.emptySubProcessCount = emptySubProcessCount;
		this.nonEmptyEventProcessCount = nonEmptyEventProcessCount;
		this.emptyEventProcessCount = emptyEventProcessCount;
	}

	@Override
	public List<Object> getValues() {
		return Arrays.asList(
			totalFlowNodeCount, 
			activityCount, 
			nonEmptySubProcessCount, 
			emptySubProcessCount, 
			nonEmptyEventProcessCount, 
			emptyEventProcessCount
		);
	}
	
	public int getTotalFlowNodeCount() {
		return totalFlowNodeCount;
	}

	public int getActivityCount() {
		return activityCount;
	}

	public int getNonEmptySubProcessCount() {
		return nonEmptySubProcessCount;
	}

	public int getNonEmptyEventProcessCount() {
		return nonEmptyEventProcessCount;
	}

	public int getEmptyEventProcessCount() {
		return emptyEventProcessCount;
	}
	
	public int getEmptySubProcessCount() {
		return emptySubProcessCount;
	}
}
