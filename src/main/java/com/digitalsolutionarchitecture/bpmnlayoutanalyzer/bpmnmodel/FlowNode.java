package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel;

import java.util.ArrayList;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.util.StringUtil;

public class FlowNode implements RepresentedByShape {

	@SuppressWarnings("serial")
	public static class TooManyTraces extends Exception {

		private final List<Trace> result;

		public TooManyTraces(List<Trace> result) {
			this.result = result;
		}
		
		public List<Trace> getFirstPartOfResult() {
			return result;
		}

	}

	private String id;
	private String type;
	
	private List<SequenceFlow> outgoingSequenceFlows = new ArrayList<>();
	private List<SequenceFlow> incomingSequenceFlows = new ArrayList<>();
	private List<FlowNode> boundaryEvents = new ArrayList<>();
	private FlowNode attachedTo;
	private Boolean cancelActivity;
	private String eventType;
	private String name;
	
	private Bounds bounds;
	private BpmnProcess parent;
	
	public FlowNode(String id, String type, BpmnProcess parent, String name) {
		this.id = id;
		this.type = type;
		this.parent = parent;
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

	public FlowNode getAttachedTo() {
		return attachedTo;
	}
	
	public void setAttachedTo(FlowNode attachedTo) {
		this.attachedTo = attachedTo;
	}

	public void addBoundaryEvent(FlowNode fn) {
		boundaryEvents.add(fn);
	}
	
	public List<FlowNode> getBoundaryEvents() {
		return boundaryEvents;
	}
	
	@Override
	public boolean hasLayoutData() {
		return bounds != null;
	}

	public void setCancelActivity(Boolean cancelActivity) {
		this.cancelActivity = cancelActivity;
	}
	
	public Boolean isCancelActivity() {
		return cancelActivity;
	}
	
	public String getEventType() {
		return eventType;
	}
	
	public void setEventType(String eventType) {
		this.eventType = eventType.replace("EventDefinition", "");
	}

	public String getTypeAndEventType() {
		if(type.endsWith("Event")) {
			String interrupting = "";
			if(type.equals("boundaryEvent")) {
				if(this.cancelActivity != null) {
					interrupting = this.cancelActivity ? "Interrupting" : "NonInterrupting";
				}
			}
			String et = eventType != null ? StringUtil.toFirstUpper(eventType) : "None";
			return interrupting + et + StringUtil.toFirstUpper(type);
		} else {
			return StringUtil.toFirstUpper(type); // for send/receive tasks
		}
	}
	
	@Override
	public String toString() {
		return getId() + "@" + getType() + (name != null && !name.equals("") ? "[@name=" + name + "]" : "");
	}

	public void addOutgoingSequenceFlow(SequenceFlow sequenceFlow) {
		if(!outgoingSequenceFlows.contains(sequenceFlow)) {
			outgoingSequenceFlows.add(sequenceFlow);
		}
		
	}

	public void addIncomingSequenceFlows(SequenceFlow sequenceFlow) {
		if(!incomingSequenceFlows.contains(sequenceFlow)) {
			incomingSequenceFlows.add(sequenceFlow);
		}
	}
	
	public BpmnProcess getParent() {
		return parent;
	}

	public String getName() {
		return name;
	}
	
	public SequenceFlow getSequenceFlowTo(FlowNode n) {
		for(SequenceFlow sf : getOutgoingSequenceFlows()) {
			if(sf.getTarget() == n) {
				return sf;
			}
		}
		return null;
	}
	
}
