/**
 * @(#)Render.java
 *
 *
 * @author 
 * @version 1.00 2016/9/16
 */

import java.util.Scanner;
import java.io.*;
import java.awt.geom.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import javax.imageio.*;
public class Render {
	int imageSizeX;
	int imageSizeY;
	int length;
	double[] x;
	double[] y;
	double[] xOld;
	double[] yOld;
	double[] m;
	int focusOn = 1;
	double centerX;
	double centerY;
	BufferedImage particleImage;
	BufferedImage trailImage;
	int blackInt = new Color(0,0,0).getRGB();
	int whiteInt = new Color(255,255,255).getRGB();
	Graphics2D trailGraphics;
	Graphics2D particleGraphics;
	String directoryTextString;
	String directoryImageString;
	String directoryImageNameString;
	int particleCount;
	int trailLength;
	int frameEnd;
	int frameStart;
	int frameSkip;
	int picCount;
	double zoom;
	long startTime, elapsedTime;
	
    public Render(String name, int particles, int lengthMultiplier, int frameStartConstruct, int frameEndConstruct, int frameSkipConstruct, int resolutionX, int resolutionY, double zoomFactor) {
    	directoryTextString = ".\\"+name+" text frames"+"\\";
    	directoryImageString = ".\\"+name+" image frames"+"\\";
    	directoryImageNameString = name+" image frames";
    	particleCount = particles;
    	trailLength   = lengthMultiplier;
    	frameStart    = frameStartConstruct;
    	frameEnd      = frameEndConstruct;
    	frameSkip     = frameSkipConstruct;
    	imageSizeX    = resolutionX - resolutionX%2;
    	imageSizeY    = resolutionY - resolutionY%2;
    	zoom          = zoomFactor;
    	createDirectory(directoryImageNameString);	
    }
    public void methodRunner(){
    	picCount=0;
    	trailImage = new BufferedImage(imageSizeX, imageSizeY, BufferedImage.TYPE_INT_RGB );
    	trailGraphics = trailImage.createGraphics();
    	trailGraphics.setColor(Color.black);
    	trailGraphics.fillRect(0,0,imageSizeX,imageSizeY);
    	x = new double[particleCount];
		xOld = new double[particleCount];
		y = new double[particleCount];
		yOld = new double[particleCount];
		m = new double[particleCount];
		x[0]=0;
		for(int frameCount=frameStart;frameCount<=frameEnd;frameCount+=frameSkip){
    		startTime=System.nanoTime();
	    	picCount++;
	    	readTextFile(frameCount);
	    	focus();
	    	drawParticles();
	    	drawTrails();
	    	saveImage(frameCount);
	    	elapsedTime=System.nanoTime()-startTime;
	    	System.out.println("Image "+picCount+" (Frame " + frameCount + ") saved (took " + String.format("%014d", elapsedTime) + " nanoseconds)");
	    	
    	}
    }
    private void readTextFile(int frameCount){
//    	String inputFileName = directoryTextString + String.format("%010d", frameCount) + ".txt";
//        File inputFile = new File(inputFileName);
//        Scanner input = null;
//        try {
//        	input = new Scanner(inputFile);
//        }
//        catch (FileNotFoundException ex){
//        	System.out.println("File "+inputFileName+" not found.");
//        	System.exit(1);
//        }
//        length=0;
//        while (input.hasNextLine()){
//        	input.nextLine();
//        	length++;
//        }
//        input.close();
		length=particleCount;
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
        for(int i = 0; i < particleCount; i++){
        	xOld[i]=x[i];
        	yOld[i]=y[i];
	        x[i]=input.nextDouble();
	        y[i]=input.nextDouble();
	        m[i]=input.nextDouble();
        }
        if (frameCount==1){
        	for(int i = 0; i < particleCount; i++){
	        	xOld[i]=x[i];
	        	yOld[i]=y[i];
        	}
        }
        input.close();
        
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
    	particleImage = new BufferedImage(imageSizeX, imageSizeY, BufferedImage.TYPE_INT_ARGB );
    	particleGraphics = particleImage.createGraphics();
    	particleGraphics.setColor(new Color(0, true));
    	particleGraphics.fillRect(0,0,imageSizeX,imageSizeY);
    	for(int i=0; i<length; i++){
			int dispX = (int)(((x[i]-centerX)*200*zoom)+imageSizeX/2);
			int dispY = (int)(((y[i]-centerY)*200*zoom)+imageSizeY/2);
			int radius = (int)((Math.sqrt(m[i])+0.5)/2*zoom);
			for (int j = dispX-radius; j <= dispX+radius;j++){
				for (int k = dispY-radius; k <= dispY+radius;k++){
					if((j<imageSizeX)&&(k<imageSizeY)&&(j>0)&&(k>0)){
						if (((dispX-j)*(dispX-j)+(dispY-k)*(dispY-k))<=(radius*radius)){
							particleImage.setRGB(j,k, whiteInt);
						}
					}
				}
			}
		}
	}
	private void drawTrails(){
		if (picCount%trailLength==0){
			int testCol = 0;
			for(int i = 0; i<imageSizeX; i++){
				for(int j = 0; j<imageSizeY; j++){
					testCol = trailImage.getRGB(i,j);
					if (testCol!= -16777216){
						int R = (testCol & 255)-frameSkip, G = ((testCol >> 8) & 255)-frameSkip, B = ((testCol >> 16) & 255)-frameSkip, A = ((testCol >> 24) & 255);
						R=Math.max(0,R);
						G=Math.max(0,G);
						B=Math.max(0,B);
						Color c = new Color(R, G, B, A);
						trailImage.setRGB(i, j, c.getRGB());
					}
				}
			}
		}
			
		
		
		trailGraphics.setColor(new Color(127,127,127));
		for(int i = 0; i<length; i++){
			if (x[i]<10000)
				trailGraphics.draw (new Line2D.Double((((x[i]-centerX)*200*zoom)+imageSizeX/2),(((y[i]-centerY)*200*zoom)+imageSizeY/2),(((xOld[i]-centerX)*200*zoom)+imageSizeX/2),(((yOld[i]-centerY)*200*zoom)+imageSizeY/2)));
		}
	}
	private void saveImage(int frameCount){
		BufferedImage outputImage = new BufferedImage(imageSizeX, imageSizeY, BufferedImage.TYPE_INT_ARGB );
		Graphics2D outputGraphics = outputImage.createGraphics();
		outputGraphics.drawImage(trailImage, 0, 0, null);
		outputGraphics.drawImage(particleImage, 0, 0, null);
		try{
            File f = new File(directoryImageString+String.format("%010d", picCount)+".png");
            ImageIO.write(outputImage, "PNG", f);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private void createDirectory(String name){
    	File directory = new File(name);
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