import java.util.ArrayList;
import java.util.Random;

public class City {
	
	private ArrayList<int[]> allCities = new ArrayList<int[]>();	
	private int gridSize;
	private int numCities;	
	
	City(){
		numCities = 5; //default numCities to 5
		calcGrid(numCities);
	}
	
	City(int numCities){
		this.numCities = numCities;
		calcGrid(numCities);
	}
	
	//adds new city to the array if it has not been already
	public void populate() {		
		int count = numCities;
		while(count > 0) {
			allCities.add(makeNewCity(new int[2]));
			count--;	
		}		
	}
	
	//creates a new random city within the grid
	public int[] makeNewCity(int[] city) {
		Random rand = new Random();
		city[0] = (int)(rand.nextInt(gridSize));
		city[1] = (int)(rand.nextInt(gridSize));
		while(!checkAvail(city)) {
			makeNewCity(city);
		}
		return city;
	}
	
	//return whether city is in arraylist or not
	public boolean checkAvail(int[] array) {
		for(int[] a : allCities) {
			if((array[0] == a[0]) && (array[1] == a[1])) {
				return false;
			}
		}
		return true;
	}
	
	//calculate size of grid based on number of cities
	public void calcGrid(int numCities) {
		int i = 0;
		while(numCities > Math.pow(2, i)) {
			i++;
		}
		gridSize = (int)Math.pow(2, i);
	}
	
	//Print cities
	public void printArrayList() {
		int i = 1;
		for(int[] city : allCities){			
			System.out.println("City number " + i + " is located at: [" + city[0] + "," + city[1] + "]");
			i++;
        }
		System.out.println();
	}
	
	//getters
	public int getNumCities() {
		return this.numCities;
	}
	
	public int getGridSize() {
		return this.gridSize;
	}
	public ArrayList<int[]> getAllCities() {
		return allCities;
	}
}
