package trafficSimulation;

import java.awt.Graphics;
import java.util.Random;

public class CarBuffer {
	
	private Car[] carBuff; //buffer of cars
	
	private Direction dir;
	
	//current indices:
	private int first; //first to enter the lane - the one first to leave
	private int last; //last to enter the lane - the one last to leave
	
	private int start;
	private int end;
	private int maxVel;
	
	private int count;
	
	final int capacityOfLane;
	
	private static final float SAFETY_MULTIPLAYER = 1.5f;
	
	CarBuffer(int length){
		capacityOfLane = (int) (SAFETY_MULTIPLAYER * length / Car.STANDARD_SIZE);
		carBuff = new Car[capacityOfLane];
		
		first = 0;
		last = 0;
		count = 0;
	}
	
	public Car getLast() {
		if(count == 0)
			return null;
		if(last == 0)
			return carBuff[capacityOfLane - 1];
		
		return carBuff[last - 1];
	}
	
	public Car getFirst() {
		if(count == 0)
			return null;
		return carBuff[first];
	}
	
	public Car get(int which) {
		return carBuff[(first + which) % capacityOfLane];
	}
	
	public int getCount() {
		return count;
	}
	
	public int getLastCarXPos() {
		int index = last != 0 ? last : capacityOfLane;
		return carBuff[index].getXPos();
	}
	
	public int getLastCarYPos() {
		int index = last != 0 ? last : capacityOfLane;
		return carBuff[index].getYPos();
	}
	
	public Car pop() {
		Car temp = carBuff[first];
		carBuff[first] = null;
		first = ++first % capacityOfLane;
		--count;
		return temp;
	}
	
	public void push(Car car) {
		carBuff[last] = car;
		last = ++last % capacityOfLane;
		++count;
	}
	
	public Direction getDirection() {
		return dir;
	}
	
	public void setStart(int start) {
		this.start = start;
	}
	
	public void setEnd(int end) {
		this.end = end;
	}
	
	public void setMaxVel(int vel) {
		maxVel = vel;
	}
	
	public void tick(int time) {
		for(int i = 0; i < count; ++i) {
			if(i != 0) {
				get(i).tick(time, get(i-1), maxVel);				
			}
			else {
				get(i).tick(time, end, maxVel);
			}
		}
	}
	
	public void render(Graphics g) {
		for(int i = 0; i < count; ++i) {
			get(i).render(g);
		}
	}
	
	public void addCar(int position) {
		Random r = new Random();
		int lengthOfTheCar = 6000;
		int widthOfTheCar = 3000;
		Car car;
		Car carNext;
		if(dir == Direction.North) {
			//System.out.println("North   start: " + start + "   end: " + end); //DEBUG
			int pos = r.nextInt(start - end) + end;
			int pos2 = pos + r.nextInt(80_000) + 20_000;
			car = new Car(position, pos, lengthOfTheCar, widthOfTheCar, 5000, Direction.North, 0, 10000, 5, null);
			carNext = new Car(position, pos2, lengthOfTheCar, widthOfTheCar, 5000, Direction.North, 0, 10000, 5, null);
		}
		else if(dir == Direction.East) {
			//System.out.println("East   start: " + start + "   end: " + end);
			int pos = r.nextInt(end - start) + start;
			int pos2 = pos - r.nextInt(80_000) + 20_000;
			car = new Car(pos, position, lengthOfTheCar, widthOfTheCar, 5000, Direction.East, 0, 10000, 5, null);
			carNext = new Car(pos2, position, lengthOfTheCar, widthOfTheCar, 5000, Direction.East, 0, 10000, 5, null);
		}
		else if(dir == Direction.South) {
			//System.out.println("South   start: " + start + "   end: " + end);
			int pos = r.nextInt(end - start) + start;
			int pos2 = pos - r.nextInt(80_000) + 20_000;
			car = new Car(position, pos, lengthOfTheCar, widthOfTheCar, 5000, Direction.South, 0, 10000, 5, null);
			carNext = new Car(position, pos2,  lengthOfTheCar, widthOfTheCar, 5000, Direction.South, 0, 10000, 5, null);
		}
		else {
			//System.out.println("West   start: " + start + "   end: " + end);
			int pos = r.nextInt(start - end) + end;
			int pos2 = pos + r.nextInt(80_000) + 20_000;
			car = new Car(pos, position , lengthOfTheCar, widthOfTheCar, 5000, Direction.West, 0, 10000, 5, null);
			carNext = new Car(pos2, position , lengthOfTheCar, widthOfTheCar, 5000, Direction.West, 0, 10000, 5, null);
		}
		
		push(car);
		push(carNext);
	}
	
	public void setDirection(Direction dir) {
		this.dir = dir;
	}
}
