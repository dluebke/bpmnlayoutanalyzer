package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface IBpmnAnaylzer {

	public void performPreAnalysisOfModel(Document bpmnDocument);
	
	public void analyse(Element bpmnDiagram, Result result);
	
}
