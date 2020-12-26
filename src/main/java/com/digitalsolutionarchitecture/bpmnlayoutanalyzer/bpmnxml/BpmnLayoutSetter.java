package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnxml;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.Participant;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.SequenceFlow;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.WayPointList;

public class BpmnLayoutSetter {

	private NamespaceContext bpmnNamespaceContext = BPMNNamespaceContext.DEFAULT;

	private XPathExpression xpDiagrams;
	private XPathExpression xpEdges;
	private XPathExpression xpShapes;

	
	public BpmnLayoutSetter() {
		try {
			XPath xpath = XPathFactory.newDefaultInstance().newXPath();
			xpath.setNamespaceContext(bpmnNamespaceContext);
			xpDiagrams = xpath.compile("//bpmndi:BPMNDiagram");
			xpEdges = xpath.compile("//bpmndi:BPMNEdge");
			xpShapes = xpath.compile("//bpmndi:BPMNShape");
		} catch(Exception e) {
			throw new RuntimeException(e); 
		}
	}
	
	public int getDiagramCount(BpmnProcess process) {
		try {
			return ((NodeList) xpDiagrams.evaluate(process.getBpmnDocument(), XPathConstants.NODESET)).getLength();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	private Element getDiagram(BpmnProcess process, int index) {
		try {
			NodeList diagrams = (NodeList) xpDiagrams.evaluate(process.getBpmnDocument(), XPathConstants.NODESET);
			return (Element)diagrams.item(index);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void setLayoutData(BpmnProcess process, int diagramIndex) {
		process.clearLayoutData();
		Element bpmnDiagram = getDiagram(process, diagramIndex);
		setShapeLayoutData(process, bpmnDiagram);
		setEdgesLayoutData(process, bpmnDiagram);
		process.setDiagramIndex(diagramIndex);
	}
	
	private void setShapeLayoutData(BpmnProcess process, Element bpmnDiagram) {
		try {
			NodeList shapes = (NodeList) xpShapes.evaluate(bpmnDiagram, XPathConstants.NODESET);
			for(int j = 0; j < shapes.getLength(); j++) {
				Element shape = (Element)shapes.item(j);
				Element boundsElement = (Element)(shape).getElementsByTagNameNS(bpmnNamespaceContext.getNamespaceURI("dc"), "Bounds").item(0);
				String bpmnElementId = shape.getAttribute("bpmnElement");
				
				FlowNode flowNode = process.getFlowNodeById(bpmnElementId);
				if(flowNode != null) {
					flowNode.setBounds(XMLReaderHelper.convertToBounds(boundsElement));
				} else {
					Participant p = process.getParticipantById(bpmnElementId);
					if(p != null) {
						p.setBounds(XMLReaderHelper.convertToBounds(boundsElement));
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setEdgesLayoutData(BpmnProcess process, Element bpmnDiagram) {
		try {
			NodeList edges = (NodeList) xpEdges.evaluate(bpmnDiagram, XPathConstants.NODESET);
			for(int j = 0; j < edges.getLength(); j++) {
				Element edge = (Element)edges.item(j);
				SequenceFlow sequenceFlow = process.getSequenceFlowById(edge.getAttribute("bpmnElement"));
				if(sequenceFlow != null) {
					NodeList waypointsNodeList = edge.getElementsByTagNameNS(bpmnNamespaceContext.getNamespaceURI("di"), "waypoint");
					WayPointList waypoints = XMLReaderHelper.convertToWayPoints(waypointsNodeList);
					sequenceFlow.setWayPoints(waypoints);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
