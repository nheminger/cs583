package edu.gmu.cs583.data;

import java.util.Vector;

public class Cluster {
	private Integer clusterId;
	private boolean hasNewMember;
	private Vector<DataPoint> cluster = new Vector<DataPoint>();
	
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

	public Vector<DataPoint> getCluster() {
		return cluster;
	}

	public void setCluster(Vector<DataPoint> cluster) {
		this.cluster = cluster;
	}
	
}
