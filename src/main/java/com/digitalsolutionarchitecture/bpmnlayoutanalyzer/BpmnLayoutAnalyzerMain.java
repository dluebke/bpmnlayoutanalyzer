package com.digitalsolutionarchitecture.bpmnlayoutanalyzer;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.BpmnLayoutAnalyzer;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.output.CsvWriterOptions;

public class BpmnLayoutAnalyzerMain {

	public static void main(String[] args) throws Exception {
		BpmnLayoutAnalyzer analyzer = new BpmnLayoutAnalyzer();
		
		String reportFile = args[0];
		
		try(PrintWriter err = new PrintWriter(new FileWriter("error.log", true))) {
			for(int i = 1; i < args.length; i++) {
				String filename = args[i];
				File f = new File(filename);
				analyzeFileOrDirectory(analyzer, f, err);
			}
		}
		
		CsvWriterOptions options = new CsvWriterOptions();
		options.setEnforceSlashesAsPathSeparator(true);
		analyzer.writeReport(reportFile, options);
	}

	private static void analyzeFileOrDirectory(BpmnLayoutAnalyzer anaylzer, File bpmnModelFile, PrintWriter err) {
//		System.out.println("Analyzing " + bpmnModelFile.getAbsolutePath() + "...");
		if(bpmnModelFile.isFile()) {
			try {
				anaylzer.analyze(bpmnModelFile);
			} catch(Exception e) {
				System.err.println("Error analyzing " + bpmnModelFile.getAbsolutePath());
				err.println("============================ Error analyzing " + bpmnModelFile.getAbsolutePath());
				e.printStackTrace();
				e.printStackTrace(err);
			}
		} else if(bpmnModelFile.isDirectory()) {
			File[] possibleFiles = bpmnModelFile.listFiles();
			for(File g : possibleFiles) {
				analyzeFileOrDirectory(anaylzer, g, err);
			}
		}
	}
	
}
