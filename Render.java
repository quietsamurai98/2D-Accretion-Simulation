/**
 * @(#)Render.java
 *
 *
 * @author 
 * @version 1.00 2016/9/16
 */

import java.util.Scanner;
import java.io.*;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import javax.imageio.*;
public class Render {
	int imageSize;
	int length;
	double[] x;
	double[] y;
	double[] m;
	int focusOn = 1;
	double centerX;
	double centerY;
	BufferedImage displayImage;
	int blackInt = new Color(0,0,0).getRGB();
	int whiteInt = new Color(255,255,255).getRGB();
	Graphics2D    displayGraphics;
	String directoryTextString;
	String directoryImageString;
	int particleCount;
	int frameEnd;
	int frameStart;
	int frameSkip;
	int picCount;
	double zoom;
	long startTime, elapsedTime;
	
    public Render(int partCount, int frameStartConstruct, int frameEndConstruct, int frameSkipConstruct, int resolution, double zoomFactor) {
    	particleCount = partCount;
    	directoryTextString = ".\\"+"Simulation text frames (particleCount="+particleCount+")"+"\\";
    	directoryImageString = ".\\"+"Simulation image frames (particleCount="+particleCount+")"+"\\";
    	frameStart = frameStartConstruct;
    	frameEnd   = frameEndConstruct;
    	frameSkip  = frameSkipConstruct;
    	imageSize  = resolution;
    	zoom       = zoomFactor;
    	createDirectory(particleCount);	
    }
    public void methodRunner(){
    	picCount=0;
    	for(int frameCount=frameStart;frameCount<=frameEnd;frameCount+=frameSkip){
    		startTime=System.nanoTime();
	    	picCount++;
	    	readTextFile(frameCount);
	    	focus();
	    	drawParticles();
	    	saveImage(frameCount);
	    	elapsedTime=System.nanoTime()-startTime;
	    	System.out.println("Image "+picCount+" (Frame " + frameCount + ") saved (took " + String.format("%014d", elapsedTime) + " nanoseconds)");
	    	
    	}
    }
    private void readTextFile(int frameCount){
    	String inputFileName = directoryTextString + String.format("%010d", frameCount) + ".txt";
        File inputFile = new File(inputFileName);
        Scanner input = null;
        try {
        	input = new Scanner(inputFile);
        }
        catch (FileNotFoundException ex){
        	System.out.println("File "+inputFileName+" not found.");
        	System.exit(1);
        }
        length=0;
        while (input.hasNextLine()){
        	input.nextLine();
        	length++;
        }
        x = new double[length];
        y = new double[length];
        m = new double[length];
        input.close();
        inputFileName = directoryTextString + String.format("%010d", frameCount) + ".txt";
        inputFile = new File(inputFileName);
        input = null;
        try {
        	input = new Scanner(inputFile);
        }
        catch (FileNotFoundException ex){
        	System.out.println("File "+inputFileName+" not found.");
        	System.exit(1);
        }
        for (int i=0; i<length;i++){
	        x[i]=input.nextDouble();
	        y[i]=input.nextDouble();
	        m[i]=input.nextDouble();
        }
        
    }
    
    private void focus(){
    	if (focusOn==1){  //Full System Barycenter
	        double sumMass=0;
			double sumX=0;
			double sumY=0;
			for(int i=0; i<length;i++){
					sumMass+= m[i];
					sumX   += x[i]*m[i];
					sumY   += y[i]*m[i];
			}
			sumX/=sumMass;
			sumY/=sumMass;
			centerX=sumX;
			centerY=sumY;
        }else if(focusOn==2){  //Largest Particle
        	
        }else{ //Origin
        	centerX=0;
        	centerY=0;
        }
    }
    
    private void drawParticles(){
    	displayImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB );
    	displayGraphics = displayImage.createGraphics();
    	for(int i=0; i<length; i++){
			int dispX = (int)(((x[i]-centerX)*200*zoom)+imageSize/2);
			int dispY = (int)(((y[i]-centerY)*200*zoom)+imageSize/2);
			int radius = (int)((Math.sqrt(m[i])+0.5)/2*zoom);
			for (int j = dispX-radius; j <= dispX+radius;j++){
				for (int k = dispY-radius; k <= dispY+radius;k++){
					if((j<imageSize)&&(k<imageSize)&&(j>0)&&(k>0)){
						if (((dispX-j)*(dispX-j)+(dispY-k)*(dispY-k))<=(radius*radius)){
							displayImage.setRGB(j,k, whiteInt);
						}
					}
				}
			}
		}
	}
	private void saveImage(int frameCount){
		try{
            File f = new File(directoryImageString+String.format("%010d", picCount)+".png");
            ImageIO.write(displayImage, "PNG", f);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private void createDirectory(int particleCount){
    	File directory = new File("Simulation image frames (particleCount="+particleCount+")");
		if (!directory.exists()) {
		    boolean result = false;
		
		    try{
		        directory.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se){
		        //handle it
		    }        
		    if(result) {
		    }
		}else{
			//if the directory exists, delete all existing images
			System.out.println("Clearing directory");
			for(File file: directory.listFiles()) 
    			if (!file.isDirectory()) 
        			file.delete();
        	System.out.println("Directory cleared!");
		}
	}
    
}