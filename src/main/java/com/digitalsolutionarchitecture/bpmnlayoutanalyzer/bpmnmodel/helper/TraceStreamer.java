package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.Trace;

public class TraceStreamer {

	@SuppressWarnings("serial")
	private static final class StreamAborted extends Exception {

	}

	public List<Trace> getNonLoopingTracesToEnd(FlowNode fn) {
		final List<Trace> result = new ArrayList<>();
		
		streamNonLoopingTracesToEnd(fn, new ITraceReceiver() {
			@Override
			public boolean nextTrace(FlowNode start, com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.Trace t) {
				result.add(t);
				return true;
			}
		});
		
		return result;
	}

	/**
	 * Returns true if operation suceeded, false if operation was aborted
	 * 
	 * @param fn
	 * @param r
	 * @return
	 */
	public boolean streamNonLoopingTracesToEnd(FlowNode fn, ITraceReceiver r) {
		Trace currentTrace = new Trace();
		currentTrace.addFlowNodeToTrace(fn);
		try {
			getNonLoopingTracesToEnd(fn, currentTrace, r);
			return true;
		} catch (StreamAborted e) {
			return false;
		}
	}

	private void getNonLoopingTracesToEnd(FlowNode start, Trace currentTrace, ITraceReceiver r) throws StreamAborted {
		FlowNode current = currentTrace.last();
		for(FlowNode following : current.getOutgoingSequenceFlows().stream().map(x -> x.getTarget()).collect(Collectors.toList())) {
			if(!currentTrace.contains(following)) {
				Trace newTrace = new Trace(currentTrace);
				newTrace.addFlowNodeToTrace(following);
				getNonLoopingTracesToEnd(start, newTrace, r);
			}
		}
		
		for(FlowNode boundaryEvent : current.getBoundaryEvents()) {
			if(!currentTrace.contains(boundaryEvent)) {
				Trace newTrace = new Trace(currentTrace);
				newTrace.addFlowNodeToTrace(boundaryEvent);
				getNonLoopingTracesToEnd(start, newTrace, r);
			}
		}
		
		if(current.getOutgoingSequenceFlows().size() == 0) {
			if(!r.nextTrace(start, currentTrace)) {
				throw new StreamAborted();
			}
		}
	}
	
}
