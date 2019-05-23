package trafficSimulation;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.ListIterator;

public class Model {
	private Graph graph;
	//LinkedList<Street> streets = new LinkedList<Street>();
	//LinkedList<Cross> crosses = new LinkedList<Cross>();
	
	//public static final int BASIC_WIDTH = 18;
	public static final int MILLIMETRES_PER_PIXEL = 800;
	
	public Model() {
		
	}
	
	public void makeGraph(Cross[] crosses, Street[] streets) {
		graph = new Graph(crosses, streets);
	}
	
	public void tick(int time) {
		Cross[] crosses = graph.getArrOfVertices();
		Street[] streets = graph.getArrOfEdges();
		
		for(int i = 0; i < crosses.length; ++i) {
			crosses[i].tick(100);
		}
		for(int i = 0; i < streets.length; ++i) {
			streets[i].tick(100);
		}
	}
	
	public void renderRoads(Graphics g) {
		Cross[] crosses = graph.getArrOfVertices();
		Street[] streets = graph.getArrOfEdges();
		
		for(int i = 0; i < crosses.length; ++i) {
			crosses[i].renderRoad(g);
		}
		for(int i = 0; i < streets.length; ++i) {
			streets[i].renderRoad(g);
		}
	}
	
	public void renderCars(Graphics g) {
		Cross[] crosses = graph.getArrOfVertices();
		Street[] streets = graph.getArrOfEdges();
		
		for(int i = 0; i < crosses.length; ++i) {
			crosses[i].renderCars(g);
		}
		for(int i = 0; i < streets.length; ++i) {
			streets[i].renderCars(g);
		}
	}
}
