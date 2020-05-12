package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel;

public class SequenceFlow implements RepresentedByWayPoints {

	private String id;
	private FlowNode source;
	private FlowNode target;
	
	private WayPointList wayPoints;
	
	public SequenceFlow(String id) {
		this(id, null, null);
	}
	
	public SequenceFlow(String id, FlowNode source, FlowNode target) {
		this.id = id;
		setSource(source);
		setTarget(target);
	}

	public String getId() {
		return id;
	}
	
	public FlowNode getSource() {
		return source;
	}
	
	public void setSource(FlowNode source) {
		if(this.source != null) {
			this.source.getOutgoingSequenceFlows().remove(this);
		}
		this.source = source;
	}
	
	public FlowNode getTarget() {
		return target;
	}
	
	public void setTarget(FlowNode target) {
		if(this.target != null) {
			this.target.getIncomingSequenceFlows().remove(this);
		}
		this.target = target;
	}

	public WayPointList getWayPoints() {
		return wayPoints;
	}
	
	public void setWayPoints(WayPointList wayPoints) {
		this.wayPoints = wayPoints;
	}
	
	@Override
	public void clearLayoutData() {
		wayPoints = null;
	}
}
