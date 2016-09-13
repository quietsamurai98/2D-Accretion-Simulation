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
    Particle[] particleArray = new Particle[0];
    SettingsGUI settings = new SettingsGUI();
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
   	BufferedImage displayImage;
   	Graphics2D displayGraphics;
   	int whiteInt;
   	int blackInt;
   	double centerX;
   	double centerY;
   	JFrame frame;
    public ParticleInteraction() {

    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	run();
    }
    
    public void run(){

//		Open Setting GUI
		SettingsGUI settings = new SettingsGUI();
		settings.prepareGUI();
        settings.createUIComponents();
//	
//		Declare Simulation Display Window and Image
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
		int blackInt = new Color(0,0,0).getRGB();
		int whiteInt = new Color(255,255,255).getRGB();
		Graphics2D    displayGraphics = displayImage.createGraphics();
//	
//		Declare misc vars
		
//	 	
//	
//	
//	
//		
		while(true){
//			Update vars
			updateSettings();
//			Spawn Particles
			spawnParticles();	
			while(!settings.getRestartBool()){
//	
//				Collide particles
				collideParticles();
//				Calculate gravity btwn particles
				calculateGravity();
//				Update velocities and positions of particles
				updatePositionsAndVelocities();
//	
//	
//				Calculate where the view's center is
				calculateCenter();
//				Draw particles
				drawParticles();
//				Update Display window and/or Save display image to a folder
				updateDisplay();
			}
//			Clear the display image
//			Delete all saved images
		}
    }
    public void collideParticles(){
    	for(int i=0; i<particleCount; i++){
    		for(int j=0; j<particleCount; j++){
    			if(i!=j){
    				double minDist=Math.pow(((Math.sqrt(particleArray[i].getMass())+0.5)/2.0)+((Math.sqrt(particleArray[j].getMass())+0.5)/2.0),2);
    				if (Math.pow((int)((particleArray[i].getXPosition()*200)+400)-(int)((particleArray[j].getXPosition()*200)+400),2)+Math.pow((int)((particleArray[i].getYPosition()*200)+400)-(int)((particleArray[j].getYPosition()*200)+400),2)<=minDist){
  		  				particleArray[i].setXVelocity(((particleArray[i].getMass()*particleArray[i].getXVelocity())+(particleArray[j].getMass()*particleArray[j].getXVelocity()))/(particleArray[i].getMass()+particleArray[j].getMass()));
    					particleArray[i].setYVelocity(((particleArray[i].getMass()*particleArray[i].getYVelocity())+(particleArray[j].getMass()*particleArray[j].getYVelocity()))/(particleArray[i].getMass()+particleArray[j].getMass()));
    					particleArray[i].setXPosition(((particleArray[i].getMass()*particleArray[i].getXPosition())+(particleArray[j].getMass()*particleArray[j].getXPosition()))/(particleArray[i].getMass()+particleArray[j].getMass()));
    					particleArray[i].setYPosition(((particleArray[i].getMass()*particleArray[i].getYPosition())+(particleArray[j].getMass()*particleArray[j].getYPosition()))/(particleArray[i].getMass()+particleArray[j].getMass()));
    					particleArray[i].setMass(particleArray[i].getMass()+particleArray[j].getMass());
						particleArray[j].setXPosition(1000000.0*(Math.random()+1.0));
						particleArray[j].setYPosition(1000000.0*(Math.random()+1.0));
						particleArray[j].setMass(0.0000000000001);
					}
				}
			}
		}
	}
	public void calculateGravity(){
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
	}
	public void updatePositionsAndVelocities(){
		for(int i=0; i<particleCount; i++){
	        particleArray[i].updateVel(deltaTime);
	        particleArray[i].updatePos(deltaTime);
		}
	}
	public void calculateCenter(){
		//FINISH THIS
		centerX=0.0;
		centerY=0.0;
	}
	public void drawParticles(){
		for(int i=0; i<particleCount; i++){
			int dispX = (int)(((particleArray[i].getXPosition()-centerX)*200)+400);
			int dispY = (int)(((particleArray[i].getYPosition()-centerY)*200)+400);
			int radius = (int)((Math.sqrt(particleArray[i].getMass())+0.5)/2);
			for (int j = dispX-radius; j <= dispX+radius;j++){
				for (int k = dispY-radius; k <= dispY+radius;k++){
					if ((((dispX-j)*(dispX-j)+(dispY-k)*(dispY-k))<=(radius*radius))&&(j<800)&&(k<800)&&(j>0)&&(k>0)){
						int red = (displayImage.getRGB(j,k) >> 16) & 0xff;
						int green = (displayImage.getRGB(j,k) >> 8) & 0xff;
						int blue = (displayImage.getRGB(j,k)) & 0xff;
						if ((red+green+blue)==0){
							displayImage.setRGB(j,k, whiteInt);
						}
					}
				}
			}
		}
	}
	public void updateDisplay(){
		frame.repaint();
	}
	public void blackDisplay(){
		displayGraphics.setPaint ( new Color(0,0,0) );
		displayGraphics.fillRect ( 0, 0, displayImage.getWidth(), displayImage.getHeight() );
	}
    public void spawnParticles(){
    	particleArray = new Particle[particleCount];
        if (centralParticle){
        	particleArray[0]=new Particle(0,0,0,centralParticleMass,0,0,0,0,0,constantGravity,centralParticleMass);
        	for(int i=1; i<particleCount; i++){
        		particleArray[i]=new Particle(diskRadius,0,0,initialMass,variationMass,0,variationVel,initialSpinFactor,variationSpin,constantGravity,centralParticleMass+((particleCount-1)*initialMass*0));
        	}
        }
        else{
        	for(int i=0; i<particleCount; i++){
        		particleArray[i]=new Particle(diskRadius,0,0,initialMass,variationMass,0,variationVel,initialSpinFactor,variationSpin,constantGravity,initialMass*particleCount);
        	}
        }
    }
    public void updateSettings(){
		particleCount = settings.getSpinner01Value(); //System.out.println(particleCount);
    	initialMass = settings.getSpinner02Value(); //System.out.println(initialMass);
    	variationMass = settings.getSpinner03Value(); //System.out.println(variationMass);
    	diskRadius = settings.getSpinner04Value(); //System.out.println(diskRadius);
    	deltaTime = settings.getSpinner05Value(); //System.out.println(deltaTime);//timestep in seconds 
    	constantGravity = settings.getSpinner06Value(); //System.out.println(constantGravity);//gravitational constant
    	variationVel=settings.getSpinner07Value(); //System.out.println(variationVel);
    	initialSpinFactor=settings.getSpinner08Value(); //System.out.println(initialSpinFactor);
    	centralParticleMass = settings.getSpinner09Value(); //System.out.println(centralParticleMass);
    }
}
