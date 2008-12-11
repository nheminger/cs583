package edu.gmu.cs583.util;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RTreeStatFileReader {
	public Vector<Stats> data = new Vector<Stats>();
	
	public static void main(String[] args){
		RTreeStatFileReader stat = new RTreeStatFileReader();
		stat.fileReader();
	}
	
	public void fileReader(){
		try{
		    FileInputStream fstream = new FileInputStream("c:\\rtree_stats.txt");
		    DataInputStream in = new DataInputStream(fstream);
		        BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String INPUT;
		    while ((INPUT = br.readLine()) != null )   {
		    	Pattern pat = Pattern.compile("(.*):(.*):(.*):(.*)");
		    	Matcher m = pat.matcher(INPUT); // get a matcher object
		    	if(m.find()) {
		    		Integer centvalue = Integer.parseInt(m.group(1));
		    		Integer ptvalue = Integer.parseInt(m.group(2));
		    		Integer timevalue = Integer.parseInt(m.group(3));
		    		if(data.isEmpty()){
		    			Stats stats = new Stats();
		    			stats.numberPoints = ptvalue;
		    			stats.add(timevalue);
		    			data.add(stats);
		    		}
		    		else {
		    			boolean found = false;
		    			for(Stats i : data){
		    				if(i.numberPoints.intValue() == ptvalue.intValue()){
		    					i.add(timevalue);
		    					found = true;
		    				}
		    			}
		    			if(!found){
			    			Stats stats = new Stats();
			    			stats.numberPoints = ptvalue;
			    			stats.add(timevalue);
			    			data.add(stats);
		    			}
		    		}
		           } 
		    	}
		    in.close();
		    }catch (Exception e){
		       e.printStackTrace();
		    }
		    csvFile();
	}
	
	public void csvFile(){
		try{
			     File f=new File("c:\\formatedRtreeStats2D.csv");
			     System.out.println(f.createNewFile());
			      if(f.exists()){
				      String str= "";
				          BufferedWriter out = new BufferedWriter(new FileWriter(f, true));
						    for(Stats i : data){
						    	str = i.numberPoints + "," + i.computeAvg() +"\n";
						    	out.write(str);
						    }
				          out.close();
			        } else {
			            System.out.println("This file is not exist");
			        }
		} catch (Exception e){
			System.out.println("LOG STATS ERROR: writing to file failed");
		}
			    
	}
	
	class Stats{
		public Integer numberPoints;
		public Vector<Integer> times = new Vector<Integer>();
		public Integer avgTime;
		public Integer maxTime;
		public Integer minTime;
		
		Stats(){
			numberPoints = 0;
			avgTime = 0;
			maxTime = 0;
			minTime = 0;
		}
		
		
		
		public void add( Integer time){
			times.add(time);
			if(times.size() == 1){
				maxTime = time;
				minTime = time;
			}
			else{
				if(maxTime < time){
					maxTime = time;
				}
				if(minTime > time){
					minTime = time;
				}
			}
		}
		
		public Integer computeAvg(){
			Integer temp = 0;
			for(Integer i : times){
				temp += i;
			}
			if(times.size() != 0)
				temp = temp/times.size();
				
			return temp;
		}
		
		
	}
	
	
}
