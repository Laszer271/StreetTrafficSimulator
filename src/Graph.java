package trafficSimulation;

import java.util.LinkedList;
import java.util.ListIterator;

public class Graph {
	private Cross[] vertices;
	private Street[] edges;
	
	private int indexToAddVertex;
	private int indexToAddEdge;
	
	public Graph(int numOfCrosses, int numOfStreets) {
		vertices = new Cross[numOfCrosses];
		edges = new Street[numOfStreets];
		
		indexToAddVertex = 0;
		indexToAddEdge = 0;
	}
	
	public Graph(Cross[] crosses, Street[] streets) {
		vertices = crosses;
		edges = streets;
		
		indexToAddVertex = crosses.length;
		indexToAddEdge = streets.length;
	}
	
	public void addCross(Cross cross) {
		vertices[indexToAddVertex] = cross;
	
		++indexToAddVertex;
	}
	
	public void addCrosses(Cross[] crosses) {
		for(int i = 0; i < crosses.length; ++i) {
			vertices[indexToAddVertex] = crosses[i];
			
			++indexToAddVertex;
		}
	}
	
	public void addStreet(Street street) {
		edges[indexToAddEdge] = street;
	
		++indexToAddEdge;
	}
	
	public void addStreets(Street[] streets) {
		for(int i = 0; i < streets.length; ++i) {
			edges[indexToAddEdge] = streets[i];
			
			++indexToAddEdge;
		}
	}
	
	public LinkedList<Street> shortestRoute(Cross start, Cross end) {
		int numberOfVertices = vertices.length - 1;
		boolean[] shortestFound = new boolean[numberOfVertices];
		int[] shortestRoute = new int[numberOfVertices];
		int whichToReturn = 0;
		
		@SuppressWarnings("unchecked")
		LinkedList<Cross>[] listOfRoutes = (LinkedList<Cross>[]) new LinkedList[numberOfVertices];
		
		for(int i = 0, j = 0;  j < listOfRoutes.length; ++i, ++j) {
			if(vertices[i] != start) {
				shortestRoute[j] = Integer.MAX_VALUE;
				
				listOfRoutes[j] = new LinkedList<Cross>();
				listOfRoutes[j].add(start);
				listOfRoutes[j].add(vertices[i]);
			}
			else 
				--j;
		}

		Street[] currentEdges = new Street[4];
		LinkedList<Cross> currentRoute = new LinkedList<Cross>();
		currentRoute.add(start);
		int currentLength = 0;

		for(int i = 0; i < numberOfVertices; ++i) {
			currentEdges[0] = currentRoute.getLast().getNorthStreet();
			currentEdges[1] = currentRoute.getLast().getEastStreet();
			currentEdges[2] = currentRoute.getLast().getSouthStreet();
			currentEdges[3] = currentRoute.getLast().getWestStreet();
			
			for(int j = 0; j < currentEdges.length; ++j) {
				if(currentEdges[j] == null)
					continue;
				
				//checking which end of the street is the vertex that we are looking for
				Cross currentVertex;
				if(currentEdges[j].getEnd() == currentRoute.getLast()) {
					currentVertex = (Cross)currentEdges[j].getStart();
				}
				else {
					currentVertex = (Cross)currentEdges[j].getEnd();
				}
				
				for(int k = 0; k < numberOfVertices; ++k) {
					
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
			
			int nextIndex = 0;
			int lastValue = Integer.MAX_VALUE;
			for(int j = 0; j < numberOfVertices; ++j) {
				if(shortestRoute[j] <= lastValue && !shortestFound[j]) {
					nextIndex = j;
					lastValue = shortestRoute[j];
				}
			}
			
			shortestFound[nextIndex] = true;
			System.out.println("nextIndex: " + nextIndex + "   shortestRoute: " + shortestRoute[nextIndex]);
			currentRoute = listOfRoutes[nextIndex];
			
			if(currentRoute.getLast() == end) {
				whichToReturn = nextIndex;
				break;
			}
			
			currentLength = shortestRoute[nextIndex];
			
		}
		
		return CrossListToStreetList(listOfRoutes[whichToReturn]);
	}
	
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
	
	public Street[] getArrOfEdges() {
		return edges;
	}
	
	public Cross[] getArrOfVertices() {
		return vertices;
	}
	
	
}
