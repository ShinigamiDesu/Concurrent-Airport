package CCP;
// TP069502 ALI AHMED ABOUELSEOUD MOUSTAFA TAHA
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AirTrafficControl {
    private Statistics STATS;
    public AirTrafficControl(Statistics stats)
    {
        this.STATS = stats;
    }
    volatile private boolean isRunwayFree = true;
    volatile int planeOnGroundCount = 0;
    private volatile Map<String, Integer> planeToGateMap = new HashMap<>(); // use hashmaps to store and retrieve gates that were assigned to the planes
    private volatile int[] gates = {0, 0, 0}; // 0 for free, 1 for occupied
    volatile private BlockingQueue<String> LandingQueue = new LinkedBlockingQueue<>();
    volatile private BlockingQueue<String> TakeOffQueue = new LinkedBlockingQueue<>();
    
    void useLandingRunway(String PlaneName) throws InterruptedException {
        synchronized (this) {
//            if(PlaneName.equals("EMERGENCY PLANE"))
//            {
//                List<String> buffer = new ArrayList<>();
//
//                // Drain the queue into the buffer
//                LandingQueue.drainTo(buffer);
//                // Add the urgent element to the now-empty queue
//                LandingQueue.add(PlaneName);
//                // Reinsert all the original elements
//                LandingQueue.addAll(buffer);
//                
//                System.out.println("----- [ATC]: YOU HAVE BEEN ADDED TO THE TOP OF THE QUEUE, PLANE: [" + PlaneName + "]");
//            }
//            else
//            {
//                LandingQueue.add(PlaneName); // Add Plane to Landing Queue
//            }
            LandingQueue.add(PlaneName); // Add Plane to Landing Queue
        }
        
        long startTime = System.currentTimeMillis(); // Start waiting timing
        
        synchronized (this) {
            while (planeOnGroundCount == 3 || !LandingQueue.peek().equals(PlaneName)){
                if(planeOnGroundCount == 3)
                {
                    System.out.println("----- [ATC]: Ground is FULL, [" + PlaneName + "] Please Wait For Instructions...");
                    wait();
                }
                else
                {
                    System.out.println("----- [ATC]: First Come First Serve, [" + PlaneName + "], Wait Your Turn For Landing....");
                    wait();
                }
            }
            ++planeOnGroundCount; // Add Plane to the ground count
        }
        
        synchronized (this) {
            while (!isRunwayFree || !LandingQueue.peek().equals(PlaneName)) {
                System.out.println("----- [ATC]: Runway is NOT FREE, [" + PlaneName + "] Please Hold Landing...");
                wait();
            }
            isRunwayFree = false; // runway being used
        }
        
        long endTime = System.currentTimeMillis(); // End timing
        long waitTimeMillis = endTime - startTime; // Calculate how long the plane waited in milliseconds
        double waitTimeSeconds = waitTimeMillis / 1000.0; // Convert milliseconds to seconds
        STATS.addPlaneTiming(PlaneName, waitTimeSeconds); // add plane and its waiting time to a hashmap is stats class
        
        try {
            System.out.println("----- [ATC]: Runway is FREE for [" + PlaneName + "], begin landing....");
            System.out.println("[" + PlaneName + "]: Landing...");
            System.out.println("[" + PlaneName + "]: Using Runway...");
            Thread.sleep(500); // Simulate the landing time
            int assignedGate = 0;
            for (int i = 0; i < gates.length; i++) {
                if (gates[i] == 0) { // Gate is free
                    gates[i] = 1; // Mark gate as occupied
                    assignedGate = i + 1; // Gate number
                    planeToGateMap.put(PlaneName, i); // Assign Gate index to the plane
                    break;
                }
            }
            System.out.println("--------------- [ATC]: [" + PlaneName + "] Landed and Docked at Gate " + assignedGate + " ---------------");
            System.out.println("--------------- [ATC]: Runway is Free For All Planes...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        synchronized (this)
        {
            LandingQueue.take(); // Remove the head of the landing queue (The first plane to request landing)
            isRunwayFree = true; // Runway is free again
            notifyAll(); // Notify other planes waiting for the runway
        }
    }
    
    void useTakeOffRunway(String PlaneName) throws InterruptedException{
        TakeOffQueue.add(PlaneName);
        synchronized (this) {
            while (!isRunwayFree || !TakeOffQueue.peek().equals(PlaneName)) {
                
                if(!isRunwayFree)
                {
                    System.out.println("----- [ATC]: Runway IS NOT FREE, [" + PlaneName + "] Please Hold Take Off...");
                    wait();
                }
                else
                {
                    System.out.println("----- [ATC]: First Come First Serve, [" + PlaneName + "], Wait Your Turn For Take Off....");
                    wait();
                }
            }
            isRunwayFree = false; // runway is being used
        }
        
        
        try { 
                System.out.println("----- [ATC]: Runway is FREE for [" + PlaneName + "] TAKE OFF,  begin take off....");
                System.out.println("[" + PlaneName + "]: Using Runway For Take Off...");
                Thread.sleep(500); // wait simulate using runway
                int gateIndex = planeToGateMap.get(PlaneName);
                gates[gateIndex] = 0; // Mark gate as free, converting back to 0-index
                System.out.println("[" + PlaneName + "]: Take Off Successful.\n----- [ATC]: Gate " + (gateIndex + 1) + " is NOW FREE.");
                System.out.println("----- [ATC]: Runway is Free For All Planes...");
                TakeOffQueue.take();  // Remove the head of the landing queue (The first plane to request landing)
                planeToGateMap.remove(PlaneName); // Remove plane from assigned gate
                --planeOnGroundCount;
            } catch (InterruptedException e) {
            e.printStackTrace();
        } 
        
        synchronized (this){
            isRunwayFree = true; // Runway is free again
            notifyAll(); // Notify other planes waiting for the runway
        }
    }
    
    public int[] getGatesStatus() {
        // Return a copy of the gates array
        return gates.clone();
        
    }
    
    public int getGateNum(String Name)
    {
        int gateIndex = planeToGateMap.get(Name);
        return gateIndex;
    }
}
