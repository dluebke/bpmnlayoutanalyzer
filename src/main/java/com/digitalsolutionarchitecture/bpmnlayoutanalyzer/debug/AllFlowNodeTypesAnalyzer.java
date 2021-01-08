package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.debug;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.Result;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;

public class AllFlowNodeTypesAnalyzer implements IBpmnAnalyzer {

	private List<AllFlowNodeTypesResult> results = new ArrayList<>();
	
	@Override
	public void analyze(BpmnProcess processWithDiagramData) {
		Set<String> flowNodeTypes = processWithDiagramData.getFlowNodes().stream().map(x -> x.getType()).collect(Collectors.toSet());
		
		for(String flowNodeType : flowNodeTypes) {
			results.add(new AllFlowNodeTypesResult(processWithDiagramData, flowNodeType));
		}
	}

	@Override
	public String getShortName() {
		return "debug.flownodenames";
	}

	@Override
	public List<? extends Result> getResults() {
		return results;
	}

	@Override
	public String[] getHeaders() {
		return new String[] { "FlowNodeType" };
	}

}
