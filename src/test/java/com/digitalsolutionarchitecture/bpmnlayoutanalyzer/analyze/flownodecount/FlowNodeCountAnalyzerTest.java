package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.flownodecount;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzerTest;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;

public class FlowNodeCountAnalyzerTest extends IBpmnAnalyzerTest<FlowNodeCountAnalyzer> {

	@Test
	public void counts_events_and_tasks() throws Exception {
		BpmnProcess p = readProcess("src/test/resources/all-leftright.bpmn");
		
		getAnalyzer().analyze(p);
		assertResult(3, 1);
	}
	
	@Test
	public void counts_in_pools() throws Exception {
		BpmnProcess p = readProcess("src/test/resources/two-connected-mixedevents-pools.bpmn");
		
		getAnalyzer().analyze(p);
		assertResult(6, 4);
	}
	
	@Test
	public void counts_elements_in_subprocess() throws Exception {
		BpmnProcess p = readProcess("src/test/resources/expanded-subprocess.bpmn");
		
		getAnalyzer().analyze(p);
		assertResult(6, 2, 1, 0);
	}
	
	@Test
	public void counts_elements_large_github_process() throws Exception {
		BpmnProcess p = readProcess("src/test/resources/github/IntermittentQRADStatus.bpmn");
		
		getAnalyzer().analyze(p);
		assertResult(85, 34);
	}
	
	@Test
	public void counts_expanded_subprocesses_and_eventprocesses() throws Exception {
		BpmnProcess p = readProcess("src/test/resources/eventprocesses-and-subprocesses.bpmn");
		
		getAnalyzer().analyze(p);
		assertResult(12, 8, 1, 2, 1, 2);
	}
	
	private void assertResult(int flowNodeCount, int activityCount) {
		assertResult(flowNodeCount, activityCount, 0, 0);
	}
	
	private void assertResult(int flowNodeCount, int activityCount, int nonEmptySubProcessCount, int nonEmptyEventProcessCount) {
		assertResult(flowNodeCount, activityCount, nonEmptySubProcessCount, 0, nonEmptyEventProcessCount, 0);
	}
	
	private void assertResult(
		int flowNodeCount, 
		int activityCount, 
		int nonEmptySubProcessCount, 
		int emptySubProcessCount, 
		int nonEmptyEventProcessCount, 
		int emptyEventProcessCount
		) {
		
		List<FlowNodeCounts> results = getAnalyzer().getResults();
		assertEquals(1, results.size());
		
		FlowNodeCounts result = results.get(0);
		assertResultInvariants(result);
		assertEquals(result.getTotalFlowNodeCount(), result.getValues().get(0));
		assertEquals(result.getActivityCount(), result.getValues().get(1));
		assertEquals(result.getNonEmptySubProcessCount(), result.getValues().get(2));
		assertEquals(result.getEmptySubProcessCount(), result.getValues().get(3));
		assertEquals(result.getNonEmptyEventProcessCount(), result.getValues().get(4));
		assertEquals(result.getEmptyEventProcessCount(), result.getValues().get(5));
		
		assertEquals(flowNodeCount, result.getTotalFlowNodeCount());
		assertEquals(activityCount, result.getActivityCount());
		assertEquals(nonEmptySubProcessCount, result.getNonEmptySubProcessCount());
		assertEquals(emptySubProcessCount, result.getEmptySubProcessCount());
		assertEquals(nonEmptyEventProcessCount, result.getNonEmptyEventProcessCount());
		assertEquals(emptyEventProcessCount, result.getEmptyEventProcessCount());
	}

	@Override
	protected FlowNodeCountAnalyzer createAnalyzer() {
		return new FlowNodeCountAnalyzer();
	}

}
