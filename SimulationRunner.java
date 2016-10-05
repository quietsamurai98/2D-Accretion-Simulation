/**
 * @(#)SimulationRunner.java
 *
 *
 * @author 
 * @version 1.00 2016/9/14
 */
import java.lang.*;
import java.util.*;
import java.util.Scanner;
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
    	String name;
    	int particleCount, frameCap;
    	double initialMass, variationMass, diskRadius, randomSpin, spinRatio, variationVel, deltaTime, constantGravity;

    	Scanner kb = new Scanner(System.in);
     	System.out.print("Edit advanced settings? (Y/N)                              : ");
		boolean debugSettings   = kb.next().equalsIgnoreCase("Y");
		kb.nextLine();
     	System.out.print("Simulation name                                            : ");
		name      		        = kb.nextLine();

     	System.out.print("Number of initial particles (default = 1000)               = ");
		try
	  	{
	    	particleCount       = Integer.parseInt(kb.next());
	  	}
	  	catch(NumberFormatException nfe)
	  	{
	    	System.out.println("Input was not recognized, defaulting to 1000.");
	    	particleCount       = 1000;
	  	}

		System.out.print("Initial particle mass (default = 10.0)                     = ");
		try
	  	{
	    	initialMass  	        = Double.parseDouble(kb.next());
	  	}
	  	catch(NumberFormatException nfe)
	  	{
	    	System.out.println("Input was not recognized, defaulting to 10.0.");
	    	initialMass         = 10.0;
	  	}

		if (debugSettings){
			System.out.print("Variation in initial particle mass (default = 5.0)         = ");
			try
		  	{
		    	variationMass   = Double.parseDouble(kb.next());
		  	}
		  	catch(NumberFormatException nfe)
		  	{
		    	System.out.println("Input was not recognized, defaulting to " + initialMass/2.0 + ".");
		    	variationMass   = initialMass/2.0;
		  	}
		} else {
			variationMass       = initialMass/2.0;
		}
		System.out.print("Radius of the initial disk (default = 2.0)                 = ");
		try
	  	{
	    	diskRadius	        = Double.parseDouble(kb.next());
	  	}
	  	catch(NumberFormatException nfe)
	  	{
	    	System.out.println("Input was not recognized, defaulting to 2.0.");
	    	diskRadius          = 2.0;
	  	}

		System.out.print("Random initial velocity normal to center (default = 0.5)   = ");
		try
	  	{
	    	randomSpin          = Double.parseDouble(kb.next());
	  	}
	  	catch(NumberFormatException nfe)
	  	{
	    	System.out.println("Input was not recognized, defaulting to 0.5.");
	    	randomSpin          = 0.5;
	  	}

		System.out.print("Spin ratio (0 = all clockwise, 1 = all anti-clockwise)     = ");
		try
	  	{
	    	spinRatio           = Double.parseDouble(kb.next());
	  	}
	  	catch(NumberFormatException nfe)
	  	{
	    	System.out.println("Input was not recognized, defaulting to 0.5.");
	    	spinRatio           = 0.5;
	  	}

		if (debugSettings){
			System.out.print("Random variation in initial velocity (default = 0)         = ");
			try
		  	{
		    	variationVel       = Double.parseDouble(kb.next());
		  	}
		  	catch(NumberFormatException nfe)
		  	{
		    	System.out.println("Input was not recognized, defaulting to 0.");
		    	variationVel       = 0;
		  	}
			System.out.print("Size of simulation timestep (default = 0.001)              = ");
			try
		  	{
		    	deltaTime       = Double.parseDouble(kb.next());
		  	}
		  	catch(NumberFormatException nfe)
		  	{
		    	System.out.println("Input was not recognized, defaulting to 0.001");
		    	deltaTime       = 0.001;
		  	}
			System.out.print("Gravitational Constant (default = 0.001)                   = ");
			try
		  	{
		    	constantGravity = Double.parseDouble(kb.next());
		  	}
		  	catch(NumberFormatException nfe)
		  	{
		    	System.out.println("Input was not recognized, defaulting to 0.001.");
		    	constantGravity = 0.001;
		  	}
			System.out.print("Stop simulation after ___ frames have passed without a\n"+
							 " collision (default = 50000)                              = ");
			try
		  	{
		    	frameCap        = Integer.parseInt(kb.next());
		  	}
		  	catch(NumberFormatException nfe)
		  	{
		    	System.out.println("Input was not recognized, defaulting to 50000.");
		    	frameCap        = 50000;
		  	}
		} else {
			variationVel        = 0.0;
			deltaTime           = 0.001;
			constantGravity     = 0.001;
			frameCap            = 50000;
		}
		
		
        ParticleInteraction simulationInstance = new ParticleInteraction(name, particleCount, frameCap, initialMass, variationMass, diskRadius, randomSpin, spinRatio, variationVel, deltaTime, constantGravity);
    }
}
