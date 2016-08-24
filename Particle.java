/**
 * @(#)Particle.java
 *
 *
 * @author 
 * @version 1.00 2016/8/24
 */


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
	}
    public Particle(double diskRadius, double diskCenterX, double diskCenterY, double startingMass, double vel) {
    	xPosition=diskCenterX;
		yPosition=diskCenterY;
		xAcceleration=0.0;
		yAcceleration=0.0;
		xVelocity=(Math.random()*2.0-1.0)*vel;
		yVelocity=(Math.random()*2.0-1.0)*vel;
		xForce=0.0;
		yForce=0.0;
		mass=startingMass;
		double xOffset=(Math.random()*2.0-1.0)*diskRadius;
		double yOffset=(Math.random()*2.0-1.0)*diskRadius;
		while (Math.sqrt(xOffset*xOffset+yOffset*yOffset)>diskRadius){
			xOffset=Math.random()*diskRadius;
			yOffset=Math.random()*diskRadius;
		}
		xPosition+=xOffset;
		yPosition+=yOffset;
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
    }
    
}