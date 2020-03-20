package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

public class BPMNNamespaceContext implements NamespaceContext {

	private Map<String, String> namespaceByUri = new HashMap<>();
	private Map<String, String> namespaceByPrefix = new HashMap<>();
	
	public BPMNNamespaceContext() {
		addNamespace("http://www.omg.org/spec/BPMN/20100524/MODEL", "bpmn");
		addNamespace("http://www.omg.org/spec/BPMN/20100524/DI", "bpmndi");
		addNamespace("http://www.omg.org/spec/DD/20100524/DI", "di");
	}

	private void addNamespace(String uri, String prefix) {
		namespaceByUri.put(uri, prefix);
		namespaceByPrefix.put(prefix, uri);
	}
	
	public String getNamespaceURI(String prefix) {
		return namespaceByPrefix.get(prefix);
	}

	public String getPrefix(String uri) {
		return namespaceByUri.get(uri);
	}

	public Iterator<String> getPrefixes(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
