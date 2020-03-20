package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel;

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
