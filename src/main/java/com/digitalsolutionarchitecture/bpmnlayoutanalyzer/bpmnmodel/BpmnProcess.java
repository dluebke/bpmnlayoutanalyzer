package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;

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
	private Document bpmnDocument;
	private List<SubProcess> subProcesses = new ArrayList<>();
	private List<Lane> lanes = new ArrayList<>();
	
	public BpmnProcess(String filename, Document bpmnDocument) {
		this.filename = filename;
		this.bpmnDocument = bpmnDocument;
	}

	public FlowNode getFlowNodeById(String id) {
		FlowNode flowNode = flowNodeById.get(id);
		if(flowNode != null) {
			return flowNode;
		}
		
		for(SubProcess sp : subProcesses) {
			if(sp.getProcess() != null && sp.getProcess().getFlowNodeById(id) != null) {
				return sp.getProcess().getFlowNodeById(id);
			}
		}
		
		for(Participant p : participants) {
			if(p.getProcess() != null && p.getProcess().getFlowNodeById(id) != null) {
				return p.getProcess().getFlowNodeById(id);
			}
		}
		
		return null;
	}
	
	public void add(FlowNode n) {
		flowNodes.add(n);
		flowNodeById.put(n.getId(), n);
		
		if(n instanceof SubProcess) {
			subProcesses.add((SubProcess)n);
		}
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
		List<FlowNode> result = new ArrayList<>();
		result.addAll(flowNodes);
		for(SubProcess sp : subProcesses) {
			if(sp.getProcess() != null) {
				result.addAll(sp.getProcess().getFlowNodes());
			}
		}
		for(Participant p : participants) {
			if(p.getProcess() != null) {
				result.addAll(p.getProcess().getFlowNodes());
			}
		}
		
		return result;
	}
	
	public SequenceFlow getSequenceFlowById(String id) {
		SequenceFlow sequenceFlow = sequenceFlowById.get(id);
		if(sequenceFlow != null)
			return sequenceFlow;
		
		for(SubProcess sp : subProcesses) {
			if(sp.getProcess() != null && sp.getProcess().getSequenceFlowById(id) != null) {
				return sp.getProcess().getSequenceFlowById(id);
			}
		}
		
		for(Participant p : participants) {
			if(p.getProcess() != null && p.getProcess().getSequenceFlowById(id) != null) {
				return p.getProcess().getSequenceFlowById(id);
			}
		}
		
		return null;
	}
	
	public void add(SequenceFlow s) {
		sequenceFlows.add(s);
		sequenceFlowById.put(s.getId(), s);
	}
	
	public List<SequenceFlow> getSequenceFlows() {
		ArrayList<SequenceFlow> result = new ArrayList<>();
		result.addAll(sequenceFlows);
		for(SubProcess sp : subProcesses) {
			if(sp.getProcess() != null) {
				result.addAll(sp.getProcess().getSequenceFlows());
			}
		}
		for(Participant p : participants) {
			if(p.getProcess() != null) {
				result.addAll(p.getProcess().getSequenceFlows());
			}
		}
		return result;
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

	public List<FlowNode> getStartFlowNodes() {
		List<FlowNode> result = new ArrayList<>();
		
		for(FlowNode fn : getFlowNodes()) {
			if(
				fn.getIncomingSequenceFlows().size() == 0 &&
				!"boundaryEvent".equals(fn.getType())
			) {
				result.add(fn);
			}
		}
		
		return result;
	}
	
	public Document getBpmnDocument() {
		return bpmnDocument;
	}

	public Participant getParticipantByProcessId(String processId) {
		for(Participant p : getParticipants()) {
			if(processId.equals(p.getProcessId())) {
				return p;
			}
		}
		return null;
	}
	
	public List<Lane> getLanes() {
		return lanes;
	}
	
	public void addLane(Lane l) {
		lanes.add(l);
	}
}
