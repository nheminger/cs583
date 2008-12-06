package edu.gmu.cs583.hierarchical;

import edu.gmu.cs583.data.*;

public class Similarity implements Comparable<Similarity> {
	
	private DataPoint point_J;
	private DataPoint point_K;
	private Long similarity;
	
	/**
	 * @return the point_J
	 */
	public DataPoint getPoint_J() {
		return point_J;
	}
	/**
	 * @param point_J the point_J to set
	 */
	public void setPoint_J(DataPoint point_J) {
		this.point_J = point_J;
	}
	/**
	 * @return the point_K
	 */
	public DataPoint getPoint_K() {
		return point_K;
	}
	/**
	 * @param point_K the point_K to set
	 */
	public void setPoint_K(DataPoint point_K) {
		this.point_K = point_K;
	}
	/**
	 * @return the similarity
	 */
	public long getSimilarity() {
		return similarity;
	}
	/**
	 * @param similarity the similarity to set
	 */
	public void setSimilarity(long similarity) {
		this.similarity = similarity;
	}
	
	/**
	 * Compares two similarity objects.
	 */
	public int compareTo(Similarity anotherSimilarity) {
		return similarity.compareTo(anotherSimilarity.similarity);
	}
}
