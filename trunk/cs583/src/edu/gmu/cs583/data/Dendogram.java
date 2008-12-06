package edu.gmu.cs583.data;

import java.util.Vector;
import edu.gmu.cs583.util.Geometry;

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
	private DataPoint centroid;

	/**
	 * Default Constructor
	 */
	public Dendogram() {
		subDendograms = new Vector<Dendogram>();
		cluster = new Cluster();
	}
	
	/**
	 * @return the centroid
	 */
	public DataPoint getCentroid() {
		return centroid;
	}
	/**
	 * @param centroid the centroid to set
	 */
	public void setCentroid(DataPoint centroid) {
		this.centroid = centroid;
	}

	/**
	 * Calculate centroid of the dendogram.
	 */
	public void calculateCentroid() {
		if (!subDendograms.isEmpty()) {
			centroid = new DataPoint();
			int i = 0;
			for (Dendogram dendogram : subDendograms) {
				dendogram.calculateCentroid();
				centroid.setX(centroid.getX() + dendogram.getCentroid().getX());
				centroid.setY(centroid.getY() + dendogram.getCentroid().getY());
				i++;
			}
			centroid.setX(centroid.getX() / i);
			centroid.setY(centroid.getY() / i);
		}
		else {
			DataPoint x = cluster.getPoints().get(0);
			DataPoint y = cluster.getPoints().get(1);
			centroid = Geometry.average(x, y);
		}
	}

	/**
	 * @return the subDendograms
	 */
	public Vector<Dendogram> getSubDendograms() {
		return subDendograms;
	}

	/**
	 * Initialization Method for hierarchical method, sets each data point into
	 * its own cluster.
	 * 
	 * @param dataPoints
	 *            Data Points to cluster.
	 * @throws Exception
	 */
	public Dendogram(Vector<DataPoint> dataPoints) throws Exception {
		subDendograms = new Vector<Dendogram>();
		for (DataPoint point : dataPoints) {
			Vector<DataPoint> points = new Vector<DataPoint>();
			points.add(point);
			Cluster cluster = new Cluster();
			cluster.setPoints(points);
			Dendogram dendogram = new Dendogram();
			dendogram.setCluster(cluster);
			subDendograms.addElement(dendogram);
		}
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
		if (cluster.getPoints().size() < 2) {
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
			for (int i = 0; i <= cluster.getPoints().size() - 1; i++) {
				str += "(" + cluster.getPoints().get(i).getX() + ", "
						+ cluster.getPoints().get(i).getY() + ")";
			}
		} else {
			for (int i = 0; i <= subDendograms.size() - 1; i++) {
				if (i == subDendograms.size() - 1)
					str += subDendograms.get(i);
				else
					str += subDendograms.get(i) + ", ";
			}
		}

		str += "}";
		return str;
	}
}
