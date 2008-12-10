package edu.gmu.cs583.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class SpecificPointGenerator {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*	create K clusters of N points within a Distance D where D = (x - h)^2 + (y - k)^2 = r^2
		 *  goal: create cluster so there is no intersection.  This means the ith cluster center must be a distance greater than its own radius plus radius of all other cluster centers 
		 *  input: number of clusters
		 *   Vector<Cluster>
		 *  
		 * 	class Cluster{
		 * 	Integer[] center; // array of Integers for ND data 
		 * 	Double Radius; // (x - h)^2 + (y - k)^2 = r^2
		 * 	Integer numberOfPts; //
		 * }
		 * 
		 * output: Vector<DataPoint>
		 */
		for (int i = 0; i < 300; i=i+2) {
			for (int j = 0; j < 300; j=j+2) {
				System.out.println(i + "\t" + j);
			}
		}
		for (int i = 0; i < 4; i++) {
			int[] range = new int[2];
			range[0] = 300;
			range[1] = 300;
			Vector<DataPoint> generateAndReturnPoints = null;
			try {
				generateAndReturnPoints = PointGenerator.generateAndReturnPoints(2, range, 1, true, false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Map<String, DataPoint> pointHash = new HashMap<String, DataPoint>();
			do {
				//generateAndReturnPoints = PointGenerator.generateAndReturnPoints(2, range, 1, true, false);
				
			} while (pointHash.size() < 75);
			
			for (Iterator iterator = pointHash.entrySet().iterator(); iterator.hasNext();) {
				DataPoint dataPoint = (DataPoint) iterator.next();
			}
		}
	}
	
	//takes number of clusters and number of points : 50% of points go into "the big clusters" the rest are divided up between remaining clusters
	public Vector<DataPoint> createClusters(Integer numberOfClusters,Integer numberOfPoints,Integer D){
		if(numberOfPoints < 0)
			numberOfPoints = 1;
		if(numberOfClusters < 0)
			numberOfClusters = 1;
		
		Vector<DataPoint> data = new Vector<DataPoint>();
		Cluster cluster = new Cluster();
		// get random point for first cluster
		numberOfPoints--;
		
		while(numberOfPoints > 0){
			
			
		}
		
		
		return data;
	}
	
	 class Cluster{
		 Integer[] center; 				// array of Integers for ND data 
		 Integer[] radiusCoordinates; 	// array of Integers for ND data 
		 Double Radius; 				// (x - h)^2 + (y - k)^2 = r^2
		 Integer numberOfPts;			 // number of points
	}

}
