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
		subDendograms = new Vector<Dendogram>();
		value = new DataPoint();
		centroid = new DataPoint();
	}

	/**
	 * Creates a dendogram with a single DataPoint value.
	 * 
	 * @param value
	 *            DataPoint value.
	 */
	public Dendogram(DataPoint value) {
		this.value = value;
		this.centroid = value;
		this.subDendograms = new Vector<Dendogram>();
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
				centroid = new DataPoint(dendogram.getCentroid().getCoords());

				for (int dim = 0; dim <= centroid.getDimensions() - 1; dim++) {
					double val = centroid.getCoords()[dim]
							+ dendogram.getCentroid().getCoords()[dim];
					centroid.getCoords()[dim] = val;
				}
				count++;
			}
			for (int dim = 0; dim <= centroid.getDimensions() - 1; dim++) {
				double val = centroid.getCoords()[dim] / count;
				centroid.getCoords()[dim] = val;
			}
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
		StringBuffer str = new StringBuffer("{");
		if (subDendograms.isEmpty()) {
			str.append("(");
			for (int dim = 0; dim <= value.getDimensions() - 1; dim++) {
				if (dim != value.getDimensions())
					str.append(value.getCoords()[dim] + ", ");
				else
					str.append(value.getCoords()[dim]);
			}
			str.append(")");
		} else {
			for (int i = 0; i <= subDendograms.size() - 1; i++) {
				if (i == subDendograms.size() - 1)
					str.append(subDendograms.get(i).toString());
				else
					str.append(subDendograms.get(i).toString() + ", ");
			}
		}

		str.append("}");
		return str.toString();
	}

	/**
	 * Gets a list of points contained in the Dendogram.
	 * 
	 * @return a list of points contained in the Dendogram.
	 */
	public Vector<DataPoint> getPoints() {
		Vector<DataPoint> points = new Vector<DataPoint>();

		for (Dendogram subCluster : subDendograms) {
			points.addAll(subCluster.getPoints());
		}
		if (value != null)
			points.addElement(value);

		return points;
	}

	/**
	 * Compares two dendogram objects.
	 */
	public boolean equals(Object anotherObj) {
		boolean equal = true;
		Dendogram anotherDendogram = (Dendogram) anotherObj;
		anotherDendogram.recalculateCentroid();
		this.recalculateCentroid();

		for (int dim = 0; dim <= this.getCentroid().getDimensions() - 1; dim++) {
			double thisVal = this.getCentroid().getCoords()[dim];
			double otherVal = anotherDendogram.getCentroid().getCoords()[dim];
			if (thisVal != otherVal)
				equal = false;
		}

		return equal;
	}
}
