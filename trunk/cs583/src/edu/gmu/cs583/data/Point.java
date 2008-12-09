package edu.gmu.cs583.data;

import java.awt.Color;

public class Point implements Comparable<Point> {

	protected int dimensions = 2;
	protected double[] coords = null;
	
	public Point() {
	
	}
	
	public Point(double[] coords) {
		this.dimensions = coords.length;
		this.coords = coords;
	}
	
	@Override
	public int compareTo(Point o) {
		if (this.getDimensions() == o.getDimensions()){
				for (int i = 0; i < this.getDimensions(); i++) {
					if (this.coords[i] == o.coords[i]) {
						continue;
					} else {
						if (this.coords[i] > o.coords[i])
							return -1;
						else
							return 1;
					}
				}
				return 0;
		} else {
			System.out.println("Dimenions do not match between " + this + " and " + o);
			return -1;
		}
	}
	
	public boolean equals(Object o){
		boolean isEqual = false;
		try {
			if(this.compareTo((DataPoint)o) == 0){
				isEqual = true;
			}
		}catch (Exception e){
			isEqual = false;
		}
		return isEqual;
	}

	public void setCoords(double[] coords) {
		this.setDimensions(coords.length);
		this.coords = coords;
	}

	public double[] getCoords() {
		return coords;
	}

	public void setDimensions(int dimensions) {
		this.dimensions = dimensions;
	}

	public int getDimensions() {
		return dimensions;
	}
	
}
