package trafficSimulation;

/**
 * A Controller coordinates View and Model and has a game-loop in which the simulation is working
 * @author Wojciech Maciejewski
 *
 */
public class Controller implements Runnable {
	
	private Model model;
	private View view;
	private Thread thread;
	private boolean running;
	/**
	 * How many ticks per second does simulation perform
	 */
	public final static int TICKS_PER_SEC = 30;
	private final static long CLOCK_RATE = 1000 / TICKS_PER_SEC;
	/**
	 * how much of in-simulation time passes every tick (in milliseconds)
	 */
	public final static int BASIC_TIME = 100;
	
	/**
	 * 
	 * @param model Model that the controller will operate on
	 * @param view View that will show the results to the user
	 */
	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;
		makeBoard();
		start();
	}
	
	/**
	 * Starts a new thread at which the controller runs
	 */
	public synchronized void start() {
		thread = new Thread(this);
        thread.start();
        running = true;

	}
	
	/**
	 * Stops the thread at which the controller runs
	 */
	public synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * Makes every object in model update its state
	 * @param time Time which is simulated to have passed during the tick
	 * @param crosses An array of crosses that will be updated
	 * @param streets An array of streets that will be updated
	 */
	public void tick(int time, Cross[] crosses, Street[] streets) {
		for(int i = 0; i < crosses.length; ++i) {

			crosses[i].tick(time);
		}
		for(int i = 0; i < streets.length; ++i) {
			int numOfNewCars = streets[i].numberOfCarsToBeGenerated(time);

			for(int j = 0; j < numOfNewCars; ++j) {
				model.generateCar(streets[i]);
			}
			streets[i].tick(time);
		}
	}

	/**
	 * A game-loop
	 */
	public void run() {
		long timer = System.currentTimeMillis();
		while(running) {
			if (System.currentTimeMillis() - timer >= CLOCK_RATE) {
				tick(BASIC_TIME, model.getCrosses(), model.getStreets());
				view.render();
                timer += CLOCK_RATE;
            }
		}
		stop();
		
	}
	
	/**
	 * A placeholder for a function that makes a board 
	 */
	public void makeBoard() {
		Street[] streets = new Street[21];
		Cross[] crosses = new Cross[8];
		
		int length = 200_000;
		int length2 = 140_000;
		int begginingX = 500_000;
		int begginingY = begginingX;
		int widthOfCross = 20_000;
		
		crosses[0] = new Cross(begginingX, begginingY, begginingX + widthOfCross, begginingY + widthOfCross);
		streets[0] = crosses[0].makeNorthStreet(length, 2, 1);
		streets[1] = crosses[0].makeEastStreet(length2, 2, 1);
		streets[2] = crosses[0].makeSouthStreet(length, 2, 1);
		streets[3] = crosses[0].makeWestStreet(length, 2, 1);
		
		crosses[1] = streets[0].makeCrossNorth();
		crosses[2] = streets[1].makeCrossEast();
		crosses[3] = streets[2].makeCrossSouth();
		crosses[4] = streets[3].makeCrossWest();

		streets[4] = crosses[1].makeNorthStreet(length, 2, 1);
		streets[5] = crosses[1].makeWestStreet(length, 2, 1);
		streets[6] = crosses[1].makeEastStreet(length2, 2, 1);
		
		streets[7] = crosses[2].makeNorthStreet(length, 2, 1);
		streets[8] = crosses[2].makeSouthStreet(length, 2, 1);
		streets[9] = crosses[2].makeEastStreet(length, 2, 1);
		
		streets[10] = crosses[3].makeSouthStreet(length, 2, 1);
		streets[11] = crosses[3].makeWestStreet(length, 2, 1);
		streets[12] = crosses[3].makeEastStreet(length2, 2, 1);
		
		streets[13] = crosses[4].makeNorthStreet(length, 2, 1);
		streets[14] = crosses[4].makeWestStreet(length, 2, 1);
		streets[15] = crosses[4].makeSouthStreet(length, 2, 1);
		
		crosses[5] = streets[7].makeCrossNorth();

		streets[16] = crosses[5].makeNorthStreet(length, 2, 1);
		streets[17] = crosses[5].makeEastStreet(length, 2, 1);
		
		crosses[6] = streets[12].makeCrossEast();

		streets[18] = crosses[6].makeEastStreet(length >>> 1, 2, 1);
		streets[19] = crosses[6].makeSouthStreet(length >>> 1, 2, 1);
		
		crosses[7] = streets[19].makeCrossSouth();

		streets[20] = crosses[7].makeEastStreet(length >>> 2, 2, 1);
		streets[0].setProbability(1);
		
		streets[6].addSecondEnd(crosses[5]);
		streets[8].addSecondEnd(crosses[6]);
		crosses[5].addWestObject(streets[6]);
		crosses[6].addNorthObject(streets[8]);
		
		model.makeGraph(crosses, streets);
	}
}
