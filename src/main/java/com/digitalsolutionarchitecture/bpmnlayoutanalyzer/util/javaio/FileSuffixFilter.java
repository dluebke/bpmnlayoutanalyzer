package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.util.javaio;

import java.io.File;
import java.io.FilenameFilter;

public class FileSuffixFilter implements FilenameFilter {

	private String suffix;

	public FileSuffixFilter(String suffix) {
		this.suffix = suffix;
	}

	@Override
	public boolean accept(File dir, String name) {
		return name.endsWith(suffix);
	}

}
