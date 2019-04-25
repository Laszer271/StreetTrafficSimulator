
public class Street {
	private final Point posStart; // start is at west/south
	private final Point posEnd;   // end is at east/north
	
	private final boolean orientation; // 1 if street is north-south oriented, 0 otherwise
	
	private int length;
	private int maxVel;
	
	private int spaceAvailable;
	
	CarBuffer[] carBuffer;
	
	
	public Street(Point start, Point end, int len, int mVel) {
		posStart = start;
		posEnd = end;
		orientation = start.getXpos() == end.getXpos();
		
		spaceAvailable = length;
		
		carBuffer = new CarBuffer[2];
		
		length = len;
		maxVel = mVel;
	}
	
	public Point getPosStart() {
		return posStart;
	}
	
	public Point getPosEnd() {
		return posEnd;
	}
	
	public int getLength() {
		return length;
	}
	
	public int getMaxVel() {
		return maxVel;
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
	
	public void push(boolean dir, Car car) {
		spaceAvailable -= car.getArea();
		
		if(dir) {
			carBuffer[0].push(car);
			return;
		}
		carBuffer[1].push(car);
	}
	
	public Car pop(boolean dir) {
		
		if(dir) {
			Car tempCar = carBuffer[0].pop();
			spaceAvailable += tempCar.getArea();
			return tempCar;
		}
		Car tempCar = carBuffer[1].pop();
		spaceAvailable += tempCar.getArea();
		return tempCar;
	}
	
	public int getSpace(boolean dir) {
		// start is at west/south
		// end is at east/north
		if(dir && orientation) {	//heading north
			return carBuffer[0].getLastCarPos().getYpos() - posStart.getYpos();
		}
		if(orientation) {			//heading south
			return carBuffer[0].getLastCarPos().getXpos() - posStart.getXpos();
		}
		if(dir) {					//heading east
			return posEnd.getYpos() - carBuffer[1].getLastCarPos().getYpos();
		}
									//heading west
		return posEnd.getXpos() - carBuffer[1].getLastCarPos().getXpos();
	}
	
	public boolean checkIfFits(Car car, boolean dir) {
		return car.getLength() >= getSpace(dir);
	}
	
	public boolean isSpace(Car car) {
		return car.getArea() <= spaceAvailable;
	}
	
	public void updateAll(int time) {
		for(int i = 0; i < 2; ++i) {
			for(int j = 0; j < carBuffer[i].getCount(); ++i) {
				Car tempCar = carBuffer[i].get(j);
				if(tempCar != carBuffer[i].getFirst()) {
					update(tempCar, carBuffer[i].get(j), time);
				}
			}
		}
	}
	
	private void update(Car car, Car nextCar, int time) {
		
		if(car.getDirection() == 0) {		//heading north
			int distance = Math.min(car.calculateDistToMove(time, maxVel), 
					nextCar.getYpos() - car.getYpos() - car.getArea());
			car.getPosition().addToY(distance);
		}
		else if(car.getDirection() == 1) {	//heading east
			int distance = Math.min(car.calculateDistToMove(time, maxVel), 
					nextCar.getXpos() - car.getXpos() - car.getArea());
			car.getPosition().addToX(distance);
		}
		else if(car.getDirection() == 2) {	//heading south
			int distance = Math.min(car.calculateDistToMove(time, maxVel), 
					car.getYpos() - nextCar.getYpos() - car.getArea());
			car.getPosition().addToY(distance);
		}
		else {								//heading west
			int distance = Math.min(car.calculateDistToMove(time, maxVel), 
					car.getXpos() - nextCar.getXpos() - car.getArea());
			car.getPosition().addToX(distance);
		}
		
		if(car.getXpos() +  car.getArea() == nextCar.getXpos() ||
		   car.getYpos() +  car.getArea() == nextCar.getYpos()) {
			car.setVelocity(nextCar.getVelocity());
		}
		else {
			car.setVelocity(Math.min(maxVel + car.getVelModifier(),
					car.getVelModifier() + car.getAcceleration() * time));
		}
		
		return;
	}
	
	private void update(Car car, int time) {
		
	}
	
	
}
