package trafficSimulation;

import java.util.LinkedList;

public class Controller implements Runnable {
	
	private Model model;
	private View view;
	private Thread thread;
	private boolean running;
	public final static int TICKS_PER_SEC = 16;
	public final static long CLOCK_RATE = 1000 / TICKS_PER_SEC;
	public final static int BASIC_TIME = 100; //how much of in-simulation time passes every tick (in milliseconds)
	
	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;
		makeBoard();
		start();
	}
	
	public synchronized void start() {
		thread = new Thread(this);
        thread.start();
        running = true;

	}
	
	public synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public void run() {
		long timer = System.currentTimeMillis();
		int counter = 0;
		int frames = 0;
		long time1;
		long timeLast = System.currentTimeMillis();
		while(running) {
			++frames;
			if (System.currentTimeMillis() - timer >= CLOCK_RATE) {
				time1 = System.currentTimeMillis();
				//System.out.println("Since last iteration: " + (time1 - timeLast));
				timeLast = time1;
				model.tick(100);
				view.render();
				++counter;
				if(counter == TICKS_PER_SEC) {
					counter = 0;
					//System.out.println("FPS: " + frames);
					frames = 0;
				}
                timer += CLOCK_RATE;
            }
		}
		stop();
		
	}
	
	public void makeBoard() {
		Street[] streets = new Street[21];
		Cross[] crosses = new Cross[8];
		
		int length = 200_000;
		int length2 = 140_000;
		int begginingX = 500_000;
		int widthOfCross = 20_000;
		int begginingY = begginingX;
		
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
		
		streets[6].addSecondEnd(crosses[5]);
		streets[8].addSecondEnd(crosses[6]);
		crosses[5].addWestObject(streets[6]);
		crosses[6].addNorthObject(streets[8]);
		
		model.makeGraph(crosses, streets);
		
		for(int i = 0; i < streets.length; ++i) {
			streets[i].addCar();
		}

		Graph g = new Graph(crosses, streets);
		g.shortestRoute(crosses[7], crosses[6]);
	}
	
	private boolean isRoute(Cross start, Cross end) { //For debugging
		LinkedList<Cross> list = new LinkedList<Cross>();
		
		if(start == end)
			return true;
		
		list.add(start);
		for(int i = 0; i < list.size(); ++i) {
			Street[] tempStreet = new Street[4];
			tempStreet[0] = list.get(i).getNorthStreet();
			tempStreet[1] = list.get(i).getEastStreet();
			tempStreet[2] = list.get(i).getSouthStreet();
			tempStreet[3] = list.get(i).getWestStreet();
			
			for(int j = 0; j < tempStreet.length; ++j) {
				if(tempStreet[j] == null)
					continue;
				if(list.get(i) != tempStreet[j].getEnd() && tempStreet[j].getEnd() != null) {
					if(!list.contains(tempStreet[j].getEnd())) {
						list.add((Cross)tempStreet[j].getEnd());
					}
				}
				else if(tempStreet[j].getStart() != null) {
					if(!list.contains(tempStreet[j].getStart())) {
						list.add((Cross)tempStreet[j].getStart());
					}
				}
				
				if(list.getLast() == end)
					return true;
			}
		}
		
		return false;
	}
	
}
