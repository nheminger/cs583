package edu.gmu.cs583.rtree;

import java.io.Serializable;

public class BoundingBox implements Serializable
{
    private double left;
    private double right;
    private double upper;
    private double lower;
    
    public BoundingBox() {
	left = 0.0;
	right = 0.0;
	upper = 0.0;
	lower = 0.0;
    }
    
    public BoundingBox(double le, double lo, double ri, double up)
	throws Error {
	if (le > ri || lo > up) {
	    System.err.println("Error: BoundingBox inconsistent: (" + le + ","
			       + lo + ")(" + ri + "," + up + ")");
	    throw new Error("Attempt at creating an inconsistent BoundingBox ("
			    + le + "," + lo + ")(" + ri + "," + up + ")");
	}
	left = le;
	right = ri;
	upper = up;
	lower = lo;
    }
    
    public synchronized double area() {
	return (right - left) * (upper - lower);
    }
    
    public synchronized BoundingBox intersect(BoundingBox bb) {
	if (bb == null)
	    return null;
	if (left > bb.getRight() || right < bb.getLeft()
	    || upper < bb.getLower() || lower > bb.getUpper())
	    return null;
	return new BoundingBox(Math.max(left, bb.getLeft()),
			       Math.max(lower, bb.getLower()),
			       Math.min(right, bb.getRight()),
			       Math.min(upper, bb.getUpper()));
    }
    
    public synchronized boolean touches(BoundingBox bb) {
	if (bb == null)
	    return false;
	if (left <= bb.getRight() && right >= bb.getLeft()
	    && upper >= bb.getLower() && lower <= bb.getUpper())
	    return (left == bb.getRight() || right == bb.getLeft()
		    || upper == bb.getLower() || lower == bb.getUpper());
	return false;
    }
    
    public synchronized boolean overlaps(BoundingBox bb) {
	if (bb == null)
	    return false;
	return (left < bb.getRight() && right > bb.getLeft()
		&& upper > bb.getLower() && lower < bb.getUpper());
    }
    
    public synchronized int overlap(BoundingBox bb) {
	if (bb == null)
	    return -1;
	if (left > bb.getRight() || right < bb.getLeft()
	    || upper < bb.getLower() || lower > bb.getUpper())
	    return -1;
	if (left == bb.getRight() && upper == bb.getLower()
	    || left == bb.getRight() && lower == bb.getUpper()
	    || right == bb.getLeft() && upper == bb.getLower()
	    || right == bb.getLeft() && lower == bb.getUpper())
	    return 0;
	if (isLine() || bb.isLine() || isPoint() || bb.isPoint()) {
	    if (left < bb.getRight() && right > bb.getLeft()
		&& upper > bb.getLower() && lower < bb.getUpper()) {
		if (isPoint() || bb.isPoint())
		    return 30;
		if (isLine() && bb.isLine())
		    return 10;
		return 11;
	    }
	    if (isPoint() || bb.isPoint())
		return 20;
	    if (isLine() && bb.isLine()) {
		if (left == right && bb.getLeft() == bb.getRight()
		    || upper == lower && bb.getUpper() == bb.getLower())
		    return 1;
		return 0;
	    }
	    if ((left == right || bb.getLeft() == bb.getRight())
		&& (left == bb.getLeft() || right == bb.getRight()))
		return 1;
	    return 20;
	}
	if ((left == bb.getRight() && upper > bb.getLower()
	     && lower < bb.getUpper())
	    || (right == bb.getLeft() && upper > bb.getLower()
		&& lower < bb.getUpper())
	    || (upper == bb.getLower() && left < bb.getRight()
		&& right > bb.getLeft())
	    || (lower == bb.getUpper() && left < bb.getRight()
		&& right > bb.getLeft()))
	    return 1;
	if (left < bb.getRight() && right > bb.getLeft()
	    && upper > bb.getLower() && lower < bb.getUpper())
	    return 2;
	return -99;
    }
    
    public synchronized BoundingBox union(BoundingBox bb) {
	if (bb == null)
	    return new BoundingBox(left, lower, right, upper);
	return new BoundingBox(Math.min(left, bb.getLeft()),
			       Math.min(lower, bb.getLower()),
			       Math.max(right, bb.getRight()),
			       Math.max(upper, bb.getUpper()));
    }
    
    public synchronized void unionin(BoundingBox bb) {
	if (bb != null) {
	    left = Math.min(left, bb.getLeft());
	    lower = Math.min(lower, bb.getLower());
	    right = Math.max(right, bb.getRight());
	    upper = Math.max(upper, bb.getUpper());
	}
    }
    
    public synchronized boolean isPoint() {
	return left == right && upper == lower;
    }
    
    public synchronized boolean isLine() {
	return (left == right && upper != lower
		|| left != right && upper == lower);
    }
    
    public synchronized double getLeft() {
	return left;
    }
    
    public synchronized double getRight() {
	return right;
    }
    
    public synchronized double getLower() {
	return lower;
    }
    
    public synchronized double getUpper() {
	return upper;
    }
    
    public synchronized String toString() {
	return ("BoundingBox{(" + left + "," + lower + ")(" + right + ","
		+ upper + ")}");
    }
}
