package edu.gmu.cs583.hierarchical;

import java.util.*;
import edu.gmu.cs583.data.*;
import edu.gmu.cs583.util.*;

/**
 * Used to calculate a simularity matrix for the Hierarchical clustering method.
 * @author Chris Andrade
 */
public class HierarchicalSimilarityMatrix {
	
	private Vector<Similarity> similarityList;
	private Vector<DataPoint> points;
	
	/**
	 * Constructs a Hierarchical_Similarity Object
	 * @param points List of data points to construct a similarity matrix from.
	 */
	public HierarchicalSimilarityMatrix(Vector<DataPoint> points) {
		this.points = points;
		similarityList = new Vector<Similarity>();
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
					
					// add similarity measures to the list
					Similarity similarity = new Similarity();
					similarity.setPoint_J(x);
					similarity.setPoint_K(y);
					similarity.setSimilarity(mathUtil.getEuclideanDistance(x, y));
					similarityList.addElement(similarity);
				}
			}
		}
	}
}
