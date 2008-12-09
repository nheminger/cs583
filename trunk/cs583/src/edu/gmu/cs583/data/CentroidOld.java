package edu.gmu.cs583.data;

import java.awt.Color;

public class CentroidOld {
	//Id is a number from 1 - 12
	private Integer CentroidId;
	private Double x;
	private Double y;
	private Color centroidColor;
	private Cluster cluster = new Cluster();
	private boolean hasMoved;
	private Double distanceMoved = 0.0;
	public CentroidOld(){

	}
	
	public CentroidOld(Double x ,Double y){
		CentroidId = 1;
		this.centroidColor = Color.black;
		this.cluster = new Cluster();
		this.x = x;
		this.y = y;
	}
	
	public CentroidOld(Integer centroidId, Color centroidColor, Cluster cluster,
			Double x, Double y) {
		CentroidId = centroidId;
		this.centroidColor = centroidColor;
		this.cluster = cluster;
		this.x = x;
		this.y = y;
	}

	public Integer getCentroidId() {
		return CentroidId;
	}

	public void setCentroidId(Integer centroidId) {
		CentroidId = centroidId;
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
	
	public void setPoints(Double x, Double y){
		this.x = x;
		this.y = y;
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
		return this.CentroidId +"\t"+ x.toString() + ":" + y.toString() + "\t\t" + distanceMoved + "\t\t" + cluster.getPoints().size();
	}	
}
