package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.connectedness.ConnectednessAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.diagramsize.DiagramDimensionAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges.SequenceFlowDirectionSummaryAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges.SequenceFlowReporter;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.exporter.ExporterEstimator;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.flownodecount.FlowNodeCountAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.layout.LayoutIdentificator;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.pattern.ControlFlowPatternAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.pools.PoolOrientationEvaluator;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnxml.BpmnLayoutSetter;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnxml.BpmnReader;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.debug.AllFlowNodeTypesAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.output.CsvResultWriter;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.output.CsvWriterOptions;

public class BpmnLayoutAnalyzer {

	private static final int MAX_TRACE_SEARCH_DEPTH = 10000000;
	
	private IBpmnAnalyzer[] analyzers = new IBpmnAnalyzer[] {
			new SequenceFlowDirectionSummaryAnalyzer(),
			new DiagramDimensionAnalyzer(),
			new PoolOrientationEvaluator(),
			new SequenceFlowReporter(),
			new ControlFlowPatternAnalyzer(),
			new FlowNodeCountAnalyzer(),
			new ConnectednessAnalyzer(),
			new AllFlowNodeTypesAnalyzer(),
			new LayoutIdentificator(MAX_TRACE_SEARCH_DEPTH)
	};

	private ExporterEstimator exporterEstimator = new ExporterEstimator();
	private BpmnLayoutSetter bpmnLayoutSetter = new BpmnLayoutSetter();
	
	public void analyze(File bpmnModelFile) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
		try {
			try(InputStream in = new FileInputStream(bpmnModelFile)) {
				BpmnProcess process = new BpmnReader().readProcess(bpmnModelFile.getPath());
				process.setExporterInfo(exporterEstimator.estimateExporter(process.getBpmnDocument()));

				int diagramCount = bpmnLayoutSetter.getDiagramCount(process);
				for(int i = 0; i < diagramCount; i++) {
					analyzeDiagram(process,  i);
				}
				
			}
		} catch(Exception e) {
			System.err.print(bpmnModelFile.getAbsolutePath() + ": ");
			e.printStackTrace();
			throw e;
		}
	}
	
	private void analyzeDiagram(BpmnProcess process, int diagramIndex) {
		bpmnLayoutSetter.setLayoutData(process, diagramIndex);
		for(IBpmnAnalyzer a : analyzers) {
			a.analyze(process);
		}
	}	
	
	public void writeReport(String baseName, CsvWriterOptions options) throws IOException {
		for(IBpmnAnalyzer a : analyzers) {
			try(CsvResultWriter out = new CsvResultWriter(baseName + "." + a.getShortName() + ".csv", options)) {
				out.writeHeader(a.getHeaders());
				out.writeRecords(a.getResults());
			}
		}
	}

}
