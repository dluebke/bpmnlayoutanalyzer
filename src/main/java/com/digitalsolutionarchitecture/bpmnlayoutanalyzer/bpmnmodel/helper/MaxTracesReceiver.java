package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.helper;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.Trace;

public abstract class MaxTracesReceiver implements ITraceReceiver {

	private int maxTraces;
	private int currentTraceCount = 0;

	public MaxTracesReceiver(int maxTraces) {
		this.maxTraces = maxTraces;
	}
	
	@Override
	public final boolean nextTrace(FlowNode start, Trace t) {
		currentTraceCount++;
		
		return nextTraceImpl(start, t) && currentTraceCount < maxTraces;
	}
	
	protected abstract boolean nextTraceImpl(FlowNode start, Trace t);	
}
