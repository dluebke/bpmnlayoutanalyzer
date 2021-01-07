package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.xml.sax.SAXException;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.SequenceFlow;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnxml.BpmnLayoutSetter;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnxml.BpmnReader;

public abstract class IBpmnAnalyzerTest<T extends IBpmnAnalyzer> {

	protected abstract T createAnalyzer();
	
	private T analyzer;
	private static BpmnReader reader = new BpmnReader();
	private static BpmnLayoutSetter layoutSetter = new BpmnLayoutSetter();
	
	protected T getAnalyzer() {
		return analyzer;
	}
	
	protected void assertResultInvariants(Result r) {
		assertEquals("Value count must match header count", analyzer.getHeaders().length, r.getValues().size());
	}
	
	@Before
	public final void setUpIBpmnAnalyzerTest() {
		this.analyzer = createAnalyzer();
	}

	public static BpmnProcess readProcess(InputStream in, String filename) throws FileNotFoundException, SAXException, IOException {
		return readProcess(in, filename, 0);
	}
	
	public static BpmnProcess readProcess(InputStream in, String filename, int diagramNo) throws FileNotFoundException, SAXException, IOException {
		BpmnProcess process = reader.readProcess(filename, in);
		layoutSetter.setLayoutData(process, diagramNo);
		return process;
	}
	
	public static BpmnProcess readProcess(String filename) throws FileNotFoundException, SAXException, IOException {
		return readProcess(filename, 0);
	}
	
	public static BpmnProcess readProcess(String filename, int diagramNo) throws FileNotFoundException, SAXException, IOException {
		BpmnProcess process = reader.readProcess(filename);
		layoutSetter.setLayoutData(process, diagramNo);
		return process;
	}
	
	public static BpmnProcess createProcess(String... flowDescriptions) {
		BpmnProcess p = new BpmnProcess("test.bpmn", null);
		
		Map<String, FlowNode> flowNodes = new HashMap<>();
		
		for(String flowDescription : flowDescriptions) {
			String[] nodes = flowDescription.split("->");
			String startNodeId = nodes[0].trim();
			String endNodeId = nodes[1].trim();
			
			FlowNode startNode = flowNodes.get(startNodeId);
			if(startNode == null) {
				startNode = new FlowNode(startNodeId, "Task", p);
				flowNodes.put(startNodeId, startNode);
				p.add(startNode);
			}
			FlowNode endNode = flowNodes.get(endNodeId);
			if(endNode == null) {
				endNode = new FlowNode(endNodeId, "Task", p);
				flowNodes.put(endNodeId, endNode);
				p.add(endNode);
			}
			
			p.add(new SequenceFlow("sf1", startNode, endNode));	
		}
		
		return p;
	}
}
