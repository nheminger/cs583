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

		HierarchicalClustering clustering = new HierarchicalClustering(
				LINK_TYPE.SINGLE_LINK);
		PointGenerator generator = new PointGenerator(10, 10, 125);
		generator.GeneratePoints();
		Vector<DataPoint> points = generator.GetPointsVector();
		System.out.print(clustering.calculateClusters(points));
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
		Vector<Dendogram> clusters = new Vector<Dendogram>();

		// Put each data point into its own cluster
		for (DataPoint point : data) {
			Dendogram dendogram = new Dendogram(point);
			clusters.addElement(dendogram);
		}

		while (clusters.size() > 1) {

			// calculate similarity between clusters
			similarityMatrix = new HierarchicalSimilarityMatrix(clusters);
			similarityMatrix.calculateSimilarity();

			// sort them by minimum distance
			Collections.sort(similarityMatrix.getSimilarityList(),
					new SingleLinkComparator());

			if (!similarityMatrix.getSimilarityList().isEmpty()) {
				// get two clusters which are similar
				Dendogram cluster_J = similarityMatrix.getSimilarityList().get(
						0).getPoint_J();
				Dendogram cluster_K = similarityMatrix.getSimilarityList().get(
						0).getPoint_K();

				// merge them into one cluster
				Dendogram dendogram = new Dendogram();
				dendogram.addSubDendogram(cluster_J);
				dendogram.addSubDendogram(cluster_K);
				dendogram.recalculateCentroid();
				similarityMatrix.getSimilarityList().remove(0);
				clusters.remove(cluster_J);
				clusters.remove(cluster_K);
				clusters.addElement(dendogram);
			}
		}
		result.addSubDendogram(clusters.get(0));
		return result;
	}

	private Dendogram calculateCompleteLinkClusters(Vector<DataPoint> data)
			throws Exception {
		Dendogram result = new Dendogram();
		Vector<Dendogram> clusters = new Vector<Dendogram>();

		// Put each data point into its own cluster
		for (DataPoint point : data) {
			Dendogram dendogram = new Dendogram(point);
			clusters.addElement(dendogram);
		}

		while (clusters.size() > 1) {

			// calculate similarity between clusters
			similarityMatrix = new HierarchicalSimilarityMatrix(clusters);
			similarityMatrix.calculateSimilarity();

			// sort them by minimum distance
			Collections.sort(similarityMatrix.getSimilarityList(),
					new CompleteLinkComparator());

			if (!similarityMatrix.getSimilarityList().isEmpty()) {
				// get two clusters which are similar
				Dendogram cluster_J = similarityMatrix.getSimilarityList().get(
						0).getPoint_J();
				Dendogram cluster_K = similarityMatrix.getSimilarityList().get(
						0).getPoint_K();

				// merge them into one cluster
				Dendogram dendogram = new Dendogram();
				dendogram.addSubDendogram(cluster_J);
				dendogram.addSubDendogram(cluster_K);
				dendogram.recalculateCentroid();
				similarityMatrix.getSimilarityList().remove(0);
				clusters.remove(cluster_J);
				clusters.remove(cluster_K);
				clusters.addElement(dendogram);
			}
		}
		result.addSubDendogram(clusters.get(0));
		return result;
	}
}
