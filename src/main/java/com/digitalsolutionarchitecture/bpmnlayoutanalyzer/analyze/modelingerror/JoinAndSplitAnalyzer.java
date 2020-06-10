package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.modelingerror;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.output.CsvResultWriter;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.output.CsvWriterOptions;

public class JoinAndSplitAnalyzer implements IBpmnAnalyzer {

	private List<JoinAndSplitAnalyzerResult> results = new ArrayList<>();

	@Override
	public void analyze(BpmnProcess processWithDiagramData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeReport(String baseName, CsvWriterOptions options) throws IOException {
		try(CsvResultWriter out = new CsvResultWriter(baseName + ".wrongjoinandsplit.csv", options)) {
			out.writeHeader(JoinAndSplitAnalyzerResult.HEADERS);
			out.writeRecords(results );
		}
	}

}
