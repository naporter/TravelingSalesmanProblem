import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GeneticAlgorithm extends City{
	Random rand = new Random();
	ArrayList<int[]> cities;
	ArrayList<int[]> routes = new ArrayList<int[]>();
	ArrayList<Integer> routeRanks = new ArrayList<Integer>();
	ArrayList<Double> routeFitness = new ArrayList<Double>();
	ArrayList<Integer> topRoutes = new ArrayList<Integer>();
	ArrayList<Double> sortedRoutes = new ArrayList<Double>();
	int numCities;
	int rankSum;
	
	GeneticAlgorithm() {
	}
	//constructor to populate requested number of cities and brings in the generated cities
	GeneticAlgorithm(int numCities, ArrayList<int[]> cities) {
		this.numCities = numCities;
		this.cities = cities;
	}
	
	public void startGeneticAlgorithm() {
		int i = 1;
		while(routes.size() > 1) {	
			System.out.println("GENERATION: " + i);
			System.out.println();
			printRoutes();
			rankRoutes();
			printRouteRanks();
			sumRanks();
			printRankSum();
			makeRouteFitness();
			printRouteFitness();
			startMatingPool();
			printTopRoutes();
			removeLesserRoutes();
			printRoutes();		
			crossover();
			mutate();
			i++;
			if(routes.size() > 1) {
				clearRouteData();
			}			
		}
		int minRouteDistance = routeRanks.get(0);
		for(int j = 0; j < routeRanks.size(); j++) {
			if(routeRanks.get(j) < minRouteDistance) {
				minRouteDistance = routeRanks.get(j);
			}
		}
		for(int[] route : routes){
			System.out.print("The best route is: [");
			for(int r = 0; r < route.length; r++) {
				if(r < route.length - 1) {
					System.out.print(route[r] + ", ");
				}
				else {
					System.out.print(route[r]);
				}
			}
			System.out.print("]");
        }
		System.out.println(" with a total distance of " + minRouteDistance);
		System.out.println("");
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
			routeRanks.add(routeDistance); 
		}
		
	}
	
	
	//return distance between ciites
	public int getDistance(int[] cityA, int[] cityB) {
		int distance = (int)Math.sqrt(Math.pow(Math.abs(cityA[0] - cityB[0]), 2) + //using Pythagorean theorem to find distance
									  Math.pow(Math.abs(cityA[1] - cityB[1]), 2));		
		return distance;
	}
	
	//mutation function, since all cities must be visited this will just swap positions of cities
	public void swapCities(int[] array, int indexA, int indexB) { 
		int temp = array[indexA];
		array[indexA] = array[indexB];
		array[indexB] = temp;
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
	
	//sum the ranks of the routes
	public void sumRanks() {
		rankSum = 0;
		for(int i : routeRanks) {
			rankSum += i;
		}
	}
	
	//assigns a chance of being picked to each route
	public void makeRouteFitness() {
		for(double i: routeRanks) {
			routeFitness.add(100 * i/rankSum);
		}		
	}
	
	//creates a list of the top routes
	public void startMatingPool() {
		int elite = routeRanks.size()/2;
		copy(sortedRoutes, routeFitness);
		Collections.sort(sortedRoutes);//sorts the routes in ascending order
		removeRange(sortedRoutes, elite, sortedRoutes.size());
		for(int i = 0; i < routeFitness.size(); i++) { //adds route number that is in sorted routes and route fitness
			if(sortedRoutes.contains(routeFitness.get(i)) && (!topRoutes.contains(i + 1))) {
				topRoutes.add(i + 1);
			}
		}
		
		while(topRoutes.size() > elite) { //randomly eliminates extras from the top routes if there are any
			topRoutes.remove(rand.nextInt(topRoutes.size()));
		}
		sortedRoutes.clear();
	}
	
	//using elitism to keep top routes
	public void removeLesserRoutes() {
		System.out.println("Removing routes from list . . .");
		int numRoutes = routes.size();
		int removeCounter = 1;
		for(int i = 1; i <= numRoutes; i++) {
			if(!topRoutes.contains(i)) {
				System.out.println("Removing route " + (i));
				routes.remove(i - removeCounter);
				removeCounter++;
			}
		}
	}
	
	//crossover between two routes, rolls new city when a route has duplicate cities
	public void crossover() {
		int crossoverPoint = routes.get(0).length/2; //where to crossover
		int crossoverNum = routes.get(0).length/3; //how many to crossover
		for(int i = 0; i < routes.size(); i = i+2) {
			if((i + 1) >= routes.size()) {
				break;
			}
			int[] firstParent = routes.get(i);
			int[] secondParent = routes.get(i+1);
			System.out.println("Crossing route " + (routes.indexOf(firstParent) + 1) + " with route " + 
							(routes.indexOf(secondParent) + 1));
			int n = crossoverNum;
			for(int j = crossoverPoint; n > 0; j++, n--) {
				int temp = firstParent[j];
				
				firstParent[j] = secondParent[j];
				secondParent[j] = temp;
				while(hasCity(firstParent, firstParent[j])) {
					firstParent[j] = rand.nextInt(numCities) + 1;
				}
				while(hasCity(secondParent, secondParent[j])) {
					secondParent[j] = rand.nextInt(numCities) + 1;
				}
			}
		}
		System.out.println();
	}
	
	//mutate is done by swapping cities
	public void mutate() {
		for(int[] i : routes) {
			System.out.println("Mutating route " + (routes.indexOf(i) + 1));
			swapCities(i,0,i.length/2); //swap first city with city near the middle
		}
		System.out.println();
	}
	
	//checks if route has city
	public boolean hasCity(int[] array, int city) {
		int dupCity = 0;
		for(int i = 0; i < array.length; i++) {
			if(array[i] == city) {
				dupCity++;
			}
			if(dupCity > 1) {
				return true;
			}
		}
		return false;
	}
	
	public void clearRouteData() {
		routeRanks.clear();
		routeFitness.clear();
		topRoutes.clear();
	}
	
	//copy one arraylist to another
	public <E> void copy(ArrayList<E> arrayA, ArrayList<E> arrayB) {
		for(int index = 0; index < arrayB.size(); index++) {
			arrayA.add(arrayB.get(index));
		}
		
	}
	
	//remove a range of values from an arraylist
	public <E> void removeRange(ArrayList<E> array, int lowerIndex, int upperIndex) {
		int numRemoved = upperIndex - lowerIndex;
		while(numRemoved > 0) {
			array.remove(lowerIndex);
			numRemoved--;
		}
		array.trimToSize();
	}
	
	
	////PRINT FUNCTIONS////
	
	
	public void printSortedRoutes() {
		System.out.println("Sorted routes: " + sortedRoutes);
		System.out.println();
	}
	//print the top routes
	public void printTopRoutes() {
		System.out.println("The top routes are: " + topRoutes);
		System.out.println();
	}
	
	//print the percentage of each route being picked
	public void printRouteFitness() {
		int n = 1;
		System.out.println("Route percentages rounded to nearest integer:");
		for(double i: routeFitness) {
			System.out.println("Fitness of route " + n + " is: " + Math.round(i) + "%");
			n++;
		}
		System.out.println();
	}
	
	//print total sum of all route distances
	public void printRankSum() {
		System.out.println("Total sum of all route distances is: " + rankSum);
		System.out.println();
	}
	
	//print total distance for all routes
	public void printRouteRanks() {
		int r = 1;
		for(int i : routeRanks) {
			System.out.println("Total distance of route " + r + " is: " + i);
			r++;
		}
		System.out.println();
	}
	
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
}
