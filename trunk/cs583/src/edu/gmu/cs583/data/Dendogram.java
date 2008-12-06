package edu.gmu.cs583.data;

import java.util.Vector;

/**
 * The Dendogram class is used to represent how the hierarchical clustering
 * method composed the clusters. A Dendogram may contain other dendograms called
 * sub-dendograms, or be composed of a single cluster of two points; not both.
 * 
 * @author Chris Andrade
 */
public class Dendogram {

	private Vector<Dendogram> subDendograms;
	private Cluster cluster;

	/**
	 * Default Constructor
	 */
	public Dendogram() {
		subDendograms = new Vector<Dendogram>();
		cluster = new Cluster();
	}

	/**
	 * Adds a sub-dendogram to the dendogram
	 * 
	 * @param subDendogram
	 *            sub-dendogram to add to the dendogram.
	 */
	public void addSubDendogram(Dendogram subDendogram) {
		cluster = new Cluster();
		subDendograms.addElement(subDendogram);
	}

	/**
	 * Removes the sub-dendogram at the specified index.
	 * 
	 * @param index
	 *            index of the sub-dendogram to remove.
	 */
	public void removeSubDendogram(int index) {
		if (!subDendograms.isEmpty())
			subDendograms.remove(index);
	}

	/**
	 * Returns the sub-dendogram contained at the specified index.
	 * 
	 * @param index
	 *            index of the desired sub-dendogram to obtain.
	 * @return sub-dendogram at the specified index.
	 */
	public Dendogram getSubDendogram(int index) {
		Dendogram subDendogram = null;
		if (!subDendograms.isEmpty()) {
			subDendogram = subDendograms.get(index);
		}
		return subDendogram;
	}

	/**
	 * Sets the cluster.
	 * 
	 * @param cluster
	 *            the cluster to set.
	 */
	public void setCluster(Cluster cluster) throws Exception {
		if (cluster.getCluster().size() < 2) {
			subDendograms = new Vector<Dendogram>();
			this.cluster = cluster;
		} else
			throw new Exception(
					"Cluster for dendogram can not contain more than 2 points.");
	}

	/**
	 * Returns the cluster.
	 * 
	 * @return the cluster.
	 */
	public Cluster getCluster() {
		return cluster;
	}

	/**
	 * Displays the Dendogram as a multi-tiered string.
	 */
	public String toString() {
		String str = "{";

		if (subDendograms.isEmpty()) {

		}

		str += "}";
		return str;
	}
}
