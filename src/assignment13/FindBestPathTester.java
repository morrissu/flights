package assignment13;

import java.io.FileNotFoundException;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Set;

/**
 * <p>An example of how a user will use your best flight API.</p>
 * <p>You will still be required to writed JUnit tests to test your program.</p>
 *
 * @author CS2420 Teaching Staff - Spring 2016
 *
 */
public class FindBestPathTester {

	public static void main(String[] args) throws FileNotFoundException {
		NetworkGraph airportGraph = null;
//		PrintStream out;
//        out =  new PrintStream("hgu");
//        System.setOut(out);
		try {
			airportGraph = new NetworkGraph("flights-2015-q3.csv");
//			 Set<String> keysSet = (Set<String>) (airportGraph.getData().keySet());           
//	            for(String keyString : keysSet) {
//	                ArrayList<String[]> airportData = ((airportGraph.getData().get(keyString))).getAirportFlightData();
//	                
//	                for (String[] flight : airportData) {
//	                    System.out.println("origin: " + flight[0] + " destination: " + flight[1] + " carriers: " + flight[2] + " delay: " + flight[3] + " cancel: " + flight[4] + " time: " + flight[5] +  " distance: " + flight[6] +  " cost: " + flight[7] );
//	                }
//	            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Returns the shortest distance path of flights from MOB to ACV
		// Solution: a path of ['MOB', 'DFW', 'SFO', 'ACV'] and distance of 2253
		BestPath shortestDistancePath = airportGraph.getBestPath("MOB", "ACV", FlightCriteria.DISTANCE);
		System.out.println(shortestDistancePath.toString());
		
		// Returns the shortest distance path of flights from SFO to DWF when flying with DL
		// Solution: a path of ['SFO', 'SLC', 'DFW'] and distance of 1588
		BestPath shortestDistancePath2 = airportGraph.getBestPath("SFO", "DFW", FlightCriteria.DISTANCE, "DL");
		System.out.println(shortestDistancePath2.toString());
		
		// Returns the shortest flight time path from MOB to SLC
		// Solution: a path of ['MOB', 'DFW', 'SLC'] and time of ~269.25
		BestPath shortestTimePath = airportGraph.getBestPath("MOB", "SLC", FlightCriteria.TIME);
		System.out.println(shortestTimePath.toString());
		
		// Returns the fiscally cheapest path of flights from LAS to LAX
		// Solution: a path of ['LAS', 'LAX'] and cost of ~138.39
		BestPath cheapestPath = airportGraph.getBestPath("LAS", "LAX", FlightCriteria.COST);
		System.out.println(cheapestPath.toString());
	}

}