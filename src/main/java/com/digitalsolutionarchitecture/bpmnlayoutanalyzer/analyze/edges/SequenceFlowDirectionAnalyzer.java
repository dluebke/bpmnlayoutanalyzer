package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnaylzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.Result;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.WayPoint;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnxml.BPMNNamespaceContext;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnxml.XMLReaderHelper;

public class SequenceFlowDirectionAnalyzer implements IBpmnAnaylzer {

	private Set<String> idsOfSequenceFlows;

	private EdgeDirectionEvaluator edgeDirectionEvaluator = new EdgeDirectionEvaluator();
	private EdgeWaypointOptimizer edgeWaypointOptimizer = new EdgeWaypointOptimizer();

	private NamespaceContext bpmnNamespaceContext = BPMNNamespaceContext.DEFAULT;

	private XPathExpression xpEdges;
	private XPathExpression xpSequenceFlows;
	
	public SequenceFlowDirectionAnalyzer() {
		try {
			XPath xpath = XPathFactory.newDefaultInstance().newXPath();
			xpath.setNamespaceContext(bpmnNamespaceContext);
			
			xpSequenceFlows = xpath.compile("//bpmn:sequenceFlow");
			xpEdges = xpath.compile("//bpmndi:BPMNEdge");
		} catch(Exception e) {
			throw new RuntimeException(e); 
		}
	}
	
	@Override
	public void performPreAnalysisOfModel(Document bpmnDocument) {
		try {
			idsOfSequenceFlows = new HashSet<>();
			NodeList sequenceFlows = (NodeList) xpSequenceFlows.evaluate(bpmnDocument, XPathConstants.NODESET);
			for(int i = 0; i < sequenceFlows.getLength(); i++) {
				String id = sequenceFlows.item(i).getAttributes().getNamedItem("id").getNodeValue();
				idsOfSequenceFlows.add(id);
			}
		} catch(Exception e) {
			System.err.println("Cannot evaluate sequence flows for BPMN model");
			e.printStackTrace();
		}
	}

	@Override
	public void analyse(Element bpmnDiagram, Result result) {
		try {
			NodeList edges = (NodeList) xpEdges.evaluate(bpmnDiagram, XPathConstants.NODESET);
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
		} catch(Exception e) {
			System.err.println("Cannot evaluate sequence flows for BPMN model");
			e.printStackTrace();
		}
	}

}
