package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.layout;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzerTest;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;

@RunWith(Parameterized.class)
public class LayoutTest {

	private LayoutIdentificator layoutIdentificator = new LayoutIdentificator();
	private String filename;
	private Layout expectedLayout;
	private int traceNo;

	@Parameterized.Parameters(name = "process {0}[{2}] has layout {1}")
	public static Collection<Object[]> versionCombos() {

		return Arrays.asList(
//			new Object[] { "", Layout.PURE_LEFT_RIGHT, 0 },
			new Object[] { "leftright-verticalgateway.bpmn", Layout.LEFT_RIGHT_GATEWAY_VERTICAL_ALLOWED, 0 },
			new Object[] { "leftright-verticalgateway.bpmn", Layout.LEFT_RIGHT_GATEWAY_VERTICAL_ALLOWED, 1 },
			new Object[] { "topdown-horizontalgateway.bpmn", Layout.TOP_DOWN_GATEWAY_HORIZONTAL_ALLOWED, 0 },
			new Object[] { "topdown-horizontalgateway.bpmn", Layout.TOP_DOWN_GATEWAY_HORIZONTAL_ALLOWED, 1 },
			new Object[] { "dirty-topdown.bpmn", Layout.DIRTY_TOP_DOWN, 0 },
			new Object[] { "snake-east-2lines.bpmn", Layout.SNAKE_EAST, 0 },
			new Object[] { "snake-east-3lines.bpmn", Layout.SNAKE_EAST, 0 },
			new Object[] { "snake-east-4lines.bpmn", Layout.SNAKE_EAST, 0 },
			new Object[] { "snake-south-2lines.bpmn", Layout.SNAKE_SOUTH, 0 },
			new Object[] { "snake-south-3lines.bpmn", Layout.SNAKE_SOUTH, 0 },
			new Object[] { "snake-south-4lines.bpmn", Layout.SNAKE_SOUTH, 0 },
			new Object[] { "snake-south-5lines.bpmn", Layout.SNAKE_SOUTH, 0 },
			new Object[] { "chaos.bpmn", Layout.OTHER, 0 },
			new Object[] { "multiline-east-2lines.bpmn", Layout.MULTILINE_EAST, 0 },
			new Object[] { "multiline-east-3lines.bpmn", Layout.MULTILINE_EAST, 0 },
			new Object[] { "multiline-south-2lines.bpmn", Layout.MULTILINE_SOUTH, 0 },
			new Object[] { "multiline-south-3lines.bpmn", Layout.MULTILINE_SOUTH, 0 },
			new Object[] { "all-leftupperright.bpmn", Layout.DIAGONAL_NORTH_EAST, 0 },
			new Object[] { "all-topbottom.bpmn", Layout.PURE_TOP_DOWN, 0 },
			new Object[] { "eventprocesses-and-subprocesses.bpmn", Layout.PURE_LEFT_RIGHT, 0 },
			new Object[] { "eventprocesses-and-subprocesses.bpmn", Layout.EVENT_SUBPROCESS, 1 },
			new Object[] { "eventprocesses-and-subprocesses.bpmn", Layout.EVENT_SUBPROCESS, 2 },
			new Object[] { "eventprocesses-and-subprocesses.bpmn", Layout.EVENT_SUBPROCESS, 3 },
			new Object[] { "eventprocesses-and-subprocesses.bpmn", Layout.PURE_LEFT_RIGHT, 4 },
			new Object[] { "expanded-subprocess.bpmn", Layout.PURE_LEFT_RIGHT, 0 },
			new Object[] { "expanded-subprocess.bpmn", Layout.PURE_LEFT_RIGHT, 1 },
			new Object[] { "one-connected-events-multiplestartend.bpmn", Layout.PURE_LEFT_RIGHT, 0 },
			new Object[] { "one-connected-events-multiplestartend.bpmn", Layout.PURE_LEFT_RIGHT, 1 },
			new Object[] { "one-connected-events-multiplestartend.bpmn", Layout.PURE_LEFT_RIGHT, 2 },
			new Object[] { "one-connected-events-multiplestartend.bpmn", Layout.PURE_LEFT_RIGHT, 3 },
			new Object[] { "simple-xor-loop.bpmn", Layout.PURE_LEFT_RIGHT, 0 },
			new Object[] { "two-connected-mixedevents-pools.bpmn", Layout.PURE_LEFT_RIGHT, 0 },
			new Object[] { "two-connected-mixedevents-pools.bpmn", Layout.PURE_LEFT_RIGHT, 1 },
			new Object[] { "two-connected-mixedevents.bpmn", Layout.PURE_LEFT_RIGHT, 0 },
			new Object[] { "two-connected-mixedevents.bpmn", Layout.PURE_LEFT_RIGHT, 1 },
			new Object[] { "two-connected-noevents.bpmn", Layout.PURE_LEFT_RIGHT, 0 },
			new Object[] { "two-connected-noevents.bpmn", Layout.PURE_LEFT_RIGHT, 1 },
			new Object[] { "all-leftright.bpmn", Layout.PURE_LEFT_RIGHT, 0 },
			new Object[] { "all-leftupperright-leftright.bpmn", Layout.DIRTY_LEFT_RIGHT, 0 }
		);
	}

	public LayoutTest(String filename, Layout expectedLayout, int trace) {
	    this.filename = filename;
	    this.expectedLayout = expectedLayout;
	    this.traceNo = trace;
	}
	
	@Test
	public void testCorrectIdentificationOfLayoutOfTrace() throws Exception {
		BpmnProcess p = IBpmnAnalyzerTest.readProcess("src/test/resources/" + filename);
		List<SequenceFlowTrace> traces = layoutIdentificator.extractSequenceFlowTraces(p);

		SequenceFlowTrace trace = traces.get(traceNo);

		assertEquals(filename, expectedLayout, Layout.evaluateLayout(trace));
	}

}
