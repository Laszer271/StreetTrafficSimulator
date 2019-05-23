package trafficSimulation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class View {
	private JFrame frame = new JFrame();
	private int width, height;
	private Model model;
	
	public View(Model model, int width, int height) {
		
		this.width = width;
		this.height = height;
		
		this.model = model;
		
		//frame.addKeyListener(new KeyInput(this));
		
		frame.setTitle("TrafficSimulation");
		frame.setSize(width, height);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setFocusable(true);
		frame.requestFocusInWindow();
		frame.createBufferStrategy(3);
	}
	
	public void render() {
		BufferStrategy bs = frame.getBufferStrategy();

        Graphics g = bs.getDrawGraphics();
        
        g.setColor(Color.white);
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
        
        model.renderRoads(g);
        model.renderCars(g);
        g.dispose();
        bs.show();
	}
}
