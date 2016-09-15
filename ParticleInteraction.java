/**
 * @(#)ParticleInteraction.java
 *
 *
 * @author 
 * @version 1.00 2016/8/24
 */
import java.lang.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.Scanner;
import java.awt.Graphics2D; 
import java.io.*;
import javax.imageio.*;
public class ParticleInteraction {

    Particle[] particleArray = new Particle[0];
	boolean[] boolArray = new boolean[0];
    int particleCount;
	double initialMass;
    double variationMass;
   	double diskRadius;
   	double deltaTime; //timestep in seconds
   	double constantGravity; //gravitational constant
   	double variationVel;
   	double initialSpinFactor;
   	double variationSpin;
   	double centralParticleMass;
   	boolean centralParticle;
   	double centerX=0;
   	double centerY=0;
   	int maxMassID=0;
   	double maxMass;
	int frameCount;
	int frameCap;
	
	long startTime;
	
	long startTimeTest;
	long collide, gravity, focus, erase, move, draw, save, timePrint;

//	
//		Declare Simulation Display Image
	BufferedImage displayImage = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB );
	int blackInt = new Color(0,0,0).getRGB();
	int whiteInt = new Color(255,255,255).getRGB();
	Graphics2D    displayGraphics = displayImage.createGraphics();
//	
//		Declare misc vars
	double collisionDistanceFactor=1;
	boolean saveFrames=true;
	int focusOn = 1; //0=origin 1=barycenter 2=largest mass
    
    public ParticleInteraction() {
    	frameCap=10;
		frameCount=0;
		particleCount=1;
		simulate();
    }
    
    /**
     * @param args the command line arguments
     */ 
    public void simulate(){
		while(true){
			updateVars();
			createDirectory();
			spawnParticles();
	        updateFocus();
	        wipeImage();
	        frameCount=0;
			startTime = System.nanoTime();
			while(frameCount<frameCap){
				startTimeTest = System.nanoTime();
				collideParticles();
				collide += System.nanoTime()-startTimeTest;
				
				startTimeTest = System.nanoTime();
				calculateGrav();
				gravity += System.nanoTime()-startTimeTest;
				 
				startTimeTest = System.nanoTime();
		        updateFocus();
		        focus += System.nanoTime()-startTimeTest;
				 
				startTimeTest = System.nanoTime();
				eraseParticles();
				erase += System.nanoTime()-startTimeTest;
				 
				startTimeTest = System.nanoTime();
				updateVelAndPos();
				move += System.nanoTime()-startTimeTest;
				 
				startTimeTest = System.nanoTime();
		        updateFocus();
		        focus += System.nanoTime()-startTimeTest;
				 
				startTimeTest = System.nanoTime();
				drawParticles();
				draw += System.nanoTime()-startTimeTest;
				 
				startTimeTest = System.nanoTime();
				saveFrame();
				save += System.nanoTime()-startTimeTest;
				 
	//			startTimeTest = System.nanoTime();
	//	        printTime();
	//	        timePrint += System.nanoTime()-startTimeTest;
			}
			System.out.println(
	        	"Amount of time taken by each method over the course of "+frameCap+" frames, simulating "+particleCount+" particles:\n"+
	        	"collideParticles() = " + String.format("%014d", collide) + " nanoseconds\n"+
	        	"calculateGrav()    = " + String.format("%014d", gravity) + " nanoseconds\n"+
	        	"updateFocus()      = " + String.format("%014d", focus) + " nanoseconds\n"+
	        	"eraseParticles()   = " + String.format("%014d", erase) + " nanoseconds\n"+
	        	"updateVelAndPos()  = " + String.format("%014d", move) + " nanoseconds\n"+
	        	"drawParticles()    = " + String.format("%014d", draw) + " nanoseconds\n"+
	        	"saveFrame()        = " + String.format("%014d", save) + " nanoseconds\n"
	        );
		}
    }
    private void updateVars(){
		particleCount *= 2; 
		initialMass = 1.0; 
		variationMass = 0.5; 
		diskRadius = 1.0; 
		deltaTime = 0.001; 
		constantGravity = 0.001; 
		variationVel=0.000; 
		initialSpinFactor=1.0; 
		centralParticleMass = 100.0; 
		centralParticle = true;
    	
    }
    private void createDirectory(){
    	File directory = new File("Simulation frames (particleCount="+particleCount+")");
		if (saveFrames){
			if (!directory.exists()) {
			    boolean result = false;
			
			    try{
			        directory.mkdir();
			        result = true;
			    } 
			    catch(SecurityException se){
			    }        
			    if(result) {
			    }
			}else{
				//if the directory exists, delete all existing images
				for(File file: directory.listFiles()) 
	    			if (!file.isDirectory()) 
	        	file.delete();
			}
		}
    }
    private void spawnParticles(){
    	particleArray = new Particle[particleCount];
		boolArray = new boolean[particleCount];
		Arrays.fill(boolArray, Boolean.TRUE);    		
        if (centralParticle){
        	particleArray[0]=new Particle(0,0,0,centralParticleMass,0,0,0,0,0,constantGravity,centralParticleMass);
        	for(int i=1; i<particleCount; i++){
        		particleArray[i]=new Particle(diskRadius,0,0,initialMass,variationMass,0,variationVel,initialSpinFactor,0,constantGravity,centralParticleMass+((particleCount-1)*initialMass*0));
        	}
        }
        else{
        	for(int i=0; i<particleCount; i++){
        		particleArray[i]=new Particle(diskRadius,0,0,initialMass,variationMass,0,variationVel,initialSpinFactor,0,constantGravity,initialMass*particleCount);
        	}
        }
        maxMass=-Double.MAX_VALUE;
    	for(int i=0; i<particleCount;i++){
    		if (particleArray[i].getMass()>maxMass){
    			maxMass=particleArray[i].getMass();
    			maxMassID=i;
    		}
    	}
    }
    private void updateFocus(){
    	if (focusOn==1){  //Full System Barycenter
	        double sumMass=0;
			double sumX=0;
			double sumY=0;
			for(int i=0; i<particleCount;i++){
				sumMass+=particleArray[i].getMass();
				sumX+=particleArray[i].getXPosition()*particleArray[i].getMass();
				sumY+=particleArray[i].getYPosition()*particleArray[i].getMass();
			}
			sumX/=sumMass;
			sumY/=sumMass;
			centerX=sumX;
			centerY=sumY;
        }else if(focusOn==2){  //Largest Particle
        	centerX=particleArray[maxMassID].getXPosition();
			centerY=particleArray[maxMassID].getYPosition();
        }else{ //Origin
        	centerX=0;
        	centerY=0;
        }
    }
    private void wipeImage(){
    	displayGraphics.setPaint ( new Color(0,0,0) );
		displayGraphics.fillRect ( 0, 0, displayImage.getWidth(), displayImage.getHeight() );
    }
    private void eraseParticles(){
		for(int i=0; i<particleCount; i++){
			if (boolArray[i]){
				int dispX = (int)(((particleArray[i].getXPosition()-centerX)*200)+400);
				int dispY = (int)(((particleArray[i].getYPosition()-centerY)*200)+400);
				int radius = (int)((Math.sqrt(particleArray[i].getMass())+0.5)/2);
				displayGraphics.setPaint ( new Color(0,0,0) );
				displayGraphics.fillRect ( dispX-radius-5, dispY-radius-5, dispX+radius+5, dispY+radius+5);
			}
		}
    }
    private void updateVelAndPos(){
    	for(int i=0; i<particleCount; i++){
			if (boolArray[i]){
		        particleArray[i].updateVel(deltaTime);
		        particleArray[i].updatePos(deltaTime);
			}
		}
    }
    private void drawParticles(){
    	for(int i=0; i<particleCount; i++){
			if (boolArray[i]){
				int dispX = (int)(((particleArray[i].getXPosition()-centerX)*200)+400);
				int dispY = (int)(((particleArray[i].getYPosition()-centerY)*200)+400);
				int radius = (int)((Math.sqrt(particleArray[i].getMass())+0.5)/2);
				for (int j = dispX-radius-5; j <= dispX+radius+5;j++){
					for (int k = dispY-radius; k <= dispY+radius;k++){
						if((j<800)&&(k<800)&&(j>0)&&(k>0)){
							if (((dispX-j)*(dispX-j)+(dispY-k)*(dispY-k))<=(radius*radius)){
								displayImage.setRGB(j,k, whiteInt);
							}
						}
					}
				}
			}
		}
    }
    private void saveFrame(){
    	frameCount++;
		try{
            File f = new File(".\\"+"Simulation frames (particleCount="+particleCount+")"+"\\"+String.format("%010d", frameCount)+".png");
            ImageIO.write(displayImage, "PNG", f);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private void printTime(){
    	System.out.println("Frame "+String.format("%010d", frameCount)+" took " + String.format("%014d", System.nanoTime()-startTime) + " nanoseconds");
		startTime = System.nanoTime();
    }
    private void calculateGrav(){
    	for(int i=0; i<particleCount; i++){
			particleArray[i].setXForce(0.0);
			particleArray[i].setYForce(0.0);
			if (boolArray[i]){
				for(int j=0; j<particleCount; j++){
					if(i!=j&&boolArray[j]){
						double angle = Math.atan2(particleArray[i].getYPosition()-particleArray[j].getYPosition(),particleArray[i].getXPosition()-particleArray[j].getXPosition());
						double forceTotal = constantGravity*particleArray[i].getMass()*particleArray[j].getMass()/(Math.pow((particleArray[i].getXPosition()-particleArray[j].getXPosition()),2.0)+Math.pow((particleArray[i].getYPosition()-particleArray[j].getYPosition()),2.0));
						particleArray[i].updateForceXY(angle, forceTotal);
					}
				}
			}
		}
    }
    private void collideParticles(){
    	for(int i=0; i<particleCount; i++){
			if (boolArray[i]){
	    		for(int j=0; j<particleCount; j++){
	    			if(i!=j&&boolArray[j]){
	    				double minDist=Math.pow((((Math.sqrt(particleArray[i].getMass())+0.5)/2.0)+((Math.sqrt(particleArray[j].getMass())+0.5)/2.0))*collisionDistanceFactor,2);
	    				if (Math.pow((int)((particleArray[i].getXPosition()*200)+400)-(int)((particleArray[j].getXPosition()*200)+400),2)+Math.pow((int)((particleArray[i].getYPosition()*200)+400)-(int)((particleArray[j].getYPosition()*200)+400),2)<=minDist){
	  		  				particleArray[i].setXVelocity(((particleArray[i].getMass()*particleArray[i].getXVelocity())+(particleArray[j].getMass()*particleArray[j].getXVelocity()))/(particleArray[i].getMass()+particleArray[j].getMass()));
	    					particleArray[i].setYVelocity(((particleArray[i].getMass()*particleArray[i].getYVelocity())+(particleArray[j].getMass()*particleArray[j].getYVelocity()))/(particleArray[i].getMass()+particleArray[j].getMass()));
	    					particleArray[i].setXPosition(((particleArray[i].getMass()*particleArray[i].getXPosition())+(particleArray[j].getMass()*particleArray[j].getXPosition()))/(particleArray[i].getMass()+particleArray[j].getMass()));
	    					particleArray[i].setYPosition(((particleArray[i].getMass()*particleArray[i].getYPosition())+(particleArray[j].getMass()*particleArray[j].getYPosition()))/(particleArray[i].getMass()+particleArray[j].getMass()));
	    					particleArray[i].setMass(particleArray[i].getMass()+particleArray[j].getMass());
							particleArray[j].setXPosition(1000000.0*(Math.random()+1.0));
							particleArray[j].setYPosition(1000000.0*(Math.random()+1.0));
							particleArray[j].setMass(0.000000001);
							boolArray[j]=false;
							maxMass=-Double.MAX_VALUE;
				        	for(int k=0; k<particleCount;k++){
				        		if (boolArray[k]&&particleArray[k].getMass()>maxMass){
				        			maxMass=particleArray[k].getMass();
				        			maxMassID=k;
				        		}
				        	}
				        	displayGraphics.setPaint ( new Color(0,0,0) );
							displayGraphics.fillRect ( 0, 0, displayImage.getWidth(), displayImage.getHeight() );
						}
					}
				}
			}
		}
    }
}
