package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.pattern;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.output.CsvResultWriter;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.output.CsvWriterOptions;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.util.StringUtil;

public class ControlFlowPatternAnalyzer implements IBpmnAnalyzer {
	
	private List<ControlFlowPatternAnalyzerResult> results = new ArrayList<>();

	@Override
	public void analyze(BpmnProcess processWithDiagramData) {
		for(FlowNode fn : processWithDiagramData.getFlowNodes()) {
			if(fn.hasLayoutData()) {
				identifyBoundaryEvents(fn, processWithDiagramData);
				identifyEventBasedGateways(fn, processWithDiagramData);
				identifyXorJoins(fn, processWithDiagramData);
				identifyAndSplits(fn, processWithDiagramData);
			}
		}
	}

	private void identifyXorJoins(FlowNode fn, BpmnProcess p) {
		if(fn.getIncomingSequenceFlows().size() > 1) {
			if(fn.getType().equals("exclusiveGateway")) {
				results.add(new ControlFlowPatternAnalyzerResult(p, "ExplicitXorJoin", "ExclusiveGateway", fn.getIncomingSequenceFlows().size()));
			} else if(!fn.getType().endsWith("Gateway")) {
				results.add(new ControlFlowPatternAnalyzerResult(p, "ImplicitXorJoin", StringUtil.toFirstUpper(fn.getTypeAndEventType()), fn.getIncomingSequenceFlows().size()));
			}
		}
	}
	
	private void identifyAndSplits(FlowNode fn, BpmnProcess p) {
		if(fn.getOutgoingSequenceFlows().size() > 1) {
			if(fn.getType().equals("parallelGateway")) {
				results.add(new ControlFlowPatternAnalyzerResult(p, "ExplicitAndSplit", "ParallelGateway", fn.getOutgoingSequenceFlows().size()));
			} else if(!fn.getType().endsWith("Gateway")) {
				results.add(new ControlFlowPatternAnalyzerResult(p, "ImplicitAndSplit", StringUtil.toFirstUpper(fn.getTypeAndEventType()), fn.getOutgoingSequenceFlows().size()));
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
		try(CsvResultWriter out = new CsvResultWriter(baseName + ".patterns.csv", options)) {
			out.writeHeader(ControlFlowPatternAnalyzerResult.HEADERS);
			out.writeRecords(results);
		}
	}

}
