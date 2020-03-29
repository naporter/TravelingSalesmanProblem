import java.util.ArrayList;
import java.util.Random;

public class SimulatedAnnealing {
	
	Random rand = new Random();
	ArrayList<int[]> cities;	
	ArrayList<int[]> routes = new ArrayList<int[]>();
	ArrayList<Integer> routeDistances = new ArrayList<Integer>();
	int numCities;
	double startingTemp;
	int numIterations;
	double coolingRate;
	
	SimulatedAnnealing(){
		this.numCities = 10;
	}
	SimulatedAnnealing(ArrayList<int[]> cities, double startingTemp, int numIterations, double coolingRate){
		this.cities = cities;
		this.numCities = cities.size();
		this.startingTemp = startingTemp;
		this.numIterations = numIterations;
		this.coolingRate = coolingRate;
	}
	public void startSimulatedAnnealing() {
		System.out.println("Starting routes:");
		printRoutes();
		System.out.println("Starting Distances:");
		rankRoutes();
		printRouteDistances();
		
		int bestRoute = routeDistances.get(0);
		int iteration = 1;
		for(int i = 0; i < numIterations; i++) {
			System.out.println("Running iteration " + iteration + ":");
			if(startingTemp <= 0.1) {
				break;
			}
			for(int[] j : routes) {
				int a = rand.nextInt(j.length);
				int b = rand.nextInt(j.length);
				while(b == a) {
					b = rand.nextInt(j.length);
				}				
				swapCities(j, a, b);
				int newRank = rankSingleRoute(routes.indexOf(j));				
				if (Math.exp((routeDistances.get(routes.indexOf(j)) - newRank) / startingTemp) < Math.random()) {// using Boltzmann function of probability distribution
					swapCities(j, a, b);
				}
				else {
					routeDistances.set(routes.indexOf(j), newRank);
				}
			}
			int r = routeDistances.get(0);
			int a = routeDistances.indexOf(r);
			for(int d : routeDistances) {
				if(d < r) {
					a = routeDistances.indexOf(d);
					r = routeDistances.get(a);					
				}
			}
			System.out.println("Current best route is " + (a) + " with a distance of " + r);
			startingTemp *= coolingRate;
			printRoutes();
			printRouteDistances();
			bestRoute = getBestRoute();
			iteration++;
		}
		System.out.println("The best route is route " + (bestRoute+1));
		
	}
	//make all routes
	public void makeRoutes(int numRoutes) {		
		int[] newRoute = new int[numCities];
		int index = 0;
		for(int city = 1; index < numCities; city++,index++) { //initialize first route
			newRoute[index] = city;
		}
		routes.add(newRoute);
		
		while(numRoutes > 1) { //accounts for the route that is already made and when the condition is checked after the last route
			newRoute = new int[numCities];
			populateRoute(newRoute);
			shuffleRoute(newRoute);
			routes.add(newRoute);
			numRoutes--;
		}
	}
	
	//populate the given route
	public void populateRoute(int[] array) {
		int index = 0;
		for(int city = 1; index < numCities; city++,index++) { //initialize first route
			array[index] = city;
		}
	}
	
	//randomly shuffle routes
	public void shuffleRoute(int[] array) {
		for (int i = 0; i < array.length; i++) {
			int randomIndexToSwap = rand.nextInt(array.length);
			int temp = array[randomIndexToSwap];
			array[randomIndexToSwap] = array[i];
			array[i] = temp;
		}
	}
	
	//swap two cities
	public void swapCities(int[] array, int indexA, int indexB) { 
		int temp = array[indexA];
		array[indexA] = array[indexB];
		array[indexB] = temp;
	}
	
	//defines a total distance traveled for each route
	public void rankRoutes() {
		int cityA;
		int cityB;
		for(int[] i : routes) { //navigates routes
			int routeDistance = 0;
			for(int j = 0; j < i.length; j++) {// navigates the cities in each route
				cityA = i[j];
				if((j + 1) == i.length) { //goes back to the beginning of the route if j runs over array
					cityB = i[0];
				}
				else {
					cityB = i[j+1];
				}			
				routeDistance += getDistance(cities.get(cityA - 1), cities.get(cityB - 1)); //add distance between all cities in the route
			}
			routeDistances.add(routeDistance); 
		}
		
	}
	
	//return distance of route to see if it is better
	public int rankSingleRoute(int route) {
		int cityA;
		int cityB;
		int routeDistance = 0;
		int[] currentRoute = routes.get(route);
		for(int j = 0; j < currentRoute.length; j++) {// navigates the cities in each route
			cityA = currentRoute[j];
			if((j + 1) == currentRoute.length) { //goes back to the beginning of the route if j runs over array
				cityB = currentRoute[0];
			}
			else {
				cityB = currentRoute[j+1];
			}			
			routeDistance += getDistance(cities.get(cityA - 1), cities.get(cityB - 1)); //add distance between all cities in the route
		}
		return routeDistance;
	}
	
	//return distance between cities
	public int getDistance(int[] cityA, int[] cityB) {
		int distance = (int)Math.sqrt(Math.pow(Math.abs(cityA[0] - cityB[0]), 2) + //using Pythagorean theorem to find distance
									  Math.pow(Math.abs(cityA[1] - cityB[1]), 2));		
		return distance;
	}
	
	public int getBestRoute() {
		int bestRoute = 0;
		for(int i : routeDistances) {
			if(i < routeDistances.get(bestRoute)) {
				bestRoute = routeDistances.indexOf(i);
			}
		}
		return bestRoute;
	}
	
	//PRINT FUNCTIONS
	
	//print all routes
	public void printRoutes() {
		int i = 1;
		for(int[] route : routes){
			System.out.print("Route number " + i + " is: [");
			for(int r = 0; r < route.length; r++) {
				if(r < route.length - 1) {
					System.out.print(route[r] + ", ");
				}
				else {
					System.out.print(route[r]);
				}
			}
			System.out.println("]");
			i++;
        }
		System.out.println();
	}
	
	//print all route distances
	public void printRouteDistances() {
		int r = 1;
		for(int i : routeDistances) {
			System.out.println("Total distance of route " + r + " is: " + i);
			r++;
		}
		System.out.println();
	}
}
