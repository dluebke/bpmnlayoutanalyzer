package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Trace {

	private List<FlowNode> trace = new ArrayList<>();

	public Trace() {
	}
	
	public Trace(Trace traceToClone) {
		trace.addAll(traceToClone.trace);
	}
	
	public void addFlowNodeToTrace(FlowNode n) {
		if(trace.size() == 0) {
			trace.add(n);
		} else {
			FlowNode lastNode = trace.get(trace.size() - 1);
			List<FlowNode> nodesReachableFromLastNode = lastNode.getOutgoingSequenceFlows()
				.stream()
				.map(x -> x.getTarget())
				.collect(Collectors.toList())
			;
			
			if(nodesReachableFromLastNode.contains(n)) {
				trace.add(n);
			} else {
				throw new RuntimeException(n + " cannot be connected to " + lastNode);
			}
		}
	}
	
	public boolean contains(FlowNode n) {
		return trace.contains(n);
	}
	
	public FlowNode getFirstCommonFlowNode(Trace other) {
		for(FlowNode n : trace) {
			if(other.contains(n)) {
				return n;
			}
		}
		
		return null;
	}

	public FlowNode last() {
		return trace.get(trace.size() - 1);
	}

	public List<FlowNode> getFlowNodes() {
		return trace;
	}
	
}
