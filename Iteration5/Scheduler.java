/**
 * The Scheduler class is a thread responsible for acting as a communication channel between the floor and the scheduler
 * It has two boxes shared with all other subsystems.
 *
 *
 * @author Ilyes Outaleb (101185290)
 * @version February 3, 2024,
 * Edited: Ilyes Outaleb (101185290)
 * @version March 02, 2024,
 * Edited: Ilyes Outaleb (101185290)
 * @version: March 10, 2024
 * Edited Taran Basati
 * @version March 20, 2024
 * Edited: Ilyes Outaleb (101185290)
 * @version April 8 2024
 */

import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Scheduler implements Runnable{

    /* Variable representing all the possible states of the scheduler. */
    private enum State {
        RECEIVING_REQUEST,
        SCHEDULING,
    }

    /* Variable representing the state of the scheduler. */
    private State state;

    private DatagramSocket socket;

    private InetAddress elevatorAddress;
    private static final int SCHEDULER_PORT = 50000;
    private static final int ELEVATOR_PORT = 60000;

    private static final int FLOOR_PORT = 55000;
    private static final int BUFFER_SIZE = 1024;

    private ArrayList<Message> listOfMessages = new ArrayList<>(); //= new Message("2023-02-05", 4, "Up", 6);

    private int numberOfElevators;

    private List<Integer> brokenElevators = new ArrayList<>(); /* Implements system faults */

    private ArrayList<Integer> fullElevators = new ArrayList<Integer>(); /* Implements capacity */
    public Scheduler(int numberElevators) {
        this.numberOfElevators = numberElevators;
        this.state = State.RECEIVING_REQUEST;
        try {
            this.socket = new DatagramSocket(SCHEDULER_PORT);

            this.elevatorAddress = InetAddress.getByName("localhost");
            log("Scheduler DatagramSocket created on port " + SCHEDULER_PORT);
        } catch (Exception e) {
            log("Error initializing SchedulerSubsystem: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void run() {
        log("SchedulerSubsystem is running.");
        int count = 0;
        int elevatorNumber = 1;
        processFloorRequest();
        System.out.println("================ REQUESTS RECEIVED BY FLOOR ============================");
        Message temp;
        do {

            temp = receiveFloorRequest();
            temp.horizontalPrint();
            this.listOfMessages.add(temp);
            count ++;

        } while(temp.getBuffer() == false); /* The Buffer in this case holds the confirmation of whether it received everything from the floor */
        System.out.println("================ END ============================");

        listOfMessages.remove(listOfMessages.size() - 1); // remove final message
        count --;
        /* Scheduling portion */
        processFloorRequest();
        for (Message messageSentToElevator : this.listOfMessages) {
            /* Scheduling portion */
            if (messageSentToElevator.getElevator_fault() == 1)
            {
                System.out.println("Elevator " + elevatorNumber + "is broken (system fault), so now");
                brokenElevators.add(elevatorNumber); // store the number of broken elevators
                elevatorNumber = (elevatorNumber) % (this.numberOfElevators) + 1;
            }
            while (brokenElevators.contains(elevatorNumber)) // skip any broken elevator
            {
                elevatorNumber = (elevatorNumber) % (this.numberOfElevators) + 1;
            }
            while(fullElevators.contains(elevatorNumber)) /* Skips any full elevator */
            {
                elevatorNumber = (elevatorNumber) % (this.numberOfElevators) + 1;
            }
            System.out.println("Sending Requests to Elevator " + elevatorNumber);
            messageSentToElevator.horizontalPrint();
            sendCommandToElevator(messageSentToElevator, ELEVATOR_PORT + elevatorNumber);
            boolean isFull = receiveElevatorResponse().getBuffer();
            if (isFull)
            {
                fullElevators.add(elevatorNumber);
            }
            elevatorNumber = (elevatorNumber) % (this.numberOfElevators) + 1;
        }
        Message doneMessage = new Message("0", 0, "0", 0, true, 0, 0);

        for(int i = 1; i <= this.numberOfElevators; i++)
        {
            sendCommandToElevator(doneMessage, ELEVATOR_PORT + i);

        }
    
        for(int i = 0; i < this.numberOfElevators; i++)
        {
            receiveElevatorResponse();
            System.out.println("HI1");
        }

        System.out.println("HI2");
        sendFloorResponse(this.listOfMessages.get(count - 1));
        fullElevators.clear(); /* Clears all the elevators as they are ready to receive new passengers */

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
            case RECEIVING_REQUEST: /* If the machine is in the entry state it checks if there is a request. Simulated by user input. */
                System.out.println("State: Receiving_Request");
                this.state = State.SCHEDULING;   /* If there is than it schedules the request to reduce waiting time */
                break;

            case SCHEDULING: /* If the machine is in the scheduling state. This will send the request to the elevator subsystem in future iterations. */
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
     * Waits for and processes incoming requests from the floor subsystem.
     */
    private Message receiveFloorRequest() {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            socket.receive(packet);

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
     * Sends a command to the elevator subsystem after processing the floor request.
     * @param command The command to be sent to the elevator subsystem.
     */
    private void sendFloorResponse(Message command) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(command);
            out.flush();
            byte[] serializedData = bos.toByteArray();

            DatagramPacket packet = new DatagramPacket(serializedData, serializedData.length, InetAddress.getLocalHost(), FLOOR_PORT);
            socket.send(packet);
        } catch (IOException e) {
            log("Error sending command to elevator: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sends a command to the elevator subsystem after processing the floor request.
     * @param command The command to be sent to the elevator subsystem.
     * @param port the number of the elevator should be different for each elevator. Serves as an identifier.
     */
    
    private void sendCommandToElevator(Message command, int port) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(command);
            out.flush();
            byte[] serializedData = bos.toByteArray();

            DatagramPacket packet = new DatagramPacket(serializedData, serializedData.length, elevatorAddress, port);
            socket.send(packet);
        } catch (IOException e) {
            log("Error sending command to elevator: " + e.getMessage());
            e.printStackTrace();
        }
    }




    /**
     * Receives and processes the response from the elevator subsystem.
     */
    private Message receiveElevatorResponse() {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            socket.receive(packet);

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

    private static void log(String message) {
        System.out.println("[SchedulerSubsystem] " + message);
    }

    public void closeSocket() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            log("Socket closed.");
        }
    }

    /**
     * The main method that starts the scheduler subsystem thread.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        final int NUMBERELEVATORS = 4; // Can be changed

        Scheduler schedulerSubsystem = new Scheduler(NUMBERELEVATORS);
        log("Starting SchedulerSubsystem thread.");

        new Thread(schedulerSubsystem).start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            schedulerSubsystem.closeSocket();
            log("SchedulerSubsystem shutdown hook executed.");
        }));


    }

}
