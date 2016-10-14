/**
 * @(#)RenderRunner.java
 *
 *
 * @author 
 * @version 1.00 2016/9/16
 */
import java.util.Scanner;
import java.io.*;
public class RenderRunner {
    Thread energy;
    Thread trails;
    /**
     * Creates a new instance of <code>RenderRunner</code>.
     */
    public RenderRunner() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
    	Scanner kb = new Scanner(System.in);
    	System.out.print("Simulation name: ");
    	final String name = kb.nextLine();
    	System.out.print("Number of particles in the simulation: ");
    	final int particles           = kb.nextInt();
    	System.out.print("First simulation frame to render (1 to start from beginning) : ");
    	final int frameStart          = kb.nextInt(); //Start at 1 to start from the very beginning
    	System.out.print("Render every nth simulation frame (1 to render all frames) n = ");
    	final int frameSkip           = kb.nextInt(); //1=all frames are rendered, 2=half of frames are rendered, 3=third of frames are rendered, etc.
    	System.out.print("Height of the output image, in pixels (1080 for 1080p HD): ");
    	final int resolutionY         = kb.nextInt();
    	System.out.print("Width of the output image, in pixels (1920 for 1080p HD): ");
    	final int resolutionX         = kb.nextInt();
    	System.out.print("Zoom factor (0.5 to zoom out by a factor of 2): ");
    	final double zoom             = kb.nextDouble();
    	System.out.print("Trail length multiplier (2 to double trail length, 0 to disable): ");
    	final int lengthMultiplier = Math.max((int)kb.nextDouble(),0);
    	System.out.print("Minimum mass to draw trails (0 to show all trails) : ");
    	final double minMass             = kb.nextDouble();
    	Thread energy = new Thread(){
    		public void run(){
	    		RenderEnergy renderEnergy = new RenderEnergy(name, particles, lengthMultiplier, minMass, frameStart, frameSkip, resolutionX, resolutionY, zoom);
	    		try{
       	 			renderEnergy.methodRunner();
       	 		}catch(IOException e){
  					e.printStackTrace();
				}
	    		System.out.println("Rendering energy completed!");
    		}
    	};
    	Thread trails = new Thread(){
    		public void run(){
    			Render render = new Render(name, particles, lengthMultiplier, minMass, frameStart, frameSkip, resolutionX, resolutionY, zoom);
       	 		try{
       	 			render.methodRunner();
       	 		}catch(IOException e){
  					e.printStackTrace();
				}
        		System.out.println("Rendering particles completed!");
    		}
    	};
        energy.start();
        trails.start();
        
        
    }
}
