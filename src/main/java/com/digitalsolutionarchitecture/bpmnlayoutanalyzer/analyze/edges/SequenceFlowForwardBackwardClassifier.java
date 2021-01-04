package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.SequenceFlow;

public class SequenceFlowForwardBackwardClassifier {

	public Map<SequenceFlow, DirectionType> evaluateDirection(BpmnProcess p) {
		Map<SequenceFlow, DirectionType> result = new HashMap<>();
		
		List<FlowNode> nodesToProcess = p.getStartFlowNodes();
		List<FlowNode> nodesToProcessInNextStep;
		Set<FlowNode> processedFlowNodes = new HashSet<>();
		Set<FlowNode> seenFlowNodes = new HashSet<>();
		
		while(nodesToProcess.size() > 0) {
			nodesToProcessInNextStep = new ArrayList<>();
			for(FlowNode n : nodesToProcess) {
				processedFlowNodes.add(n);
				seenFlowNodes.remove(n);
				for(SequenceFlow sf : n.getOutgoingSequenceFlows()) {
					if(!result.containsKey(sf)) {
						result.put(sf, DirectionType.FORWARD);
					}
					
					FlowNode target = sf.getTarget();
					if(!processedFlowNodes.contains(target)) {
						if(allIncomingAreContained(result.keySet(), target)) {
							nodesToProcessInNextStep.add(target);
							seenFlowNodes.remove(target);
						} else {
							seenFlowNodes.add(target);
						}
					}
				}
			}
			
			if(nodesToProcessInNextStep.size() == 0 && seenFlowNodes.size() > 0) {
				FlowNode firstFlowNode = seenFlowNodes.iterator().next();
				markAllRemainingIncomingSequenceFlowsAsBackwards(firstFlowNode, result, seenFlowNodes);
				nodesToProcessInNextStep.add(firstFlowNode);
				seenFlowNodes.remove(firstFlowNode);
			}
			nodesToProcess = nodesToProcessInNextStep;
		}
		
		return result;
	}

	private FlowNode markAllRemainingIncomingSequenceFlowsAsBackwards(FlowNode firstFlowNode, Map<SequenceFlow, DirectionType> result,
			Set<FlowNode> seenFlowNodes) {
		
		for(SequenceFlow sf : firstFlowNode.getIncomingSequenceFlows()) {
			if(!result.containsKey(sf)) {
				result.put(sf, DirectionType.BACKWARD);
			}
		}
		return firstFlowNode;
	}

	private boolean allIncomingAreContained(Set<SequenceFlow> sequenceFlows, FlowNode n) {
		for(SequenceFlow sf : n.getIncomingSequenceFlows()) {
			if(!sequenceFlows.contains(sf)) {
				return false;
			}
		}
		return true;
	}
	
}
