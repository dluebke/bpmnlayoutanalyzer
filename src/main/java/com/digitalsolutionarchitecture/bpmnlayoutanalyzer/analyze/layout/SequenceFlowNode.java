package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.layout;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges.EdgeDirection;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.SequenceFlow;

public class SequenceFlowNode {

	private SequenceFlow sequenceFlow;
	private boolean isReverseSequenceFlow;
	private EdgeDirection edgeDirection;
	
	public SequenceFlowNode(SequenceFlow sf, boolean isReverseSequenceFlow, EdgeDirection edgeDirection) {
		this.sequenceFlow = sf;
		this.isReverseSequenceFlow = isReverseSequenceFlow;
		this.edgeDirection = edgeDirection;
	}
	
	public boolean isFromSplit() {
		return sequenceFlow.getSource().getOutgoingSequenceFlows().size() > 1;
	}
	
	public boolean isToJoin() {
		return sequenceFlow.getTarget().getIncomingSequenceFlows().size() > 1;
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
}
