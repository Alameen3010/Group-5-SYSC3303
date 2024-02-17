import java.util.Scanner;

public class Elevator {

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
    private int currentFloor;
    private State state; // Initial state is stop
    private boolean elevatorButtonPressed;
    public Elevator()
    {
        this.state = State.STOP;
    }

    public void processSchedulerRequest() {
        switch (this.state) {
            case State.STOP:
                System.out.println("State: Stop");
                System.out.println("Current Floor:" + this.currentFloor);
                if (currentFloor == floorRequested)
                {
                    this.state = State.DOOR_OPENING;
                }
                else
                {
                    this.state = State.ACCELERATING;
                }
                break;
            case State.DOOR_OPENING:
                System.out.println("State: Door opening");
                this.state = State.DOOR_OPEN;
                break;

            case State.DOOR_OPEN:
                System.out.println("State: Door open");
                state = State.DOOR_CLOSING;
                this.elevatorButtonPressed = processResponse();
                if (elevatorButtonPressed)
                {
                    System.out.println("Which button floor was pressed?");
                    floorRequested = Integer.parseInt(myObj.nextLine());
                }
                this.state = State.DOOR_CLOSING;
                break;
            case State.DOOR_CLOSING:
                System.out.println("State: Door closing");
                state = State.DOOR_CLOSED;
                break;
            case State.DOOR_CLOSED:
                System.out.println("Door closed");
                if (elevatorButtonPressed)
                {   /* Notify Scheduler that the elevator is being requested by a passenger. Add current floor and destination floor */
                    if(floorRequested == currentFloor)
                    {
                        this.state = State.DOOR_OPENING;
                    }
                    else
                    {
                        this.state = State.ACCELERATING;
                    }
                }
                else
                {
                    /* Notify Scheduler that the elevator is available for next Floor request. For now resting IDLE. */
                    System.exit(0);

                }
                break;
            case State.ACCELERATING:
                System.out.println("State: Accelerating");
                state = State.CRUISING;
                break;
            case State.CRUISING:
                System.out.println("State: Cruising");
                state = State.DECELERATING;
                break;
            case State.DECELERATING:
                System.out.println("State: Decelerating");
                state = State.STOP;
                this.currentFloor = this.floorRequested;
                break;
            default:
                System.out.println("Unknown state");
                state = State.STOP;
                break;
        }
    }
    public boolean processResponse()
    {   Scanner myObj = new Scanner(System.in);
        String userInput;
        System.out.println("Would you like to simulate a pressed button inside the elevator? (Options:'Yes'/'No')");
        userInput = myObj.nextLine();
        return userInput.equals("Yes");
    }

    public static void main(String[] args) {
        Elevator elevator = new Elevator();
        elevator.currentFloor = 0;

        Scanner myObj = new Scanner(System.in);
        System.out.println("What will be the floor number to simulate the scheduler request?");
        elevator.floorRequested = Integer.parseInt(myObj.nextLine());

        // Simulate the elevator moving through its states
        while (true)
        {
            elevator.processSchedulerRequest();
        }
    }
}
