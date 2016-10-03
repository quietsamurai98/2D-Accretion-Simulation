/**
 * @(#)Particle.java
 *
 *
 * @author
 * @version 1.00 2016/8/24
 */
import java.util.*;

public class Particle {
	private double xPosition;
	private double yPosition;
	private double xAcceleration;
	private double yAcceleration;
	private double xVelocity;
	private double yVelocity;
	private double xForce;
	private double yForce;
	private double mass;
	private double distToCenter;
	private double randSpin;
	private double radius;
	public Particle() {
    	xPosition=0.0;
		yPosition=0.0;
		xAcceleration=0.0;
		yAcceleration=0.0;
		xVelocity=0.0;
		yVelocity=0.0;
		xForce=0.0;
		yForce=0.0;
		mass=0.0;
		distToCenter=0.0;
	}
    public Particle(double diskRadius, double diskCenterX, double diskCenterY, double initialMass, double varyMass, double vel, double varyVel, double spinFactor, double varySpin, double spinRatio, double constantGravity, double baryMass) {
    	Random randGen = new Random();
    	xPosition=diskCenterX;
		yPosition=diskCenterY;
		xAcceleration=0.0;
		yAcceleration=0.0;
		xForce=0.0;
		yForce=0.0;
		randSpin=varySpin;
		mass=initialMass+((Math.random()*2.0-1.0)*varyMass);
		if (!(diskRadius==0.0)){
			//double rad = randGen.nextGaussian()*diskRadius/2;
			double radDist = Math.sqrt(randGen.nextDouble()*diskRadius*diskRadius);
			radDist *= Math.sqrt(radDist/diskRadius);
			double theta=randGen.nextDouble()*Math.PI*2;

			double xOffset=radDist*Math.cos(theta);
			double yOffset=radDist*Math.sin(theta);
			xPosition+=xOffset;
			yPosition+=yOffset;
			distToCenter=Math.sqrt((xPosition-diskCenterX)*(xPosition-diskCenterX)+(yPosition-diskCenterY)*(yPosition-diskCenterY));
			double spin=Math.sqrt((constantGravity*baryMass)/distToCenter)*spinFactor;
			xVelocity=vel+((Math.random()*2.0-1.0)*varyVel);
			yVelocity=vel+((Math.random()*2.0-1.0)*varyVel);
			double rad;
			if (Math.random()<spinRatio){
				rad = (randGen.nextGaussian()-randSpin)*Math.sqrt(radDist/diskRadius);
			}else{
				rad = (randGen.nextGaussian()+randSpin)*Math.sqrt(radDist/diskRadius);
			}
			if (randSpin==0.0){
				rad = 0;
			}
			theta=Math.atan2(yPosition,xPosition) + 0.5*Math.PI;
			xVelocity=+rad*Math.cos(theta);
			yVelocity=+rad*Math.sin(theta);
		}
    }
    public double getXPosition(){
    	return xPosition;
    }
	public double getYPosition(){
    	return yPosition;
    }
	public double getXAcceleration(){
    	return xAcceleration;
    }
	public double getYAcceleration(){
    	return yAcceleration;
    }
	public double getXVelocity(){
    	return xVelocity;
    }
	public double getYVelocity(){
    	return yVelocity;
    }
	public double getXForce(){
    	return xForce;
    }
	public double getYForce(){
    	return yForce;
    }
	public double getMass(){
    	return mass;
    }
    public double getRadius(){
    	return radius;
    }
	public void setXPosition(double newXPosition){
    	xPosition=newXPosition;
    }
	public void setYPosition(double newYPosition){
    	yPosition=newYPosition;
    }
	public void setXAcceleration(double newXAcceleration){
    	xAcceleration=newXAcceleration;
    }
	public void setYAcceleration(double newYAcceleration){
    	yAcceleration=newYAcceleration;
    }
	public void setXVelocity(double newXVelocity){
    	xVelocity=newXVelocity;
    }
	public void setYVelocity(double newYVelocity){
    	yVelocity=newYVelocity;
    }
	public void setXForce(double newXForce){
    	xForce=newXForce;
    }
	public void setYForce(double newYForce){
    	yForce=newYForce;
    }
	public void setMass(double newMass){
    	mass=newMass;
    	radius=Math.sqrt(mass);
    }
    public void updateVel(double deltaTime){
        xVelocity += (xForce*deltaTime)/mass;
        yVelocity += (yForce*deltaTime)/mass;
    }
    public void updatePos(double deltaTime){
	    xPosition += xVelocity*deltaTime;
        yPosition += yVelocity*deltaTime;
    }
    public void updateForce(double xF, double yF){
    	xForce += xF;
    	yForce += yF;
    }
    public void zeroForce(){
    	xForce = 0;
    	yForce = 0;
    }
    public String toString(){
    	String out = (int)((xPosition+0.0005)*1000)/1000.0 + " " + (int)((yPosition+0.0005)*1000)/1000.0 + " " + (int)((mass+0.0005)*1000)/1000.0;
    	return out;
    }
}