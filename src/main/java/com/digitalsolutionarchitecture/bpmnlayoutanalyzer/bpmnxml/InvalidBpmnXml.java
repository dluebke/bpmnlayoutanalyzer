package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnxml;

@SuppressWarnings("serial")
public class InvalidBpmnXml extends RuntimeException {

	public InvalidBpmnXml(String msg, Throwable e) {
		super(msg, e);
	}

	public InvalidBpmnXml(String msg) {
		super(msg);
	}

}
