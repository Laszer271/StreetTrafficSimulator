package trafficSimmulation;

import java.awt.Graphics;

public class Cross extends StreetObject{
	
	private final StreetObject[] ends = new StreetObject[4];

	public Cross(int TopLeftX, int TopLeftY, int BottomRightX, int BottomRightY) {
		super(TopLeftX, TopLeftY, BottomRightX, BottomRightY);
	}
	
	public Cross(int TopLeftX, int TopLeftY, int BottomRightX, int BottomRightY,
				 StreetObject north, StreetObject east, StreetObject south, StreetObject west) {
		this(TopLeftX, TopLeftY, BottomRightX, BottomRightY);
		
		ends[0] = north;
		ends[1] = east;
		ends[2] = south;
		ends[3] = west;
	}
	
	public void addNorthObject(StreetObject north) {
		ends[0] = north;
	}
	
	public void addEastObject(StreetObject east) {
		ends[1] = east;
	}
	
	public void addSouthObject(StreetObject south) {
		ends[2] = south;
	}
	
	public void addWestObject(StreetObject west) {
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
		addNorthObject(street);
		return street;
	}
	
	public Street makeSouthStreet(int length, int numOfLanes, int lanesOriented) {
		Street street = new Street(TopLeftX, BottomRightY, BottomRightX, BottomRightY + length, numOfLanes, lanesOriented, Street.SOUTH_NORTH);
		street.addFirstEnd(this);
		addNorthObject(street);
		return street;
	}
	
	public Street makeWestStreet(int length, int numOfLanes, int lanesOriented) {
		Street street = new Street(TopLeftX - length, TopLeftY, TopLeftX, BottomRightY, numOfLanes, lanesOriented, Street.WEST_EAST);
		street.addFirstEnd(this);
		addNorthObject(street);
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
	
}
