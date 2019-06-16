package trafficSimulation;

import java.awt.Graphics;

/**
 * Cross is a square junction that has at most 4 streets connected to itself
 * @author Wojciech Maciejewski
 *
 */
public class Cross extends StreetObject{
	
	private final Street[] ends = new Street[4];
	private boolean light;
	
	public final static boolean NORTH_SOUTH = true;
	public final static boolean WEST_EAST = false;

	/**
	 * 
	 * @param TopLeftX X-coordinate of the top left corner of the cross
	 * @param TopLeftY Y-coordinate of the top left corner of the cross
	 * @param BottomRightX X-coordinate of the bottom right corner of the cross
	 * @param BottomRightY Y-coordinate of the bottom right corner of the cross
	 */
	public Cross(int TopLeftX, int TopLeftY, int BottomRightX, int BottomRightY) {
		super(TopLeftX, TopLeftY, BottomRightX, BottomRightY);
	}
	
	/**
	 * 
	 * @param TopLeftX X-coordinate of the top left corner of the cross
	 * @param TopLeftY Y-coordinate of the top left corner of the cross
	 * @param BottomRightX X-coordinate of the bottom right corner of the cross
	 * @param BottomRightY Y-coordinate of the bottom right corner of the cross
	 * @param north The street north of the cross that is connected with the cross
	 * @param east The street east of the cross that is connected with the cross
	 * @param south The street south of the cross that is connected with the cross
	 * @param west The street west of the cross that is connected with the cross
	 */
	public Cross(int TopLeftX, int TopLeftY, int BottomRightX, int BottomRightY,
				 Street north, Street east, Street south, Street west) {
		this(TopLeftX, TopLeftY, BottomRightX, BottomRightY);
		
		ends[0] = north;
		ends[1] = east;
		ends[2] = south;
		ends[3] = west;
	}
	
	/**
	 * Adds a connection on the north with new street given as the parameter
	 * @param north The street to be connected with the cross on the north
	 */
	public void addNorthObject(Street north) {
		ends[0] = north;
	}
	
	/**
	 * Adds a connection on the east with new street given as the parameter
	 * @param east The street to be connected with the cross on the east
	 */
	public void addEastObject(Street east) {
		ends[1] = east;
	}
	
	/**
	 * Adds a connection on the south with new street given as the parameter
	 * @param south The street to be connected with the cross on the south
	 */
	public void addSouthObject(Street south) {
		ends[2] = south;
	}
	
	/**
	 * Adds a connection on the west with new street given as the parameter
	 * @param west The street to be connected with the cross on the west
	 */
	public void addWestObject(Street west) {
		ends[3] = west;
	}
	
	
	/**
	 * A method that makes new street and adds it as a north connection to current cross
	 * @param length Length of the new street
	 * @param numOfLanes Number of lanes of the new street
	 * @param lanesOriented Value that precise how many lanes will be directed towards north/east
	 * @return The new street that was created
	 */
	public Street makeNorthStreet(int length, int numOfLanes, int lanesOriented) {
		Street street = new Street(TopLeftX, TopLeftY - length, BottomRightX, TopLeftY,  numOfLanes, lanesOriented, Street.SOUTH_NORTH);
		street.addFirstEnd(this);
		addNorthObject(street);
		return street;
	}
	
	/**
	 * 
	 * A method that makes new street and adds it as a east connection to current cross
	 * @param length Length of the new street
	 * @param numOfLanes Number of lanes of the new street
	 * @param lanesOriented Value that precise how many lanes will be directed towards north/east
	 * @return The new street that was created
	 */
	public Street makeEastStreet(int length, int numOfLanes, int lanesOriented) {
		Street street = new Street(BottomRightX, TopLeftY, BottomRightX + length, BottomRightY, numOfLanes, lanesOriented, Street.WEST_EAST);
		street.addFirstEnd(this);
		addEastObject(street);
		return street;
	}
	
	/**
	 * 
	 * A method that makes new street and adds it as a south connection to current cross
	 * @param length Length of the new street
	 * @param numOfLanes Number of lanes of the new street
	 * @param lanesOriented Value that precise how many lanes will be directed towards north/east
	 * @return The new street that was created
	 */
	public Street makeSouthStreet(int length, int numOfLanes, int lanesOriented) {
		Street street = new Street(TopLeftX, BottomRightY, BottomRightX, BottomRightY + length, numOfLanes, lanesOriented, Street.SOUTH_NORTH);
		street.addFirstEnd(this);
		addSouthObject(street);
		return street;
	}
	
	/**
	 * 
	 * A method that makes new street and adds it as a west connection to current cross
	 * @param length Length of the new street
	 * @param numOfLanes Number of lanes of the new street
	 * @param lanesOriented Value that precise how many lanes will be directed towards north/east
	 * @return The new street that was created
	 */
	public Street makeWestStreet(int length, int numOfLanes, int lanesOriented) {
		Street street = new Street(TopLeftX - length, TopLeftY, TopLeftX, BottomRightY, numOfLanes, lanesOriented, Street.WEST_EAST);
		street.addFirstEnd(this);
		addWestObject(street);
		return street;
	}

	@Override
	public void tick(int length) {
		
	}

	@Override
	public void renderCars(Graphics g, int xOffset, int yOffset) {
		
	}
	
	/**
	 * 
	 * @return The street connected with the cross on the north
	 */
	public Street getNorthStreet() {
		return ends[0];
	}
	
	/**
	 * 
	 * @return The street connected with the cross on the east
	 */
	public Street getEastStreet() {
		return ends[1];
	}
	
	/**
	 * 
	 * @return The street connected with the cross on the south
	 */
	public Street getSouthStreet() {
		return ends[2];
	}
	
	/**
	 * 
	 * @return The street connected with the cross on the west
	 */
	public Street getWestStreet() {
		return ends[3];
	}
	
	/**
	 * Set the traffic light to the value given as the parameter
	 * @param value The new value of the traffic light (True means that the west-east traffic through the cross is allowed and south-north is forbidden. False means otherwise)
	 */
	public void setLigth(boolean value) {
		light = value;
	}
	
	/**
	 * 
	 * @return The current value of the traffic light
	 */
	public boolean getLight() {
		return light;
	}
}
