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
   	boolean centralParticle;
   	double randomSpin;
   	double centerX=0;
   	double centerY=0;
   	int maxMassID=0;
   	double maxMass;
	int frameCount;
	long startTime, elapsedTime;
	double collisionDistanceFactor;
    public ParticleInteraction(int particles) {
		simulate(particles);
    }
    
    /**
     * @param args the command line arguments
     */    
     private void updateVars(int particles){
		particleCount           = particles; 
		initialMass             = 1.0; 
		variationMass           = 0.5; 
		diskRadius              = 8.0; 
		deltaTime               = 0.001; 
		constantGravity         = 0.001; 
		variationVel            = 20.000; 
		initialSpinFactor       = 0.0;
		randomSpin			    = 0.0;
		centralParticleMass     = 100; 
		centralParticle         = false;
		collisionDistanceFactor = 1.0;
    } 
    	
    	
    public void simulate(int particles){
		updateVars(particles);
		createDirectory();
		spawnParticles();
		frameCount=0;
		startTime = System.nanoTime();
		while(true){
			startTime = System.nanoTime();
			collideParticles();
			calculateGrav();
	        frameCount++;
			stepTime();
			elapsedTime=System.nanoTime()-startTime;
			saveText();
	        printTime();
		}
    }

    private void createDirectory(){
    	File directory = new File("Simulation text frames (particleCount="+particleCount+")");
		if (!directory.exists()) {
		    boolean result = false;
		
		    try{
		        directory.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se){
		        //handle it
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
		String outputFileName = ".\\"+"Simulation text frames (particleCount="+particleCount+")"+"\\"   + "Simulation settings.txt";
        File outputFile = new File(outputFileName);
        PrintWriter output = null;
        try {
        	output = new PrintWriter(outputFile);
        }
        catch (FileNotFoundException ex){
        	System.out.println("File "+outputFileName+" not found.");
        	System.exit(1);
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
				"randomSpin			     = "+ randomSpin + "\r\n" +
				"centralParticleMass     = "+ centralParticleMass + "\r\n" +
				"centralParticle         = "+ Boolean.toString(centralParticle) + "\r\n" +
				"collisionDistanceFactor = "+ collisionDistanceFactor
        	
        	);
        output.close();
	}
    private void spawnParticles(){
    	particleArray = new Particle[particleCount];
		boolArray = new boolean[particleCount];
		Arrays.fill(boolArray, Boolean.TRUE);    		
        if (centralParticle){
        	particleArray[0]=new Particle(0,0,0,centralParticleMass,0,0,0,0,0,constantGravity,centralParticleMass);
        	for(int i=1; i<particleCount; i++){
        		particleArray[i]=new Particle(diskRadius,0,0,initialMass,variationMass,0,variationVel,initialSpinFactor,0,constantGravity,centralParticleMass+((particleCount-1)*initialMass*0));
        	}
        }
        else{
        	for(int i=0; i<particleCount; i++){
        		particleArray[i]=new Particle(diskRadius,0,0,initialMass,variationMass,0,variationVel,initialSpinFactor,0,constantGravity,initialMass*particleCount);
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
    	for(int i=0; i<particleCount; i++){
			if (boolArray[i]){
		        particleArray[i].updateVel(deltaTime);
		        particleArray[i].updatePos(deltaTime);
		        particleArray[i].zeroForce();
			}
		}
    }
    private void saveText(){
		String outputFileName = ".\\"+"Simulation text frames (particleCount="+particleCount+")"+"\\"+String.format("%010d", frameCount)+".txt";
        File outputFile = new File(outputFileName);
        PrintWriter output = null;
        try {
        	output = new PrintWriter(outputFile);
        }
        catch (FileNotFoundException ex){
        	System.out.println("File "+outputFileName+" not found.");
        	System.exit(1);
        }
        
        for(int i=0;i<particleCount;i++){
        	if(boolArray[i]){
        		output.println(particleArray[i]);
        	}
        }
        output.close();
    }
    private void printTime(){
    	
    	System.out.println("Frame "+String.format("%010d", frameCount)+" took " + String.format("%014d", elapsedTime) + " nanoseconds");
//		String outputFileName = ".\\"+"Simulation text frames (particleCount="+particleCount+")"+"\\"+"Computation time per frame.txt";
//        try{
//			File file =new File(outputFileName);
//			if(!file.exists()){
//			file.createNewFile();
//			}
//			FileWriter fw = new FileWriter(file,true);
//			BufferedWriter bw = new BufferedWriter(fw);
//			bw.write(String.format("%014d", elapsedTime)+"\r\n");
//			bw.close();
//			
//			}catch(IOException ioe){
//			System.out.println("Exception occurred:");
//			ioe.printStackTrace();
//       }
		
		
    }
    private void calculateGrav(){
    	for(int i=0; i<particleCount; i++){
			if (boolArray[i]){
				for(int j=0; j<particleCount; j++){
					if(i!=j&&boolArray[j]){
						double rX = particleArray[i].getXPosition()-particleArray[j].getXPosition();
						double rY = particleArray[i].getYPosition()-particleArray[j].getYPosition();
						double rT = Math.sqrt(rX*rX+rY*rY);
						double rF = rT*rT*rT;
						double fT = -constantGravity*particleArray[i].getMass()*particleArray[j].getMass()/rF;
						particleArray[i].updateForce(rX*fT, rY*fT);
					}
				}
			}
		}
    }
    private void collideParticles(){
    	boolean collisionOccuredTest = false;
    	for(int i=0; i<particleCount; i++){
			if (boolArray[i]){
	    		for(int j=0; j<particleCount; j++){
	    			if(i!=j&&boolArray[j]){
	    				double minDist=Math.pow((((Math.sqrt(particleArray[i].getMass())+0.5)/2.0)+((Math.sqrt(particleArray[j].getMass())+0.5)/2.0))*collisionDistanceFactor,2);
	    				if (Math.pow((int)((particleArray[i].getXPosition()*200)+400)-(int)((particleArray[j].getXPosition()*200)+400),2)+Math.pow((int)((particleArray[i].getYPosition()*200)+400)-(int)((particleArray[j].getYPosition()*200)+400),2)<=minDist){
	  		  				particleArray[i].setXVelocity(((particleArray[i].getMass()*particleArray[i].getXVelocity())+(particleArray[j].getMass()*particleArray[j].getXVelocity()))/(particleArray[i].getMass()+particleArray[j].getMass()));
	    					particleArray[i].setYVelocity(((particleArray[i].getMass()*particleArray[i].getYVelocity())+(particleArray[j].getMass()*particleArray[j].getYVelocity()))/(particleArray[i].getMass()+particleArray[j].getMass()));
	    					particleArray[i].setXPosition(((particleArray[i].getMass()*particleArray[i].getXPosition())+(particleArray[j].getMass()*particleArray[j].getXPosition()))/(particleArray[i].getMass()+particleArray[j].getMass()));
	    					particleArray[i].setYPosition(((particleArray[i].getMass()*particleArray[i].getYPosition())+(particleArray[j].getMass()*particleArray[j].getYPosition()))/(particleArray[i].getMass()+particleArray[j].getMass()));
	    					particleArray[i].setMass(particleArray[i].getMass()+particleArray[j].getMass());
							particleArray[j].setXPosition(1000000.0*(Math.random()+1.0));
							particleArray[j].setYPosition(1000000.0*(Math.random()+1.0));
							particleArray[j].setMass(0.000000001);
							boolArray[j]=false;
							maxMass=-Double.MAX_VALUE;
				        	for(int k=0; k<particleCount;k++){
				        		if (boolArray[k]&&particleArray[k].getMass()>maxMass){
				        			maxMass=particleArray[k].getMass();
				        			maxMassID=k;
				        		}
				        	}
				        	collisionOccuredTest=true;
						}
					}
				}
			}
		}
    }
}
