package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel;

import static org.junit.Assert.*;

import org.junit.Test;

public class FlowNodeTest {

	@Test
	public void test() {
		FlowNode n1 = new FlowNode(null, null, null);
		FlowNode n2 = new FlowNode(null, null, null);
		SequenceFlow sf = new SequenceFlow(null, n1, n2);
		
		assertSame(sf, n1.getSequenceFlowTo(n2));
	}

}
