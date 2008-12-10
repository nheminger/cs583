package edu.gmu.cs583.rtree;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Vector;

import edu.gmu.cs583.data.DataPoint;
import edu.gmu.cs583.data.PointGenerator;
import edu.gmu.cs583.kmeans_basic.Kmeans;
import edu.gmu.cs583.util.Geometry;

public class RTree {
	public static final int QUADRATICSPLIT = 0;
	public static final int LINEARSPLIT = 1;
	public static final int EXHAUSTIVESPLIT = 2;
	public RTreeContext context;
	private int splitting = 0;
	private RTreeNode root;
	private String name;
	private int M;
	private int m;
	protected boolean debug = false;
	int finalNumberOfCluster = 0;
	
	private static Vector<Integer> pointSize = Geometry.makeRTreePointSize();
	
	

	public RTree(int maxentries) {
		this(maxentries, maxentries / 2, "No name", null);
	}

	public RTree(int maxentries, String name) {
		this(maxentries, maxentries / 2, name, null);
	}

	public RTree(int maxentries, int minentries) {
		this(maxentries, minentries, "No name", null);
	}

	public RTree(int maxentries, int minentries, String name) {
		this(maxentries, minentries, name, null);
	}

	public RTree(int maxentries, int minentries, String name, RTreeContext cntxt) {
		context = cntxt;
		if (maxentries < 2) {
			System.out.println("M has to be greater than 1!, the suggested M: "
					+ maxentries + " - is too small");
			throw new Error(
					"Attempt at creating a degenerate R-Tree (M has to be greater than 1!, the suggested M: "
							+ maxentries + " - is too small");
		}
		if (minentries < 1) {
			System.out
					.println("m has to be greater than or equal to 1!, your suggestion: "
							+ minentries + " - is too small");
			throw new Error(
					"Attempt at creating an inconsistent R-Tree (m has to be greater than or equal to 1!, your suggestion: "
							+ minentries + " - is too small");
		}
		if (minentries > maxentries / 2) {
			System.out
					.println("m has to be smaller than or equal to M/2!, your suggestion: M="
							+ maxentries + ", m=" + minentries + ".");
			throw new Error(
					"Attempt at creating an inconsistent R-Tree (m has to be smaller than or equal to M/2!, your suggestion: M="
							+ maxentries + ", m=" + minentries + ".");
		}
		this.name = name;
		root = new RTreeLeafNode(this);
		M = maxentries;
		m = minentries;
	}

	
	// adapted from http://gis.umb.no/gis/applets/rtree2/jdk1.1/noumbgisrtree.jar
	@SuppressWarnings({"unchecked","unused"})
	public static void main(String[] args) {
		
		Vector<DataPoint> randomGeneratedPoints = null;
		try {
			randomGeneratedPoints = PointGenerator.generatePointsInShapeAndReturn("c:/natural_clusters.dat", 2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("SIZE: " + randomGeneratedPoints.size());
		
		int clusterNum = 4;
		int maxEntries = randomGeneratedPoints.size() / clusterNum;
		
		RTree rtree = new RTree(maxEntries);
		long starttime = System.currentTimeMillis();
		for (int i = 0; i < randomGeneratedPoints.size(); i++) {
			DataPoint dp = randomGeneratedPoints.get(i);
			rtree.insert(new RTreeLeafEntry(null, new BoundingBox(dp.getCoords()[0], dp.getCoords()[1], dp.getCoords()[0], dp.getCoords()[1])));
		}
		long endtime = System.currentTimeMillis();
		
		// iterate over results
		rtree.iterateTree(rtree.getRoot(), 1);
		

		RTree.logStats(rtree.finalNumberOfCluster, randomGeneratedPoints.size(), (endtime - starttime), null);
		
	
		
//		for(int clusterNum = 2; clusterNum < 10; clusterNum++){  // testing range of centroids 2 - 10
//				System.out.println("testing " + clusterNum + " centroids....");
//				
//				for(Integer pointNum: pointSize){ // points size 10 - 100000 increasing by a factor of 10 each test
//
//					for(int k = 0; k < 20; k++ ){ // run test 20 times and get averages
//						System.out.println("number of data points: " + pointNum);
//						
//						int maxEntries = pointNum / clusterNum;						
//						System.out.println("Using a max entries of " + maxEntries);
//						
//						RTree rtree = new RTree(maxEntries);
//						System.out.println("Entering RTree");
//
//						Vector<DataPoint> randomGeneratedPoints = null;
//						try {
//							int[] range = new int[2];
//							range[0] = pointNum;
//							range[1] = pointNum;
//							randomGeneratedPoints = PointGenerator.generateAndReturnPoints(2, range, pointNum, true, true);
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//							System.exit(0);
//						}
//
//						// run the r-tree insertions while recording time
//						long starttime = System.currentTimeMillis();
//						for (int i = 0; i < randomGeneratedPoints.size(); i++) {
//							DataPoint dp = randomGeneratedPoints.get(i);
//							rtree.insert(new RTreeLeafEntry(null, new BoundingBox(dp.getCoords()[0], dp.getCoords()[1], dp.getCoords()[0], dp.getCoords()[1])));
//						}
//						long endtime = System.currentTimeMillis();
//						
//						// iterate over results
//						rtree.iterateTree(rtree.getRoot(), 1);
//						
//						RTree.logStats(rtree.finalNumberOfCluster, pointNum, (endtime - starttime), null);
//					
//					}
//				}
//		}

//		PointGenerator gen = new PointGenerator(numberOfPoints);
//		gen.GeneratePoints();
//		Vector<DataPoint> points = gen.GetPointsVector();
//		for (int i = 0; i < points.size(); i++) {
//			DataPoint dp = points.get(i);
//			rtree.insert(new RTreeLeafEntry(null, new BoundingBox(dp.getX(), dp.getY(), dp.getX(), dp.getY())));
//		}
		
//		rtree.insert(new RTreeLeafEntry(null, new BoundingBox(10.0, 10.0, 20.0,20.0)));
//		rtree.insert(new RTreeLeafEntry(null, new BoundingBox(15.0, 15.0, 25.0,25.0)));
//		rtree.insert(new RTreeLeafEntry(null, new BoundingBox(5.0, 5.0, 15.0,15.0)));
//		rtree.insert(new RTreeLeafEntry(null, new BoundingBox(20.0, 20.0, 30.0,30.0)));
//		rtree.insert(new RTreeLeafEntry(null, new BoundingBox(0.0, 0.0, 10.0,10.0)));
//		rtree.insert(new RTreeLeafEntry(null, new BoundingBox(6.0, 3.0, 53.0,10.0)));
//		rtree.insert(new RTreeLeafEntry(null, new BoundingBox(36.0, 36.0, 43.0,43.0)));
//		rtree.insert(new RTreeLeafEntry(null, new BoundingBox(64.0, 20.0, 64.0,20.0)));
//		rtree.insert(new RTreeLeafEntry(null, new BoundingBox(54.0, 3.0, 64.0,53.0)));
//		rtree.insert(new RTreeLeafEntry(null, new BoundingBox(4.0, 4.0, 54.0,54.0)));

		//Vector endClusters = rtree.search(new BoundingBox(Double.MIN_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MAX_VALUE));

		
		System.out.println("Leaving RTree");
	}
	
	public static void logStats(Integer number_of_cents, Integer number_of_pts, long time,Integer iters){
		   
		try{
			     File f=new File("c:\\rtree_stats.txt");
			      if(f.exists()){
			      String str= number_of_cents + ":" + number_of_pts+ ":" + time + ":" + iters + "\n";
			          BufferedWriter out = new BufferedWriter(new FileWriter(f, true));
			          out.write(str);
			          out.close();
			          //System.out.println("The data has been written");
			          }
			          else
			            System.out.println("This file is not exist");
		}catch (Exception e){
			System.out.println("LOG STATS ERROR: writing to file failed");
		}
			    
	}
	
	public void visitAllChildren(RTreeNode leaf, int branchId) {
		
		boolean hadChild = false;
		for (int i = 0; i < leaf.entries.size(); i++) {
			//System.out.println("Leaf entry [" + branchId + "]: " + leaf.getParent().entries.get(i));
			BoundingBox bbox = ((RTreeEntry)leaf.entries.get(i)).getI();
			System.out.println( bbox.getLeft() + "\t" + bbox.getLower() + "\t" + branchId);
			hadChild = true;
		}
		
		// save the largest cluster id as the total number of clusters
		if (hadChild & (branchId > this.finalNumberOfCluster)) {
			 this.finalNumberOfCluster = branchId;
		}
	}
	
	public synchronized RTreeLeafNode iterateTree(RTreeNode currentNode, int branchId) {

		//System.out.println("CLUSTER");
		if (currentNode.isLeaf()) {
			// visit siblings here?
			visitAllChildren(currentNode, branchId);
			return (RTreeLeafNode) currentNode;
		}
		
		for (int i = 0; i < currentNode.entries.size(); i++) {
			RTreeInternalEntry preferredie = (RTreeInternalEntry) currentNode.entries.elementAt(i);
			iterateTree(preferredie.getPointer(), branchId++);
		}
		
		return null;
	}
	/*
	public void visitAllChildren(RTreeNode leaf) {
		for (int i = 0; i < leaf.getParent().entries.size(); i++) {
			System.out.println("Leaf entry: " + leaf.getParent().entries.get(i));
		}
	}
	
	public synchronized RTreeLeafNode iterateTree(RTreeNode currentNode) {

		System.out.println("CLUSTER");
		if (currentNode.isLeaf()) {
			// visit siblings here?
			visitAllChildren(currentNode);
			return (RTreeLeafNode) currentNode;
		}
		
		
		for (int i = 0; i < currentNode.entries.size(); i++) {
			RTreeInternalEntry preferredie = (RTreeInternalEntry) currentNode.entries.elementAt(i);
			return iterateTree(preferredie.getPointer());
		}
		
		return null;
	} */
	
	public RTreeNode getRoot() {
		return root;
	}

	public int getM() {
		return M;
	}

	public int getm() {
		return m;
	}

	public synchronized void setM(int capacity) {
		if (capacity < 2) {
			System.out.println("M has to be greater than 1!, your suggestion: "
					+ capacity + " - is too small");
			throw new Error(
					"Attempt at creating a degenerate R-Tree (M has to be greater than 1!, the suggested M: "
							+ capacity + " - is too small");
		}
		M = capacity;
		if (m > M / 2)
			m = M / 2;
	}

	public synchronized void setm(int mincapacity) {
		if (mincapacity < 1) {
			System.out
					.println("m has to be greater than or equal to 1!, the suggested m: "
							+ mincapacity + " - is too small");
			throw new Error(
					"Attempt at creating an inconsistent R-Tree (m has to be greater than or equal to 1!, the suggested m: "
							+ mincapacity + " - is too small");
		}
		if (mincapacity > getM() / 2) {
			System.out
					.println("m has to be smaller than or equal to M/2!, the suggested M="
							+ getM() + ", m=" + mincapacity + ".");
			throw new Error(
					"Attempt at creating an inconsistent R-Tree (m has to be smaller than or equal to M/2!, the suggested M="
							+ getM() + ", m=" + mincapacity + ".");
		}
		m = mincapacity;
	}

	public int getSplitMethod() {
		return splitting;
	}

	public synchronized void setSplitMethod(int splitmethod) {
		if (splitmethod == 1 || splitmethod == 0 || splitmethod == 2)
			splitting = splitmethod;
		else {
			System.out
					.println("Illegal splitting method! The splitting method was not changed.");
			throw new Error(
					"Illegal splitting method! The splitting method was not changed.");
		}
	}

	public synchronized Vector search(BoundingBox S) {
		return root.search(S);
	}

	public synchronized void insert(RTreeLeafEntry le) {
		if (debug)
			System.out.println("Insert - Leaf entry: " + le);
		
		RTreeLeafNode ln = root.chooseLeaf(le);
		if (ln == null) {
			System.out.println("RTree, insert: INTERNAL ERROR...Leafnode for new entry not found!");
			throw new Error("RTree, insert: INTERNAL ERROR...Leafnode for new entry not found!");
		}
		if (debug)
			System.out.println("R-Tree, insert: Adding leaf entry: " + le);
		
		RTreeInternalEntry rootoverflowentry = ln.add(le);
		if (debug) {
			System.out.println(" RTree, insert: root level: " + root.getLevel() + ".");
			
			if (rootoverflowentry != null)
				System.out.println("Level overflow: " + rootoverflowentry.getPointer().getLevel());
			
			System.out.println(" RTree, insert: After adjustTree, leaf node: " + ln);
			System.out.println(" RTree, insert: root (after adjusting): " + root);
			System.out.println("RTree, insert: Root overflow: " + rootoverflowentry);
		}
		
		// overflow, need to re-assign
		if (rootoverflowentry != null)
			rootOverflow(rootoverflowentry);
		if (debug)
			System.out.println("RTree, insert - finished. ---- Root: " + root);
	}

	public synchronized void rootOverflow(RTreeInternalEntry rootoverflowentry) {
		if (debug) {
			System.out.println("RTree, rootOverflow: old root: " + root);
			System.out.println("RTree, rootOverflow: overflow entry: "
					+ rootoverflowentry);
			if (root.getLevel() != rootoverflowentry.getPointer().getLevel())
				System.out
						.println("ERROR - RTree, rootOverflow (start), level-error: root: "
								+ root.getLevel()
								+ ", overflow: "
								+ rootoverflowentry.getPointer().getLevel());
		}
		RTreeInternalEntry oldrootentry = new RTreeInternalEntry(root);
		if (debug)
			System.out.println("RTree, rootOverflow: old root entry: "
					+ oldrootentry);
		RTreeInternalNode newrootnode = new RTreeInternalNode(this);
		RTreeInternalEntry overflowentry = newrootnode.add(oldrootentry);
		if (overflowentry != null) {
			System.out
					.println("RTree, rootOverflow: INTERNAL ERROR... nonexpected root overflow");
			throw new Error(
					"RTree, rootOverflow: INTERNAL ERROR... nonexpected root overflow");
		}
		if (debug)
			System.out.println("RTree, rootOverflow: newrootnode: "
					+ newrootnode);
		if (newrootnode.getLevel() - 1 != rootoverflowentry.getPointer()
				.getLevel())
			System.out
					.println("ERROR - RTree, rootOverflow, level-error: newrn: "
							+ newrootnode.getLevel()
							+ ", oldre: "
							+ rootoverflowentry.getPointer().getLevel());
		overflowentry = newrootnode.add(rootoverflowentry);
		if (overflowentry != null) {
			System.out
					.println("RTree, rootOverflow: INTERNAL ERROR... unexpected root overflow");
			throw new Error(
					"RTree, rootOverflow: INTERNAL ERROR... unexpected root overflow");
		}
		if (debug)
			System.out.println("RTree, rootOverflow: newrootnode: "
					+ newrootnode);
		root = newrootnode;
		if (debug)
			System.out.println("RTree, rootOverflow, Root: " + root);
	}

	private synchronized void rootUnderflow() {
		if (debug)
			System.out.println("RTree, rootUnderflow: old root: " + root);
		while (root.entries.size() == 1 && !root.isLeaf()) {
			root = ((RTreeInternalEntry) root.entries.elementAt(0))
					.getPointer();
			root.setParent(null);
		}
		if (debug)
			System.out.println("RTree, rootUnderflow: new Root: " + root);
	}

	public synchronized void delete(RTreeLeafEntry le) {
		if (le == null) {
			System.out
					.println("RTree.delete() - Delete: ERROR le == null (cannot delete null entry)");
			throw new Error(
					"RTree.delete() - Delete: ERROR le == null (trying to delete null entry)");
		}
		RTreeLeafNode ln = root.findLeaf(le);
		if (ln == null) {
			System.out
					.println("RTree.delete() - Delete: ERROR Leaf entry not present in the tree, BB: "
							+ le.getI() + " RTree: " + this);
			throw new Error(
					"RTree.delete() - Delete: ERROR Leaf entry not present in the tree, BB: "
							+ le.getI());
		}
		if (debug)
			System.out.println("RTree, delete: Removing: " + le + " RTree: "
					+ this);
		ln.delete(le);
		if (debug)
			System.out
					.println("RTree, delete: Condensing the leaf node: " + ln);
		condenseTree(ln);
		rootUnderflow();
	}

	private synchronized void condenseTree(RTreeLeafNode ln) {
		Vector Q = new Vector();
		Q = ln.condense(Q);
		if (debug)
			System.out.println("RTree, condenseTree: Size of Q: " + Q.size());
		while (Q.size() > 0) {
			Vector entr = ((RTreeNode) Q.lastElement()).entries;
			Q.removeElement(Q.lastElement());
			if (debug)
				System.out.println("RTree, condenseTree: Number of entries: "
						+ entr.size());
			for (int i = 0; i < entr.size(); i++) {
				RTreeInternalEntry rootoverflow = root
						.reInsert((RTreeEntry) entr.elementAt(i));
				if (rootoverflow != null)
					rootOverflow(rootoverflow);
			}
		}
	}

	private synchronized int getSubtreeHeight(RTreeNode node) {
		if (node == null) {
			System.out.println("RTree - getSubtreeHeight: NULL entry");
			return 0;
		}
		if (node.isLeaf())
			return 1;
		if (node.entries.size() > 0) {
			if (debug)
				System.out.println("RTree, getSubtreeHeight: "
						+ ((RTreeEntry) node.entries.elementAt(0)).getI());
			return 1 + getSubtreeHeight(((RTreeInternalEntry) node.entries
					.elementAt(0)).getPointer());
		}
		return 0;
	}

	public int getHeight() {
		RTreeNode node = root;
		if (node == null)
			return 0;
		int treeheight = 1;
		if (!node.isLeaf()) {
			for (/**/; !node.isLeaf(); node = ((RTreeInternalEntry) node.entries
					.elementAt(0)).getPointer()) {
				treeheight++;
				if (node.entries == null || node.entries.size() <= 0)
					break;
			}
		}
		return treeheight;
	}

	public synchronized double getBrutto(int level) {
		return getBruttoCoverage(root, level);
	}

	public synchronized double getNetto(int level) {
		return getNettoCoverage(root, level);
	}

	private synchronized double getBruttoCoverage(RTreeNode rtreenode, int level) {
		double area = 0.0;
		if (debug)
			System.out.println("RTree, getBruttoCoverage(" + level + "): "
					+ rtreenode);
		if (rtreenode == null) {
			if (debug)
				System.out
						.println("RTree, getBruttoCoverage: rtreenode is NULL");
			return 0.0;
		}
		if (level == 0) {
			for (int i = 0; i < rtreenode.entries.size(); i++)
				area += ((RTreeEntry) rtreenode.entries.elementAt(i)).getI()
						.area();
			return area;
		}
		if (!rtreenode.isLeaf() && rtreenode.entries != null) {
			for (int i = 0; i < rtreenode.entries.size(); i++)
				area += getBruttoCoverage(
						((RTreeInternalEntry) rtreenode.entries.elementAt(i))
								.getPointer(), level - 1);
			return area;
		}
		return 0.0;
	}

	private synchronized void qsort(Vector v, int left, int right) {
		if (left < right) {
			swap(v, left, (left + right) / 2);
			int last = left;
			for (int i = left + 1; i <= right; i++) {
				if ((((RTreeEntry) v.elementAt(i)).getI().getLower() < ((RTreeEntry) v
						.elementAt(left)).getI().getLower())
						|| ((((RTreeEntry) v.elementAt(i)).getI().getLower() == ((RTreeEntry) v
								.elementAt(left)).getI().getLower()) && (((RTreeEntry) v
								.elementAt(i)).getI().getLeft() < ((RTreeEntry) v
								.elementAt(left)).getI().getLeft())))
					swap(v, ++last, i);
			}
			swap(v, left, last);
			qsort(v, left, last - 1);
			qsort(v, last + 1, right);
		}
	}

	private synchronized void xsort(Vector v, int left, int right) {
		if (left < right) {
			swap(v, left, (left + right) / 2);
			int last = left;
			for (int i = left + 1; i <= right; i++) {
				if (((RTreeEntry) v.elementAt(i)).getI().getLeft() < ((RTreeEntry) v
						.elementAt(left)).getI().getLeft())
					swap(v, ++last, i);
			}
			swap(v, left, last);
			xsort(v, left, last - 1);
			xsort(v, last + 1, right);
		}
	}

	private synchronized void swap(Vector v, int i, int j) {
		Object tempobj = v.elementAt(i);
		v.setElementAt(v.elementAt(j), i);
		v.setElementAt(tempobj, j);
		tempobj = null;
	}

	private synchronized double getNettoCoverage(RTreeNode rtreenode, int level) {
		Vector entr = getAllEntries(root, level);
		if (debug)
			System.out.println("RTree, getNettoCoverage - #entries@level "
					+ level + ": " + entr.size());
		if (entr == null)
			return 0.0;
		Vector active = new Vector();
		double sum = 0.0;
		if (debug)
			System.out.println("Sorting on y");
		qsort(entr, 0, entr.size() - 1);
		if (debug)
			System.out.println("Finished Sorting on y");
		active.addElement(entr.elementAt(0));
		entr.removeElementAt(0);
		double nexty = ((RTreeEntry) active.elementAt(0)).getI().getLower();
		while (entr.size() > 0 || active.size() > 0) {
			if (active.size() == 0) {
				if (entr.size() <= 0)
					break;
				active.addElement(entr.elementAt(0));
				entr.removeElementAt(0);
				nexty = ((RTreeEntry) active.elementAt(0)).getI().getLower();
			}
			double currenty = nexty;
			nexty = ((RTreeEntry) active.elementAt(0)).getI().getUpper();
			for (int i = 1; i < active.size(); i++) {
				if (((RTreeEntry) active.elementAt(i)).getI().getUpper() < nexty)
					nexty = ((RTreeEntry) active.elementAt(i)).getI()
							.getUpper();
			}
			for (int i = 0; i < entr.size(); i++) {
				if (((RTreeEntry) entr.elementAt(i)).getI().getLower() > currenty) {
					if (((RTreeEntry) entr.elementAt(i)).getI().getLower() < nexty)
						nexty = ((RTreeEntry) entr.elementAt(i)).getI()
								.getLower();
				} else {
					if (((RTreeEntry) entr.elementAt(i)).getI().getUpper() < nexty)
						nexty = ((RTreeEntry) entr.elementAt(i)).getI()
								.getUpper();
					active.addElement(entr.elementAt(i));
					entr.removeElementAt(i);
					i--;
				}
			}
			if (debug)
				System.out.println("Step - currenty=" + currenty + ", nexty="
						+ nexty + ", Entries size: " + entr.size()
						+ ", Active size: " + active.size() + ", Sum: " + sum);
			xsort(active, 0, active.size() - 1);
			double xstart = ((RTreeEntry) active.elementAt(0)).getI().getLeft();
			double xend = ((RTreeEntry) active.elementAt(0)).getI().getRight();
			for (int i = 1; i < active.size(); i++) {
				if (debug)
					System.out.println("active xl="
							+ ((RTreeEntry) active.elementAt(i)).getI()
									.getLeft()
							+ ", active xh: "
							+ ((RTreeEntry) active.elementAt(i)).getI()
									.getRight());
				if (xend > ((RTreeEntry) active.elementAt(i)).getI().getLeft()) {
					if (((RTreeEntry) active.elementAt(i)).getI().getRight() > xend)
						xend = ((RTreeEntry) active.elementAt(i)).getI()
								.getRight();
				} else {
					sum += (nexty - currenty) * (xend - xstart);
					xstart = ((RTreeEntry) active.elementAt(i)).getI()
							.getLeft();
					xend = ((RTreeEntry) active.elementAt(i)).getI().getRight();
				}
			}
			sum += (nexty - currenty) * (xend - xstart);
			for (int i = 0; i < active.size(); i++) {
				if (((RTreeEntry) active.elementAt(i)).getI().getUpper() == nexty) {
					active.removeElementAt(i);
					i--;
				}
			}
			if (debug)
				System.out.println("End of while - nexty=" + nexty
						+ ", Entries size: " + entr.size() + ", Active size: "
						+ active.size() + ", Sum: " + sum);
		}
		return sum;
	}

	private synchronized Vector getAllEntries(RTreeNode rtreenode, int level) {
		Vector allentries = new Vector();
		if (debug)
			System.out.println("RTree, getAllEntries(" + level + "): "
					+ rtreenode);
		if (rtreenode == null) {
			System.err.println("RTree, getAllEntries: rtreenode is NULL");
			return null;
		}
		if (level == 0) {
			for (int i = 0; i < rtreenode.entries.size(); i++)
				allentries.addElement(rtreenode.entries.elementAt(i));
			return allentries;
		}
		if (!rtreenode.isLeaf() && rtreenode.entries != null) {
			for (int i = 0; i < rtreenode.entries.size(); i++) {
				Vector tmpentries = getAllEntries(((RTreeInternalEntry) rtreenode.entries.elementAt(i)).getPointer(), level - 1);
				if (tmpentries != null) {
					for (int j = 0; j < tmpentries.size(); j++)
						allentries.addElement(tmpentries.elementAt(j));
				}
			}
			return allentries;
		}
		return null;
	}

	public synchronized void clear() {
		RTreeNode oldroot = root;
		root = new RTreeLeafNode(this);
		oldroot = null;
	}

	public synchronized String toString() {
		return "RTree " + name + " (" + root + ")";
	}
}
