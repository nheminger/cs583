package edu.gmu.cs583.util;

import java.text.DecimalFormat;
import java.util.Vector;

import edu.gmu.cs583.data.Centroid;
import edu.gmu.cs583.data.DataPoint;
import edu.gmu.cs583.data.Point;

public class Geometry {
	private static boolean DEBUG = false;
	private static Vector<Integer> pointSize = new Vector<Integer>();
	
	public Geometry() {
	
	}

	public static void main(String[] args) {
		Geometry geo = new Geometry();
		
		double[] coords1 = new double[2];
		coords1[0] = 5.0;
		coords1[1] = 20.0;
		double[] coords2 = new double[2];
		coords2[0] = 5.0;
		coords2[1] = 20.0;
		
		Centroid cent = new Centroid(coords1);
		DataPoint point = new DataPoint(coords2);
		
		geo.setDEBUG(true);
		
		System.out.println(geo.getDistance(cent, point));
	}

	public static Double getDistance(Point point1, Point point2) {
		return Geometry.getDistance(point1, point2, false);
	}
	
	/**
	 * Returns the Euclidean distance of two data points.
	 * @param Point Point1 
	 * @param Point Point2 
	 * @param boolean truncate 
	 * @throws RuntimeException when dimensions of points don't match
	 * @return Euclidean distance between two dimension points.
	 */
	public static Double getDistance(Point point1, Point point2, boolean truncate) {
		
		// make sure they are the same dimension
		if (point1.getDimensions() != point2.getDimensions()) {
			throw new RuntimeException("The dimensions of the points do not match when comparing distances! Point1: " + point1 + ", Point2: " + point2);
		}
		
		int dimension = point1.getDimensions();
		double[] coords1 = point1.getCoords();
		double[] coords2 = point2.getCoords();
		double sumOfSquares = 0;
		
		for (int i = 0; i < dimension; i++) {
			sumOfSquares = sumOfSquares + Math.pow((coords1[i] - coords2[i]), 2);
		}
		double distance = Math.sqrt(sumOfSquares);
		
		if (DEBUG) {
			System.out.println("point1: " + point1.toString() + " point2: "
					+ point2.toString() + " Distance: "
					+ distance);
		}
		
		if (truncate)
			return truncate(distance);
		else
			return distance;
	}

	public boolean isDEBUG() {
		return DEBUG;
	}

	public static void setDEBUG(boolean debug) {
		DEBUG = debug;
	}
	
    public static double truncate (double x){
        DecimalFormat df = new  DecimalFormat ("0.##");
        String d = df.format (x);
        d = d.replaceAll (",", ".");
        Double dbl = new Double (d);
        return dbl.doubleValue ();
    }
    
	public static Vector<Integer> makepointSize(){
		pointSize.add(15);
		pointSize.add(50);
		pointSize.add(100);
		pointSize.add(500);
		pointSize.add(1000);
		pointSize.add(2000);
		pointSize.add(5000);
		pointSize.add(10000);
//		pointSize.add(15000);
//		pointSize.add(50000);
//		pointSize.add(70000);
//		pointSize.add(100000);
		return pointSize;
	}
	
	public static Vector<Integer> makeRTreePointSize(){
//		pointSize.add(15);
//		pointSize.add(50);
//		pointSize.add(100);
		pointSize.add(500);
//		pointSize.add(1000);
//		pointSize.add(2000);
//		pointSize.add(5000);
//		pointSize.add(7500);
//		pointSize.add(9000);
//		pointSize.add(10000);
//		pointSize.add(12000);
//		pointSize.add(15000);
		return pointSize;
	}
	
}
