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
	private DataPoint value;
	private DataPoint centroid;

	/**
	 * Default Constructor
	 */
	public Dendogram() {
		super();
	}

	/**
	 * Creates a dendogram with a single DataPoint value.
	 * 
	 * @param value
	 *            DataPoint value.
	 */
	public Dendogram(DataPoint value) {
		this.value = value;
	}

	/**
	 * Adds a subdendogram to the dendogram.
	 * 
	 * @param subDendogram
	 *            subdendogram to add to the dendogram.
	 */
	public void addSubDendogram(Dendogram subDendogram) {
		subDendograms.addElement(subDendogram);
	}

	/**
	 * Recalculate the centroid of the Cluster based on its sub-clusters
	 * 
	 * @return the centroid representing the center of the dendogram.
	 */
	public DataPoint recalculateCentroid() {

		if (subDendograms.size() > 1) {
			centroid = new DataPoint();
			int count = 0;
			for (Dendogram dendogram : subDendograms) {
				centroid.setX(centroid.getX()
						+ dendogram.recalculateCentroid().getX());
				centroid.setY(centroid.getY()
						+ dendogram.recalculateCentroid().getY());
				count++;
			}
			centroid.setX(centroid.getX() / count);
			centroid.setY(centroid.getY() / count);
		}

		return centroid;
	}

	/**
	 * @return the value
	 */
	public DataPoint getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(DataPoint value) {
		this.value = value;
		this.centroid = value;
	}

	/**
	 * @return the centroid
	 */
	public DataPoint getCentroid() {
		return centroid;
	}

	/**
	 * @param centroid
	 *            the centroid to set
	 */
	public void setCentroid(DataPoint centroid) {
		this.centroid = centroid;
	}

	/**
	 * @return the subDendograms
	 */
	public Vector<Dendogram> getSubDendograms() {
		return subDendograms;
	}

	/**
	 * String representation of the Dendogram.
	 */
	public String toString() {
		String str = "{";
		if (subDendograms.isEmpty())
			str += "(" + value.getX() + ", " + value.getY() + ")";
		else {
			for (int i = 0; i <= subDendograms.size() - 1; i++) {
				if (i == subDendograms.size() - 1)
					str += "\n" + subDendograms.toString();
				else
					str += "\n" + subDendograms.toString() + ", ";
			}
		}

		str += "}";
		return str;
	}
}
