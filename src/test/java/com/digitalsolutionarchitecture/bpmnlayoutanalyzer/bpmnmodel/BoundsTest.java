package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel;

import static org.junit.Assert.*;

import org.junit.Test;

public class BoundsTest {

	@Test
	public void extendTo_West() {
		Bounds b1 = new Bounds(0, 0, 10, 10);
		Bounds b2 = new Bounds(-10, 0, 10, 10);

		b1.extendTo(b2);
		
		assertEquals(new Bounds(-10, 0, 20, 10), b1);
	}
	
	@Test
	public void extendTo_East() {
		Bounds b1 = new Bounds(0, 0, 10, 10);
		Bounds b2 = new Bounds(10, 0, 10, 10);

		b1.extendTo(b2);
		
		assertEquals(new Bounds(0, 0, 20, 10), b1);
	}
	
	@Test
	public void extendTo_North() {
		Bounds b1 = new Bounds(0, 0, 10, 10);
		Bounds b2 = new Bounds(0, -10, 10, 10);

		b1.extendTo(b2);
		
		assertEquals(new Bounds(0, -10, 10, 20), b1);
	}
	
	@Test
	public void extendTo_South() {
		Bounds b1 = new Bounds(0, 0, 10, 10);
		Bounds b2 = new Bounds(0, 10, 10, 10);

		b1.extendTo(b2);
		
		assertEquals(new Bounds(0, 00, 10, 20), b1);
	}
	
	@Test
	public void extendTo_NorthEast() {
		Bounds b1 = new Bounds(0, 0, 10, 10);
		Bounds b2 = new Bounds(10, 10, 10, 10);

		b1.extendTo(b2);
		
		assertEquals(new Bounds(0, 0, 20, 20), b1);
	}

	@Test
	public void extendTo_NorthWest() {
		Bounds b1 = new Bounds(0, 0, 10, 10);
		Bounds b2 = new Bounds(-10, -10, 10, 10);

		b1.extendTo(b2);
		
		assertEquals(new Bounds(-10, -10, 20, 20), b1);
	}
	
	
	@Test
	public void extendTo_SouthEast() {
		Bounds b1 = new Bounds(0, 0, 10, 10);
		Bounds b2 = new Bounds(10, 10, 10, 10);

		b1.extendTo(b2);
		
		assertEquals(new Bounds(0, 0, 20, 20), b1);
	}
	
	@Test
	public void extendTo_SouthWest() {
		Bounds b1 = new Bounds(0, 0, 10, 10);
		Bounds b2 = new Bounds(-10, 10, 10, 10);

		b1.extendTo(b2);
		
		assertEquals(new Bounds(-10, 0, 20, 20), b1);
	}
	
}
