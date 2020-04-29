package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.analyze.edges.EdgeDirection;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel.Bounds;
import com.digitalsolutionarchitecture.bpmnlayoutanalyzer.util.CounterMap;

public class Result {
	private String exporter;
	private String exporterVersion;
	
	private CounterMap<EdgeDirection> sequenceFlowTypes = new CounterMap<>();
	private CounterMap<EdgeDirection> optimizableSequenceFlowTypes = new CounterMap<>();
	private EdgeDirection dominantSequenceFlowDirection;
	private double dominantSequenceFlowDirectionPurity;
	private String name;
	private int diagramNo;
	private Bounds diagramBounds;
	private int poolOrientationUnknown;
	private int poolOrientationVertical;
	private int poolOrientationHorizontal;
	
	public Result(String name, int diagramNo) {
		this.name = name;
		this.diagramNo = diagramNo;
	}

	public void calculateMetrics() {
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

	public String getName() {
		return name;
	}
	
	public int getDiagramNo() {
		return diagramNo;
	}
	
	public EdgeDirection getDominantSequenceFlowDirection() {
		return dominantSequenceFlowDirection;
	}
	
	public double getDominantSequenceFlowDirectionPurity() {
		return dominantSequenceFlowDirectionPurity;
	}
	
	public void addSequenceFlow(EdgeDirection at) {
		sequenceFlowTypes.inc(at);
	}

	public String getExporter() {
		return exporter;
	}
	
	public String getExporterVersion() {
		return exporterVersion;
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

	public void setDiagramBounds(Bounds diagramBounds) {
		this.diagramBounds = diagramBounds;
	}
	
	public Bounds getDiagramBounds() {
		return diagramBounds;
	}
	
	public void setExporter(String exporter) {
		this.exporter = exporter;
	}
	
	public void setExporterVersion(String exporterVersion) {
		this.exporterVersion = exporterVersion;
	}

	public void incPoolOrientationHorizontal() {
		poolOrientationHorizontal++;
	}

	public void incPoolOrientationVertical() {
		poolOrientationVertical++;
	}

	public void incPoolOrientationUnknown() {
		poolOrientationUnknown++;
	}
	
	public int getPoolOrientationHorizontal() {
		return poolOrientationHorizontal;
	}
	
	public int getPoolOrientationVertical() {
		return poolOrientationVertical;
	}
	
	public int getPoolOrientationUnknown() {
		return poolOrientationUnknown;
	}
}
