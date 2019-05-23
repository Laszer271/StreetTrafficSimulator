package trafficSimulation;

import java.awt.Graphics;

public class Cross extends StreetObject{
	
	private final Street[] ends = new Street[4];

	public Cross(int TopLeftX, int TopLeftY, int BottomRightX, int BottomRightY) {
		super(TopLeftX, TopLeftY, BottomRightX, BottomRightY);
	}
	
	public Cross(int TopLeftX, int TopLeftY, int BottomRightX, int BottomRightY,
				 Street north, Street east, Street south, Street west) {
		this(TopLeftX, TopLeftY, BottomRightX, BottomRightY);
		
		ends[0] = north;
		ends[1] = east;
		ends[2] = south;
		ends[3] = west;
	}
	
	public void addNorthObject(Street north) {
		ends[0] = north;
	}
	
	public void addEastObject(Street east) {
		ends[1] = east;
	}
	
	public void addSouthObject(Street south) {
		ends[2] = south;
	}
	
	public void addWestObject(Street west) {
		ends[3] = west;
	}
	
	public Street makeNorthStreet(int length, int numOfLanes, int lanesOriented) {
		Street street = new Street(TopLeftX, TopLeftY - length, BottomRightX, TopLeftY,  numOfLanes, lanesOriented, Street.SOUTH_NORTH);
		street.addFirstEnd(this);
		addNorthObject(street);
		return street;
	}
	
	public Street makeEastStreet(int length, int numOfLanes, int lanesOriented) {
		Street street = new Street(BottomRightX, TopLeftY, BottomRightX + length, BottomRightY, numOfLanes, lanesOriented, Street.WEST_EAST);
		street.addFirstEnd(this);
		addEastObject(street);
		return street;
	}
	
	public Street makeSouthStreet(int length, int numOfLanes, int lanesOriented) {
		Street street = new Street(TopLeftX, BottomRightY, BottomRightX, BottomRightY + length, numOfLanes, lanesOriented, Street.SOUTH_NORTH);
		street.addFirstEnd(this);
		addSouthObject(street);
		return street;
	}
	
	public Street makeWestStreet(int length, int numOfLanes, int lanesOriented) {
		Street street = new Street(TopLeftX - length, TopLeftY, TopLeftX, BottomRightY, numOfLanes, lanesOriented, Street.WEST_EAST);
		street.addFirstEnd(this);
		addWestObject(street);
		return street;
	}

	@Override
	public void tick(int length) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renderCars(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
	public Street getNorthStreet() {
		return ends[0];
	}
	
	public Street getEastStreet() {
		return ends[1];
	}
	
	public Street getSouthStreet() {
		return ends[2];
	}
	
	public Street getWestStreet() {
		return ends[3];
	}
	
}
