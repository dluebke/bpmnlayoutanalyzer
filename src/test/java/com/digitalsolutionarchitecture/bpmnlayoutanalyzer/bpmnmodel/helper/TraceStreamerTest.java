package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.helper;

import static org.junit.Assert.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzerTest;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.Trace;

public class TraceStreamerTest {

	private TraceStreamer traceStreamer = new TraceStreamer();
	
	@Test
	public void test_Stream_1Trace() {
		BpmnProcess p = IBpmnAnalyzerTest.createProcess("A->B", "B->C");
		
		List<Trace> traces = traceStreamer.getNonLoopingTracesToEnd(p.getFlowNodeById("A"));
		
		assertEquals(1, traces.size());
		assertEquals(3, traces.get(0).getFlowNodes().size());
		assertEquals("A", traces.get(0).getFlowNodes().get(0).getId());
		assertEquals("B", traces.get(0).getFlowNodes().get(1).getId());
		assertEquals("C", traces.get(0).getFlowNodes().get(2).getId());
	}
	
	@Test
	public void test_Stream_2Traces_XOR() {
		BpmnProcess p = IBpmnAnalyzerTest.createProcess("A->XOR1", "XOR1->B", "XOR1->C", "B->XOR2", "C->XOR2", "XOR2->D");
		
		List<Trace> traces = traceStreamer.getNonLoopingTracesToEnd(p.getFlowNodeById("A"));
		
		assertEquals(2, traces.size());
		
		List<FlowNode> flowNodesTrace1 = traces.get(0).getFlowNodes();
		assertEquals(5, flowNodesTrace1.size());
		assertEquals("A", flowNodesTrace1.get(0).getId());
		assertEquals("XOR1", flowNodesTrace1.get(1).getId());
		assertEquals("B", flowNodesTrace1.get(2).getId());
		assertEquals("XOR2", flowNodesTrace1.get(3).getId());
		assertEquals("D", flowNodesTrace1.get(4).getId());
		
		List<FlowNode> flowNodesTrace2 = traces.get(1).getFlowNodes();
		assertEquals(5, flowNodesTrace2.size());
		assertEquals("A", flowNodesTrace2.get(0).getId());
		assertEquals("XOR1", flowNodesTrace2.get(1).getId());
		assertEquals("C", flowNodesTrace2.get(2).getId());
		assertEquals("XOR2", flowNodesTrace2.get(3).getId());
		assertEquals("D", flowNodesTrace2.get(4).getId());
	}
	
	@Test
	@Ignore
	public void testLargeModel() throws Exception {
		BpmnProcess p = IBpmnAnalyzerTest.readProcess("E:\\sync\\research-processes-danieldaniel-data\\repos_bpmn\\ajvarela-empiricalsoftware\\empiricalsoftware-master\\Resources\\clustersAGFeatures\\ERP\\BPMN\\cluster0.bpmn");
		
		try(FileWriter out = new FileWriter("traces.txt")) {
			traceStreamer.streamNonLoopingTracesToEnd(p.getStartFlowNodes().get(0), new ITraceReceiver() {
				
				int traces = 0;
				
				@Override
				public boolean nextTrace(FlowNode start, Trace t) {
					try {
						System.out.println(traces);
						out.write(t.toString());
						out.write("\n");
					} catch (IOException e) {
						e.printStackTrace();
						return false;
					}
					
					return ++traces < 10000000;
				}
			});
		}
		
	}

}
