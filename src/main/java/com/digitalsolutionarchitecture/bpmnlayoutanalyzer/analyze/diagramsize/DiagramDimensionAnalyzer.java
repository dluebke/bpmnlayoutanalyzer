package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.diagramsize;

import java.util.ArrayList;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.Bounds;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;

public class DiagramDimensionAnalyzer implements IBpmnAnalyzer {

	private List<DiagramDimensionResult> results = new ArrayList<>();
	
	public DiagramDimensionAnalyzer() {
	}
	
	@Override
	public void analyze(BpmnProcess p) {
		Bounds diagramBounds = null;
		
		for(FlowNode fn : p.getFlowNodes()) {
			if (fn.hasLayoutData()) {
				if(diagramBounds == null) {
					diagramBounds = new Bounds(fn.getBounds());
				} else {
					diagramBounds.extendTo(fn.getBounds());
				}
			}
		}
		
		if (diagramBounds != null) {
			results.add(
					new DiagramDimensionResult(
							p,
							diagramBounds
							)
					);
		}
	}

	@Override
	public String getShortName() {
		return "dimensions";
	}
	
	@Override
	public List<DiagramDimensionResult> getResults() {
		return results;
	}
	
	@Override
	public String[] getHeaders() {
		return DiagramDimensionResult.HEADERS;
	}
}
