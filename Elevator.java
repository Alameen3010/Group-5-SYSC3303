/**
 * @author Al-ameen Alliu, 101159780
 *The thread for the elevator class where data is collected from the scheduler,
 *processed and sent back o the scheduler
 */
public class Elevator implements Runnable {

    private Box sharedBox;

    private Box buffer;

    public Elevator(Box box) {
        this.sharedBox = box;
    }

    public void run() {
        //for (int i = 0; i < 2; i++) {
        while(true)
        {   receiveFromScheduler();
            // include process block if needed in future iterations.
            sendToSchedulerResponse();

        }
        //}
    }

    private void receiveFromScheduler() {
        /**
         * 
         */
        // Receives the message from the scheduler to take action,
        //String message = sharedBox.sendToDestination();
        this.buffer = sharedBox.sendToDestination();
        //System.out.println("Elevator received: " + message);
    }

    private void sendToSchedulerResponse() {
        // Sends the response back to the scheduler, could be the new floor number or a status
        //String response = "Elevator processed: " + FloorNumber;

        sharedBox.getFromDestination(this.buffer);
    }
}