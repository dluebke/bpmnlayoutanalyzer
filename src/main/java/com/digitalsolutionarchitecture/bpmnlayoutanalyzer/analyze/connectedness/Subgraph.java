package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.connectedness;

import java.util.HashSet;
import java.util.Set;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.Trace;

public class Subgraph {

	private Set<FlowNode> flowNodes = new HashSet<>();
	
	public boolean containsTrace(Trace t) {
		for(FlowNode fn : t.getFlowNodes()) {
			if(flowNodes.contains(fn)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void add(Trace t) {
		for(FlowNode fn : t.getFlowNodes()) {
			flowNodes.add(fn);
		}
	}
	
}
