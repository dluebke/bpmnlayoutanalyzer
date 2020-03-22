package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnxml;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.Bounds;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.WayPoint;

public class XMLReaderHelper {

	public static WayPoint fromDiElement(Node item) {
		WayPoint wp = new WayPoint();
		
		NamedNodeMap attributes = item.getAttributes();
		
		wp.setX(Double.parseDouble(attributes.getNamedItem("x").getNodeValue()));
		wp.setY(Double.parseDouble(attributes.getNamedItem("y").getNodeValue()));
		
		return wp;
	}

	public static List<WayPoint> convertToWayPoints(NodeList waypointsNodeList) {
		List<WayPoint> result = new ArrayList<>();
		for(int i = 0; i < waypointsNodeList.getLength(); i++) {
			result.add(fromDiElement(waypointsNodeList.item(i)));
		}
		return result;
	}

	public static Bounds convertToBounds(Element boundsElement) {
		return new Bounds(
			Double.parseDouble(boundsElement.getAttribute("x")),
			Double.parseDouble(boundsElement.getAttribute("y")),
			Double.parseDouble(boundsElement.getAttribute("width")),
			Double.parseDouble(boundsElement.getAttribute("height"))
		);
	}

}
