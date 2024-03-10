import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

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

    private int stopFloor; /* Buffer variable to represent the destination floor of any given path of the state machine */
    private State state; /* Initial state is stop */

    private boolean elevatorHasPassenger; /* Boolean to represent whether the elevator is moving with or without passenger */

    private Set<Integer> completedRequests = new HashSet<>();
    private Message request;

    private DatagramSocket sendReceiveSocket;
    private int schedulerPort = 50000;
    private int listeningPort = 60000;
    private InetAddress schedulerAddress;
    private static final int BUFFER_SIZE = 1024;

    public Elevator(String schedulerIP, int schedulerPort) {
        this.schedulerPort = schedulerPort;
        this.state = State.STOP; // Initialize the state enum to STOP
        try {
            this.schedulerAddress = InetAddress.getByName(schedulerIP);
            this.sendReceiveSocket = new DatagramSocket(listeningPort);
            System.out.println("ElevatorSubsystem listening on port: " + listeningPort);
        } catch (Exception e) {
            System.err.println("Error initializing DatagramSocket: " + e.getMessage());
            System.exit(1);
        }
    }

    public void run() {
        while (true) {
            receiveFromScheduler();

            if (!completedRequests.contains(request.getDestination())) {
                this.floorRequested = this.request.getSource();
                this.destinationFloor = this.request.getDestination();

                System.out.println("The Elevator is starting at floor: " + this.currentFloor);

                while (!processSchedulerRequest()) {
                    if (this.currentFloor == this.floorRequested) {
                        this.stopFloor = this.destinationFloor;
                    } else {
                        this.stopFloor = this.floorRequested;
                    }
                }

                completedRequests.add(request.getDestination());
                this.request.setConfirmation(true);
                sendToSchedulerResponse();
            } else {
                System.out.println("Skipping request as it's already completed: " + request.getDestination());
            }
        }
    }


    public boolean processSchedulerRequest() {

        switch (state) {
            /* Will start at Stop and will transisient depending if its on the right floor or not */
            case STOP:

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
            case DOOR_OPENING:
                System.out.println("State: Door opening");
                this.state = State.DOOR_OPEN;
                break;

            /* This state is where the elevator has its door opens. Then checks if passenger entered */
            case DOOR_OPEN:
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
            case DOOR_CLOSING:
                System.out.println("State: Door closing");
                state = State.DOOR_CLOSED;
                break;

            /* This state checks if an elevator request is in progress or not.*/
            case DOOR_CLOSED:
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
            case ACCELERATING:
                System.out.println("State: Accelerating");
                state = State.CRUISING;
                break;

            /* This state always transitions to the Decelerating state*/
            case CRUISING:
                System.out.println("State: Cruising");
                state = State.DECELERATING;
                break;

            /* This state always transitions to the Decelerating state*/
            case DECELERATING:
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

    private void receiveFromScheduler() {
        this.request = receiveUDPMessage();
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
            message.printMessage();
            return message;
        } catch (Exception e) {
            System.err.println("ElevatorSubsystem failed to receive UDP message: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void sendToSchedulerResponse() {
        sendUDPMessage(this.request);
    }

    private void sendUDPMessage(Message message) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(message);
            out.flush();
            byte[] serializedData = bos.toByteArray();
            DatagramPacket packet = new DatagramPacket(serializedData, serializedData.length, schedulerAddress, schedulerPort);
            sendReceiveSocket.send(packet);
        } catch (IOException e) {
            System.err.println("Error sending command to scheduler: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void closeSocket() {
        if (sendReceiveSocket != null && !sendReceiveSocket.isClosed()) {
            sendReceiveSocket.close();
            System.out.println("Socket closed.");
        }
    }

    public int getCurrentFloor() {
        return this.currentFloor;
    }

    public String getCurrentDirection() {
        return this.request.getDirection();
    }

    public static void main(String[] args) {
        String schedulerIP = "localhost";
        int schedulerPort = 50000;

        Elevator elevatorSubsystem = new Elevator(schedulerIP, schedulerPort);
        new Thread(elevatorSubsystem).start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            elevatorSubsystem.closeSocket();
            System.out.println("ElevatorSubsystem shutdown hook executed.");
        }));
    }
}
