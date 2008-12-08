package edu.gmu.cs583.util;

import java.text.DecimalFormat;
import java.util.Vector;

import edu.gmu.cs583.data.Centroid;
import edu.gmu.cs583.data.DataPoint;

public class Geometry {
	private static boolean DEBUG = false;
	private static Vector<Integer> pointSize = new Vector<Integer>();
	
	public Geometry() {
	
	}

	public static void main(String[] args) {
		Geometry geo = new Geometry();
		Centroid cent = new Centroid(5.0, 20.0);
		DataPoint point = new DataPoint(10.0, 30.0);
		geo.setDEBUG(true);
		System.out.println(geo.getDistance(cent, point));
	}

	public static Double getDistance(Centroid centroid, DataPoint point) {
		Double xVals = Math.pow((centroid.getX() - point.getX()), 2);
		Double yVals = Math.pow((centroid.getY() - point.getY()), 2);
		if (DEBUG)
			System.out.println("Centroid: " + centroid.toString() + " point: "
					+ point.toString() + " Distance: "
					+ Math.sqrt(xVals + yVals));

		return truncate(Math.sqrt(xVals + yVals));
	}
	
	/**
	 * Returns the Euclidean distance of two data points.
	 * @param x Data Point X
	 * @param y Data Point Y
	 * @return Euclidean distance between two dimension points.
	 */
	public static double getEuclideanDistance(DataPoint x, DataPoint y) {
		double xVal = Math.pow((x.getX() - y.getX()), 2);
		double yVal = Math.pow((x.getY() - y.getY()), 2);

		return Math.sqrt((xVal + yVal));
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
//		pointSize.add(10);
		pointSize.add(50);
//		pointSize.add(100);
//		pointSize.add(500);
//		pointSize.add(1000);
//		pointSize.add(2000);
//		pointSize.add(5000);
//		pointSize.add(10000);
//		pointSize.add(15000);
//		pointSize.add(50000);
//		pointSize.add(70000);
//		pointSize.add(100000);
		return pointSize;
	}
	
	
}
