package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.connectedness;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.SequenceFlow;

public class ConnectednessAnalyzer implements IBpmnAnalyzer {

	private List<ConnectednessAnalyzerResult> results = new ArrayList<>();

	@Override
	public void analyze(BpmnProcess processWithDiagramData) {
		
		List<FlowNode> startFlowNodes = processWithDiagramData.getStartFlowNodes();
		List<Subgraph> subgraphs = calculateSubgraphs(startFlowNodes, processWithDiagramData);
		Set<FlowNode> endFlowNodes = calculateEndFlowNodes(subgraphs);
		Connectedness connectedness = subgraphs.size() == 1 ? Connectedness.SINGLE_PROCESS : Connectedness.MULTIPLE_PROCESSES;

		Set<StartAndEndConstellation> startAndEnds = calculateStartAndEndConstellations(subgraphs);
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

	private Set<FlowNode> calculateEndFlowNodes(List<Subgraph> subgraphs) {
		Set<FlowNode> result = new HashSet<>();
		
		for(Subgraph sg : subgraphs) {
			result.addAll(sg.getEndFlowNodes());
		}
		
		return result;
	}

	private List<Subgraph> calculateSubgraphs(List<FlowNode> startFlowNodes, BpmnProcess p) {
		final List<Subgraph> result = new ArrayList<>();
		List<FlowNode> starts = new ArrayList<>(startFlowNodes);
		
		while(starts.size() > 0) {
			Subgraph currentSubgraph = new Subgraph();
			result.add(currentSubgraph);
			
			List<FlowNode> elementsToProcess = new ArrayList<>();
			elementsToProcess.add(starts.get(starts.size() - 1));
			
			while(!elementsToProcess.isEmpty()) {
				FlowNode currentElement = elementsToProcess.remove(elementsToProcess.size() - 1);
				starts.remove(currentElement);
				currentSubgraph.add(currentElement);
				currentSubgraph.addAll(currentElement.getBoundaryEvents());
				
				for(SequenceFlow sf : currentElement.getIncomingSequenceFlows()) {
					FlowNode possibleNextNode = sf.getSource();
					if(!currentSubgraph.contains(possibleNextNode) && !elementsToProcess.contains(possibleNextNode)) {
						elementsToProcess.add(possibleNextNode);
					}
				}
				for(SequenceFlow sf : currentElement.getOutgoingSequenceFlows()) {
					FlowNode possibleNextNode = sf.getTarget();
					if(!currentSubgraph.contains(possibleNextNode) && !elementsToProcess.contains(possibleNextNode)) {
						elementsToProcess.add(possibleNextNode);
					}
				}
			}
		}
		
		return result;
	}

	private Set<StartAndEndConstellation> calculateStartAndEndConstellations(List<Subgraph> subgraphs) {
		Set<StartAndEndConstellation> startAndEnds = new HashSet<>();
		for(Subgraph sg : subgraphs) {
			for(FlowNode start : sg.getStartFlowNodes()) {
				if(start.getType().endsWith("startEvent")) {
					if(areAllEndEvents(sg)) {
						startAndEnds.add(StartAndEndConstellation.EVENTS);
					} else {
						startAndEnds.add(StartAndEndConstellation.INCORRECT);
					}
				} else {
					if(containsNoEndEvents(sg)) {
						startAndEnds.add(StartAndEndConstellation.NO_EVENTS);
					} else {
						startAndEnds.add(StartAndEndConstellation.INCORRECT);
					}
				}
			}
		}
		
		return startAndEnds;
	}

	private boolean containsNoEndEvents(Subgraph sg) {
		for(FlowNode fn : sg.getEndFlowNodes()) {
			if(fn.getType().equals("endEvent")) {
				return false;
			}
		}
		return true;
	}

	private boolean areAllEndEvents(Subgraph sg) {
		for(FlowNode fn : sg.getEndFlowNodes()) {
			if(!fn.getType().equals("endEvent")) {
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
