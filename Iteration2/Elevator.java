import java.util.Scanner;

/**
 * The Elevator class which will implement all the actions required to move passengers from floor to floor.
 * The Elevator state machine starts with a scheduler request and finishes when all elevator Requests (done by passengers
 * inside the elevator) are processed. Then it stays IDLE and waits for another scheduler request.
 * @author Alameen Alliu (101159780)
 * @version 2024-02-17
 */

public class Elevator {
    /* Variable representing all the possible states of the Elevator. */
    private enum State {
        STOP,
        DOOR_OPENING,
        DOOR_OPEN,
        DOOR_CLOSING,
        DOOR_CLOSED,
        ACCELERATING,
        CRUISING,
        DECELERATING,
    }

    private int floorRequested; /* Assuming the floor requested is retrieved from scheduler */
    private int currentFloor; /* Variable representing on which the elevator is currently on. */
    private State state; // Initial state is stop
    private boolean elevatorButtonPressed; /*Variable representing if the button has been pressed inside the elevator*/

    /**
     * The constructor for the elevator class which sets the entry condition to STGP.
     */
    public Elevator()
    {
        this.state = State.STOP;
    }

    /**
     * Main class responsible for simulating the state machine for the elevator subsystem. This will transition from
     * the different states of the machine under the right conditions/events.
     */
    public void processSchedulerRequest() {
        Scanner myObj = new Scanner(System.in);

        /* Will enter different case blocks according to the state attribute */
        switch (this.state) {
            /* Will start at Stop and will transisient depending if its on the right floor or not */
            case State.STOP:
                System.out.println("State: Stop");
                System.out.println("Current Floor:" + this.currentFloor);
                if (currentFloor == floorRequested)
                {
                    this.state = State.DOOR_OPENING; /* If its on right floor just open door */
                }
                else
                {
                    this.state = State.ACCELERATING; /* If its on wrong floor move to the appropriate one */
                }
                break;

            /* This state always transitions to the Doors being opened */
            case State.DOOR_OPENING:
                System.out.println("State: Door opening");
                this.state = State.DOOR_OPEN;
                break;

            /* This state is where the elevator has its door opens. Then checks if passenger entered */
            case State.DOOR_OPEN:
                System.out.println("State: Door open");
                this.elevatorButtonPressed = processResponse(); /* Simulate passenger entering and pressing elevator button */
                if (elevatorButtonPressed) /* For the purpose of the simulation the timer condition is not implemented */
                {
                    System.out.println("Which button floor was pressed?"); /* Enters which floor it would like to move to */
                    floorRequested = Integer.parseInt(myObj.nextLine());
                }
                this.state = State.DOOR_CLOSING;
                break;

            /* This state always transitions to the Doors being Closed */
            case State.DOOR_CLOSING:
                System.out.println("State: Door closing");
                state = State.DOOR_CLOSED;
                break;

            /* This state checks if an elevator request is in progress or not.*/
            case State.DOOR_CLOSED:
                System.out.println("Door closed");
                if (elevatorButtonPressed) /* If there is an elevator request than it proceeds to move. */
                {   /* Notify Scheduler that the elevator is being requested by a passenger. Add current floor and destination floor */
                    if(floorRequested == currentFloor) /* If the elevator is already there do not move*/
                    {
                        this.state = State.DOOR_OPENING;
                    }
                    else /* else move */
                    {
                        this.state = State.ACCELERATING;
                    }
                }
                else /* Else gracefully exits and remains IDLE waiting for next Scheduler call. */
                {
                    /* Notify Scheduler that the elevator is available for next Floor request. For now resting IDLE. */
                    System.exit(0);

                }
                break;

            /* This state always transitions to the Cruising Constant Speed*/
            case State.ACCELERATING:
                System.out.println("State: Accelerating");
                state = State.CRUISING;
                break;

            /* This state always transitions to the Decelerating state*/
            case State.CRUISING:
                System.out.println("State: Cruising");
                state = State.DECELERATING;
                break;

            /* This state always transitions to the Decelerating state*/
            case State.DECELERATING:
                System.out.println("State: Decelerating");
                state = State.STOP;
                this.currentFloor = this.floorRequested;
                break;

            /* Default state in case something goes wrong always transition back to the initial state of STOP */
            default:
                System.out.println("Unknown state");
                state = State.STOP;
                break;
        }
    }

    /**
     *  FUnction responsible for taking the user input.
     *
     * @return Boolean representing the user wants to simulate an elevator request or not.
     */
    public boolean processResponse()
    {   Scanner myObj = new Scanner(System.in);
        String userInput;
        System.out.println("Would you like to simulate a pressed button inside the elevator? (Options:'Yes'/'No')");
        userInput = myObj.nextLine();
        return userInput.equals("Yes");
    }

    /**
     * Main function responsible for creating the object and running the state transition method.
     *
     * @param args Optional arguments
     */
    public static void main(String[] args) {
        Elevator elevator = new Elevator();
        /* Assume Elevator starts on the button floor of the facility */
        elevator.currentFloor = 0; /* possibility of doing the same as below for where the elevator starts */

        Scanner myObj = new Scanner(System.in); /* Scanner object to get input from the user to simulate scheduler request */
        System.out.println("What will be the floor number to simulate the scheduler request?");
        elevator.floorRequested = Integer.parseInt(myObj.nextLine());

        // Simulate the elevator moving through its states
        while (true) /* Continuously loop until all the elevator request are done and the one scheduler request. */
        {
            elevator.processSchedulerRequest();
        }
    }
}

