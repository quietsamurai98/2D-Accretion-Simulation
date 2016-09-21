/**
 * @(#)RenderRunner.java
 *
 *
 * @author 
 * @version 1.00 2016/9/16
 */
import java.util.Scanner;
public class RenderRunner {
        
    /**
     * Creates a new instance of <code>RenderRunner</code>.
     */
    public RenderRunner() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	Scanner kb = new Scanner(System.in);
    	System.out.print("Simulation name: ");
    	String name = kb.next();
    	System.out.print("Number of particles in the simulation: ");
    	int particles           = kb.nextInt();
    	System.out.print("First simulation frame to render (1 to start from beginning) : ");
    	int frameStart          = kb.nextInt(); //Start at 1 to start from the very beginning
    	System.out.print("Last simulation frame to render : ");
    	int frameEnd            = kb.nextInt();
    	System.out.print("Render every nth simulation frame (1 to render all frames) n = ");
    	int frameSkip           = kb.nextInt(); //1=all frames are rendered, 2=half of frames are rendered, 3=third of frames are rendered, etc.
    	System.out.print("Height of the output image, in pixels (1080 for 1080p HD): ");
    	int resolutionY         = kb.nextInt();
    	System.out.print("Width of the output image, in pixels (1920 for 1080p HD): ");
    	int resolutionX         = kb.nextInt();
    	System.out.print("Zoom factor (0.5 to zoom out by a factor of 2): ");
    	double zoom             = kb.nextDouble();
    	System.out.print("Trail length multiplier (2 to double trail length): ");
    	int lengthMultiplier = (int)kb.nextDouble();
        Render render = new Render(name, particles, lengthMultiplier, frameStart, frameEnd, frameSkip, resolutionX, resolutionY, zoom);
        render.methodRunner();
    }
}
