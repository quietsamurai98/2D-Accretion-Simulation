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
    	int particleCount      = 20000;
    	System.out.println("Particle count = "+particleCount);
        ParticleInteraction simulationInstance = new ParticleInteraction(particleCount);
    }
}