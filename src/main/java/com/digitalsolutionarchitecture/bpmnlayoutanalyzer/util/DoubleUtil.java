package com.digitalsolutionarchitecture.bpmnlayoutanalyzer.util;

public class DoubleUtil {
	
	public static final double DEFAULT_PRECISION = 0.000001; 
	
	public static boolean equals(double d1, double d2, double maxDelta) {
		double diff = Math.abs(d1 - d2);
		
		return diff <= maxDelta;
	}

	public static boolean equals(double d1, double d2) {
		return equals(d1, d2, DEFAULT_PRECISION);
	}
}
