/* Authors: Chris Andrade, Nathan Heminger, Alex Prunka
 * Class: CS583 Section 002
 * Purpose: Final Project
 * Title: Clustering Algorithms
 * Description: DataPoint Class holds X and Y values for points.
 */

package edu.gmu.cs583.data;

import java.awt.Color;


public class DataPoint implements Comparable<DataPoint>{
	private Double x;
	private Double y;
	private Integer MembershipId = -1;
	private Color centroidMembership = null;
	private boolean ownerChanged = false;
	
	public DataPoint(){
		Double x = -1.0;
		Double y = -1.0;
		centroidMembership = Color.white;
	}
	
	public DataPoint(Double x, Double y) {
		super();
		this.x = x;
		this.y = y;
		centroidMembership = Color.white;
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}
	
	public String toString(){
		return x.toString() + ":" + y.toString() + "\t" + this.MembershipId;
	}
	
	public void setPoints(Integer x, Integer y){
		this.x = x.doubleValue();
		this.y = y.doubleValue();
	}

	public void setPoints(Double x, Double y){
		this.x = x.doubleValue();
		this.y = y.doubleValue();
	}
	
	public Integer getMembershipId() {
		return MembershipId;
	}

	public boolean setMembershipId(Integer membershipId) {
		if(MembershipId != membershipId)
				setOwnerChanged(true);
		MembershipId = membershipId;
		return isOwnerChanged();
	}

	public Color getCentroidMembership() {
		return centroidMembership;
	}

	public void setCentroidMembership(Color centroidMembership) {
		this.centroidMembership = centroidMembership;
	}

	public boolean isOwnerChanged() {
		boolean temp = ownerChanged;
		ownerChanged = false;
		return temp;
	}

	public void setOwnerChanged(boolean ownerChanged) {
		this.ownerChanged = ownerChanged;
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
	
	public boolean equals(Object o){
		boolean isEqual = false;
		try{
		if(this.compareTo((DataPoint)o) == 1){
			isEqual = true;
		}
		}catch (Exception e){
			isEqual = false;
		}
		return isEqual;
	}
	
}
