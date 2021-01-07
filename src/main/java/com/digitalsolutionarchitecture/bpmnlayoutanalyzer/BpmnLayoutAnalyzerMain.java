package com.digitalsolutionarchitecture.bpmnlayoutanalyzer;

import java.io.File;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.BpmnLayoutAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.output.CsvWriterOptions;

public class BpmnLayoutAnalyzerMain {

	public static void main(String[] args) throws Exception {
		BpmnLayoutAnalyzer analyzer = new BpmnLayoutAnalyzer();
		
		String reportFile = args[0];
		
		for(int i = 1; i < args.length; i++) {
			String filename = args[i];
			File f = new File(filename);
			analyzeFileOrDirectory(analyzer, f);
		}
		
		analyzer.writeReport(reportFile, new CsvWriterOptions());
	}

	private static void analyzeFileOrDirectory(BpmnLayoutAnalyzer anaylzer, File bpmnModelFile) {
		System.out.println("Analyzing " + bpmnModelFile.getAbsolutePath() + "...");
		if(bpmnModelFile.isFile()) {
			try {
				anaylzer.analyze(bpmnModelFile);
			} catch(Exception e) {
				System.err.println("Error analyzing " + bpmnModelFile.getAbsolutePath());
				e.printStackTrace();
			}
		} else if(bpmnModelFile.isDirectory()) {
			File[] possibleFiles = bpmnModelFile.listFiles();
			for(File g : possibleFiles) {
				analyzeFileOrDirectory(anaylzer, g);
			}
		}
	}
	
}
