package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.flownodecount;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.SubProcess;

public class FlowNodeCountAnalyzer implements IBpmnAnalyzer {

	private List<FlowNodeCounts> flowNodeCounts = new ArrayList<>();
	
	@Override
	public void analyze(BpmnProcess processWithDiagramData) {
		List<FlowNode> flowNodesWithLayoutData = processWithDiagramData.getFlowNodes()
			.stream().filter(x -> x.hasLayoutData()).collect(Collectors.toList());
		
		List<SubProcess> subProcesses = copyTo(flowNodesWithLayoutData
		.stream().filter(x -> x instanceof SubProcess).collect(Collectors.toList()));
		
		int activityCount = calculateActivityCount(flowNodesWithLayoutData);
		int nonEmptySubProcessCount = countAllNonEmptySubProcesses(subProcesses);
		int emptySubProcessCount = countAllEmptySubProcesses(subProcesses);
		int nonEmptyEventProcessCount = countAllNonEmptyEventProcesses(subProcesses);
		int emptyEventProcessCount = countAllEmptyEventProcesses(subProcesses);
		
		flowNodeCounts.add(new FlowNodeCounts(
			processWithDiagramData, 
			flowNodesWithLayoutData.size(), 
			activityCount, 
			nonEmptySubProcessCount,
			emptySubProcessCount,
			nonEmptyEventProcessCount,
			emptyEventProcessCount
		));
	}

	private List<SubProcess> copyTo(List<FlowNode> collect) {
		List<SubProcess> result = new ArrayList<>(collect.size());
		for(FlowNode fn : collect) {
			result.add((SubProcess)fn);
		}
		return result;
	}

	private int countAllNonEmptyEventProcesses(List<SubProcess> subProcesses) {
		return (int)subProcesses.stream().filter(x -> 
			((SubProcess)x).getProcess() != null &&
			((SubProcess)x).isTriggeredByEvent()
		).count();
	}
	
	private int countAllEmptyEventProcesses(List<SubProcess> subProcesses) {
		return (int)subProcesses.stream().filter(x -> 
			((SubProcess)x).getProcess() == null &&
			((SubProcess)x).isTriggeredByEvent()
		).count();
	}

	private int countAllNonEmptySubProcesses(List<SubProcess> subProcesses) {
		return (int)subProcesses.stream().filter(x -> 
			((SubProcess)x).getProcess() != null &&
			!((SubProcess)x).isTriggeredByEvent()
		).count();
	}
	
	private int countAllEmptySubProcesses(List<SubProcess> subProcesses) {
		return (int)subProcesses.stream().filter(x -> 
			((SubProcess)x).getProcess() == null &&
			!((SubProcess)x).isTriggeredByEvent()
		).count();
	}

	private int calculateActivityCount(List<FlowNode> flowNodesWithLayoutData) {
		int activityCount = 0;
		for(FlowNode fn : flowNodesWithLayoutData) {
			if(
				fn.getType().endsWith("Task") ||
				fn.getType().equals("task") ||
				fn.getType().equals("subProcess") 
			) {
				activityCount++;
			}
		}
		return activityCount;
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
