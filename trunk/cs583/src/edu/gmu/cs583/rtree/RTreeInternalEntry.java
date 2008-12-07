package edu.gmu.cs583.rtree;


public class RTreeInternalEntry extends RTreeEntry
{
    private RTreeNode pointer;
    
    public RTreeInternalEntry(RTreeNode o) {
    	super(o, o.calculateBB());
    	pointer = o;
    }
    
    public RTreeNode getPointer() {
    	return pointer;
    }
    
    public String toString() {
    	return "RTreeInternalEntry(" + this.getI() + "-" + getPointer() + ")";
    }
}
