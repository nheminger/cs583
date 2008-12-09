/* Authors: Chris Andrade, Nathan Heminger, Alex Prunka
 * Class: CS583 Section 002
 * Purpose: Final Project
 * Title: Clustering Algorithms
 * Description: DataPoint Class holds X and Y values for points.
 */

package edu.gmu.cs583.data;

import java.awt.Color;

public class DataPoint extends Point {
	protected Integer MembershipId = -1;
	protected Color centroidMembership = null;
	protected boolean ownerChanged = false;
	
	public DataPoint(){
		super();
		centroidMembership = Color.white;
	}
	
	public DataPoint(double[] coords) {
		super(coords);
		centroidMembership = Color.white;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < coords.length; i++) {
			sb.append(coords[i] + "\t");
		}
		sb.append("\t" + this.MembershipId);
		return sb.toString();
		//return x.toString() + "\t" + y.toString() + "\t" + this.MembershipId;
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

}
