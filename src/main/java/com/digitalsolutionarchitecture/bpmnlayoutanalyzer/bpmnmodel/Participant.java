package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel;

public class Participant implements RepresentedByShape {
	private String id;
	private Bounds bounds;
	private Boolean isHorizontal;
	
	public Participant(String id) {
		super();
		this.id = id;
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
	}
	
	public Boolean getIsHorizontal() {
		return isHorizontal;
	}
	
	public void setIsHorizontal(Boolean isHorizontal) {
		this.isHorizontal = isHorizontal;
	}
	
}
