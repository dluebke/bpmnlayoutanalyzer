package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.layouttrace;

import static org.junit.Assert.*;

import org.junit.Test;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.layouttrace.Layout;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.layouttrace.TraceToDiagramLayoutCalulator;

public class TraceToDiagramLayoutCalulatorTest {

	@Test
	public void testLayoutPair_Equals() throws Exception {
		TraceToDiagramLayoutCalulator.LayoutPair p1 = new TraceToDiagramLayoutCalulator.LayoutPair(Layout.LEFT_RIGHT_PURE, Layout.TOP_DOWN_PURE);
		TraceToDiagramLayoutCalulator.LayoutPair p1a = new TraceToDiagramLayoutCalulator.LayoutPair(Layout.LEFT_RIGHT_PURE, Layout.TOP_DOWN_PURE);
		TraceToDiagramLayoutCalulator.LayoutPair p2 = new TraceToDiagramLayoutCalulator.LayoutPair(Layout.TOP_DOWN_PURE, Layout.LEFT_RIGHT_PURE);
		
		assertEquals(p1, p2);
		assertEquals(p1, p1);
		assertEquals(p1, p1a);
	}
	
	@Test
	public void test() {
		TraceToDiagramLayoutCalulator c = new TraceToDiagramLayoutCalulator();
		assertEquals(Layout.LEFT_RIGHT_DIRTY, c.combineLayout(Layout.LEFT_RIGHT_DIRTY, Layout.LEFT_RIGHT_PURE));
		assertEquals(Layout.LEFT_RIGHT_DIRTY, c.combineLayout(Layout.LEFT_RIGHT_PURE, Layout.LEFT_RIGHT_DIRTY));
	}
	
}
