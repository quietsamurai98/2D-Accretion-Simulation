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
public class ParticleInteraction {
        
    /**
     * Creates a new instance of <code>ParticleInteraction</code>.
     */
    public ParticleInteraction() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	//Create display
    	BufferedImage displayImage = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB );
    	JFrame frame = new JFrame();
		ImageIcon icon = new ImageIcon(displayImage);
		JLabel label = new JLabel(icon);
		frame.add(label);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        int frameX = (int) rect.getMaxX() - frame.getWidth();
        int frameY = 0;
        frame.setLocation(frameX, frameY);
		frame.setVisible(true);
		int Black = new Color(0,0,0).getRGB();
		int White = new Color(255,255,255).getRGB();
		int NearWhite = new Color(254,254,254).getRGB();
		int Gray = new Color(64,64,64).getRGB();
		int Red = new Color(255,0,0).getRGB();
		int PinkWhite = new Color(255,254,254).getRGB();
		Graphics2D    graphics = displayImage.createGraphics();

		
		long startNano=0;
	    long elapsedNano=0;
	    
	    
	        	
		//Create GUI
		MyGUI testGUI = new MyGUI();
        testGUI.createUIComponents();
		
		Particle[] particleArray;
		
    	//Set simulation parameters for a non accretion system
//    	int particleCount = 3;
//	    double initialMass = 10.0;
//    	double variationMass = 9.9;
//    	double xOrigin = 0.0;
//	 	double yOrigin = 0.0;
//    	double diskRadius = 0.5;
//    	double deltaTime = 0.001; //timestep in seconds
//    	double constantGravity=0.001; //gravitational constant
//    	double initialVel=0.0;
//    	double variationVel=0.000;
//    	double initialSpinFactor=0.5;
//    	double variationSpin=0.00;
//    	double centralParticleMass = 100.0;
//    	boolean drawTrails=true;
//    	boolean centralParticle=false;
//    	boolean enableCollisions=true;
//    	boolean maintainParticles=false;
//    	double collisionDistFactor=1;
    	
    	//set simulation parameters for an accretion system
    	int particleCount = 100;
    	double initialMass = 1.0;
    	double variationMass = 0.9;
    	double xOrigin = 0.0;
    	double yOrigin = 0.0;
    	double diskRadius = 1.5;
    	double deltaTime = 0.001; //timestep in seconds
    	double constantGravity=0.001; //gravitational constant
    	double initialVel=0.0;
    	double variationVel=0.000;
    	double initialSpinFactor=1.0;
    	double variationSpin=0.00;
    	double centralParticleMass = 100.0;
    	boolean drawTrails=false;
    	boolean centralParticle=true;
    	boolean enableCollisions=true;
    	boolean maintainParticles=true;
    	double collisionDistFactor=1.0;
    	boolean focusBary=true;
    	boolean drawBary=false;
    	int outOfBounds = 1000;
    	double maxMass = 10000.0;
    	int fpsCap=120;
    	while (true){
    		particleCount = testGUI.getSpinner01Value(); //System.out.println(particleCount);
	    	initialMass = testGUI.getSpinner02Value(); //System.out.println(initialMass);
	    	variationMass = testGUI.getSpinner03Value(); //System.out.println(variationMass);
	    	diskRadius = testGUI.getSpinner04Value(); //System.out.println(diskRadius);
	    	deltaTime = testGUI.getSpinner05Value(); //System.out.println(deltaTime);//timestep in seconds 
	    	constantGravity = testGUI.getSpinner06Value(); //System.out.println(constantGravity);//gravitational constant
	    	variationVel=testGUI.getSpinner07Value(); //System.out.println(variationVel);
	    	initialSpinFactor=testGUI.getSpinner08Value(); //System.out.println(initialSpinFactor);
	    	centralParticleMass = testGUI.getSpinner09Value(); //System.out.println(centralParticleMass);
//   	    fpsCap=60;
//	    	drawTrails=false;
//	    	centralParticle=true;
//	    	enableCollisions=true;
//	    	maintainParticles=true;
//	    	collisionDistFactor=1.0;
//	    	focusBary=true;
//	    	drawBary=false;
//	    	outOfBounds = 1000;
//	    	maxMass = 10000.0;


			long fpsNano=(1000000000/fpsCap);
//    		long fpsNano=16666666;
    		
    		
    		
    		
    		
    		//System.out.println(testGUI.getRestartBool());
	        //Create array of particles and place them in the disk, next 12 lines
	        particleArray = new Particle[particleCount];
	        if (centralParticle){
	        	particleArray[0]=new Particle(0,xOrigin,yOrigin,centralParticleMass,0.0*variationMass,0.0*initialVel,0.0*variationVel,0.0*initialSpinFactor,0.0*variationSpin,constantGravity,centralParticleMass);
	        	for(int i=1; i<particleCount; i++){
	        		particleArray[i]=new Particle(diskRadius,xOrigin,yOrigin,initialMass,variationMass,initialVel,variationVel,initialSpinFactor,variationSpin,constantGravity,centralParticleMass+((particleCount-1)*initialMass*0));
	        	}
	        }
	        else{
	        	for(int i=0; i<particleCount; i++){
	        		particleArray[i]=new Particle(diskRadius,xOrigin,yOrigin,initialMass,variationMass,initialVel,variationVel,initialSpinFactor,variationSpin,constantGravity,initialMass*particleCount);
	        	}
	        }
	        double baryX=0.0;
	        double baryY=0.0;
	        double baryMass=0.0;
	        boolean wipeScreen=false;
	        int frameCount = 0;
	        startNano=System.nanoTime();
	        while(!testGUI.getRestartBool()){
	        	elapsedNano=0;
	        	if (enableCollisions){
		        	for(int i=0; i<particleCount; i++){
		        		for(int j=0; j<particleCount; j++){
		        			if(i!=j){
		        				double minDist=(((Math.sqrt(particleArray[i].getMass())+0.5)/2.0)+((Math.sqrt(particleArray[j].getMass())+0.5)/2.0))*collisionDistFactor;
		        				if (Math.sqrt(Math.pow((int)((particleArray[i].getXPosition()*200)+400)-(int)((particleArray[j].getXPosition()*200)+400),2)+Math.pow((int)((particleArray[i].getYPosition()*200)+400)-(int)((particleArray[j].getYPosition()*200)+400),2))<=minDist){
		      		  				particleArray[i].setXVelocity(((particleArray[i].getMass()*particleArray[i].getXVelocity())+(particleArray[j].getMass()*particleArray[j].getXVelocity()))/(particleArray[i].getMass()+particleArray[j].getMass()));
		        					particleArray[i].setYVelocity(((particleArray[i].getMass()*particleArray[i].getYVelocity())+(particleArray[j].getMass()*particleArray[j].getYVelocity()))/(particleArray[i].getMass()+particleArray[j].getMass()));
		        					particleArray[i].setXPosition(((particleArray[i].getMass()*particleArray[i].getXPosition())+(particleArray[j].getMass()*particleArray[j].getXPosition()))/(particleArray[i].getMass()+particleArray[j].getMass()));
		        					particleArray[i].setYPosition(((particleArray[i].getMass()*particleArray[i].getYPosition())+(particleArray[j].getMass()*particleArray[j].getYPosition()))/(particleArray[i].getMass()+particleArray[j].getMass()));
		        					particleArray[i].setMass(particleArray[i].getMass()+particleArray[j].getMass());
		        					if (maintainParticles){
		        						baryX = 0.0;
		        						baryY = 0.0;
		        						baryMass = 0.0;
		        						for(int k=0; k<particleCount; k++){
		        							if(k!=j){
		        								baryX += particleArray[k].getXPosition()*particleArray[k].getMass();
		        								baryY += particleArray[k].getYPosition()*particleArray[k].getMass();
		        								baryMass += particleArray[k].getMass();
		        							}
		        						}
		        						baryX /= baryMass;
		        						baryY /= baryMass;
		        						
		        						if (baryMass<maxMass){
		        							particleArray[j]=new Particle(diskRadius,baryX,baryY,initialMass,variationMass,initialVel,variationVel,initialSpinFactor,variationSpin,constantGravity,baryMass/2.0);
		        							wipeScreen=focusBary;
		        						}else{
		        							particleArray[j].setXPosition(1000000.0*(Math.random()+1.0));
		        							particleArray[j].setYPosition(1000000.0*(Math.random()+1.0));
		        							particleArray[j].setMass(0.0000000000001);
		        						}
		        						//System.out.println("System mass: "+baryMass);
		        						//particleArray[j]=new Particle(diskRadius,baryX,baryY,initialMass,variationMass,initialVel,variationVel,initialSpinFactor,variationSpin,constantGravity,baryMass/2.0);
		        						
		        					}else{
		        						particleArray[j].setXPosition(1000000.0*(Math.random()+1.0));
		        						particleArray[j].setYPosition(1000000.0*(Math.random()+1.0));
		        						particleArray[j].setMass(0.0000000000001);
		        						//wipeScreen=true;
		        					}
		        					
		        					
		        					
		        				}
		        			}
		        		}
		        	}
	        	}
	        	//Calculate total force on particle, next 10 lines
	        	//int[][] forceXArray = new double[particleCount][particleCount];
	        	//int[][] forceYArray = new double[particleCount][particleCount];
	        	for(int i=0; i<particleCount; i++){
	        		particleArray[i].setXForce(0.0);
	        		particleArray[i].setYForce(0.0);
	        		for(int j=0; j<particleCount; j++){
	        			if(i!=j){
	        				double angle = Math.atan2(particleArray[i].getYPosition()-particleArray[j].getYPosition(),particleArray[i].getXPosition()-particleArray[j].getXPosition());
	        				double forceTotal = constantGravity*particleArray[i].getMass()*particleArray[j].getMass()/(Math.pow((particleArray[i].getXPosition()-particleArray[j].getXPosition()),2.0)+Math.pow((particleArray[i].getYPosition()-particleArray[j].getYPosition()),2.0));
	        				particleArray[i].updateForceXY(angle, forceTotal);
	        			}
	        		}
	        	}
	        	//Wipe screen or gray out trails
	        	if (drawTrails){
	        		if (focusBary||drawBary){
		    			baryX = 0.0;
						baryY = 0.0;
						baryMass = 0.0;
						for(int k=0; k<particleCount; k++){
							baryX += particleArray[k].getXPosition()*particleArray[k].getMass();
							baryY += particleArray[k].getYPosition()*particleArray[k].getMass();
							baryMass += particleArray[k].getMass();
						}
						baryX /= baryMass;
						baryY /= baryMass;
	        		}
	        		if(drawBary){
	        			if(focusBary){
	        				displayImage.setRGB(400,400,Red);	
	        			}else{
	        				int j = (int)(baryX*200.0)+400;
	        				int k = (int)(baryY*200.0)+400;
	        				if ((j<800)&&(k<800)&&(j>0)&&(k>0)){
	        					displayImage.setRGB(j,k, Red);
	        				}
	        			}
	        			
	        		}
		    		if(!focusBary){
		    			baryX=0.0;
		    			baryY=0.0;
		    		}
	        		for(int i=0; i<particleCount; i++){
		        		int dispX = (int)(((particleArray[i].getXPosition()-baryX)*200)+400);
						int dispY = (int)(((particleArray[i].getYPosition()-baryY)*200)+400);
						if ((dispX<800)&&(dispY<800)&&(dispX>0)&&(dispY>0)){
							displayImage.setRGB(dispX,dispY, Gray);
						}
						int radius = (int)((Math.sqrt(particleArray[i].getMass())+0.5)/2);
						for (int j = dispX-radius*2-5; j <= dispX+radius*2+5;j++){
							for (int k = dispY-radius*2-5; k <= dispY+radius*2+5;k++){
								if ((j<800)&&(k<800)&&(j>0)&&(k>0)){
									int red = (displayImage.getRGB(j,k) >> 16) & 0xff;
	    							int green = (displayImage.getRGB(j,k) >> 8) & 0xff;
	    							int blue = (displayImage.getRGB(j,k)) & 0xff;
									if ((red+green+blue)==765){
										displayImage.setRGB(j,k, Black);
									}
									else{
										if((red+green+blue)==762){
											displayImage.setRGB(j,k, Gray);
										}
										if((red+green+blue)==763){
											displayImage.setRGB(j,k, Red);
										}
									}
								}
							}
						}
	        		}
	        	}
	        	else{
	        		if (focusBary||drawBary){
		    			baryX = 0.0;
						baryY = 0.0;
						baryMass = 0.0;
						for(int k=0; k<particleCount; k++){
							baryX += particleArray[k].getXPosition()*particleArray[k].getMass();
							baryY += particleArray[k].getYPosition()*particleArray[k].getMass();
							baryMass += particleArray[k].getMass();
						}
						baryX /= baryMass;
						baryY /= baryMass;
	        		}
	        		if(drawBary){
	        			if(focusBary){
	        				displayImage.setRGB(400,400,Red);	
	        			}else{
	        				int j = (int)(baryX*200.0)+400;
	        				int k = (int)(baryY*200.0)+400;
	        				if ((j<800)&&(k<800)&&(j>0)&&(k>0)){
	        					displayImage.setRGB(j,k, Red);
	        				}
	        			}
	        		}
		    		if(!focusBary){
		    			baryX=0.0;
		    			baryY=0.0;
		    		}
	        		for(int i=0; i<particleCount; i++){
		        		int dispX = (int)(((particleArray[i].getXPosition()-baryX)*200)+400);
						int dispY = (int)(((particleArray[i].getYPosition()-baryY)*200)+400);
						int radius = (int)((Math.sqrt(particleArray[i].getMass())+0.5)/2);
						for (int j = dispX-radius*2-5; j <= dispX+radius*2+5;j++){
							for (int k = dispY-radius*2-5; k <= dispY+radius*2+5;k++){
								if ((j<800)&&(k<800)&&(j>0)&&(k>0)){
									int red = (displayImage.getRGB(j,k) >> 16) & 0xff;
	    							int green = (displayImage.getRGB(j,k) >> 8) & 0xff;
	    							int blue = (displayImage.getRGB(j,k)) & 0xff;
									if ((red+green+blue)==765){
										displayImage.setRGB(j,k, Black);
									}
									if ((red+green+blue)==763){
										displayImage.setRGB(j,k, Red);
									}
								}
							}
						}
	        		}
	       			//for(int r=0;r<800;r++){
					//	for(int c=0;c<800;c++){
					//		displayImage.setRGB(r,c, Black);
					//	}
					//}
	       		}
	        	//Update velocities and positions
	        	if (focusBary){
	    			baryX = 0.0;
					baryY = 0.0;
					baryMass = 0.0;
					for(int k=0; k<particleCount; k++){
						baryX += particleArray[k].getXPosition()*particleArray[k].getMass();
						baryY += particleArray[k].getYPosition()*particleArray[k].getMass();
						baryMass += particleArray[k].getMass();
					}
					baryX /= baryMass;
					baryY /= baryMass;
	    		}else{
	    			baryX=0.0;
	    			baryY=0.0;
	    		}
	        	for(int i=0; i<particleCount; i++){
	        		particleArray[i].updateVel(deltaTime);
	        		particleArray[i].updatePos(deltaTime);
	        		
	        		//Draw bodies
	        		
	        		int dispX = (int)(((particleArray[i].getXPosition()-baryX)*200)+400);
					int dispY = (int)(((particleArray[i].getYPosition()-baryY)*200)+400);
					int radius = (int)((Math.sqrt(particleArray[i].getMass())+0.5)/2);
					for (int j = dispX-radius; j <= dispX+radius;j++){
						for (int k = dispY-radius; k <= dispY+radius;k++){
							if ((((dispX-j)*(dispX-j)+(dispY-k)*(dispY-k))<=(radius*radius))&&(j<800)&&(k<800)&&(j>0)&&(k>0)){
								int red = (displayImage.getRGB(j,k) >> 16) & 0xff;
	    						int green = (displayImage.getRGB(j,k) >> 8) & 0xff;
	    						int blue = (displayImage.getRGB(j,k)) & 0xff;
								if ((red+green+blue)==0){
									displayImage.setRGB(j,k, White);
								}
								else{
									if((red+green+blue)==192){
										displayImage.setRGB(j,k, NearWhite);
									}
									if((red+green+blue)==255){
											displayImage.setRGB(j,k, PinkWhite);
									}
								}
							} else {
								if (maintainParticles&&((j>800+outOfBounds)||(k>800+outOfBounds)||(j<0-outOfBounds)||(k<0-outOfBounds))){
		        					baryX = 0.0;
		        					baryY = 0.0;
		        					baryMass = 0.0;
		        					for(int l=0; l<particleCount; l++){
		        						if(l!=i){
		        							baryX += particleArray[l].getXPosition()*particleArray[l].getMass();
		        							baryY += particleArray[l].getYPosition()*particleArray[l].getMass();
		        							baryMass += particleArray[l].getMass();
		        						}
		        					}
		        					baryX /= baryMass;
		        					baryY /= baryMass;
		        					if (baryMass<maxMass){
	        							particleArray[i]=new Particle(diskRadius,baryX,baryY,initialMass,variationMass,initialVel,variationVel,initialSpinFactor,variationSpin,constantGravity,baryMass/2.0);
	        							
	        						}else{
	        							particleArray[i].setXPosition(1000000.0*(Math.random()+1.0));
	        							particleArray[i].setYPosition(1000000.0*(Math.random()+1.0));
	        							particleArray[i].setMass(0.0000000000001);
	        						}
		        					wipeScreen=true;
	        					}
							}
						}
					}
	       		}
	       		
//				try {
//	   				Thread.sleep(16,666666);
//	   			} catch (Exception e) {
//	   				System.out.println(e);
//	   			}
				elapsedNano = System.nanoTime()-startNano;
				if (elapsedNano<fpsNano){
					long milli = (fpsNano-elapsedNano)/1000000;
					int nano = (int)((fpsNano-elapsedNano)%1000000);
		   			try {
		   				Thread.sleep(milli,nano);
		   			} catch (Exception e) {
		   				System.out.println(e);
		   			}
				}
				startNano=System.nanoTime();
	   			frame.repaint();
	   			
	   			if (wipeScreen){
		   			graphics.setPaint ( new Color(0,0,0) );
					graphics.fillRect ( 0, 0, displayImage.getWidth(), displayImage.getHeight() );
	   			}
	   			
	   			wipeScreen=false;
	   			//System.out.println(++frameCount+"  og");
	        }
	    	testGUI.setRestartBool(false);
	    	graphics.setPaint ( new Color(0,0,0) );
			graphics.fillRect ( 0, 0, displayImage.getWidth(), displayImage.getHeight() );
    	
    	//Particle[] particleArray = new Particle[particleCount];
    	}
    }
}