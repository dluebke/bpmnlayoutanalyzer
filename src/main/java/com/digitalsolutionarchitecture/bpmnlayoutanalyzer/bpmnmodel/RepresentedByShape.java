package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel;

public interface RepresentedByShape {

	Bounds getBounds();
	void setBounds(Bounds bounds);
	void clearLayoutData();
	
}
