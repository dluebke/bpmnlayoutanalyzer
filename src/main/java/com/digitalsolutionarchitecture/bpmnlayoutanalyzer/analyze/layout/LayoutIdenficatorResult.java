package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.layout;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.Result;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;

public class LayoutIdenficatorResult extends Result {

	private Layout layout;
	private Set<Layout> layouts;
	
	public LayoutIdenficatorResult(BpmnProcess p, Layout layout, List<SequenceFlowTrace> traces) {
		super(p);
		this.layout = layout;
		this.layouts = traces.stream().map(x -> x.getLayout()).collect(Collectors.toSet());
	}

	@Override
	public List<Object> getValues() {
		return Arrays.asList(layout, layouts.toString());
	}
	
	public Layout getLayout() {
		return layout;
	}

	public Set<Layout> getLayouts() {
		return layouts;
	}
}
