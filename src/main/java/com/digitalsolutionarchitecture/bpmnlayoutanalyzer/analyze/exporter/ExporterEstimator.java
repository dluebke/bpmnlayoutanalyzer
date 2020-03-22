package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.exporter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnaylzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.Result;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.util.StringUtil;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.util.XmlUtil;

public class ExporterEstimator implements IBpmnAnaylzer {

	private Map<String, ExporterInfo> namespacesForTool;

	private ExporterInfo exporterInfo;
	
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

	@Override
	public void performPreAnalysisOfModel(Document bpmnDocument) {
		Element documentElement = bpmnDocument.getDocumentElement();
		
		String exporter = documentElement.getAttribute("exporter");
		String exporterVersion = documentElement.getAttribute("exporterVersion");
		
		if(!StringUtil.isEmpty(exporter)) {
			this.exporterInfo = new ExporterInfo(exporter, exporterVersion);
			return;
		} 
		
		List<String> namespaceURIs = XmlUtil.gatherDirectlyDeclaredNamespaceUris(documentElement);
		for(String namespaceUri : namespaceURIs) {
			ExporterInfo t = namespacesForTool.get(namespaceUri);
			if(t != null) {
				exporterInfo = t;
				return;
			}
		}
		
		String targetNamespace = documentElement.getAttribute("targetNamespace");
		if(!StringUtil.isEmpty(targetNamespace)) {
			if(namespacesForTool.containsKey(targetNamespace)) {
				exporterInfo = namespacesForTool.get(targetNamespace);
				return;
			}
			
			if("http://bpmn.io/schema/bpmn".equals(targetNamespace)) {
				exporterInfo = new ExporterInfo("bpmn.io?");
				return;
			} else if(targetNamespace.startsWith("http://www.trisotech.com/")) {
				exporterInfo = new ExporterInfo("Trisotech Workflow Modeler?");
				return;
			} else if(targetNamespace.startsWith("http://activiti.org")) {
				exporterInfo = ACTIVITI;
				return;
			}
		}
		
		try {
			Element firstProcessElement = (Element)documentElement.getElementsByTagNameNS("http://www.omg.org/spec/BPMN/20100524/MODEL", "process").item(0);
			if(firstProcessElement.getAttribute("id").startsWith("sid-")) {
				exporterInfo = SIGNAVIO;
				return;
			}
		} catch(Exception e) {
			// ignore access to nowhere
		}
		
		// <bizagi:BizagiExtensions xmlns:bizagi="http://www.bizagi.com/bpmn20">
		if(documentElement.getElementsByTagNameNS("http://www.bizagi.com/bpmn20", "BizagiExtensions").getLength() > 0) {
			exporterInfo = BIZAGI;
			return;
		}
		
		exporterInfo = new ExporterInfo(null, null);
	}
	
	@Override
	public void analyse(Element bpmnDiagram, Result result) {
		result.setExporter(exporterInfo.getExporter());
		result.setExporterVersion(exporterInfo.getExporterVersion());
	}
	
}
