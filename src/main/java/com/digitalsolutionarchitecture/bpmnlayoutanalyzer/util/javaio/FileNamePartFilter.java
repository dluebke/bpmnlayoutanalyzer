package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.util.javaio;

import java.io.File;
import java.io.FilenameFilter;

public class FileNamePartFilter implements FilenameFilter {

	private String filePart;

	public FileNamePartFilter(String filePart) {
		this.filePart = filePart;
	}

	@Override
	public boolean accept(File dir, String name) {
		return name.contains(filePart);
	}


}
