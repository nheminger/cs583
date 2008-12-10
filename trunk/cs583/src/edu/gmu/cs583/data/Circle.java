package edu.gmu.cs583.data;

import edu.gmu.cs583.util.Geometry;

public class Circle { 
    private Point center;
    private double radius;
   
    public Circle(Point center, double radius) {
        this.center = center;
        this.radius = radius;
    }
    public double area()      {
    	return Math.PI * radius * radius;
    }
    
    public double perimeter() {
    	return 2 * Math.PI * radius;
    }

    public boolean contains(Point p) {
    	return Geometry.getDistance(center, p) <= radius;
    }

    public static void main(String[] args) {

    }
}