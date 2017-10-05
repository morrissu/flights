/**
 * 
 * @author Morris Su, Maycol Vilchez
 */
package assignment13;

import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.lang.String;

/**
 * <p>
 * This class represents a graph of flights and airports along with specific
 * data about those flights. It is recommended to create an airport class and a
 * flight class to represent nodes and edges respectively. There are other ways
 * to accomplish this and you are free to explore those.
 * </p>
 * 
 * <p>
 * Testing will be done with different criteria for the "best" path so be sure
 * to keep all information from the given file. Also, before implementing this
 * class (or any other) draw a diagram of how each class will work in relation
 * to the others. Creating such a diagram will help avoid headache and confusion
 * later.
 * </p>
 * 
 * <p>
 * Be aware also that you are free to add any member variables or methods needed
 * to completed the task at hand
 * </p>
 * 
 * @author CS2420 Teaching Staff - Spring 2016
 */
public class NetworkGraph {

    // ArrayList<Flight>[][] allFlights; // 2D array, origin x destination.
    // other data structure??

    private Hashtable<String, Airport> allFlights;

    int kindOfPath;

    public Hashtable<String, Airport> getData() {
        return allFlights;
    }

    /**
     * <p>
     * Constructs a NetworkGraph object and populates it with the information
     * contained in the given file. See the sample files or a randomly generated
     * one for the proper file format.
     * </p>
     * 
     * <p>
     * You will notice that in the given files there are duplicate flights with
     * some differing information. That information should be averaged and
     * stored properly. For example some times flights are canceled and that is
     * represented with a 1 if it is and a 0 if it is not. When several of the
     * same flights are reported totals should be added up and then reported as
     * an average or a probability (value between 0-1 inclusive).
     * </p>
     * 
     * @param flightInfoPath
     *            - The path to the file containing flight data. This should be
     *            a *.csv(comma separated value) file
     * 
     * @throws FileNotFoundException
     *             The only exception that can be thrown in this assignment and
     *             only if a file is not found.
     */

    public NetworkGraph(String flightInfoPath) throws FileNotFoundException {
        // TODO: Implement a constructor that reads in the file and stores the
        // information
        // appropriately in this object.
        BufferedReader file = new BufferedReader(new FileReader(flightInfoPath));
        String line = null;
        String[] tmpInfo;
        Hashtable<String, LinkedList<String[]>> info = new Hashtable<String, LinkedList<String[]>>();
        try {
            file.readLine();
            while ((line = file.readLine()) != null) {
                tmpInfo = new String[9];
                for (int i = 0; i < 8; i++) {
                    tmpInfo[i] = line.split(",")[i];
                }
                tmpInfo[8] = "1"; // count

                // aggregates flights
                if (info.get(tmpInfo[0] + tmpInfo[1]) != null) {
                    info.get(tmpInfo[0] + tmpInfo[1]).add(tmpInfo);
                }
                else {
                    LinkedList<String[]> flightList = new LinkedList<String[]>();
                    flightList.add(tmpInfo);
                    info.put(tmpInfo[0] + tmpInfo[1], flightList);
                }
            }

            // averages flight attributes.
            for (String key : info.keySet()) {
                LinkedList<String[]> listOfFlightsFromA = info.get(key);
                if (listOfFlightsFromA.size() > 1) {
                    String[] aggregate = new String[9];
                    aggregate[0] = listOfFlightsFromA.getFirst()[0];
                    aggregate[1] = listOfFlightsFromA.getFirst()[1];
                    aggregate[2] = "";
                    aggregate[3] = "0";
                    aggregate[4] = "0";
                    aggregate[5] = "0";
                    aggregate[6] = "0";
                    aggregate[7] = "0";
                    aggregate[8] = "0";
                    for (String[] flight : listOfFlightsFromA) {
                        
                        for (int o = 3; o < 8; o++) {
                            aggregate[o] = "" + (Double.parseDouble(aggregate[o]) + Double.parseDouble(flight[o]));
                        }                           
                        aggregate[8] = "" + (Double.parseDouble(aggregate[8]) + 1);
                        if (aggregate[2].indexOf(flight[2]) < 0) {
                            aggregate[2] = aggregate[2] + " " + flight[2];
                        }

                    }
                    for (int o = 3; o < 8; o++) {
                        aggregate[o] ="" + (Double.parseDouble(aggregate[o])/Double.parseDouble(aggregate[8]));
                    }
                    listOfFlightsFromA.addFirst(aggregate); // if duplicate flights found, first in array represents aggregate
                }
                
            }
            
            // adds flight into Airport in Hashtable.
            allFlights = new Hashtable<String, Airport>();
            for (String key : info.keySet()) {
                String[] currentFlight = info.get(key).getFirst();
                String currentOrigin = currentFlight[0];
                Flight currentFlightFlight = new Flight(currentFlight[0], currentFlight[1], currentFlight[2], Double.parseDouble(currentFlight[3]), Double.parseDouble(currentFlight[4]), Double.parseDouble(currentFlight[5]), Double.parseDouble(currentFlight[6]), Double.parseDouble(currentFlight[7]));
                
                if(allFlights.size() == 0 || !allFlights.containsKey(currentOrigin)) { // if airport with the name not found, create new airport
                        // and add flight.
                    Airport currentAirport = new Airport(currentOrigin);
                    currentAirport.addFlight(currentFlightFlight);
                    allFlights.put(currentOrigin, currentAirport);
                }
                
                else { // if duplicate airport found, add flight to the airport.
                    allFlights.get(currentOrigin).addFlight(currentFlightFlight);
                }
            }

            
            file.close();
            
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileNotFoundException();
        }

    }

    /**
     * This method returns a BestPath object containing information about the
     * best way to fly to the destination from the origin. "Best" is defined by
     * the FlightCriteria parameter <code>enum</code>. This method should throw
     * no exceptions and simply return a BestPath object with information
     * dictating the result. For example, if a destination or origin is not
     * contained in this instance of NetworkGraph simply return a BestPath with
     * no path (not a <code>null</code> path). If origin or destination are
     * <code>null</code> return a BestPath object with null as origin or
     * destination (which ever is <code>null</code>.
     * 
     * @param origin
     *            - The starting location to find a path from. This should be a
     *            3 character long string denoting an airport.
     * 
     * @param destination
     *            - The destination location from the starting airport. Again,
     *            this should be a 3 character long string denoting an airport.
     * 
     * @param criteria
     *            - This enum dictates the definition of "best". Based on this
     *            value a path should be generated and return.
     * 
     * @return - An object containing path information including origin,
     *         destination, and everything in between.
     */
    public BestPath getBestPath(String origin, String destination,
            FlightCriteria criteria) {
        // TODO: First figure out what kind of path you need to get (HINT: Use a
        // switch!) then

        return getBestPath(origin, destination, criteria, "");
    }

    /**
     * <p>
     * This overloaded method should do the same as the one above only when
     * looking for paths skip the ones that don't match the given airliner.
     * </p>
     * 
     * @param origin
     *            - The starting location to find a path from. This should be a
     *            3 character long string denoting an airport.
     * 
     * @param destination
     *            - The destination location from the starting airport. Again,
     *            this should be a 3 character long string denoting an airport.
     * 
     * @param criteria
     *            - This enum dictates the definition of "best". Based on this
     *            value a path should be generated and return.
     * 
     * @param airliner
     *            - a string dictating the airliner the user wants to use
     *            exclusively. Meaning no flights from other airliners will be
     *            considered.
     * 
     * @return - An object containing path information including origin,
     *         destination, and everything in between.
     */
    public BestPath getBestPath(String origin, String destination, FlightCriteria criteria, String airliner) {
       
        if(criteria == null)
        {
            ArrayList<String> bPath = new ArrayList<String>();
            return new BestPath(bPath, 0.0); 
        }
        
        if (airliner == null) {
            airliner = "";
        }
        
        if (origin == null || destination == null) {
            ArrayList<String> bPath = new ArrayList<String>();
            bPath.add(origin);
            bPath.add(destination);
            return new BestPath(bPath, 0.0);    
        }   
        
        kindOfPath = 0;
        String curr = origin;
        Airport currAirport = allFlights.get(curr);

        switch (criteria) {
        case COST:
            kindOfPath = 7;
            break;
        case DELAY:
            kindOfPath = 3;
            break;
        case DISTANCE:
            kindOfPath = 6;
            break;
        case CANCELED:
            kindOfPath = 4;
            break;
        case TIME:
            kindOfPath = 5;
            break;
        default:
            System.out.println("error"); // ??
        }

        //Comparator for priority queue

        class AirportComparator implements Comparator<String> {

            public int compare(String ap1, String ap2) {
                double toAp1Data = allFlights.get(ap1).getCost();
                double toAp2Data = allFlights.get(ap2).getCost();

                if(toAp1Data > toAp2Data) {
                    return 1;
                } else if (toAp1Data < toAp2Data) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
        
        // Search for the shortest path using Dijkstra's algorithm.

        AirportComparator compareAirportComparator = new AirportComparator();
        PriorityQueue<String> airportQueue = new PriorityQueue<String>(compareAirportComparator);
        airportQueue.add(origin);
        if (allFlights.get(destination) == null) {
        	Airport destinationAirport = new Airport(destination);
            allFlights.put(destination, destinationAirport);
        }
        try {
            allFlights.get(origin).setCost(0); //
        }
        
        catch (Exception e) {
            ArrayList<String> pathArrayList = new ArrayList<String>();
            return new BestPath (pathArrayList, 0.0);
        }
        
        boolean backTrack = false;

        while (!airportQueue.isEmpty()) {
        
            curr = airportQueue.poll(); // null if queue empty!
            currAirport = allFlights.get(curr);
            

            if (curr.equals(destination)) { // destination reached
                backTrack = true;
                break;
            }
            currAirport.isVisited(true);

            for (String[] connectionFlight : currAirport.getAirportFlightData()) {
                if ((allFlights.get(connectionFlight[1])) != null)
                {
                    Airport connectingAirport = allFlights.get(connectionFlight[1]);
                    if (connectingAirport.getCost() > currAirport.getCost()
                            + Double.parseDouble(connectionFlight[kindOfPath])
                            && connectionFlight[2].contains(airliner) && Double.parseDouble(connectionFlight[kindOfPath]) >= 0)
                    {
                        airportQueue.offer(connectionFlight[1]);
                        connectingAirport.setPrev(currAirport);
                        connectingAirport.setCost(currAirport.getCost()
                                + Double.parseDouble(connectionFlight[kindOfPath]));
                    }
                }
            }
        }

        BestPath bPath = new BestPath(new ArrayList<String>(), 0);
        LinkedList<String> airportList = new LinkedList<String>();
//      Airport temp;
        if(backTrack){
        bPath.setPathLength(currAirport.getCost());
        while (!curr.equals(origin)) {
            airportList.addFirst(curr);
            curr = currAirport.getPrev().airportName;
            currAirport = currAirport.getPrev();
        }           
        

        airportList.addFirst(curr);

        for (String airport : airportList) {
            bPath.add(airport);
        }
    }
        for (String keyString : allFlights.keySet())
        {
            allFlights.get(keyString).visited = false;
            allFlights.get(keyString).previousAirport = null;
            allFlights.get(keyString).cost = 10000000000.0;
        }
        return bPath;

    }

    // Helper Classes

    public class Airport {
        private double cost = 10000000000.0;
        private boolean visited = false;
        private ArrayList<String> connected; // destinations from this airport
         private String airportName;
        private ArrayList<Flight> flightsFromAirport;
        private Airport previousAirport = null;

        public Airport(String name) {
            this.airportName = name;
            flightsFromAirport = new ArrayList<Flight>();
            connected = new ArrayList<String>();
        }

        public void setPrev(Airport ap) {
            previousAirport = ap;
        }

        public Airport getPrev() {
            return previousAirport;
        }

        public double getCost() {
            return this.cost;
        }

        public void setCost(double cost) {
            this.cost = cost;
        }

        public void addFlight(Flight fromHere) {
            flightsFromAirport.add(fromHere);
            connected.add(fromHere.getDestination());
        }

        public void isVisited(boolean visited) {
            this.visited = visited;
        }
        
        public boolean getVisited(){
            return this.visited;
        }

        public ArrayList<String[]> getAirportFlightData() {
            ArrayList<String[]> data = new ArrayList<>();
            for (Flight flight : flightsFromAirport) {
                data.add(flight.getFlightData());
            }

            return data;
        }

        public ArrayList<String> getAirporConnectiontData() {
            ArrayList<String> data = new ArrayList<>();
            for (Flight flight : flightsFromAirport) {
                data.add(flight.getDestination());
            }
            return data;
        }
        // Airport prevAirport;
        // ArrayList<Airport> connectedAirports;
        // public Airport(ArrayList<Airport> connected)
        // {
        // this.connectedAirports = connected;
        // }

    }

    public class Flight {
        private String origin;
        private String destination;
        private double canceled;
        private double price;
        private double time;
        private double distance;
        private String carrier;
        private double delay;

        public Flight(String origin, String destination, String carrier,
                double delay, double cancellation, double time,
                double distance, double cost) {
            this.origin = origin;
            this.destination = destination;
            this.price = cost;
            this.time = time;
            this.distance = distance;
            this.carrier = carrier;
            this.delay = delay;
            this.canceled = cancellation;
        }

        public String getDestination() {
            return destination;
        }

        public String[] getFlightData() {
            return new String[] { origin, destination, carrier, "" + delay,
                    "" + canceled, "" + time, "" + distance, "" + price };
        }
    }
}
