package edu.gmu.cs583.hierarchical;

import java.util.Collections;
import java.util.Vector;
import edu.gmu.cs583.data.*;
import edu.gmu.cs583.data.PointGenerator;

/**
 * Calculates a clustering of DataPoints using the hierarchical agglomerative
 * method. This method may be done using single link, or complete link. It
 * returns a dendogram representing the hierarchy of computed clusters.
 * 
 * @author Chris Andrade
 * 
 */
public class HierarchicalClustering {
	public static enum LINK_TYPE {
		SINGLE_LINK, COMPLETE_LINK
	};

	public static void main(String[] args) throws Exception {
		
		HierarchicalClustering clustering = new HierarchicalClustering(LINK_TYPE.COMPLETE_LINK);
		PointGenerator generator = new PointGenerator(10);
		generator.GeneratePoints();
		Vector<DataPoint> points = generator.GetPointsVector();
		clustering.calculateClusters(points);
	}
	
	private HierarchicalSimilarityMatrix similarityMatrix;
	private LINK_TYPE linkageType;

	/**
	 * Constructor for object to computer clustering using the agglomerative
	 * hierarchical method.
	 * 
	 * @param linkageType
	 *            Linkage Type single or complete.
	 */
	public HierarchicalClustering(LINK_TYPE linkageType) {
		this.linkageType = linkageType;
	}

	/**
	 * Calculates a dendogram representing the hierarchy of clustering for a
	 * given set of points.
	 * 
	 * @param data
	 *            a set of data points.
	 * @return a dendogram representing the hierarchy of clustering for a given
	 *         set of points.
	 * @exception
	 */
	public Dendogram calculateClusters(Vector<DataPoint> data) throws Exception {
		Dendogram result = null;

		// compute cluster membership using single link or complete link method
		if (linkageType.equals(LINK_TYPE.SINGLE_LINK))
			result = calculateSingleLinkClusters(data);
		else
			result = calculateCompleteLinkClusters(data);

		return result;
	}

	private Dendogram calculateSingleLinkClusters(Vector<DataPoint> data)
			throws Exception {
		Dendogram result = new Dendogram();
		
		return result;
	}

	private Dendogram calculateCompleteLinkClusters(Vector<DataPoint> data)
			throws Exception {
		Dendogram result = new Dendogram();

		return result;
	}
}
