//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Box boxFtoS = new Box();

        Thread scheduler = new Thread(new Scheduler(boxFtoS), "Scheduler");
        //Thread scheduler = new Scheduler();
        Thread floor = new Thread(new Floor(boxFtoS), "Floor");
        //Thread elevator = new Thread(new Floor(scheduler), "Elevator");

        floor.start();
        scheduler.start();
        //elevator.start();
    }
}