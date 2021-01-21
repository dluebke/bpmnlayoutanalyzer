package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.Result;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges.DirectionType;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges.EdgeDirection;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges.EdgeDirectionEvaluator;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges.EdgeWaypointOptimizer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges.SequenceFlowForwardBackwardClassifier;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.SequenceFlow;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.Trace;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.WayPoint;

public class LayoutIdentificator implements IBpmnAnalyzer {

	private static final String[] HEADERS = new String[] { "Layout" };
	
	private List<LayoutIdenficatorResult> results = new ArrayList<>();
	private EdgeDirectionEvaluator edgeDirectionEvaluator = new EdgeDirectionEvaluator();
	private EdgeWaypointOptimizer edgeWaypointOptimizer = new EdgeWaypointOptimizer();
	private SequenceFlowForwardBackwardClassifier sequenceFlowClassifier = new SequenceFlowForwardBackwardClassifier();
	private TraceToDiagramLayoutCalulator traceToDiagramLayoutCalulator = new TraceToDiagramLayoutCalulator();
	
	@Override
	public void analyze(BpmnProcess p) {
		List<SequenceFlowTrace> traces = extractSequenceFlowTraces(p);
		List<Layout> tracesLayouts = traces.stream().map(x -> Layout.evaluateLayout(x)).collect(Collectors.toList());
		results.add(new LayoutIdenficatorResult(p, traceToDiagramLayoutCalulator.calculateDiagramLayout(tracesLayouts)));
	}

	List<SequenceFlowTrace> extractSequenceFlowTraces(BpmnProcess p) {
		List<SequenceFlowTrace> result = new ArrayList<>();
		Map<SequenceFlow, DirectionType> directions = sequenceFlowClassifier.evaluateDirection(p);
		
		for(FlowNode start : p.getStartFlowNodes()) {
			for(Trace t: start.getNonLoopingTracesToEnd()) {
				if(t.hasCompleteLayoutData()) {
					if(t.getFlowNodes().size() > 1) {
						result.add(convertToSequenceFlowTrace(t, directions));
					} else {
						result.add(new SequenceFlowTrace(t.getFlowNodes().get(0)));
					}
				}
			}
		}
		
		return result;
	}

	private SequenceFlowTrace convertToSequenceFlowTrace(Trace t, Map<SequenceFlow, DirectionType> directions) {
		SequenceFlowTrace result = new SequenceFlowTrace();
		
		List<FlowNode> flowNodes = t.getFlowNodes();
		for(int i = 0; i < flowNodes.size() - 1; i++) {
			FlowNode n1 = flowNodes.get(i);
			FlowNode n2 = flowNodes.get(i + 1);
			
			SequenceFlow sf = n1.getSequenceFlowTo(n2);
			List<WayPoint> waypoints = new ArrayList<>(sf.getWayPoints().getWaypoints());
			edgeWaypointOptimizer.optimize(waypoints);
			EdgeDirection arcType = edgeDirectionEvaluator.evaluateArcType(waypoints);
			DirectionType directionType = directions.get(sf);
			
			result.getSequenceFlows().add(new SequenceFlowNode(sf, directionType == DirectionType.BACKWARD, arcType));
		}
		
		return result;
	}

	@Override
	public String getShortName() {
		return "layout";
	}

	@Override
	public List<? extends Result> getResults() {
		return results;
	}

	@Override
	public String[] getHeaders() {
		return HEADERS;
	}

}