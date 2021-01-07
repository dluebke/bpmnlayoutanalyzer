package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel;

public class Participant implements RepresentedByShape {
	private String id;
	private Bounds bounds;
	private Boolean isHorizontal;
	private BpmnProcess process;
	private String processId;
	
	public Participant(String id, String processId) {
		super();
		this.id = id;
		this.processId = processId;
	}

	public String getId() {
		return id;
	}
	
	@Override
	public Bounds getBounds() {
		return bounds;
	}
	
	@Override
	public void setBounds(Bounds bounds) {
		this.bounds = bounds;
	}
	
	@Override
	public void clearLayoutData() {
		bounds = null;
		isHorizontal = null;
		if(process != null) {
			process.clearLayoutData();
		}
	}
	
	public Boolean getIsHorizontal() {
		return isHorizontal;
	}
	
	public void setIsHorizontal(Boolean isHorizontal) {
		this.isHorizontal = isHorizontal;
	}

	@Override
	public boolean hasLayoutData() {
		return bounds != null;
	}
	
	public BpmnProcess getProcess() {
		return process;
	}
	
	public void setProcess(BpmnProcess process) {
		this.process = process;
	}
	
	public String getProcessId() {
		return processId;
	}
}
