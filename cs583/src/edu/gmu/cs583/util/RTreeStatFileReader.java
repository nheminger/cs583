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
	public Vector<CentroidStats> CentValues = new Vector<CentroidStats>();
	
	public static void main(String[] args){
		RTreeStatFileReader stat = new RTreeStatFileReader();
		stat.fileReader();
	}
	
	public void fileReader(){
		try{
		    FileInputStream fstream = new FileInputStream("c:\\stats.txt");
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
		    		Integer itervalue = Integer.parseInt(m.group(4));
		           if(CentValues.isEmpty()){
		        	   CentroidStats record = new CentroidStats();
		        	   record.setId(centvalue);
		        	   record.addTestResults(ptvalue, timevalue, itervalue);
		        	   CentValues.add(record);
		           }
		           else {
		        	   boolean found = false;
		        	   for(CentroidStats i : CentValues){
		        		   if(i.getId() == centvalue){
		        			   i.addTestResults(ptvalue, timevalue, itervalue);
		        			   found = true;
		        		   }
		        	   }
		        	   if(found == false){
		        		   CentroidStats record = new CentroidStats();
			        	   record.setId(centvalue);
			        	   record.addTestResults(ptvalue, timevalue, itervalue);
			        	   CentValues.add(record);
		        	   }
		        	   
		           }
		           
		    	}
		    }
		    in.close();
		    System.out.println("Number of Centroid Sizes:\t" + CentValues.size());
		    for(CentroidStats i : CentValues){
		    	System.out.println("-------- Centroid " + i.getId() + " -------");
		    	System.out.println("Number of Different Point Sizes:\t" + i.timeValues.size());
		    	for(PointSize j : i.timeValues){
		    		System.out.println("\nSTATS FOR " + j.getId() + " Pts. OUT OF " + j.iterValues.size()+" TESTS >");
		    		System.out.println("Average Time (ms):\t"+j.getAvgTime() );
		    		System.out.println("Max Time (ms):\t\t"+j.getTimeMax() );
		    		System.out.println("Min Time (ms):\t\t"+j.getTimeMin() );
		    		System.out.println("Average Iterations:\t"+j.getAvgIter() );
		    		System.out.println("Max Iterations:\t\t"+j.getIterMax() );
		    		System.out.println("Min Iterations:\t\t"+j.getIterMin() );	
		    	}
		    }

		    }catch (Exception e){
		       e.printStackTrace();
		    }
		    csvFile();

	}
	
	public void csvFile(){
		try{
			     File f=new File("c:\\formatedRtreeStats2D.csv");
			      if(f.exists()){
				      String str= "";
				          BufferedWriter out = new BufferedWriter(new FileWriter(f, true));
						    for(CentroidStats i : CentValues){
						    	System.out.println("cent");
						    	for(PointSize j : i.timeValues){
							    	System.out.println("pointsize");
						    		// cents,number of points,number of tests,average time, average iterations
						    		str = i.getId()+"," + j.getId() +"," +j.getAvgTime()+","+j.getAvgIter()+"\n";
							        out.write(str);
						    	}
						    }
				          out.close();
			        } else {
			            System.out.println("This file is not exist");
			        }
		} catch (Exception e){
			System.out.println("LOG STATS ERROR: writing to file failed");
		}
			    
	}
	
	public Vector<CentroidStats> getCentValues() {
		return CentValues;
	}

	public void setCentValues(Vector<CentroidStats>  CentValues) {
		this.CentValues =  CentValues;
	}

	private class CentroidStats {
		public Integer Id;
		public Integer numberOfTests;
		public Vector<PointSize> timeValues = new Vector<PointSize>();
		
		CentroidStats(){
			Id = 0;
			numberOfTests = 0;
		}
		
		public void addTestResults(Integer numberOfPts, Integer time, Integer iter){
			boolean found = false;
			Integer Id = numberOfPts;
			
			PointSize ps = new PointSize();
			ps.setId(Id);
			ps.add(time, iter);
			if(timeValues.isEmpty()){
				timeValues.add(ps);				
			}
			else{
				for(PointSize i : timeValues){
					if(i.getId().intValue() == Id.intValue()){ //look for that point size in vector 
						found = true;
						i.add(time, iter);  //add data to record
					}
				}
				if(found == false){     // not found in timeValues
					timeValues.add(ps); // add it as a new record
				}
			}
			numberOfTests++;
		}
		
		public Integer getId() {
			return Id;
		}
		public void setId(Integer id) {
			Id = id;
		}
		public Integer getNumberOfTests() {
			return numberOfTests;
		}
		public void setNumberOfTests(Integer numberOfTests) {
			this.numberOfTests = numberOfTests;
		}
		public Vector<PointSize> getTimeValues() {
			return timeValues;
		}
		public void setTimeValues(Vector<PointSize> timeValues) {
			this.timeValues = timeValues;
		}
	
	}
	
	private class PointSize{
		private Integer Id;
		private Integer timeMax;
		private Integer timeMin;
		private Integer iterMax;
		private Integer iterMin;
		private Integer timeAvg;
		private Integer iterAvg;
		private Vector<Integer> timeValues = new Vector<Integer>();
		private Vector<Integer> iterValues = new Vector<Integer>();
		private Vector<Double> timeOverIter = new Vector<Double>();

		
		
		
		public PointSize() {
			Id = 0;
			this.iterAvg = 0;
			this.iterMax = 0;
			this.iterMin = 0;
			this.timeAvg = 0;
			this.timeMax = 0;
			this.timeMin = 0;
		}


		public void add(Integer time, Integer iter){
			double ratio = 0;
			timeValues.add(time);
			iterValues.add(iter);
			if(iter != 0)
				ratio =  time/iter;
			timeOverIter.add(ratio);
		}
		
		
		public Integer getId() {
			return Id;
		}
		
		public void setId(Integer size) {
			this.Id = size;
		}
		
		public Integer getAvgTime() {
			Integer avg = 0, temp = 0;
			if(!timeValues.isEmpty()){
				for(Integer i: timeValues){
					temp += i;
					if(timeMin == 0){
						timeMin = i;
					}
					else if(timeMin > i){
						timeMin = i;
					}
					else if(timeMax < i){
						timeMax = i;
					}
				}
				avg = temp/timeValues.size();
			}
			return avg;
		}

		public Integer getAvgIter() {
			Integer avg = 0, temp = 0;
			if(!iterValues.isEmpty()){
				for(Integer i: iterValues){
					temp += i;
					if(iterMin == 0){
						iterMin = i;
					}
					else if(iterMin > i){
						iterMin = i;
					}
					else if(iterMax < i){
						iterMax = i;
					}
				}
				avg = temp/iterValues.size();
			}
			return avg;
		}

		public Integer getTimeMax() {
			return timeMax;
		}

		public void setTimeMax(Integer timeMax) {
			this.timeMax = timeMax;
		}

		public Integer getTimeMin() {
			return timeMin;
		}

		public void setTimeMin(Integer timeMin) {
			this.timeMin = timeMin;
		}

		public Integer getIterMax() {
			return iterMax;
		}

		public void setIterMax(Integer iterMax) {
			this.iterMax = iterMax;
		}

		public Integer getIterMin() {
			return iterMin;
		}

		public void setIterMin(Integer iterMin) {
			this.iterMin = iterMin;
		}
		
	
	}
	
	
	
}
