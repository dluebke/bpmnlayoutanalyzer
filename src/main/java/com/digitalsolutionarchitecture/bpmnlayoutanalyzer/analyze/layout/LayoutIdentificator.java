package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzer;
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

	private static final String[] HEADERS = new String[] { "Layout", "TraceLayouts" };
	
	private List<LayoutIdenficatorResult> results = new ArrayList<>();
	private EdgeDirectionEvaluator edgeDirectionEvaluator = new EdgeDirectionEvaluator();
	private EdgeWaypointOptimizer edgeWaypointOptimizer = new EdgeWaypointOptimizer();
	private SequenceFlowForwardBackwardClassifier sequenceFlowClassifier = new SequenceFlowForwardBackwardClassifier();
	private TraceToDiagramLayoutCalulator traceToDiagramLayoutCalulator = new TraceToDiagramLayoutCalulator();
	
	@Override
	public void analyze(BpmnProcess p) {
		List<SequenceFlowTrace> traces = extractSequenceFlowTraces(p);
		evaluateLayoutForTraces(traces);
		results.add(new LayoutIdenficatorResult(
			p, 
			traceToDiagramLayoutCalulator.calculateDiagramLayout(traces),
			traces
		));
	}

	void evaluateLayoutForTraces(List<SequenceFlowTrace> traces) {
		for(SequenceFlowTrace t : traces) {
			t.setLayout(Layout.evaluateLayout(t));
		}
	}

	List<SequenceFlowTrace> extractSequenceFlowTraces(BpmnProcess p) {
		List<SequenceFlowTrace> result = new ArrayList<>();
		Map<SequenceFlow, DirectionType> directions = sequenceFlowClassifier.evaluateDirection(p);
		
		for(FlowNode start : p.getStartFlowNodes()) {
			for(Trace t : start.getNonLoopingTracesToEnd()) {
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
			EdgeDirection arcType;
			DirectionType directionType;
			if(sf != null) {
				List<WayPoint> waypoints = new ArrayList<>(sf.getWayPoints().getWaypoints());
				edgeWaypointOptimizer.optimize(waypoints);
				arcType = edgeDirectionEvaluator.evaluateArcType(waypoints);
				directionType = directions.get(sf);
			} else if(n2.getAttachedTo() == n1) {
				arcType = EdgeDirection.BOUNDARY;
				directionType = DirectionType.FORWARD;
			} else {
				throw new RuntimeException("Cannot find connection between " + n1 + " and " + n2);
			}
			
			result.getSequenceFlows().add(new SequenceFlowNode(n1, n2, directionType == DirectionType.BACKWARD, arcType));
		}
		
		return result;
	}

	@Override
	public String getShortName() {
		return "layout";
	}

	@Override
	public List<LayoutIdenficatorResult> getResults() {
		return results;
	}

	@Override
	public String[] getHeaders() {
		return HEADERS;
	}

}
