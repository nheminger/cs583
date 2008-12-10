package edu.gmu.cs583.kmeans_basic;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Timer;
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
	private static Vector<Integer> pointSize = Geometry.makepointSize();
	private static String DATE_FORMAT_NOW = "H:mm:ss:SSS";
	
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
		
		//PointGenerator gen = new PointGenerator(x,y,numberOfDataPoints);
		//setDataPoints(gen.GetPointsVector());
		
		int range[] = new int[2];
		range[0] = x;
		range[1] = y;
		
		Vector<DataPoint> randomGeneratedPoints = null;;
		try {
			randomGeneratedPoints = PointGenerator.generateAndReturnPoints(2, range, numberOfDataPoints, true, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		setDataPoints(randomGeneratedPoints);
		
		System.out.println("constructor " + dataPoints.size());
		createCentroids();
	}
	
	public static void main(String[] args) {
		Kmeans kmeans = null;
//		Integer h = 15000;
//		Calendar cal = Calendar.getInstance();
//		cal.getTime();
		for(int i = 2; i < 10; i++){  // testing range of centroids 2 - 10 
			System.out.println("testing " + i + " centroids....");
			for(Integer h: pointSize){ // points size 10 - 100000 increasing by a factor of 10 each test
				for(int k = 0; k < 20; k++ ){ // run test 100 times
					System.out.println("number of data points: " + h);
					kmeans = new Kmeans(i ,h ,h, h);
					kmeans.runKmeans(kmeans);
				}
			}
		}
		System.out.println("END");
	}
	
	public void runKmeans(Kmeans kmeans){
		Integer iterations = 0;
		long starttime = System.currentTimeMillis();
		do{
			iterations++;
			kmeans.calulateMembership();
			kmeans.recomputeCentroids();
		}while(!kmeans.isDone());
		long endtime = System.currentTimeMillis();
		logStats(kmeans.getCentroids().size(), kmeans.getDataPoints().size(), (endtime - starttime), iterations); 
	}
	
	public void logStats(Integer number_of_cents, Integer number_of_pts, long time,Integer iters){
		   
		try{
			     File f=new File("c:\\stats.txt");
			      if(f.exists()){
			      String str= number_of_cents + ":" + number_of_pts+ ":" + time + ":" + iters + "\n";
			          BufferedWriter out = new BufferedWriter(new FileWriter(f, true));
			          out.write(str);
			          out.close();
			          //System.out.println("The data has been written");
			          }
			          else
			            System.out.println("This file is not exist");
		}catch (Exception e){
			System.out.println("LOG STATS ERROR: writing to file failed");
		}
			    
	}
	
		public void logGNUPlotFile(Integer Cent,Integer pts,Integer it){
		BufferedWriter out = null;
		try{
			     File f=new File("c:\\plot\\data"+Cent+"p"+pts+"i"+it+".txt");
			     File g=new File("c:\\plot\\data"+Cent+"p"+pts+"i"+it+"cent"+".txt");
			     f.createNewFile();
			     g.createNewFile();
			      if(f.exists()){
			      String str= "";
			      	 for(DataPoint i : dataPoints){
			      	//	 str += i.getX() +"\t"+i.getY() + " \t" + i.getMembershipId() + "\n";
			      	 }
			          out = new BufferedWriter(new FileWriter(f, true));
			          out.write(str);
			          out.close();
			          //System.out.println("The data has been written");
			          }
		          else
			            System.out.println("This file is not exist");
			      if(g.exists()){
				      String str= "";
				      	 for(Centroid i : centroids){
				      	//	 str += i.getX() +"\t"+i.getY() + "\t" + i.getCentroidId() + "\n";
				      	 }
				          out = new BufferedWriter(new FileWriter(g, true));
				          out.write(str);
				          out.close();
				          }
			          else
			            System.out.println("This file is not exist");
		}catch (Exception e){
			System.out.println("GNUPLOT ERROR: writing to file failed");
		}
			    
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
		System.out.println(" init " + centroids.size() +"  " + dataPoints.size());
		for(Centroid i: centroids){
			//System.out.println(dataPoints.get(t).getX()+ " ===== " +dataPoints.get(t).getY() + " t: " + t + " -> " + dataPoints.size() );
	
			// 2-D to N-D conversion
			//i.setPoints(dataPoints.get(t).getX(),dataPoints.get(t).getY());
			i.setCoords(dataPoints.get(t).getCoords());
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
				
				double[] means = new double[temp.get(0).getDimensions()];
				
				for(int i = 0; i < temp.size(); i++){
					DataPoint pt = new DataPoint();
					pt = temp.get(i);
					
					// CONVERSION FROM 2-D to N-D
					double[] currentCoords = pt.getCoords();
					for (int k = 0; k < means.length; k++) {
						means[k] += currentCoords[k];
					}
					//x_mean += pt.getX();
					//y_mean += pt.getY();
					
				}
				
				if(temp.size() > 1){
					
					// calculate mean coordinates of all the points (since there is more than 1)
					for (int k = 0; k < means.length; k++) {
						means[k] = Geometry.truncate(means[k] / temp.size());
					}
					
					// set the new position of where the centroid will be
					newPos.setCoords(means);
					
					// calculate and set the distance the centroid moved (used for statistics and/or termination)
					j.setDistanceMoved(Geometry.getDistance(j, newPos));
					
					// update the location of the centroid to the new mean location
					j.setCoords(means);
					
					//System.out.println("x total: " + x_mean + " y total: " + y_mean + "(n) cent pos: " + x_mean/temp.size()+ " -- " + y_mean/temp.size());
				} else if(temp.size() == 1){
					newPos.setCoords(means);
					j.setDistanceMoved(Geometry.getDistance(j, newPos));
					j.setCoords(means);
					//System.out.println("x total: " + x_mean + " y total: " + y_mean + "(1) cent pos: " + j.getX() + " -- " + j.getY());
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
//		Integer k = 0;
		String str = new String();
//		str = "----- Data Points -----\nptId\tpos\t\tcentId\n";
//		for(DataPoint i : dataPoints){
//			//str = str + k + "\t" + i + "\n";
//			str = str + i + "\n";
//			k++;
//		}

		str = str + "\n\n" + "----- Centroidr Points -----\ncentId\tpos\t\t\tdistMoved\tnumberOfPts\n";;
		for(Centroid j: centroids){
			str = str + j + "\n";
		
		}
		return str;
	}
	
	public boolean isDone(){
		boolean done = true;
		for(Centroid j: centroids){
			if((j.getDistanceMoved() > 0.0) && (done == true)){
				done = false;
			}
		}
		return done;
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
