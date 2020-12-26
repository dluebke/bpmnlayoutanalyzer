package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze;

import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;

public interface IBpmnAnalyzer {

	public void analyze(BpmnProcess processWithDiagramData);

	public String getShortName();
	
	public List<?extends Result> getResults();

	public String[] getHeaders();
}
