package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.SequenceFlow;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.WayPoint;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.output.CsvWriter;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.output.CsvWriterOptions;

public class SequenceFlowDirectionSummaryAnalyzer implements IBpmnAnalyzer {

	private static final String[] HEADERS;
	private EdgeDirectionEvaluator edgeDirectionEvaluator = new EdgeDirectionEvaluator();
	private EdgeWaypointOptimizer edgeWaypointOptimizer = new EdgeWaypointOptimizer();
	private List<SequenceFlowDirectionSummaryResult> results = new ArrayList<>();
	
	static {
		List<String> headers = new ArrayList<>();
		headers.addAll(Arrays.asList(
			"DominantSequenceFlowDirection", 
			"DominantSequenceFlowDirectionPurity"
		));
		for(EdgeDirection at : EdgeDirection.values()) {
			headers.add("SEQUENCEFLOW_" + at.toString());
		}
		headers.add("SEQUENCEFLOW_TOTAL");
		for(EdgeDirection at : EdgeDirection.values()) {
			headers.add("OPTIMIZABLE_SEQUENCEFLOW_" + at.toString());
		}
		headers.add("OPTIMIZABLE_SEQUENCEFLOW_TOTAL");
		
		HEADERS = headers.toArray(new String[headers.size()]);
	}
	
	public SequenceFlowDirectionSummaryAnalyzer() {
	}
	
	@Override
	public void analyze(BpmnProcess p) {
		SequenceFlowDirectionSummaryResult result = new SequenceFlowDirectionSummaryResult(p);
		
		for(SequenceFlow sf : p.getSequenceFlows()) {
			if(sf.getWayPoints() != null) {
				List<WayPoint> waypoints = new ArrayList<>(sf.getWayPoints().getWaypoints());
				
				boolean optimized = edgeWaypointOptimizer.optimize(waypoints);
				EdgeDirection at = edgeDirectionEvaluator.evaluateArcType(waypoints);
				result.addSequenceFlow(at);
				if(optimized) {
					result.addOptimizableSequenceFlow(at);
				}
			}
		}
		
		result.calculateMetrics();
		results.add(result);
	}
	
	@Override
	public void writeReport(String baseName, CsvWriterOptions options) throws IOException {
		try(CsvWriter out = new CsvWriter(baseName + ".sequenceflowsummary.csv", options)) {
			out.writeHeader(HEADERS);
			
			List<Object> fields = new ArrayList<>();
			for(SequenceFlowDirectionSummaryResult r : results) {
				fields.addAll(Arrays.asList(
					r.getDominantSequenceFlowDirection() != null ? r.getDominantSequenceFlowDirection().toString() : "", 
					r.getDominantSequenceFlowDirectionPurity() >= 0.0 ? Double.toString(r.getDominantSequenceFlowDirectionPurity()) : ""
				));
				for(EdgeDirection at : EdgeDirection.values()) {
					fields.add(r.getSequenceFlowDirections().get(at));
				}
				fields.add(r.getSequenceFlowDirections().sumAll());
				for(EdgeDirection at : EdgeDirection.values()) {
					fields.add(r.getOptimizableSequenceFlowDirections().get(at));
				}
				fields.add(r.getOptimizableSequenceFlowDirections().sumAll());

				out.writeRecord(r, fields.toArray(new Object[fields.size()]));
				
				fields.clear();
			}
		}
		
	}
	
}
