package edu.gmu.cs583.kmeans_basic;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
	
	Kmeans(Integer numberOfCentroids,Integer numberOfDataPoints,Integer x,Integer y){
		number_of_points = numberOfDataPoints;
		if(numberOfCentroids > 12){
			System.out.println("Max number of centroids is 12, The number of centroids has been set to 12");
			number_of_centroids = 12;
		} else if (numberOfCentroids > 0){
			number_of_centroids = numberOfCentroids;
		} else {
			System.out.println("Invalid number was entered for number of centroids, The number of centroids has been set to 3");
			number_of_centroids = 3;
		}
		distanceTable = new Double[number_of_points][number_of_centroids];
		Geometry.setDEBUG(false);
		makecolors();
		PointGenerator gen = new PointGenerator(numberOfDataPoints);
		gen.GeneratePoints();
		setDataPoints(gen.GetPointsVector());
		createCentroids();
	}
	
	public static void main(String[] args) {
		Kmeans kmeans = new Kmeans(3,16, 100,100);  // test constructor using generated points
		String str = " ";
		while(!str.equals("r")){
	    try {
	        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	        System.out.print("> ");
	        str = in.readLine();
	        System.out.println(kmeans);
	        kmeans.calulateMembership();
	        kmeans.recomputeCentroids();
	    } catch (IOException e) {
	    }

		}
		System.out.println("END");
	}
	
	public void createCentroids(){
		for(int i = 0; i < number_of_centroids;i++){
			Centroid cent = new Centroid();
			cent.setCentroidId(i+1);
			centroids.add(cent);
		}
		initCentroids();
	}
	
	public void initCentroids(){
		int t = 0;
		for(Centroid i: centroids){
			i.setPoints(dataPoints.get(t).getX(),dataPoints.get(t).getY());
			i.setCentroidColor(colors.get(t));
			t++;
		}
	}
	
	public void calulateMembership(){
		if(DEBUG)
			System.out.println("calulate mebership");
		double minDistance = Double.MAX_VALUE;
		Integer pointsChanged = 0;
		Vector<Centroid> movedCentroids = new Vector<Centroid>();
		for(Centroid j: centroids){
			j.getCluster().setHasNewMember(false);               // ini hasNewMember bool
			j.getCluster().setPoints(new Vector<DataPoint>());   // clear clusters for each centroid
//			if(j.isHasMoved()){
//				movedCentroids.add(j);                           // these are the only centroid that need to recompute distance to points
//			}
		}
		//TODO: does a centroid need to be recalulated if it does not move?		
		for(DataPoint i: dataPoints){
			Centroid tempcent = new Centroid();
			for(Centroid j: centroids){
				if (minDistance > Geometry.getDistance(j,i)){
					minDistance = Geometry.getDistance(j,i);
					tempcent = j;
				}
			}
			
			if(i.setMembershipId(tempcent.getCentroidId()));
				pointsChanged++;
			i.setCentroidMembership(tempcent.getCentroidColor());
			tempcent.getCluster().getPoints().add(i);
			minDistance = Double.MAX_VALUE;
			
		}

	}
	
	public void recomputeCentroids(){
		if(DEBUG)
			System.out.println("recompute centroids");
			for(Centroid j: centroids){
				Double x_mean = 0.0, y_mean = 0.0;
				DataPoint newPos = new DataPoint();
				Vector<DataPoint> temp = new Vector<DataPoint>();
				temp = j.getCluster().getPoints();
				
				for(int i = 0; i < temp.size(); i++){
					DataPoint pt = new DataPoint();
					pt = temp.get(i);
					x_mean =+ pt.getX();
					y_mean =+ pt.getY();
					
				}
				if(temp.size() > 0){
					newPos.setPoints(Geometry.truncate(x_mean/temp.size()), Geometry.truncate(y_mean/temp.size()));
					j.setDistanceMoved(Geometry.getDistance(j, newPos));
					j.setPoints(Geometry.truncate(x_mean/temp.size()), Geometry.truncate(y_mean/temp.size()));
				}
				if(DEBUG){
				System.out.println("------ centroid " + j.getCentroidId() + " ------");	
					for(DataPoint i: j.getCluster().getPoints()){
						System.out.println(i);
					}
				System.out.println(" ");
				}
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

	public String toString(){
		Integer k = 0;
		String str = new String();
		str = "----- Data Points -----\nptId\tpos\t\tcentId\n";
		for(DataPoint i : dataPoints){
			str = str + k + "\t" + i + "\n";
			k++;
		}

		str = str + "\n\n" + "----- Centroidr Points -----\ncentId\tpos\t\tdistMoved\tnumberOfPts\n";;
		for(Centroid j: centroids){
			str = str + j + "\n";
		
		}
		return str;
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
