package CCP;
// TP069502 ALI AHMED ABOUELSEOUD MOUSTAFA TAHA
public class RefuelingTruck {
    volatile private boolean isTruckFree = true;
    void useTruck(String PlaneName) throws InterruptedException{
        synchronized (this) {
            while (!isTruckFree) {
                System.out.println("[Refueling Truck]: [" + PlaneName + "] Please Wait, Im Being Used....");
                wait();
            }
            isTruckFree = false; // truck is being used
        }
        try {
            System.out.println("--------------- [" + PlaneName + "]: Using Fuel Truck ---------------");
            System.out.println("[" + PlaneName + "]: refueling... \n-----------------------------------------------------------");
            Thread.sleep(500); // simulate refueling
            System.out.println("--------------- [" + PlaneName + "]: Finished Refueling ---------------");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        synchronized (this)
        {
            isTruckFree = true; // truck is free to use now
            notify(); // Notify waiting threads after refueling
        }

    }
}
