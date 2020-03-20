package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class WayPoint {

	private double x;
	private double y;

	public WayPoint() {
	}
	
	public WayPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double d) {
		this.x = d;
	}

	public void setY(double y) {
		this.y = y;
	}

	public static WayPoint fromDiElement(Node item) {
		WayPoint wp = new WayPoint();
		
		NamedNodeMap attributes = item.getAttributes();
		
		wp.setX(Double.parseDouble(attributes.getNamedItem("x").getNodeValue()));
		wp.setY(Double.parseDouble(attributes.getNamedItem("y").getNodeValue()));
		
		return wp;
	}

	@Override
	public String toString() {
		return "WayPoint " + x + "/" + y;
	}
	
	@Override
	public int hashCode() {
		return (int)(x % y);
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null || other.getClass() != getClass()) {
			return false;
		}
		
		WayPoint wpOther = (WayPoint)other;
		return x == wpOther.x && y == wpOther.y;
	}
}
