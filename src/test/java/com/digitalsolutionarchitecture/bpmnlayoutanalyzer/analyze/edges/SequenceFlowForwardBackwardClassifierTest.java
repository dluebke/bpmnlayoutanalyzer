package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.SequenceFlow;

public class SequenceFlowForwardBackwardClassifierTest {

	private SequenceFlowForwardBackwardClassifier classifier = new SequenceFlowForwardBackwardClassifier();
	
	@Test
	public void classifies_single_forward_sequence_flow() {
		BpmnProcess p = new BpmnProcess("test.bpmn", null);
		FlowNode n1 = new FlowNode("n1", "startEvent", p, "n1");
		p.add(n1);
		FlowNode n2 = new FlowNode("n2", "endEvent", p, "n2");
		p.add(n2);
		SequenceFlow sf1 = new SequenceFlow("sf1", n1, n2);
		p.add(sf1);
		
		Map<SequenceFlow, DirectionType> result = classifier.evaluateDirection(p);
		
		assertEquals(1, result.size());
		assertEquals(DirectionType.FORWARD, result.get(sf1));
	}
	
	@Test
	public void classifies_all_forward_sequence_flows() {
		BpmnProcess p = new BpmnProcess("test.bpmn", null);
		FlowNode n1 = new FlowNode("n1", "startEvent", p, "n1");
		p.add(n1);
		FlowNode n2 = new FlowNode("n2", "serviceTask", p, "n2");
		p.add(n2);
		FlowNode n3 = new FlowNode("n3", "endEvent", p, "n3");
		p.add(n3);
		SequenceFlow sf1 = new SequenceFlow("sf1", n1, n2);
		p.add(sf1);
		SequenceFlow sf2 = new SequenceFlow("sf2", n2, n3);
		p.add(sf2);
		
		Map<SequenceFlow, DirectionType> result = classifier.evaluateDirection(p);
		
		assertEquals(2, result.size());
		assertEquals(DirectionType.FORWARD, result.get(sf1));
		assertEquals(DirectionType.FORWARD, result.get(sf2));
	}
	
	@Test
	public void classifies_cycle() {
		BpmnProcess p = new BpmnProcess("test.bpmn", null);
		FlowNode n1 = new FlowNode("n1", "startEvent", p, "n1");
		p.add(n1);
		FlowNode n2 = new FlowNode("n2", "serviceTask", p, "n2");
		p.add(n2);
		FlowNode n3 = new FlowNode("n3", "serviceTask", p, "n3");
		p.add(n3);
		FlowNode n4 = new FlowNode("n4", "endEvent", p, "n4");
		p.add(n4);
		SequenceFlow sf1 = new SequenceFlow("sf1", n1, n2);
		p.add(sf1);
		SequenceFlow sf2 = new SequenceFlow("sf2", n2, n3);
		p.add(sf2);
		SequenceFlow sf3 = new SequenceFlow("sf3", n3, n4);
		p.add(sf3);
		SequenceFlow sf4 = new SequenceFlow("sf4", n3, n2);
		p.add(sf4);
		
		Map<SequenceFlow, DirectionType> result = classifier.evaluateDirection(p);
		
		assertEquals(4, result.size());
		assertEquals(DirectionType.FORWARD, result.get(sf1));
		assertEquals(DirectionType.FORWARD, result.get(sf2));
		assertEquals(DirectionType.FORWARD, result.get(sf3));
		assertEquals(DirectionType.BACKWARD, result.get(sf4));
	}

}
