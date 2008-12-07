package edu.gmu.cs583.rtree;


import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

public class SplitAnimate extends Thread
{
    public int currentstep;
    public boolean backstep = false;
    public boolean redraw = false;
    RTreeContext context;
    int delay = 2000;
    Graphics gmc;
    RTreeNode node;
    RTreeEntry entry;
    int stepto = -1;
    boolean done = false;
    Color outlinecolor = Color.gray;
    Color newoutlinecolor = Color.red;
    Color group1seedcolor = Color.blue;
    Color group2seedcolor = Color.orange;
    Color group1outlinecolor = group1seedcolor;
    Color group2outlinecolor = group2seedcolor;
    Color group1color = Color.cyan;
    Color group2color = Color.yellow;
    Color textcolor = Color.magenta;
    Color outlcolor;
    Color fillcolor;
    
    public SplitAnimate(RTreeContext cntxt, RTreeNode node, RTreeEntry entry,
			int ffwto) {
	context = cntxt;
	gmc = cntxt.mapcanvas.getGraphics();
	this.node = node;
	this.entry = entry;
	this.setName("Split animation thread");
	stepto = ffwto;
    }
    
    public void setDelay(int del) {
	delay = del;
    }
    
    public void stopanim() {
	done = true;
    }
    
    public int getDelay() {
	return delay;
    }
    
    public synchronized void run() {
    while_6_:
	while (!done) {
	    currentstep = 0;
	    RTreeEntryGroup orig = new RTreeEntryGroup();
	    for (int i = 0; i < node.entries.size(); i++)
		orig.addEntry((RTreeEntry) node.entries.elementAt(i));
	    orig.addEntry(entry);
	    outlcolor = outlinecolor;
	    fillcolor = context.focuscolor;
	    BoundingBox bb = orig.getBB();
	    gmc.setColor(fillcolor);
	    gmc.fillRect((int) bb.getLeft(), (int) bb.getLower(),
			 (int) bb.getRight() - (int) bb.getLeft(),
			 (int) bb.getUpper() - (int) bb.getLower());
	    gmc.setColor(outlcolor);
	    gmc.drawRect((int) bb.getLeft(), (int) bb.getLower(),
			 (int) bb.getRight() - (int) bb.getLeft(),
			 (int) bb.getUpper() - (int) bb.getLower());
	    fillcolor = context.normalcolor;
	    for (int j = 0; j < orig.size(); j++) {
		gmc.setColor(fillcolor);
		gmc.fillRect((int) orig.entryAt(j).getI().getLeft(),
			     (int) orig.entryAt(j).getI().getLower(),
			     ((int) orig.entryAt(j).getI().getRight()
			      - (int) orig.entryAt(j).getI().getLeft()),
			     ((int) orig.entryAt(j).getI().getUpper()
			      - (int) orig.entryAt(j).getI().getLower()));
	    }
	    outlcolor = outlinecolor;
	    for (int j = 0; j < orig.size(); j++) {
		gmc.setColor(outlcolor);
		if (j == node.tree.getM())
		    gmc.setColor(newoutlinecolor);
		gmc.drawRect((int) orig.entryAt(j).getI().getLeft(),
			     (int) orig.entryAt(j).getI().getLower(),
			     ((int) orig.entryAt(j).getI().getRight()
			      - (int) orig.entryAt(j).getI().getLeft()),
			     ((int) orig.entryAt(j).getI().getUpper()
			      - (int) orig.entryAt(j).getI().getLower()));
		gmc.setColor(textcolor);
		gmc.drawString(Integer.toString(j + 1),
			       (int) orig.entryAt(j).getI().getLeft(),
			       (int) orig.entryAt(j).getI().getUpper());
	    }
	    context.nextbutton.setEnabled(true);
	    if (stepto == -1 || stepto <= currentstep) {
		try {
		    Thread.sleep((long) delay);
		} catch (InterruptedException interruptedexception) {
		    /* empty */
		}
	    }
	    if (backstep) {
		if (currentstep > 0)
		    stepto = currentstep - 1;
		backstep = false;
	    } else if (redraw) {
		stepto = currentstep;
		redraw = false;
	    } else {
		Vector seeds;
		if (node.tree.getSplitMethod() == 1)
		    seeds = RTreeNode.linearPickSeeds(orig);
		else
		    seeds = RTreeNode.pickSeeds(orig);
		RTreeEntry group1seed = (RTreeEntry) seeds.elementAt(0);
		RTreeEntry group2seed = (RTreeEntry) seeds.elementAt(1);
		orig.removeEntry((RTreeEntry) seeds.elementAt(0));
		orig.removeEntry((RTreeEntry) seeds.elementAt(1));
		fillcolor = context.normalcolor;
		gmc.setColor(fillcolor);
		for (int j = 0; j < orig.size(); j++)
		    gmc.fillRect((int) orig.entryAt(j).getI().getLeft(),
				 (int) orig.entryAt(j).getI().getLower(),
				 ((int) orig.entryAt(j).getI().getRight()
				  - (int) orig.entryAt(j).getI().getLeft()),
				 ((int) orig.entryAt(j).getI().getUpper()
				  - (int) orig.entryAt(j).getI().getLower()));
		fillcolor = group1seedcolor;
		gmc.setColor(fillcolor);
		gmc.fillRect
		    ((int) ((RTreeEntry) seeds.elementAt(0)).getI().getLeft(),
		     (int) ((RTreeEntry) seeds.elementAt(0)).getI().getLower(),
		     ((int) ((RTreeEntry) seeds.elementAt(0)).getI().getRight()
		      - (int) ((RTreeEntry) seeds.elementAt(0)).getI()
				  .getLeft()),
		     ((int) ((RTreeEntry) seeds.elementAt(0)).getI().getUpper()
		      - (int) ((RTreeEntry) seeds.elementAt(0)).getI()
				  .getLower()));
		fillcolor = group2seedcolor;
		gmc.setColor(fillcolor);
		gmc.fillRect
		    ((int) ((RTreeEntry) seeds.elementAt(1)).getI().getLeft(),
		     (int) ((RTreeEntry) seeds.elementAt(1)).getI().getLower(),
		     ((int) ((RTreeEntry) seeds.elementAt(1)).getI().getRight()
		      - (int) ((RTreeEntry) seeds.elementAt(1)).getI()
				  .getLeft()),
		     ((int) ((RTreeEntry) seeds.elementAt(1)).getI().getUpper()
		      - (int) ((RTreeEntry) seeds.elementAt(1)).getI()
				  .getLower()));
		outlcolor = group1seedcolor;
		gmc.setColor(outlcolor);
		gmc.drawRect
		    ((int) ((RTreeEntry) seeds.elementAt(0)).getI().getLeft(),
		     (int) ((RTreeEntry) seeds.elementAt(0)).getI().getLower(),
		     ((int) ((RTreeEntry) seeds.elementAt(0)).getI().getRight()
		      - (int) ((RTreeEntry) seeds.elementAt(0)).getI()
				  .getLeft()),
		     ((int) ((RTreeEntry) seeds.elementAt(0)).getI().getUpper()
		      - (int) ((RTreeEntry) seeds.elementAt(0)).getI()
				  .getLower()));
		outlcolor = group2seedcolor;
		gmc.setColor(outlcolor);
		gmc.drawRect
		    ((int) ((RTreeEntry) seeds.elementAt(1)).getI().getLeft(),
		     (int) ((RTreeEntry) seeds.elementAt(1)).getI().getLower(),
		     ((int) ((RTreeEntry) seeds.elementAt(1)).getI().getRight()
		      - (int) ((RTreeEntry) seeds.elementAt(1)).getI()
				  .getLeft()),
		     ((int) ((RTreeEntry) seeds.elementAt(1)).getI().getUpper()
		      - (int) ((RTreeEntry) seeds.elementAt(1)).getI()
				  .getLower()));
		for (int j = 0; j < orig.size(); j++) {
		    outlcolor = outlinecolor;
		    if (j == node.tree.getM())
			outlcolor = newoutlinecolor;
		    gmc.setColor(outlcolor);
		    gmc.drawRect((int) orig.entryAt(j).getI().getLeft(),
				 (int) orig.entryAt(j).getI().getLower(),
				 ((int) orig.entryAt(j).getI().getRight()
				  - (int) orig.entryAt(j).getI().getLeft()),
				 ((int) orig.entryAt(j).getI().getUpper()
				  - (int) orig.entryAt(j).getI().getLower()));
		    gmc.setColor(textcolor);
		    gmc.drawString(Integer.toString(j + 1),
				   (int) orig.entryAt(j).getI().getLeft(),
				   (int) orig.entryAt(j).getI().getUpper());
		}
		currentstep++;
		context.nextbutton.setEnabled(true);
		if (stepto == -1 || stepto <= currentstep) {
		    try {
			Thread.sleep((long) delay);
		    } catch (InterruptedException interruptedexception) {
			/* empty */
		    }
		}
		if (backstep) {
		    if (currentstep > 0)
			stepto = currentstep - 1;
		    backstep = false;
		} else if (redraw) {
		    stepto = currentstep;
		    redraw = false;
		} else {
		    RTreeEntryGroup group1 = new RTreeEntryGroup();
		    RTreeEntryGroup group2 = new RTreeEntryGroup();
		    group1.addEntry((RTreeEntry) seeds.elementAt(0));
		    group2.addEntry((RTreeEntry) seeds.elementAt(1));
		    while (orig.size() > 0) {
			if (group1.size() + orig.size() <= node.tree.getm()) {
			    RTreeEntry origentry = orig.entryAt(0);
			    group1.addEntry(origentry);
			    orig.removeEntryAt(0);
			} else if (group2.size() + orig.size()
				   <= node.tree.getm()) {
			    RTreeEntry origentry = orig.entryAt(0);
			    group2.addEntry(origentry);
			    orig.removeEntryAt(0);
			} else if (node.tree.getSplitMethod() == 1) {
			    RTreeEntry origentry = orig.entryAt(0);
			    if ((group1.getBB().union(origentry.getI()).area()
				 - group1.getBB().area())
				< group2.getBB().union(origentry.getI())
				      .area() - group2.getBB().area())
				group1.addEntry(origentry);
			    else if (group1.getBB().union(origentry.getI())
					 .area() - group1.getBB().area()
				     == group2.getBB().union
					    (origentry.getI())
					    .area() - group2.getBB().area()) {
				if (group1.getBB().area()
				    < group2.getBB().area())
				    group1.addEntry(origentry);
				else if (group1.getBB().area()
					 == group2.getBB().area()) {
				    if (group1.size() <= group2.size())
					group1.addEntry(origentry);
				    else
					group2.addEntry(origentry);
				} else
				    group2.addEntry(origentry);
			    } else
				group2.addEntry(origentry);
			    orig.removeEntryAt(0);
			} else if (node.tree.getSplitMethod() == 0) {
			    RTreeEntry origentry
				= RTreeNode.pickNext(orig, group1, group2);
			    if ((group1.getBB().union(origentry.getI()).area()
				 - group1.getBB().area())
				< group2.getBB().union(origentry.getI())
				      .area() - group2.getBB().area())
				group1.addEntry(origentry);
			    else if (group1.getBB().union(origentry.getI())
					 .area() - group1.getBB().area()
				     == group2.getBB().union
					    (origentry.getI())
					    .area() - group2.getBB().area()) {
				if (group1.getBB().area()
				    < group2.getBB().area())
				    group1.addEntry(origentry);
				else if (group1.getBB().area()
					 == group2.getBB().area()) {
				    if (group1.size() <= group2.size())
					group1.addEntry(origentry);
				    else
					group2.addEntry(origentry);
				} else
				    group2.addEntry(origentry);
			    } else
				group2.addEntry(origentry);
			    orig.removeEntry(origentry);
			}
			outlcolor = outlinecolor;
			fillcolor = context.focuscolor;
			bb = group1.getBB().union(group2.getBB())
				 .union(orig.getBB());
			gmc.setColor(fillcolor);
			gmc.fillRect((int) bb.getLeft(), (int) bb.getLower(),
				     (int) bb.getRight() - (int) bb.getLeft(),
				     ((int) bb.getUpper()
				      - (int) bb.getLower()));
			gmc.setColor(outlcolor);
			gmc.drawRect((int) bb.getLeft(), (int) bb.getLower(),
				     (int) bb.getRight() - (int) bb.getLeft(),
				     ((int) bb.getUpper()
				      - (int) bb.getLower()));
			fillcolor = context.normalcolor;
			for (int j = 0; j < orig.size(); j++) {
			    gmc.setColor(fillcolor);
			    gmc.fillRect
				((int) orig.entryAt(j).getI().getLeft(),
				 (int) orig.entryAt(j).getI().getLower(),
				 ((int) orig.entryAt(j).getI().getRight()
				  - (int) orig.entryAt(j).getI().getLeft()),
				 ((int) orig.entryAt(j).getI().getUpper()
				  - (int) orig.entryAt(j).getI().getLower()));
			}
			fillcolor = group1seedcolor;
			gmc.setColor(fillcolor);
			gmc.fillRect((int) group1.entryAt(0).getI().getLeft(),
				     (int) group1.entryAt(0).getI().getLower(),
				     ((int) group1.entryAt(0).getI().getRight()
				      - (int) group1.entryAt(0).getI()
						  .getLeft()),
				     ((int) group1.entryAt(0).getI().getUpper()
				      - (int) group1.entryAt(0).getI()
						  .getLower()));
			fillcolor = group1color;
			gmc.setColor(fillcolor);
			for (int j = 1; j < group1.size(); j++)
			    gmc.fillRect
				((int) group1.entryAt(j).getI().getLeft(),
				 (int) group1.entryAt(j).getI().getLower(),
				 ((int) group1.entryAt(j).getI().getRight()
				  - (int) group1.entryAt(j).getI().getLeft()),
				 ((int) group1.entryAt(j).getI().getUpper()
				  - (int) group1.entryAt(j).getI()
					      .getLower()));
			fillcolor = group2seedcolor;
			gmc.setColor(fillcolor);
			gmc.fillRect((int) group2.entryAt(0).getI().getLeft(),
				     (int) group2.entryAt(0).getI().getLower(),
				     ((int) group2.entryAt(0).getI().getRight()
				      - (int) group2.entryAt(0).getI()
						  .getLeft()),
				     ((int) group2.entryAt(0).getI().getUpper()
				      - (int) group2.entryAt(0).getI()
						  .getLower()));
			fillcolor = group2color;
			gmc.setColor(fillcolor);
			for (int j = 1; j < group2.size(); j++)
			    gmc.fillRect
				((int) group2.entryAt(j).getI().getLeft(),
				 (int) group2.entryAt(j).getI().getLower(),
				 ((int) group2.entryAt(j).getI().getRight()
				  - (int) group2.entryAt(j).getI().getLeft()),
				 ((int) group2.entryAt(j).getI().getUpper()
				  - (int) group2.entryAt(j).getI()
					      .getLower()));
			bb = group1.getBB();
			outlcolor = group1outlinecolor;
			gmc.setColor(outlcolor);
			gmc.drawRect((int) bb.getLeft(), (int) bb.getLower(),
				     (int) bb.getRight() - (int) bb.getLeft(),
				     ((int) bb.getUpper()
				      - (int) bb.getLower()));
			bb = group2.getBB();
			outlcolor = group2outlinecolor;
			gmc.setColor(outlcolor);
			gmc.drawRect((int) bb.getLeft(), (int) bb.getLower(),
				     (int) bb.getRight() - (int) bb.getLeft(),
				     ((int) bb.getUpper()
				      - (int) bb.getLower()));
			outlcolor = group1seedcolor;
			gmc.setColor(outlcolor);
			gmc.drawRect((int) group1.entryAt(0).getI().getLeft(),
				     (int) group1.entryAt(0).getI().getLower(),
				     ((int) group1.entryAt(0).getI().getRight()
				      - (int) group1.entryAt(0).getI()
						  .getLeft()),
				     ((int) group1.entryAt(0).getI().getUpper()
				      - (int) group1.entryAt(0).getI()
						  .getLower()));
			outlcolor = group2seedcolor;
			gmc.setColor(outlcolor);
			gmc.drawRect((int) group2.entryAt(0).getI().getLeft(),
				     (int) group2.entryAt(0).getI().getLower(),
				     ((int) group2.entryAt(0).getI().getRight()
				      - (int) group2.entryAt(0).getI()
						  .getLeft()),
				     ((int) group2.entryAt(0).getI().getUpper()
				      - (int) group2.entryAt(0).getI()
						  .getLower()));
			outlcolor = group1outlinecolor;
			gmc.setColor(outlcolor);
			for (int j = 1; j < group1.size(); j++)
			    gmc.drawRect
				((int) group1.entryAt(j).getI().getLeft(),
				 (int) group1.entryAt(j).getI().getLower(),
				 ((int) group1.entryAt(j).getI().getRight()
				  - (int) group1.entryAt(j).getI().getLeft()),
				 ((int) group1.entryAt(j).getI().getUpper()
				  - (int) group1.entryAt(j).getI()
					      .getLower()));
			outlcolor = group2outlinecolor;
			gmc.setColor(outlcolor);
			for (int j = 1; j < group2.size(); j++)
			    gmc.drawRect
				((int) group2.entryAt(j).getI().getLeft(),
				 (int) group2.entryAt(j).getI().getLower(),
				 ((int) group2.entryAt(j).getI().getRight()
				  - (int) group2.entryAt(j).getI().getLeft()),
				 ((int) group2.entryAt(j).getI().getUpper()
				  - (int) group2.entryAt(j).getI()
					      .getLower()));
			outlcolor = outlinecolor;
			for (int j = 0; j < orig.size(); j++) {
			    gmc.setColor(outlcolor);
			    gmc.drawRect
				((int) orig.entryAt(j).getI().getLeft(),
				 (int) orig.entryAt(j).getI().getLower(),
				 ((int) orig.entryAt(j).getI().getRight()
				  - (int) orig.entryAt(j).getI().getLeft()),
				 ((int) orig.entryAt(j).getI().getUpper()
				  - (int) orig.entryAt(j).getI().getLower()));
			    gmc.setColor(textcolor);
			    gmc.drawString(Integer.toString(j + 1),
					   (int) orig.entryAt(j).getI()
						     .getLeft(),
					   (int) orig.entryAt(j).getI()
						     .getUpper());
			}
			currentstep++;
			context.nextbutton.setEnabled(true);
			if (stepto == -1 || stepto <= currentstep) {
			    try {
				Thread.sleep((long) delay);
			    } catch (InterruptedException interruptedexception) {
				/* empty */
			    }
			}
			if (backstep) {
			    if (currentstep > 0)
				stepto = currentstep - 1;
			    backstep = false;
			    continue while_6_;
			}
			if (redraw) {
			    stepto = currentstep;
			    redraw = false;
			    continue while_6_;
			}
		    }
		    done = true;
		}
	    }
	}
	context.focusnode = null;
    }
}
