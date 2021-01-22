package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.connectedness;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.Trace;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.helper.MaxTracesReceiver;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.helper.TraceStreamer;

public class ConnectednessAnalyzer implements IBpmnAnalyzer {

	private List<ConnectednessAnalyzerResult> results = new ArrayList<>();
	private TraceStreamer traceStreamer = new TraceStreamer();
	private int traceSearchDepth;

	public ConnectednessAnalyzer() {
		this(1000000);
	}
	
	public ConnectednessAnalyzer(int traceSearchDepth) {
		this.traceSearchDepth = traceSearchDepth;
	}

	@Override
	public void analyze(BpmnProcess processWithDiagramData) {
		
		List<FlowNode> startFlowNodes = processWithDiagramData.getStartFlowNodes();
		List<Subgraph> subgraphs = calculateSubgraphs(startFlowNodes);
		Set<FlowNode> endFlowNodes = calculateEndFlowNodes(startFlowNodes);
		Connectedness connectedness = subgraphs.size() == 1 ? Connectedness.SINGLE_PROCESS : Connectedness.MULTIPLE_PROCESSES;

		Set<StartAndEndConstellation> startAndEnds = calculateStartAndEndConstellations(startFlowNodes);
		StartAndEndConstellation startAndEnd = startAndEnds.size() == 1 ? startAndEnds.iterator().next() : 
			startAndEnds.contains(StartAndEndConstellation.INCORRECT) ? StartAndEndConstellation.INCORRECT : StartAndEndConstellation.SOMETIMES_EVENTS_SOMETIMES_NOEVENTS;
		
		int startFlowNodeCount = startFlowNodes.size();
		int endFlowNodeCount = endFlowNodes.size();
		int subgraphCount = subgraphs.size();
		
		String[] startFlowNodeIds = startFlowNodes.stream().map(x -> x.getId()).collect(Collectors.toList()).toArray(new String[0]);
		results.add(new ConnectednessAnalyzerResult(
			processWithDiagramData, 
			connectedness, 
			startAndEnd, 
			startFlowNodeCount, 
			endFlowNodeCount, 
			subgraphCount,
			String.join(";", startFlowNodeIds)
		));
	}

	private Set<FlowNode> calculateEndFlowNodes(List<FlowNode> startFlowNodes) {
		final Set<FlowNode> result = new HashSet<>();
		
		for(FlowNode start : startFlowNodes) {
			traceStreamer.streamNonLoopingTracesToEnd(start, new MaxTracesReceiver(traceSearchDepth) {
				
				@Override
				public boolean nextTraceImpl(FlowNode start, Trace t) {
					result.add(t.last());
					return true;
				}
			});
		}
		
		return result;
	}

	private List<Subgraph> calculateSubgraphs(List<FlowNode> startFlowNodes) {
		final List<Subgraph> result = new ArrayList<>();
		
		for(FlowNode start : startFlowNodes) {
			traceStreamer.streamNonLoopingTracesToEnd(start, new MaxTracesReceiver(traceSearchDepth) {
				
				@Override
				public boolean nextTraceImpl(FlowNode start, Trace t) {
					boolean found = false;
					for(Subgraph s : result) {
						if(s.containsTrace(t)) {
							s.add(t);
							found = true;
							break;
						}
					}
					
					if(!found) {
						Subgraph s = new Subgraph();
						s.add(t);
						result.add(s);
					}
					return true;
				}
			});
		}
		
		return result;
	}

	private Set<StartAndEndConstellation> calculateStartAndEndConstellations(List<FlowNode> startFlowNodes) {
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
		return startAndEnds;
	}

	private boolean isSometimesEndingInEndEvents(FlowNode start) {
		
		return !traceStreamer.streamNonLoopingTracesToEnd(start, new MaxTracesReceiver(traceSearchDepth) {
			@Override
			public boolean nextTraceImpl(FlowNode start, Trace t) {
				return !t.last().getType().equals("endEvent");
			}
		});
	}

	private boolean isAlwaysEndingInEndEvents(FlowNode start) {
		return traceStreamer.streamNonLoopingTracesToEnd(start, new MaxTracesReceiver(traceSearchDepth) {
			@Override
			public boolean nextTraceImpl(FlowNode start, Trace t) {
				return t.last().getType().equals("endEvent");
			}
		});
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
