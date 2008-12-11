package edu.gmu.cs583.rtree;

/**
 * Adapted from http://gis.umb.no/gis/applets/rtree2/jdk1.1/
 */

public abstract class RTreeEntry {
	private Object pointer;
	private BoundingBox I;

	public RTreeEntry(Object o, BoundingBox bb) {
		pointer = o;
		I = bb;
	}

	public synchronized BoundingBox getI() {
		return I.union(null);
	}

	public synchronized void setI(BoundingBox bb) {
		I = bb.union(null);
	}

	public synchronized void clear() {
		pointer = null;
		I = null;
	}

	public String toString() {
		return "RTreeEntry(" + I.toString() + "-" + pointer.toString() + ")";
	}
}
