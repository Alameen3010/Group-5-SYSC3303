/**
 * The Scheduler class is a thread responsible for acting as a communication channel between the floor and the scheduler
 * It has two boxes shared with all other subsystems.
 *
 *
 * @author Ilyes Outaleb (101185290)
 * @version February 3, 2023
 */

public class Scheduler implements Runnable{

    private Box sharedBoxFloor;
    private Box sharedBoxElevator;

    /*Buffer is an extra box to save the content in case some information needs to be processed by the elevator for */
    // future iterations of the project. The same thing was done for the Elevator .
    private Box buffer;

    /**
     *  Constructor for the scheduler class which required two shared boxes.
     * @param box Is the box shared with floor
     * @param box2 Is the second shared box with elevator
     */
    public Scheduler(Box box, Box box2)
    {
        this.sharedBoxFloor = box;
        this.sharedBoxElevator = box2;
    }

    public void run() {
        while(true){
            getFromFloor();
            /* Processing can be done here before sending to elevator if needed for future iterations */
            sendToElevator();
            getFromElevator();
            /* Processing can be done here before sending to elevator if needed for future iterations */
            sendToFloor();
        }
    }

    /**
     *  Once the floor sends a request the scheduler retrieves from the box and assigns to the buffer.
     *
     */
    void getFromFloor()
    {
        this.buffer = sharedBoxFloor.sendToDestination();
    }

    /**
     * This method sends the message from floor to elevator by assigning the shared box with elevator to the content of
     * buffer.
     *
     */
    void sendToElevator()
    {
        sharedBoxElevator.getFromSource(this.buffer);
    }

    /**
     *
     * This method gets the message from elevator through the sharedbox and assigns it to the buffer.
     */
    void getFromElevator()
    {
        this.buffer = sharedBoxElevator.sendToSource();
    }

    /**
     *
     * This method gets sends the final response message of the system from elevator.
     */
    void sendToFloor() {
        sharedBoxFloor.getFromDestination(this.buffer);
    }
    
    // *The following methods were added for testing purposes*
    /**
     * getter for buffer
     * @return Box, the box object
     */
    public Box getBuffer() {
        return buffer;
    }

    /**
     * setter for the box object
     * @param buffer, Box object
        */
    public void setBuffer(Box buffer) {
        this.buffer = buffer;
    }
}
