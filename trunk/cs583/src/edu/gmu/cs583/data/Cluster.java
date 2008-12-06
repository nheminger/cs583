package edu.gmu.cs583.data;

import java.util.Vector;

public class Cluster {
	private Integer clusterId;
	private boolean hasNewMember;
	private Vector<DataPoint> points = new Vector<DataPoint>();
	
	Cluster(){
		
	}

	public Integer getClusterId() {
		return clusterId;
	}

	public void setClusterId(Integer clusterId) {
		this.clusterId = clusterId;
	}

	public boolean isHasNewMember() {
		return hasNewMember;
	}

	public void setHasNewMember(boolean hasNewMember) {
		this.hasNewMember = hasNewMember;
	}

	public Vector<DataPoint> getPoints() {
		return points;
	}

	public void setPoints(Vector<DataPoint> points) {
		this.points = points;
	}


	
}
