package trafficSimulation;

import java.awt.Color;
import java.awt.Graphics;

public class Car {
	private int xPos, yPos;	// position of the back of the car
	
	private final int length;
	private final int width;
	private final int area; // it's length of the car + the minimal length of gap 
							// between this car and the one in front of it.
	
	public Direction direction; 
	
	private int velocity; // current velocity in millimeters per second
	private final int acceleration; // in millimeters per second^2
	private final int velModifier; // the car will try to get to the speed of 
								   // allowed speed at the street + his velModifier
	
	private Street[] route;
	private int currentStreet = 0;
	
	private boolean isWaitingForGreenLight = false;
	
	public static final int STANDARD_SIZE = 4000; //4000 millimetres is the standard length of a car
	
	public Car(int x, int y, int len, int width, int gap, Direction dir,
			int vel, int acc, int vMod, Street[] rt) {
		this.xPos = x;
		this.yPos = y;
		this.length = len;
		this.width = width;
		this.area = len + gap;
		this.direction = dir;
		this.velocity = vel;
		this.acceleration = acc;
		this.velModifier = vMod;
		this.route = rt;
	}
	
	public int getXPos() {
		return xPos;
	}
	
	public int getYPos() {
		return yPos;
	}
	
	public int getLength() {
		return length;
	}
	
	public int getArea() {
		return area;
	}
	
	public int getGapToNext() {
		return area - length;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public int getVelocity() {
		return velocity;
	}
	
	public int getAcceleration() {
		return acceleration;
	}
	
	public int getVelModifier() {
		return velModifier;
	}
	
	public void setXPos(int x) {
		xPos = x;
	}
	
	public void setYPos(int y) {
		yPos = y;
	}
	
	public void setVelocity(int vel) {
		velocity = vel;
	}
	
	public void updateVelocity(int vel) {
		velocity += vel;
	}
	
	public void updateXPos(int x) {
		xPos += x;
	}
	
	public void updateYPos(int y) {
		yPos += y;
	}
	
	public void setRoute(Street[] rt) {
		route = rt;
	}
	
	public int calculateDistToMove(int time, int maxVel) {
		//A proper equation of calculating the distance in uniformly accelerated motion is not used as the callculation was meant
		//to be as fast as possible. Simplified algorithm (that works fine it time is small) is used instead
		
		//maxVel, velModifier and velocity are in millimeters per second
		//distance is in millimeters
		//acceleration is in millimeters per second squared

		maxVel += velModifier;
		//System.out.println("velocity: " + velocity  + " mm/s"); //DEBUG
		if(velocity >= maxVel) {
			velocity = maxVel;
		}
		
		int distance = velocity * time / 1000;
		velocity += acceleration * time / 1000;
		return distance;
	}
	
	//tick for all cars in lane except of the first one
	public void tick(int time, Car other, int maxVel) {
		
		int distanceBetweenCars;
		int distanceToMove;
		
		if(direction == Direction.North) {
			distanceBetweenCars = yPos - area - other.yPos;
			distanceToMove = calculateDistToMove(time, maxVel);
			
			if(distanceToMove > distanceBetweenCars) {
				distanceToMove = distanceBetweenCars;
			}
			
			yPos -= distanceToMove;
		}
		else if(direction == Direction.East) {
			distanceBetweenCars = other.xPos - area - xPos;
			distanceToMove = calculateDistToMove(time, maxVel);

			if(distanceToMove > distanceBetweenCars) {
				distanceToMove = distanceBetweenCars;
			}
			
			xPos += distanceToMove;
		}
		else if(direction == Direction.South) {
			distanceBetweenCars = other.yPos - area - yPos;
			distanceToMove = calculateDistToMove(time, maxVel);

			if(distanceToMove > distanceBetweenCars) {
				distanceToMove = distanceBetweenCars;
			}
			
			yPos += distanceToMove;
		}
		else {
			distanceBetweenCars = xPos - area - other.xPos;
			distanceToMove = calculateDistToMove(time, maxVel);

			if(distanceToMove > distanceBetweenCars) {
				distanceToMove = distanceBetweenCars;
			}
			
			xPos -= distanceToMove;
		}
	}
	
	//tick only for the first car in the lane
	public void tick(int time, int end, int maxVel) { 
		int distanceToMove = calculateDistToMove(time, maxVel);
		int position;
		
		if(!isWaitingForGreenLight) {
			
			if(direction == Direction.North) {
				position = yPos - distanceToMove;
				if(position > end) {
					yPos = position;
					
				}
				else {
					yPos = end + length;
					isWaitingForGreenLight = true;
				}
			}
			else if(direction == Direction.East) {
				position = xPos + distanceToMove;
				if(position < end) {
					xPos = position;
					
				}
				else {
					xPos = end - length;
					isWaitingForGreenLight = true;
				}
			}
			else if(direction == Direction.South) {
				position = yPos + distanceToMove;
				if(position < end) {
					yPos = position;
					
				}
				else {
					yPos = end - length;
					isWaitingForGreenLight = true;
				}
			}
			else {
				position = xPos - distanceToMove;
				if(position > end) {
					xPos = position;
				
				}
				else {
					xPos = end + length;
					isWaitingForGreenLight = true;
				}
			}
		}
		
		if(isWaitingForGreenLight) {
			
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		int length;
		int width;
		int topLeftX;
		int topLeftY;
		
		if(direction == Direction.North) {
			width = this.width / Model.MILLIMETRES_PER_PIXEL;
			length = this.length / Model.MILLIMETRES_PER_PIXEL;
			
			topLeftX = xPos / Model.MILLIMETRES_PER_PIXEL - width / 2;
			topLeftY = yPos / Model.MILLIMETRES_PER_PIXEL - length;
			
		}
		else if(direction == Direction.East) {
			width = this.length / Model.MILLIMETRES_PER_PIXEL;
			length = this.width / Model.MILLIMETRES_PER_PIXEL;
			
			topLeftX = xPos / Model.MILLIMETRES_PER_PIXEL;
			topLeftY = yPos / Model.MILLIMETRES_PER_PIXEL - length / 2;
		}
		else if(direction == Direction.South) {
			width = this.width / Model.MILLIMETRES_PER_PIXEL;
			length = this.length / Model.MILLIMETRES_PER_PIXEL;
			
			topLeftX = xPos / Model.MILLIMETRES_PER_PIXEL - width / 2;
			topLeftY = yPos / Model.MILLIMETRES_PER_PIXEL;
		}
		else { //direction == Direction.West
			width = this.length / Model.MILLIMETRES_PER_PIXEL;
			length = this.width / Model.MILLIMETRES_PER_PIXEL;
			
			topLeftX = xPos / Model.MILLIMETRES_PER_PIXEL - width;
			topLeftY = yPos / Model.MILLIMETRES_PER_PIXEL - length / 2;
		}
		
		g.fillRect(topLeftX, topLeftY, width, length);
	}
}
