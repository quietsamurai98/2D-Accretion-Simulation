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
        
    /**
     * Creates a new instance of <code>ParticleInteraction</code>.
     */
    
    public ParticleInteraction() {

    }
    
    /**
     * @param args the command line arguments
     */ 
    public static void main(String[] args){
    	

    	
    	
    	
    	Particle[] particleArray = new Particle[0];
    	boolean[] boolArray = new boolean[0];
    	Arrays.fill(boolArray, Boolean.TRUE);
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

//		Open Setting GUI
		SettingsGUI settings = new SettingsGUI();
		settings.prepareGUI();
        settings.createUIComponents();
//	
//		Declare Simulation Display Window and Image
		BufferedImage displayImage = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB );
//    	JFrame frame = new JFrame();
//		ImageIcon icon = new ImageIcon(displayImage);
//		JLabel label = new JLabel(icon);
//		frame.add(label);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.pack();
//		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
//        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
//        int frameX = (int) rect.getMaxX() - frame.getWidth();
//        int frameY = 0;
//        frame.setLocation(frameX, frameY);
//		frame.setVisible(true);
		int blackInt = new Color(0,0,0).getRGB();
		int whiteInt = new Color(255,255,255).getRGB();
		Graphics2D    displayGraphics = displayImage.createGraphics();
//	
//		Declare misc vars
		double collisionDistanceFactor=1;
		boolean saveFrames=true;
		
//		File directory = new File("Simulation frames (particleCount="+particleCount);

		// if the directory does not exist, create it
//		if (saveFrames){
//			if (!directory.exists()) {
//			    boolean result = false;
//			
//			    try{
//			        directory.mkdir();
//			        result = true;
//			    } 
//			    catch(SecurityException se){
//			        //handle it
//			    }        
//			    if(result) {
//			    }
//			}else{
//				//if the directory exists, delete all existing images
//				for(File file: directory.listFiles()) 
//	    			if (!file.isDirectory()) 
//	        	file.delete();
//			}
//		}
		
		int focusOn = 1; //0=origin 1=barycenter 2=largest mass
//	 	
//	
//	
//	
//		
		while(true){
			
			
			
//			Update vars
			particleCount = settings.getSpinner01Value(); 
    		initialMass = settings.getSpinner02Value(); 
    		variationMass = settings.getSpinner03Value(); 
    		diskRadius = settings.getSpinner04Value(); 
    		deltaTime = settings.getSpinner05Value(); 
    		constantGravity = settings.getSpinner06Value(); 
    		variationVel=settings.getSpinner07Value(); 
    		initialSpinFactor=settings.getSpinner08Value(); 
    		centralParticleMass = settings.getSpinner09Value(); 
    		centralParticle = true; //ADD CHECKBOX TO GUI TO SET THIS!
//			Spawn Particles
			particleArray = new Particle[particleCount];
			boolArray = new boolean[particleCount];
    		Arrays.fill(boolArray, Boolean.TRUE);
    		
    		File directory = new File("Simulation frames (particleCount="+particleCount+")");
			if (saveFrames){
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
					for(File file: directory.listFiles()) 
		    			if (!file.isDirectory()) 
		        	file.delete();
				}
			}
    		
    		
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
	        double maxMass=-Double.MAX_VALUE;
        	for(int i=0; i<particleCount;i++){
        		if (particleArray[i].getMass()>maxMass){
        			maxMass=particleArray[i].getMass();
        			maxMassID=i;
        		}
        	}
	        
	        //calculate focus
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
			
			
				
	        displayGraphics.setPaint ( new Color(0,0,0) );
			displayGraphics.fillRect ( 0, 0, displayImage.getWidth(), displayImage.getHeight() );
			settings.setRestartBool(false);
			
			int frameCount=0;
			long startTime = System.nanoTime();
			while(!settings.getRestartBool()){
//	
//				Collide particles
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
//				Calculate gravity btwn particles
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
				
				//			calculate focus
		        if (focusOn==1){  //Full System Barycenter
			        double sumMass=0;
					double sumX=0;
					double sumY=0;
					for(int i=0; i<particleCount;i++){
						if (boolArray[i]){
							sumMass+=particleArray[i].getMass();
							sumX+=particleArray[i].getXPosition()*particleArray[i].getMass();
							sumY+=particleArray[i].getYPosition()*particleArray[i].getMass();
						}
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
				
				//erase old particles
				for(int i=0; i<particleCount; i++){
					if (boolArray[i]){
						int dispX = (int)(((particleArray[i].getXPosition()-centerX)*200)+400);
						int dispY = (int)(((particleArray[i].getYPosition()-centerY)*200)+400);
						int radius = (int)((Math.sqrt(particleArray[i].getMass())+0.5)/2);
//						for (int j = dispX-radius-5; j <= dispX+radius+5;j++){
//							for (int k = dispY-radius-5; k <= dispY+radius+5;k++){
//								if((j<800)&&(k<800)&&(j>0)&&(k>0)){
//										displayImage.setRGB(j,k, blackInt);
//								}
//							}
//						}
						displayGraphics.setPaint ( new Color(0,0,0) );
						displayGraphics.fillRect ( dispX-radius-5, dispY-radius-5, dispX+radius+5, dispY+radius+5);
					}
				}
				
//				Update velocities and positions of particles
				for(int i=0; i<particleCount; i++){
					if (boolArray[i]){
			        particleArray[i].updateVel(deltaTime);
			        particleArray[i].updatePos(deltaTime);
					}
				}
//	
//	
	//			calculate focus
		        if (focusOn==1){  //Full System Barycenter
			        double sumMass=0;
					double sumX=0;
					double sumY=0;
					for(int i=0; i<particleCount;i++){
						if (boolArray[i]){
							sumMass+=particleArray[i].getMass();
							sumX+=particleArray[i].getXPosition()*particleArray[i].getMass();
							sumY+=particleArray[i].getYPosition()*particleArray[i].getMass();
						}
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
				
//				Draw particles
				displayGraphics.setPaint ( new Color(0,0,0) );
				displayGraphics.fillRect ( 0, 0, displayImage.getWidth(), displayImage.getHeight() );
				for(int i=0; i<particleCount; i++){
					if (boolArray[i]){
						int dispX = (int)(((particleArray[i].getXPosition()-centerX)*200)+400);
						int dispY = (int)(((particleArray[i].getYPosition()-centerY)*200)+400);
						int radius = (int)((Math.sqrt(particleArray[i].getMass())+0.5)/2);
						for (int j = dispX-radius-5; j <= dispX+radius+5;j++){
							for (int k = dispY-radius-5; k <= dispY+radius+5;k++){
								if((j<800)&&(k<800)&&(j>0)&&(k>0)){
									if (((dispX-j)*(dispX-j)+(dispY-k)*(dispY-k))<=(radius*radius)){
										displayImage.setRGB(j,k, whiteInt);
									}
								}
							}
						}
					}
				}
//				Update Display window
//				frame.repaint();
				
//				Save frame
				frameCount++;
				try{
		            File f = new File(".\\"+"Simulation frames (particleCount="+particleCount+")"+"\\"+String.format("%010d", frameCount)+".png");
		            ImageIO.write(displayImage, "PNG", f);
		        }
		        catch(Exception e){
		            e.printStackTrace();
		        }
		        
		        System.out.println("Frame "+String.format("%010d", frameCount)+" took " + String.format("%014d", System.nanoTime()-startTime) + " nanoseconds");
		        startTime = System.nanoTime();
			}
		}
    }
}
