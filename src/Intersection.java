
public class Intersection {
	private final Point TL_CORNER;
	private final Point BR_CORNER;
	
	private final Street northStreet;
	private final Street eastStreet;
	private final Street southStreet;
	private final Street westStreet;
	
	private boolean trafficLight = false; //true means it's letting in cars from north/south, false means otherwise
	
	Intersection(Point tlcorner, Point brcorner, Street north, Street east, Street south, Street west){
		TL_CORNER = tlcorner;
		BR_CORNER = brcorner;
		
		northStreet = north;
		eastStreet = east;
		southStreet = south;
		westStreet = west;
	}
	
	public boolean getLight() {
		return trafficLight;
	}
	
	public void setLight(boolean light) {
		trafficLight = light;
	}
	
	public Point getTL_CORNER() {
		return TL_CORNER;
	}
	
	public Point getBR_CORNER() {
		return BR_CORNER;
	}
	
	public Street getNorth() {
		return northStreet;
	}
	
	public Street getEast() {
		return eastStreet;
	}
	
	public Street getSouth() {
		return southStreet;
	}
	
	public Street getWest() {
		return westStreet;
	}
}
