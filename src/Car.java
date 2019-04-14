
public class Car {
	private Point position;
	private int direction; // 1 means heading from south to north or from west to east, -1 means otherwise
	
	private final int length;
	private final int area; // it's length of the car + the minimal length of gap 
							// between this car and the one in front of it.
	
	private int velocity; // current velocity
	private final int acceleration;
	private final int velModifier; // the car will try to get to the speed of 
								   // allowed speed at the street + his velModifier
	
	private Street[] route;
	private int currentStreet = 0;
	
	public static final int STANDARD_SIZE = 4000; //4000 mm is the standard length of a car
	
	public Car(Point pos, int len, int gap,
			int vel, int acc, int vMod) {
		position = pos;
		length = len;
		area = len + gap;
		velocity = vel;
		acceleration = acc;
		velModifier = vMod;
	}
	
	public Car(Point pos, int len, int gap,
			int vel, int acc, int vMod, Street[] rt) {
		position = pos;
		length = len;
		area = len + gap;
		velocity = vel;
		acceleration = acc;
		velModifier = vMod;
		route = rt;
	}
	
	public Car(Car other) {
		position = other.position;
		length = other.length;
		area = other.area;
		velocity = other.velocity;
		acceleration = other.acceleration;
		velModifier = other.velModifier;
		route = other.route;
		currentStreet = other.currentStreet;
	}
	
	public Point getPosition() {
		return position;
	}
	
	public int getXpos() {
		return position.getXpos();
	}
	
	public int getYpos() {
		return position.getYpos();
	}
	
	public int getDirection() {
		return direction;
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
	
	public int getVelocity() {
		return velocity;
	}
	
	public int getAcceleration() {
		return acceleration;
	}
	
	public int getVelModifier() {
		return velModifier;
	}
	
	public void setXpos(int x) {
		position.setXpos(x);
	}
	
	public void setYpos(int y) {
		position.setYpos(y);
	}
	
	public void setDirection(int dir) {
		direction = dir;
	}
	
	public void setVelocity(int vel) {
		velocity = vel;
	}
	
	public void updateVelocity(int vel) {
		velocity += vel;
	}
	
	public void updateXpos(int x) {
		position.addToX(x);
	}
	
	public void updateYpos(int y) {
		position.addToY(y);
	}
	
	public void setRoute(Street[] rt) {
		route = rt;
	}
}
