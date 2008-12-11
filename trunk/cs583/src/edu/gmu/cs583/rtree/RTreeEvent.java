package edu.gmu.cs583.rtree;

/**
 * Adapted from http://gis.umb.no/gis/applets/rtree2/jdk1.1/
 */

import java.io.Serializable;

public class RTreeEvent implements Serializable
{
    public static final int DELETE = 0;
    public static final int INSERT = 1;
    private int eventtype;
    private RTreeLeafEntry entry;
    
    public RTreeEvent(int eventtype, RTreeLeafEntry entry) {
	if (eventtype != 0 && eventtype != 1)
	    throw new Error("Invalid eventtype in RTreeEvent constructor: "
			    + eventtype);
	if (entry == null)
	    throw new Error("null RTreeLeafEntry in RTreeEvent constructor.");
	this.eventtype = eventtype;
	this.entry = entry;
    }
    
    public synchronized int getEventType() {
	return eventtype;
    }
    
    public synchronized RTreeLeafEntry getEntry() {
	return entry;
    }
    
    public synchronized String toString() {
	if (eventtype == 0)
	    return "RTreeEvent(DELETE: " + entry + ")";
	return "RTreeEvent(INSERT: " + entry + ")";
    }
}
