package edu.gmu.cs583.rtree;

/**
 * Adapted from http://gis.umb.no/gis/applets/rtree2/jdk1.1/
 */

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;

public class RTreeCanvas extends Canvas
{
    private RTreeContext context;
    private int rcwidth;
    private int rcheight;
    private int boxheight = 6;
    private int branchheight = 16;
    private int nodeheight = boxheight + branchheight;
    private int boxwidth = 7;
    private int nodewidth = (boxwidth - 1) * 5 + 1;
    private int spacing = 3;
    private int nodespace = nodewidth + spacing;
    private int objectwidth = boxwidth - spacing;
    private int objectheight = boxheight;
    private static final Color BRANCHCOLOR = Color.blue;
    private static final Color NODEOUTLINECOLOR = Color.red;
    private static final Color FOCUSNODECOLOR = Color.magenta;
    
    public RTreeCanvas(RTreeContext rtreecontext) {
	context = rtreecontext;
	this.setBackground(new Color(238, 238, 238));
	this.setCursor(new Cursor(1));
	this.validate();
	paint(this.getGraphics());
	rcheight = 200;
	rcwidth = 400;
    }
    
    public synchronized int getBoxHeight() {
	return boxheight;
    }
    
    public synchronized int getBoxWidth() {
	return boxwidth;
    }
    
    public synchronized int getNodeHeight() {
	return boxheight + branchheight;
    }
    
    public synchronized int getNodeWidth() {
	return (boxwidth - 1) * context.rtree.getM() + 1;
    }
    
    public synchronized int getObjectHeight() {
	return objectheight;
    }
    
    public synchronized int getObjectWidth() {
	return objectwidth;
    }
    
    public synchronized int getSpacing() {
	return spacing;
    }
    
    public synchronized void setBoxHeight(int height) {
	boxheight = height;
	nodeheight = boxheight + branchheight;
    }
    
    public synchronized void setBoxWidth(int width) {
	boxwidth = width;
	nodewidth = (boxwidth - 1) * context.rtree.getM() + 1;
	objectwidth = boxwidth - spacing;
	objectheight = boxheight;
    }
    
    public synchronized void setBranchHeight(int height) {
	branchheight = height;
	nodeheight = boxheight + branchheight;
    }
    
    public synchronized void setSpacing(int width) {
	spacing = width;
    }
    
    public synchronized void drawNode(Graphics g, RTreeNode rtnode, int px,
				      int y, int width) {
	int x = px;
	g.setColor(Color.black);
	for (int i = 0; i < rtnode.getTree().getM(); i++) {
	    int xpos = x - nodewidth / 2 + (boxwidth - 1) * i;
	    int tox = (x - width / 2 + width / (rtnode.getTree().getM() * 2)
		       + i * width / rtnode.getTree().getM());
	    if (rtnode == context.focusnode) {
		g.setColor(context.focuscolor);
		g.fillRect(xpos, y, boxwidth - 1, boxheight - 1);
	    }
	    g.setColor(NODEOUTLINECOLOR);
	    g.drawRect(xpos, y, boxwidth - 1, boxheight - 1);
	    g.setColor(BRANCHCOLOR);
	    g.drawLine(xpos + (boxwidth - 1) / 2, y + boxheight, tox,
		       y + nodeheight - 1);
	    if (rtnode.entries != null && rtnode.entries.size() > i) {
		if (rtnode.isLeaf()) {
		    g.setColor(context.normalcolor);
		    for (int j = 0; j < context.current.size(); j++) {
			if ((RTreeLeafEntry) rtnode.entries.elementAt(i)
			    == context.current.elementAt(j)) {
			    g.setColor(context.selectedcolor);
			    break;
			}
		    }
		    g.fillRect(tox - objectwidth / 2, y + nodeheight,
			       objectwidth, objectheight);
		} else
		    drawNode(g,
			     ((RTreeInternalEntry) rtnode.entries.elementAt(i))
				 .getPointer(),
			     tox, y + nodeheight,
			     width / rtnode.getTree().getM());
	    }
	}
    }
    
    public synchronized void update(Graphics g) {
	if (g != null) {
	    g.setColor(this.getBackground());
	    g.fillRect(0, 0, this.getSize().width + 100,
		       this.getSize().height + 100);
	    paint(g);
	}
    }
    
    public synchronized void paint(Graphics g) {
	if (g != null && context.rtree != null) {
	    nodewidth = (boxwidth - 1) * context.rtree.getM() + 1;
	    nodespace = nodewidth + spacing;
	    RTreeNode node = context.rtree.getRoot();
	    if (node != null) {
		int treeheight = context.rtree.getHeight();
		rcheight = treeheight * nodeheight + (objectheight + 1);
		rcwidth
		    = nodespace * (int) Math.pow((double) context.rtree.getM(),
						 (double) (treeheight - 1));
		this.getParent().doLayout();
		if (context.rtree.getRoot().entries != null)
		    drawNode(g, context.rtree.getRoot(), rcwidth / 2, 0,
			     rcwidth);
	    }
	}
    }
    
    public int getHeight() {
	return rcheight;
    }
    
    public Dimension getMinimumSize() {
	return new Dimension(rcwidth + 2, rcheight);
    }
    
    public Dimension getPreferredSize() {
	return new Dimension(rcwidth + 2, rcheight);
    }
    
    public int getWidth() {
	return rcwidth;
    }
    
    public String toString() {
	return "RTreeCanvas (width: " + rcwidth + ", height:" + rcheight + ")";
    }
}
