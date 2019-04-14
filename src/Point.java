
public class Point {
	private int xpos;
	private int ypos;
	
	public Point(int x, int y) {
		xpos = x;
		ypos = y;
	}
	
	public int getXpos() {
		return xpos;
	}
	
	public int getYpos() {
		return ypos;
	}
	
	public void setXpos(int x) {
		xpos = x;
	}
	
	public void setYpos(int y) {
		ypos = y;
	}
	
	public void addToX(int x) {
		xpos += x;
	}
	
	public void addToY(int y) {
		ypos += y;
	}
}
