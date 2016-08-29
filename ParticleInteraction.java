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
		int Gray = new Color(128,128,128).getRGB();
		
    	//Set simulation parameters
    	int particleCount = 3;
    	double startingMass = 10.0;
    	double variationMass = 9.0;
    	double xOrigin = 0.0;
    	double yOrigin = 0.0;
    	double diskRadius = 0.15;
    	double deltaTime = 0.001; //timestep in seconds
    	double constantGravity=0.001; //gravitational constant
    	double initialVel=0.0;
    	double variationVel=0.001;
    	double initialSpin=0.1;
    	double variationSpin=0.00;
    	boolean drawTrails=true;
    	
        //Create array of particles and place them in the disk, next 4 lines
        Particle[] particleArray = new Particle[particleCount];
        for(int i=0; i<particleCount; i++){
        	particleArray[i]=new Particle(diskRadius,xOrigin,yOrigin,startingMass,variationMass,initialVel,variationVel,initialSpin,variationSpin);
        }
        
        
        while(true){
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
					for (int j = dispX-(int)((Math.sqrt(particleArray[i].getMass())+0.5)/2); j <= dispX+(int)((Math.sqrt(particleArray[i].getMass())+0.5)/2);j++){
						for (int k = dispY-(int)((Math.sqrt(particleArray[i].getMass())+0.5)/2); k <= dispY+(int)((Math.sqrt(particleArray[i].getMass())+0.5)/2);k++){
							if ((j<800)&&(k<800)&&(j>0)&&(k>0)){
								int alpha = (displayImage.getRGB(j,k) >> 24) & 0xff;
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
					for (int j = dispX-(int)((Math.sqrt(particleArray[i].getMass())+0.5)/2); j <= dispX+(int)((Math.sqrt(particleArray[i].getMass())+0.5)/2);j++){
						for (int k = dispY-(int)((Math.sqrt(particleArray[i].getMass())+0.5)/2); k <= dispY+(int)((Math.sqrt(particleArray[i].getMass())+0.5)/2);k++){
							if ((j<800)&&(k<800)&&(j>0)&&(k>0)){
								int alpha = (displayImage.getRGB(j,k) >> 24) & 0xff;
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
								int alpha = (displayImage.getRGB(j,k) >> 24) & 0xff;
								int red = (displayImage.getRGB(j,k) >> 16) & 0xff;
    							int green = (displayImage.getRGB(j,k) >> 8) & 0xff;
    							int blue = (displayImage.getRGB(j,k)) & 0xff;
								if ((red+green+blue)==0){
									displayImage.setRGB(j,k, White);
								}
								else{
									if((red+green+blue)==384)
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
