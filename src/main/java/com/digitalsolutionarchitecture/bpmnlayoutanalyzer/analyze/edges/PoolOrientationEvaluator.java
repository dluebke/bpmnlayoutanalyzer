package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnaylzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.Result;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnxml.BPMNNamespaceContext;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.util.StringUtil;

public class PoolOrientationEvaluator implements IBpmnAnaylzer {

	private NamespaceContext bpmnNamespaceContext = BPMNNamespaceContext.DEFAULT;
	private List<String> participantIds;
	
	private XPathExpression xpParticipantIds;
	private XPathExpression xpShapes;
	
	public PoolOrientationEvaluator() {
		try {
			XPath xpath = XPathFactory.newDefaultInstance().newXPath();
			xpath.setNamespaceContext(bpmnNamespaceContext);
			
			xpParticipantIds = xpath.compile("/bpmn:definitions/bpmn:collaboration/bpmn:participant/@id");
			xpShapes = xpath.compile("//bpmndi:BPMNShape");
		} catch(Exception e) {
			throw new RuntimeException(e); 
		}
	}
	
	@Override
	public void performPreAnalysisOfModel(Document bpmnDocument) {
		try {
			NodeList ids = (NodeList) xpParticipantIds.evaluate(bpmnDocument, XPathConstants.NODESET);
			participantIds = new ArrayList<>(ids.getLength());
			for(int i = 0; i < ids.getLength(); i++) {
				participantIds.add(ids.item(i).getNodeValue());
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void analyse(Element bpmnDiagram, Result result) {
		try {
			NodeList shapes = (NodeList)xpShapes.evaluate(bpmnDiagram, XPathConstants.NODESET);
			for(int i = 0;  i < shapes.getLength(); i++) {
				Element shape = (Element) shapes.item(i);
				String bpmnId = shape.getAttribute("bpmnElement");
				if(!StringUtil.isEmpty(bpmnId) && participantIds.contains(bpmnId)) {
					String isHorizontal = shape.getAttribute("isHorizontal");
					if("true".equals(isHorizontal)) {
						result.incPoolOrientationHorizontal();
					} else if("false".equals(isHorizontal)) {
						result.incPoolOrientationVertical();
					} else {
						result.incPoolOrientationUnknown();
					}
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
}
