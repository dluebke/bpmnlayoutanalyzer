package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.connectedness;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.junit.Test;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnxml.BpmnLayoutSetter;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnxml.BpmnReader;

public class ConnectednessAnalyzerTest {

	private BpmnReader reader = new BpmnReader();
	private BpmnLayoutSetter layoutSetter = new BpmnLayoutSetter();
	
	@Test
	public void analyzes_events_1process_correctly() throws Exception {
		BpmnProcess p = reader.readProcess("src/test/resources/one-connected-events.bpmn");
		
		layoutSetter.setLayoutData(p, 0);
		ConnectednessAnalyzer connectednessAnalyzer = new ConnectednessAnalyzer();
		connectednessAnalyzer.analyze(p);
		
		assertResult(connectednessAnalyzer.getResults(), Connectedness.SINGLE_PROCESS, StartAndEndConstellation.EVENTS);
	}
	

	@Test
	public void analyzes_noevents_1process_correctly() throws Exception {
		BpmnProcess p = reader.readProcess("src/test/resources/one-connected-noevents.bpmn");
		
		layoutSetter.setLayoutData(p, 0);
		ConnectednessAnalyzer connectednessAnalyzer = new ConnectednessAnalyzer();
		connectednessAnalyzer.analyze(p);
		
		assertResult(connectednessAnalyzer.getResults(), Connectedness.SINGLE_PROCESS, StartAndEndConstellation.NO_EVENTS);
	}
	
	@Test
	public void analyzes_events_2processes_correctly() throws Exception {
		BpmnProcess p = reader.readProcess("src/test/resources/two-connected-events.bpmn");
		
		layoutSetter.setLayoutData(p, 0);
		
		ConnectednessAnalyzer connectednessAnalyzer = new ConnectednessAnalyzer();
		connectednessAnalyzer.analyze(p);

		assertResult(connectednessAnalyzer.getResults(), Connectedness.MULTIPLE_PROCESSES, StartAndEndConstellation.EVENTS);
	}
	
	@Test
	public void analyzes_noevents_2processes_correctly() throws Exception {
		BpmnProcess p = reader.readProcess("src/test/resources/two-connected-noevents.bpmn");
		
		layoutSetter.setLayoutData(p, 0);
		
		ConnectednessAnalyzer connectednessAnalyzer = new ConnectednessAnalyzer();
		connectednessAnalyzer.analyze(p);
		
		assertResult(connectednessAnalyzer.getResults(), Connectedness.MULTIPLE_PROCESSES, StartAndEndConstellation.NO_EVENTS);
	}
	
	@Test
	public void analyzes_mixed_2processes_correctly() throws Exception {
		BpmnProcess p = reader.readProcess("src/test/resources/two-connected-mixedevents.bpmn");
		
		layoutSetter.setLayoutData(p, 0);
		
		ConnectednessAnalyzer connectednessAnalyzer = new ConnectednessAnalyzer();
		connectednessAnalyzer.analyze(p);
		
		assertResult(connectednessAnalyzer.getResults(), Connectedness.MULTIPLE_PROCESSES, StartAndEndConstellation.SOMETIMES_EVENTS_SOMETIMES_NOEVENTS);
	}
	
	@Test
	public void analyzes_incorrect_2processes_correctly() throws Exception {
		BpmnProcess p = reader.readProcess("src/test/resources/two-connected-incorrect.bpmn");
		
		layoutSetter.setLayoutData(p, 0);
		
		ConnectednessAnalyzer connectednessAnalyzer = new ConnectednessAnalyzer();
		connectednessAnalyzer.analyze(p);
		
		assertResult(connectednessAnalyzer.getResults(), Connectedness.MULTIPLE_PROCESSES, StartAndEndConstellation.INCORRECT);
	}
	
	@Test
	public void analyzes_incorrect_1process_correctly() throws Exception {
		BpmnProcess p = reader.readProcess("src/test/resources/one-connected-incorrect.bpmn");
		
		layoutSetter.setLayoutData(p, 0);
		
		ConnectednessAnalyzer connectednessAnalyzer = new ConnectednessAnalyzer();
		connectednessAnalyzer.analyze(p);
		
		assertResult(connectednessAnalyzer.getResults(), Connectedness.SINGLE_PROCESS, StartAndEndConstellation.INCORRECT);
	}

	private void assertResult(List<ConnectednessAnalyzerResult> results, Connectedness c,
			StartAndEndConstellation e) {
		assertEquals(1, results.size());
		assertSame(c, results.get(0).getValues().get(0));
		assertSame(e, results.get(0).getValues().get(1));
	}
}
