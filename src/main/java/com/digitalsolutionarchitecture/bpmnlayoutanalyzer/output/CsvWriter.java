package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.output;

import java.io.FileWriter;
import java.io.IOException;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.Result;

public class CsvWriter implements AutoCloseable {

	private CsvWriterOptions options;
	private FileWriter writer;

	public CsvWriter(String filename) throws IOException {
		this(filename, new CsvWriterOptions());
	}
	
	public CsvWriter(String filename, CsvWriterOptions options) throws IOException {
		this.options = options;
		this.writer = new FileWriter(filename);
	}

	
	public void writeHeader(String... headers) throws IOException {
		String[] values = new String[headers.length + 4];
		
		values[0] = "Filename";
		values[1] = "DiagramIndex";
		values[2] = "Exporter";
		values[3] = "ExporterVersion";
		for(int i = 0; i < headers.length; i++) {
			values[i + 4] = headers[i].toString();
		}
		
		writeRecord(values);
	}
	
	public void writeRecord(Result r, Object... fields) throws IOException {
		String[] values = new String[fields.length + 4];
		
		values[0] = sanitize(r.filename);
		values[1] = Integer.toString(r.digramIndex);
		values[2] = sanitize(r.exporterInfo.getExporter());
		values[3] = sanitize(r.exporterInfo.getExporterVersion());
		for(int i = 0; i < fields.length; i++) {
			values[i + 4] = sanitize(fields[i].toString());
		}
		
		writeRecord(values);
	}

	private String sanitize(String string) {
		if(string == null)
			return null;

		return string
			.replace(options.getFieldSeparator(), options.getFieldSeparatorReplacement())
			.replace(options.getRecordSeparator(), options.getRecordSeparatorReplacement())
		;
	}

	private void writeRecord(String[] values) throws IOException {
		writer.write(String.join(options.getFieldSeparator(), values));
		writer.write(options.getRecordSeparator());
	}
	
	@Override
	public void close() throws IOException {
		writer.close();
	}
	
}
