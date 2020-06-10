package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze;

import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.exporter.ExporterInfo;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;

public abstract class Result {

	public Result(BpmnProcess p) {
		this(p.getFilename(), p.getDiagramIndex(), p.getExporterInfo());
	}
	
	public Result(String filename, int digramIndex, ExporterInfo exporterInfo) {
		super();
		this.filename = filename;
		this.digramIndex = digramIndex;
		this.exporterInfo = exporterInfo;
	}
	
	public final String filename;
	public final int digramIndex;
	public final ExporterInfo exporterInfo;
	
	public abstract List<Object> getValues();

}
