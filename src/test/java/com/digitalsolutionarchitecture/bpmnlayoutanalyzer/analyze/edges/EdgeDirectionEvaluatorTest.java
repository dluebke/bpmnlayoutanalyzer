package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.WayPoint;

public class EdgeDirectionEvaluatorTest {

	private EdgeDirectionEvaluator edgeDirectionEvaluator = new EdgeDirectionEvaluator();
	
	@Test
	public void test_Direct_LeftRight() {
		List<WayPoint> waypoints = Arrays.asList(
			new WayPoint(1.0, 1.0), 
			new WayPoint(2.0, 1.0)
		);
		assertEquals(EdgeDirection.DIRECT_EAST, edgeDirectionEvaluator.evaluateArcType(waypoints));
	}
	
	@Test
	public void test_Direct_TopBottom() {
		List<WayPoint> waypoints = Arrays.asList(
				new WayPoint(1.0, 1.0), 
				new WayPoint(1.0, 2.0)
				);
		assertEquals(EdgeDirection.DIRECT_SOUTH, edgeDirectionEvaluator.evaluateArcType(waypoints));
	}
	
	@Test
	public void test_Direct_RightLeft() {
		List<WayPoint> waypoints = Arrays.asList(
				new WayPoint(2.0, 1.0), 
				new WayPoint(1.0, 1.0)
				);
		assertEquals(EdgeDirection.DIRECT_WEST, edgeDirectionEvaluator.evaluateArcType(waypoints));
	}
	
	@Test
	public void test_Direct_BottomTop() {
		List<WayPoint> waypoints = Arrays.asList(
				new WayPoint(1.0, 2.0), 
				new WayPoint(1.0, 1.0)
				);
		assertEquals(EdgeDirection.DIRECT_NORTH, edgeDirectionEvaluator.evaluateArcType(waypoints));
	}
	
	@Test
	public void test_Direct_LeftUpperRight() {
		List<WayPoint> waypoints = Arrays.asList(
			new WayPoint(1.0, 1.0), 
			new WayPoint(2.0, 0.0)
		);
		assertEquals(EdgeDirection.DIRECT_NORTHEAST, edgeDirectionEvaluator.evaluateArcType(waypoints));
	}
	
	@Test
	public void test_Direct_LeftLowerRight() {
		List<WayPoint> waypoints = Arrays.asList(
			new WayPoint(1.0, 1.0), 
			new WayPoint(2.0, 2.0)
		);
		assertEquals(EdgeDirection.DIRECT_SOUTHEAST, edgeDirectionEvaluator.evaluateArcType(waypoints));
	}
	
	@Test
	public void test_Direct_RightUpperLeft() {
		List<WayPoint> waypoints = Arrays.asList(
			new WayPoint(1.0, 1.0), 
			new WayPoint(0.0, 0.0)
		);
		assertEquals(EdgeDirection.DIRECT_NORTHWEST, edgeDirectionEvaluator.evaluateArcType(waypoints));
	}
	
	@Test
	public void test_Direct_RightLowerLeft() {
		List<WayPoint> waypoints = Arrays.asList(
			new WayPoint(1.0, 1.0), 
			new WayPoint(0.0, 2.0)
		);
		assertEquals(EdgeDirection.DIRECT_SOUTHWEST, edgeDirectionEvaluator.evaluateArcType(waypoints));
	}

	@Test
	public void test_Zagged_TopBottom() {
		List<WayPoint> waypoints = Arrays.asList(
			new WayPoint(0.0, 0.0), 
			new WayPoint(1.0, 0.0),
			new WayPoint(1.0, 1.0),
			new WayPoint(0.0, 1.0)
		);
		assertEquals(EdgeDirection.ZAGGED_SOUTH, edgeDirectionEvaluator.evaluateArcType(waypoints));
	}
	
	@Test
	public void test_Zagged_BottomTop() {
		List<WayPoint> waypoints = Arrays.asList(
			new WayPoint(1.0, 1.0), 
			new WayPoint(0.0, 1.0),
			new WayPoint(0.0, 0.0),
			new WayPoint(1.0, 0.0)
		);
		assertEquals(EdgeDirection.ZAGGED_NORTH, edgeDirectionEvaluator.evaluateArcType(waypoints));
	}
	
	@Test
	public void test_Zagged_LeftUpperRight() {
		List<WayPoint> waypoints = Arrays.asList(
			new WayPoint(0.0, 1.0), 
			new WayPoint(1.0, 1.0),
			new WayPoint(1.0, 0.0),
			new WayPoint(2.0, 0.0)
		);
		assertEquals(EdgeDirection.ZAGGED_NORTHEAST, edgeDirectionEvaluator.evaluateArcType(waypoints));
	}
	
	@Test
	public void test_Zagged_LeftLowerRight() {
		List<WayPoint> waypoints = Arrays.asList(
			new WayPoint(0.0, 0.0), 
			new WayPoint(1.0, 0.0),
			new WayPoint(1.0, 1.0),
			new WayPoint(2.0, 1.0)
		);
		assertEquals(EdgeDirection.ZAGGED_SOUTHEAST, edgeDirectionEvaluator.evaluateArcType(waypoints));
	}
	
	@Test
	public void test_Zagged_RightUpperLeft() {
		List<WayPoint> waypoints = Arrays.asList(
			new WayPoint(2.0, 1.0), 
			new WayPoint(1.0, 1.0),
			new WayPoint(1.0, 0.0),
			new WayPoint(0.0, 0.0)
		);
		assertEquals(EdgeDirection.ZAGGED_NORTHWEST, edgeDirectionEvaluator.evaluateArcType(waypoints));
	}
	
	@Test
	public void test_Zagged_RightLowerLeft() {
		List<WayPoint> waypoints = Arrays.asList(
			new WayPoint(2.0, 0.0), 
			new WayPoint(1.0, 0.0),
			new WayPoint(1.0, 1.0),
			new WayPoint(0.0, 1.0)
		);
		assertEquals(EdgeDirection.ZAGGED_SOUTHWEST, edgeDirectionEvaluator.evaluateArcType(waypoints));
	}
	
	
}
