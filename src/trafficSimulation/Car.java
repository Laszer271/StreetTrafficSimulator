package trafficSimulation;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

/**
 * Car is representing a car that is moving by itself according to some rules during the simulation
 * @author Wojciech Maciejewski
 *
 */
public class Car {
	private int xPos, yPos;	// position of the back of the car
	
	private final int length;
	private final int width;
	private final int area; // it's length of the car + the minimal length of gap 
							// between this car and the one in front of it.
	
	private Direction direction; 
	private final int reactionTime;
	
	private int velocity; // current velocity in millimeters per second
	private final int acceleration; // in millimeters per second^2
	private final int velModifier; // the car will try to get to the speed of 
								   // allowed speed at the street + his velModifier
	
	private boolean reachedDestination; // if true - it's a signal for the buffer to delete this car
	
	private int millisToWait; // defines how many seconds the car should wait before accelerating (simulates reaction time of the driver)
	private boolean isWaitingForTheNext;
	
	@SuppressWarnings("unused")
	private LinkedList<Street> route;
	private int end; // at what coordinate of the last street in the route the car should end his trip
	
	/**
	 * standard length of the car
	 */
	public static final int STANDARD_SIZE = 4000; 

	/**
	 * 
	 * @param len Length of the new car
	 * @param width Width of the new car
	 * @param gap Gap to the car in front of the new car that the new car keeps
	 * @param dir Direction in which the new car is moving
	 * @param rTime ReactionTime of the new Car. reactionTime simulates the reactionTime of the real drivers
	 * @param vel Starting velocity for the new car
	 * @param acc Accuracy that the new car will have
	 * @param vMod Velocity modifier - determines how the maximum speed of the new car differs from the maximum speed of the street it's on
	 * @param end Determines at what point of the last street the car will end its journey
	 * @param route LinkedList that holds every street that the new car will go through before reaching his destination (last street from the list is the destination)
	 */
	public Car(int len, int width, int gap, Direction dir, int rTime,
			int vel, int acc, int vMod, int end, LinkedList<Street> route) {

		this.length = len;
		this.width = width;
		this.area = len + gap;
		this.direction = dir;
		this.reactionTime = rTime;
		this.velocity = vel;
		this.acceleration = acc;
		this.velModifier = vMod;
		this.end = end;
		this.route = route;
	}
	
	/**
	 * 
	 * @return X coordinate of the back of the car
	 */
	public int getXPos() {
		return xPos;
	}
	
	/**
	 * 
	 * @return Y coordinate of the back of the car
	 */
	public int getYPos() {
		return yPos;
	}
	
	/**
	 * 
	 * @return The length of the car
	 */
	public int getLength() {
		return length;
	}
	
	/**
	 * 
	 * @return The sum of the length and the gap of the car
	 */
	public int getArea() {
		return area;
	}
	
	/**
	 * 
	 * @return The gap that the car keeps to the car that is in front of it
	 */
	public int getGapToNext() {
		return area - length;
	}
	
	/**
	 * 
	 * @return The direction in which the car is driving
	 */
	public Direction getDirection() {
		return direction;
	}
	
	/**
	 * 
	 * @return Current velocity of the car
	 */
	public int getVelocity() {
		return velocity;
	}
	
	/**
	 * 
	 * @return The acceleration of the car
	 */
	public int getAcceleration() {
		return acceleration;
	}
	
	/**
	 * 
	 * @return The velocityModifier of the car which says how the maximum velocity of the car differs from the maximum velocity of the street
	 */
	public int getVelModifier() {
		return velModifier;
	}
	
	/**
	 * Changes the x coordinate of the back of the car
	 * @param x New x coordinate of the back of the car
	 */
	public void setXPos(int x) {
		xPos = x;
	}
	
	/**
	 * Changes the y coordinate of the back of the car
	 * @param y New y coordinate of the back of the car
	 */
	public void setYPos(int y) {
		yPos = y;
	}
	
	/**
	 * Changes current velocity of the car
	 * @param vel New velocity of the car
	 */
	public void setVelocity(int vel) {
		velocity = vel;
	}
	
	/**
	 * Adds velocity to the current velocity of the car
	 * @param vel Velocity to be added to current velocity
	 */
	public void updateVelocity(int vel) {
		velocity += vel;
	}
	
	/**
	 * Changes the route that the car follows
	 * @param rt New route for the car to follow
	 */
	public void setRoute(LinkedList<Street> rt) {
		route = rt;
	}
	
	/**
	 * Calculates the distance that the car would travel if it didn't encounter any obstacles.
	 * @param time Time that the method uses to calculate the distance to move
	 * @param maxVel Max Velocity of the street that the car is currently in.
	 * @return The distance that the car would travel if it didn't encounter any obstacles
	 */
	public int calculateDistToMove(int time, int maxVel) {
		//A proper equation of calculating the distance in uniformly accelerated motion is not used as the callculation was meant
		//to be as fast as possible. Simplified algorithm (that works fine if time is small) is used instead
		
		//maxVel, velModifier and velocity are in millimeters per second
		//distance is in millimeters
		//acceleration is in millimeters per second squared

		maxVel += velModifier;
		if(velocity >= maxVel) {
			velocity = maxVel;
		}
		
		int distance = velocity * time / 1000;
		velocity += acceleration * time / 1000;
		return distance;
	}
	
	private int	getMinDistance(int distanceBetweenCars, int distanceToMove, int otherVel) {
		if(distanceToMove > distanceBetweenCars) {
			distanceToMove = distanceBetweenCars;
			velocity = otherVel;
			if(velocity == 0)
				isWaitingForTheNext = true;
		}
		
		return distanceToMove;
	}
	
	private int wait(int time) {
		if(millisToWait != 0) {
			millisToWait -= time;
			if(millisToWait < 0) {
				time = ~millisToWait + 1; //time = millisToWait * (-1)
				millisToWait = 0;
			}
			else {
				time = 0;
			}
		}
		
		return time;
	}
	
	private int wait(int time, Car other) {
		if(isWaitingForTheNext && other.getMillisToWait() != 0) {
			millisToWait = reactionTime;
			return 0;
		}
			
		isWaitingForTheNext = false;
		
		return wait(time);
	}
	
	/**
	 * Updates the state of the car. It's being used by every car except of the cars that are first on their roads
	 * @param time Time that is simulated to be passed during the tick
	 * @param other The car that is in front of the current car
	 * @param maxVel The max Velocity of the street the car is currently in
	 */
	public void tick(int time, Car other, int maxVel) {
		
		int distanceBetweenCars;

		time = wait(time, other);
		if(time == 0)
			return;

		int distanceToMove = calculateDistToMove(time, maxVel);
		
		if(direction == Direction.North) {
			distanceBetweenCars = yPos - area - other.yPos;
			
			distanceToMove = getMinDistance(distanceBetweenCars, distanceToMove, other.velocity);
			
			yPos -= distanceToMove;
			if(yPos <= end && route.size() == 1) {
				reachedDestination = true;
			}
		}
		else if(direction == Direction.East) {
			distanceBetweenCars = other.xPos - area - xPos;

			distanceToMove = getMinDistance(distanceBetweenCars, distanceToMove, other.velocity);
			
			xPos += distanceToMove;
			if(xPos >= end && route.size() == 1) {
				reachedDestination = true;
			}
		}
		else if(direction == Direction.South) {
			distanceBetweenCars = other.yPos - area - yPos;

			distanceToMove = getMinDistance(distanceBetweenCars, distanceToMove, other.velocity);
			
			yPos += distanceToMove;
			if(yPos >= end && route.size() == 1) {			
				reachedDestination = true;
			}
		}
		else {
			distanceBetweenCars = xPos - area - other.xPos;

			distanceToMove = getMinDistance(distanceBetweenCars, distanceToMove, other.velocity);
			
			xPos -= distanceToMove;
			if(xPos <= end && route.size() == 1) {			
				reachedDestination = true;
			}
		}
	}
	
	/**
	 * Updates the state of the car. It's being used only by the cars that are the first car on their roads
	 * @param time Time that is simulated to be passed during the tick
	 * @param end The end of the current road
	 * @param maxVel The max Velocity of the street the car is currently in
	 */
	public void tick(int time, int end, int maxVel) { 
		
		time = wait(time);
		if(time == 0)
			return;
		
		int distanceToMove = calculateDistToMove(time, maxVel);
		
		if(direction == Direction.North) {
			int distanceAvailable = yPos - end - area;
			
			if(distanceToMove <= distanceAvailable) {
				yPos -= distanceToMove;
				
				if(yPos - area <= end && route.size() == 1) 
					reachedDestination = true;
			}
			else {
				yPos = end + area;
				goThrough();
			}
		}
		else if(direction == Direction.East) {
			int distanceAvailable = end - xPos - area;
			
			if(distanceToMove <= distanceAvailable) {
				xPos += distanceToMove;
				
				if(xPos + area >= end && route.size() == 1) 
					reachedDestination = true;
			}
			else {
				xPos = end - area;
				goThrough();
			}
		}
		else if(direction == Direction.South) {
			int distanceAvailable = end - yPos - area;

			if(distanceToMove <= distanceAvailable) {
				yPos += distanceToMove;
				
				if(yPos + area >= end && route.size() == 1) 
					reachedDestination = true;

			}
			else {
				yPos = end - area;
				goThrough();
			}
		}
		else {
			int distanceAvailable = xPos - end - area;
			
			if(distanceToMove <= distanceAvailable) {
				xPos -= distanceToMove;
				
				if(xPos - area <= end && route.size() == 1) 
					reachedDestination = true;

			}
			else {
				xPos = end + area;
				goThrough();
			}
		}
	}
	
	/**
	 * A method to draw the car
	 * @param g A graphics parameter that is used to draw the car
	 * @param xOffset An offset on the x-axis that determines how far from the stored position the car should be drawn
	 * @param yOffset An offset on the y-axis that determines how far from the stored position the car should be drawn
	 */
	public void render(Graphics g, int xOffset, int yOffset) {
		g.setColor(Color.green);
		int length;
		int width;
		int topLeftX;
		int topLeftY;
		
		if(direction == Direction.North) {
			width = this.width / Model.millimetersPerPixel;
			length = this.length / Model.millimetersPerPixel;
			
			topLeftX = xPos / Model.millimetersPerPixel - width / 2;
			topLeftY = yPos / Model.millimetersPerPixel - length;
			
		}
		else if(direction == Direction.East) {
			width = this.length / Model.millimetersPerPixel;
			length = this.width / Model.millimetersPerPixel;
			
			topLeftX = xPos / Model.millimetersPerPixel;
			topLeftY = yPos / Model.millimetersPerPixel - length / 2;
		}
		else if(direction == Direction.South) {
			width = this.width / Model.millimetersPerPixel;
			length = this.length / Model.millimetersPerPixel;
			
			topLeftX = xPos / Model.millimetersPerPixel - width / 2;
			topLeftY = yPos / Model.millimetersPerPixel;
		}
		else { //direction == Direction.West
			width = this.length / Model.millimetersPerPixel;
			length = this.width / Model.millimetersPerPixel;
			
			topLeftX = xPos / Model.millimetersPerPixel - width;
			topLeftY = yPos / Model.millimetersPerPixel - length / 2;
		}
		
		g.fillRect(topLeftX + xOffset, topLeftY + yOffset, width, length);
	}
	
	/**
	 * Checks if the car is heading either North or South
	 * @return True if the car is heading either North or South. False otherwise
	 */
	public boolean isNorthSouthOriented() {
		return direction == Direction.North || direction == Direction.South;
	}
	
	/**
	 * Checks if the car is heading either West or East
	 * @return True if the car is heading either West or East. False otherwise
	 */
	public boolean isWestEastOriented() {
		return direction == Direction.West || direction == Direction.East;
	}
	
	/**
	 * A method used by the car that is the first car on his current road if he encounters the end on the road. It's making the car move forward
	 * onto the next road or makes it wait if it is unable to cross the junction (for example because of the red light).e
	 */
	void goThrough() {
		
		if(route.size() < 2) {
			route.getFirst().removeFirst(this);
			route.pop();
			return;
		}
		
		Cross nextCross = Graph.getCrossBetweenStreets(route.getFirst(), route.get(1));
		if((isNorthSouthOriented() && nextCross.getLight() == Cross.NORTH_SOUTH) ||
		   (isWestEastOriented() && nextCross.getLight() == Cross.WEST_EAST)) {
			
			route.getFirst().removeFirst(this);
			route.pop();
			
			Direction earlierDir = direction;
			if(nextCross.getNorthStreet() == route.getFirst())
				direction = Direction.North;
			else if(nextCross.getEastStreet() == route.getFirst())
				direction = Direction.East;
			else if(nextCross.getSouthStreet() == route.getFirst())
				direction = Direction.South;
			else
				direction = Direction.West;
			
			route.getFirst().push(this);
			
			if(earlierDir != direction) {
				velocity = velocity >>> 1;
			}
		}
		else {
			velocity = 0;
			millisToWait = reactionTime;
		}
		
	}
	
	/**
	 * 
	 * @return The boolean value that tells whether the car has already reached its destination or not
	 */
	public boolean getReachedDestination() {
		return reachedDestination;
	}
	
	/**
	 * 
	 * @return The route that the car follows
	 */
	public LinkedList<Street> getRoute() {
		return route;
	}
	
	/**
	 * 
	 * @return The time in milliseconds that the cars waits before moving 
	 */
	public int getMillisToWait() {
		return millisToWait;
	}
}
