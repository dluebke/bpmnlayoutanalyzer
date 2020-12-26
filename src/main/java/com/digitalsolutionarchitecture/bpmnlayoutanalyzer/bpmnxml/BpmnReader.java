package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnxml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.Participant;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.SequenceFlow;

public class BpmnReader {

	private final DocumentBuilder docBuilder;
	
	private NamespaceContext bpmnNamespaceContext = BPMNNamespaceContext.DEFAULT;
	private XPathExpression xpFlowNodes;
	private XPathExpression xpSequenceFlows;
	private XPathExpression xpParticipantIds;
	
	public BpmnReader() {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			dbf.setValidating(false);
			dbf.setExpandEntityReferences(false);
			docBuilder = dbf.newDocumentBuilder();
			docBuilder.setEntityResolver(new EntityResolver() {
				
				@Override
				public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
					return new InputSource(new StringReader(""));
				}
			});
		
			XPath xpath = XPathFactory.newDefaultInstance().newXPath();
			xpath.setNamespaceContext(bpmnNamespaceContext);
			
			xpFlowNodes = xpath.compile("//bpmn:process/*[local-name(.) != 'sequenceFlow']");
			xpSequenceFlows = xpath.compile("//bpmn:sequenceFlow");
			xpParticipantIds = xpath.compile("/bpmn:definitions/bpmn:collaboration/bpmn:participant/@id");
		} catch(Exception e) {
			throw new RuntimeException(e); 
		}
	}
	
	
	public BpmnProcess readProcess(String filename) throws FileNotFoundException, SAXException, IOException {
		try {
			Document bpmnDocument = docBuilder.parse(new FileInputStream(filename));
			
			BpmnProcess process = new BpmnProcess(filename, bpmnDocument);
			
			collectAllFlowNodes(process);
			connectFlowNodesBySequenceFlows(process);
			collectAllParticipants(process);
			
			return process;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void collectAllParticipants(BpmnProcess process) throws XPathExpressionException {
		NodeList ids = (NodeList) xpParticipantIds.evaluate(process.getBpmnDocument(), XPathConstants.NODESET);
		
		for(int i = 0; i < ids.getLength(); i++) {
			process.add(new Participant(ids.item(i).getNodeValue()));
		}
	}
	
	private void connectFlowNodesBySequenceFlows(BpmnProcess process) throws XPathExpressionException {
		NodeList sequenceFlowElements = (NodeList) xpSequenceFlows.evaluate(process.getBpmnDocument(), XPathConstants.NODESET);
		for(int i = 0; i < sequenceFlowElements.getLength(); i++) {
			Element e = (Element)sequenceFlowElements.item(i);
			String sourceId = e.getAttribute("sourceRef");
			String targetId = e.getAttribute("targetRef");
			FlowNode source = process.getFlowNodeById(sourceId);
			FlowNode target = process.getFlowNodeById(targetId);
			
			SequenceFlow s = new SequenceFlow(e.getAttribute("id"), source, target);
			process.add(s);
		}
	}

	private void collectAllFlowNodes(BpmnProcess process) throws XPathExpressionException {
		Map<FlowNode, String> attachedToId = new HashMap<>();
		
		NodeList flowNodeElements = (NodeList) xpFlowNodes.evaluate(process.getBpmnDocument(), XPathConstants.NODESET);
		for(int i = 0; i < flowNodeElements.getLength(); i++) {
			Element e = (Element)flowNodeElements.item(i);
			String id = e.getAttribute("id");
			String type = e.getLocalName();
			FlowNode fn = new FlowNode(id, type);
			
			if(e.getLocalName().equals("boundaryEvent")) {
				String cancelActivityAttribute = e.getAttribute("cancelActivity");
				if(cancelActivityAttribute != null && !"".equals(cancelActivityAttribute)) {
					boolean cancelActivity = "true".equals(cancelActivityAttribute);
					fn.setCancelActivity(cancelActivity);
				}
				
				String attachedToRef = e.getAttribute("attachedToRef");
				if(attachedToRef != null) {
					attachedToId.put(fn, attachedToRef);
				}
			}
			if(e.getLocalName().endsWith("Event")) {
				NodeList children = e.getChildNodes();
				for(int x = 0; x < children.getLength(); x++) {
					Node n = children.item(x);
					if(n.getNodeType() == Node.ELEMENT_NODE && n.getLocalName().endsWith("EventDefinition")) {
						String eventType = n.getLocalName().replace("EventDefintion", "");
						fn.setEventType(eventType);
					}
				}
			}
			
			process.add(fn);
		}
		
		for(Entry<FlowNode, String> entry : attachedToId.entrySet()) {
			try {
				FlowNode attachedToNode = process.getFlowNodeById(entry.getValue());
				entry.getKey().setAttachedTo(attachedToNode);
				attachedToNode.addBoundaryEvent(entry.getKey());
			} catch(Exception e) {
				System.err.println("Error attaching boundary event " + entry.getKey().getId() + " to " + entry.getValue());
				e.printStackTrace();
			}
		}
	}

	

}
