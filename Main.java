import java.util.Scanner;

/* 
 * This was used as a general guide for the genetic algorithm involving TSP:
 * Citation: https://towardsdatascience.com/evolution-of-a-salesman-a-complete-genetic-algorithm-tutorial-for-python-6fe5d2b3ca35

*/

public class Main {

	public static void main(String[] args) {
		
		//This section is used to run the code from command line with user input
		Scanner input = new Scanner(System.in);
		System.out.println("Every step will be printed out, to include every route.\nChoosing a large number of cities will yield a large amount of text.");
		System.out.println("How many cities to use?");
		int numCities = input.nextInt();
		System.out.println("How many routes to use?");
		int numRoutes = input.nextInt();
		System.out.println("For Simulated Annealing, what is your starting temperature, number of iterations, and cooling rate?\nExample Input: 10 5 0.5");
		double startTemp = input.nextDouble();
		int numIteration = input.nextInt();
		double coolRate = input.nextDouble();
		
		
		
		City cities = new City(numCities);//you can change integer for number of cities, choosing a number 5 or over gives clearer results, choosing nothing defaults to 5
		cities.populate();
		System.out.println("Randomly generated cities are:");
		cities.printArrayList();
		
		System.out.println("USING GENETIC ALGORITHM:");
		GeneticAlgorithm ga = new GeneticAlgorithm(cities.getNumCities(), cities.getAllCities());
		ga.makeRoutes(numRoutes); // you can change integer for number of routes to be made, number should remain less than (numCities!) for clear results
		
		long start = System.currentTimeMillis();
		ga.startGeneticAlgorithm();
		long finish = System.currentTimeMillis();
		long time = finish - start;
		System.out.println("Time in milliseconds to complete Genetic Algorithm: " + time);
		
		System.out.println("Randomly generated cities are:");
		cities.printArrayList();
		
		System.out.println("USING SIMULATED ANNEALING ALGORITHM:");
		SimulatedAnnealing sa = new SimulatedAnnealing(cities.getAllCities(), startTemp, numIteration, coolRate);
		sa.makeRoutes(numRoutes);
		start = System.currentTimeMillis();
		sa.startSimulatedAnnealing();
		finish = System.currentTimeMillis();
		time = finish - start;
		System.out.println("Time in milliseconds to complete Simulated Annealing: " + time);
		input.close();
	}
}
