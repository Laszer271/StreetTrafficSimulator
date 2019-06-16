package trafficSimulation;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

/**
 * Graph is storing the crosses as vertices and streets as edges. It also has some useful algorithms for generating a new car
 * @author Wojciech Maciejewski
 *
 */
public class Graph {
	private Cross[] vertices;
	private Street[] edges;
	
	private int indexToAddVertex;
	private int indexToAddEdge;
	
	/**
	 * 
	 * @param numOfCrosses Number of crosses in the model
	 * @param numOfStreets Number of streets in the model
	 */
	public Graph(int numOfCrosses, int numOfStreets) {
		vertices = new Cross[numOfCrosses];
		edges = new Street[numOfStreets];
		
		indexToAddVertex = 0;
		indexToAddEdge = 0;
	}
	
	/**
	 * 
	 * @param crosses An array of crosses 
	 * @param streets An array of streets
	 */
	public Graph(Cross[] crosses, Street[] streets) {
		vertices = crosses;
		edges = streets;
		
		indexToAddVertex = crosses.length;
		indexToAddEdge = streets.length;
	}
	
	/**
	 * Adds cross to current crosses
	 * @param cross Cross to be added
	 */
	public void addCross(Cross cross) {
		vertices[indexToAddVertex] = cross;
	
		++indexToAddVertex;
	}
	
	/**
	 * Adds crosses to current crosses
	 * @param crosses Array of crosses to be added
	 */
	public void addCrosses(Cross[] crosses) {
		for(int i = 0; i < crosses.length; ++i) {
			vertices[indexToAddVertex] = crosses[i];
			
			++indexToAddVertex;
		}
	}
	
	/**
	 * Adds street to current streets
	 * @param street Street to be added
	 */
	public void addStreet(Street street) {
		edges[indexToAddEdge] = street;
	
		++indexToAddEdge;
	}
	
	/**
	 * Adds streets to current streets
	 * @param streets Streets to be added
	 */
	public void addStreets(Street[] streets) {
		for(int i = 0; i < streets.length; ++i) {
			edges[indexToAddEdge] = streets[i];
			
			++indexToAddEdge;
		}
	}
	
	
	/**
	 * Finds shortest route between 2 points given as a parameters
	 * @param start Street at which the shortest route begins
	 * @param startPoint Specific point of the start street at which the shortest route begins
	 * @param end Street at which the shortest route ends
	 * @param endPoint Specific point of the end street at which the shortest route ends
	 * @return The shortest route between the given points in the form of the LinkedList of streets
	 */
	public LinkedList<Street> shortestRoute(Street start, int startPoint, Street end, int endPoint) {

		boolean[] shortestFound = new boolean[vertices.length];
		int[] shortestRoute = new int[vertices.length];
		
		@SuppressWarnings("unchecked")
		LinkedList<Cross>[] listOfRoutes = (LinkedList<Cross>[]) new LinkedList[vertices.length];
		
		for(int i = 0;  i < listOfRoutes.length; ++i) {
			if(start.getStart() == vertices[i]) 
				shortestRoute[i] = startPoint;
			else if(start.getEnd() == vertices[i]) 
				shortestRoute[i] = start.getLength() - startPoint;
			else 
				shortestRoute[i] = Integer.MAX_VALUE;
		
			listOfRoutes[i] = new LinkedList<Cross>();
			listOfRoutes[i].add(vertices[i]);
		}

		Street[] currentEdges = new Street[4];
		LinkedList<Cross> currentRoute = new LinkedList<Cross>();
		int currentLength = Integer.MAX_VALUE;
		int nextIndex = 0;
		
		for(int i = 0; i < vertices.length; ++i) {
			if(shortestRoute[i] < currentLength ) {
				nextIndex = i;
				currentLength = shortestRoute[i];
			}
		}
		currentRoute = listOfRoutes[nextIndex];
		shortestFound[nextIndex] = true;
		
		Cross lastVertex = null;
		int finalRouteLength = Integer.MAX_VALUE;

		for(int i = 0; i < vertices.length; ++i) {
			currentEdges[0] = currentRoute.getLast().getNorthStreet();
			currentEdges[1] = currentRoute.getLast().getEastStreet();
			currentEdges[2] = currentRoute.getLast().getSouthStreet();
			currentEdges[3] = currentRoute.getLast().getWestStreet();
			
			
			for(int j = 0; j < currentEdges.length; ++j) {
			
				if(currentEdges[j] == null)
					continue;
				
				Cross currentVertex = currentRoute.getLast();
				
				//checking if we found the vertex that is connected with our destination
				if(currentEdges[j] == end) {
					
					int lengthOfCurrentRoute;
					if(currentEdges[j].getStart() == currentVertex) 
						lengthOfCurrentRoute = currentLength + endPoint;
					else 
						lengthOfCurrentRoute = currentLength + currentEdges[j].getLength() - endPoint;
					
					if(lengthOfCurrentRoute < finalRouteLength) {
						finalRouteLength = lengthOfCurrentRoute;
						lastVertex = currentVertex;
					}
				}
				
				//checking which end of the street is the vertex that we are looking for
				if(currentEdges[j].getEnd() == currentVertex) {
					currentVertex = (Cross)currentEdges[j].getStart();
				}
				else if(currentEdges[j].getEnd() != null) 
					currentVertex = (Cross)currentEdges[j].getEnd();
				else
					continue;
				
				//updating the route to the current vertex if shorter was found
				for(int k = 0; k < vertices.length; ++k) {
					
					if(listOfRoutes[k].getLast() == currentVertex) {
						int temp = currentLength + currentEdges[j].getLength();
						if(shortestRoute[k] > temp) {
							shortestRoute[k] = temp;
							
							listOfRoutes[k] = new LinkedList<Cross>(currentRoute);
							listOfRoutes[k].add(currentVertex);
						}
						break;
					}
				}
			}
			
			nextIndex = 0;
			int lastValue = Integer.MAX_VALUE;
			for(int j = 0; j < vertices.length; ++j) {
				if(shortestRoute[j] < lastValue && !shortestFound[j]) {
					nextIndex = j;
					lastValue = shortestRoute[j];
				}
			}
			
			if(lastValue == Integer.MAX_VALUE) {
				break;
			}
			
			shortestFound[nextIndex] = true;
			currentRoute = listOfRoutes[nextIndex];
			currentLength = shortestRoute[nextIndex];
			
		}
		
		int indexOfRouteToReturn = 0;
		for(int i = 0; i < vertices.length; ++i) {
			if(vertices[i] == lastVertex) {
				indexOfRouteToReturn = i;
				break;
			}
		}
		
		LinkedList<Street> listToReturn = CrossListToStreetList(listOfRoutes[indexOfRouteToReturn]);
		listToReturn.add(end);
		
		return listToReturn;
	}
	
	/**
	 * Takes 2 crosses as the parameters and returns the street that connects those 2 crosses
	 * @param cross1 First cross 
	 * @param cross2 Second cross
	 * @return The street that connects first cross with the second cross
	 */
	private Street getConnectingStreet(Cross cross1, Cross cross2) {
		if(cross1.getTopLeftY() < cross2.getTopLeftY() && cross1.getTopLeftX() == cross2.getTopLeftX()) {
			return cross1.getSouthStreet();
		}
		else if(cross1.getTopLeftX() == cross2.getTopLeftX()) {
			return cross1.getNorthStreet();
		}
		else if(cross1.getTopLeftX() < cross2.getTopLeftX()) {
			return cross1.getEastStreet();
		}
		else {
			return cross1.getWestStreet();
		}
	}
	
	/**
	 * Takes LinkedList of crosses as a parameter and returns a LinkedList of streets based on the one given
	 * @param list The list of crosses based on which the list of streets is to be returned
	 * @return The list of streets based on the list of crosses taken as parameter
	 */
	private LinkedList<Street> CrossListToStreetList(LinkedList<Cross> list){
		LinkedList<Street> routeToReturn = new LinkedList<Street>();
		
		ListIterator<Cross> iterator1 = list.listIterator();
		ListIterator<Cross> iterator2 = list.listIterator();
		iterator2.next();
		
		int maxIndex = list.size() - 1;
		for(int i = 0; i < maxIndex; ++i) {
			routeToReturn.add(getConnectingStreet(iterator1.next(), iterator2.next()));
		}
		return routeToReturn;
	}
	
	/**
	 * 
	 * @return The array of streets
	 */
	public Street[] getArrOfEdges() {
		return edges;
	}
	
	/**
	 * 
	 * @return The array of crosses
	 */
	public Cross[] getArrOfVertices() {
		return vertices;
	}
	
	/**
	 * A method that uses to generate a new car
	 * @param start A street at which the new car should appear
	 * @param minLen A minimum length of the car that will be generated
	 * @param maxLen A maximum length of the car that will be generated
	 * @param minWidth A minimum width of the car that will be generated
	 * @param maxWidth A maximum width of the car that will be generated
	 * @param minGap A minimum gap of the car that will be generated
	 * @param maxGap A maximum gap of the car that will be generated
	 * @param minVMod A minimum velocity modifier of the car that will be generated
	 * @param maxVMod A maximum velocity modifier of the car that will be generated
	 * @param minAcc A minimum acceleration of the car that will be generated
	 * @param maxAcc A maximum acceleration of the car that will be generated
	 * @param minRTime A minimum reaction time of the car that will be generated
	 * @param maxRTime A maximum reaction time of the car that will be generated
	 */
	public void generateCar(Street start, int minLen, int maxLen, int minWidth, int maxWidth, int minGap, int maxGap,
							int minVMod, int maxVMod, int minAcc, int maxAcc, int minRTime, int maxRTime) {
		Random r = new Random();
		int length = randomize(r, minLen, maxLen);
		int width = randomize(r, minWidth, maxWidth);
		int gap = randomize(r, minGap, maxGap);
		int velModifier = randomize(r, minVMod, maxVMod);
		int acceleration = randomize(r, minAcc, maxAcc);
		int reactionTime = randomize(r, minRTime, maxRTime);
		
		Street end = edges[r.nextInt(edges.length)];
		
		int begginingPoint = randomize(r, length, start.getLength() - length - gap);
		int endPoint = randomize(r, length, end.getLength() - length - gap);
		LinkedList<Street> route = shortestRoute(start, begginingPoint, end, endPoint);
		
		if(route.getLast() != start)
			route.add(0, start);
		
		Direction dir;
		int whichDirection;
		
		if(route.size() > 1) {
			Cross tempCross = getCrossBetweenStreets(start, route.get(1));
			
			if(tempCross.getNorthStreet() == start) {
				dir = Direction.South;
				whichDirection = 1;
			}
			else if(tempCross.getEastStreet() == start) {
				dir = Direction.West;
				whichDirection = 1;
			}
			else if(tempCross.getSouthStreet() == start) {
				dir = Direction.North;
				whichDirection = 0;
			}
			else {
				dir = Direction.East;
				whichDirection = 0;
			}
			
		}
		else if(start.isSouthNorthOriented()) {
			if(begginingPoint > endPoint) {
				dir = Direction.North;
				whichDirection = 0;
			}
			else {
				dir = Direction.South;
				whichDirection = 1;
			}
			
		}
		else if(begginingPoint > endPoint) {
			dir = Direction.West;
			whichDirection = 1;
		}
		else {
			dir = Direction.East;
			whichDirection = 0;
		}
		
		if(dir == Direction.North || dir == Direction.South) {
			endPoint += end.TopLeftY;
		}
		else {
			endPoint += end.TopLeftX;
		}
		
		Car newCar = new Car(length, width, gap, dir, reactionTime, 0, acceleration, velModifier, endPoint, route);
		start.insert(newCar, begginingPoint, whichDirection);
	}
	
	private int randomize(Random r, int min, int max) {
		return r.nextInt(max - min) + min;
	}
	
	/**
	 * Takes two streets as parameters and returns the cross that connects them
	 * @param start First street
	 * @param end Second street
	 * @return The cross that connects first street with the second
	 */
	public static Cross getCrossBetweenStreets(Street start, Street end) {
		if(start.getStart() == end.getStart() || start.getStart() == end.getEnd()) {
			return (Cross)start.getStart();
		}
		
		return (Cross)start.getEnd();
	}
}
