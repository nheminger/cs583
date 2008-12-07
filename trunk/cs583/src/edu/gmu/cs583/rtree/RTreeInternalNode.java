package edu.gmu.cs583.rtree;

public class RTreeInternalNode extends RTreeNode
{
    public RTreeInternalNode(RTree tree) {
	super(tree);
    }
    
    public synchronized RTreeInternalEntry add(RTreeInternalEntry newentry) {
	if (entries == null) {
	    System.err.println
		("ERROR - RTreeInternalNode, add: entries-Vector was null");
	    return null;
	}
	if (debug)
	    System.out.println("RTreeInternalNode, add. entries:"
			       + entries.size() + ", level:" + this.getLevel()
			       + ", This: " + this + "\n---- Newentry: "
			       + newentry);
	if (entries.size() > 0
	    && newentry.getPointer().getLevel() != this.getLevel() - 1)
	    System.out.println
		("ERROR - RTreeInternalNode, add, level error: this level: "
		 + this.getLevel() + ", (child) newentry-level: "
		 + newentry.getPointer().getLevel());
	if (entries.size() < tree.getM()) {
	    if (debug)
		System.out.println("RTreeInternalNode, add: room left, this: "
				   + this);
	    entries.addElement(newentry);
	    newentry.getPointer().parent = this;
	    this.adjustTree();
	    return null;
	}
	if (debug) {
	    RTreeInternalEntry newent = this.splitNode(newentry);
	    System.out.println
		("RTreeInternalNode, add: (after splitting) this: " + this
		 + "\n--- RTreeInternalNode, add: (after splitting) newent: "
		 + newent);
	    return newent;
	}
	return this.splitNode(newentry);
    }
    
    public boolean isLeaf() {
	return false;
    }
    
    public synchronized String toString() {
	String string = new String("");
	if (entries != null) {
	    for (int i = 0; i < entries.size(); i++) {
		string = string.concat(" "
				       + ((RTreeInternalEntry)
					  entries.elementAt(i))
					     .getI
					     ().toString()
				       + " leaf: "
				       + ((RTreeInternalEntry)
					  entries.elementAt(i))
					     .getPointer
					     ().isLeaf());
		if ((((RTreeInternalEntry) entries.elementAt(i)).getPointer()
		     .parent)
		    == this)
		    string
			= string.concat("-childs parent entry is consistent.");
		else
		    string = (string.concat
			      ("\n    INCONSISTENT parent entry in child"));
	    }
	}
	if (parent != null)
	    return ("RTreeInternalNode(Level: " + this.getLevel()
		    + ", BoundingBox: " + this.calculateBB()
		    + ", Parent not null, Entries: " + string + ", [Parent: "
		    + parent + "]");
	return ("RTreeInternalNode(Level: " + this.getLevel()
		+ ", BoundingBox: " + this.calculateBB()
		+ ", Parent: null, Entries: " + string);
    }
}
