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
				if (i != j) {
					// calculate similarities
					Dendogram cluster_J = clusters.get(i);
					Dendogram cluster_K = clusters.get(j);
					Similarity similarity = new Similarity();
					similarity.setPoint_J(cluster_J);
					similarity.setPoint_K(cluster_K);
					similarity.setSimilarity(Geometry.getDistance(cluster_J
							.getCentroid(), cluster_K.getCentroid(), false));
					similarityList.addElement(similarity);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void updateSimilarities(Dendogram oldCluster_x,
			Dendogram oldCluster_y, Dendogram updated) {
		Vector<Dendogram> _clusters = (Vector<Dendogram>) clusters.clone();
		Vector<Similarity> _similarityList = (Vector<Similarity>) similarityList
				.clone();

		int i = 0;
		while (i <= clusters.size() - 1) {
			Dendogram dendogram = clusters.get(i);
			if (dendogram.equals(oldCluster_x)) {
				_clusters.remove(dendogram); // remove dendogram from list
			} else if (dendogram.equals(oldCluster_y)) {
				_clusters.remove(dendogram); // remove dendogram from list
			} else {
				Similarity sim = new Similarity();

				// update similarity rows
				sim.setPoint_J(updated);
				sim.setPoint_K(dendogram);
				sim.setSimilarity(Geometry.getDistance(updated.getCentroid(),
						dendogram.getCentroid()));
				_similarityList.addElement(sim);

				// update similarity cols
				sim = new Similarity();
				sim.setPoint_J(dendogram);
				sim.setPoint_K(updated);
				sim.setSimilarity(Geometry.getDistance(dendogram.getCentroid(),
						updated.getCentroid()));
				_similarityList.addElement(sim);
			}

			// remove old similarity rows
			Similarity sim = new Similarity();
			sim.setPoint_J(oldCluster_x);
			sim.setPoint_K(dendogram);
			_similarityList.remove(sim);

			// remove old similarity cols
			sim = new Similarity();
			sim.setPoint_J(dendogram);
			sim.setPoint_K(oldCluster_x);
			_similarityList.remove(sim);

			// remove old similarity rows
			sim = new Similarity();
			sim.setPoint_J(oldCluster_y);
			sim.setPoint_K(dendogram);
			_similarityList.remove(sim);

			// remove old similarity cols
			sim = new Similarity();
			sim.setPoint_J(dendogram);
			sim.setPoint_K(oldCluster_y);
			_similarityList.remove(sim);

			i++;
		}

		_clusters.addElement(updated);

		if (clusters.size() <= _clusters.size())
			System.err.println("Cluster size is increasing!");

		if (similarityList.size() <= _similarityList.size())
			System.err.println("Similarity matrix is increasing!");

		clusters = _clusters;
		similarityList = _similarityList;
	}
}
