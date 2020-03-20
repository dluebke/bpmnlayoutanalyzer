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

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BPMNNamespaceContext;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.WayPoint;

public class BpmnLayoutAnalyzer {

	private ExporterEstimator toolEstimator = new ExporterEstimator();
	
	private static final int DEGREE_TOLERANCE = 2;
	
	public List<Result> analyze(File bpmnModelFile) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
		List<Result> results = new ArrayList<>();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		
		try(InputStream in = new FileInputStream(bpmnModelFile)) {
			Document document = dbf.newDocumentBuilder().parse(in);
			
			XPath xpath = XPathFactory.newDefaultInstance().newXPath();
			BPMNNamespaceContext bpmnNamespaceContext = new BPMNNamespaceContext();
			xpath.setNamespaceContext(bpmnNamespaceContext);
			XPathExpression xpDiagrams = xpath.compile("//bpmndi:BPMNDiagram");
			XPathExpression xpEdges = xpath.compile("//bpmndi:BPMNEdge");
			XPathExpression xpSequenceFlows = xpath.compile("//bpmn:sequenceFlow");
			
			Set<String> idsOfSequenceFlows = new HashSet<>();
			NodeList sequenceFlows = (NodeList) xpSequenceFlows.evaluate(document, XPathConstants.NODESET);
			for(int i = 0; i < sequenceFlows.getLength(); i++) {
				String id = sequenceFlows.item(i).getAttributes().getNamedItem("id").getNodeValue();
				idsOfSequenceFlows.add(id);
			}
			
			NodeList diagrams = (NodeList) xpDiagrams.evaluate(document, XPathConstants.NODESET);
			for(int i = 0; i < diagrams.getLength(); i++) {
				ExporterInfo toolInfo = toolEstimator.estimate(document.getDocumentElement());
				Result result = new Result(
					bpmnModelFile.getName(),
					i + 1,
					toolInfo.getExporter(),
					toolInfo.getExporterVersion()
				);
				
				NodeList edges = (NodeList) xpEdges.evaluate(document, XPathConstants.NODESET);
				for(int j = 0; j < edges.getLength(); j++) {
					Element edge = (Element)edges.item(j);
					if(idsOfSequenceFlows.contains(edge.getAttribute("bpmnElement"))) {
						NodeList waypointsNodeList = edge.getElementsByTagNameNS(bpmnNamespaceContext.getNamespaceURI("di"), "waypoint");
						List<WayPoint> waypoints = convertToWayPoints(waypointsNodeList);
						boolean optimized = optimize(waypoints);
						EdgeDirection at = evaluateArcType(waypoints);
						result.addSequenceFlow(at);
						if(optimized) {
							result.addOptimizableSequenceFlow(at);
						}
					}
				}
				result.calculateMetrics();
				results.add(result);
			}
		}
		
		return results;
	}

	static boolean optimize(List<WayPoint> waypoints) {
		Set<WayPoint> toRemove = new HashSet<>();
		
		for(int i = 0; i < waypoints.size() - 2; i++) {
			WayPoint wp1 = waypoints.get(i);
			WayPoint wp2 = waypoints.get(i + 1);
			WayPoint wp3 = waypoints.get(i + 2);
			
			if(calDegree(wp1, wp2) == calDegree(wp2, wp3)) {
				toRemove.add(wp2);
			}
		}
		
		waypoints.removeAll(toRemove);
		return toRemove.size() > 0;
	}
	
	private List<WayPoint> convertToWayPoints(NodeList waypointsNodeList) {
		List<WayPoint> result = new ArrayList<>();
		for(int i = 0; i < waypointsNodeList.getLength(); i++) {
			result.add(WayPoint.fromDiElement(waypointsNodeList.item(i)));
		}
		return result;
	}

	private EdgeDirection evaluateArcType(List<WayPoint> waypoints) {
		WayPoint wp1 = waypoints.get(0);
		WayPoint wpLast = waypoints.get(waypoints.size() - 1);
		
		String direction;
		int degree = calDegree(wp1, wpLast);
		if(degree < DEGREE_TOLERANCE) {
			direction = "LEFT_RIGHT";
		} else if(degree < 90 - DEGREE_TOLERANCE) {
			direction = "LEFT_LOWERRIGHT";
		} else if(degree < 90 + DEGREE_TOLERANCE) {
			direction = "TOP_BOTTOM";
		} else if(degree < 180 - DEGREE_TOLERANCE) {
			direction = "RIGHT_LOWERLEFT";
		} else if(degree < 180 + DEGREE_TOLERANCE) {
			direction = "RIGHT_LEFT";
		} else if(degree < 270 - DEGREE_TOLERANCE) {
			direction = "RIGHT_UPPERLEFT";
		} else if(degree < 270 + DEGREE_TOLERANCE) {
			direction = "BOTTOM_TOP";
		} else {
			direction = "LEFT_UPPERRIGHT";
		}
		
		if(waypoints.size() == 2) {
			return EdgeDirection.valueOf("DIRECT_" + direction);
		} else if(waypoints.size() == 3) {
			WayPoint wp2 = waypoints.get(1);
			if(wp1.getX() == wp2.getX() && wp2.getY() == wpLast.getY()) {
				return EdgeDirection.valueOf("PERPENDICULAR_" + direction);
			} else if(wp1.getY() == wp2.getY() && wp2.getX() == wpLast.getX()) {
				return EdgeDirection.valueOf("PERPENDICULAR_" + direction);
			} else {
				return EdgeDirection.valueOf("BEND_" + direction);
			}
		} else if(waypoints.size() == 4) {
			WayPoint wp2 = waypoints.get(1);
			WayPoint wp3 = waypoints.get(2);
			if(wp1.getX() == wp2.getX() && wp2.getY() == wp3.getY() && wp3.getX() == wpLast.getX()) {
				return EdgeDirection.valueOf("ZAGGED_" + direction);
			} else if(wp1.getY() == wp2.getY() && wp2.getX() == wp3.getX() && wp3.getY() == wpLast.getY()) {
				return EdgeDirection.valueOf("ZAGGED_" + direction);
			} else {
				return EdgeDirection.valueOf("BEND_" + direction);
			}
		} else {
			return EdgeDirection.valueOf("BEND_" + direction);
		}
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
