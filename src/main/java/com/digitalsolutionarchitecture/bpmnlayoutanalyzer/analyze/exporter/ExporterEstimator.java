package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.exporter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.util.StringUtil;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.util.XmlUtil;

public class ExporterEstimator {

	private Map<String, ExporterInfo> namespacesForTool;

	public static final ExporterInfo ACTIVITI = new ExporterInfo("Activiti");
	public static final ExporterInfo BIZAGI = new ExporterInfo("Bizagi");
	public static final ExporterInfo CAMUNDA = new ExporterInfo("Camunda");
	public static final ExporterInfo DROOLS_FLOW = new ExporterInfo("Drools Flow");
	public static final ExporterInfo FIXFLOW = new ExporterInfo("Fix Flow");
	public static final ExporterInfo FLOWABLE = new ExporterInfo("Flowable");
	public static final ExporterInfo SIGNAVIO = new ExporterInfo("Signavio Process Editor, http://www.signavio.com");
	
	public ExporterEstimator() {
		namespacesForTool = getNamespacesForTool();
	}
	
	private Map<String, ExporterInfo> getNamespacesForTool() {
		Map<String, ExporterInfo> result =  new HashMap<>();
		
		result.put("http://activiti.org/bpmn", ACTIVITI);
		result.put("http://www.founderfix.com/fixflow", FIXFLOW);
		result.put("http://www.jboss.org/drools/flow/gpd", DROOLS_FLOW);
		result.put("http://www.jboss.org/drools", DROOLS_FLOW);
		result.put("http://camunda.org/schema/1.0/bpmn", CAMUNDA);
		result.put("http://www.signavio.com/bpmn20", SIGNAVIO);
		result.put("http://flowable.org/bpmn", FLOWABLE);
		
		return result;
	}

	public ExporterInfo estimateExporter(Document bpmnDocument) {
		Element documentElement = bpmnDocument.getDocumentElement();
		
		String exporter = documentElement.getAttribute("exporter");
		String exporterVersion = documentElement.getAttribute("exporterVersion");
		
		if(!StringUtil.isEmpty(exporter)) {
			return new ExporterInfo(exporter, exporterVersion);
		} 
		
		List<String> namespaceURIs = XmlUtil.gatherDirectlyDeclaredNamespaceUris(documentElement);
		for(String namespaceUri : namespaceURIs) {
			ExporterInfo t = namespacesForTool.get(namespaceUri);
			if(t != null) {
				return t;
			}
		}
		
		String targetNamespace = documentElement.getAttribute("targetNamespace");
		if(!StringUtil.isEmpty(targetNamespace)) {
			if(namespacesForTool.containsKey(targetNamespace)) {
				return namespacesForTool.get(targetNamespace);
			}
			
			if("http://bpmn.io/schema/bpmn".equals(targetNamespace)) {
				return new ExporterInfo("bpmn.io?");
			} else if(targetNamespace.startsWith("http://www.trisotech.com/")) {
				return new ExporterInfo("Trisotech Workflow Modeler?");
			} else if(targetNamespace.startsWith("http://activiti.org")) {
				return ACTIVITI;
			}
		}
		
		try {
			Element firstProcessElement = (Element)documentElement.getElementsByTagNameNS("http://www.omg.org/spec/BPMN/20100524/MODEL", "process").item(0);
			if(
				firstProcessElement != null && 
				firstProcessElement.getAttribute("id") != null && 
				firstProcessElement.getAttribute("id").startsWith("sid-")
			) {
				return SIGNAVIO;
			}
		} catch(Exception e) {
			// ignore access to nowhere
		}
		
		// <bizagi:BizagiExtensions xmlns:bizagi="http://www.bizagi.com/bpmn20">
		if(documentElement.getElementsByTagNameNS("http://www.bizagi.com/bpmn20", "BizagiExtensions").getLength() > 0) {
			return BIZAGI;
		}
		
		return new ExporterInfo(null, null);
	}
	
}
