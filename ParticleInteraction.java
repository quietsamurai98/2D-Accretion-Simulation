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
		
    	//Set simulation parameters
    	int particleCount = 10;
    	double startingMass = 10.0;
    	double variationMass = 9.9;
    	double xOrigin = 0.0;
    	double yOrigin = 0.0;
    	double diskRadius = 1.5;
    	double deltaTime = 0.001; //timestep in seconds
    	double constantGravity=0.001; //gravitational constant
    	double initialVel=0.0;
    	double variationVel=0.000;
    	double initialSpin=0.2;
    	double variationSpin=0.00;
    	double centralParticleMass = 100.0;
    	boolean drawTrails=false;
    	boolean centralParticle=true;
    	
    	
    	Scanner kb = new Scanner(System.in);
    	 System.out.print("Use default settings? (Y/N):");
    	if (kb.next().equalsIgnoreCase("N")){
	    	System.out.print("Particle Count (default "+particleCount+"):");
	    	particleCount = kb.nextInt();
	    	System.out.print("Starting mass (default "+startingMass+"):");
	    	startingMass = kb.nextDouble();
	    	System.out.print("Variation mass (default "+variationMass+"):");
	    	variationMass = kb.nextDouble();
	    	System.out.print("X origin (default "+xOrigin+"):");
	    	xOrigin = kb.nextDouble();
	    	System.out.print("Y origin (default "+yOrigin+"):");
	    	yOrigin = kb.nextDouble();
	    	System.out.print("Initial disk radius "+diskRadius+":");
	    	diskRadius = kb.nextDouble();
	    	System.out.print("Time step (default "+deltaTime+"):");
	    	deltaTime = kb.nextDouble();
	    	System.out.print("Gravitational constant (default "+constantGravity+"):");
	    	constantGravity = kb.nextDouble();
	    	System.out.print("Initial velocity (default "+initialVel+"):");
	    	initialVel = kb.nextDouble();
	    	System.out.print("Variation velocity (default "+variationVel+"):");
	    	variationVel = kb.nextDouble();
	    	System.out.print("Initial disk rotation (default "+initialSpin+"):");
	    	initialSpin = kb.nextDouble();
	    	System.out.print("Variation disk rotation (default "+variationSpin+"):");
	    	variationSpin = kb.nextDouble();
	    	
	    	System.out.print("Draw trails (Y/N):");
	    	if (kb.next().equalsIgnoreCase("Y")){
	    		drawTrails = true;
	    	} else {
	    		drawTrails = false;
	    	}
	    	
	    	System.out.print("Central particle (Y/N):");
	    	if (kb.next().equalsIgnoreCase("Y")){
	    		centralParticle = true;
	    		System.out.print("Central particle mass (default "+centralParticleMass+"):");
	    		centralParticleMass = kb.nextDouble();
	    	} else {
	    		centralParticle = false;
	    		centralParticleMass = 0.0;
	    	}
    	}
        //Create array of particles and place them in the disk, next 12 lines
        Particle[] particleArray = new Particle[particleCount];
        if (centralParticle){
        	particleArray[0]=new Particle(0,xOrigin,yOrigin,centralParticleMass,0.0*variationMass,0.0*initialVel,0.0*variationVel,0.0*initialSpin,0.0*variationSpin);
        	for(int i=1; i<particleCount; i++){
        		particleArray[i]=new Particle(diskRadius,xOrigin,yOrigin,startingMass,variationMass,initialVel,variationVel,initialSpin,variationSpin);
        	}
        }
        else{
        	for(int i=0; i<particleCount; i++){
        		particleArray[i]=new Particle(diskRadius,xOrigin,yOrigin,startingMass,variationMass,initialVel,variationVel,initialSpin,variationSpin);
        	}
        }
        
        boolean collisionOccurred = false;
        while(true){
        	for(int i=0; i<particleCount; i++){
        		for(int j=0; j<particleCount; j++){
        			if(i!=j){
        				int minDist=(int)((Math.sqrt(particleArray[i].getMass())+0.5)/2)+(int)((Math.sqrt(particleArray[j].getMass())+0.5)/2);
        				if (Math.sqrt(Math.pow((int)((particleArray[i].getXPosition()*200)+400)-(int)((particleArray[j].getXPosition()*200)+400),2)+Math.pow((int)((particleArray[i].getYPosition()*200)+400)-(int)((particleArray[j].getYPosition()*200)+400),2))<=minDist){
      		  				particleArray[i].setXVelocity(((particleArray[i].getMass()*particleArray[i].getXVelocity())+(particleArray[j].getMass()*particleArray[j].getXVelocity()))/(particleArray[i].getMass()+particleArray[j].getMass()));
        					particleArray[i].setYVelocity(((particleArray[i].getMass()*particleArray[i].getYVelocity())+(particleArray[j].getMass()*particleArray[j].getYVelocity()))/(particleArray[i].getMass()+particleArray[j].getMass()));
        					particleArray[i].setXPosition(((particleArray[i].getMass()*particleArray[i].getXPosition())+(particleArray[j].getMass()*particleArray[j].getXPosition()))/(particleArray[i].getMass()+particleArray[j].getMass()));
        					particleArray[i].setYPosition(((particleArray[i].getMass()*particleArray[i].getYPosition())+(particleArray[j].getMass()*particleArray[j].getYPosition()))/(particleArray[i].getMass()+particleArray[j].getMass()));
        					particleArray[i].setMass(particleArray[i].getMass()+particleArray[j].getMass());
        					particleArray[j].setXPosition(1000000.0*(Math.random()+1.0));
        					particleArray[j].setYPosition(1000000.0*(Math.random()+1.0));
        					particleArray[j].setMass(0.0000000000001);
        					collisionOccurred = true;
        				}
        			}
        		}
        	}
        	
        	//Calculate total force on particle, next 10 lines
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
        		for(int i=0; i<particleCount; i++){
        			int dispX = (int)((particleArray[i].getXPosition()*200)+400);
					int dispY = (int)((particleArray[i].getYPosition()*200)+400);
					if ((dispX<800)&&(dispY<800)&&(dispX>0)&&(dispY>0)){
						displayImage.setRGB(dispX,dispY, Gray);
					}
					int radius = (int)((Math.sqrt(particleArray[i].getMass())+0.5)/2);
					for (int j = dispX-radius-5; j <= dispX+radius+5;j++){
						for (int k = dispY-radius-5; k <= dispY+radius+5;k++){
							if ((j<800)&&(k<800)&&(j>0)&&(k>0)){
								int red = (displayImage.getRGB(j,k) >> 16) & 0xff;
    							int green = (displayImage.getRGB(j,k) >> 8) & 0xff;
    							int blue = (displayImage.getRGB(j,k)) & 0xff;
								if ((red+green+blue)==765){
									displayImage.setRGB(j,k, Black);
								}
								else{
									if((red+green+blue)==762)
									displayImage.setRGB(j,k, Gray);
								}
							}
						}
					}
        		}
        	}
        	else{
        		for(int i=0; i<particleCount; i++){
        			int dispX = (int)((particleArray[i].getXPosition()*200)+400);
					int dispY = (int)((particleArray[i].getYPosition()*200)+400);
					int radius = (int)((Math.sqrt(particleArray[i].getMass())+0.5)/2);
					for (int j = dispX-radius-5; j <= dispX+radius+5;j++){
						for (int k = dispY-radius-5; k <= dispY+radius+5;k++){
							if ((j<800)&&(k<800)&&(j>0)&&(k>0)){
								int red = (displayImage.getRGB(j,k) >> 16) & 0xff;
    							int green = (displayImage.getRGB(j,k) >> 8) & 0xff;
    							int blue = (displayImage.getRGB(j,k)) & 0xff;
								if ((red+green+blue)==765){
									displayImage.setRGB(j,k, Black);
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
        	for(int i=0; i<particleCount; i++){
        		particleArray[i].updateVel(deltaTime);
        		particleArray[i].updatePos(deltaTime);
        		
        		//Draw bodies
        		int dispX = (int)((particleArray[i].getXPosition()*200)+400);
				int dispY = (int)((particleArray[i].getYPosition()*200)+400);
				for (int j = dispX-(int)((Math.sqrt(particleArray[i].getMass())+0.5)/2); j <= dispX+(int)((Math.sqrt(particleArray[i].getMass())+0.5)/2);j++){
					for (int k = dispY-(int)((Math.sqrt(particleArray[i].getMass())+0.5)/2); k <= dispY+(int)((Math.sqrt(particleArray[i].getMass())+0.5)/2);k++){
						if ((j<800)&&(k<800)&&(j>0)&&(k>0)){
							int red = (displayImage.getRGB(j,k) >> 16) & 0xff;
    						int green = (displayImage.getRGB(j,k) >> 8) & 0xff;
    						int blue = (displayImage.getRGB(j,k)) & 0xff;
							if ((red+green+blue)==0){
								displayImage.setRGB(j,k, White);
							}
							else{
								if((red+green+blue)==192)
								displayImage.setRGB(j,k, NearWhite);
							}
						}
					}
				}
       		}
			try {
   				Thread.sleep(1);
   			} catch (Exception e) {
   				System.out.println(e);
   			}
   			frame.repaint();
   			//System.out.println(particleArray[0].getXVelocity()+","+particleArray[0].getYVelocity());
        }
    }
}
