package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.exporter;

public class ExporterInfo {

	private String exporter;
	private String exporterVersion;
	
	public ExporterInfo(String exporter) {
		this(exporter, null);
	}
	
	public ExporterInfo(String exporter, String exporterVersion) {
		super();
		this.exporter = exporter;
		this.exporterVersion = exporterVersion;
	}

	public String getExporter() {
		return exporter;
	}
	
	public String getExporterVersion() {
		return exporterVersion;
	}
}
