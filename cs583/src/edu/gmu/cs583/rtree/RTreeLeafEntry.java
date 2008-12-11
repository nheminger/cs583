package edu.gmu.cs583.rtree;

/**
 * Adapted from http://gis.umb.no/gis/applets/rtree2/jdk1.1/
 */

public class RTreeLeafEntry extends RTreeEntry
{
    private Object pointer;
    
    public RTreeLeafEntry(Object o, BoundingBox bb) {
    	super(o, bb);
    	pointer = o;
    }
    
    public Object getPointer() {
    	return pointer;
    }
    
    public String toString() {
    	return "RTreeLeafEntry(" + this.getI() + "-" + getPointer() + ")";
    }
}
