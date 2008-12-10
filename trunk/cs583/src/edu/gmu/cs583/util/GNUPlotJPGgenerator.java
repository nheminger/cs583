package edu.gmu.cs583.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import edu.gmu.cs583.data.Centroid;
import edu.gmu.cs583.data.DataPoint;

public class GNUPlotJPGgenerator {
	
	public void createAll(){
		int[] pointSizes = {15,50,100,500,1000,2000,5000,10000};
		int numberOfCents = 8;
		int iteration = 0;
		String str = "";
		BufferedWriter out = null;
		try{
			for(int i = 0; i < numberOfCents; i++){
				File g=new File("C:\\Users\\Alex\\Desktop\\gp424win32\\gnuplot\\bin\\GraphKmeans");
				for(int k = 0; k < pointSizes.length; k++){
					File f=new File("C:\\plot\\data"+i+"p"+pointSizes[k]+"i"+iteration);
					while(f.exists()){ // file exits in c:/plot
						iteration++;
						f.createNewFile();
						g.createNewFile();
						if(f.exists()){
							out = new BufferedWriter(new FileWriter(f, true));
							out.write(str);
							out.close();
						}

					
				}
			}
		}
	}catch (Exception e){
		
	}
	}
}
