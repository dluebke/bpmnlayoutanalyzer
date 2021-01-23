package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.helper.subgraph;

import java.util.ArrayList;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.SequenceFlow;

public class SubgraphCalculator {

	public List<Subgraph> calculateSubgraphs(List<FlowNode> startFlowNodes, BpmnProcess p) {
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
	
}
