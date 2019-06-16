package trafficSimulation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

/**
 * View shows the data to the user in a meaningful way
 * @author Wojciech Maciejewski
 *
 */
public class View {
	private JFrame frame = new JFrame();
	private Handler handler;
	private Model model;
	
	/**
	 * 
	 * @param model The reference to the model that the view will be operating on
	 */
	public View(Model model) {
		
		this.model = model;
		
		handler = new Handler();
		frame.addMouseWheelListener(handler);
		frame.addMouseListener(handler);
		frame.addKeyListener(handler);
		
		frame.setTitle("TrafficSimulation");
		frame.setSize(1000, 1000);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setFocusable(true);
		frame.requestFocusInWindow();
		frame.createBufferStrategy(3);
	}
	
	/**
	 * A method that draws the background and calls render of every other element in the simulation to draw them all
	 * @param arrOfCrosses An array of crosses that will be rendered
	 * @param arrOfStreets An array of streets that will be rendered
	 */
	public void render() {
		Cross[] arrOfCrosses = model.getCrosses();
		Street[] arrOfStreets = model.getStreets();
		
		BufferStrategy bs = frame.getBufferStrategy();

        Graphics g = bs.getDrawGraphics();
        
        g.setColor(Color.white);
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
        
        //Rendering roads:
		for(int i = 0; i < arrOfCrosses.length; ++i) {
			arrOfCrosses[i].renderRoad(g, handler.getX(), handler.getY());
		}
		for(int i = 0; i < arrOfStreets.length; ++i) {
			arrOfStreets[i].renderRoad(g, handler.getX(), handler.getY());
		}
        
		//Rendering cars:
		for(int i = 0; i < arrOfCrosses.length; ++i) {
			arrOfCrosses[i].renderCars(g, handler.getX(), handler.getY());
		}
		for(int i = 0; i < arrOfStreets.length; ++i) {
			arrOfStreets[i].renderCars(g, handler.getX(), handler.getY());
		}
		
        g.dispose();
        bs.show();
	}
	
	private class Handler implements MouseWheelListener, MouseListener, KeyListener
	{
		private int xPos, yPos;
		private int xInitialPos, yInitialPos;
		
		public void mouseWheelMoved(MouseWheelEvent e) {
			
			int newRatio = Model.millimetersPerPixel + 100 * e.getWheelRotation();
			if(newRatio > 0)
				Model.setMillimetersPerPixel(newRatio);
			
		}

		public void mouseClicked(MouseEvent e) {
			
		}

		public void mousePressed(MouseEvent e) {
			xInitialPos = e.getX();
			yInitialPos = e.getY();
			
		}

		public void mouseReleased(MouseEvent e) {
			xPos += e.getX() - xInitialPos;
			yPos += e.getY() - yInitialPos;
			
		}

		public void mouseEntered(MouseEvent e) {
			
		}

		public void mouseExited(MouseEvent e) {
			
		}
		
		public int getX() {
			return xPos;
		}
		
		public int getY() {
			return yPos;
		}

		public void keyTyped(KeyEvent e) {
			
		}

		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			
			if(key == KeyEvent.VK_SPACE)
				model.changeLights();
			
		}

		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
}
