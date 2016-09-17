/**
 * @(#)SimulationRunner.java
 *
 *
 * @author 
 * @version 1.00 2016/9/14
 */

public class SimulationRunner {
        
    /**
     * Creates a new instance of <code>SimulationRunner</code>.
     */
    public SimulationRunner() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	int particleCount      = 5000;
    	int frameSize          = 1080;
    	boolean saveFrameImage = true;
    	boolean saveFrameText  = true;
        ParticleInteraction simulationInstance = new ParticleInteraction(particleCount, frameSize, saveFrameImage, saveFrameText);
    }
}
