package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.diagramsize.DiagramDimensionAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges.PoolOrientationEvaluator;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges.SequenceFlowDirectionAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.exporter.ExporterEstimator;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.WayPoint;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnxml.BPMNNamespaceContext;

public class BpmnLayoutAnalyzer {

	private XPathExpression xpDiagrams;
	
	private NamespaceContext bpmnNamespaceContext = BPMNNamespaceContext.DEFAULT;
	
	private IBpmnAnaylzer[] analyzers = new IBpmnAnaylzer[] {
			new ExporterEstimator(),
			new SequenceFlowDirectionAnalyzer(),
			new DiagramDimensionAnalyzer(),
			new PoolOrientationEvaluator()
	};
	
	public BpmnLayoutAnalyzer() {
		try {
			XPath xpath = XPathFactory.newDefaultInstance().newXPath();
			xpath.setNamespaceContext(bpmnNamespaceContext);
			
			xpDiagrams = xpath.compile("//bpmndi:BPMNDiagram");
		} catch(Exception e) {
			throw new RuntimeException(e); 
		}
	}
	
	public List<Result> analyze(File bpmnModelFile) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
		List<Result> results = new ArrayList<>();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		
		try(InputStream in = new FileInputStream(bpmnModelFile)) {
			Document bpmnDocument = dbf.newDocumentBuilder().parse(in);
			
			for(IBpmnAnaylzer bpmnAnalyzer : analyzers) {
				bpmnAnalyzer.performPreAnalysisOfModel(bpmnDocument);
			}
			
			NodeList diagrams = (NodeList) xpDiagrams.evaluate(bpmnDocument, XPathConstants.NODESET);
			for(int i = 0; i < diagrams.getLength(); i++) {
				Element diagram = (Element)diagrams.item(i);
				
				Result result = new Result(
					bpmnModelFile.getName(),
					i + 1
				);
				
				for(IBpmnAnaylzer bpmnAnalyzer : analyzers) {
					bpmnAnalyzer.analyse(diagram, result);
				}
				
				result.calculateMetrics();
				results.add(result);
			}
		}
		
		return results;
	}

	static int calDegree(WayPoint wp1, WayPoint wpLast) {
		double x0 = wp1.getX();
		double y0 = wp1.getY();
		
		double x1 = wpLast.getX() - x0;
		double y1 = wpLast.getY() - y0;
		int result = (int)(Math.atan2(y1, x1) * 180 / Math.PI);
		if(result >= 0) {
			return result;
		} else {
			return 360 + result;
		}
	}
	
}
