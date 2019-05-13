package trafficSimmulation;

import java.awt.Color;
import java.awt.Graphics;

public class Street extends StreetObject{
	
	private CarBuffer[] lanes;
	private final StreetObject[] ends = new StreetObject[2];
	
	private int maxVel;
	
	public static final int SOUTH_NORTH = 0;
	public static final int WEST_EAST = 1;
	public final int orientation;
	

	public Street(int TopLeftX, int TopLeftY, int BottomRightX, int BottomRightY, int numOfLanes,
				  int lanesOriented, int orientation) {
		
		super(TopLeftX, TopLeftY, BottomRightX, BottomRightY);
		
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
		
		maxVel = 50; // meters per second
		
		lanes = new CarBuffer[numOfLanes];
		for(int i = 0; i < lanesOriented; ++i) { //lanesOriented value means how many lanes should be directed towards North/East
			lanes[i] = new CarBuffer(1_000_000);
			lanes[i].setStart(start);
			lanes[i].setEnd(end);
			lanes[i].setMaxVel(maxVel);
			lanes[i].setDirection(dir1);
		}
		
		for(int i = lanesOriented; i < numOfLanes; ++i) {
			lanes[i] = new CarBuffer(1_000_000);
			lanes[i].setStart(end);
			lanes[i].setEnd(start);
			lanes[i].setMaxVel(maxVel);
			lanes[i].setDirection(dir2);
		}
	}
	
	public Street(int TopLeftX, int TopLeftY, int BottomRightX, int BottomRightY, int numOfLanes,
				  int lanesOriented, int orientation, StreetObject obj1, StreetObject obj2) {
		
		this(TopLeftX, TopLeftY, BottomRightX, BottomRightY, numOfLanes, lanesOriented, orientation);
		
		ends[0] = obj1;
		ends[1] = obj2;
	}
	
	public void addFirstEnd(StreetObject obj) {
		ends[0] = obj;
	}
	
	public void addSecondEnd(StreetObject obj) {
		ends[1] = obj;
	}
	
	@Override
	public void tick(int time) {
		for(int i = 0; i < lanes.length; ++i) {
			lanes[i].tick(time);
		}
	}
	
	@Override
	public void renderCars(Graphics g) {

		for(int i = 0; i < lanes.length; ++i) {
			lanes[i].render(g);
		}
	}
	
	@Override
	public void renderRoad(Graphics g) {
		g.setColor(Color.black);
		
		int x = TopLeftX / Model.MILLIMETRES_PER_PIXEL;
		int y = TopLeftY / Model.MILLIMETRES_PER_PIXEL;
		int width = super.getWidth() / Model.MILLIMETRES_PER_PIXEL;
		int height = super.getHeight() / Model.MILLIMETRES_PER_PIXEL;
		
		g.fillRect(x, y, width, height);
		
		g.setColor(Color.red);
		g.drawRect(x, y, width, height);
	}
	
	public void addCar() {
		int position;
		int offset = getWidth() / (lanes.length * 2);
		
		if(isSouthNorthOriented()) {
			position = TopLeftX;
		}
		else {
			position = TopLeftY;
		}
		
		//System.out.println("Car added   possition: " + position); //DEBUG
		
		for(int j = 0; j < 2; ++j) {
			for(int i = 0; i < 1; ++i) {
				lanes[j].addCar(position + (j+1) * offset);
			}	
		}
	}
	
	public Cross makeCrossNorth() {
		Cross cross = new Cross(TopLeftX, TopLeftY - getWidth(), BottomRightX, TopLeftY);
		cross.addSouthObject(this);
		return cross;
	}
	
	public Cross makeCrossEast() {
		Cross cross = new Cross(BottomRightX, TopLeftY, BottomRightX +getWidth(), BottomRightY);
		cross.addSouthObject(this);
		return cross;
	}
	
	public Cross makeCrossSouth() {
		Cross cross = new Cross(TopLeftX, BottomRightY, BottomRightX, BottomRightY + getWidth());
		cross.addSouthObject(this);
		return cross;
	}
	
	public Cross makeCrossWest() {
		Cross cross = new Cross(TopLeftX - getWidth(), TopLeftY, TopLeftX, BottomRightY);
		cross.addSouthObject(this);
		return cross;
	}
	
	public int getMaxVel() {
		return maxVel;
	}
	
	public boolean isSouthNorthOriented() {
		return orientation == SOUTH_NORTH;
	}
	
	public boolean isWestEastOriented() {
		return orientation == WEST_EAST;
	}
	
	@Override
	public int getWidth() {
		if(orientation == SOUTH_NORTH)
			return BottomRightX - TopLeftX;
		
		return BottomRightY - TopLeftY;
	}
	
	public int getLength() {
		if(orientation == SOUTH_NORTH)
			return BottomRightY - TopLeftY;
		
		return BottomRightX - TopLeftX;
	}

}
