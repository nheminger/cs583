package edu.gmu.cs583.data;

import java.awt.Color;

public class Centroid extends Point {
	//Id is a number from 1 - 12
	private Integer CentroidId;

	private Color centroidColor;
	private Cluster cluster = new Cluster();
	private boolean hasMoved;
	private Double distanceMoved = 0.0;
	
	public Centroid(){

	}
	
	public Centroid(double[] coords){
		super(coords);
		CentroidId = 1;
		this.centroidColor = Color.black;
		this.cluster = new Cluster();
	}
	
	public Centroid(Integer centroidId, Color centroidColor, Cluster cluster, double[] coords, int dimensions) {
		super(coords);
		this.CentroidId = centroidId;
		this.centroidColor = centroidColor;
		this.cluster = cluster;
	}
	
//	public Centroid(Integer centroidId, Color centroidColor, Cluster cluster,
//			Double x, Double y) {
//		CentroidId = centroidId;
//		this.centroidColor = centroidColor;
//		this.cluster = cluster;
//		this.x = x;
//		this.y = y;
//	}

	public Integer getCentroidId() {
		return CentroidId;
	}

	public void setCentroidId(Integer centroidId) {
		CentroidId = centroidId;
	}

	public Color getCentroidColor() {
		return centroidColor;
	}

	public void setCentroidColor(Color centroidColor) {
		this.centroidColor = centroidColor;
	}

	public Cluster getCluster() {
		return cluster;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}
	
	public boolean isHasMoved() {
		return hasMoved;
	}

	public void setHasMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}
	

	public Double getDistanceMoved() {
		return distanceMoved;
	}

	public void setDistanceMoved(Double distanceMoved) {
		this.distanceMoved = distanceMoved;
	}
	
	public String toString(){
		// OLD METHOD
		//return this.CentroidId +"\t"+ x.toString() + ":" + y.toString() + "\t\t" + distanceMoved + "\t\t" + cluster.getPoints().size();
		
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < coords.length; i++) {
			sb.append(coords[i] + "\t");
		}
		sb.append("\t\t" + this.distanceMoved + "\t\t" + cluster.getPoints().size());
		return sb.toString();
	}
	
}
