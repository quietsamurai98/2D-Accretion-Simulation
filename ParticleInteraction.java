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
	double systemMass;
	double escapeVelocity;
	int orbitNum;
	int particleNum;
    public ParticleInteraction(int particles) {
		simulate(particles);
    }
    
    /**
     * @param args the command line arguments
     */    
     private void updateVars(int particles){
		particleCount           = particles; 
		orbitNum                = particles;
		initialMass             = 1.0; 
		variationMass           = 0.5; 
		diskRadius              = 15.0; 
		deltaTime               = 0.001; 
		constantGravity         = 0.001; 
		variationVel            = 0.000; 
		initialSpinFactor       = 0.0;
		randomSpin			    = 1.0;
		centralParticleMass     = 100; 
		centralParticle         = false;
		collisionDistanceFactor = 1.0;
		
		particleNum             = particleCount;
    } 
    	
    	
    public void simulate(int particles){
		updateVars(particles);
		createDirectory();
		spawnParticles();
		frameCount=0;
		startTime = System.nanoTime();
		while(orbitNum>1){
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
        		particleArray[i]=new Particle(diskRadius,0,0,initialMass,variationMass,0,variationVel,initialSpinFactor,randomSpin,constantGravity,centralParticleMass+((particleCount-1)*initialMass*0));
        	}
        }
        else{
        	for(int i=0; i<particleCount; i++){
        		particleArray[i]=new Particle(diskRadius,0,0,initialMass,variationMass,0,variationVel,initialSpinFactor,randomSpin,constantGravity,initialMass*particleCount);
        	}
        }
        systemMass=0;
    	for(int i=0; i<particleCount;i++){
			systemMass+=particleCount;
    	}
    }
    private void stepTime(){
    	orbitNum=particleNum;
    	for(int i=0; i<particleCount; i++){
			if (boolArray[i]){
		        particleArray[i].updateVel(deltaTime);
		        particleArray[i].updatePos(deltaTime);
		        particleArray[i].zeroForce();
		        if ((frameCount%100==0)&&(particleArray[i].getXVelocity()*particleArray[i].getXVelocity()+particleArray[i].getYVelocity()*particleArray[i].getYVelocity())>((2*constantGravity*systemMass))/Math.sqrt(particleArray[i].getXPosition()*particleArray[i].getXPosition()+particleArray[i].getYPosition()*particleArray[i].getYPosition()))
				{
					orbitNum--;
				}
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
    	
    	System.out.println("Frame "+String.format("%010d", frameCount)+" took " + String.format("%014d", elapsedTime) + " nanoseconds, and contained " + orbitNum + " non-escaping particles (" + particleNum + " total)");
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
    	for(int i=0; i<particleCount; i++){
			if (boolArray[i]){
	    		for(int j=0; j<particleCount; j++){
	    			if(boolArray[j]&&i!=j){
	    				double massI   = particleArray[i].getMass();
	  		  			double massJ   = particleArray[j].getMass();
	  		  			int posXI = (int)(particleArray[i].getXPosition()*200)+400;
	  		  			int posYI = (int)(particleArray[i].getYPosition()*200)+400;
	  		  			int posXJ = (int)(particleArray[j].getXPosition()*200)+400;
	  		  			int posYJ = (int)(particleArray[j].getYPosition()*200)+400;
	    				double minDist = Math.pow(((Math.sqrt(massI)+Math.sqrt(massJ)+1)/2)*collisionDistanceFactor,2);
	    				if (Math.pow(posXI-posXJ,2)<=minDist && Math.pow(posYI-posYJ,2)<=minDist && Math.pow(posXI-posXJ,2)+Math.pow(posYI-posYJ,2)<=minDist){
	    					particleNum--;
	  		  				double massTotal = massI+massJ;
	  		  				particleArray[i].setXVelocity(((massI*particleArray[i].getXVelocity())+(massJ*particleArray[j].getXVelocity()))/massTotal);
	    					particleArray[i].setYVelocity(((massI*particleArray[i].getYVelocity())+(massJ*particleArray[j].getYVelocity()))/massTotal);
	    					particleArray[i].setXPosition(((massI*particleArray[i].getXPosition())+(massJ*particleArray[j].getXPosition()))/massTotal);
	    					particleArray[i].setYPosition(((massI*particleArray[i].getYPosition())+(massJ*particleArray[j].getYPosition()))/massTotal);
	    					particleArray[i].setMass(massTotal);
							particleArray[j].setXPosition(10000000);
							particleArray[j].setYPosition(10000000);
							particleArray[j].setMass(0);
							boolArray[j]=false;
						}
					}
				}
			}
		}
    }
}
