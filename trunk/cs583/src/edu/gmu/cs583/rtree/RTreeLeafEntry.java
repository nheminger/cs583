package edu.gmu.cs583.rtree;


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
