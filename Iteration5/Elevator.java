import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import static java.lang.Math.abs;

/**
 * Elevator class handles the operations of an elevator as it receives commands from a scheduler.
 * It runs within its own thread, processing incoming requests and sending responses.
 *
 * @author Al-ameen Alliu
 * @version February 03, 2024,
 * Edited: Ilyes Outaleb (101185290)
 * @version March 02, 2024,
 * Integrated it with Iteration 1
 * Edited: Ilyes Outaleb (101185290)
 * @version March 10, 2024
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
    public int currentFloor; /* Variable representing on which the elevator is currently on. */

    private int stopFloor; /* Buffer variable to represent the destination floor of any given path of the state machine */
    public State state; /* Initial state is stop */

    private boolean elevatorHasPassenger; /* Boolean to represent whether the elevator is moving with or without passenger */

    private ArrayList<Message> request = new ArrayList<>();




    private DatagramSocket sendReceiveSocket;

    private int schedulerPort = 50000;
    private int listeningPort = 60000;
    private InetAddress schedulerAddress;
    private static final int BUFFER_SIZE = 1024;

    private int id;

    private boolean doorBroken = false;

    private String direction;

    private static final int size = 5;

    /**
     * Constructor for Elevator class.
     *
     */
    public Elevator(String schedulerIP, int schedulerPort, int id) {

        this.state = State.STOP;
        this.currentFloor = 0;
        this.elevatorHasPassenger = false;
        this.id = id;


        this.schedulerPort = schedulerPort;
        try {
            this.schedulerAddress = InetAddress.getByName(schedulerIP);
            this.sendReceiveSocket = new DatagramSocket(listeningPort + id);
            System.out.println("ElevatorSubsystem " + this.id + " listening on port: " + listeningPort + id);
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
        //System.out.println("HI");
        int count = 0;
        int globalCount = 0;
        //while(true) {

        //System.out.println("================ REQUESTS IN ELEVATOR " + this.id + "QUEUE ============================");
        Message temp;
        do {

            temp = receiveFromScheduler();
            if (temp.getBuffer() == false)
            {
                temp.horizontalPrint(this.id);
                this.request.add(temp);
                count ++;
                /* Message if array can still accept more passengers */
                Message responseSize = new Message("0",0,"0",0, false, 0, 0);
                if (count >= size)
                {   /* Message if the array is full */
                    responseSize = new Message("0",0,"0",0, true, 0, 0);
                }
                sendToSchedulerResponse(responseSize);
                globalCount ++;
                System.out.println("Global Count: Elevator " + id + ": " + globalCount);
                System.out.println("Sent Size: " + responseSize.getBuffer());
            }
        } while(temp.getBuffer() == false);
        //System.out.println("================ END ============================");

        //System.out.println("Count" + count);
        //for(Message currentRequest : this.request)

        for (int i = 0; i < count; i++) {
            System.out.println("=========== Executing request: ");
            if (this.request.get(i).getDoor_fault() == 1) {
                doorBroken = true;
            }

            this.request.get(i).horizontalPrint(this.id);

            this.floorRequested = this.request.get(i).getSource(); /* Retrieves the data from the box to be used */
            this.destinationFloor = this.request.get(i).getDestination();

            System.out.println("The Elevator " + this.id + " is starting at floor: " + this.currentFloor);
            boolean hasPickedUpPassenger = false;
            boolean needToMove = true;
            /* Continues while the state machine has not finished marked by the arrival of the passenger at destination */

            /* If the elevator is where the passenger is pressing the floor button */
            if (this.currentFloor == this.floorRequested) {
                this.stopFloor = this.destinationFloor;
                needToMove = false;
            }
            else
            {   needToMove = true;
                this.stopFloor = this.floorRequested;
            }
            if (this.currentFloor < this.stopFloor) {
                this.direction = "Up";
            } else {
                this.direction = "Down";
            }

            while (!processSchedulerRequest()) {
                if (needToMove)
                {   /* then it must move to where the passenger is located first */
                    if (this.currentFloor == this.stopFloor)
                    {
                        needToMove = true;
                        if (processSchedulerRequest() == true)
                        {
                            break;
                        }

                        this.stopFloor = this.destinationFloor;
                        if (this.currentFloor < this.stopFloor)
                        {
                            this.direction = "Up";
                        }
                        else
                        {
                            this.direction = "Down";
                        }
                    }
                }

            }
            System.out.println("");// only for one elevator change it to multiple by looking at iteration 2 original.

            this.request.get(i).setBuffer(true); /* The buffer in this case is used to tell the elevator that it procesed the request */
            System.out.println("Finished moving on");
            doorBroken = false;
            System.out.println(" ");
        }

        System.out.println("Finished list of requests. Now available for new.");
        sendToSchedulerResponse(this.request.get(count-1));
        this.request.get(count-1).horizontalPrint(id);
        //}
    }

    public boolean processSchedulerRequest() {

        switch (this.state) {
            /* Will start at Stop and will transisient depending if its on the right floor or not */
            //case State.STOP:
            case STOP:
                System.out.println(" State: Stop ");
                System.out.print(" Current Floor: " + this.currentFloor + " -> ");
                /* If elevator is in requested or destination floor it must open to let passenger in or out */
                if (this.currentFloor == this.stopFloor)
                {
                    this.state = State.DOOR_OPENING; /* If its on right floor just open door */
                }
                else
                {
                    this.state = State.ACCELERATING; /* If its on wrong floor move to the appropriate one */
                }
                break;

            /* This state always transitions to the Doors being opened */
            //case State.DOOR_OPENING:
            case DOOR_OPENING:
                if (doorBroken)
                {
                    System.out.print(" State: Door is opening slower than usual (transient fault) " + " -> ");
                    this.state = State.DOOR_CLOSING;
                    try{
                        Thread.sleep(3000); // 3s
                    } catch (InterruptedException e) {throw new RuntimeException(e);}
                    break;
                }
                else
                {
                    System.out.print(" State: Door opening " + " -> ");
                    try{
                        Thread.sleep(1500); // 1.5s
                    } catch (InterruptedException e) {throw new RuntimeException(e);}
                    this.state = State.DOOR_OPEN;
                    break;
                }

            /* This state is where the elevator has its door opens. Then checks if passenger entered */
            //case State.DOOR_OPEN:
            case DOOR_OPEN:
                System.out.println(" State: Door open ");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (this.elevatorHasPassenger)             /* If the elevator has a passenger it must be debarking it */
                {
                    System.out.print("Passenger has left elevator at floor " + this.destinationFloor
                            + " and Elevator now ready for new scheduler request " +  " -> ");
                    this.elevatorHasPassenger = false;              /* Now the elevator has no longer a passenger */
                }
                else                                                        /* If not  it must be embarking it */
                {
                    System.out.print("Passenger has entered at floor " + this.floorRequested
                        + " and will be moving to floor: " + this.destinationFloor + " -> ");
                    this.elevatorHasPassenger = true;                   /* Now the elevator has a passenger */
                }
                this.state = State.DOOR_CLOSING;
                break;

            /* This state always transitions to the Doors being Closed */
            //case State.DOOR_CLOSING:
            case DOOR_CLOSING:

                if (doorBroken) {
                    System.out.print(" State: Door is closing again due to transisent fault " + " -> ");

                    doorBroken = false;
                    try{
                        Thread.sleep(1500); // 1.5s
                    } catch (InterruptedException e) {throw new RuntimeException(e);}
                    state = State.DOOR_OPENING;
                    break;
                }
                else {
                    System.out.print(" State: Door closing " + " -> ");
                    try{
                        Thread.sleep(1500); // 1.5s
                    } catch (InterruptedException e) {throw new RuntimeException(e);}
                    state = State.DOOR_CLOSED;
                    break;
                }



            /* This state checks if an elevator request is in progress or not.*/
            //case State.DOOR_CLOSED:
            case DOOR_CLOSED:
                System.out.print(" Door closed " + " -> ");
                if (this.currentFloor != this.destinationFloor) /* If there is an elevator request than it proceeds to move. */
                {   /* Notify Scheduler that the elevator is being requested by a passenger. Add current floor and destination floor */
                    this.state = State.ACCELERATING;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
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
                System.out.print(" State: Accelerating " + " -> ");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (abs(currentFloor - this.stopFloor) == 1)
                {
                    state = State.DECELERATING;
                    break;
                }
                state = State.CRUISING;
                break;

            /* This state always transitions to the Decelerating state*/
            case CRUISING:
                if (this.direction.equals("Up"))
                {
                    currentFloor += 1;
                    try {
                        Thread.sleep(500); // 0.5 s for every floor
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } // Change this part
                    System.out.print(" State: Cruising and currently on floor: " + this.currentFloor  + " -> ");
                    if (currentFloor + 1 == this.stopFloor)
                    {
                        state = State.DECELERATING;
                        break;
                    }
                    state = State.CRUISING;
                    break;

                }
                else if(this.direction.equals("Down"))
                {
                    currentFloor -= 1;
                    try {
                        Thread.sleep(500); // 0.5 s for every floor
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } // Change this part
                    System.out.print(" State: Cruising and currently on floor: " + this.currentFloor  + " -> ");
                    if (currentFloor - 1 == this.stopFloor)
                    {
                        state = State.DECELERATING;
                        break;
                    }
                    state = State.CRUISING;
                    break;

                }


            /* This state always transitions to the Decelerating state*/
            case DECELERATING:
                System.out.print("State: Decelerating"  + "->");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

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
    private Message receiveFromScheduler() {
        return receiveUDPMessage(); // Blocking call, waits for a message

    }

    private Message receiveUDPMessage() {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            //System.out.println("ElevatorSubsystem ready to receive UDP message on port: " + listeningPort);

            sendReceiveSocket.receive(packet);

            ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
            ObjectInputStream in = new ObjectInputStream(bis);
            Message message = (Message) in.readObject();

            //System.out.println("Received object:");
            //message.printMessage();


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
    private void sendToSchedulerResponse(Message messageToBeSent) {
        sendUDPMessage(messageToBeSent);
    }

    public void sendUDPMessage(Message message) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(message);
            out.flush();
            byte[] serializedData = bos.toByteArray();

            DatagramPacket packet = new DatagramPacket(serializedData, serializedData.length, schedulerAddress, schedulerPort);
            sendReceiveSocket.send(packet);
        } catch (IOException e) {
            log("Error sending command to elevator: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Logs a message to the standard output, prefixed with the class name.
     * @param message The message to log.
     */
    private static void log(String message) {
        System.out.println("[ElevatorSubsystem] " + message);
    }

    public void closeSocket() {
        if (sendReceiveSocket != null && !sendReceiveSocket.isClosed()) {
            sendReceiveSocket.close();
            log("Socket closed.");
        }
    }

    public static void main(String[] args) {
        String schedulerIP = "localhost";
        int schedulerPort = 50000;
        if (args[0].equals("Elevator1"))
        {
            Elevator elevatorSubsystem = new Elevator(schedulerIP, schedulerPort, 1);
            new Thread(elevatorSubsystem).start();
            // Register shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                elevatorSubsystem.closeSocket();
                log("SchedulerSubsystem shutdown hook executed.");
            }));
        } else if (args[0].equals("Elevator2"))
        {
            Elevator elevatorSubsystem2 = new Elevator(schedulerIP, schedulerPort, 2);
            new Thread(elevatorSubsystem2).start();
            // Register shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                elevatorSubsystem2.closeSocket();
                log("SchedulerSubsystem shutdown hook executed.");
            }));
        } else if (args[0].equals("Elevator3"))
        {
            Elevator elevatorSubsystem3 = new Elevator(schedulerIP, schedulerPort, 3);
            new Thread(elevatorSubsystem3).start();
            // Register shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                elevatorSubsystem3.closeSocket();
                log("SchedulerSubsystem shutdown hook executed.");
            }));
        } else if (args[0].equals("Elevator4"))
        {
            Elevator elevatorSubsystem4 = new Elevator(schedulerIP, schedulerPort, 4);
            new Thread(elevatorSubsystem4).start();
            // Register shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                elevatorSubsystem4.closeSocket();
                log("SchedulerSubsystem shutdown hook executed.");
            }));
        }

    }

    public int getCurrentFloor()
    {
        return this.currentFloor;
    }
    // *The following methods were added for testing purposes*


}