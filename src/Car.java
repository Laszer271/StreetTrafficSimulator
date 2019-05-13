package trafficSimmulation;

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
	private final int acceleration; // in millimeters per second
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
		//System.out.println("velocity: " + velocity + " mm/ms"); //DEBUG

		if(velocity >= maxVel) {
			velocity = maxVel;
			return maxVel * time;
		}
		else {
			int deltaVel = maxVel - velocity;
			int timeAccelerating =  1000 * deltaVel / acceleration;
			if(timeAccelerating > time) {
				timeAccelerating = time;
			}
			
			int distance = 	timeAccelerating * velocity + ((acceleration * timeAccelerating * timeAccelerating / 1000) >>> 1) +
							+ (time - timeAccelerating) * maxVel;
			velocity += timeAccelerating * acceleration;
			return distance;
		}
	}
	
	public void tick(int time, Car other, int maxVel) {
		System.out.println("Car (not first) ticked");
		int distanceToMove = calculateDistToMove(time, maxVel);
		int distanceBetweenCars;
		
		if(xPos == other.getXPos()) {
			distanceBetweenCars = yPos - other.getYPos();
		}
		else {
			distanceBetweenCars = xPos - other.getXPos();
		}
		System.out.println("BetweenCars: " + distanceBetweenCars);
		
		if(direction == Direction.North || direction == Direction.West) {
			distanceToMove = ~distanceToMove + 1;
			distanceBetweenCars = ~distanceBetweenCars + 1;
		}
		
		System.out.println("Move: " + distanceToMove);

		if(distanceToMove > distanceBetweenCars) {
			distanceToMove = distanceBetweenCars;
		}
		
		if(xPos == other.getXPos()) {
			yPos += distanceToMove;
		}
		else {
			xPos += distanceToMove;
		}
	}
	
	public void tick(int time, int end, int maxVel) {
		int distanceToMove = calculateDistToMove(time, maxVel);
		int position;
		
		if(!isWaitingForGreenLight) {
			//System.out.println("Car (first) ticked   Distance: " + distanceToMove); //DEBUG
			
			if(direction == Direction.North) {
				position = yPos - distanceToMove;
				if(position > end) {
					//System.out.println("Moved : " + (yPos - position));
					yPos = position;
					
				}
				else {
					yPos = end;
					isWaitingForGreenLight = true;
				}
			}
			else if(direction == Direction.East) {
				position = xPos + distanceToMove;
				if(position < end) {
					//System.out.println("Moved : " + (position - xPos));
					xPos = position;
					
				}
				else {
					xPos = end;
					isWaitingForGreenLight = true;
				}
			}
			else if(direction == Direction.South) {
				position = yPos + distanceToMove;
				if(position < end) {
					//System.out.println("Moved : " + (position - yPos));
					yPos = position;
					
				}
				else {
					yPos = end;
					isWaitingForGreenLight = true;
				}
			}
			else {
				position = xPos - distanceToMove;
				if(position > end) {
					//System.out.println("Moved : " + (position - xPos));
					xPos = position;
					
				}
				else {
					xPos = end;
					isWaitingForGreenLight = true;
				}
			}
		}
		
		if(isWaitingForGreenLight) {
			
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		int length = direction == Direction.North || direction == Direction.South ? this.width / Model.MILLIMETRES_PER_PIXEL : this.length / Model.MILLIMETRES_PER_PIXEL;
		int width = direction == Direction.North || direction == Direction.South ? this.length / Model.MILLIMETRES_PER_PIXEL : this.width / Model.MILLIMETRES_PER_PIXEL;
		g.fillRect(xPos / Model.MILLIMETRES_PER_PIXEL, yPos / Model.MILLIMETRES_PER_PIXEL, length, width);
	}
}
