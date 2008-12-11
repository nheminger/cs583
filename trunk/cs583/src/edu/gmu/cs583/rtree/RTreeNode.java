package edu.gmu.cs583.rtree;

/**
 * Adapted from http://gis.umb.no/gis/applets/rtree2/jdk1.1/
 */

import java.util.Vector;

public abstract class RTreeNode {
	protected boolean debug = false;
	protected RTree tree;
	public Vector entries;
	protected RTreeInternalNode parent;
	private double bestfit;
	private int cuts;
	private int tests;
	private RTreeEntryGroup bestgroup1 = new RTreeEntryGroup();
	private RTreeEntryGroup bestgroup2 = new RTreeEntryGroup();

	public RTreeNode(RTree tree) {
		parent = null;
		this.tree = tree;
		entries = new Vector(tree.getM());
		if (debug)
			System.out.println("New Node - isleaf: " + isLeaf() + ", parent: "
					+ parent + ", entries: " + entries + ", getLevel: "
					+ getLevel());
	}

	public synchronized RTreeInternalNode getParent() {
		return parent;
	}

	public synchronized void setParent(RTreeInternalNode par) {
		parent = par;
	}

	public RTree getTree() {
		return tree;
	}

	public synchronized Vector search(BoundingBox bb) {
		Vector matches = new Vector();
		if (isLeaf()) {
			if (entries != null) {
				for (int i = 0; i < entries.size(); i++) {
					if (((RTreeLeafEntry) entries.elementAt(i)).getI()
							.overlaps(bb)
							|| ((RTreeLeafEntry) entries.elementAt(i)).getI()
									.touches(bb))
						matches.addElement(entries.elementAt(i));
				}
			}
		} else if (entries != null) {
			for (int i = 0; i < entries.size(); i++) {
				if (((RTreeInternalEntry) entries.elementAt(i)).getI()
						.overlaps(bb)
						|| ((RTreeInternalEntry) entries.elementAt(i)).getI()
								.touches(bb)) {
					Vector tempv = ((RTreeInternalEntry) entries.elementAt(i))
							.getPointer().search(bb);
					for (int j = 0; j < tempv.size(); j++)
						matches.addElement(tempv.elementAt(j));
				}
			}
		}
		return matches;
	}

	public synchronized RTreeLeafNode findLeaf(RTreeLeafEntry le) {
		if (isLeaf()) {
			if (entries != null && entries.size() != 0
					&& entries.indexOf(le) != -1)
				return (RTreeLeafNode) this;
			return null;
		}
		for (int i = 0; i < entries.size(); i++) {
			if (((RTreeInternalEntry) entries.elementAt(i)).getI().overlaps(
					le.getI())
					|| ((RTreeInternalEntry) entries.elementAt(i)).getI()
							.touches(le.getI())) {
				RTreeLeafNode tmpln = ((RTreeInternalEntry) entries
						.elementAt(i)).getPointer().findLeaf(le);
				if (tmpln != null)
					return tmpln;
			}
		}
		return null;
	}

	public synchronized RTreeLeafNode chooseLeaf(RTreeLeafEntry le) {
		
		if (isLeaf())
			return (RTreeLeafNode) this;
		
		double newdelta = 0.0;
		double delta = (((RTreeInternalEntry) entries.elementAt(0)).getI().union(le.getI()).area() - ((RTreeInternalEntry) entries.elementAt(0)).getI().area());
		RTreeInternalEntry preferredie = (RTreeInternalEntry) entries.elementAt(0);
		BoundingBox bb1 = preferredie.getI();
		for (int i = 1; i < entries.size(); i++) {
			newdelta = (((RTreeInternalEntry) entries.elementAt(i)).getI()
					.union(le.getI()).area() - ((RTreeInternalEntry) entries
					.elementAt(i)).getI().area());
			if (newdelta < delta) {
				delta = newdelta;
				preferredie = (RTreeInternalEntry) entries.elementAt(i);
				bb1 = ((RTreeInternalEntry) entries.elementAt(i)).getI();
			} else if (newdelta == delta
					&& ((RTreeInternalEntry) entries.elementAt(i)).getI()
							.area() < bb1.area()) {
				preferredie = (RTreeInternalEntry) entries.elementAt(i);
				bb1 = ((RTreeInternalEntry) entries.elementAt(i)).getI();
			}
		}
		return preferredie.getPointer().chooseLeaf(le);
	}

	protected synchronized RTreeInternalEntry splitNode(RTreeEntry newentry) {
		Vector seeds = null;
		Vector newgroups = null;
		RTreeEntryGroup orig = null;
		RTreeEntryGroup group1 = null;
		RTreeEntryGroup group2 = null;
		if (debug)
			System.out.println("RTreeNode, splitNode: Splitting node: " + this);
		if (isLeaf()) {
			if (newentry.getClass().getName() != "edu.gmu.cs583.rtree.RTreeLeafEntry")
				System.out.println("ERROR - RTreeNode, splitNode, level mismatch. overflow entrys class: "
								+ newentry.getClass().getName());
		} else if (getLevel() != (((RTreeInternalEntry) newentry).getPointer()
				.getLevel() + 1))
			System.out
					.println("ERROR - RTreeNode, splitNode, level mismatch. this.level: "
							+ getLevel()
							+ "overflowentry level: "
							+ ((RTreeInternalEntry) newentry).getPointer()
									.getLevel());
//		if (tree != null && tree.context != null
//				&& tree.context.focusnode == this) {
//			tree.context.splitanimate = new SplitAnimate(tree.context, this,
//					newentry, -1);
//			tree.context.nextbutton.setEnabled(true);
//			tree.context.splitanimate.setDelay(tree.context.delay);
//			tree.context.splitanimate.start();
//			try {
//				tree.context.splitanimate.join();
//			} catch (InterruptedException interruptedexception) {
//				/* empty */
//			}
//			tree.context.nextbutton.setEnabled(false);
//		}
		orig = new RTreeEntryGroup();
		while (entries.size() > 0) {
			orig.addEntry((RTreeEntry) entries.elementAt(0));
			entries.removeElementAt(0);
		}
		orig.addEntry(newentry);
		if (tree.getSplitMethod() == 1)
			newgroups = linearSplit(orig);
		else if (tree.getSplitMethod() == 0)
			newgroups = quadraticSplit(orig);
		else if (tree.getSplitMethod() == 2)
			newgroups = exhaustiveSplit(orig);
		else
			newgroups = quadraticSplit(orig);
		if (newgroups != null) {
			group1 = (RTreeEntryGroup) newgroups.elementAt(0);
			group2 = (RTreeEntryGroup) newgroups.elementAt(1);
			while (newgroups.size() > 0)
				newgroups.removeElementAt(0);
		} else
			System.out.println("ERROR - split returned an empty result!");
		if (debug)
			System.out
					.println("RTreeNode, splitNode: adding group1 entries to this node");
		while (group1.size() > 0) {
			if (!isLeaf())
				((RTreeInternalEntry) group1.entryAt(0)).getPointer().parent = (RTreeInternalNode) this;
			entries.addElement(group1.entryAt(0));
			group1.removeEntryAt(0);
		}
		RTreeNode overflownode;
		if (isLeaf())
			overflownode = new RTreeLeafNode(tree);
		else
			overflownode = new RTreeInternalNode(tree);
		if (debug)
			System.out
					.println("RTreeNode, splitNode: adding group2 entries to the node in the overflow entry");
		while (group2.size() > 0) {
			if (!isLeaf())
				((RTreeInternalEntry) group2.entryAt(0)).getPointer().parent = (RTreeInternalNode) overflownode;
			overflownode.entries.addElement(group2.entryAt(0));
			group2.removeEntryAt(0);
		}
		RTreeInternalEntry overflowentry = new RTreeInternalEntry(overflownode);
		if (isRoot())
			return overflowentry;
		if (parent.entries.size() < tree.getM()) {
			parent.entries.addElement(overflowentry);
			overflownode.parent = parent;
			adjustTree();
			return null;
		}
		updatebb();
		return parent.splitNode(overflowentry);
	}

	private synchronized Vector exhaustiveSplit(RTreeEntryGroup newgroup) {
		bestfit = 1.7976931348623157E308;
		cuts = 0;
		tests = 0;
		while (bestgroup1.size() > 0)
			bestgroup1.removeEntry(bestgroup1.entryAt(0));
		while (bestgroup2.size() > 0)
			bestgroup2.removeEntry(bestgroup2.entryAt(0));
		RTreeEntryGroup firstelementgroup = new RTreeEntryGroup();
		firstelementgroup.addEntry(newgroup.entryAt(0));
		newgroup.removeEntryAt(0);
		exhaustiveBest(firstelementgroup, newgroup);
		Vector result = new Vector(2);
		result.addElement(bestgroup1);
		result.addElement(bestgroup2);
		return result;
	}

	private synchronized void exhaustiveBest(RTreeEntryGroup firstgroup,
			RTreeEntryGroup rest) {
		if (debug)
			System.out.println("ExhaustiveBest, entering, bestfit: " + bestfit
					+ "\n fgsize: " + firstgroup.size() + " firstgroup: "
					+ firstgroup + "\n rgsize: " + rest.size() + " Rest: "
					+ rest);
		tests++;
		RTreeEntryGroup fgroup = new RTreeEntryGroup();
		for (int i = 0; i < firstgroup.size(); i++)
			fgroup.addEntry(firstgroup.entryAt(i));
		RTreeEntryGroup newrest = new RTreeEntryGroup();
		for (int i = 0; i < rest.size(); i++)
			newrest.addEntry(rest.entryAt(i));
		if (fgroup.size() >= tree.getm() && newrest.size() >= tree.getm()
				&& fgroup.getArea() + newrest.getArea() < bestfit) {
			bestfit = fgroup.getArea() + newrest.getArea();
			while (bestgroup1.size() > 0)
				bestgroup1.removeEntry(bestgroup1.entryAt(0));
			if (bestgroup1.size() > 0)
				System.err.println("Failed to clear bestgroup1, size: "
						+ bestgroup1.size());
			for (int j = 0; j < fgroup.size(); j++)
				bestgroup1.addEntry(fgroup.entryAt(j));
			while (bestgroup2.size() > 0)
				bestgroup2.removeEntry(bestgroup2.entryAt(0));
			if (bestgroup2.size() > 0)
				System.err.println("Failed to clear bestgroup2, size: "
						+ bestgroup2.size());
			for (int j = 0; j < newrest.size(); j++)
				bestgroup2.addEntry(newrest.entryAt(j));
			if (debug)
				System.out.println("new best fit, bestfit: " + bestfit
						+ "\n* best1: " + bestgroup1 + "\n* best2: "
						+ bestgroup2);
		}
		if (newrest == null || newrest.size() == 0)
			System.out
					.println("RTreeNode.java, exhaustiveBest: inconsistent R-Tree");
		else if (newrest.size() <= tree.getm()) {
			if (debug)
				System.out
						.println("exhaustiveBest, second group needs the rest:  rest: "
								+ newrest.size() + ", m: " + tree.getm());
		} else {
			for (int i = 0; i < newrest.size(); i++) {
				RTreeEntryGroup rec1group = new RTreeEntryGroup();
				for (int j = 0; j < fgroup.size(); j++)
					rec1group.addEntry(fgroup.entryAt(j));
				RTreeEntry rtmp = newrest.entryAt(i);
				rec1group.addEntry(rtmp);
				RTreeEntryGroup rec2group = new RTreeEntryGroup();
				for (int j = 0; j < newrest.size(); j++)
					rec2group.addEntry(newrest.entryAt(j));
				rec2group.removeEntry(rtmp);
				if (rec1group.getArea() >= bestfit) {
					if (debug)
						System.out.println("exhaustiveBest, cut branch");
					cuts++;
				} else
					exhaustiveBest(rec1group, rec2group);
			}
		}
	}

	private synchronized Vector quadraticSplit(RTreeEntryGroup overflowgroup) {
		Vector seeds = null;
		RTreeEntryGroup group1 = new RTreeEntryGroup();
		RTreeEntryGroup group2 = new RTreeEntryGroup();
		seeds = pickSeeds(overflowgroup);
		if (debug)
			System.out.println("RTreeNode, quadraticSplit: Seed1:"
					+ (RTreeEntry) seeds.elementAt(0) + " - Seed2:  "
					+ (RTreeEntry) seeds.elementAt(1));
		group1.addEntry((RTreeEntry) seeds.elementAt(0));
		if (overflowgroup.indexOf((RTreeEntry) seeds.elementAt(0)) != -1)
			overflowgroup.removeEntry((RTreeEntry) seeds.elementAt(0));
		else
			System.err
					.println("RTreeNode, quadraticSplit: ERROR...seed[0] not in overflowgroup.");
		group2.addEntry((RTreeEntry) seeds.elementAt(1));
		if (overflowgroup.indexOf((RTreeEntry) seeds.elementAt(1)) != -1)
			overflowgroup.removeEntry((RTreeEntry) seeds.elementAt(1));
		else
			System.err
					.println("RTreeNode, quadraticSplit: ERROR...seed[1] not in overflowgroup.");
		if (overflowgroup.size() > 0) {
			do {
				RTreeEntry entry = pickNext(overflowgroup, group1, group2);
				if ((group1.getBB().union(entry.getI()).area() - group1.getBB()
						.area()) < (group2.getBB().union(entry.getI()).area() - group2
						.getBB().area()))
					group1.addEntry(entry);
				else if ((group1.getBB().union(entry.getI()).area() - group1
						.getBB().area()) == (group2.getBB().union(entry.getI())
						.area() - group2.getBB().area())) {
					if (group1.getBB().area() < group2.getBB().area())
						group1.addEntry(entry);
					else if (group1.getBB().area() == group2.getBB().area()) {
						if (group1.size() <= group2.size())
							group1.addEntry(entry);
						else
							group2.addEntry(entry);
					} else
						group2.addEntry(entry);
				} else
					group2.addEntry(entry);
				if (overflowgroup.indexOf(entry) != -1)
					overflowgroup.removeEntry(entry);
				else
					System.err
							.println("RTreeNode, quadraticSplit: ERROR...entry not in overflowgroup.");
			} while (overflowgroup.size() > 0
					&& tree.getm() - group1.size() < overflowgroup.size()
					&& tree.getm() - group2.size() < overflowgroup.size());
			if (overflowgroup.size() > 0) {
				if (debug)
					System.out
							.println("RTreeNode, quadraticSplit: the rest to one group");
				if (group2.size() > group1.size()) {
					while (overflowgroup.size() > 0) {
						group1.addEntry(overflowgroup.entryAt(0));
						overflowgroup.removeEntryAt(0);
					}
				} else {
					while (overflowgroup.size() > 0) {
						group2.addEntry(overflowgroup.entryAt(0));
						overflowgroup.removeEntryAt(0);
					}
				}
			}
		}
		if (debug)
			System.out
					.println("RTreeNode, quadraticSplit: Group1: " + group1
							+ " Group2: " + group2 + " Overflowgroup: "
							+ overflowgroup);
		Vector result = new Vector(2);
		result.addElement(group1);
		result.addElement(group2);
		return result;
	}

	private synchronized Vector linearSplit(RTreeEntryGroup overflowgroup) {
		RTreeEntryGroup group1 = new RTreeEntryGroup();
		RTreeEntryGroup group2 = new RTreeEntryGroup();
		if (overflowgroup == null) {
			System.err
					.println("RTreeNode, linearSplit: ERROR...Trying to split an empty entry group");
			return null;
		}
		if (debug)
			System.out
					.println("RTreeNode, linearSplit: Splitting entry group: "
							+ overflowgroup);
		Vector seeds = linearPickSeeds(overflowgroup);
		if (debug)
			System.out.println("RTreeNode, linearSplit: Seed1: "
					+ (RTreeEntry) seeds.elementAt(0) + " - Seed2:  "
					+ (RTreeEntry) seeds.elementAt(1));
		group1.addEntry((RTreeEntry) seeds.elementAt(0));
		if (overflowgroup.indexOf((RTreeEntry) seeds.elementAt(0)) != -1)
			overflowgroup.removeEntry((RTreeEntry) seeds.elementAt(0));
		else
			System.err
					.println("RTreeNode, linearSplit: ERROR...seed[0] not in overflowgroup.");
		group2.addEntry((RTreeEntry) seeds.elementAt(1));
		if (overflowgroup.indexOf((RTreeEntry) seeds.elementAt(1)) != -1)
			overflowgroup.removeEntry((RTreeEntry) seeds.elementAt(1));
		else
			System.err
					.println("RTreeNode, linearSplit: ERROR...seed[1] not in overflowgroup.");
		if (overflowgroup.size() > 0) {
			do {
				RTreeEntry entry = overflowgroup.entryAt(0);
				if ((group1.getBB().union(entry.getI()).area() - group1.getBB()
						.area()) < (group2.getBB().union(entry.getI()).area() - group2
						.getBB().area()))
					group1.addEntry(entry);
				else if ((group1.getBB().union(entry.getI()).area() - group1
						.getBB().area()) == (group2.getBB().union(entry.getI())
						.area() - group2.getBB().area())) {
					if (group1.getBB().area() < group2.getBB().area())
						group1.addEntry(entry);
					else if (group1.getBB().area() == group2.getBB().area()) {
						if (group1.size() <= group2.size())
							group1.addEntry(entry);
						else
							group2.addEntry(entry);
					} else
						group2.addEntry(entry);
				} else
					group2.addEntry(entry);
				if (overflowgroup.indexOf(entry) != -1)
					overflowgroup.removeEntry(entry);
				else
					System.err
							.println("RTreeNode, linearSplit: ERROR...entry not in overflowgroup.");
			} while (overflowgroup.size() > 0
					&& tree.getm() - group1.size() < overflowgroup.size()
					&& tree.getm() - group2.size() < overflowgroup.size());
			if (overflowgroup.size() > 0) {
				if (debug)
					System.out
							.println("RTreeNode, linearSplit: the rest to one group");
				if (group2.size() > group1.size()) {
					while (overflowgroup.size() > 0) {
						group1.addEntry(overflowgroup.entryAt(0));
						overflowgroup.removeEntryAt(0);
					}
				} else {
					while (overflowgroup.size() > 0) {
						group2.addEntry(overflowgroup.entryAt(0));
						overflowgroup.removeEntryAt(0);
					}
				}
			}
		}
		if (debug)
			System.out
					.println("RTreeNode, linearSplit: Group1: " + group1
							+ " Group2: " + group2 + " Overflowgroup: "
							+ overflowgroup);
		Vector result = new Vector(2);
		result.addElement(group1);
		result.addElement(group2);
		return result;
	}

	public static Vector pickSeeds(RTreeEntryGroup group) {
		if (group == null || group.size() <= 1) {
			System.err
					.println("RTreeNode, pickSeeds: Trying to pick from an empty or too small vector");
			return null;
		}
		RTreeEntry entry1 = group.entryAt(0);
		RTreeEntry entry2 = group.entryAt(1);
		double ineff = (entry1.getI().union(entry2.getI()).area()
				- entry1.getI().area() - entry2.getI().area());
		for (int i = 0; i < group.size() - 1; i++) {
			for (int j = i + 1; j < group.size(); j++) {
				double tmpineff = (group.entryAt(i).getI().union(
						group.entryAt(j).getI()).area()
						- group.entryAt(i).getI().area() - group.entryAt(j)
						.getI().area());
				if (tmpineff > ineff) {
					ineff = tmpineff;
					entry1 = group.entryAt(i);
					entry2 = group.entryAt(j);
				}
			}
		}
		Vector seeds = new Vector();
		seeds.addElement(entry1);
		seeds.addElement(entry2);
		return seeds;
	}

	public static Vector linearPickSeeds(RTreeEntryGroup group) {
		Vector seeds = new Vector();
		if (group == null || group.size() <= 1) {
			System.err
					.println("RTreeNode, linearPickSeeds: Trying to pick from an empty or too small vector");
			return null;
		}
		RTreeEntry lowesthighx;
		RTreeEntry highestlowy;
		RTreeEntry lowesthighy;
		RTreeEntry highestlowx = lowesthighx = highestlowy = lowesthighy = group
				.entryAt(0);
		double minx = group.entryAt(0).getI().getLeft();
		double maxx = group.entryAt(0).getI().getRight();
		double miny = group.entryAt(0).getI().getLower();
		double maxy = group.entryAt(0).getI().getUpper();
		for (int i = 1; i < group.size(); i++) {
			if (highestlowx.getI().getLeft() < group.entryAt(i).getI()
					.getLeft())
				highestlowx = group.entryAt(i);
			if (lowesthighx.getI().getRight() > group.entryAt(i).getI()
					.getRight())
				lowesthighx = group.entryAt(i);
			if (highestlowy.getI().getLower() < group.entryAt(i).getI()
					.getLower())
				highestlowy = group.entryAt(i);
			if (lowesthighy.getI().getUpper() > group.entryAt(i).getI()
					.getUpper())
				lowesthighy = group.entryAt(i);
			if (minx > group.entryAt(i).getI().getLeft())
				minx = group.entryAt(i).getI().getLeft();
			if (maxx < group.entryAt(i).getI().getRight())
				maxx = group.entryAt(i).getI().getRight();
			if (miny > group.entryAt(i).getI().getLower())
				miny = group.entryAt(i).getI().getLower();
			if (maxy < group.entryAt(i).getI().getUpper())
				maxy = group.entryAt(i).getI().getUpper();
		}
		double separationx = highestlowx.getI().getLeft()
				- lowesthighx.getI().getRight();
		double separationy = highestlowy.getI().getLower()
				- lowesthighy.getI().getUpper();
		double normalizedseparationx = separationx / (maxx - minx);
		double normalizedseparationy = separationy / (maxy - miny);
		if (normalizedseparationx > normalizedseparationy) {
			seeds.addElement(lowesthighx);
			seeds.addElement(highestlowx);
		} else {
			seeds.addElement(lowesthighy);
			seeds.addElement(highestlowy);
		}
		return seeds;
	}

	public static RTreeEntry pickNext(RTreeEntryGroup remain,
			RTreeEntryGroup group1, RTreeEntryGroup group2) {
		double preference = 0.0;
		RTreeEntry next = remain.entryAt(0);
		for (int i = 0; i < remain.size(); i++) {
			double d1 = (group1.getBB().union(remain.entryAt(i).getI()).area() - group1
					.getBB().area());
			double d2 = (group2.getBB().union(remain.entryAt(i).getI()).area() - group2
					.getBB().area());
			if (Math.abs(d1 - d2) > preference) {
				preference = Math.abs(d1 - d2);
				next = remain.entryAt(i);
			}
		}
		return next;
	}

	protected synchronized void adjustTree() {
		RTreeInternalEntry parentoverflowentry = null;
		if (debug)
			System.out.println("RTreeNode, adjustTree (start). This: " + this);
		if (isRoot()) {
			if (debug)
				System.out
						.println("RTreeNode, adjustTree (end). This: " + this);
		} else {
			BoundingBox oldbb = getbb();
			updatebb();
			if (debug)
				System.out
						.println("RTreeNode, adjustTree (end). This: " + this);
			if (oldbb.getLeft() != getbb().getLeft()
					|| oldbb.getRight() != getbb().getRight()
					|| oldbb.getLower() != getbb().getLower()
					|| oldbb.getUpper() != getbb().getUpper())
				parent.adjustTree();
		}
	}

	public synchronized Vector condense(Vector Q) {
		RTreeNode par = parent;
		if (isRoot())
			return Q;
		if (entries.size() == 0) {
			for (int i = 0; i < parent.entries.size(); i++) {
				if (((RTreeInternalEntry) parent.entries.elementAt(i))
						.getPointer() == this) {
					parent.entries
							.removeElement((RTreeInternalEntry) parent.entries
									.elementAt(i));
					parent = null;
					break;
				}
			}
			return par.condense(Q);
		}
		if (entries.size() < tree.getm()) {
			updatebb();
			for (int i = 0; i < parent.entries.size(); i++) {
				if (((RTreeInternalEntry) parent.entries.elementAt(i))
						.getPointer() == this) {
					Q.addElement(this);
					parent.entries
							.removeElement((RTreeInternalEntry) parent.entries
									.elementAt(i));
					parent = null;
					break;
				}
			}
			return par.condense(Q);
		}
		adjustTree();
		return Q;
	}

	public synchronized RTreeInternalEntry reInsert(RTreeEntry rte) {
		if (rte instanceof RTreeLeafEntry) {
			RTreeLeafNode leafnode = chooseLeaf((RTreeLeafEntry) rte);
			return leafnode.add((RTreeLeafEntry) rte);
		}
		if (((RTreeInternalEntry) rte).getPointer().getLevel() == getLevel() - 1)
			return ((RTreeInternalNode) this).add((RTreeInternalEntry) rte);
		RTreeEntry bestchild = (RTreeEntry) entries.elementAt(0);
		double delta = (((RTreeEntry) entries.elementAt(0)).getI().union(
				rte.getI()).area() - ((RTreeEntry) entries.elementAt(0)).getI()
				.area());
		for (int i = 1; i < entries.size(); i++) {
			double newdelta = (((RTreeInternalEntry) entries.elementAt(i))
					.getI().union(rte.getI()).area() - ((RTreeInternalEntry) entries
					.elementAt(i)).getI().area());
			if (newdelta < delta) {
				delta = newdelta;
				bestchild = (RTreeInternalEntry) entries.elementAt(i);
			} else if (newdelta == delta
					&& ((RTreeInternalEntry) entries.elementAt(i)).getI()
							.area() < bestchild.getI().area())
				bestchild = (RTreeInternalEntry) entries.elementAt(i);
		}
		return ((RTreeInternalEntry) bestchild).getPointer().reInsert(rte);
	}

	public synchronized void updatebb() {
		if (parent != null) {
			for (int i = 0; i < parent.entries.size(); i++) {
				if (((RTreeInternalEntry) parent.entries.elementAt(i))
						.getPointer() == this) {
					BoundingBox newbb = ((RTreeEntry) entries.elementAt(0))
							.getI();
					if (entries.size() > 1) {
						for (int j = 1; j < entries.size(); j++)
							newbb.unionin(((RTreeEntry) entries.elementAt(j))
									.getI());
					}
					BoundingBox oldbb = getbb();
					if (oldbb.getLeft() != newbb.getLeft()
							|| oldbb.getRight() != newbb.getRight()
							|| oldbb.getLower() != newbb.getLower()
							|| oldbb.getUpper() != newbb.getUpper())
						((RTreeEntry) parent.entries.elementAt(i)).setI(newbb);
					return;
				}
			}
			System.err
					.println("RTreeNode, updatebb: ERROR - Node not found among parents entries: "
							+ this + "Parent: " + parent);
		}
		if (debug)
			System.out.println("RTreeNode, updatebb: ERROR - root node?: "
					+ getbb());
	}

	public BoundingBox getbb() {
		if (entries == null)
			return null;
		if (entries.size() == 0)
			return null;
		if (isRoot())
			return calculateBB();
		if (parent.entries != null && parent.entries.size() > 0) {
			for (int i = 0; i < parent.entries.size(); i++) {
				if (((RTreeInternalEntry) parent.entries.elementAt(i))
						.getPointer() == this)
					return ((RTreeInternalEntry) parent.entries.elementAt(i))
							.getI();
			}
			System.err
					.println("RTreeNode, getbb: ERROR!!!! The tree is not in a consistent state (not among parent entries). #entries in parent: "
							+ parent.entries.size());
			System.err.println(" this BB: " + calculateBB());
			for (int i = 0; i < parent.entries.size(); i++) {
				System.err.println(" Parent info, entry "
						+ i
						+ ": "
						+ ((RTreeInternalEntry) parent.entries.elementAt(i))
								.getI());
				if (((RTreeInternalEntry) parent.entries.elementAt(i))
						.getPointer().isRoot())
					System.err.println(" Parent link OK");
			}
			return calculateBB();
		}
		if (parent.entries == null) {
			System.err
					.println("RTreeNode, getbb: Parent.entries == null!!!! The tree is not in a consistent state");
			return null;
		}
		System.err
				.println("RTreeNode, getbb: Parent.entries.size <= 0! The tree is not in a consistent state");
		return null;
	}

	public BoundingBox calculateBB() {
		synchronized (entries) {
			if (entries == null)
				return null;
			if (entries.size() == 0)
				return null;
			BoundingBox newbb = ((RTreeEntry) entries.elementAt(0)).getI();
			for (int i = 1; i < entries.size(); i++)
				newbb.unionin(((RTreeEntry) entries.elementAt(i)).getI());
			return newbb;
		}
	}

	public synchronized int getLevel() {
		if (isLeaf())
			return 0;
		if (entries != null && entries.size() > 0)
			return 1 + ((RTreeInternalEntry) entries.elementAt(0)).getPointer()
					.getLevel();
		return -1000;
	}

	public boolean isRoot() {
		return parent == null;
	}

	public abstract boolean isLeaf();

	public synchronized String toString() {
		return "RTreeNode(isleaf=" + isLeaf() + ")";
	}
}
