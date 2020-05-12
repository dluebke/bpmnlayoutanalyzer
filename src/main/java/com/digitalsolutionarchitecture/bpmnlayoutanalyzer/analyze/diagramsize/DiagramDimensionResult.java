package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.diagramsize;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.Result;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.Bounds;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;

class DiagramDimensionResult extends Result {

	public DiagramDimensionResult(BpmnProcess process, Bounds bounds) {
		super(process);
		this.bounds = bounds;
	}
	
	final Bounds bounds;
}