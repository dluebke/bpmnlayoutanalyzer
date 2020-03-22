package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges.EdgeDirection;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges.EdgeDirectionEvaluator;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges.EdgeWaypointOptimizer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.exporter.ExporterEstimator;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.exporter.ExporterInfo;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.WayPoint;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnxml.BPMNNamespaceContext;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnxml.XMLReaderHelper;

public class BpmnLayoutAnalyzer {

	private ExporterEstimator toolEstimator = new ExporterEstimator();
	private EdgeDirectionEvaluator edgeDirectionEvaluator = new EdgeDirectionEvaluator();
	private EdgeWaypointOptimizer edgeWaypointOptimizer = new EdgeWaypointOptimizer();
	private XPathExpression xpDiagrams;
	private XPathExpression xpEdges;
	private XPathExpression xpSequenceFlows;
	private BPMNNamespaceContext bpmnNamespaceContext = new BPMNNamespaceContext();
	
	public BpmnLayoutAnalyzer() throws XPathExpressionException {
		XPath xpath = XPathFactory.newDefaultInstance().newXPath();
		xpath.setNamespaceContext(bpmnNamespaceContext);
		
		xpDiagrams = xpath.compile("//bpmndi:BPMNDiagram");
		xpEdges = xpath.compile("//bpmndi:BPMNEdge");
		xpSequenceFlows = xpath.compile("//bpmn:sequenceFlow");
	}
	
	public List<Result> analyze(File bpmnModelFile) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
		List<Result> results = new ArrayList<>();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		
		try(InputStream in = new FileInputStream(bpmnModelFile)) {
			Document document = dbf.newDocumentBuilder().parse(in);
			
			Set<String> idsOfSequenceFlows = gatherAllSequenceFlowIds(document);
			
			NodeList diagrams = (NodeList) xpDiagrams.evaluate(document, XPathConstants.NODESET);
			for(int i = 0; i < diagrams.getLength(); i++) {
				Element diagram = (Element)diagrams.item(i);
				ExporterInfo toolInfo = toolEstimator.estimate(document.getDocumentElement());
				Result result = new Result(
					bpmnModelFile.getName(),
					i + 1,
					toolInfo.getExporter(),
					toolInfo.getExporterVersion()
				);
				
				analyzeSequenceFlows(diagram, idsOfSequenceFlows, result);
				
				result.calculateMetrics();
				results.add(result);
			}
		}
		
		return results;
	}

	private void analyzeSequenceFlows(Element diagram, Set<String> idsOfSequenceFlows, Result result)
			throws XPathExpressionException {
		NodeList edges = (NodeList) xpEdges.evaluate(diagram, XPathConstants.NODESET);
		for(int j = 0; j < edges.getLength(); j++) {
			Element edge = (Element)edges.item(j);
			if(idsOfSequenceFlows.contains(edge.getAttribute("bpmnElement"))) {
				NodeList waypointsNodeList = edge.getElementsByTagNameNS(bpmnNamespaceContext.getNamespaceURI("di"), "waypoint");
				List<WayPoint> waypoints = XMLReaderHelper.convertToWayPoints(waypointsNodeList);
				boolean optimized = edgeWaypointOptimizer.optimize(waypoints);
				EdgeDirection at = edgeDirectionEvaluator.evaluateArcType(waypoints);
				result.addSequenceFlow(at);
				if(optimized) {
					result.addOptimizableSequenceFlow(at);
				}
			}
		}
	}

	private Set<String> gatherAllSequenceFlowIds(Document document) throws XPathExpressionException {
		Set<String> idsOfSequenceFlows = new HashSet<>();
		NodeList sequenceFlows = (NodeList) xpSequenceFlows.evaluate(document, XPathConstants.NODESET);
		for(int i = 0; i < sequenceFlows.getLength(); i++) {
			String id = sequenceFlows.item(i).getAttributes().getNamedItem("id").getNodeValue();
			idsOfSequenceFlows.add(id);
		}
		return idsOfSequenceFlows;
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
