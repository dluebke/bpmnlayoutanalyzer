package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.bpmnmodel;

public class Bounds {
	private double x;
	private double y;
	private double width;
	private double height;

	public Bounds() {
	}
	
	public Bounds(double x, double y, double width, double height) {
		super();
		set(x, y, width, height);
	}

	public void set(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public boolean setMinX(double x) {
		if (this.x >= x) {
			this.x = x;
			return true;
		} else {
			return false;
		}
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public boolean setMinY(double y) {
		if (this.y >= y) {
			this.y = y;
			return true;
		} else {
			return false;
		}
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public boolean setMaxWidth(double width) {
		if (this.width <= width) {
			this.width = width;
			return true;
		} else {
			return false;
		}
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public boolean setMaxHeight(double height) {
		if (this.height <= height) {
			this.height = height;
			return true;
		} else {
			return false;
		}
	}

	public void extendTo(Bounds shapeBounds) {
		setMinX(shapeBounds.getX());
		setMinY(shapeBounds.getY());
		setMaxWidth(shapeBounds.getWidth());
		setMaxHeight(shapeBounds.getHeight());
	}

	public double getArea() {
		return getWidth() * getHeight();
	}

}
