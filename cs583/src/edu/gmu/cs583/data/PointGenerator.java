/* Authors: Chris Andrade, Nathan Heminger, Alex Prunka
 * Class: CS583 Section 002
 * Purpose: Final Project
 * Title: Clustering Algorithms
 * Description: Point Generator Class generators n number of x and y data points in a given
 * range. It outputs a Vector of n DataPoint Class initialized with x and y position in the given range.
 */

package edu.gmu.cs583.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

public class PointGenerator {

	private Integer x_range;
	private Integer y_range;
	private Integer number_of_points;
	private Vector<DataPoint> returnDataPoints = new Vector<DataPoint>();
	private HashMap<String,DataPoint> hashedPoints = new HashMap<String,DataPoint>();
	private boolean DEBUG = false;
	
	public PointGenerator(){
		x_range = 301;
		y_range = 301;
		number_of_points = 50;
		GeneratePoints();
	}
	
	public PointGenerator(Integer numberOfPoints){
		x_range = 301;
		y_range = 301;
		number_of_points = numberOfPoints;
		GeneratePoints();
	}
	
	
	public PointGenerator(Integer x, Integer y, Integer numberOfPoints){
		long tempx = x, tempy = y;
		x_range = x;
		y_range = y;
		if((tempx * tempy) > numberOfPoints){
			number_of_points = numberOfPoints;
		}
		else{
			number_of_points = (x_range * y_range) - 1;
		}
		GeneratePoints();
	}
	
	public PointGenerator(Integer x, Integer y, Integer numberOfPoints, boolean debug){
		x_range = x+1;
		y_range = y+1;
		if((x_range * y_range) > numberOfPoints){
		number_of_points = numberOfPoints;
		}
		else{
			number_of_points = (x_range * y_range) - 1;
		}
		GeneratePoints();
		this.DEBUG = debug;
	}
	
	public DataPoint GeneratePoints(){
		Random generator = new Random();
		DataPoint point = null;
		while(hashedPoints.size() < number_of_points){
			point = new DataPoint();
			double[] coords = new double[2];
			coords[0] = generator.nextInt(x_range);
			coords[1] = generator.nextInt(y_range);
			point.setCoords(coords);
			hashedPoints.put(point.toString(), point);			
		}
		return point;
	}
	
	public Vector<DataPoint> GetPointsVector(){
		for(String key : hashedPoints.keySet()){
			returnDataPoints.add(hashedPoints.get(key));
		}
		return returnDataPoints;
	}
	
	public static Vector<DataPoint> generateAndReturnPoints(int dimensions, int[] range, long numPoints, boolean randomize, boolean debug) throws Exception{

		// temporary storage of points (to guarantee points are unique)
		HashMap<String,DataPoint> pointsUniqueHash = new HashMap<String,DataPoint>();
		
		// used to return the points
		Vector<DataPoint> points = new Vector<DataPoint>();

		if (range.length < dimensions) {
			throw new Exception("Error - specify a range for each dimension. Range.length '" + range.length +"' and dimension '" + dimensions + "'.");
		}
		
//		x_range = x+1;
//		y_range = y+1;
//		if((x_range * y_range) > numberOfPoints){
//			number_of_points = numberOfPoints;
//		}
//		else{
//			number_of_points = (x_range * y_range) - 1;
//		}

		Random generator = null;
		if (randomize) {
			generator = new Random(System.currentTimeMillis());
		} else {
			generator = new Random();
		}
			
		DataPoint point = null;
		double[] coords = null;
		while(pointsUniqueHash.size() < numPoints){
			coords = new double[dimensions];
			for (int i = 0; i < dimensions; i++) {
				coords[i] = generator.nextInt(range[i]);
			}
			point = new DataPoint(coords);
			pointsUniqueHash.put(point.toString(), point);			
		}
		
		for(String key : pointsUniqueHash.keySet()){
			points.add(pointsUniqueHash.get(key));
		}
		
		return points;
	}
	
	public static Vector<DataPoint> readPointsFromFile(String filename, int dimensions) throws Exception{
		// temporary storage of points (to guarantee points are unique)
		HashMap<String,DataPoint> pointsUniqueHash = new HashMap<String,DataPoint>();
		
		// used to return the points
		Vector<DataPoint> points = new Vector<DataPoint>();
		
	 	File aFile = new File(filename);
	    try {
		        BufferedReader input =  new BufferedReader(new FileReader(aFile));
		        try {
				          String line = null;
			      		  DataPoint point = null;
			    		  double[] coords = null;
				          while (( line = input.readLine()) != null) {
								String[] parts = line.split("[\\s]+");
								coords = new double[dimensions];
								for (int i = 0; i < dimensions; i++) {
									 //System.out.println("part: " + parts[i]);
									 coords[i] = Double.parseDouble(parts[i]);
								}
								point = new DataPoint(coords);
								pointsUniqueHash.put(point.toString(), point);			
				          }
		        } finally {
		          input.close();
		        }
	      } catch (IOException ex){
	    	  ex.printStackTrace();
	      }
		
		for(String key : pointsUniqueHash.keySet()){
			points.add(pointsUniqueHash.get(key));
		}
		
		return points;
	}
	
	public HashMap<String, DataPoint> GetPointsHashed(){
		return hashedPoints;
	}
	
	public void printValues(){
		for(String key : hashedPoints.keySet()){
			System.out.println(hashedPoints.get(key).toString());
		}
	}
	
	public void newPoints(Integer x, Integer y,Integer n){
		x_range = x+1;
		y_range = y+1;
		if(n > 0)
		number_of_points = n;
		GeneratePoints();
	}

	public Integer getX_range() {
		return x_range;
	}

	public void setX_range(Integer x_range) {
		this.x_range = x_range;
	}

	public Integer getY_range() {
		return y_range;
	}

	public void setY_range(Integer y_range) {
		this.y_range = y_range;
	}
	
}
