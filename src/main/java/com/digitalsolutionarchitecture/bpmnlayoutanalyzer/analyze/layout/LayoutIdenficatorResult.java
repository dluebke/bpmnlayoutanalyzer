package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.layout;

import java.util.Arrays;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.Result;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;

public class LayoutIdenficatorResult extends Result {

	private Layout layout;
	
	public LayoutIdenficatorResult(BpmnProcess p, Layout layout) {
		super(p);
		this.layout = layout;
	}

	@Override
	public List<Object> getValues() {
		return Arrays.asList(layout);
	}
	
	public Layout getLayout() {
		return layout;
	}

}
