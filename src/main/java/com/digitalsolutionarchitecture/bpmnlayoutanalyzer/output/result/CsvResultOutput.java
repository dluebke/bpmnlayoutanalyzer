package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.output.result;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.Result;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges.EdgeDirection;

public class CsvResultOutput {

	private String fieldSeparator = ";";
	private String recordSeparator = "\n";
	private boolean printHeaders = true;
	
	private static final String[] HEADERS;

	static {
		List<String> headers = new ArrayList<>();
		headers.addAll(Arrays.asList(
			"Filename",
			"DiagramIndex",
			"DominantSequenceFlowDirection", 
			"DominantSequenceFlowDirectionPurity",
			"Exporter",
			"ExporterVersion"
		));
		for(EdgeDirection at : EdgeDirection.values()) {
			headers.add("SEQUENCEFLOW_" + at.toString());
		}
		headers.add("SEQUENCEFLOW_TOTAL");
		for(EdgeDirection at : EdgeDirection.values()) {
			headers.add("OPTIMIZABLE_SEQUENCEFLOW_" + at.toString());
		}
		headers.add("OPTIMIZABLE_SEQUENCEFLOW_TOTAL");
		headers.add("DIAGRAM_X");
		headers.add("DIAGRAM_Y");
		headers.add("DIAGRAM_WIDTH");
		headers.add("DIAGRAM_HEIGHT");
		headers.add("DIAGRAM_AREA");
		headers.add("POOLORIENTATION_HORIZONTAL");
		headers.add("POOLORIENTATION_VERTICAL");
		headers.add("POOLORIENTATION_UNKONWN");
		
		HEADERS = headers.toArray(new String[headers.size()]);
	}
	
	public void write(Writer out, List<Result> results) throws IOException {
		if(printHeaders) {
			writeLine(out, HEADERS);
		}
		
		List<String> fields = new ArrayList<>();
		for(Result r : results) {
			fields.addAll(Arrays.asList(
				r.getName(),
				Integer.toString(r.getDiagramNo()), 
				r.getDominantSequenceFlowDirection() != null ? r.getDominantSequenceFlowDirection().toString() : "", 
				r.getDominantSequenceFlowDirectionPurity() >= 0.0 ? Double.toString(r.getDominantSequenceFlowDirectionPurity()) : "",
				r.getExporter(),
				r.getExporterVersion() != null ? r.getExporterVersion() : ""
			));
			for(EdgeDirection at : EdgeDirection.values()) {
				fields.add(Integer.toString(r.getSequenceFlowDirections().get(at)));
			}
			fields.add(Integer.toString(r.getSequenceFlowDirections().sumAll()));
			for(EdgeDirection at : EdgeDirection.values()) {
				fields.add(Integer.toString(r.getOptimizableSequenceFlowDirections().get(at)));
			}
			fields.add(Integer.toString(r.getOptimizableSequenceFlowDirections().sumAll()));
			
			fields.add(Double.toString(r.getDiagramBounds().getX()));
			fields.add(Double.toString(r.getDiagramBounds().getY()));
			fields.add(Double.toString(r.getDiagramBounds().getWidth()));
			fields.add(Double.toString(r.getDiagramBounds().getHeight()));
			fields.add(Double.toString(r.getDiagramBounds().getArea()));
			fields.add(Integer.toString(r.getPoolOrientationHorizontal()));
			fields.add(Integer.toString(r.getPoolOrientationVertical()));
			fields.add(Integer.toString(r.getPoolOrientationUnknown()));
			
			writeLine(out, fields.toArray(new String[fields.size()]));
			fields.clear();
		}
	}

	private void writeLine(Writer out, String... fields) throws IOException {
		out.write(String.join(fieldSeparator, fields));
		out.write(recordSeparator);
	}
	
	public boolean isPrintHeaders() {
		return printHeaders;
	}
	
	public void setPrintHeaders(boolean printHeaders) {
		this.printHeaders = printHeaders;
	}
	
	public String getFieldSeparator() {
		return fieldSeparator;
	}
	
	public void setFieldSeparator(String fieldSeparator) {
		this.fieldSeparator = fieldSeparator;
	}
	
	public String getRecordSeparator() {
		return recordSeparator;
	}
	
	public void setRecordSeparator(String recordSeparator) {
		this.recordSeparator = recordSeparator;
	}
	
}
