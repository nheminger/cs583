package edu.gmu.cs583.hierarchical;

import java.util.*;
import edu.gmu.cs583.data.*;
import edu.gmu.cs583.util.*;

/**
 * Used to calculate a simularity matrix for the Hierarchical clustering method.
 * 
 * @author Chris Andrade
 */
public class HierarchicalSimilarityMatrix {

	private Vector<Similarity> similarityList;
	private Vector<Dendogram> clusters;

	/**
	 * Generate a similarity matrix from the given dendograms
	 * 
	 * @param clusters
	 *            a set of dendograms.
	 */
	public HierarchicalSimilarityMatrix(Vector<Dendogram> clusters) {
		this.clusters = clusters;
		similarityList = new Vector<Similarity>();
	}

	/**
	 * @return the similarityList
	 */
	public Vector<Similarity> getSimilarityList() {
		return similarityList;
	}

	/**
	 * @param similarityList
	 *            the similarityList to set
	 */
	public void setSimilarityList(Vector<Similarity> similarityList) {
		this.similarityList = similarityList;
	}

	/**
	 * @return the clusters
	 */
	public Vector<Dendogram> getClusters() {
		return clusters;
	}

	/**
	 * @param clusters
	 *            the clusters to set
	 */
	public void setClusters(Vector<Dendogram> clusters) {
		this.clusters = clusters;
	}

	/**
	 * Calculates the similarity of data points using Euclidean distance.
	 * 
	 * @param type
	 *            Type Hierarchical LINK type.
	 */
	public void calculateSimilarity() {
		for (int i = 0; i <= clusters.size() - 1; i++) {
			for (int j = 0; j <= clusters.size() - 1; j++) {
				// calculate similarities
				Dendogram cluster_J = clusters.get(i);
				Dendogram cluster_K = clusters.get(j);
				Similarity similarity = new Similarity();
				similarity.setPoint_J(cluster_J);
				similarity.setPoint_K(cluster_K);
				similarity.setSimilarity(Geometry.getEuclideanDistance(
						cluster_J.getCentroid(), cluster_K.getCentroid()));
				similarityList.addElement(similarity);
			}
		}
	}
}
