package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.connectedness;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.junit.Test;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzerTest;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;

public class ConnectednessAnalyzerTest extends IBpmnAnalyzerTest<ConnectednessAnalyzer> {

	@Test
	public void analyzes_events_1process_correctly() throws Exception {
		BpmnProcess p = readProcess("src/test/resources/one-connected-events.bpmn");
		
		getAnalyzer().analyze(p);
		
		assertCorrectResult(
			getAnalyzer().getResults(),
			Connectedness.SINGLE_PROCESS,
			StartAndEndConstellation.EVENTS,
			1,
			1,
			1
		);
	}
	

	@Test
	public void analyzes_noevents_1process_correctly() throws Exception {
		BpmnProcess p = readProcess("src/test/resources/one-connected-noevents.bpmn");
		
		getAnalyzer().analyze(p);
		
		assertCorrectResult(
			getAnalyzer().getResults(), 
			Connectedness.SINGLE_PROCESS, 
			StartAndEndConstellation.NO_EVENTS, 
			1,
			1,
			1
		);
	}
	
	@Test
	public void analyzes_events_2processes_correctly() throws Exception {
		BpmnProcess p = readProcess("src/test/resources/two-connected-events.bpmn");
		
		getAnalyzer().analyze(p);

		assertCorrectResult(
			getAnalyzer().getResults(), 
			Connectedness.MULTIPLE_PROCESSES, 
			StartAndEndConstellation.EVENTS, 
			2,
			2,
			2
		);
	}
	
	@Test
	public void analyzes_noevents_2processes_correctly() throws Exception {
		BpmnProcess p = readProcess("src/test/resources/two-connected-noevents.bpmn");
		
		getAnalyzer().analyze(p);
		
		assertCorrectResult(
			getAnalyzer().getResults(),
			Connectedness.MULTIPLE_PROCESSES, 
			StartAndEndConstellation.NO_EVENTS, 
			2,
			2,
			2
		);
	}
	
	@Test
	public void analyzes_mixed_2processes_correctly() throws Exception {
		BpmnProcess p = readProcess("src/test/resources/two-connected-mixedevents.bpmn");
		
		getAnalyzer().analyze(p);
		
		assertCorrectResult(
			getAnalyzer().getResults(), 
			Connectedness.MULTIPLE_PROCESSES, 
			StartAndEndConstellation.SOMETIMES_EVENTS_SOMETIMES_NOEVENTS, 
			2,
			2,
			2
		);
	}
	
	@Test
	public void analyzes_mixed_2processes_withpools_correctly() throws Exception {
		BpmnProcess p = readProcess("src/test/resources/two-connected-mixedevents-pools.bpmn");
		
		getAnalyzer().analyze(p);
		
		assertCorrectResult(
			getAnalyzer().getResults(), 
			Connectedness.MULTIPLE_PROCESSES, 
			StartAndEndConstellation.SOMETIMES_EVENTS_SOMETIMES_NOEVENTS, 
			2,
			2,
			2
		);
	}
	
	@Test
	public void analyzes_incorrect_2processes_correctly() throws Exception {
		BpmnProcess p = readProcess("src/test/resources/two-connected-incorrect.bpmn");
		
		getAnalyzer().analyze(p);
		
		assertCorrectResult(
			getAnalyzer().getResults(), 
			Connectedness.MULTIPLE_PROCESSES, 
			StartAndEndConstellation.INCORRECT, 
			2,
			2,
			2
		);
	}
	
	@Test
	public void analyzes_incorrect_1process_correctly() throws Exception {
		BpmnProcess p = readProcess("src/test/resources/one-connected-incorrect.bpmn");
		
		getAnalyzer().analyze(p);
		
		assertCorrectResult(
			getAnalyzer().getResults(), 
			Connectedness.SINGLE_PROCESS, 
			StartAndEndConstellation.INCORRECT, 
			1,
			1,
			1
		);
	}
	
	@Test
	public void analyzes_correct_1process_multiplestartsandends_correctly() throws Exception {
		BpmnProcess p = readProcess("src/test/resources/one-connected-events-multiplestartend.bpmn");
		
		getAnalyzer().analyze(p);
		
		assertCorrectResult(
				getAnalyzer().getResults(), 
				Connectedness.SINGLE_PROCESS, 
				StartAndEndConstellation.EVENTS, 
				2,
				2,
				1
				);
	}

	private void assertCorrectResult(
			List<ConnectednessAnalyzerResult> results, Connectedness c,
			StartAndEndConstellation e,
			int startFlowNodeCount,
			int endFlowNodeCount,
			int subgraphCount
		) {
		
		assertEquals(1, results.size());
		ConnectednessAnalyzerResult result = results.get(0);
		assertResultInvariants(result);
		
		assertSame(result.getConnectedness(), result.getValues().get(0));
		assertSame(result.getStartAndEnd(), result.getValues().get(1));
		assertEquals(result.getStartFlowNodeCount(), result.getValues().get(2));
		assertEquals(result.getEndFlowNodeCount(), result.getValues().get(3));
		assertEquals(result.getSubgraphCount(), result.getValues().get(4));
		
		assertSame(c, result.getConnectedness());
		assertSame(e, result.getStartAndEnd());
		assertEquals(startFlowNodeCount, result.getStartFlowNodeCount());
		assertEquals(endFlowNodeCount, result.getStartFlowNodeCount());
		assertEquals(subgraphCount, result.getSubgraphCount());
	}


	@Override
	protected ConnectednessAnalyzer createAnalyzer() {
		return new ConnectednessAnalyzer();
	}
}
