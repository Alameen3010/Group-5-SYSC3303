//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
/**
 * The main class which will start all the subsystems.
 * Responsible for initializing and starting threads.
 * @author Ilyes Outaleb (101185290)
 * @version February 03 2024
 *
 */



public class Main {
    public static void main(String[] args) {
        Box boxFtoS = new Box(); // initialize to start values
        Box boxStoE = new Box(); // initialize to start values

        Thread scheduler = new Thread(new Scheduler(boxFtoS, boxStoE), "Scheduler");
        //Thread scheduler = new Scheduler();
        Thread floor = new Thread(new Floor(boxFtoS), "Floor");
        Thread elevator = new Thread(new Elevator(boxStoE), "Elevator");

        floor.start();
        scheduler.start();
        elevator.start();
    }
}
