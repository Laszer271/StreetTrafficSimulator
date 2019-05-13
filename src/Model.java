package trafficSimmulation;

import java.awt.Graphics;
import java.util.Vector;

public class Model {
	Vector<StreetObject> objects = new Vector<StreetObject>();
	
	//public static final int BASIC_WIDTH = 18;
	public static final int MILLIMETRES_PER_PIXEL = 1000;
	
	public Model() {
		
	}
	
	public void addObject(StreetObject object) {
		objects.add(object);
	}
	
	public void tick(int time) {
		for(int i = 0; i < objects.size(); ++i) {
			objects.get(i).tick(time);
		}
	}
	
	public void renderRoads(Graphics g) {
		for(int i = 0; i < objects.size(); ++i) {
			objects.get(i).renderRoad(g);
		}
	}
	
	public void renderCars(Graphics g) {
		for(int i = 0; i < objects.size(); ++i) {
			objects.get(i).renderCars(g);
		}
	}
}
