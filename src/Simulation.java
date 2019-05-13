package trafficSimmulation;

public class Simulation {

	public static void main(String[] argv) {
		Model model = new Model();
		View view = new View(model, 1000, 1000);
		Controller controller = new Controller(model, view);
		controller.makeBoard();
		controller.start();
	}
	
}
