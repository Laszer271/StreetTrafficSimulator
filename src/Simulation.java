import trafficSimulation.Controller;
import trafficSimulation.Model;
import trafficSimulation.View;

/**
 *
 * @author Wojciech Maciejewski
 *
 */
public class Simulation {

	public static void main(String[] argv) {
		Model model = new Model();
		View view = new View(model);
		@SuppressWarnings("unused")
		Controller controller = new Controller(model, view);
	}
	
}
