package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.layout;

import java.util.Arrays;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.Result;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.util.CounterMap;

public class LayoutIdenficatorResult extends Result {

	private Layout layout;
	private String layouts;
	
	public LayoutIdenficatorResult(BpmnProcess p, Layout layout, CounterMap<Layout> traceLayouts) {
		super(p);
		this.layout = layout;
		this.layouts = traceLayouts.toString();
	}

	@Override
	public List<Object> getValues() {
		return Arrays.asList(layout, layouts.toString());
	}
	
	public Layout getLayout() {
		return layout;
	}

	public String getLayouts() {
		return layouts;
	}
}
