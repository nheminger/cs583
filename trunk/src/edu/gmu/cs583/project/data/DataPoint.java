/* Authors: Chris Andrade, Nathan Heminger, Alex Prunka
 * Class: CS583 Section 002
 * Purpose: Final Project
 * Title: Clustering Algorithms
 * Description: DataPoint Class holds X and Y values for points.
 */

package edu.gmu.cs583.project.data;

import java.awt.Color;


public class DataPoint {
	Integer x;
	Integer y;
	
	public DataPoint(){
		Integer x = -1;
		Integer y = -1;
	}
	
	public DataPoint(Integer x, Integer y) {
		super();
		this.x = x;
		this.y = y;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}
	
	public String toString(){
		return x.toString() + ":" + y.toString();
	}
	
	public void setPoints(Integer x, Integer y){
		this.x = x;
		this.y = y;
	}
	
}
