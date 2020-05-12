package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel;

import static org.junit.Assert.*;

import org.junit.Test;

public class BoundsTest {

	@Test
	public void testExtendTo() {
		Bounds b1 = new Bounds(0, 0, 10, 10);
		Bounds b2 = new Bounds(10, 10, 10, 10);
		
		b1.extendTo(b2);
		assertEquals(new Bounds(0, 0, 20, 20), b1);
		
		Bounds b3 = new Bounds(-10, -10, 10, 10);
		b1.extendTo(b3);
		assertEquals(new Bounds(-10,-10, 30, 30), b1);
	}

}
