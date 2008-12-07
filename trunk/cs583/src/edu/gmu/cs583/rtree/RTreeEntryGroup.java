package edu.gmu.cs583.rtree;


import java.util.Vector;

public class RTreeEntryGroup
{
    private Vector elements = new Vector();
    
    public synchronized void addEntry(RTreeEntry entry) {
	elements.addElement(entry);
    }
    
    public synchronized RTreeEntry entryAt(int pos) {
	if (elements.size() > pos)
	    return (RTreeEntry) elements.elementAt(pos);
	System.err.println
	    ("RTreeEntryGroup, elementAt: ERROR... index out of range");
	return (RTreeEntry) elements.elementAt(pos);
    }
    
    public synchronized int indexOf(RTreeEntry rte) {
	return elements.indexOf(rte);
    }
    
    public synchronized void removeEntry(RTreeEntry entry) {
	if (elements.indexOf(entry) != -1)
	    elements.removeElement(entry);
	else
	    System.err.println
		("RTreeEntryGroup, remove: ERROR... element not present");
    }
    
    public synchronized void removeEntryAt(int pos) {
	if (elements.size() > pos)
	    elements.removeElementAt(pos);
	else
	    System.err.println
		("RTreeEntryGroup, removeElementAt: ERROR... index out of range");
    }
    
    public synchronized int size() {
	return elements.size();
    }
    
    public synchronized double getArea() {
	if (getBB() == null)
	    return 0.0;
	return getBB().area();
    }
    
    public synchronized BoundingBox getBB() {
	BoundingBox newbb = null;
	if (elements.size() == 0)
	    return null;
	newbb = ((RTreeEntry) elements.elementAt(0)).getI();
	for (int i = 1; i < elements.size(); i++)
	    newbb.unionin(((RTreeEntry) elements.elementAt(i)).getI());
	return newbb;
    }
    
    public synchronized String toString() {
	String string = new String("");
	if (elements != null) {
	    for (int i = 0; i < elements.size(); i++)
		string = string.concat(" " + elements.elementAt(i));
	}
	return "RTreeEntryGroup(" + getBB() + " Entries: " + string + ")";
    }
}
