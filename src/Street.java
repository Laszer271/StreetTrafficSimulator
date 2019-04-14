
public class Street {
	private final Point posStart; // start is at west/south
	private final Point posEnd;   // end is at east/north
	private final boolean orientation; // 1 if street is north-south oriented, 0 otherwise
	
	private int numOfLanes;
	private int lanesDirection; // How many lanes face towards north/east
	private int length;
	private int maxVel;
	
	private Car[] laneBuffer;
	private int[] startOfLane;
	private int capacityOfLane;
	
	private int[] firstCar;
	private int[] lastCar;
	private int[] numOfCars;
	
	
	public Street(Point start, Point end, int lan, int dir, int len, int mVel) {
		posStart = start;
		posEnd = end;
		orientation = start.getXpos() == end.getXpos();
		
		numOfLanes = lan;
		lanesDirection = dir;
		length = len;
		maxVel = mVel;
		
		
		capacityOfLane = 2 * len / Car.STANDARD_SIZE;
		int tempSize = lan * capacityOfLane;
		
		laneBuffer = new Car[tempSize];
		startOfLane = new int[lan];
		numOfCars = new int[lan];
		
		int tempIndex = 0;
		for(int i = 0; i < lan; ++i) {
			startOfLane[i] = tempIndex;
			numOfCars[i] = 0;
			
			firstCar[i] = 0;
			lastCar[i] = 0;
			
			tempIndex += capacityOfLane;
		}
	}
	
	public Point getPosStart() {
		return posStart;
	}
	
	public Point getPosEnd() {
		return posEnd;
	}
	
	public int getNumOflanes() {
		return numOfLanes;
	}
	
	public int getLanesDirection() {
		return lanesDirection;
	}
	
	public int getLength() {
		return length;
	}
	
	public int getMaxVel() {
		return maxVel;
	}
	
	public void setNumOflanes(int lan) {
		numOfLanes = lan;
	}
	
	public void setLanesDirection(int dir) {
		lanesDirection = dir;
	}
	
	public void setLength(int len) {
		length = len;
	}
	
	public void setMaxVel(int vel) {
		maxVel = vel;
	}
	
	public boolean checkIfContains(Point p) {
		return p.getXpos() <= posEnd.getXpos() && p.getYpos() <= posEnd.getYpos() &&
			   p.getXpos() >= posStart.getXpos() && p.getYpos() >= posStart.getYpos();
	}
	
	public int decideLane(boolean dir, Car car) {
		int tempIndex = -1;
		int tempNumOfCars =  2_147_483_647; //this magic number is the maximum value an integer can store
		
		if(dir) {
			for(int i = 0; i < lanesDirection; ++i) {
				if(numOfCars[i] < tempNumOfCars && isSpace(car.getArea(), i)) {
					tempIndex = i;
					tempNumOfCars = numOfCars[i];
				}
			}
		}
		else {
			for(int i = lanesDirection; i < numOfLanes; ++i) {
				if(numOfCars[i] < tempNumOfCars && isSpace(car.getArea(), i)) {
					tempIndex = i;
					tempNumOfCars = numOfCars[i];
				}
			}
		}
		
		return tempIndex;
	}
	
	private boolean isSpace(int space, int laneIndex) {
		
		Car car = laneBuffer[lastCar[laneIndex] % capacityOfLane + startOfLane[laneIndex]];
		
		if(orientation) { //south-north oriented
			if(laneIndex < lanesDirection) { //start of the lane is at the south - at the start of the street
				return 	space <= car.getYpos() - car.getLength() - posStart.getYpos();
			}
			
			//start of the lane is at the north - at the end of the street
			return space <= posEnd.getYpos() - car.getYpos() - car.getLength();
		}
		
		//west-east oriented
		if(laneIndex < lanesDirection) { //start of the lane is at the west - at the start of the street
			return space <= car.getXpos() - car.getLength() - posStart.getXpos();
		}
		
		//start of the lane is at the east - at the end of the street
		return space <= posEnd.getXpos() - car.getXpos() - car.getLength();
	}
	
	public boolean push(boolean dir, Car car) {
		int tempIndex = decideLane(dir, car);
		
		if(tempIndex < 0)
			return false;
		
		++numOfCars[tempIndex];
		
		laneBuffer[startOfLane[tempIndex] + ++lastCar[tempIndex] % capacityOfLane] = car;
		return true;
	}
	
	public Car pop(int lane) {
		--numOfCars[lane];
		Car car = laneBuffer[startOfLane[lane] + firstCar[lane]++ % capacityOfLane];
		
		laneBuffer[startOfLane[lane] + firstCar[lane]++ % capacityOfLane] = null;
		return car;
	}
	
	private update(int buffIndex) {
		if(laneBuffer[buffIndex].getWhenReady() <= )
	}
	 
}
