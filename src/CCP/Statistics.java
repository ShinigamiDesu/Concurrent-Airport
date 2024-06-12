package CCP;
// TP069502 ALI AHMED ABOUELSEOUD MOUSTAFA TAHA

import java.util.HashMap;
import java.util.Map;


public class Statistics {
    private int disembarkedPassengers = 0;
    private int boardedPassengers = 0;
    private int numberOfPlanesServed = 0;
    private Map<String, Double> planeWaitTimes = new HashMap<>();
    AirTrafficControl atc = new AirTrafficControl(this);
    private int[] Gates;
    private double averageTime=0;
    private double maxTime = 0;
    private double minTime = Double.MAX_VALUE;;
    
    synchronized public void disembarkedPassengers()
    {
        disembarkedPassengers++;
    }
    
    synchronized public void boardedPassengers()
    {
        boardedPassengers++;
    }
    
    synchronized public void planesServed()
    {
        numberOfPlanesServed++;
    }
    
    synchronized public void addPlaneTiming(String PlaneName, double time)
    {
        planeWaitTimes.put(PlaneName, time);
    }
    
    public void displayStats()
    {
        System.out.println("\n\n\n------------------------------------------");
        System.out.println("                STATISTICS");
        System.out.println("------------------------------------------");
        System.out.println("Disembarked Passengers: " + disembarkedPassengers);
        System.out.println("Boarded Passengers: " + boardedPassengers);
        System.out.println("Total Passengers: " + (disembarkedPassengers + boardedPassengers));
        System.out.println("Number Of Planes Served: " + numberOfPlanesServed);
        System.out.println("------------------------------------------");
        System.out.println("             AIRPORT STATUS");
        System.out.println("------------------------------------------");
        this.Gates = atc.getGatesStatus();
        for(int i = 0; i < Gates.length; i++)
        {
            if(Gates[i] == 0)
            {
               System.out.println("GATE " + (i + 1) + ": FREE"); 
            }
            else
            {
                System.out.println("GATE " + (i + 1) + ": OCCUPIED");
            }
        }
        System.out.println("------------------------------------------");
        System.out.println("            PLANE WAITING TIME");
        System.out.println("------------------------------------------");
        for (Map.Entry<String, Double> entry : planeWaitTimes.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + "s");
        }
        for(Map.Entry<String, Double> entry : planeWaitTimes.entrySet())
        {
            averageTime = averageTime + entry.getValue();
        }
        System.out.println("Average Waiting Time: " + averageTime/planeWaitTimes.size() + "s");
        for(Map.Entry<String, Double> entry : planeWaitTimes.entrySet())
        {
            if(entry.getValue() < minTime)
            {
                minTime = entry.getValue();
            }
        }
        System.out.println("Minimum Waiting Time: " + maxTime + "s");
        for(Map.Entry<String, Double> entry : planeWaitTimes.entrySet())
        {
            if(entry.getValue() > maxTime)
            {
                maxTime = entry.getValue();
            }
        }
        System.out.println("Maximum Waiting Time: " + maxTime + "s");
        System.out.println("------------------------------------------");
    }
}
