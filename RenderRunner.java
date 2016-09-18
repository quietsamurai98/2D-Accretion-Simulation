/**
 * @(#)RenderRunner.java
 *
 *
 * @author 
 * @version 1.00 2016/9/16
 */

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
    	int particleCount = 100000;
    	int frameStart    = 1; //Start at 1 to start from the very beginning
    	int frameEnd      = 17;
    	int frameSkip     = 1; //1=all frames are rendered, 2=half of frames are rendered, 3=third of frames are rendered, etc.
    	int resolution    = 4320;
    	double zoom       = 2.0;
        Render render = new Render(particleCount,frameStart, frameEnd, frameSkip, resolution, zoom);
        render.methodRunner();
    }
}
