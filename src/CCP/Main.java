package CCP;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        RefuelingTruck t;
        AirTrafficControl r;    
        Statistics s;    
        List<PlaneThread> planeList = new ArrayList<>();
        
        
        System.out.println("------------------------------------------");
        System.out.println("   [Welcome To Asia Pacific Airport]");
        System.out.println("------------------------------------------");
        
        
        t = new RefuelingTruck();
        s = new Statistics();
        r = new AirTrafficControl(s);
        
        
        Random random = new Random();
        for (int i = 1; i <= 6; i++) {
            PlaneThread p = new PlaneThread("Plane " + i, t, r, s);
            planeList.add(p);
            p.start();
            try {
                // Plane can arraive every 0, 1 , or 2
                Thread.sleep(random.nextInt(3) * 1000);
            } catch (InterruptedException ex) {}
        }
        
//        PlaneThread emergency = new PlaneThread("EMERGENCY PLANE", t, r, s);
//        emergency.emergencyPlane("EMERGENCY PLANE");
//          UN COMMENT WHEN YOU WANT TO RUN EMERGEBCY PLANE SCENARIO
        
        // join all threads so after they are all done, we print the statistics, if we dont join them, the stats will get printed after the last thread starts.
        for(PlaneThread p : planeList)
        {
            p.join();
        }
        
        s.displayStats();
        
    }
    
}
