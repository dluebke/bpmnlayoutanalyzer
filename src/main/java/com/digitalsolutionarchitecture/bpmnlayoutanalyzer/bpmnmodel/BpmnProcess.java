package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.exporter.ExporterInfo;

public class BpmnProcess {

	private List<FlowNode> flowNodes = new ArrayList<>();
	private Map<String, FlowNode> flowNodeById = new HashMap<>();
	private List<SequenceFlow> sequenceFlows = new ArrayList<>();
	private Map<String, SequenceFlow> sequenceFlowById = new HashMap<>();
	private List<Participant> participants = new ArrayList<>();
	private Map<String, Participant> participantById = new HashMap<>();
	private ExporterInfo exporterInfo;
	private String filename;
	private int diagramIndex;
	
	public FlowNode getFlowNodeById(String id) {
		return flowNodeById.get(id);
	}
	
	public void add(FlowNode n) {
		flowNodes.add(n);
		flowNodeById.put(n.getId(), n);
	}

	public void clearLayoutData() {
		for(FlowNode n : flowNodes) {
			n.clearLayoutData();
		}
		
		for(SequenceFlow sf : sequenceFlows) {
			sf.clearLayoutData();
		}
		
		for(Participant p : participants) {
			p.clearLayoutData();
		}
	}
	
	public List<FlowNode> getFlowNodes() {
		return Collections.unmodifiableList(flowNodes);
	}
	
	public SequenceFlow getSequenceFlowById(String id) {
		return sequenceFlowById.get(id);
	}
	
	public void add(SequenceFlow s) {
		sequenceFlows.add(s);
		sequenceFlowById.put(s.getId(), s);
	}
	
	public List<SequenceFlow> getSequenceFlows() {
		return Collections.unmodifiableList(sequenceFlows);
	}
	
	public ExporterInfo getExporterInfo() {
		return exporterInfo;
	}
	
	public void setExporterInfo(ExporterInfo exporterInfo) {
		this.exporterInfo = exporterInfo;
	}

	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getDiagramIndex() {
		return diagramIndex;
	}
	
	public void setDiagramIndex(int diagramIndex) {
		this.diagramIndex = diagramIndex;
	}

	public void add(Participant participant) {
		this.participants.add(participant);
		this.participantById.put(participant.getId(), participant);
	}
	
	public List<Participant> getParticipants() {
		return participants;
	}
	
	public Participant getParticipantById(String id) {
		return participantById.get(id);
	}
	
}
