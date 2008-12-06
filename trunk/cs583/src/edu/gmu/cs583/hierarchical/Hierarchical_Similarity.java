package edu.gmu.cs583.hierarchical;

import java.util.*;
import edu.gmu.cs583.data.*;
import edu.gmu.cs583.util.*;

/**
 * Used to calculate a simularity matrix for the Hierarchical clustering method.
 * @author Chris Andrade
 */
public class Hierarchical_Similarity {
	
	private double[][] similarity;
	private Vector<DataPoint> points;
	
	/**
	 * Constructs a Hierarchical_Similarity Object
	 * @param points List of data points to construct a similarity matrix from.
	 */
	public Hierarchical_Similarity(Vector<DataPoint> points) {
		this.points = points;
		similarity = new double[points.size()][points.size()];
		
	}
	
	/**
	 * Calculates the similarity of data points using Euclidean distance.
	 * @param type Type Hierarchical LINK type.
	 */
	public void calculateSimilarity() {
		Geometry mathUtil = new Geometry();
		for(int i = 0; i <= points.size() - 1; i++) {
			for(int j=0; j <= points.size() - 1; j++) {
				if(i != j) { // don't calculate distances for itself
					DataPoint x = points.get(i);
					DataPoint y = points.get(j);
					similarity[i][j] = mathUtil.getEuclideanDistance(x, y);
				}
			}
		}
	}
	
	
}
