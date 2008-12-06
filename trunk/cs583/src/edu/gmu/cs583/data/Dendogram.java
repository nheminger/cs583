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
	private Vector<DataPoint> cluster;

	/**
	 * Default Constructor
	 */
	public Dendogram() {
		subDendograms = new Vector<Dendogram>();
		cluster = new Vector<DataPoint>();
	}

	/**
	 * Adds a sub-dendogram to the dendogram
	 * @param subDendogram sub-dendogram to add to the dendogram.
	 */
	public void addSubDendogram(Dendogram subDendogram) {
		cluster = new Vector<DataPoint>();
		subDendograms.addElement(subDendogram);
	}

	/**
	 * Removes the sub-dendogram at the specified index.
	 * @param index index of the sub-dendogram to remove.
	 */
	public void removeSubDendogram(int index) {
		if(!subDendograms.isEmpty())
			subDendograms.remove(index);
	}
	
	/**
	 * Returns the sub-dendogram contained at the specified index.
	 * @param index index of the desired sub-dendogram to obtain.
	 * @return sub-dendogram at the specified index.
	 */
	public Dendogram getSubDendogram(int index) {
		Dendogram subDendogram = null;
		if(!subDendograms.isEmpty()) {
			subDendogram = subDendograms.get(index);
		}
		return subDendogram;
	}
	
	/**
	 * Sets the cluster.
	 * @param cluster the cluster to set.
	 */
	public void setCluster(Vector<DataPoint> cluster) {
		subDendograms = new Vector<Dendogram>();
		this.cluster = cluster;
	}
	
	/**
	 * Returns the cluster.
	 * @return the cluster.
	 */
	public Vector<DataPoint> getCluster() {
		return cluster;
	}
	
	/**
	 * Displays the Dendogram as a multi-tiered string.
	 */
	public String toString() {
		String str = "{";
		if (!cluster.isEmpty()) {
			for (int i = 0; i <= cluster.size() - 1; i++) {
				if (i == 0)
					str += "(" + cluster.get(0).getX() + ", "
							+ cluster.get(0).getY() + ")";
				else
					str += ",(" + cluster.get(0).getX() + ", "
							+ cluster.get(0).getY() + ")";
			}
			str += "}";
		} else {
			for (Dendogram subDendogram : subDendograms) {
				str += "\n" + subDendogram.toString();
			}
			str += "}";
		}
		return str;
	}
}
