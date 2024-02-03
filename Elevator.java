/**
 * Elevator class handles the operations of an elevator as it receives commands from a scheduler.
 * It runs within its own thread, processing incoming requests and sending responses.
 *
 * @author Al-ameen Alliu
 * 
 */
public class Elevator implements Runnable {

    private Box sharedBox; // The shared Box for communication
    private Box buffer; // Temporary buffer to store messages

    /**
     * Constructor for Elevator class.
     *
     * @param box The shared Box instance for communication between Scheduler and Elevator.
     */
    public Elevator(Box box) {
        this.sharedBox = box;
    }

    /**
     * The main run method that is executed when the elevator thread starts.
     * It continuously processes requests from the scheduler and sends responses.
     */
    public void run() {
        while(true) {
            receiveFromScheduler();
            // include process block if needed in future iterations.
            sendToSchedulerResponse();
        }
    }

    /**
     * Receives messages from the scheduler. It waits for a message,
     * processes it, and updates the buffer with the result.
     */
    private void receiveFromScheduler() {
        this.buffer = sharedBox.sendToDestination();
        // Additional processing can be done here
    }

    /**
     * Sends a response back to the scheduler. This method retrieves the processed data
     * from the buffer and sends it back to the scheduler.
     */
    private void sendToSchedulerResponse() {
        sharedBox.getFromDestination(this.buffer);
    }
}
