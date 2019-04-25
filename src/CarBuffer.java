
public class CarBuffer {
	
	private Car[] carBuff; //buffer of cars moving towards north or east
	
	//current idexes:
	private int first; //first to enter the lane - the one first to leave
	private int last; //last to enter the lane - the one last to leave
	
	private int count;
	
	final int capacityOfLane;
	
	private static final int SAFETY_MULTIPLAYER = 2;
	
	CarBuffer(int length){
		capacityOfLane = SAFETY_MULTIPLAYER * length / Car.STANDARD_SIZE;
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
	
	public Point getLastCarPos() {
		int index = last != 0 ? last : capacityOfLane;
		return carBuff[index].getPosition();
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
}
