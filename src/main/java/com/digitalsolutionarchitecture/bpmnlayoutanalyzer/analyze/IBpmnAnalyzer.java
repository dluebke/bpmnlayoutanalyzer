package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze;

import java.io.IOException;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.output.CsvWriterOptions;

public interface IBpmnAnalyzer {

	public void analyze(BpmnProcess processWithDiagramData);

	public void writeReport(String baseName, CsvWriterOptions options) throws IOException;


}
