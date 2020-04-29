package com.digitalsolutionarchitecture.bpmnlayoutanalyzer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.BpmnLayoutAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.Result;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.output.result.CsvResultOutput;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.util.javaio.FileNamePartFilter;

public class BpmnLayoutAnalyzerMain {

	public static void main(String[] args) {
		List<Result> results = new ArrayList<>();
		BpmnLayoutAnalyzer anaylzer = new BpmnLayoutAnalyzer();
		CsvResultOutput output = new CsvResultOutput();
		
		String reportFile = args[0];
		
		for(int i = 1; i < args.length; i++) {
			String filename = args[i];
			File f = new File(filename);
			analyzeFileOrDirectory(results, anaylzer, f);
		}
		
		try(Writer out = new FileWriter(reportFile)) {
			System.out.println("Writing report file " + reportFile);
			output.write(out, results);
		} catch (IOException e) {
			System.err.println("Error writing metrics report!");
			e.printStackTrace();
		}
	}

	private static void analyzeFileOrDirectory(List<Result> results, BpmnLayoutAnalyzer anaylzer, File f) {
		System.out.println("Analyzing " + f.getAbsolutePath() + "...");
		if(f.isFile()) {
			try {
				List<Result> resultsForFile = anaylzer.analyze(f);
				results.addAll(resultsForFile);
			} catch(Exception e) {
				System.err.println("Error analyzing " + f.getAbsolutePath());
				e.printStackTrace();
			}
		} else if(f.isDirectory()) {
			File[] possibleFiles = f.listFiles(new FileNamePartFilter(".bpmn"));
			for(File g : possibleFiles) {
				analyzeFileOrDirectory(results, anaylzer, g);
			}
		}
	}
	
}
