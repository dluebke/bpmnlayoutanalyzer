package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.helper;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.Trace;

public interface ITraceReceiver {

	/**
	 * 
	 * @param start start flownode with which the trace search was initiated
	 * @param t found trace
	 * @return true, if to proceed with next trace, false if stream should be aborted
	 */
	public boolean nextTrace(FlowNode start, Trace t);
	
}
