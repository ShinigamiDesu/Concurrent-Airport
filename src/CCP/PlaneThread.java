package CCP;
// TP069502 ALI AHMED ABOUELSEOUD MOUSTAFA TAHA
public class PlaneThread extends Thread{
    
    RefuelingTruck FuelTruck;
    AirTrafficControl ATC;
    Statistics Stats;
    String PlaneName;
    PlaneThread(String name, RefuelingTruck t, AirTrafficControl r, Statistics s)
    { 
        FuelTruck = t; 
        ATC = r;
        Stats = s;
        this.PlaneName = name;
    }

    public void run() {
      try {
          System.out.println("[" + PlaneName + "]: Requesting for Landing");
          ATC.useLandingRunway(PlaneName);
          disembarkPassengers(PlaneName);
          FuelTruck.useTruck(PlaneName);
          Maintenance(PlaneName);
          BoardPassengers(PlaneName);
          ATC.useTakeOffRunway(PlaneName);
      } catch (InterruptedException ex) {System.out.println("SOMETHING WENT WRONG........");}
    }
    
    public void disembarkPassengers(String PlaneName)
    {
        try{
                for (int i=1; i<=50; i++){
                    Thread.sleep(100);
                    System.out.println("        [" + PlaneName + "]: Disembarking Passenger: " + i + " TO [GATE " + (ATC.getGateNum(PlaneName) + 1) + "]");
                    Stats.disembarkedPassengers(); // increment disembarked passengers count
                }
                System.out.println("--------------- [" + PlaneName + "]: Finished Disembarking All Passengers ----------------");
        } catch (InterruptedException e) {System.out.println("SOMETHING WENT WRONG........");}
    }
    
    public void BoardPassengers(String PlaneName)
    {
        try{
                for (int i=1; i<=50; i++){
                    Thread.sleep(50);
                    System.out.println("        [" + PlaneName + "]: Boarding New Passenger: " + i + " FROM [GATE " + (ATC.getGateNum(PlaneName) + 1) + "]");
                    Stats.boardedPassengers(); // increment boarded passengers count
                    
                }
                System.out.println("--------------- [" + PlaneName + "]: Finished Boarding All Passengers ----------------\n"
                        + "[" + PlaneName + "] Ready For Departure!");
            Stats.planesServed();
        } 
        catch (InterruptedException e) {System.out.println("SOMETHING WENT WRONG........");}
    }
    
    public void Maintenance(String PlaneName)
    {
        try{
            System.out.println("[" + PlaneName + "]: Begin Maintainance, Cleaning, and Resupplying...");
            Thread.sleep(500);
            System.out.println("[" + PlaneName + "]: Finished Maintainance, Cleaning, and Resupplying...");
        } 
        
        catch (InterruptedException ex) {System.out.println("SOMETHING WENT WRONG........");}
    }
    
//    public void emergencyPlane(String PlaneName) throws InterruptedException
//    {
//        System.out.println("["+ PlaneName +"]: EMERGENCY !!!! NEED TO REFUEL !!! REQUESTING LANDING ASAP");
//        ATC.useLandingRunway(PlaneName);
//        FuelTruck.useTruck(PlaneName);
//        Maintenance(PlaneName);
//        System.out.println("["+ PlaneName +"] Ready For Departure!");
//        ATC.useTakeOffRunway(PlaneName);
//    }
    // UNCOMMENT WHEN YOU WANT TO RUN EMERGENCY PLANE SCENARIO
}
