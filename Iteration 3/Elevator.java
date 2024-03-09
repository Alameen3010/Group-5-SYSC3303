import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.*;
import java.net.*;

/**
 * Elevator class handles the operations of an elevator as it receives commands from a scheduler.
 * It runs within its own thread, processing incoming requests and sending responses.
 *
 * @author Al-ameen Alliu
 * @version February 03, 2024,
 * Edited: Ilyes Outaleb (101185290)
 * @version March 02, 2024,
 * Integrated it with Iteration 1
 */
public class Elevator implements Runnable {

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

    private int destinationFloor; /* The floor to which the passenger has asked to go */
    private int currentFloor; /* Variable representing on which the elevator is currently on. */

    private int stopFloor; /* Buffer varaible to represent the destination floor of any given path of the state machine */
    private State state; /* Initial state is stop */

    private boolean elevatorHasPassenger; /* Boolean to represent whether the elevator is moving with or without passenger */

    private Message buffer;


    private DatagramSocket sendReceiveSocket;

    private int schedulerPort= 5000;
    private int listeningPort = 6000;
    private InetAddress schedulerAddress;
    private static final int BUFFER_SIZE = 1024;

    /**
     * Constructor for Elevator class.
     *
     */
    public Elevator(String schedulerIP, int schedulerPort) {

        this.state = State.STOP;
        this.currentFloor = 0;
        this.elevatorHasPassenger = false;


        this.schedulerPort = schedulerPort;
        try {
            this.schedulerAddress = InetAddress.getByName(schedulerIP);
            this.sendReceiveSocket = new DatagramSocket(listeningPort);
            System.out.println("ElevatorSubsystem listening on port: " + listeningPort);
        } catch (Exception e) {
            System.err.println("Error initializing DatagramSocket: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * The main run method that is executed when the elevator thread starts.
     * It continuously processes requests from the scheduler and sends responses.
     */
    public void run() {
        receiveFromScheduler();



        this.floorRequested = this.buffer.getSource(); /* Retrieves the data from the box to be used */
        this.destinationFloor = this.buffer.getDestination();

        System.out.println("The Elevator is starting at floor: " + this.currentFloor);

        /* Continues while the state machine has not finished marked by the arrival of the passenger at destination */
        while(!processSchedulerRequest())
        {
            /* If the elevator is where the passenger is pressing the floor button */
            if (this.currentFloor == this.floorRequested)
            {
                /* Then elevator must move to where the passenger wants to debark */
                this.stopFloor = this.destinationFloor;

            }
            else /* if that is not the case */
            {   /* then it must move to where the passenger is located first */
                this.stopFloor = this.floorRequested;
            }
        };// only for one elevator change it to multiple by looking at iteration 2 original.

        sendToSchedulerResponse();

        sendReceiveSocket.close();


    }


    public boolean processSchedulerRequest() {

        switch (this.state) {
            /* Will start at Stop and will transisient depending if its on the right floor or not */
            case State.STOP:

                System.out.println("State: Stop");
                System.out.println("Current Floor:" + this.currentFloor);

                /* If elevator is in requested or destination floor it must open to let passenger in or out */
                if (currentFloor == floorRequested || currentFloor == destinationFloor)
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

                if (this.elevatorHasPassenger)             /* If the elevator has a passenger it must be debarking it */
                {
                    System.out.println("Passenger has left elevator at floor " + this.destinationFloor
                            + " and Elevator now ready for new scheduler request");
                    this.elevatorHasPassenger = false;              /* Now the elevator has no longer a passenger */
                }
                else                                                        /* If not  it must be embarking it */
                {
                    System.out.println("Passenger has entered at floor " + this.floorRequested
                        + " and will be moving to floor: " + this.destinationFloor);
                    this.elevatorHasPassenger = true;                   /* Now the elevator has a passenger */
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
                if (this.currentFloor != this.destinationFloor) /* If there is an elevator request than it proceeds to move. */
                {   /* Notify Scheduler that the elevator is being requested by a passenger. Add current floor and destination floor */
                    this.state = State.ACCELERATING;

                }
                else /* Else gracefully exits and remains IDLE waiting for next Scheduler call. */
                {
                    /* Notify Scheduler that the elevator is available for next Floor request. For now resting IDLE. */
                    this.state = State.STOP;       /* Make sure that when re-enters state machine it must be at STOP */
                    return true;                    /* Exited the state machine */

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
                this.currentFloor = this.stopFloor;
                break;

            /* Default state in case something goes wrong always transition back to the initial state of STOP */
            default:
                System.out.println("Unknown state");
                state = State.STOP;
                break;

        }
        return false;   /* Has not finished the state machine yet. Need to repeat */
    }
    /**
     * Receives messages from the scheduler. It waits for a message,
     * processes it, and updates the buffer with the result.
     */
    private void receiveFromScheduler() {
        this.buffer = receiveUDPMessage(); // Blocking call, waits for a message

    }

    private Message receiveUDPMessage() {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            System.out.println("ElevatorSubsystem ready to receive UDP message on port: " + listeningPort);

            sendReceiveSocket.receive(packet);

            ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
            ObjectInputStream in = new ObjectInputStream(bis);
            Message message = (Message) in.readObject();

            System.out.println("Received object:");
            System.out.println("Date " + message.getDate());
            System.out.println("Source " + message.getSource());
            System.out.println("Direction " + message.getDirection());
            System.out.println("Destination " + message.getDestination());


            return message;
        } catch (Exception e) {
            System.err.println("ElevatorSubsystem failed to receive UDP message: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sends a response back to the scheduler. This method retrieves the processed data
     * from the buffer and sends it back to the scheduler.
     */
    private void sendToSchedulerResponse() {
        sendUDPMessage("Got it. Thanks");
    }

    public void sendUDPMessage(String message) {
        try {
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, schedulerAddress, schedulerPort);
            sendReceiveSocket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String schedulerIP = "localhost";
        int schedulerPort = 5000;

        Elevator elevatorSubsystem = new Elevator(schedulerIP, schedulerPort);

        // Start the ElevatorSubsystem in a new thread
        new Thread(elevatorSubsystem).start();
    }
    
    // *The following methods were added for testing purposes*


}