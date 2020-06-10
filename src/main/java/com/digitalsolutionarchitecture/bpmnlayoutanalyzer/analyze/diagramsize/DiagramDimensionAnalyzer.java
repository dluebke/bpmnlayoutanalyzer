package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.diagramsize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.Bounds;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.output.CsvResultWriter;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.output.CsvWriterOptions;

public class DiagramDimensionAnalyzer implements IBpmnAnalyzer {

	private List<DiagramDimensionResult> results = new ArrayList<>();
	
	public DiagramDimensionAnalyzer() {
	}
	
	@Override
	public void analyze(BpmnProcess p) {
		Bounds diagramBounds = new Bounds(Double.MAX_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
		
		for(FlowNode fn : p.getFlowNodes()) {
			diagramBounds.extendTo(fn.getBounds());
		}
		
		results.add(
			new DiagramDimensionResult(
				p,
				diagramBounds
			)
		);
	}

	@Override
	public void writeReport(String baseName, CsvWriterOptions options) throws IOException {
		try(CsvResultWriter out = new CsvResultWriter(baseName + ".dimensions.csv", options)) {
			out.writeHeader(DiagramDimensionResult.HEADERS);
			
			out.writeRecords(results);
		}
	}

}
