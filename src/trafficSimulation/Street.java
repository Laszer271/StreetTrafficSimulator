package trafficSimulation;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**
 * Street is a street that has lanes which are represented by carBuffers. Every lane stores cars that are in it.
 * Street also can have up to 2 crosses that it is connected to at its ends.
 * @author Wojciech Maciejewski
 *
 */
public class Street extends StreetObject{
	
	private CarBuffer[] lanes;
	private int lanesOriented;
	private final StreetObject[] ends = new StreetObject[2];
	
	private int maxVel;
	
	private int probability = 1;
	private int baseTime = 100;
	
	public static final int SOUTH_NORTH = 0;
	public static final int WEST_EAST = 1;
	private final int orientation;
	
	/**
	 * 
	 * @param TopLeftX X-coordinate of the top left corner of the new street
	 * @param TopLeftY Y-coordinate of the top left corner of the new street
	 * @param BottomRightX X-coordinate of the bottom right corner of the new street
	 * @param BottomRightY Y-coordinate of the bottom right corner of the new street
	 * @param numOfLanes Number of lanes of the new street
	 * @param lanesOriented Number of lanes directed towards north/east
	 * @param orientation 0 if the new street is south-north oriented, 1 otherwise
	 */
	public Street(int TopLeftX, int TopLeftY, int BottomRightX, int BottomRightY, int numOfLanes,
				  int lanesOriented, int orientation) {
		
		super(TopLeftX, TopLeftY, BottomRightX, BottomRightY);
		
		this.lanesOriented = lanesOriented;
		this.orientation = orientation;
		
		int start;
		int end;
		Direction dir1;
		Direction dir2;
		
		if(orientation == SOUTH_NORTH) {
			start = BottomRightY;
			end = TopLeftY;
			dir1 = Direction.North;
			dir2 = Direction.South;
		}
		else {
			start = TopLeftX;
			end = BottomRightX;
			dir1 = Direction.East;
			dir2 = Direction.West;
		}
		
		maxVel = 50000; // millimeters per second
		
		lanes = new CarBuffer[numOfLanes];
		for(int i = 0; i < lanesOriented; ++i) { //lanesOriented value means how many lanes should be directed towards North/East
			lanes[i] = new CarBuffer();
			lanes[i].setStart(start);
			lanes[i].setEnd(end);
			lanes[i].setMaxVel(maxVel);
			lanes[i].setDirection(dir1);
		}
		
		for(int i = lanesOriented; i < numOfLanes; ++i) {
			lanes[i] = new CarBuffer();
			lanes[i].setStart(end);
			lanes[i].setEnd(start);
			lanes[i].setMaxVel(maxVel);
			lanes[i].setDirection(dir2);
		}
	}
	
	/**
	 * 
	 * @param TopLeftX X-coordinate of the top left corner of the new street
	 * @param TopLeftY Y-coordinate of the top left corner of the new street
	 * @param BottomRightX X-coordinate of the bottom right corner of the new street
	 * @param BottomRightY Y-coordinate of the bottom right corner of the new street
	 * @param numOfLanes Number of lanes of the new street
	 * @param lanesOriented Number of lanes directed towards north/east
	 * @param orientation 0 if the new street is south-north oriented, 1 otherwise
	 * @param obj1 StreetObject connected with the new street that will be the first end of the street
	 * @param obj2 StreetObject connected with the new street that will be the second end of the street
	 */
	public Street(int TopLeftX, int TopLeftY, int BottomRightX, int BottomRightY, int numOfLanes,
				  int lanesOriented, int orientation, StreetObject obj1, StreetObject obj2) {
		
		this(TopLeftX, TopLeftY, BottomRightX, BottomRightY, numOfLanes, lanesOriented, orientation);
		
		ends[0] = obj1;
		ends[1] = obj2;
	}
	
	/**
	 * Adds the given StreetObject as a first end of the current street
	 * @param obj StreetObject that will become first end of the current street
	 */
	public void addFirstEnd(StreetObject obj) {
		ends[0] = obj;
	}
	
	/**
	 * Adds the given StreetObject as a second end of the current street
	 * @param obj StreetObject that will become second end of the current street
	 */
	public void addSecondEnd(StreetObject obj) {
		ends[1] = obj;
	}
	
	@Override
	/**
	 * Calls tick of every CarBuffer in this street
	 * @param time Time which will be simulated to be passed during the tick
	 */
	public void tick(int time) {		
		for(int i = 0; i < lanes.length; ++i) {
			lanes[i].tick(time);
		}
	}
	
	@Override
	/**
	 * Calls renderCars of every CarBuffer in this street
	 * @param g Graphics parameter needed to do the drawing
	 * @param xOffset An offset on the x-axis that determines how far from their stored position the cars should be drawn
	 * @param yOffset An offset on the y-axis that determines how far from their stored position the cars should be drawn
	 */
	public void renderCars(Graphics g, int xOffset, int yOffset) {

		for(int i = 0; i < lanes.length; ++i) {
			lanes[i].render(g, xOffset, yOffset);
		}
	}
	
	@Override
	/**
	 * Renders the road without any cars
	 * @param g Graphics parameter needed to do the drawing
	 * @param xOffset An offset on the x-axis that determines how far from its stored position the road should be drawn
	 * @param yOffset An offset on the y-axis that determines how far from its stored position the road should be drawn
	 */
	public void renderRoad(Graphics g, int xOffset, int yOffset) {
		g.setColor(Color.black);
		
		int x = TopLeftX / Model.millimetersPerPixel + xOffset;
		int y = TopLeftY / Model.millimetersPerPixel + yOffset;
		int width = super.getWidth() / Model.millimetersPerPixel;
		int height = super.getHeight() / Model.millimetersPerPixel;
		
		g.fillRect(x, y, width, height);
		
		g.setColor(Color.red);
		g.drawRect(x, y, width, height);
	}
	
	/**
	 * Makes new Cross north of the Street and connects it to the street making it the second end of the street
	 * @return The new cross
	 */
	public Cross makeCrossNorth() {
		Cross cross = new Cross(TopLeftX, TopLeftY - getWidth(), BottomRightX, TopLeftY);
		cross.addSouthObject(this);
		addSecondEnd(cross);
		return cross;
	}
	
	/**
	 * Makes new Cross east of the Street and connects it to the street making it the second end of the street
	 * @return The new cross
	 */
	public Cross makeCrossEast() {
		Cross cross = new Cross(BottomRightX, TopLeftY, BottomRightX + getWidth(), BottomRightY);
		cross.addWestObject(this);
		addSecondEnd(cross);
		return cross;
	}
	
	/**
	 * Makes new Cross south of the Street and connects it to the street making it the second end of the street
	 * @return The new cross
	 */
	public Cross makeCrossSouth() {
		Cross cross = new Cross(TopLeftX, BottomRightY, BottomRightX, BottomRightY + getWidth());
		cross.addNorthObject(this);
		addSecondEnd(cross);
		return cross;
	}
	
	/**
	 * Makes new Cross west of the Street and connects it to the street making it the second end of the street
	 * @return The new cross
	 */
	public Cross makeCrossWest() {
		Cross cross = new Cross(TopLeftX - getWidth(), TopLeftY, TopLeftX, BottomRightY);
		cross.addEastObject(this);
		addSecondEnd(cross);
		return cross;
	}
	
	/**
	 * 
	 * @return The max velocity allowed at the street
	 */
	public int getMaxVel() {
		return maxVel;
	}
	
	/**
	 * 
	 * @return True if orientation is south-north, false otherwise
	 */
	public boolean isSouthNorthOriented() {
		return orientation == SOUTH_NORTH;
	}
	
	/**
	 * 
	 * @return True if orientation is west-east, false otherwise
	 */
	public boolean isWestEastOriented() {
		return orientation == WEST_EAST;
	}
	
	@Override
	/**
	 * 
	 * @return The width of the street
	 */
	public int getWidth() {
		if(orientation == SOUTH_NORTH)
			return BottomRightX - TopLeftX;
		
		return BottomRightY - TopLeftY;
	}
	
	/**
	 * 
	 * @return The length of the street
	 */
	public int getLength() {
		if(orientation == SOUTH_NORTH)
			return BottomRightY - TopLeftY;
		
		return BottomRightX - TopLeftX;
	}
	
	/**
	 * 
	 * @return The first end of the street
	 */
	public StreetObject getStart() {
		return ends[0];
	}
	
	/**
	 * 
	 * @return The second end of the street
	 */
	public StreetObject getEnd() {
		return ends[1];
	}
	
	/**
	 * Changes the probability of new car appearing during any given tick
	 * @param prob New probability
	 */
	public void setProbability(int prob) {
		probability = prob;
	}
	
	/**
	 * Changes the base time
	 * @param bTime new base time
	 */
	public void setBaseTime(int bTime) {
		baseTime = bTime;
	}
	
	/**
	 * Adds the car given as parameter to the right carBuffer
	 * @param car The car that has arrived at the street
	 * @param startingPoint The point at which the car should appear
	 * @param whichDirection 0 if the car is moving south-north, 1 otherwise
	 */
	public void insert (Car car, int startingPoint, int whichDirection) {
		int indexOfBuffer = decideCarBuffer(whichDirection);
		int offset = getWidth() / (lanes.length * 2);
		int position;
		if(isSouthNorthOriented()) {
			position = TopLeftX + (2 - indexOfBuffer) * offset;
			car.setXPos(position);
			
		}
		else {
			position = TopLeftY + (2 - indexOfBuffer) * offset;;
			car.setYPos(position);
		}
		
		lanes[indexOfBuffer].addCar(car, startingPoint);
	}
	
	/**
	 * Decides which buffer should be used for the new car
	 * @param whichDirection 0 if the car is moving south-north, 1 otherwise
	 * @return The index of the buffer that was chosen
	 */
	private int decideCarBuffer(int whichDirection) {
		int indexToReturn = 0;
		if(whichDirection == 0) {
			int numberOfCars = Integer.MAX_VALUE;
			for(int i = 0; i < lanesOriented; ++i) { //lanesOriented value means how many lanes should be directed towards North/East
				if(lanes[i].getCount() < numberOfCars) {
					indexToReturn = i;
					numberOfCars = lanes[i].getCount();
				}
			}
		}
		else {
			int numberOfCars = Integer.MAX_VALUE;
			for(int i = lanesOriented; i < lanes.length; ++i) {
				if(lanes[i].getCount() < numberOfCars) {
					indexToReturn = i;
					numberOfCars = lanes[i].getCount();
				}
			}
		}
		
		return indexToReturn;
	}
	
	/**
	 * Based on the time given as parameter, the base time and the probability - decides how many new cars should appear in the street at any given tick
	 * @param time Time on which the chances of new car appearing depend
	 * @return The number of cars that should appear in the current tick
	 */
	public int numberOfCarsToBeGenerated(int time) {
		int numberToReturn = 0;
		Random r = new Random();
		
		int count = time / baseTime;
		int remain = time - count * baseTime;

		for(int i = 0; i < count; ++i) {
			if(r.nextInt(100) < probability)
				++numberToReturn;
		}
		
		int remainingProbability = (int)(probability * (float)remain / baseTime);
		if(r.nextInt(100) < remainingProbability)
			++numberToReturn;
		
		return numberToReturn;
	}
	
	/**
	 * Removes from the cars that are the first cars in their buffers the car that was given as parameter
	 * @param car Car to be removed
	 */
	public void removeFirst(Car car) {
		for(int i = 0; i < lanes.length; ++i) {
			if(lanes[i].getFirst() == car) {
				lanes[i].pop();
				break;
			}
		}
	}
	
	/**
	 * Removes from the cars that are the last cars in their buffers the car that was given as parameter
	 * @param car Car to be removed
	 */
	public void removeLast(Car car) {
		for(int i = 0; i < lanes.length; ++i) {
			if(lanes[i].getLast() == car) {
				lanes[i].removeLast();
				break;
			}
		}
	}
	
	/**
	 * Pushes the car given as parameter to the beginning of the street
	 * @param car The car to be addet to one of the carBuffers of the street
	 */
	public void push(Car car) {
		if(car.getDirection() == Direction.North || car.getDirection() == Direction.East) {
			insert(car, 0, 0);
		}
		else {
			insert(car, 0, 1);
		}
	}

}
