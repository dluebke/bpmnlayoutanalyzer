package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.pools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.IBpmnAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.Participant;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.output.CsvWriter;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.output.CsvWriterOptions;

public class PoolOrientationEvaluator implements IBpmnAnalyzer {

	private List<PoolOrientationResult> results = new ArrayList<>();
	
	public PoolOrientationEvaluator() {
	}
	
	@Override
	public void analyze(BpmnProcess pr) {
		PoolOrientationResult result = new PoolOrientationResult(pr);
		
		for(Participant p : pr.getParticipants()) {
			if(p.getBounds() != null) {
				if(p.getIsHorizontal() == null) {
					result.incPoolOrientationUnknown();
				} else if(p.getIsHorizontal()) {
					result.incPoolOrientationHorizontal();
				} else {
					result.incPoolOrientationVertical();
				}
			}
		}
		
		results.add(result);
	}

	@Override
	public void writeReport(String baseName, CsvWriterOptions options) throws IOException {
		try(CsvWriter out = new CsvWriter(baseName + ".poolorientation.csv", options)) {
			out.writeHeader("Horizontal", "Vertical", "Unknown");
			
			for(PoolOrientationResult result : results) {
				out.writeRecord(result, result.getPoolOrientationHorizontal(), result.getPoolOrientationVertical(), result.getPoolOrientationUnknown());
			}
		}
		
	}
	
}
