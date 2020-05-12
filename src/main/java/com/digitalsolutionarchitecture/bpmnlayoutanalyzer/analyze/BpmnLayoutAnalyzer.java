package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
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
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.diagramsize.DiagramDimensionAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges.SequenceFlowDirectionSummaryAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges.SequenceFlowReporter;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.exporter.ExporterEstimator;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.pools.PoolOrientationEvaluator;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.Participant;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.SequenceFlow;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.WayPointList;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnxml.BPMNNamespaceContext;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnxml.XMLReaderHelper;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.output.CsvWriterOptions;

public class BpmnLayoutAnalyzer {

	private NamespaceContext bpmnNamespaceContext = BPMNNamespaceContext.DEFAULT;

	private XPathExpression xpFlowNodes;
	private XPathExpression xpSequenceFlows;
	private XPathExpression xpEdges;
	private XPathExpression xpShapes;
	private XPathExpression xpDiagrams;
	private XPathExpression xpParticipantIds;

	private BpmnProcess process;
	
	private boolean needToCleanUpFlowNodes = false;

	private IBpmnAnalyzer[] analyzers = new IBpmnAnalyzer[] {
			new SequenceFlowDirectionSummaryAnalyzer(),
			new DiagramDimensionAnalyzer(),
			new PoolOrientationEvaluator(),
			new SequenceFlowReporter()
	};

	private ExporterEstimator exporterEstimator = new ExporterEstimator();

	
	public void analyze(File bpmnModelFile) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			dbf.setValidating(false);
			dbf.setExpandEntityReferences(false);
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			docBuilder.setEntityResolver(new EntityResolver() {
				
				@Override
				public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
					return new InputSource(new StringReader(""));
				}
			});
			
			try(InputStream in = new FileInputStream(bpmnModelFile)) {
				Document bpmnDocument = docBuilder.parse(in);
				
				performPreAnalysisOfModel(bpmnModelFile.getPath(), bpmnDocument);
				process.setExporterInfo(exporterEstimator.estimateExporter(bpmnDocument));
				
				NodeList diagrams = (NodeList) xpDiagrams.evaluate(bpmnDocument, XPathConstants.NODESET);
				for(int i = 0; i < diagrams.getLength(); i++) {
					Element diagram = (Element)diagrams.item(i);
					analyzeDiagram(diagram, i);
				}
			}
		} catch(Exception e) {
			System.err.print(bpmnModelFile.getAbsolutePath() + ": ");
			e.printStackTrace();
			throw e;
		}
	}
	
	public BpmnLayoutAnalyzer() {
		try {
			XPath xpath = XPathFactory.newDefaultInstance().newXPath();
			xpath.setNamespaceContext(bpmnNamespaceContext);
			
			xpFlowNodes = xpath.compile("//bpmn:process/*[local-name(.) != 'sequenceFlow']");
			xpSequenceFlows = xpath.compile("//bpmn:sequenceFlow");
			xpDiagrams = xpath.compile("//bpmndi:BPMNDiagram");
			xpEdges = xpath.compile("//bpmndi:BPMNEdge");
			xpShapes = xpath.compile("//bpmndi:BPMNShape");
			xpParticipantIds = xpath.compile("/bpmn:definitions/bpmn:collaboration/bpmn:participant/@id");
		} catch(Exception e) {
			throw new RuntimeException(e); 
		}
	}
	
	private void performPreAnalysisOfModel(String filename, Document bpmnDocument) {
		try {
			process = new BpmnProcess();
			process.setFilename(filename);
			collectAllFlowNodes(bpmnDocument);
			connectFlowNodesBySequenceFlows(bpmnDocument);
			collectAllParticipants(bpmnDocument);
			
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}

	private void collectAllParticipants(Document bpmnDocument) throws XPathExpressionException {
		NodeList ids = (NodeList) xpParticipantIds.evaluate(bpmnDocument, XPathConstants.NODESET);
		
		for(int i = 0; i < ids.getLength(); i++) {
			process.add(new Participant(ids.item(i).getNodeValue()));
		}
	}

	private void connectFlowNodesBySequenceFlows(Document bpmnDocument) throws XPathExpressionException {
		NodeList sequenceFlowElements = (NodeList) xpSequenceFlows.evaluate(bpmnDocument, XPathConstants.NODESET);
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

	private void collectAllFlowNodes(Document bpmnDocument) throws XPathExpressionException {
		NodeList flowNodeElements = (NodeList) xpFlowNodes.evaluate(bpmnDocument, XPathConstants.NODESET);
		for(int i = 0; i < flowNodeElements.getLength(); i++) {
			Element e = (Element)flowNodeElements.item(i);
			String id = e.getAttribute("id");
			String type = e.getLocalName();
			FlowNode fn = new FlowNode(id, type);
			process.add(fn);
		}
	}

	private void analyzeDiagram(Element bpmnDiagram, int diagramIndex) {
		cleanUpLayoutData();

		setShapeLayoutData(bpmnDiagram);
		setEdgesLayoutData(bpmnDiagram);
		
		process.setDiagramIndex(diagramIndex);
		for(IBpmnAnalyzer a : analyzers) {
			a.analyze(process);
		}
		
		needToCleanUpFlowNodes = true;
	}

	private void setShapeLayoutData(Element bpmnDiagram) {
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
	
	private void setEdgesLayoutData(Element bpmnDiagram) {
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

	private void cleanUpLayoutData() {
		if(needToCleanUpFlowNodes) {
			process.clearLayoutData();
			needToCleanUpFlowNodes = false;
		}
	}

	public void writeReport(String baseName, CsvWriterOptions options) throws IOException {
		for(IBpmnAnalyzer a : analyzers) {
			a.writeReport(baseName, options);
		}
	}

}
