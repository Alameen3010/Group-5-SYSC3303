/**
 * The main class which will start all the subsystems.
 * Responsible for initializing and starting the three threads and the shared boxes.
 * @author Ilyes Outaleb (101185290)
 * @version February 03, 2024
 *
 */

public class Main {
    /**
     * The main method which will be used to start the system
     * 
     * @param args arguments that be added
     */
    public static void main(String[] args) {
        /* The boxes will be shared between floor and scheduler (FtoS) and scheduler and elevator (StoE) */
        Box boxFtoS = new Box();   
        Box boxStoE = new Box(); 
        
        /* The scheduler will have both boxes and the other two subsystem will have one each. Allowing the transfer*/
        // of data.
        Thread scheduler = new Thread(new Scheduler(boxFtoS, boxStoE), "Scheduler");
        Thread floor = new Thread(new Floor(boxFtoS), "Floor");
        Thread elevator = new Thread(new Elevator(boxStoE), "Elevator");
        
        /* Will execute the run() method from runnable and is overwritten in each respective class*/ 
        floor.start();
        scheduler.start();
        elevator.start();
    }
}
