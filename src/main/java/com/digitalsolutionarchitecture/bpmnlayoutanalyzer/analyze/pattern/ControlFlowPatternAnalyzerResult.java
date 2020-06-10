package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.pattern;

import java.util.Arrays;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.Result;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;

class ControlFlowPatternAnalyzerResult extends Result {

	static final String[] HEADERS = new String[] {
			"Pattern", "PatternClassification", "PatternSize"
	};
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
	
	@Override
	public List<Object> getValues() {
		return Arrays.asList(
			getPattern(),
			getPatternClassification(),
			getPatternSize()
		);
	}
}
