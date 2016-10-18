/**
 * @(#)Render.java
 *
 *
 * @author 
 * @version 1.00 2016/9/16
 */

import java.util.Scanner;
import java.util.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import javax.imageio.*;
public class RenderEnergyParallel{
	
	
	
	//int plotRes = 1;
	int threadRes = 120;
	
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
	BufferedImage energyImage;
	int blackInt = new Color(0,0,0).getRGB();
	int whiteInt = new Color(255,255,255).getRGB();
	Graphics2D energyGraphics;
	String directoryTextString;
	String directoryImageString;
	String directoryImageNameString;
	int particleCount;
	int trailLength;
	double minMass;
	int frameStart;
	int frameSkip;
	int frameCount;
	int picCount;
	double saveOn;
	ArrayList<Integer> colorMap;
	double zoom;
	BufferedReader input;
	String dataString;
	long startTime, elapsedTime;
	File inputFile;
	FileReader inputFW;
	
	Thread ImageSaver;
	double incrementer;
	
	static final double base = 1.1; //Log base for contour color
	static final double baseLog = 1.0/Math.log(base); // used for contour color
	
	double[][] PotentialEnergyArray;
	Thread[][] CalculationThreads;
	
    public RenderEnergyParallel(String name, int particles, int lengthMultiplier, double min, int frameStartConstruct, int frameSkipConstruct, int resolutionX, int resolutionY, double zoomFactor) {
    	directoryImageString = ".\\"+name+"\\energy image frames\\";
    	directoryImageNameString = name+"\\energy image frames";
    	particleCount = particles;
    	trailLength   = lengthMultiplier;
    	minMass		  = min;
    	frameStart    = frameStartConstruct;
    	frameSkip     = 1;
    	incrementer   = frameSkipConstruct;
    	imageSizeX    = resolutionX - resolutionX%2;
    	imageSizeY    = resolutionY - resolutionY%2;
    	zoom          = zoomFactor;
    	createDirectory(name);	
    }
    public void methodRunner() throws IOException{
    	generateColorMap();
    	length=particleCount;
    	x = new double[particleCount];
		xOld = new double[particleCount];
		y = new double[particleCount];
		yOld = new double[particleCount];
		m = new double[particleCount];
		x[0]=0;
		frameCount = 0;
		picCount = 0;
		saveOn = 1;
		for (int i=1; i<frameStart; i++){
			input.readLine();
			frameCount++;
		}
		while((dataString = input.readLine()) != null){
				frameCount++;
	    		startTime=System.nanoTime();
		    	readTextFile(dataString);
		    	if ((int)saveOn==frameCount){
		    		focus();
		    		drawEnergy();
		    		saveImage();
		    	}
    	}
    	if (dataString == null){
    		System.out.println("Reached a null");
    	}
    }
    private void readTextFile(String dataString){
        Scanner inputScanner = new Scanner(dataString);
        int j = 0;
        String temp = "";
        while(inputScanner.hasNext()){
        	temp = inputScanner.next();
        	xOld[j]=x[j];
        	yOld[j]=y[j];
        	if (temp.equals("SKIP")){
        		x[j]=10001;
	        	y[j]=10001;
	        	m[j]=0;
        	} else {
        		x[j]=Double.parseDouble(temp);
	        	y[j]=inputScanner.nextDouble();
	        	m[j]=inputScanner.nextDouble();
        	}
	        
	        j++;
        }
        if (picCount==0){
        	for(int i = 0; i < particleCount; i++){
	        	xOld[i]=x[i];
	        	yOld[i]=y[i];
        	}
        }
        inputScanner.close();
        
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
        	double max = Double.MIN_VALUE;
        	for(int i=0; i<length;i++){
        		if (m[i]>max){
        			max=m[i];
        			centerX=x[i];
        			centerY=y[i];
        		}
        	}
        }else{ //Origin
        	centerX=0;
        	centerY=0;
        }
    }
    
	private void drawEnergy(){
		energyImage = new BufferedImage(imageSizeX, imageSizeY, BufferedImage.TYPE_INT_RGB );
		energyGraphics = energyImage.createGraphics();
		CalculationThreads = new Thread[imageSizeX/threadRes][imageSizeY/threadRes];
		for(int pxlX=0; pxlX<CalculationThreads.length; pxlX+=1){
    		for(int pxlY=0; pxlY<CalculationThreads[pxlX].length; pxlY+=1){
    			CalculationThreads[pxlX][pxlY] = new Thread(){
					public void run(){
						int nameX = Integer.parseInt(getName().substring(0,4));
						int nameY = Integer.parseInt(getName().substring(4,8));
						for(int offsetX=0; offsetX<threadRes; offsetX++){
							for(int offsetY=0; offsetY<threadRes; offsetY++){
								double truX = ((nameX+offsetX-(imageSizeX/2.0))/(200.0*zoom))+centerX;
								double truY = ((nameY+offsetY-(imageSizeY/2.0))/(200.0*zoom))+centerY;
								double sum = 0;
								for(int i=0; i<length; i++){
									if(m[i]!=0){
										sum+=m[i]/Math.sqrt((truX-x[i])*(truX-x[i])+(truY-y[i])*(truY-y[i]));
									}
								}
								int intensity = (int)(6*sum/100);  // sum/100
		    					intensity = intensity%colorMap.size();
		    					energyImage.setRGB(nameX+offsetX,nameY+offsetY,(int)colorMap.get(intensity));
							}
						}
					}
				};
			    CalculationThreads[pxlX][pxlY].setName(String.format("%04d",pxlX*threadRes)+String.format("%04d",pxlY*threadRes));
    			CalculationThreads[pxlX][pxlY].start();
    		}
		}
		for(int pxlX=0; pxlX<CalculationThreads.length; pxlX+=1){
    		for(int pxlY=0; pxlY<CalculationThreads[pxlX].length; pxlY+=1){
    			try{
					CalculationThreads[pxlX][pxlY].join();
				}
		    	catch (InterruptedException e){
		    		e.printStackTrace();
		    	}
    		}
		}
	}
	private void saveImage(){
		if ((int)saveOn==frameCount){
			if (ImageSaver!=null){
				try{
					ImageSaver.join();
				}
		    	catch (InterruptedException e){
		    		e.printStackTrace();
		    	}
			}
			BufferedImage outputImage = new BufferedImage(imageSizeX, imageSizeY, BufferedImage.TYPE_INT_ARGB );
			Graphics2D outputGraphics = outputImage.createGraphics();
			outputGraphics.drawImage(energyImage, 0, 0, null);
	        ImageSaver = new Thread(){
	    		public void run(){
					File f = null;
					try{
			            f = new File(directoryImageString+String.format("%010d", picCount)+".PNG");
			            ImageIO.write(outputImage, "PNG", f);
			        }
			        catch(Exception e){
			        	System.out.println(f.getAbsolutePath() + "\nFAILURE TO SAVE IMAGE");
			            e.printStackTrace();
			        }
				}
	    	};
	    	ImageSaver.start();
	    	picCount++;
	    	saveOn+=incrementer;
	    	incrementer+=0.0000; //0.0025;
	    	elapsedTime=System.nanoTime()-startTime;
		    System.out.println("Image "+picCount+" (Frame " + frameCount + ") saved (took " + String.format("%014d", elapsedTime) + " nanoseconds)");
		}
    }
    private void createDirectory(String name){
    	File directory = new File(directoryImageNameString);
		if (!directory.exists()) {
		    boolean result = false;
		
		    try{
		        directory.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se){
		        System.out.println(directory.getAbsolutePath() + "\nFAILURE TO MAKE DIRECTORY");
        		se.printStackTrace();
		    }        
		    if(result) {
		    	System.out.println("Directory created!");
		    }
		}else{
			//if the directory exists, delete all existing images
			System.out.println("Clearing directory");
			for(File file: directory.listFiles()) 
    			if (!file.isDirectory()) 
        			file.delete();
        	System.out.println("Directory cleared!");
		}
		String inputFileName = ".\\" + name + "\\raw data.txt";
        inputFile = new File(inputFileName);
        input = null;
        try {
        	inputFW = new FileReader (inputFile);
        }
        catch (IOException e){
        	System.out.println(inputFile.getAbsolutePath() + "\nFAILURE TO MAKE FileReader");
        	e.printStackTrace();
        }
        input = new BufferedReader(inputFW);

	}
	
	private void generateColorMap(){
		colorMap = new ArrayList<Integer>();
		colorMap.clear();
//		for (int i = 255; i>=0; i--){
//			colorMap.add(new Color(i,i,i).getRGB());
//		}
		
		int[] GRB = {0,255,0};
		int delta = 1;
		for(int loop=0; loop<2;loop++){
			for(int GRBi=0; GRBi<3; GRBi++){
				for(int i=0; i<255; i++){
					GRB[GRBi]+=delta;
					colorMap.add(new Color(GRB[1],GRB[0],GRB[2]).getRGB());
				}
				delta*=-1;
			}
		}
	}
    
}