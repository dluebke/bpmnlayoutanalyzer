package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.layout;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges.EdgeDirection;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;

public class SequenceFlowNode {

	private boolean isReverseSequenceFlow;
	private EdgeDirection edgeDirection;
	private FlowNode from;
	private FlowNode to;
	
	public SequenceFlowNode(FlowNode from, FlowNode to, boolean isReverseSequenceFlow, EdgeDirection edgeDirection) {
		this.from = from;
		this.to = to;
		this.isReverseSequenceFlow = isReverseSequenceFlow;
		this.edgeDirection = edgeDirection;
	}
	
	public boolean isFromSplit() {
		return 
			from.getOutgoingSequenceFlows().size() > 1 || 
			from.getType().equals("boundaryEvent")
		;
	}
	
	public boolean isToJoin() {
		return to.getIncomingSequenceFlows().size() > 1;
	}
	
	public boolean isReverseSequenceFlow() {
		return isReverseSequenceFlow;
	}

	public EdgeDirection getEdgeDirection() {
		return edgeDirection;
	}

	public boolean isFromSplitOrToJoin() {
		return isFromSplit() || isToJoin();
	}
	
	@Override
	public String toString() {
		return from + " -> " + to;
	}
}
