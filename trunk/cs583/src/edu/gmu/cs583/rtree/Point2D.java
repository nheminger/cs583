package edu.gmu.cs583.rtree;

public class Point2D {

	private final static int DIMENSIONS = 2;

	public double[] coordinates;

	public Point2D(double x, double y) {
		coordinates = new double[DIMENSIONS];
		coordinates[0] = x; 
		coordinates[1] = y;
	}	
}
