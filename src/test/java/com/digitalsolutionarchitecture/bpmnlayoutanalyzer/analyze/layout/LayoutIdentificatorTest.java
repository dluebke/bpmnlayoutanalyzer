package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.layout;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzerTest;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.FlowNode;

public class LayoutIdentificatorTest extends IBpmnAnalyzerTest<LayoutIdentificator> {

	@Override
	protected LayoutIdentificator createAnalyzer() {
		return new LayoutIdentificator();
	}
	
	@Test
	@Ignore
	public void test() throws Exception {
		BpmnProcess p = readProcess("E:\\sync\\dluebke-research\\projects\\XXXX BPMNGithub\\bpmns-stefanko\\bpmns-stefanko\\align-elements.bpmn");
		
		assertAllBoundaryEventsAreAttached(p);
		
		getAnalyzer().analyze(p);
		
		assertLayout(Layout.DIAGONAL_SOUTH_EAST);
		
	}

	private void assertAllBoundaryEventsAreAttached(BpmnProcess p) {
		for(FlowNode fn : p.getFlowNodes()) {
			if(fn.getType().equals("boundaryEvent")) {
				assertNotNull(fn.toString(), fn.getAttachedTo());
			}
		}
	}

	private void assertLayout(Layout l) {
		List<LayoutIdenficatorResult> results = getAnalyzer().getResults();
		assertEquals(1, results.size());
		
		LayoutIdenficatorResult result = results.get(0);
		assertResultInvariants(result);
		assertEquals(result.getValues().get(0), result.getLayout());
		
		assertEquals(l, result.getLayout());
	}

}
