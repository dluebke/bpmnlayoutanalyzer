package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzerTest;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;

public class SequenceFlowDirectionSummaryAnalyzerTest extends IBpmnAnalyzerTest<SequenceFlowDirectionSummaryAnalyzer> {

	@Override
	protected SequenceFlowDirectionSummaryAnalyzer createAnalyzer() {
		return new SequenceFlowDirectionSummaryAnalyzer();
	}
	
	@Test
	public void classifies_left_right() throws Exception {
		BpmnProcess p = readProcess("src/test/resources/all-leftright.bpmn");
		
		getAnalyzer().analyze(p);
		
		assertResult(
			getAnalyzer().getResults(),
			EdgeDirection.DIRECT_LEFT_RIGHT,
			1.0,
			EdgeDirection.DIRECT_LEFT_RIGHT,
			1.0
		);
	}
	
	@Test
	public void classifies_top_bottom() throws Exception {
		BpmnProcess p = readProcess("src/test/resources/all-topbottom.bpmn");
		
		getAnalyzer().analyze(p);
		
		assertResult(
				getAnalyzer().getResults(),
				EdgeDirection.DIRECT_TOP_BOTTOM,
				1.0,
				EdgeDirection.DIRECT_TOP_BOTTOM,
				1.0
				);
	}
	
	@Test
	public void classifies_xor_loop() throws Exception {
		BpmnProcess p = readProcess("src/test/resources/simple-xor-loop.bpmn");
		
		getAnalyzer().analyze(p);
		
		assertResult(
				getAnalyzer().getResults(),
				EdgeDirection.DIRECT_LEFT_RIGHT,
				1.0,
				EdgeDirection.DIRECT_LEFT_RIGHT,
				0.8
				);
	}

	private void assertResult(
		List<SequenceFlowDirectionSummaryResult> results, 
		EdgeDirection dominantForwardSequenceFlowDirection,
		double dominantForwardSequenceFlowPurity,
		EdgeDirection dominantSequenceFlowDirection,
		double dominantSequenceFlowPurity
		) {
		
		assertEquals(1, results.size());
		SequenceFlowDirectionSummaryResult r = results.get(0);
		assertResultInvariants(r);
		
		assertEquals(dominantForwardSequenceFlowDirection, r.getDominantForwardSequenceFlowDirection());
		assertEquals(dominantForwardSequenceFlowPurity, r.getDominantForwardSequenceFlowDirectionPurity(), 0.00001);
		assertEquals(dominantSequenceFlowDirection, r.getDominantSequenceFlowDirection());
		assertEquals(dominantSequenceFlowPurity, r.getDominantSequenceFlowDirectionPurity(), 0.00001);
	}

}
