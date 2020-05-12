package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.SequenceFlow;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.WayPoint;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.output.CsvWriter;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.output.CsvWriterOptions;

public class SequenceFlowReporter implements IBpmnAnalyzer {

	private List<SequenceFlowReportItem> results = new ArrayList<>();
	private EdgeDirectionEvaluator edgeDirectionEvaluator = new EdgeDirectionEvaluator();
	private EdgeWaypointOptimizer edgeWaypointOptimizer = new EdgeWaypointOptimizer();
	
	@Override
	public void analyze(BpmnProcess p) {
		for(SequenceFlow sf : p.getSequenceFlows()) {
			if(sf.getWayPoints() != null) {
				try {
					SequenceFlowReportItem result = new SequenceFlowReportItem(p);
					result.id = sf.getId();
					result.sourceId = sf.getSource().getId();
					result.sourceType = sf.getSource().getType();
					result.targetId = sf.getTarget().getId();
					result.targetType = sf.getTarget().getType();
					
					List<WayPoint> waypoints = new ArrayList<>(sf.getWayPoints().getWaypoints());
					result.wayPoints = waypoints.size();
				
					result.isOptimizable = edgeWaypointOptimizer.optimize(waypoints);
					result.sequenceFlowDirection = edgeDirectionEvaluator.evaluateArcType(waypoints);
					
					results.add(result);
				} catch(Exception e) {
					System.err.print("Error while evaluating sequence flow " + sf.getId() + ": ");
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void writeReport(String baseName, CsvWriterOptions options) throws IOException {
		try(CsvWriter out = new CsvWriter(baseName + ".sequenceflows.csv", options)) {
			out.writeHeader("Id", "SourceId", "SourceType", "TargetId", "TargetType", "WayPoints", "IsOptimizable", "Direction");
			
			for(SequenceFlowReportItem r : results) {
				out.writeRecord(r, r.id, r.sourceId, r.sourceType, r.targetId, r.targetType, r.wayPoints, r.isOptimizable, r.sequenceFlowDirection);
			}
		}
	}


}