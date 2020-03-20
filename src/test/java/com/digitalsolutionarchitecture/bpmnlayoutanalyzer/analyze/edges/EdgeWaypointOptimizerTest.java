package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.WayPoint;

public class EdgeWaypointOptimizerTest {

	private EdgeWaypointOptimizer edgeWayPointOptimizer = new EdgeWaypointOptimizer();
	
	@Test
	public void test_OptimizeWayPoints_Optimizable() throws Exception {
		WayPoint p1 = new WayPoint(1.0, 1.0);
		WayPoint p2 = new WayPoint(2.0, 2.0);
		WayPoint p3 = new WayPoint(3.0, 3.0);
		
		List<WayPoint> optimizableList = new ArrayList<>(Arrays.asList(p1, p2, p3));
		assertTrue(edgeWayPointOptimizer.optimize(optimizableList));
		assertEquals(2, optimizableList.size());
		assertSame(p1, optimizableList.get(0));
		assertSame(p3, optimizableList.get(1));
	}
	
	@Test
	public void test_OptimizeWayPoints_NotOptimizable() throws Exception {
		WayPoint p1 = new WayPoint(1.0, 1.0);
		WayPoint p2 = new WayPoint(2.0, 2.0);
		WayPoint p3 = new WayPoint(1.0, 0.5);
		
		List<WayPoint> optimizableList = new ArrayList<>(Arrays.asList(p1, p2, p3));
		assertFalse(edgeWayPointOptimizer.optimize(optimizableList));
		assertEquals(3, optimizableList.size());
		assertSame(p1, optimizableList.get(0));
		assertSame(p2, optimizableList.get(1));
		assertSame(p3, optimizableList.get(2));
	}
	
	@Test
	public void test_OptimizeWayPoints_NotOptimizableZagged() throws Exception {
		WayPoint p1 = new WayPoint(215.0, 174.99999809265137);
		WayPoint p2 = new WayPoint(253.0, 174.99999809265137);
		WayPoint p3 = new WayPoint(253.0, 133.99999809265137);
		WayPoint p4 = new WayPoint(291.0, 133.99999809265137);
		
		List<WayPoint> optimizableList = new ArrayList<>(Arrays.asList(p1, p2, p3, p4));
		assertFalse(edgeWayPointOptimizer.optimize(optimizableList));
		assertEquals(4, optimizableList.size());
		assertSame(p1, optimizableList.get(0));
		assertSame(p2, optimizableList.get(1));
		assertSame(p3, optimizableList.get(2));
		assertSame(p4, optimizableList.get(3));
	}

}
