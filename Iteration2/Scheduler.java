import java.util.Scanner;

/**
 * The Scheduler class which will schedule and send the requests from the floor subsystem to the elevator subsystem.
 * The scheduler goes from getting the request to scheduling the requests with many elevator.
 * @author Ilyes Outaleb (101185290)
 * @version 2024-02-17
 *
 */


public class Scheduler {
    /* Variable representing all the possible states of the scheduler. */
    private enum State {
        RECEIVING_REQUEST,
        SCHEDULING,
    }

    /* Variable representing the state of the scheduler. */
    private State state;

    /* Variable simulating whether the scheduler received a request from the floor system
    *  This is used to exit from the state machine once no more requests are needed from floor subsystem.
    */
    private boolean hasRequestFromFloor;

    /**
     * The constructor for the scheduler class which sets the entry condition to RECEIVING REQUEST.
     */
    public Scheduler()
    {
        this.state = State.RECEIVING_REQUEST;
    }

    /**
     * Main class responsible for simulating the state machine for the scheduler subsystem. This will transistion from
     * the different states of the machine under the right conditions/events.
     */
    public void processFloorRequest()
    {
        /* Will enter different case blocks according to the state attribute */
        switch(this.state)
        {
            case State.RECEIVING_REQUEST: /* If the machine is in the entry state it checks if there is a request. Simulated by user input. */
                System.out.println("State: Receiving_Request");
                if (processResponse())
                {
                    this.state = State.SCHEDULING;          /* If there is than it schedules the request to reduce waiting time */
                }
                else                                    /* if no request than it goes back to IDLE and waits for a future request from floor */
                {   System.out.println("Scheduler going IDLE as no more requests to be processed.");
                    System.exit(0);

                }


            case State.SCHEDULING: /* If the machine is in the scheduling state. This will send the request to the elevator subsystem in future iterations. */
                System.out.println("State: Scheduling");
                this.state = State.RECEIVING_REQUEST;   /* Once finished returns to the receiving state. */
        }
    }

    /**
     *  FUnction responsible for taking the user input.
     *
     * @return Boolean representing the user wants to simulate a floor request or not.
     */
    public boolean processResponse()
    {   Scanner myObj = new Scanner(System.in); /* Create a scanner object to read from the terminal */
        String userInput;
        System.out.println("Would you like to simulate a floor request? (Options:'Yes'/'No')");
        userInput = myObj.nextLine();
        return userInput.equals("Yes"); /* Anything else than "Yes" will be considered as no" */
    }

    /**
     * Main function responsible for creating the object and running the state transition method.
     *
     * @param args Optional arguments
     */
    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();

        /* Continuously loop until all floor requests are scheduled. */
        while (true)
        {
            scheduler.processFloorRequest();
        }
    }


}
