package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.pools;

import java.util.ArrayList;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.Participant;

public class PoolOrientationEvaluator implements IBpmnAnalyzer {

	private List<PoolOrientationResult> results = new ArrayList<>();
	
	public PoolOrientationEvaluator() {
	}
	
	@Override
	public void analyze(BpmnProcess pr) {
		
		PoolOrientationResult result = new PoolOrientationResult(pr);
		
		for(Participant p : pr.getParticipants()) {
			if(p.hasLayoutData()) {
				if(p.getIsHorizontal() == null) {
					result.incPoolOrientationUnknown();
				} else if(p.getIsHorizontal()) {
					result.incPoolOrientationHorizontal();
				} else {
					result.incPoolOrientationVertical();
				}
				
				if(p.getProcess() != null) {
					result.incLaneCount(p.getProcess().getLanes().size());
				}
			}
		}
		
		results.add(result);
	}

	@Override
	public String getShortName() {
		return "poolorientation";
	}
	
	@Override
	public List<PoolOrientationResult> getResults() {
		return results;
	}
	
	@Override
	public String[] getHeaders() {
		return PoolOrientationResult.HEADERS;
	}
}
