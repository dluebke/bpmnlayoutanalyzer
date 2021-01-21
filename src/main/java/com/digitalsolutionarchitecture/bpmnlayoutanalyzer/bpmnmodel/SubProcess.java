package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel;

public class SubProcess extends FlowNode {

	private BpmnProcess process;
	private boolean triggeredByEvent;
	
	public SubProcess(String id, String type, BpmnProcess parent, String name, boolean triggeredByEvent) {
		super(id, type, parent, name);
		this.triggeredByEvent = triggeredByEvent;
	}

	public BpmnProcess getProcess() {
		return process;
	}
	
	public void setProcess(BpmnProcess process) {
		this.process = process;
	}
	
	@Override
	public void clearLayoutData() {
		super.clearLayoutData();
		
		if(process != null) {
			process.clearLayoutData();
		}
	}
	
	public boolean isTriggeredByEvent() {
		return triggeredByEvent;
	}
}
