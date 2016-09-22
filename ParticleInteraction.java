/**
 * @(#)ParticleInteraction.java
 *
 *
 * @author 
 * @version 1.00 2016/8/24
 */
import java.lang.*;
import java.util.*;
import java.util.Scanner;
import java.io.*;
public class ParticleInteraction {
        
    /**
     * Creates a new instance of <code>ParticleInteraction</code>.
     */
    
    
    Particle[] particleArray = new Particle[0];
	boolean[] boolArray = new boolean[0];
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
   	boolean centralParticle=false;
   	double randomSpin;
   	double centerX=0;
   	double centerY=0;
   	int maxMassID=0;
   	double maxMass;
	int frameCount;
	long startTime, elapsedTime;
	double collisionDistanceFactor=1;
	int particleNum;
	int oldParticleNum;
	int planets;
	int escaping;
	double spinRatio;
	String name;
	BufferedWriter dataBW;
    public ParticleInteraction() {
		simulate();
    }
    
    /**
     * @param args the command line arguments
     */    
     private void updateVars(){
     	Scanner kb = new Scanner(System.in);
     	System.out.print("Simulation name                                            : ");
		name           = kb.next(); 
     	System.out.print("Number of initial particles (default = 1000)               = ");
		particleCount           = kb.nextInt(); 
		System.out.print("Initial particle mass (default = 10.0)                     = ");
		initialMass             = kb.nextDouble(); 
		System.out.print("Variation in initial particle mass (default = 5.0)         = ");	
		variationMass           = kb.nextDouble(); 
		System.out.print("Radius of the initial disk (default = 2.0)                 = ");
		diskRadius              = kb.nextDouble(); 
		System.out.print("Size of simulation timestep (default = 0.001)              = ");
		deltaTime               = kb.nextDouble(); 
		System.out.print("Gravitational Constant (default = 0.001)                   = ");
		constantGravity         = kb.nextDouble(); 
		System.out.print("Random variation in initial velocity (default = 0)         = ");
		variationVel            = kb.nextDouble(); 
		System.out.print("Random initial velocity normal to center (default = 0.5)   = ");
		randomSpin              = kb.nextDouble();
		System.out.print("Spin ratio (0 = all clockwise, 1 = all anti-clockwise)     = ");
		spinRatio			    = kb.nextDouble();
//		System.out.print("Is there a central particle? (default = false)             = ");
//		centralParticle         = kb.nextBoolean();
//		if (centralParticle){
//			System.out.print("Mass of the central particle (default = 100)                   = ");
//			centralParticleMass     = kb.nextDouble(); 
//		}
//		System.out.print("Collision Distance Factor (default = 1.0)                  = ");
//		collisionDistanceFactor = kb.nextDouble();
		
		particleNum             = particleCount;
    } 
    	
    	
    public void simulate(){
		updateVars();
		createDirectory();
		spawnParticles();
		frameCount=0;
		startTime = System.nanoTime();
		while(particleNum>1){
			startTime = System.nanoTime();
			collideParticles();
			calculateGrav();
	        frameCount++;
			stepTime();
			elapsedTime=System.nanoTime()-startTime;
			saveText();
	        printTime();
			try{
	        	dataBW.flush();
	        }
	        catch (IOException e){
	        	e.printStackTrace();
	        }
		}
    }

    private void createDirectory(){
    	File directory = new File(name);
		if (!directory.exists()) {
		    boolean result = false;
		
		    try{
		        directory.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se){
		        se.printStackTrace();
		    }        
		    if(result) {
		    }
		}else{
			//if the directory exists, delete all existing images
			System.out.println("Clearing directory");
			for(File file: directory.listFiles()) 
    			if (!file.isDirectory()) 
        	file.delete();
        	System.out.println("Directory cleared!");
		}
		String outputFileName = ".\\"+name+"\\settings.txt";
        File outputFile = new File(outputFileName);
        PrintWriter output = null;
        try {
        	output = new PrintWriter(outputFile);
        }
        catch (FileNotFoundException ex){
        	System.out.println("File "+outputFileName+" not found.");
        	ex.printStackTrace();
        }
        
        output.println(
        	
				"particleCount           = "+ particleCount + "\r\n" +
				"initialMass             = "+ initialMass + "\r\n" +
				"variationMass           = "+ variationMass + "\r\n" +
				"diskRadius              = "+ diskRadius + "\r\n" +
				"deltaTime               = "+ deltaTime + "\r\n" +
				"constantGravity         = "+ constantGravity + "\r\n" +
				"variationVel            = "+ variationVel + "\r\n" +
				"initialSpinFactor       = "+ initialSpinFactor + "\r\n" +
				"randomSpin	             = "+ randomSpin + "\r\n" +
				"spinRatio               = "+ spinRatio + "\r\n"
//				"centralParticleMass     = "+ centralParticleMass + "\r\n" +
//				"centralParticle         = "+ Boolean.toString(centralParticle) + "\r\n" +
//				"collisionDistanceFactor = "+ collisionDistanceFactor
        	
        	);
        output.close();
        try {
        	String dataFileName = ".\\"+name+"\\raw data.txt";
        	File dataFile = new File(dataFileName);
        	if(!dataFile.exists()){
    			dataFile.createNewFile();
    		}
    		FileWriter dataFW = new FileWriter(dataFileName);
    		dataBW = new BufferedWriter(dataFW);
        }
        catch (IOException e){
        	e.printStackTrace();
        }
	}
    private void spawnParticles(){
    	particleArray = new Particle[particleCount];
		boolArray = new boolean[particleCount];
		planets = 0;
		Arrays.fill(boolArray, Boolean.TRUE);    		
        if (centralParticle){
        	particleArray[0]=new Particle(0,0,0,centralParticleMass,0,0,0,0,0,0,constantGravity,centralParticleMass);
        	for(int i=1; i<particleCount; i++){
        		particleArray[i]=new Particle(diskRadius,0,0,initialMass,variationMass,0,variationVel,initialSpinFactor,randomSpin,spinRatio,constantGravity,centralParticleMass+((particleCount-1)*initialMass*0));
        	}
        }
        else{
        	for(int i=0; i<particleCount; i++){
        		particleArray[i]=new Particle(diskRadius,0,0,initialMass,variationMass,0,variationVel,initialSpinFactor,randomSpin,spinRatio,constantGravity,initialMass*particleCount);
        	}
        }
        maxMass=-Double.MAX_VALUE;
    	for(int i=0; i<particleCount;i++){
    		if (boolArray[i]&&particleArray[i].getMass()>maxMass){
    			maxMass=particleArray[i].getMass();
    			maxMassID=i;
    		}
    	}
    }
    private void stepTime(){
    	oldParticleNum=particleNum;
    	particleNum=0;
    	for(int i=0; i<particleCount; i++){
			if (boolArray[i]){
		        particleArray[i].updateVel(deltaTime);
		        particleArray[i].updatePos(deltaTime);
		        particleArray[i].zeroForce();
		        particleNum++;
			}
		}
    }
    private void saveText(){
		

        String out = "";
        
        try{
        	for(int i=0;i<particleCount;i++){
        		dataBW.write(particleArray[i] + " ");
        	}
    		dataBW.write("\n");
    	}
    	catch (IOException e){
    		e.printStackTrace();
    	}
    }
    private void printTime(){
    	if (oldParticleNum!=particleNum){
    		System.out.println("Frame "+String.format("%010d", frameCount)+" took " + String.format("%014d", elapsedTime) + " nanoseconds, and contained... ");
    		System.out.println("     "+particleNum+" particles total");
    		System.out.println("     "+planets+" planets");//, of which "+escaping+" are escaping");
    	}
    }
    private void calculateGrav(){
    	for(int i=0; i<particleCount; i++){
			if (boolArray[i]){
				double iX = particleArray[i].getXPosition();
				double iY = particleArray[i].getYPosition();
				double iM = particleArray[i].getMass();
				for(int j=0; j<particleCount; j++){
					if(i!=j&&boolArray[j]){
						double rX = iX-particleArray[j].getXPosition();
						double rY = iY-particleArray[j].getYPosition();
						double rT = Math.sqrt(rX*rX+rY*rY);
						double rF = rT*rT*rT;
						double fT = -constantGravity*iM*particleArray[j].getMass()/rF;
						particleArray[i].updateForce(rX*fT, rY*fT);
					}
				}
			}
		}
    }
    private void collideParticles(){
    	escaping=0;
    	planets=0;
    	for(int i=0; i<particleCount; i++){
			if (boolArray[i]){
	    		for(int j=0; j<particleCount; j++){
	    			if(boolArray[j]&&i!=j){
	  		  			double radiusI   = particleArray[i].getRadius();
	  		  			double radiusJ   = particleArray[j].getRadius();
	  		  			double posXI     = (particleArray[i].getXPosition()*200)+400;
	  		  			double posYI     = (particleArray[i].getYPosition()*200)+400;
	  		  			double posXJ     = (particleArray[j].getXPosition()*200)+400;
	  		  			double posYJ     = (particleArray[j].getYPosition()*200)+400;
	    				double minDist = ((radiusI+radiusJ+1)/2)*collisionDistanceFactor;
	    				minDist *= minDist;
	    				if (
	    					(posXI-posXJ)*(posXI-posXJ)<=minDist && 
	    					(posYI-posYJ)*(posYI-posYJ)<=minDist && 
	    					(posXI-posXJ)*(posXI-posXJ)+(posYI-posYJ)*(posYI-posYJ)<=minDist
	    					){
	    						double massI   = particleArray[i].getMass();
	  		  					double massJ   = particleArray[j].getMass();
		  		  				double massTotal = massI+massJ;
		  		  				particleArray[i].setXVelocity(((massI*particleArray[i].getXVelocity())+(massJ*particleArray[j].getXVelocity()))/massTotal);
		    					particleArray[i].setYVelocity(((massI*particleArray[i].getYVelocity())+(massJ*particleArray[j].getYVelocity()))/massTotal);
		    					particleArray[i].setXPosition(((massI*particleArray[i].getXPosition())+(massJ*particleArray[j].getXPosition()))/massTotal);
		    					particleArray[i].setYPosition(((massI*particleArray[i].getYPosition())+(massJ*particleArray[j].getYPosition()))/massTotal);
		    					particleArray[i].setMass(massTotal);
								particleArray[j].setXPosition(10001);
								particleArray[j].setYPosition(10001);
								particleArray[j].setMass(0);
								boolArray[j]=false;
						}
					}
				}
			}
			if (particleArray[i].getMass()>(initialMass)*2){
				planets++; //Bodies consisting of 2+ particles
//				if (particleArray[i].getXVelocity()*particleArray[i].getXVelocity()+particleArray[i].getYVelocity()*particleArray[i].getYVelocity() > initialMass*particleCount*constantGravity*2/Math.sqrt(particleArray[i].getXPosition()*particleArray[i].getXPosition()+particleArray[i].getYPosition()*particleArray[i].getYPosition()))
//					escaping++;
			}
		}
    }
}
