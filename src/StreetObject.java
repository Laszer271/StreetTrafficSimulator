package trafficSimmulation;

import java.awt.Color;
import java.awt.Graphics;

public abstract class StreetObject {
	
	final int TopLeftX, TopLeftY;
	final int BottomRightX, BottomRightY;
	
	public StreetObject(int TopLeftX, int TopLeftY, int BottomRightX, int BottomRightY) {
		this.TopLeftX = TopLeftX;
		this.TopLeftY = TopLeftY;
		this.BottomRightX = BottomRightX;
		this.BottomRightY = BottomRightY;
	}
	
	public void renderRoad(Graphics g) {
		g.setColor(Color.black);
		
		int x = TopLeftX / Model.MILLIMETRES_PER_PIXEL;
		int y = TopLeftY / Model.MILLIMETRES_PER_PIXEL;
		int width = getWidth() / Model.MILLIMETRES_PER_PIXEL;
		int height = getHeight() / Model.MILLIMETRES_PER_PIXEL;
		
		g.fillRect(x, y, width, height);
		
		g.setColor(Color.red);
		g.drawRect(x, y, width, height);
	}
	
	public abstract void renderCars(Graphics g);
	
	public abstract void tick(int time);

	public int getTopLeftX() {
		return TopLeftX;
	}
	
	public int getTopLeftY() {
		return TopLeftY;
	}
	
	public int getBottomRightX() {
		return BottomRightX;
	}
	
	public int getBottomRightY() {
		return BottomRightY;
	}
	
	public int getWidth() {
		return BottomRightX - TopLeftX;
	}
	
	public int getHeight() {
		return BottomRightY - TopLeftY;
	}
}
