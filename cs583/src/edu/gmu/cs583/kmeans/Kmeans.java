package edu.gmu.cs583.kmeans;

import edu.gmu.cs583.data.PointGenerator;

public class Kmeans {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PointGenerator gen = new PointGenerator();
		gen.printValues();
		KMeansClusterer clusterer = new KMeansClusterer();
	}

}
