package trafficSimulation;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * CarBuffer is a buffer of cars. It's function is to simulate a lane on the road
 * @author Wojciech Maciejewski
 *
 */
public class CarBuffer {
	
	private LinkedList<Car> carBuff = new LinkedList<Car>(); //buffer of cars
	private LinkedList<Car> carQueue = new LinkedList<Car>(); // queue of cars that want to get onto the lane
	
	private Direction dir;
	
	private int start;
	private int end;
	private int maxVel;
	
	CarBuffer(){}
	
	/**
	 * 
	 * @return The last car in the buffer (the last on the road)
	 */
	public Car getLast() {
		if(carBuff.size() == 0)
			return null;

		return carBuff.getLast();
	}
	
	/**
	 * 
	 * @return The first car in the buffer (the first car on the road)
	 */
	public Car getFirst() {
		if(carBuff.size() == 0)
			return null;

		return carBuff.getFirst();
	}
	
	/**
	 * 
	 * @param which Determines which car to return
	 * @return Returns the car of the index specified by which
	 */
	public Car get(int which) {
		return carBuff.get(which);
	}
	
	/**
	 * 
	 * @return The number of cars in the buffer
	 */
	public int getCount() {
		return carBuff.size();
	}
	
	/**
	 * 
	 * @return The x coordinate of the last car in the buffer
	 */
	public int getLastCarXPos() {
		return carBuff.getLast().getXPos();
	}
	
	/**
	 * 
	 * @return The y coordinate of the last car in the buffer
	 */
	public int getLastCarYPos() {
		return carBuff.getLast().getYPos();
	}
	
	/**
	 * Removes the first car in the buffer from the buffer and returns it
	 * @return The first car in the buffer
	 */
	public Car pop() {
		return carBuff.pop();
	}
	
	/**
	 * Adds the car given as parameter to the buffer
	 * @param car The car to be added to the buffer
	 */
	public void push(Car car) {
		carBuff.add(car);
	}
	
	/**
	 * 
	 * @return The direction of the road
	 */
	public Direction getDirection() {
		return dir;
	}
	
	/**
	 * Sets the start position of the buffer
	 * @param start The new start position of the buffer
	 */
	public void setStart(int start) {
		this.start = start;
	}
	
	/**
	 * Sets the end position of the buffer
	 * @param end The new end position of the buffer
	 */
	public void setEnd(int end) {
		this.end = end;
	}
	
	/**
	 * Changes the maximum velocity of the road to the new value given as parameter
	 * @param vel The new value of the maximum velocity of the road
	 */
	public void setMaxVel(int vel) {
		maxVel = vel;
	}
	
	private void tickCarQueue() {
		for(ListIterator<Car> iterator = carQueue.listIterator(); iterator.hasNext();) {
			Car tempCar = iterator.next();
			int startingPoint;
			
			if(dir == Direction.North || dir == Direction.South)
				startingPoint = tempCar.getYPos();
			else 
				startingPoint = tempCar.getXPos();
			
			int index = getPlace(startingPoint, tempCar.getArea());
			
			if(index != -1) {
				carBuff.add(index, tempCar);
				iterator.remove();
			}
		}
	}
	
	private void tickFirst(int time) {
		int sizeBefore = carBuff.size() + 1;
		while(carBuff.size() != 0 && sizeBefore != carBuff.size()) {
			sizeBefore = carBuff.size();
			Car tempCar = carBuff.getFirst();
			tempCar.tick(time, end, maxVel);
			
			if(tempCar.getReachedDestination() && sizeBefore == carBuff.size()) {
				carBuff.pop();
			}
		}
	}
	
	private void tickOthersThanFirst(int time) {
		for(ListIterator<Car> iterator = carBuff.listIterator(1); iterator.hasNext();) {
			Car prevCar = iterator.previous();
			iterator.next();
			Car currentCar = iterator.next();
			currentCar.tick(time, prevCar, maxVel);
			
			if(currentCar.getReachedDestination()) {
				iterator.remove();
			}
		}
	}
	
	/**
	 * Makes every car in the buffer do a tick and also checks if any of the cars that couldn't be moved on the road can be moved now. 
	 * If they can - they are
	 * @param time Determines the time to be simulated during the tick
	 */
	public void tick(int time) {
		
		tickCarQueue();
		tickFirst(time);
		
		if(carBuff.size() <= 1)
			return;
		
		tickOthersThanFirst(time);
	}
	
	/**
	 * Draws every car that is currently in the buffer
	 * @param g A graphics parameter needed to do the drawing
	 * @param xOffset An offset on the x-axis that determines how far from the stored position should be drawn
	 * @param yOffset An offset on the y-axis that determines how far from the stored position should be drawn
	 */
	public void render(Graphics g, int xOffset, int yOffset) {
		for(ListIterator<Car> iterator = carBuff.listIterator(); iterator.hasNext();) {
			iterator.next().render(g, xOffset, yOffset);
		}
	}
	
	/**
	 * Changes the direction of the road
	 * @param dir The new direction of the road
	 */
	public void setDirection(Direction dir) {
		this.dir = dir;
	}

	/**
	 * A method used to add new car to the buffer
	 * @param car A car to be added
	 * @param startingPoint A position in which the new car should appear
	 */
	public void addCar(Car car, int startingPoint) {
		
		if(dir == Direction.North) {
			startingPoint = start - startingPoint;
			car.setYPos(startingPoint);
		}
		else if(dir == Direction.East) {
			startingPoint += start;
			car.setXPos(startingPoint);
		}
		else if(dir == Direction.South) {
			startingPoint += start;
			car.setYPos(startingPoint);
		}
		else {
			startingPoint = start - startingPoint;
			car.setXPos(startingPoint);
		}
		
		int index = getPlace(startingPoint, car.getArea());
		if(index != -1)
			carBuff.add(index, car);
		else
			carQueue.add(car);
		
	}
	
	/**
	 * Removes the last car in the buffer
	 */
	public void removeLast() {
		carBuff.removeLast();
	}
	
	/**
	 * A method used to determine at what index the new car should be added to the buffer
	 * @param startingPoint The position in the buffer that the new car should appear
	 * @param area The area (length + gap) that the new car reserves
	 * @return An index at which the new car should be added or -1 if there is no space for the new car right now
	 */
	private int getPlace(int startingPoint, int area) {
		
		if(carBuff.isEmpty())
			return 0;
		
		for(ListIterator<Car> iterator = carBuff.listIterator(); iterator.hasNext();) {
			Car currentCar = iterator.next();
			
			if(dir == Direction.North && currentCar.getYPos() - currentCar.getArea() > startingPoint) { //Found a car that should be behind our new car
				iterator.previous(); //currentCar
				
				if(iterator.hasPrevious()) { //There is a car in front of the currentCar
					Car nextCar = iterator.previous();
					//our new Car should go between currentCar and nextCar if there is space available
					if(startingPoint - area >= nextCar.getYPos()) {
						iterator.next();
						return iterator.nextIndex();
					}
					return -1;
				}
				else if(startingPoint - area >= end) {
					return 0;
				}
				return -1;
			}
			
			else if(dir == Direction.East && currentCar.getXPos() + currentCar.getArea() < startingPoint) { //Found a car that should be behind our new car
				iterator.previous(); //currentCar
				
				if(iterator.hasPrevious()) { //There is a car in front of the currentCar
					Car nextCar = iterator.previous();
					if(startingPoint + area <= nextCar.getXPos()) {
						iterator.next();
						return iterator.nextIndex();
					}
					return -1;
				}
				else if(startingPoint + area <= end) {
					return 0;
				}
				return -1;
			}
			
			else if(dir == Direction.South && currentCar.getYPos() + currentCar.getArea() < startingPoint) { //Found a car that should be behind our new car
				iterator.previous(); //currentCar
				
				if(iterator.hasPrevious()) { //There is a car in front of the currentCar
					Car nextCar = iterator.previous();
					if(startingPoint + area <= nextCar.getYPos()) {
						iterator.next();
						return iterator.nextIndex();
					}
					return -1;
				}
				else if(startingPoint + area <= end) {
					return 0;
				}
				return -1;
			}
			
			else if(dir == Direction.West && currentCar.getXPos() - currentCar.getArea() > startingPoint) { //Found a car that should be behind our new car
				iterator.previous(); //currentCar
				
				if(iterator.hasPrevious()) { //There is a car in front of the currentCar
					Car nextCar = iterator.previous();
					if(startingPoint - area >= nextCar.getXPos()) {
						iterator.next();
						return iterator.nextIndex();
					}
					return -1;
				}
				else if(startingPoint - area >= end) {
					return 0;
				}
				return -1;
			}
		}
		
		if(dir == Direction.North) {
			if(carBuff.getLast().getYPos() < startingPoint - area)
				return carBuff.size();
		}
		else if(dir == Direction.East) {
			if(carBuff.getLast().getXPos() > startingPoint + area)
				return carBuff.size();
		}
		else if(dir == Direction.South) {
			if(carBuff.getLast().getYPos() > startingPoint + area)
				return carBuff.size();
		}
		else if(carBuff.getLast().getXPos() < startingPoint - area)
			return carBuff.size();
		
		return -1;

	}
	
	/**
	 * 
	 * @return The start of the road
	 */
	public int getStart() {
		return start;
	}
	
	/**
	 * 
	 * @return The end of the road
	 */
	public int getEnd() {
		return end;
	}
	
	/**
	 * Checks if the buffer is empty
	 * @return True if the buffer is empty, false otherwise
	 */
	public boolean isEmpty() {
		return carBuff.isEmpty();
	}

}
