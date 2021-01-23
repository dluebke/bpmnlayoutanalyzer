package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.layouttrace;

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
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.helper.trace.MaxTracesReceiver;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.helper.trace.TraceStreamer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.util.CounterMap;

public class LayoutIdentificator implements IBpmnAnalyzer {

	private static final String[] HEADERS = new String[] { "Layout", "TraceLayouts" };
	
	private List<LayoutIdenficatorResult> results = new ArrayList<>();
	private EdgeDirectionEvaluator edgeDirectionEvaluator = new EdgeDirectionEvaluator();
	private EdgeWaypointOptimizer edgeWaypointOptimizer = new EdgeWaypointOptimizer();
	private SequenceFlowForwardBackwardClassifier sequenceFlowClassifier = new SequenceFlowForwardBackwardClassifier();
	private TraceToDiagramLayoutCalulator traceToDiagramLayoutCalulator = new TraceToDiagramLayoutCalulator();
	private TraceStreamer traceStreamer = new TraceStreamer();

	private int traceSearchDepth;

	public LayoutIdentificator() {
		this(1000000);
	}
	
	public LayoutIdentificator(int traceSearchDepth) {
		this.traceSearchDepth = traceSearchDepth;
	}

	@Override
	public void analyze(BpmnProcess p) {
		CounterMap<Layout> traceLayouts = calculateTraceLayouts(p);
		results.add(new LayoutIdenficatorResult(
			p, 
			traceToDiagramLayoutCalulator.calculateDiagramLayout(traceLayouts),
			traceLayouts
		));
	}

	CounterMap<Layout> calculateTraceLayouts(BpmnProcess p) {
		CounterMap<Layout> result = new CounterMap<>();
		Map<SequenceFlow, DirectionType> directions = sequenceFlowClassifier.evaluateDirection(p);
		
		for(FlowNode start : p.getStartFlowNodes()) {
			traceStreamer.streamNonLoopingTracesToEnd(start, new MaxTracesReceiver(traceSearchDepth) {
				
				@Override
				public boolean nextTraceImpl(FlowNode start, Trace t) {
					if(t.hasCompleteLayoutData()) {
						SequenceFlowTrace st;
						if(t.getFlowNodes().size() > 1) {
							st = convertToSequenceFlowTrace(t, directions);
						} else {
							st = new SequenceFlowTrace(t.getFlowNodes().get(0));
						}
						Layout layout = Layout.evaluateLayout(st);
//						st.setLayout(layout);
//						st.clearFlowNodeData(); // make sure to save memory
						result.inc(layout);
					}
					return true;
				}
			});
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
