package edu.gmu.cs583.rtree;

/**
 * Adapted from http://gis.umb.no/gis/applets/rtree2/jdk1.1/
 */

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

public class MapCanvas extends Canvas implements MouseListener,
		MouseMotionListener {
	public static final int INSERT = 0;
	public static final int DELETE = 1;
	public static final int SEARCH = 2;
	public static final int FOCUS = 3;
	public int mode = 0;
	private static Color backgroundcolor = new Color(238, 238, 238);
	private static Color[] levelcolours = { Color.yellow, Color.cyan,
			Color.magenta, Color.orange, Color.darkGray, Color.red, Color.gray,
			Color.blue, Color.pink };
	private static Color defaultcolor = Color.black;
	private RTreeContext context;
	private int mcwidth;
	private int mcheight;
	public boolean updatebg;
	public boolean drawing;
	public int x1;
	public int y1;
	public int x2;
	public int y2;
	public int x2prev;
	public int y2prev;
	public boolean debug = false;

	public MapCanvas(RTreeContext rtreecontext) {
		context = rtreecontext;
		this.setBackground(backgroundcolor);
		this.setCursor(new Cursor(1));
		this.validate();
		mcheight = 300;
		mcwidth = 400;
		updatebg = true;
		drawing = false;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	private synchronized void painttree(Graphics g, RTreeNode node, int level,
			Vector selected) {
		if (debug)
			System.out.println("Mapcanvas Paint, node, level: " + level);
		if (node == context.focusnode) {
			g.setColor(context.focuscolor);
			if (context.splitanimate != null && context.splitanimate.isAlive())
				g.fillRect((int) node.getbb().getLeft(), (int) node.getbb()
						.getLower(),
						((int) node.getbb().getRight() - (int) node.getbb()
								.getLeft()),
						((int) node.getbb().getUpper() - (int) node.getbb()
								.getLower()));
			else {
				synchronized (node) {
					g.fillRect((int) node.getbb().getLeft(), (int) node.getbb()
							.getLower(),
							((int) node.getbb().getRight() - (int) node.getbb()
									.getLeft()),
							((int) node.getbb().getUpper() - (int) node.getbb()
									.getLower()));
				}
			}
		}
		if (level < levelcolours.length)
			g.setColor(levelcolours[level]);
		else
			g.setColor(defaultcolor);
		if (node.isLeaf()) {
			if (context.splitanimate != null && context.splitanimate.isAlive())
				g.drawRect((int) node.getbb().getLeft(), (int) node.getbb()
						.getLower(),
						((int) node.getbb().getRight() - (int) node.getbb()
								.getLeft()),
						((int) node.getbb().getUpper() - (int) node.getbb()
								.getLower()));
			else {
				synchronized (node) {
					g.drawRect((int) node.getbb().getLeft(), (int) node.getbb()
							.getLower(),
							((int) node.getbb().getRight() - (int) node.getbb()
									.getLeft()),
							((int) node.getbb().getUpper() - (int) node.getbb()
									.getLower()));
				}
			}
			for (int i = 0; i < node.entries.size(); i++) {
				g.setColor(context.normalcolor);
				for (int j = 0; j < context.current.size(); j++) {
					if ((RTreeLeafEntry) node.entries.elementAt(i) == context.current
							.elementAt(j)) {
						g.setColor(context.selectedcolor);
						selected.addElement(node.entries.elementAt(i));
					}
				}
				g
						.fillRect(
								(int) ((RTreeEntry) node.entries.elementAt(i))
										.getI().getLeft(),
								(int) ((RTreeEntry) node.entries.elementAt(i))
										.getI().getLower(),
								((int) ((RTreeEntry) node.entries.elementAt(i))
										.getI().getRight()
										- (int) ((RTreeEntry) node.entries
												.elementAt(i)).getI().getLeft() + 1),
								((int) ((RTreeEntry) node.entries.elementAt(i))
										.getI().getUpper()
										- (int) ((RTreeEntry) node.entries
												.elementAt(i)).getI()
												.getLower() + 1));
			}
		} else {
			if (context.splitanimate != null && context.splitanimate.isAlive())
				g.drawRect((int) node.getbb().getLeft(), (int) node.getbb()
						.getLower(),
						((int) node.getbb().getRight() - (int) node.getbb()
								.getLeft()),
						((int) node.getbb().getUpper() - (int) node.getbb()
								.getLower()));
			else {
				synchronized (node) {
					g.drawRect((int) node.getbb().getLeft(), (int) node.getbb()
							.getLower(),
							((int) node.getbb().getRight() - (int) node.getbb()
									.getLeft()),
							((int) node.getbb().getUpper() - (int) node.getbb()
									.getLower()));
				}
			}
			for (int i = 0; i < node.entries.size(); i++)
				painttree(g, ((RTreeInternalEntry) node.entries.elementAt(i))
						.getPointer(), level + 1, selected);
		}
		if (node.isRoot()) {
			g.setColor(context.selectedcolor);
			while (selected.size() > 0) {
				g.fillRect((int) ((RTreeEntry) selected.elementAt(0)).getI()
						.getLeft(), (int) ((RTreeEntry) selected.elementAt(0))
						.getI().getLower(), ((int) ((RTreeEntry) selected
						.elementAt(0)).getI().getRight()
						- (int) ((RTreeEntry) selected.elementAt(0)).getI()
								.getLeft() + 1), ((int) ((RTreeEntry) selected
						.elementAt(0)).getI().getUpper()
						- (int) ((RTreeEntry) selected.elementAt(0)).getI()
								.getLower() + 1));
				selected.removeElementAt(0);
			}
		}
	}

	public synchronized void update(Graphics g) {
		if (g != null) {
			if (drawing) {
				if (updatebg) {
					updatebg = false;
					g.setColor(this.getBackground());
					g.fillRect(0, 0, this.getSize().width,
							this.getSize().height);
					paint(g);
				} else {
					if (context.rtree.getRoot().entries.size() > 0)
						painttree(g, context.rtree.getRoot(), 0, new Vector());
					g.setColor(this.getBackground());
					g.drawRect(Math.min(x1, x2prev), Math.min(y1, y2prev), Math
							.abs(x2prev - x1), Math.abs(y2prev - y1));
					g.setColor(Color.darkGray);
					g.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2
							- x1), Math.abs(y2 - y1));
				}
			} else {
				g.setColor(this.getBackground());
				g.fillRect(0, 0, this.getSize().width, this.getSize().height);
				paint(g);
			}
		}
	}

	public synchronized void paint(Graphics g) {
		if (g == null || context == null || context.rtree == null) {
			if (debug)
				System.out.println("MapCanvas: no graphics / context / rtree.");
		} else {
			if (context.rtree.getRoot().entries.size() == 0) {
				mcwidth = 0;
				mcheight = 0;
				if (context.getnext() != null
						&& context.getnext().getEventType() == 1) {
					g.setColor(context.normalcolor);
					g.drawRect(((int) context.getnext().getEntry().getI()
							.getLeft() + 1), ((int) context.getnext()
							.getEntry().getI().getLower() + 1), ((int) context
							.getnext().getEntry().getI().getRight()
							- (int) context.getnext().getEntry().getI()
									.getLeft() - 2), ((int) context.getnext()
							.getEntry().getI().getUpper()
							- (int) context.getnext().getEntry().getI()
									.getLower() - 2));
					g.setColor(context.selectedcolor);
					g.drawRect((int) context.getnext().getEntry().getI()
							.getLeft(), (int) context.getnext().getEntry()
							.getI().getLower(), ((int) context.getnext()
							.getEntry().getI().getRight() - (int) context
							.getnext().getEntry().getI().getLeft()),
							((int) context.getnext().getEntry().getI()
									.getUpper() - (int) context.getnext()
									.getEntry().getI().getLower()));
				}
			} else {
				this.getParent().doLayout();
				painttree(g, context.rtree.getRoot(), 0, new Vector());
				if (context.getnext() != null) {
					if (context.getnext().getEventType() == 1) {
						g.setColor(context.normalcolor);
						g.drawRect((int) context.getnext().getEntry().getI()
								.getLeft() + 1, (int) context.getnext()
								.getEntry().getI().getLower() + 1,
								((int) context.getnext().getEntry().getI()
										.getRight()
										- (int) context.getnext().getEntry()
												.getI().getLeft() - 2),
								((int) context.getnext().getEntry().getI()
										.getUpper()
										- (int) context.getnext().getEntry()
												.getI().getLower() - 2));
						g.setColor(context.selectedcolor);
						g.drawRect((int) context.getnext().getEntry().getI()
								.getLeft(), (int) context.getnext().getEntry()
								.getI().getLower(), ((int) context.getnext()
								.getEntry().getI().getRight() - (int) context
								.getnext().getEntry().getI().getLeft()),
								((int) context.getnext().getEntry().getI()
										.getUpper() - (int) context.getnext()
										.getEntry().getI().getLower()));
					} else {
						if (context.current.indexOf(context.getnext()
								.getEntry()) != -1)
							g.setColor(context.normalcolor);
						else
							g.setColor(context.selectedcolor);
						g.drawRect((int) context.getnext().getEntry().getI()
								.getLeft(), (int) context.getnext().getEntry()
								.getI().getLower(), ((int) context.getnext()
								.getEntry().getI().getRight() - (int) context
								.getnext().getEntry().getI().getLeft()),
								((int) context.getnext().getEntry().getI()
										.getUpper() - (int) context.getnext()
										.getEntry().getI().getLower()));
						g.drawLine((int) context.getnext().getEntry().getI()
								.getLeft(), (int) context.getnext().getEntry()
								.getI().getLower(), (int) context.getnext()
								.getEntry().getI().getRight(), (int) context
								.getnext().getEntry().getI().getUpper());
						g.drawLine((int) context.getnext().getEntry().getI()
								.getLeft(), (int) context.getnext().getEntry()
								.getI().getUpper(), (int) context.getnext()
								.getEntry().getI().getRight(), (int) context
								.getnext().getEntry().getI().getLower());
					}
				}
				BoundingBox rbb = context.rtree.getRoot().calculateBB();
				mcwidth = (int) rbb.getRight();
				mcheight = (int) rbb.getUpper();
			}
			this.invalidate();
			this.getParent().doLayout();
			if (context != null && context.splitanimate != null
					&& context.splitanimate.isAlive()) {
				context.splitanimate.redraw = true;
				context.splitanimate.interrupt();
			}
		}
	}

	public synchronized void mouseClicked(MouseEvent e) {
		/* empty */
	}

	public synchronized void mousePressed(MouseEvent e) {
		/* empty */
	}

	public synchronized void mouseReleased(MouseEvent e) {
		/* empty */
	}

	public synchronized void mouseEntered(MouseEvent e) {
		/* empty */
	}

	public synchronized void mouseExited(MouseEvent e) {
		/* empty */
	}

	public synchronized void mouseDragged(MouseEvent e) {
		/* empty */
	}

	public synchronized void mouseMoved(MouseEvent e) {
		/* empty */
	}

	public synchronized Dimension getMinimumSize() {
		return new Dimension(mcwidth, mcheight);
	}

	public synchronized Dimension getPreferredSize() {
		return new Dimension(mcwidth, mcheight);
	}

	public synchronized String toString() {
		return ("MapCanvas (minimum width: " + mcwidth + ", minimum height:"
				+ mcheight + ")");
	}
}
