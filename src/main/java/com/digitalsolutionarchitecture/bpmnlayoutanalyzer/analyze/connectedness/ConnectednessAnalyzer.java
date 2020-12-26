package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.connectedness;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.Trace;

public class ConnectednessAnalyzer implements IBpmnAnalyzer {

	private List<ConnectednessAnalyzerResult> results = new ArrayList<>();

	@Override
	public void analyze(BpmnProcess processWithDiagramData) {
		
		List<FlowNode> startFlowNodes = processWithDiagramData.getStartFlowNodes();
		
		Connectedness connectedness = startFlowNodes.size() == 1 ? Connectedness.SINGLE_PROCESS : Connectedness.MULTIPLE_PROCESSES;

		Set<StartAndEndConstellation> startAndEnds = new HashSet<>();
		for(FlowNode start : startFlowNodes) {
			if(start.getType().endsWith("startEvent")) {
				if(isAlwaysEndingInEndEvents(start)) {
					startAndEnds.add(StartAndEndConstellation.EVENTS);
				} else {
					startAndEnds.add(StartAndEndConstellation.INCORRECT);
				}
			} else {
				if(isSometimesEndingInEndEvents(start)) {
					startAndEnds.add(StartAndEndConstellation.INCORRECT);
				} else {
					startAndEnds.add(StartAndEndConstellation.NO_EVENTS);
				}
			}
		}
		
		StartAndEndConstellation startAndEnd = startAndEnds.size() == 1 ? startAndEnds.iterator().next() : 
			startAndEnds.contains(StartAndEndConstellation.INCORRECT) ? StartAndEndConstellation.INCORRECT : StartAndEndConstellation.SOMETIMES_EVENTS_SOMETIMES_NOEVENTS;
		
		results.add(new ConnectednessAnalyzerResult(processWithDiagramData, connectedness, startAndEnd));
	}

	private boolean isSometimesEndingInEndEvents(FlowNode start) {
		List<Trace> traces = start.getNonLoopingTracesToEnd();
		for(Trace t : traces) {
			if(t.last().getType().equals("endEvent")) {
				return true;
			}
	
		}
		return false;
	}

	private boolean isAlwaysEndingInEndEvents(FlowNode start) {
		List<Trace> traces = start.getNonLoopingTracesToEnd();
		for(Trace t : traces) {
			if(!t.last().getType().equals("endEvent")) {
				return false;
			}
	
		}
		return true;
	}

	@Override
	public String getShortName() {
		return "connectedness";
	}

	@Override
	public List<ConnectednessAnalyzerResult> getResults() {
		return results;
	}
	
	@Override
	public String[] getHeaders() {
		return ConnectednessAnalyzerResult.HEADERS;
	}
}
