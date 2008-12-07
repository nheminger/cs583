package edu.gmu.cs583.rtree;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.PrintJob;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestProgram extends Frame implements ActionListener {

    MapCanvas canvas;

    public TestProgram() {
        super("Printing Test");
        
		RTree rtree = new RTree(2);
		System.out.println("Entering RTree");
		rtree.insert(new RTreeLeafEntry(null, new BoundingBox(10.0, 10.0, 20.0,20.0)));
		rtree.insert(new RTreeLeafEntry(null, new BoundingBox(15.0, 15.0, 25.0,25.0)));
		rtree.insert(new RTreeLeafEntry(null, new BoundingBox(5.0, 5.0, 15.0,15.0)));
		rtree.insert(new RTreeLeafEntry(null, new BoundingBox(20.0, 20.0, 30.0,30.0)));
		rtree.insert(new RTreeLeafEntry(null, new BoundingBox(0.0, 0.0, 10.0,10.0)));
		
        RTreeContext rtreecontext = new RTreeContext();
        rtreecontext.rtree = rtree;
        
        canvas = new MapCanvas(rtreecontext);
        add("Center", canvas);
        
        Button b = new Button("Print");
        b.setActionCommand("print");
        b.addActionListener(this);
        add("South", b);

        pack();
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("print")) {
            PrintJob pjob = getToolkit().getPrintJob(this,
                               "Printing Test", null, null);

            if (pjob != null) {          
                Graphics pg = pjob.getGraphics();

                if (pg != null) {
                    canvas.printAll(pg);
                    pg.dispose(); // flush page
                }
                pjob.end();

            }
        }
    }

    public static void main(String args[]) {
    	TestProgram test = new TestProgram();
    	test.setSize(new Dimension(500,500));
        test.show();
    }
}
