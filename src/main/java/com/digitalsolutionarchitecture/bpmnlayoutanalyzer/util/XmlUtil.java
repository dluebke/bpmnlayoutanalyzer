package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.util;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class XmlUtil {

	private XmlUtil() {
	}

	public static List<String> gatherDirectlyDeclaredNamespaceUris(Element e) {
		List<String> result = new ArrayList<>();
		
		NamedNodeMap attributes = e.getAttributes();
		for(int i = 0; i < attributes.getLength(); i++) {
			Node attribute = attributes.item(i);
			if(
				"xmlns".equals(attribute.getPrefix()) || 
				(StringUtil.isEmpty(attribute.getPrefix()) && "xmlns".equals(attribute.getLocalName()))
				) {
				result.add(attribute.getNodeValue());
			}
		}
		
		return result;
	}
	
	
	
}
