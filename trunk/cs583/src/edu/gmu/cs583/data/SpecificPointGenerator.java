package edu.gmu.cs583.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import edu.gmu.cs583.util.Geometry;

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
		for (int i = 300; i < 525; i=i+7) {
				for (int j = 315; j < 400; j = j + 7) {
					System.out.println(i + "\t" + j + "\t1");
				}
		}
		
		List clusterCenters = new ArrayList();
		
		for (int i = 0; i < 4; i++) {
			int[] range = new int[2];
			range[0] = 500;
			range[1] = 500;
			Vector<DataPoint> generateAndReturnPoints = null;
			try {
					boolean outsideRange = false;
					while(!outsideRange) {
						generateAndReturnPoints = PointGenerator.generateAndReturnPoints(2, range, 1, true, false);
						for (int j = 0; j < clusterCenters.size(); j++) {
							if (Geometry.getDistance(generateAndReturnPoints.get(0), (DataPoint)clusterCenters.get(j)) < 150) {
								outsideRange = false;
								break;
							} else if (j == (clusterCenters.size() - 1)) {
								clusterCenters.add(generateAndReturnPoints.get(0));
								outsideRange = true;
								break;
							}
						}
						
						if (clusterCenters.size() == 0) {
							clusterCenters.add(generateAndReturnPoints.get(0));
							outsideRange = true;
						}
					}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Circle circle = new Circle(generateAndReturnPoints.get(0), 50);
			
			Map<String, DataPoint> pointHash = new HashMap<String, DataPoint>();
			do {
				Vector<DataPoint> randomPoints = null;
				try {
					randomPoints = PointGenerator.generateAndReturnPoints(2, range, 1, true, false);
					if (circle.contains(randomPoints.get(0))) {
						pointHash.put(randomPoints.get(0).toString(), randomPoints.get(0));
					}	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} while (pointHash.size() < 200);
			
			for(String key : pointHash.keySet()){
				System.out.println(pointHash.get(key).toString());
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
