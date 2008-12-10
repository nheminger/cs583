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
//		try {
//			PointGenerator.generatePointsInShapeAndReturn("c:/UnevenData2D.dat", 2);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

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
				generateAndReturnPoints = PointGenerator.generateAndReturnPoints(2, range, 1, true, false);
				
			} while (pointHash.size() < 75);
			
			for (Iterator iterator = pointHash.entrySet().iterator(); iterator.hasNext();) {
				DataPoint dataPoint = (DataPoint) iterator.next();
				
			}
			
			
		}
		
		
	}

}
