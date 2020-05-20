package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.pattern;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.Result;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;

class ControlFlowPatternAnalyzerResult extends Result {

	private String pattern;
	private String patternClassification;
	private int patternSize;
	
	public ControlFlowPatternAnalyzerResult(BpmnProcess p, String pattern, String patternClassification, int patternSize) {
		super(p);
		this.pattern = pattern;
		this.patternClassification = patternClassification;
		this.patternSize = patternSize;
	}
	
	public String getPattern() {
		return pattern;
	}
	
	public String getPatternClassification() {
		return patternClassification;
	}
	
	public int getPatternSize() {
		return patternSize;
	}
}
