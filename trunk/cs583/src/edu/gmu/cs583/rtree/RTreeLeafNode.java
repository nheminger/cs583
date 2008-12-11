package edu.gmu.cs583.rtree;

/**
 * Adapted from http://gis.umb.no/gis/applets/rtree2/jdk1.1/
 */

public class RTreeLeafNode extends RTreeNode
{
    protected boolean debug = false;
    
    public RTreeLeafNode(RTree tree) {
	super(tree);
    }
    
    public synchronized RTreeInternalEntry add(RTreeLeafEntry le) {
	if (debug)
	    System.out
		.println("RTreeLeafNode, add: adding a leaf entry: " + le);
	if (entries.size() < tree.getM()) {
	    entries.addElement(le);
	    if (debug)
		System.out.println("RTreeLeafNode, add: entry added");
	    this.adjustTree();
	    return null;
	}
	return this.splitNode(le);
    }
    
    public synchronized void delete(RTreeLeafEntry le) {
	if (entries.indexOf(le) == -1)
	    System.out.println("RTreeLeafNode, delete: entry not found");
	else
	    entries.removeElement(le);
    }
    
    public boolean isLeaf() {
	return true;
    }
    
    public synchronized String toString() {
	String string = new String("");
	if (entries != null) {
	    for (int i = 0; i < entries.size(); i++)
		string = string.concat(" GeoOBJ-" + ((RTreeLeafEntry)
						     entries.elementAt(i))
							.getI());
	}
	if (parent != null) {
	    boolean consistent = false;
	    for (int i = 0; i < parent.entries.size(); i++) {
		if (((RTreeInternalEntry) parent.entries.elementAt(i))
			.getPointer()
		    == this) {
		    consistent = true;
		    break;
		}
	    }
	    if (consistent)
		return ("RTreeLeafNode(BB: " + this.getbb()
			+ ", Parent not null, Entries: " + string
			+ ", [Parent: " + parent + "]");
	    return ("\n      RTreeLeafNode(BB: " + this.calculateBB()
		    + " (NOT CONSISTENT - not found amoung parents entries), Parent not null, Entries: "
		    + string + ", [Parent: " + parent + "]");
	}
	return ("RTreeLeafNode(BB: " + this.calculateBB()
		+ ", Parent: null, Entries: " + string);
    }
}
