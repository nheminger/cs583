package edu.gmu.cs583.rtree;

import java.awt.Button;
import java.awt.Color;
import java.util.Vector;

public class RTreeContext
{
    public RTree rtree;
    public MapCanvas mapcanvas;
    public int delay;
    public RTreeCanvas rtreecanvas;
    public RTreeAnimate anim;
    public SplitAnimate splitanimate;
    public int currentsplitanim;
    private int currentevent;
    public RTreeNode focusnode;
    public Button nextbutton;
    public Vector current;
    private RTreeEvent next;
    public Color selectedcolor = Color.red;
    public Color normalcolor = Color.green;
    public Color focuscolor;
    
    public RTreeContext() {
	focuscolor = new Color(204, 255, 255);
	rtree = null;
	mapcanvas = null;
	rtreecanvas = null;
	current = new Vector();
	next = null;
	focusnode = null;
    }
    
    public RTreeContext(RTree rtree, MapCanvas mapcanvas,
			RTreeCanvas rtreecanvas) {
	focuscolor = new Color(204, 255, 255);
	this.rtree = rtree;
	this.mapcanvas = mapcanvas;
	this.rtreecanvas = rtreecanvas;
	current = new Vector();
	next = null;
    }
    
    public synchronized RTreeEvent getnext() {
	return next;
    }
    
    public synchronized void setnext(RTreeEvent nextevent) {
	next = nextevent;
    }
    
    public synchronized int getcurrentevent() {
	return currentevent;
    }
    
    public synchronized void setcurrentevent(int number) {
	currentevent = number;
    }
    
    public synchronized void currenteventincrement(int increment) {
	currentevent = currentevent + increment;
    }
    
    public synchronized String toString() {
	return ("RTreeContext{(" + rtree + ") (" + mapcanvas + ") ("
		+ rtreecanvas + ")}");
    }
}
