package edu.gmu.cs583.rtree;

import java.util.Vector;

public class RTreeAnimate extends Thread
{
    private Vector animationevents;
    private RTreeContext context;
    private int delay;
    private boolean quit = false;
    private int counter = -1;
    private int ffwto;
    
    public RTreeAnimate(RTreeContext contxt, int delayTime) {
	this.setPriority(10);
	context = contxt;
	delay = delayTime;
	animationevents = new Vector();
	this.setName("R-Tree Animation Thread: " + context);
    }
    
    public void setDelay(int del) {
	delay = del;
    }
    
    public int getDelay() {
	return delay;
    }
    
    public void discontinue() {
	context.setnext(null);
	quit = true;
    }
    
    public synchronized void setFfwto(int step) {
	ffwto = step;
    }
    
    public synchronized void clear() {
	for (int i = 0; i < animationevents.size(); i++)
	    animationevents.removeElementAt(0);
    }
    
    public int getEventsLeft() {
	if (animationevents != null)
	    return animationevents.size();
	return -1;
    }
    
    public synchronized void addEvent(Object animel) {
	animationevents.addElement(animel);
    }
    
    public synchronized void run() {
	counter = 0;
	context.rtree.clear();
	context.mapcanvas.repaint();
	context.rtreecanvas.repaint();
	while (animationevents.size() > 0) {
	    if (quit)
		return;
	    try {
		context.setnext((RTreeEvent) animationevents.elementAt(0));
		context.mapcanvas.repaint();
		context.rtreecanvas.repaint();
		if (animationevents.size() > 0
		    && animationevents.elementAt(0) != null) {
		    runEvent((RTreeEvent) animationevents.elementAt(0));
		    animationevents.removeElementAt(0);
		    counter++;
		}
		context.mapcanvas.repaint();
		context.rtreecanvas.repaint();
	    } catch (Exception e) {
		if (Thread.interrupted())
		    System.out
			.println("RTreeAnimate, run: interrupted e: " + e);
		else
		    System.out.println("RTreeAnimate, run, exception: " + e);
	    }
	}
	context.setnext(null);
    }
    
    private void runEvent(RTreeEvent rte) {
	if (counter >= ffwto && !Thread.interrupted()) {
	    try {
		Thread.sleep((long) delay);
	    } catch (InterruptedException interruptedexception) {
		/* empty */
	    }
	}
	if (rte.getEventType() == 1) {
	    while (context.current.size() > 0)
		context.current.removeElementAt(0);
	    context.current.addElement(rte.getEntry());
	    context.rtree.insert(rte.getEntry());
	} else if (rte.getEventType() == 0) {
	    while (context.current.size() > 0)
		context.current.removeElementAt(0);
	    context.rtree.delete(rte.getEntry());
	}
	if (counter >= ffwto) {
	    context.mapcanvas.repaint();
	    context.rtreecanvas.repaint();
	}
	context.currenteventincrement(1);
    }
}
