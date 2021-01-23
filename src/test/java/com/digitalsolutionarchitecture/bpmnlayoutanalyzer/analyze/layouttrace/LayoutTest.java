package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.layouttrace;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzerTest;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.layouttrace.Layout;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.layouttrace.LayoutIdentificator;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.util.CounterMap;

@RunWith(Parameterized.class)
public class LayoutTest {

	private LayoutIdentificator layoutIdentificator = new LayoutIdentificator();
	private String filename;
	private CounterMap<Layout> expectedLayouts;

	@Parameterized.Parameters(name = "process {0}[{2}] has layout {1}")
	public static Collection<Object[]> versionCombos() {

		return Arrays.asList(
//			new Object[] { "", Layout.PURE_LEFT_RIGHT, 0 },
			new Object[] { "leftright-verticalgateway.bpmn", new CounterMap<Layout>(Layout.LEFT_RIGHT_GATEWAY_VERTICAL_ALLOWED, Layout.LEFT_RIGHT_GATEWAY_VERTICAL_ALLOWED) },
			new Object[] { "topdown-horizontalgateway.bpmn", new CounterMap<Layout>(Layout.TOP_DOWN_GATEWAY_HORIZONTAL_ALLOWED, Layout.TOP_DOWN_GATEWAY_HORIZONTAL_ALLOWED) },
			new Object[] { "dirty-topdown.bpmn", new CounterMap<Layout>(Layout.TOP_DOWN_DIRTY) },
			new Object[] { "snake-east-2lines.bpmn", new CounterMap<Layout>(Layout.SNAKE_EAST) },
			new Object[] { "snake-east-3lines.bpmn", new CounterMap<Layout>(Layout.SNAKE_EAST) },
			new Object[] { "snake-east-4lines.bpmn", new CounterMap<Layout>(Layout.SNAKE_EAST) },
			new Object[] { "snake-south-2lines.bpmn", new CounterMap<Layout>(Layout.SNAKE_SOUTH) },
			new Object[] { "snake-south-3lines.bpmn", new CounterMap<Layout>(Layout.SNAKE_SOUTH) },
			new Object[] { "snake-south-4lines.bpmn", new CounterMap<Layout>(Layout.SNAKE_SOUTH) },
			new Object[] { "snake-south-5lines.bpmn", new CounterMap<Layout>(Layout.SNAKE_SOUTH) },
			new Object[] { "chaos.bpmn", new CounterMap<Layout>(Layout.OTHER) },
			new Object[] { "multiline-east-2lines.bpmn", new CounterMap<Layout>(Layout.MULTILINE_EAST) },
			new Object[] { "multiline-east-3lines.bpmn", new CounterMap<Layout>(Layout.MULTILINE_EAST) },
			new Object[] { "multiline-south-2lines.bpmn", new CounterMap<Layout>(Layout.MULTILINE_SOUTH) },
			new Object[] { "multiline-south-3lines.bpmn", new CounterMap<Layout>(Layout.MULTILINE_SOUTH) },
			new Object[] { "all-leftupperright.bpmn", new CounterMap<Layout>(Layout.DIAGONAL_NORTH_EAST) },
			new Object[] { "all-topbottom.bpmn", new CounterMap<Layout>(Layout.TOP_DOWN_PURE) },
			new Object[] { "eventprocesses-and-subprocesses.bpmn", new CounterMap<Layout>(Layout.LEFT_RIGHT_PURE, Layout.EVENT_SUBPROCESS, Layout.EVENT_SUBPROCESS, Layout.EVENT_SUBPROCESS, Layout.LEFT_RIGHT_PURE) },
			new Object[] { "expanded-subprocess.bpmn", new CounterMap<Layout>(Layout.LEFT_RIGHT_PURE, Layout.LEFT_RIGHT_PURE) },
			new Object[] { "one-connected-events-multiplestartend.bpmn", new CounterMap<Layout>(Layout.LEFT_RIGHT_PURE, Layout.LEFT_RIGHT_PURE, Layout.LEFT_RIGHT_PURE, Layout.LEFT_RIGHT_PURE) },
			new Object[] { "simple-xor-loop.bpmn", new CounterMap<Layout>(Layout.LEFT_RIGHT_PURE) },
			new Object[] { "two-connected-mixedevents-pools.bpmn", new CounterMap<Layout>(Layout.LEFT_RIGHT_PURE, Layout.LEFT_RIGHT_PURE) },
			new Object[] { "two-connected-mixedevents.bpmn", new CounterMap<Layout>(Layout.LEFT_RIGHT_PURE, Layout.LEFT_RIGHT_PURE) },
			new Object[] { "two-connected-noevents.bpmn", new CounterMap<Layout>(Layout.LEFT_RIGHT_PURE, Layout.LEFT_RIGHT_PURE) },
			new Object[] { "all-leftright.bpmn", new CounterMap<Layout>(Layout.LEFT_RIGHT_PURE) },
			new Object[] { "all-leftupperright-leftright.bpmn", new CounterMap<Layout>(Layout.LEFT_RIGHT_DIRTY) }
		);
	}

	public LayoutTest(String filename, CounterMap<Layout> expectedLayouts) {
	    this.filename = filename;
	    this.expectedLayouts = expectedLayouts;
	}
	
	@Test
	public void testCorrectIdentificationOfLayoutOfTraces() throws Exception {
		BpmnProcess p = IBpmnAnalyzerTest.readProcess("src/test/resources/" + filename);
		CounterMap<Layout> traces = layoutIdentificator.calculateTraceLayouts(p);

		assertEquals(traces, expectedLayouts);
	}
}
