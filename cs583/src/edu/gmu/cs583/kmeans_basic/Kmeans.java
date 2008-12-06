package edu.gmu.cs583.kmeans_basic;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.xml.crypto.Data;

import edu.gmu.cs583.data.Centroid;
import edu.gmu.cs583.data.DataPoint;
import edu.gmu.cs583.data.PointGenerator;
import edu.gmu.cs583.util.Geometry;

public class Kmeans {
	
	private Double[][] distanceTable = null;
	private Vector<DataPoint> dataPoints = new Vector<DataPoint>();
	private Vector<Centroid> centroids = new Vector<Centroid>();
	private boolean DEBUG = false;
	private List<Color> colors = new ArrayList<Color>();
	private Integer number_of_centroids;
	private Integer number_of_points;
	private Geometry distance;
	
	Kmeans(Integer numberOfCentroids,Integer numberOfDataPoints,Integer x,Integer y){
		number_of_points = numberOfDataPoints;
		if(numberOfCentroids < 12){
			System.out.println("Max number of centroids is 12, The number of centroids has been set to 12");
			number_of_centroids = 12;
		} else if (numberOfCentroids > 0){
			number_of_centroids = numberOfCentroids;
		} else {
			System.out.println("Invalid number was entered for number of centroids, The number of centroids has been set to 3");
			number_of_centroids = 3;
		}
		distanceTable = new Double[number_of_points][number_of_centroids];
		distance.setDEBUG(true);
		makecolors();
		PointGenerator gen = new PointGenerator(numberOfDataPoints);
		gen.GeneratePoints();
		createCentroids();
	}
	
	public static void main(String[] args) {
		//TODO: run kmeans
	}
	
	public void createCentroids(){
		for(int i = 0; i < number_of_centroids;i++){
			Centroid cent = new Centroid();
			centroids.add(cent);
		}
		initCentroids();
	}
	
	public void initCentroids(){
		int t = 0;
		for(Centroid i: centroids){
			i.setPoints(dataPoints.get(t).getX(),dataPoints.get(t).getY());
			i.setCentroidId((t+1));
			i.setCentroidColor(colors.get(t));
			t++;
			if(DEBUG){
				System.out.println(i.toString());
			}
		}
	}
	
	public void calulateMembership(){
		double minDistance = Double.MAX_VALUE;
		Vector<Centroid> movedCentroids = new Vector<Centroid>();
		for(Centroid j: centroids){
			j.getCluster().setHasNewMember(false);               // ini hasNewMember bool
			j.getCluster().setPoints(new Vector<DataPoint>());   // clear clusters for each centroid
			if(j.isHasMoved()){
				movedCentroids.add(j);                           // these are the only centroid that need to recompute distance to points
			}
		}
		//TODO: does a centroid need to be recalulated if it does not move?		
		for(DataPoint i: dataPoints){
			Centroid tempcent = new Centroid();
			for(Centroid j: movedCentroids){
				if (minDistance > distance.getDistance(j,i)){
					tempcent = j;
				}
			}
			
			i.setMembershipId(tempcent.getCentroidId());
			i.setCentroidMembership(tempcent.getCentroidColor());
			tempcent.getCluster().getPoints().add(i);
			tempcent.getCluster().setHasNewMember(true);
			minDistance = Double.MAX_VALUE;
			
		}

	}
	
	public void recomputeCentroids(){
		
			for(Centroid j: centroids){
				Double x_mean = 0.0, y_mean = 0.0;
				Vector<DataPoint> temp = new Vector<DataPoint>();
				temp = j.getCluster().getPoints();
				
				for(int i = 0; i < temp.size(); i++){
					DataPoint pt = new DataPoint();
					pt = temp.get(i);
					x_mean =+ pt.getX();
					y_mean =+ pt.getY();
					
				}
				j.setPoints(x_mean/temp.size(), y_mean/temp.size());
			}
		//TODO: move centroid to middle of its cluster
	}
	
	public Vector<DataPoint> getDataPoints() {
		return dataPoints;
	}

	public void setDataPoints(Vector<DataPoint> dataPoints) {
		this.dataPoints = dataPoints;
	}

	public Vector<Centroid> getCentroids() {
		return centroids;
	}

	public void setCentroids(Vector<Centroid> centroids) {
		this.centroids = centroids;
	}

	public void makecolors(){
		colors.add(Color.blue);
		colors.add(Color.cyan);
		colors.add(Color.green);
		colors.add(Color.orange);
		colors.add(Color.red);
		colors.add(Color.magenta);
		colors.add(Color.yellow);
		colors.add(Color.pink);
		colors.add(Color.gray);
		colors.add(Color.lightGray);
		colors.add(Color.magenta);
		colors.add(Color.darkGray);
	}

}
