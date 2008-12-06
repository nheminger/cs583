/* Authors: Chris Andrade, Nathan Heminger, Alex Prunka
 * Class: CS583 Section 002
 * Purpose: Final Project
 * Title: Clustering Algorithms
 * Description: DataPoint Class holds X and Y values for points.
 */

package edu.gmu.cs583.data;

import java.awt.Color;


public class DataPoint implements Comparable<DataPoint>{
	private Integer x;
	private Integer y;
	private Integer MembershipId;
	private Color centroidMembership = null;
	
	public DataPoint(){
		Integer x = -1;
		Integer y = -1;
		centroidMembership = Color.white;
	}
	
	public DataPoint(Integer x, Integer y) {
		super();
		this.x = x;
		this.y = y;
		centroidMembership = Color.white;
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

	public Integer getMembershipId() {
		return MembershipId;
	}

	public void setMembershipId(Integer membershipId) {
		MembershipId = membershipId;
	}

	public Color getCentroidMembership() {
		return centroidMembership;
	}

	public void setCentroidMembership(Color centroidMembership) {
		this.centroidMembership = centroidMembership;
	}

	@Override
	public int compareTo(DataPoint o) {
		int result = 0;
		if(this.getX() == o.getX()){
			if(this.getY() == o.getY()){
				result = 1;
			}
		}
		return result;
	}
	
}
