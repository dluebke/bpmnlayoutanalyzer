package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.pattern;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.output.CsvWriter;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.output.CsvWriterOptions;

public class ControlFlowPatternAnalyzer implements IBpmnAnalyzer {
	
	private List<ControlFlowPatternAnalyzerResult> results = new ArrayList<>();

	@Override
	public void analyze(BpmnProcess processWithDiagramData) {
		for(FlowNode fn : processWithDiagramData.getFlowNodes()) {
			if(fn.hasLayoutData()) {
				identifyBoundaryEvents(fn, processWithDiagramData);
				identifyEventBasedGateways(fn, processWithDiagramData);
			}
		}
	}

	private void identifyEventBasedGateways(FlowNode fn, BpmnProcess processWithDiagramData) {
		if(processWithDiagramData.getFilename().endsWith("SequenceFlow_Visibility.bpmn")) {
			System.out.println("Found");
		}
		if("eventBasedGateway".equals(fn.getType())) {
			List<String> outgoingFlowNodes = fn.getOutgoingSequenceFlows().stream()
					.map(x -> x.getTarget())
					.map(x -> x.getTypeAndEventType())
					.sorted()
					.collect(Collectors.toList());
			
			String patternClassification = String.join(",", outgoingFlowNodes.toArray(new String[outgoingFlowNodes.size()]));
			ControlFlowPatternAnalyzerResult r = new ControlFlowPatternAnalyzerResult(processWithDiagramData, "EventBasedGateway", patternClassification, outgoingFlowNodes.size());
			
			results.add(r);
		}
		
	}

	private void identifyBoundaryEvents(FlowNode fn, BpmnProcess processWithDiagramData) {
		if(fn.getBoundaryEvents().size() > 0) {
			
			List<String> events = fn.getBoundaryEvents().stream().map(x -> x.getTypeAndEventType()).sorted().collect(Collectors.toList());
			String patternClassification = String.join(",", events.toArray(new String[events.size()]));
					
			ControlFlowPatternAnalyzerResult r = new ControlFlowPatternAnalyzerResult(processWithDiagramData, "BoundaryEvent", patternClassification, fn.getBoundaryEvents().size());
			results.add(r);
		}
	}

	@Override
	public void writeReport(String baseName, CsvWriterOptions options) throws IOException {
		try(CsvWriter out = new CsvWriter(baseName + ".patterns.csv", options)) {
			out.writeHeader("Pattern", "PatternClassification", "PatternSize");
			
			for(ControlFlowPatternAnalyzerResult r : results) {
				out.writeRecord(
					r, 
					r.getPattern(),
					r.getPatternClassification(),
					r.getPatternSize()
				);
			}
		}
	}

}
