package edu.gmu.cs583.hierarchical;

import java.util.Comparator;

/**
 * SingleLinkComparator uses minimum distance when comparing elements.
 * 
 * @author Chris Andrade
 */
public class SingleLinkComparator implements Comparator<Similarity> {

	/**
	 * Compares two similarity objects.
	 */
	public int compare(Similarity o1, Similarity o2) {
		if (o1.getSimilarity().equals(o2.getSimilarity()))
			return 0;
		else if (o1.getSimilarity() > o2.getSimilarity())
			return 1;

		return -1;
	}
}
