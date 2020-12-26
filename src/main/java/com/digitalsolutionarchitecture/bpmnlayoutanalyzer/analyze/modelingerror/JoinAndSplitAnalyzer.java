package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.modelingerror;

import java.util.ArrayList;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;

public class JoinAndSplitAnalyzer implements IBpmnAnalyzer {

	private List<JoinAndSplitAnalyzerResult> results = new ArrayList<>();

	@Override
	public void analyze(BpmnProcess processWithDiagramData) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getShortName() {
		return "wrongjoinandsplit";
	}
	
	@Override
	public List<JoinAndSplitAnalyzerResult> getResults() {
		return results;
	}
	
	@Override
	public String[] getHeaders() {
		return JoinAndSplitAnalyzerResult.HEADERS;
	}
}
