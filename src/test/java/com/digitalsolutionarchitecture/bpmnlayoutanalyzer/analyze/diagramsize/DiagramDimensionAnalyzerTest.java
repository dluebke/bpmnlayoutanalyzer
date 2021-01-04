package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.diagramsize;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzerTest;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;

public class DiagramDimensionAnalyzerTest extends IBpmnAnalyzerTest<DiagramDimensionAnalyzer> {

	@Test
	public void test() throws Exception {
		BpmnProcess p = readProcess("src/test/resources/one-connected-events-multiplestartend.bpmn");
		
		getAnalyzer().analyze(p);
		
		assertResult(
			getAnalyzer().getResults(),
			179.0,
			81.0,
			472.0,
			168.0
		);
	}

	private void assertResult(List<DiagramDimensionResult> results, double x, double y, double width, double height) {
		assertEquals(1, results.size());
		
		DiagramDimensionResult result = results.get(0);
		assertResultInvariants(result);
		assertEquals(result.getBounds().getX(), result.getValues().get(0));
		assertEquals(result.getBounds().getY(), result.getValues().get(1));
		assertEquals(result.getBounds().getWidth(), result.getValues().get(2));
		assertEquals(result.getBounds().getHeight(), result.getValues().get(3));
		assertEquals(result.getBounds().getArea(), result.getValues().get(4));
		
		assertEquals(x, result.getBounds().getX(), 0.0000001);
		assertEquals(y, result.getBounds().getY(), 0.0000001);
		assertEquals(width, result.getBounds().getWidth(), 0.0000001);
		assertEquals(height, result.getBounds().getHeight(), 0.0000001);
		assertEquals(width * height, result.getBounds().getArea(), 0.0000001);
	}

	@Override
	protected DiagramDimensionAnalyzer createAnalyzer() {
		return new DiagramDimensionAnalyzer();
	}

}
