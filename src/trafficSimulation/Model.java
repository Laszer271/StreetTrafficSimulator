package trafficSimulation;

/**
 * Model stores data in the simulation
 * @author Wojciech Maciejewski
 *
 */
public class Model {
	private Graph graph;
	
	/**
	 * Ratio of millimeters per pixel used to draw 
	 */
	public static int millimetersPerPixel = 1000;
	
	public Model() {
		
	}
	
	/**
	 * Takes arrays of crosses and streets as parameter and makes a new graph based on them
	 * @param crosses Crosses that will be included in the new graph
	 * @param streets Streets that will be included in the new graph
	 */
	public void makeGraph(Cross[] crosses, Street[] streets) {
		graph = new Graph(crosses, streets);
	}

	/**
	 * Changes the ratio of millimeters per pixel to the value given as parameter (used in zooming)
	 * @param toBeSet The new ratio of millimeters per pixel
	 */
	public static void setMillimetersPerPixel(int toBeSet) {
		millimetersPerPixel = toBeSet;
	}
	
	/**
	 * A method that defines the values given to the generateCar in the class graph and calls it
	 * @param start The street in which the new car should appear
	 */
	public void generateCar(Street start) {
		graph.generateCar(start, 4000, 5000, 2000, 3000, 500, 2000, -5, 10, 1000, 10000, 100, 250);
	}
	
	/**
	 * Changes every traffic light in every cross
	 */
	public void changeLights() {
		Cross[] crosses = graph.getArrOfVertices();
		
		for(int i = 0; i < crosses.length; ++i) {
			crosses[i].setLigth(!crosses[i].getLight());
		}
	}
	
	public Cross[] getCrosses() {
		return graph.getArrOfVertices();
	}
	
	public Street[] getStreets() {
		return graph.getArrOfEdges();
	}
}
