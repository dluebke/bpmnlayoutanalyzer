package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.flownodecount;

import java.util.ArrayList;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;

public class FlowNodeCountAnalyzer implements IBpmnAnalyzer {

	private List<FlowNodeCounts> flowNodeCounts = new ArrayList<>();
	
	@Override
	public void analyze(BpmnProcess processWithDiagramData) {
		flowNodeCounts.add(new FlowNodeCounts(processWithDiagramData, processWithDiagramData.getFlowNodes().size()));
	}

	@Override
	public String getShortName() {
		return "flownodecounts";
	}
	
	@Override
	public List<FlowNodeCounts> getResults() {
		return flowNodeCounts;
	}

	@Override
	public String[] getHeaders() {
		return FlowNodeCounts.HEADERS;
	}
}
