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
    public ParticleInteraction() {

    }
    
    /**
     * @param args the command line arguments
     */
    public void main(String[] args) {
    

//		Open Setting GUI
		SettingsGUI settings = new SettingsGUI();
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
//		Declare Simulation Settings Vars
//		Declare misc vars
		
//		Assign values to vars
//	 	
//	
//	
//	
//		Spawn particles
//		
		while(true){
			
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
//			Update simulation settings using values from GUI
//			Clear the display image
//			Delete all saved images
//			Respawn particles 
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
		
	}
	public void drawParticles(){
		
	}
	public void updateDisplay(){
		
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
    
}
