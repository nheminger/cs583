package edu.gmu.cs583.hierarchical;

import edu.gmu.cs583.data.*;

/**
 * Similarity is used to represent similarity/dis-similarity between points
 * using Euclidean distance.
 * 
 * @author Chris Andrade
 * 
 */
public class Similarity implements Comparable<Similarity> {

	private Dendogram point_J;
	private Dendogram point_K;
	private Double similarity;

	/**
	 * @return the point_J
	 */
	public Dendogram getPoint_J() {
		return point_J;
	}

	/**
	 * @param point_J
	 *            the point_J to set
	 */
	public void setPoint_J(Dendogram point_J) {
		this.point_J = point_J;
	}

	/**
	 * @return the point_K
	 */
	public Dendogram getPoint_K() {
		return point_K;
	}

	/**
	 * @param point_K
	 *            the point_K to set
	 */
	public void setPoint_K(Dendogram point_K) {
		this.point_K = point_K;
	}

	/**
	 * @return the similarity
	 */
	public Double getSimilarity() {
		return similarity;
	}

	/**
	 * @param similarity
	 *            the similarity to set
	 */
	public void setSimilarity(Double similarity) {
		this.similarity = similarity;
	}

	/**
	 * Compares two similarity objects
	 */
	public int compareTo(Similarity anotherSimilarity) {
		return similarity.compareTo(anotherSimilarity.getSimilarity());
	}
	
	/**
	 * Compares two similarity objects.
	 * @param anotherSimilarity another similarity object.
	 * @return true if two Similarity objects are equal, false otherwise.
	 */
	public boolean equals(Similarity anotherSimilarity) {
		boolean equal = false;
		
		Dendogram point_J = anotherSimilarity.getPoint_J();
		Dendogram point_K = anotherSimilarity.getPoint_K();
		if(point_J.equals(point_K))
			equal = true;
		
		return equal;
	}
}
