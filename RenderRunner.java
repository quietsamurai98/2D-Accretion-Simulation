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
    	int frameCap      = 1;
    	int resolution    = 4320;
        Render render = new Render(particleCount,frameCap,resolution);
        render.methodRunner();
    }
}
