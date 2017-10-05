package assignment13;

import static org.junit.Assert.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Morris Su, Maycol Vilchez
 */
public class NetworkGraphTest
{
    NetworkGraph airportGraph = null;
    NetworkGraph airportGraph1 = null;
    NetworkGraph airportGraph2 = null;
    NetworkGraph airportGraph3 = null;
    NetworkGraph airportGraph4 = null;

    @Before
    public void initialeze ()
    {

        try
        {
            airportGraph = new NetworkGraph("testfile.csv");
            airportGraph1 = new NetworkGraph("testfile1.csv");
            airportGraph2 = new NetworkGraph("testfile2.csv");
            airportGraph3 = new NetworkGraph("testfile3.csv");
            airportGraph4 = new NetworkGraph("testFlight2..csv");

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /************************************ Small List of Flights************************************/
    @Test
    public void shortestPathOfAfewFlightsByDelay ()
    {
        double costPath = airportGraph.getBestPath("HFZ", "LEA", FlightCriteria.DELAY).getPathLenght();
        assertEquals("90.5", costPath + "");
    }
    
    @Test
    public void shortestPathOfAfewFlightsByCanceled ()
    {
        double costPath = airportGraph.getBestPath("HFZ", "LEA", FlightCriteria.CANCELED).getPathLenght();
        assertEquals("0.5", costPath + "");
    }
    
    @Test
    public void shortestPathOfAfewFlightsByTime()
    {
        double timePath = airportGraph.getBestPath("HFZ", "LEA", FlightCriteria.TIME).getPathLenght();
        assertEquals("279.0", timePath + "");
    }
    
    @Test
    public void shortestPathOfAfewFlightsByDistance()
    {
        double distancePath = airportGraph.getBestPath("HFZ", "LEA", FlightCriteria.DISTANCE).getPathLenght();
        assertEquals("1935.0", distancePath + "");
    }
   
    @Test
    public void shortestPathOfAfewFlightsByCost ()
    {
        double costPath = airportGraph.getBestPath("HFZ", "LEA", FlightCriteria.COST).getPathLenght();
        assertEquals("782.9099999999999", costPath + "");
    }
    
    /************************************* Regular List of Flights************************************/
    
    @Test
    public void shortestPathOfAlotFlightsByDelay()
    {
        double timePath = airportGraph1.getBestPath("FNL", "HKB", FlightCriteria.DELAY).getPathLenght();
        assertEquals("444.0", timePath + "");
    }

    @Test
    public void shortestPathOfAlotFlightsByCanceled()
    {
        double timePath = airportGraph1.getBestPath("FNL", "HKB", FlightCriteria.CANCELED).getPathLenght();
        assertEquals("0.0", timePath + "");
    }

    @Test
    public void shortestPathOfAlotFlightsByTime()
    {
        double timePath = airportGraph1.getBestPath("FNL", "HKB", FlightCriteria.TIME).getPathLenght();
        assertEquals("437.0", timePath + "");
    }
    
    @Test
    public void shortestPathOfAlotFlightsByDistance()
    {
        double distancePath = airportGraph1.getBestPath("FNL", "HKB", FlightCriteria.DISTANCE).getPathLenght();
        assertEquals("3017.0", distancePath + "");
    }
    
    @Test
    public void shortestPathOfAlotFlightsByeCost ()
    {
        double costPath = airportGraph1.getBestPath("FNL", "HKB", FlightCriteria.COST).getPathLenght();
        assertEquals("1221.18", costPath + "");
    }

    /********************************** When Origin or Destination not defined ******************************/
    
    @Test
    public void testOriginNotDefined ()
    {
        airportGraph2.getBestPath(null, "PVY", FlightCriteria.DISTANCE);

        ArrayList<String> pathGoal = new ArrayList<String>();
        pathGoal.add(null);
        pathGoal.add("PVY");
        assertTrue(airportGraph2.getBestPath(null, "PVY", FlightCriteria.DISTANCE).getPath().equals(pathGoal));
        assertTrue(airportGraph2.getBestPath(null, "PVY", FlightCriteria.DISTANCE).getPathLenght() == 0.0);
    }

    @Test
    public void testDestinationNotDefined ()
    {
        airportGraph2.getBestPath("VCI", null, FlightCriteria.TIME);

        ArrayList<String> pathGoal = new ArrayList<String>();
        pathGoal.add("VCI");
        pathGoal.add(null);
        assertTrue(airportGraph2.getBestPath("VCI", null, FlightCriteria.DISTANCE).getPath().equals(pathGoal));
        assertTrue(airportGraph2.getBestPath("VCI", null, FlightCriteria.DISTANCE).getPathLenght() == 0.0);
    }

    @Test
    public void testOriginAndDestinationNotDefined ()
    {
        airportGraph2.getBestPath(null, null, FlightCriteria.COST);

        ArrayList<String> pathGoal = new ArrayList<String>();
        pathGoal.add(null);
        pathGoal.add(null);
        assertTrue(airportGraph2.getBestPath(null, null, FlightCriteria.DISTANCE).getPath().equals(pathGoal));
        assertTrue(airportGraph2.getBestPath(null, null, FlightCriteria.DISTANCE).getPathLenght() == 0.0);
    }

    /********************************** Negatives Values ******************************/
    
    @Test
    public void shortestPathOf_Afew_FlightsBy_CostHoldingNegativeValues ()
    {
        double costPath = airportGraph3.getBestPath("USR", "NEP", FlightCriteria.COST).getPathLenght();
        ArrayList<String> pathGoal = new ArrayList<String>();
        pathGoal.add("USR");
        pathGoal.add("ABC");
        pathGoal.add("NEP");
        System.out.println(airportGraph3.getBestPath("USR", "NEP", FlightCriteria.COST).getPath().toString());
        assertTrue(airportGraph3.getBestPath("USR", "NEP", FlightCriteria.COST).getPath().containsAll(pathGoal));
        assertEquals("1119.45", costPath + "");
    }

    /********************************** No path *****************************************/
    
    @Test
    public void shortestPathOfNoPath ()
    {
        double costPath = airportGraph4.getBestPath("ABC", "AAA", FlightCriteria.COST).getPathLenght();
        ArrayList<String> pathGoal = new ArrayList<String>();
        assertTrue(airportGraph4.getBestPath("ABC", "AAA", FlightCriteria.COST).getPath().equals(pathGoal));
        assertEquals("0.0", costPath + "");
    }

    /********************************** File does not exist ******************************/
    
    @Test
    public void testFileNotFound ()
    {
        NetworkGraph airportG = null;
        try
        {
            airportG = new NetworkGraph("tesjjuytt.csv");
        }
        catch (FileNotFoundException e)
        {
            assertTrue(airportG == null);
        }
    }
}
