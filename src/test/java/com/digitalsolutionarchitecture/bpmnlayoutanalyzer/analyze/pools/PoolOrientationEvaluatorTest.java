package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.pools;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzerTest;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;

public class PoolOrientationEvaluatorTest extends IBpmnAnalyzerTest<PoolOrientationEvaluator> {

	@Override
	protected PoolOrientationEvaluator createAnalyzer() {
		return new PoolOrientationEvaluator();
	}
	
	@Test
	public void identifies_no_pools() throws Exception {
		BpmnProcess p = readProcess("src/test/resources/all-leftright.bpmn");
		
		getAnalyzer().analyze(p);
		
		assertPoolOrientationResult(p, 0, 0, 0, 0);
	}
	
	@Test
	public void identifies_pool_withoutlane() throws Exception {
		BpmnProcess p = readProcess("src/test/resources/1pool-0laneeach.bpmn");
		
		getAnalyzer().analyze(p);
		
		assertPoolOrientationResult(p, 1, 0, 0, 0);
	}
	@Test
	public void identifies_pool_with2lanes() throws Exception {
		BpmnProcess p = readProcess("src/test/resources/1pool-2laneseach.bpmn");
		
		getAnalyzer().analyze(p);
		
		assertPoolOrientationResult(p, 1, 0, 0, 2);
	}

	private void assertPoolOrientationResult(BpmnProcess p, int poolHorizontalCount, int poolVerticalCount, int poolUnknownCount, int laneCount) {
		List<PoolOrientationResult> results = getAnalyzer().getResults();
		
		assertEquals(1, results.size());
		
		PoolOrientationResult r = results.get(0);
		assertResultInvariants(r);
		
		assertEquals(r.getPoolOrientationHorizontal(), r.getValues().get(0));
		assertEquals(r.getPoolOrientationVertical(), r.getValues().get(1));
		assertEquals(r.getPoolOrientationUnknown(), r.getValues().get(2));
		assertEquals(r.getLaneCount(), r.getValues().get(3));
		
		assertEquals(poolHorizontalCount, r.getPoolOrientationHorizontal());
		assertEquals(poolVerticalCount, r.getPoolOrientationVertical());
		assertEquals(poolUnknownCount, r.getPoolOrientationUnknown());
		assertEquals(laneCount, r.getLaneCount());
	}

}
