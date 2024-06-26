import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
 * Edited: Taran Basati
 * @version March 20 2024
 * Edited: Ilyes Outaleb (101185290)
 *@version April 8 2024
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

    private boolean doorBroken = false; /* Implements the transisent fault */

    private String direction;

    private static final int size = 5; /* Implements capacity This could be set to anything */

    /* The following code is used for getting measurements. */
    private double startTimerRequest;
    private double endTimerRequest;
    private HashMap<Message, Double> timerRequests = new HashMap<Message, Double>();
    private double startTimerSystem;
    private double endTimerSystem;

    private int floorCounter;
    private int totalFloors;
    private HashMap<Message, Integer> floorsTraversed = new HashMap<Message, Integer>();

    private int countPassengers = 0;

    private int numberOfTransientFaults = 0;

    private String systemFaults = "Non-Functional";

    private String doorStatus = "CLOSED";

    /**
     * Constructor for Elevator class.
     *
     */
    public Elevator(String schedulerIP, int schedulerPort, int id) {

        this.state = State.STOP;
        this.currentFloor = 0;
        this.elevatorHasPassenger = false;
        this.id = id;
        this.totalFloors = 0;
        this.floorCounter = 0;
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
        int count = 0;
        int globalCount = 0;

        //System.out.println("================ REQUESTS IN ELEVATOR " + this.id + "QUEUE ============================");
        Message temp;
        do {

            temp = receiveFromScheduler();

            if (temp.getBuffer() == false)
            {
                temp.horizontalPrint(this.id);
                this.request.add(temp);
                this.systemFaults = "Normal";
                count ++;
                this.countPassengers ++;
                /* Message if array can still accept more passengers */
                Message responseSize = new Message("0",0,"0",0, false, 0, 0);
                if (this.countPassengers >= size)
                {   /* Message if the array is full */
                    responseSize = new Message("0",0,"0",0, true, 0, 0);
                }
                sendToSchedulerResponse(responseSize);
                System.out.println("Sent Size: " + responseSize.getBuffer());
            }
        } while(temp.getBuffer() == false);

        this.startTimerSystem = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            this.floorCounter = 0;
            this.startTimerRequest = System.currentTimeMillis();

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
            } else if (this.currentFloor > this.stopFloor){

                this.direction = "Down";
            }
            else {
                this.direction = "None";
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
                        if (this.currentFloor < this.stopFloor) {
                            this.direction = "Up";
                        } else if (this.currentFloor > this.stopFloor){

                            this.direction = "Down";
                        }
                        else {
                            this.direction = "None";
                        }

                    }
                }

            }
            System.out.println("");// only for one elevator change it to multiple by looking at iteration 2 original.

            this.request.get(i).setBuffer(true); /* The buffer in this case is used to tell the elevator that it procesed the request */
            doorBroken = false;
            System.out.println("Finished moving on");
            this.endTimerRequest = System.currentTimeMillis() - startTimerRequest;
            this.timerRequests.put(request.get(i),(endTimerRequest/1000));
            this.floorsTraversed.put(request.get(i),(floorCounter));
            this.totalFloors += floorCounter;
            System.out.println(" ");
        }

        System.out.println("Finished list of requests. Now available for new.");
        this.endTimerSystem = System.currentTimeMillis() - startTimerSystem;
        System.out.println(count);
        if (count > 1)
        {
            sendToSchedulerResponse(this.request.get(count-1));
            this.request.get(count-1).horizontalPrint(id);
        }
    }

    public boolean processSchedulerRequest() {

        switch (this.state) {
            /* Will start at Stop and will transisient depending if its on the right floor or not */
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
            case DOOR_OPENING:
                this.doorStatus = "OPENING";
                if (doorBroken)
                {   this.numberOfTransientFaults ++;
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
                
            case DOOR_OPEN:
                this.doorStatus = "OPEN";
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
                    this.countPassengers --;
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
            case DOOR_CLOSING:
                this.doorStatus = "CLOSING";
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
            case DOOR_CLOSED:
                this.doorStatus = "CLOSED";
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
                    floorCounter += 1;
                    try {
                        Thread.sleep(500); // 0.5 s for every floor
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } 
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
                    floorCounter += 1;
                    try {
                        Thread.sleep(500); // 0.5 s for every floor
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } 
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
                floorCounter += 1;
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

    /**
     * Returns the message received by UDP communication.
     * Serialization and Deserialization is used as an object is being passed.
     */
    private Message receiveUDPMessage() {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            sendReceiveSocket.receive(packet);

            ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
            ObjectInputStream in = new ObjectInputStream(bis);
            Message message = (Message) in.readObject();
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

    /**
     * Returns the message received by UDP communication.
     * Serialization and Deserialization is used as an object is being passed.
     */
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

     /**
     * The method used to get the measurements used in UI.
     * @return a String representing all the values measured in the process.
     */
    
    public String measurements(){
        StringBuilder measurement = new StringBuilder();
        measurement.append("\n\nFull Breakdown for Elevator ").append(this.id).append(":\n");
        for (Map.Entry<Message,Double> timerRequests: timerRequests.entrySet()) {
            measurement.append("Request [").append(timerRequests.getKey().toString()).append("] took ")
                    .append(timerRequests.getValue()).append(" seconds\n");
        }
        for (Map.Entry<Message,Integer> floorsTraversed: floorsTraversed.entrySet()) {
            measurement.append("Request [").append(floorsTraversed.getKey().toString()).append("] traversed through ")
                    .append(floorsTraversed.getValue()).append(" floors.\n");
        }
        measurement.append("In total, Elevator ").append(this.id).append(" took ").append(this.endTimerSystem/1000)
                .append(" seconds and traversed through ").append(this.totalFloors).append(" floors\n\n");
        return String.valueOf(measurement);
    }


    /* Below is in case the second way to run code is used with seperate terminals */
    public static void main(String[] args) {
        String schedulerIP = "localhost";
        int schedulerPort = 50000;
        if (args[0].equals("Elevator1")) {
            Elevator elevatorSubsystem = new Elevator(schedulerIP, schedulerPort, 1);
            new Thread(elevatorSubsystem).start();
            // Register shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log(elevatorSubsystem.measurements());
                elevatorSubsystem.closeSocket();
                log("SchedulerSubsystem shutdown hook executed.");
            }));
        } else if (args[0].equals("Elevator2")) {
            Elevator elevatorSubsystem2 = new Elevator(schedulerIP, schedulerPort, 2);
            new Thread(elevatorSubsystem2).start();
            // Register shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log(elevatorSubsystem2.measurements());
                elevatorSubsystem2.closeSocket();
                log("SchedulerSubsystem shutdown hook executed.");
            }));
        } else if (args[0].equals("Elevator3")) {
            Elevator elevatorSubsystem3 = new Elevator(schedulerIP, schedulerPort, 3);
            new Thread(elevatorSubsystem3).start();
            // Register shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log(elevatorSubsystem3.measurements());
                elevatorSubsystem3.closeSocket();
                log("SchedulerSubsystem shutdown hook executed.");
            }));
        } else if (args[0].equals("Elevator4")) {
            Elevator elevatorSubsystem4 = new Elevator(schedulerIP, schedulerPort, 4);
            new Thread(elevatorSubsystem4).start();
            // Register shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log(elevatorSubsystem4.measurements());
                elevatorSubsystem4.closeSocket();
                log("SchedulerSubsystem shutdown hook executed.");
            }));
        }
    }

    public int getCurrentFloor()
    {
        return this.currentFloor;
    }

    // This method returns the elevator's current direction.
    public String getDirection() {
        return this.direction; // Ensure that 'direction' is being updated appropriately in your state machine
    }

    /* All the following methods are to give access to private fields for the elevator Mangager which will populate the GUI */
    public int getTransientFaults() {
        return this.numberOfTransientFaults;
    }

    
    public String getStatus() {
        // You need to implement the logic for status
        // For now, returning a dummy status
        return this.systemFaults;
    }

    public int getNumberOfPassengers()
    {
        return this.countPassengers;
    }
    

    public String getDoorStatus()
    {
        return this.doorStatus;
    }
}
