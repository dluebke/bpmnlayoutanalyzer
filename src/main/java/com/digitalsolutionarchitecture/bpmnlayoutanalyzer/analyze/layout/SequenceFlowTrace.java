package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.layout;

import java.util.ArrayList;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;

public class SequenceFlowTrace {

	private List<SequenceFlowNode> sequenceFlows;
	private FlowNode singleFlowNode; 
	private Layout layout;
	
	public SequenceFlowTrace(FlowNode singleFlowNode) {
		this.singleFlowNode = singleFlowNode;
		this.sequenceFlows = null;
	}
	
	public SequenceFlowTrace() {
		this.sequenceFlows = new ArrayList<>();
	}
	
	public List<SequenceFlowNode> getSequenceFlows() {
		return sequenceFlows;
	}
	
	public void setLayout(Layout layout) {
		this.layout = layout;
	}
	
	public Layout getLayout() {
		return layout;
	}
	
	public FlowNode getSingleFlowNode() {
		return singleFlowNode;
	}
	
	@Override
	public String toString() {
		return sequenceFlows.toString();
	}

	public void clearFlowNodeData() {
		sequenceFlows = null;
		singleFlowNode = null;
	}
}
