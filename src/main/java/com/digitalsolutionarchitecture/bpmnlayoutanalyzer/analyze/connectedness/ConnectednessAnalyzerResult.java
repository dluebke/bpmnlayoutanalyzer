package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.connectedness;

import java.util.Arrays;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.Result;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;

class ConnectednessAnalyzerResult extends Result {

	private Connectedness connectedness;
	private StartAndEndConstellation startAndEnd;
	
	public ConnectednessAnalyzerResult(BpmnProcess p, Connectedness connectedness, StartAndEndConstellation startAndEnd) {
		super(p);
		this.connectedness = connectedness;
		this.startAndEnd = startAndEnd;
	}

	public static final String[] HEADERS = new String[] { "Connectedness", "StartAndEnd" };

	@Override
	public List<Object> getValues() {
		return Arrays.asList(connectedness, startAndEnd);
	}

}
