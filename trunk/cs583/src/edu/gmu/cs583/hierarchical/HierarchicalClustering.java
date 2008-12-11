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
		SINGLE_LINK, COMPLETE_LINK, AVERAGE_LINK;
	};

	public static void main(String[] args) throws Exception {

		int dimensions = new Integer(args[0]);
		int[] range = new int[dimensions];
		for(int i = 0; i <= dimensions - 1; i++) {
			range[i] = 300;
		}
		Integer numPoints = new Integer(args[1]);
		
		
		HierarchicalClustering clustering = new HierarchicalClustering(
				LINK_TYPE.AVERAGE_LINK);
		
		Vector<DataPoint> points = PointGenerator.generateAndReturnPoints(dimensions, range, numPoints, true, false);
		
		long startTime = System.currentTimeMillis();
		clustering.calculateClusters(points);
		long endTime = System.currentTimeMillis();
		System.out.println("\nMilliseconds Elapsed: " + (endTime - startTime));
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
		if (linkageType.equals(LINK_TYPE.AVERAGE_LINK))
			result = calculateAverageLinkClusters(data);
		
		return result;
	}

	private Dendogram calculateAverageLinkClusters(Vector<DataPoint> data)
			throws Exception {
		Dendogram result = new Dendogram();
		Vector<Dendogram> clusters = new Vector<Dendogram>();

		// Put each data point into its own cluster
		for (DataPoint point : data) {
			Dendogram dendogram = new Dendogram(point);
			dendogram.setDimensions(point.getDimensions());
			clusters.addElement(dendogram);
		}

		while (clusters.size() > 1) {

			System.out.println("\nNumber of clusters:\t" + clusters.size());
			System.out.println("---------------------------------------------");
			int i = 1;
			for (Dendogram cluster : clusters) {
				StringBuffer str = new StringBuffer("Centroid:\t");
				for (int dim = 0; dim <= cluster.getCentroid().getDimensions() - 1; dim++) {
					str.append(cluster.getCentroid().getCoords()[dim] + " ");
				}
				System.out.println(str);

				str = new StringBuffer();
				str.append("Contained Points:\n\t");
				for (DataPoint point : cluster.getPoints()) {
					for (int dim = 0; dim <= point.getDimensions() - 1; dim++) {
						str.append(" " + point.getCoords()[dim]);
						if(dim == point.getDimensions() - 1)
							str.append(" " + i + "\n\t");
					}
				}
				System.out.println(str);
				i++;
			}
			System.out
					.println("---------------------------------------------\n");

			// calculate similarity between clusters
			similarityMatrix = new HierarchicalSimilarityMatrix(clusters);
			similarityMatrix.calculateAverageLinkSimilarity();

			// sort them by minimum distance
			Collections.sort(similarityMatrix.getSimilarityList(),
					new SimilarityComparator());

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
				dendogram.setDimensions(cluster_J.getDimensions());
				dendogram.recalculateAverageCentroid();
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
