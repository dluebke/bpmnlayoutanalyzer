package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class DoubleUtilTest {

	@Test
	public void test() {
		assertTrue(DoubleUtil.equals(1.0, 1.0, 0.0));
		assertTrue(DoubleUtil.equals(1.0, 0.99, 0.02));
		assertTrue(DoubleUtil.equals(0.99, 1.0, 0.02));
		
		assertFalse(DoubleUtil.equals(1.0, 0.99, 0.0));
	}

}
