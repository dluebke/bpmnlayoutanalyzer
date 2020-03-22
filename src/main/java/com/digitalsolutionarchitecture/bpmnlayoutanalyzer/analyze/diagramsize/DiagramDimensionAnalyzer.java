package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.diagramsize;

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
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.Bounds;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnxml.BPMNNamespaceContext;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnxml.XMLReaderHelper;

public class DiagramDimensionAnalyzer implements IBpmnAnaylzer {

	private NamespaceContext bpmnNamespaceContext = BPMNNamespaceContext.DEFAULT;

	private XPathExpression xpShapes;
	
	public DiagramDimensionAnalyzer() {
		try {
			XPath xpath = XPathFactory.newDefaultInstance().newXPath();
			xpath.setNamespaceContext(bpmnNamespaceContext);
			
			xpShapes = xpath.compile("//bpmndi:BPMNShape");
		} catch(Exception e) {
			throw new RuntimeException(e); 
		}
	}
	
	@Override
	public void performPreAnalysisOfModel(Document bpmnDocument) {
	}

	@Override
	public void analyse(Element bpmnDiagram, Result result) {
		Bounds diagramBounds = new Bounds(Double.MAX_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
		
		try {
			NodeList shapes = (NodeList) xpShapes.evaluate(bpmnDiagram, XPathConstants.NODESET);
			if(shapes.getLength() > 0) {
				for(int i = 0; i < shapes.getLength(); i++) {
					Element boundsElement = (Element)((Element)shapes.item(i)).getElementsByTagNameNS(bpmnNamespaceContext .getNamespaceURI("dc"), "Bounds").item(0);
					Bounds shapeBounds = XMLReaderHelper.convertToBounds(boundsElement);
					diagramBounds.extendTo(shapeBounds);
				}
			} else {
				throw new RuntimeException("No elements in diagram");
			}
		} catch(Exception e) {
			System.err.println("Cannot evaluate diagram size");
			e.printStackTrace();
			diagramBounds.set(Double.NaN, Double.NaN, Double.NaN, Double.NaN);
		}
		result.setDiagramBounds(diagramBounds);
	}

}
