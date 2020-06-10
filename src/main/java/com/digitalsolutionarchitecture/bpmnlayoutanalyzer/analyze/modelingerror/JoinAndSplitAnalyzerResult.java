package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.modelingerror;

import java.util.Arrays;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.Result;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;

class JoinAndSplitAnalyzerResult extends Result {

	static final String[] HEADERS = new String[] {
		"SplitElement", "Path", "PatternSize"
	};


	public JoinAndSplitAnalyzerResult(BpmnProcess p) {
		super(p);
	}

	
	@Override
	public List<Object> getValues() {
		return Arrays.asList(
		);
	}
}
