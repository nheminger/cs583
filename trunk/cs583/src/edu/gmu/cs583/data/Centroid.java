package edu.gmu.cs583.data;

import java.awt.Color;

public class Centroid {
	//Id is a number from 1 - 12
	private Integer CentroidId;
	private Integer x;
	private Integer y;
	private Color centroidColor;
	private Cluster cluster;
	
	public Centroid(){

	}
	
	public Centroid(Integer x ,Integer y){
		CentroidId = 1;
		this.centroidColor = Color.black;
		this.cluster = new Cluster();
		this.x = x;
		this.y = y;
	}
	
	public Centroid(Integer centroidId, Color centroidColor, Cluster cluster,
			Integer x, Integer y) {
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
	
	public void setPoints(Integer x, Integer y){
		this.x = x;
		this.y = y;
	}
	
	public String toString(){
		return x.toString() + ":" + y.toString();
	}	
}
