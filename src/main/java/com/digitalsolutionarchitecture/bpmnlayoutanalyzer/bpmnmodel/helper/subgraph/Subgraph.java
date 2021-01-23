package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.helper.subgraph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.Trace;

public class Subgraph {

	private Set<FlowNode> flowNodes = new HashSet<>();
	private Set<FlowNode> startFlowNodes = new HashSet<>();
	private Set<FlowNode> endFlowNodes = new HashSet<>();
	
	public boolean containsTrace(Trace t) {
		for(FlowNode fn : t.getFlowNodes()) {
			if(flowNodes.contains(fn)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Deprecated
	public void add(Trace t) {
		for(FlowNode fn : t.getFlowNodes()) {
			flowNodes.add(fn);
		}
	}
	
	public void add(FlowNode fn) {
		flowNodes.add(fn);
		if(fn.getIncomingSequenceFlows().size() == 0) {
			startFlowNodes.add(fn);
		}
		if(fn.getOutgoingSequenceFlows().size() == 0) {
			endFlowNodes.add(fn);
		}
	}
	
	public Set<FlowNode> getStartFlowNodes() {
		return startFlowNodes;
	}
	
	public Set<FlowNode> getEndFlowNodes() {
		return endFlowNodes;
	}
	
	public Set<FlowNode> getFlowNodes() {
		return flowNodes;
	}

	public void addAll(List<FlowNode> flowNodes) {
		for(FlowNode fn : flowNodes) {
			add(fn);
		}
		
	}

	public boolean contains(FlowNode fn) {
		return flowNodes.contains(fn);
	}
}
