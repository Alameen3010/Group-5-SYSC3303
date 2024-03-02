/**
 * The Scheduler class is a thread responsible for acting as a communication channel between the floor and the scheduler
 * It has two boxes shared with all other subsystems.
 *
 *
 * @author Ilyes Outaleb (101185290)
 * @version February 3, 2024,
 * Edited: Ilyes Outaleb (101185290)
 * @version March 02, 2024,
 */

public class Scheduler implements Runnable{

    /* Variable representing all the possible states of the scheduler. */
    private enum State {
        RECEIVING_REQUEST,
        SCHEDULING,
    }

    /* Variable representing the state of the scheduler. */
    private State state;
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
        this.state = State.RECEIVING_REQUEST;
    }

    public void run() {
        while(true){
            getFromFloor();
            /* Processing can be done here before sending to elevator if needed for future iterations */
            while(!processFloorRequest());
            sendToElevator();
            getFromElevator();
            /* Processing can be done here before sending to elevator if needed for future iterations */
            sendToFloor();
        }
    }

    /**
     * Main class responsible for simulating the state machine for the scheduler subsystem. This will transistion from
     * the different states of the machine under the right conditions/events.
     */
    public boolean processFloorRequest()
    {
        /* Will enter different case blocks according to the state attribute */
        switch(this.state)
        {
            case State.RECEIVING_REQUEST: /* If the machine is in the entry state it checks if there is a request. Simulated by user input. */
                System.out.println("State: Receiving_Request");
                this.state = State.SCHEDULING;   /* If there is than it schedules the request to reduce waiting time */
                break;

            case State.SCHEDULING: /* If the machine is in the scheduling state. This will send the request to the elevator subsystem in future iterations. */
                System.out.println("State: Scheduling");
                this.state = State.RECEIVING_REQUEST;/* Once finished returns to the receiving state. */
                return true;

            /* Default state in case something goes wrong always transition back to the initial state */
            default:
                System.out.println("Unknown state");
                this.state = State.RECEIVING_REQUEST ;
                break;

        }
        return false;
    }


    /**
     *  Once the floor sends a request the scheduler retrieves from the box and assigns to the buffer.
     *
     */
    private void getFromFloor()
    {
        this.buffer = sharedBoxFloor.sendToDestination();
    }

    /**
     * This method sends the message from floor to elevator by assigning the shared box with elevator to the content of
     * buffer.
     *
     */
    private void sendToElevator()
    {
        sharedBoxElevator.getFromSource(this.buffer);
    }

    /**
     *
     * This method gets the message from elevator through the sharedbox and assigns it to the buffer.
     */
    private void getFromElevator()
    {
        this.buffer = sharedBoxElevator.sendToSource();
    }

    /**
     *
     * This method gets sends the final response message of the system from elevator.
     */
    private void sendToFloor()
    {
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
