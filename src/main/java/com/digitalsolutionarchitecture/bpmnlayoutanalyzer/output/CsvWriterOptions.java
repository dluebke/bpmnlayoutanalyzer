package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.output;

public class CsvWriterOptions {

	private String fieldSeparator = ",";
	private String fieldSeparatorReplacement = "_";
	private String recordSeparator = "\n";
	private String recordSeparatorReplacement = "_";

	public String getFieldSeparator() {
		return fieldSeparator;
	}

	public void setFieldSeparator(String fieldSeparator) {
		this.fieldSeparator = fieldSeparator;
	}

	public String getFieldSeparatorReplacement() {
		return fieldSeparatorReplacement;
	}

	public void setFieldSeparatorReplacement(String fieldSeparatorReplacement) {
		this.fieldSeparatorReplacement = fieldSeparatorReplacement;
	}

	public String getRecordSeparatorReplacement() {
		return recordSeparatorReplacement;
	}

	public void setRecordSeparatorReplacement(String recordSeparatorReplacement) {
		this.recordSeparatorReplacement = recordSeparatorReplacement;
	}

	public String getRecordSeparator() {
		return recordSeparator;
	}

	public void setRecordSeparator(String recordSeparator) {
		this.recordSeparator = recordSeparator;
	}

}
