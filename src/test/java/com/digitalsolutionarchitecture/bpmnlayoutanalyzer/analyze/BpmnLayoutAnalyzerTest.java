package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.EdgeDirection;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.BpmnLayoutAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.Result;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.WayPoint;

public class BpmnLayoutAnalyzerTest {

	@Test
	public void testCalcDegree() {
		WayPoint wp1 = new WayPoint(0, 0);
		WayPoint wp2 = new WayPoint(10, 0);
		assertEquals(0, BpmnLayoutAnalyzer.calDegree(wp1, wp2));
		
		WayPoint wp3 = new WayPoint(0, 10);
		assertEquals(90, BpmnLayoutAnalyzer.calDegree(wp1, wp3));
		
		WayPoint wp4 = new WayPoint(-10, 0);
		assertEquals(180, BpmnLayoutAnalyzer.calDegree(wp1, wp4));
		
		WayPoint wp5 = new WayPoint(0, -10);
		assertEquals(270, BpmnLayoutAnalyzer.calDegree(wp1, wp5));
		
		WayPoint wp6 = new WayPoint(10, -10);
		assertEquals(315, BpmnLayoutAnalyzer.calDegree(wp1, wp6));
	}

	@Test
	public void test_All_LeftRight() throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		List<Result> results = new BpmnLayoutAnalyzer().analyze(new File("src/test/resources/all-leftright.bpmn"));
		
		assertEquals(1, results.size());
		Result result = results.get(0);
		assertEquals("Camunda Modeler", result.getExporter());
		assertEquals("3.1.2", result.getExporterVersion());
		assertEquals(2, result.getSequenceFlowDirections().get(EdgeDirection.DIRECT_LEFT_RIGHT));
		assertEquals(2, result.getSequenceFlowDirections().sumAll());
		
		assertSame(EdgeDirection.DIRECT_LEFT_RIGHT, result.getDominantSequenceFlowDirection());
		assertEquals(1.0, result.getDominantSequenceFlowDirectionPurity(), 0.0001);
	}
	
	@Test
	public void test_All_TopBottom() throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		List<Result> results = new BpmnLayoutAnalyzer().analyze(new File("src/test/resources/all-topbottom.bpmn"));
		
		assertEquals(1, results.size());
		Result result = results.get(0);
		assertEquals("Camunda Modeler", result.getExporter());
		assertEquals("3.1.2", result.getExporterVersion());
		assertEquals(2, result.getSequenceFlowDirections().get(EdgeDirection.DIRECT_TOP_BOTTOM));
		assertEquals(2, result.getSequenceFlowDirections().sumAll());
		
		assertSame(EdgeDirection.DIRECT_TOP_BOTTOM, result.getDominantSequenceFlowDirection());
		assertEquals(1.0, result.getDominantSequenceFlowDirectionPurity(), 0.0001);
	}
	
	@Test
	public void test_All_LeftUpperRight() throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		List<Result> results = new BpmnLayoutAnalyzer().analyze(new File("src/test/resources/all-leftupperright.bpmn"));
		
		assertEquals(1, results.size());
		Result result = results.get(0);
		assertEquals("Camunda Modeler", result.getExporter());
		assertEquals("3.1.2", result.getExporterVersion());
		assertEquals(2, result.getSequenceFlowDirections().get(EdgeDirection.ZAGGED_LEFT_UPPERRIGHT));
		assertEquals(2, result.getSequenceFlowDirections().sumAll());
		
		assertSame(null, result.getDominantSequenceFlowDirection());
		assertEquals(-1.0, result.getDominantSequenceFlowDirectionPurity(), 0.0001);
	}
	
	@Test
	public void test_All_LeftUpperRight_Left() throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		List<Result> results = new BpmnLayoutAnalyzer().analyze(new File("src/test/resources/all-leftupperright-leftright.bpmn"));
		
		assertEquals(1, results.size());
		Result result = results.get(0);
		System.out.println(result.getSequenceFlowDirections());
		assertEquals("Camunda Modeler", result.getExporter());
		assertEquals("3.1.2", result.getExporterVersion());
		assertEquals(1, result.getSequenceFlowDirections().get(EdgeDirection.ZAGGED_LEFT_UPPERRIGHT));
		assertEquals(1, result.getSequenceFlowDirections().get(EdgeDirection.DIRECT_LEFT_RIGHT));
		assertEquals(2, result.getSequenceFlowDirections().sumAll());
		
		assertSame(EdgeDirection.DIRECT_LEFT_RIGHT, result.getDominantSequenceFlowDirection());
		assertEquals(1.0, result.getDominantSequenceFlowDirectionPurity(), 0.0001);
	}
	
	@Test
	public void test_OptimizeWayPoints_Optimizable() throws Exception {
		WayPoint p1 = new WayPoint(1.0, 1.0);
		WayPoint p2 = new WayPoint(2.0, 2.0);
		WayPoint p3 = new WayPoint(3.0, 3.0);
		
		List<WayPoint> optimizableList = new ArrayList<>(Arrays.asList(p1, p2, p3));
		assertTrue(BpmnLayoutAnalyzer.optimize(optimizableList));
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
		assertFalse(BpmnLayoutAnalyzer.optimize(optimizableList));
		assertEquals(3, optimizableList.size());
		assertSame(p1, optimizableList.get(0));
		assertSame(p2, optimizableList.get(1));
		assertSame(p3, optimizableList.get(2));
	}
}
