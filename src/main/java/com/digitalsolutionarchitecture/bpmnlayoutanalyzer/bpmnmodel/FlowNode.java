package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel;

import java.util.ArrayList;
import java.util.List;

public class FlowNode implements RepresentedByShape {

	private String id;
	private String type;
	
	private List<SequenceFlow> outgoingSequenceFlows = new ArrayList<>();
	private List<SequenceFlow> incomingSequenceFlows = new ArrayList<>();
	
	private Bounds bounds;
	
	public FlowNode(String id, String type) {
		this.id = id;
		this.type = type;
	}

	@Override
	public void clearLayoutData() {
		bounds = null;
	}

	@Override
	public Bounds getBounds() {
		return bounds;
	}
	
	@Override
	public void setBounds(Bounds bounds) {
		this.bounds = bounds;
	}
	
	public String getId() {
		return id;
	}

	public List<SequenceFlow> getIncomingSequenceFlows() {
		return incomingSequenceFlows;
	}

	public List<SequenceFlow> getOutgoingSequenceFlows() {
		return outgoingSequenceFlows;
	}
	
	public String getType() {
		return type;
	}
	
}
