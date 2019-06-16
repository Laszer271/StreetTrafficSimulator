package trafficSimulation;

import java.awt.Color;
import java.awt.Graphics;

/**
 * An abstract class that has some methods helpful for drawing basic objects. Its children are Street and Cross
 * @author Wojciech Maciejewski
 *
 */
public abstract class StreetObject {
	
	final int TopLeftX, TopLeftY;
	final int BottomRightX, BottomRightY;
	
	/**
	 * 
	 * @param TopLeftX X-coordinate of the top left corner of the cross
	 * @param TopLeftY Y-coordinate of the top left corner of the cross
	 * @param BottomRightX X-coordinate of the bottom right corner of the cross
	 * @param BottomRightY Y-coordinate of the bottom right corner of the cross
	 */
	public StreetObject(int TopLeftX, int TopLeftY, int BottomRightX, int BottomRightY) {
		this.TopLeftX = TopLeftX;
		this.TopLeftY = TopLeftY;
		this.BottomRightX = BottomRightX;
		this.BottomRightY = BottomRightY;
	}
	
	/**
	 * Draws the road but no cars
	 * @param g Graphics parameter needed to do the drawing
	 * @param xOffset An offset on the x-axis that determines how far from their stored position the road should be drawn
	 * @param yOffset An offset on the y-axis that determines how far from their stored position the road should be drawn
	 */
	public void renderRoad(Graphics g, int xOffset, int yOffset) {
		g.setColor(Color.black);
		
		int x = TopLeftX / Model.millimetersPerPixel + xOffset;
		int y = TopLeftY / Model.millimetersPerPixel + yOffset;
		int width = getWidth() / Model.millimetersPerPixel;
		int height = getHeight() / Model.millimetersPerPixel;
		
		g.fillRect(x, y, width, height);
		
		g.setColor(Color.red);
		g.drawRect(x, y, width, height);
	}
	
	
	/**
	 * Abstract method that will be used to draw cars on the road
	 * @param g Graphics parameter needed to do the drawing
	 * @param xOffset An offset on the x-axis that determines how far from their stored position the cars should be drawn
	 * @param yOffset An offset on the y-axis that determines how far from their stored position the cars should be drawn
	 */
	public abstract void renderCars(Graphics g, int xOffset, int yOffset);
	
	/**
	 * Abstract method that will be used to update state of every car on the road
	 * @param time Determines the time to be simulated during the tick
	 */
	public abstract void tick(int time);

	/**
	 * 
	 * @return X-coordinate of the top left corner of the cross
	 */
	public int getTopLeftX() {
		return TopLeftX;
	}
	
	/**
	 * 
	 * @return Y-coordinate of the top left corner of the cross
	 */
	public int getTopLeftY() {
		return TopLeftY;
	}
	
	/**
	 * 
	 * @return X-coordinate of the bottom right corner of the cross
	 */
	public int getBottomRightX() {
		return BottomRightX;
	}
	
	/**
	 * 
	 * @return Y-coordinate of the bottom right corner of the cross
	 */
	public int getBottomRightY() {
		return BottomRightY;
	}
	
	/**
	 * 
	 * @return The width of the StreetObject
	 */
	public int getWidth() {
		return BottomRightX - TopLeftX;
	}
	
	/**
	 * 
	 * @return The height of the StreetObject
	 */
	public int getHeight() {
		return BottomRightY - TopLeftY;
	}
}
