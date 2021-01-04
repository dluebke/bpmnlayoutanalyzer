package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.diagramsize;

import java.util.Arrays;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.Result;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.Bounds;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;

class DiagramDimensionResult extends Result {

	static final String[] HEADERS = new String[] {
		"MinX", "MinY", "MaxWidth", "MaxHeight", "Area"
	};

	public DiagramDimensionResult(BpmnProcess process, Bounds bounds) {
		super(process);
		this.bounds = bounds;
	}
	
	final Bounds bounds;
	
	@Override
	public List<Object> getValues() {
		return Arrays.asList(
			bounds.getX(), 
			bounds.getY(), 
			bounds.getWidth(), 
			bounds.getHeight(),
			bounds.getArea()
		);
	}
	
	public Bounds getBounds() {
		return bounds;
	}
}