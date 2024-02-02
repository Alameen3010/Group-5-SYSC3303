/**
 * The main class which will start everything for the Elevator general system.
 * @version January 30 2023
 *
 */
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {


    public static void main(String[] args) {
        // intializes all threads. So One producer which is Agent and three consumers.
        // The three chefs named by their ingredient
        Box boxFtoS = new Box();
        //Box boxStoE = new Box();


        Thread floor = new Thread(new Floor(boxFtoS), "Floor");
        //Thread elevator = new Thread(new Elevator(boxStoE), "Elevator");
        Thread scheduler = new Thread(new Scheduler(boxFtoS), "Scheduler");

        // starts all  threads and finishes. The program will terminate when all non-deamon threads are termianted.
        floor.start();
        //boxFtoS.printContents();
        scheduler.start();
        //elevator.start();
    }

}