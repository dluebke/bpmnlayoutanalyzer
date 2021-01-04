package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.Result;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.BpmnProcess;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.util.CounterMap;

class SequenceFlowDirectionSummaryResult extends Result {
	
	private CounterMap<EdgeDirection> sequenceFlowTypes = new CounterMap<>();
	private CounterMap<EdgeDirection> forwardSequenceFlowTypes = new CounterMap<>();
	private CounterMap<EdgeDirection> optimizableSequenceFlowTypes = new CounterMap<>();
	private EdgeDirection dominantSequenceFlowDirection;
	private double dominantSequenceFlowDirectionPurity;
	private EdgeDirection dominantForwardSequenceFlowDirection;
	private double dominantForwardSequenceFlowDirectionPurity;
	
	public SequenceFlowDirectionSummaryResult(BpmnProcess process) {
		super(process);
	}

	public void calculateMetrics() {
		calculcateAllSequenceFlows();
		calculcateForwardSequenceFlows();
	}

	private void calculcateAllSequenceFlows() {
		int leftright = sequenceFlowTypes.sum(EdgeDirection.LEFT_RIGHT_FACING_ARCS);
		int rightleft = sequenceFlowTypes.sum(EdgeDirection.RIGHT_LEFT_FACING_ARCS);
		int topbottom = sequenceFlowTypes.sum(EdgeDirection.TOP_BOTTOM_FACING_ARCS);
		int bottomtop = sequenceFlowTypes.sum(EdgeDirection.BOTTOM_TOP_FACING_ARCS);
		
		List<EdgeDirection> remainingArcTypes = new ArrayList<>(Arrays.asList(EdgeDirection.values()));
		
		if(leftright > rightleft && leftright > topbottom && leftright > bottomtop) {
			dominantSequenceFlowDirection = EdgeDirection.DIRECT_LEFT_RIGHT;
			remainingArcTypes.removeAll(Arrays.asList(EdgeDirection.LEFT_RIGHT_FACING_ARCS));
			int otherArcs = sequenceFlowTypes.sum(remainingArcTypes.toArray(new EdgeDirection[remainingArcTypes.size()]));
			dominantSequenceFlowDirectionPurity = (double)leftright / (double)(leftright + otherArcs);
		} else if(rightleft > leftright && rightleft > topbottom && rightleft > bottomtop) {
			dominantSequenceFlowDirection = EdgeDirection.DIRECT_RIGHT_LEFT;
			remainingArcTypes.removeAll(Arrays.asList(EdgeDirection.RIGHT_LEFT_FACING_ARCS));
			int otherArcs = sequenceFlowTypes.sum(remainingArcTypes.toArray(new EdgeDirection[remainingArcTypes.size()]));
			dominantSequenceFlowDirectionPurity = (double)rightleft / (double)(rightleft + otherArcs);
		} else if(topbottom > leftright && topbottom > rightleft && topbottom > bottomtop) {
			dominantSequenceFlowDirection = EdgeDirection.DIRECT_TOP_BOTTOM;
			remainingArcTypes.removeAll(Arrays.asList(EdgeDirection.TOP_BOTTOM_FACING_ARCS));
			int otherArcs = sequenceFlowTypes.sum(remainingArcTypes.toArray(new EdgeDirection[remainingArcTypes.size()]));
			dominantSequenceFlowDirectionPurity = (double)topbottom / (double)(topbottom + otherArcs);
		} else if(bottomtop > leftright && bottomtop > rightleft && bottomtop > topbottom) {
			dominantSequenceFlowDirection = EdgeDirection.DIRECT_BOTTOM_TOP;
			remainingArcTypes.removeAll(Arrays.asList(EdgeDirection.BOTTOM_TOP_FACING_ARCS));
			int otherArcs = sequenceFlowTypes.sum(remainingArcTypes.toArray(new EdgeDirection[remainingArcTypes.size()]));
			dominantSequenceFlowDirectionPurity = (double)bottomtop / (double)(bottomtop + otherArcs);
		} else {
			dominantSequenceFlowDirection = null;
			dominantSequenceFlowDirectionPurity = -1.0;
		}
	}
	
	private void calculcateForwardSequenceFlows() {
		int leftright = forwardSequenceFlowTypes.sum(EdgeDirection.LEFT_RIGHT_FACING_ARCS);
		int rightleft = forwardSequenceFlowTypes.sum(EdgeDirection.RIGHT_LEFT_FACING_ARCS);
		int topbottom = forwardSequenceFlowTypes.sum(EdgeDirection.TOP_BOTTOM_FACING_ARCS);
		int bottomtop = forwardSequenceFlowTypes.sum(EdgeDirection.BOTTOM_TOP_FACING_ARCS);
		
		List<EdgeDirection> remainingArcTypes = new ArrayList<>(Arrays.asList(EdgeDirection.values()));
		
		if(leftright > rightleft && leftright > topbottom && leftright > bottomtop) {
			dominantForwardSequenceFlowDirection = EdgeDirection.DIRECT_LEFT_RIGHT;
			remainingArcTypes.removeAll(Arrays.asList(EdgeDirection.LEFT_RIGHT_FACING_ARCS));
			int otherArcs = forwardSequenceFlowTypes.sum(remainingArcTypes.toArray(new EdgeDirection[remainingArcTypes.size()]));
			dominantForwardSequenceFlowDirectionPurity = (double)leftright / (double)(leftright + otherArcs);
		} else if(rightleft > leftright && rightleft > topbottom && rightleft > bottomtop) {
			dominantForwardSequenceFlowDirection = EdgeDirection.DIRECT_RIGHT_LEFT;
			remainingArcTypes.removeAll(Arrays.asList(EdgeDirection.RIGHT_LEFT_FACING_ARCS));
			int otherArcs = forwardSequenceFlowTypes.sum(remainingArcTypes.toArray(new EdgeDirection[remainingArcTypes.size()]));
			dominantForwardSequenceFlowDirectionPurity = (double)rightleft / (double)(rightleft + otherArcs);
		} else if(topbottom > leftright && topbottom > rightleft && topbottom > bottomtop) {
			dominantForwardSequenceFlowDirection = EdgeDirection.DIRECT_TOP_BOTTOM;
			remainingArcTypes.removeAll(Arrays.asList(EdgeDirection.TOP_BOTTOM_FACING_ARCS));
			int otherArcs = forwardSequenceFlowTypes.sum(remainingArcTypes.toArray(new EdgeDirection[remainingArcTypes.size()]));
			dominantForwardSequenceFlowDirectionPurity = (double)topbottom / (double)(topbottom + otherArcs);
		} else if(bottomtop > leftright && bottomtop > rightleft && bottomtop > topbottom) {
			dominantForwardSequenceFlowDirection = EdgeDirection.DIRECT_BOTTOM_TOP;
			remainingArcTypes.removeAll(Arrays.asList(EdgeDirection.BOTTOM_TOP_FACING_ARCS));
			int otherArcs = forwardSequenceFlowTypes.sum(remainingArcTypes.toArray(new EdgeDirection[remainingArcTypes.size()]));
			dominantForwardSequenceFlowDirectionPurity = (double)bottomtop / (double)(bottomtop + otherArcs);
		} else {
			dominantForwardSequenceFlowDirection = null;
			dominantForwardSequenceFlowDirectionPurity = -1.0;
		}
	}

	public EdgeDirection getDominantSequenceFlowDirection() {
		return dominantSequenceFlowDirection;
	}
	
	public double getDominantSequenceFlowDirectionPurity() {
		return dominantSequenceFlowDirectionPurity;
	}
	
	public EdgeDirection getDominantForwardSequenceFlowDirection() {
		return dominantForwardSequenceFlowDirection;
	}
	
	public double getDominantForwardSequenceFlowDirectionPurity() {
		return dominantForwardSequenceFlowDirectionPurity;
	}
	
	public void addSequenceFlow(EdgeDirection at) {
		sequenceFlowTypes.inc(at);
	}


	public void addForwardSequenceFlow(EdgeDirection at) {
		forwardSequenceFlowTypes.inc(at);
	}
	
	public CounterMap<EdgeDirection> getSequenceFlowDirections() {
		return sequenceFlowTypes;
	}

	public void addOptimizableSequenceFlow(EdgeDirection at) {
		optimizableSequenceFlowTypes.inc(at);
	}
	
	public CounterMap<EdgeDirection> getOptimizableSequenceFlowDirections() {
		return optimizableSequenceFlowTypes;
	}
	
	@Override
	public List<Object> getValues() {
		List<Object> fields = new ArrayList<>();
		
		fields.addAll(Arrays.asList(
			this.getDominantSequenceFlowDirection() != null ? this.getDominantSequenceFlowDirection().toString() : "", 
			this.getDominantSequenceFlowDirectionPurity() >= 0.0 ? Double.toString(this.getDominantSequenceFlowDirectionPurity()) : ""
		));
		fields.addAll(Arrays.asList(
			this.getDominantForwardSequenceFlowDirection() != null ? this.getDominantForwardSequenceFlowDirection().toString() : "", 
			this.getDominantForwardSequenceFlowDirectionPurity() >= 0.0 ? Double.toString(this.getDominantForwardSequenceFlowDirectionPurity()) : ""
		));
		for(EdgeDirection at : EdgeDirection.values()) {
			fields.add(this.getSequenceFlowDirections().get(at));
		}
		fields.add(this.getSequenceFlowDirections().sumAll());
		for(EdgeDirection at : EdgeDirection.values()) {
			fields.add(this.getOptimizableSequenceFlowDirections().get(at));
		}
		fields.add(this.getOptimizableSequenceFlowDirections().sumAll());
			
		return fields;
	}

}
