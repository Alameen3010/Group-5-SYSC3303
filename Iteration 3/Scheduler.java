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


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;
import java.io.*;
import java.net.*;

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
    private static final int SCHEDULER_PORT = 5000;
    private static final int ELEVATOR_PORT = 6000;

    private static final int FLOOR_PORT = 6050;

    private static final int BUFFER_SIZE = 1024;

    private Message listOfMessages; //= new Message("2023-02-05", 4, "Up", 6);


    public Scheduler() {
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
        this.listOfMessages = receiveFloorRequest();
        while(!processFloorRequest());
        sendCommandToElevator(this.listOfMessages);

        //receiveElevatorResponse();



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
    private void sendCommandToElevator(Message command) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(listOfMessages);
            out.flush();
            byte[] serializedData = bos.toByteArray();

            DatagramPacket packet = new DatagramPacket(serializedData, serializedData.length, elevatorAddress, ELEVATOR_PORT);
            socket.send(packet);
            log("Command sent to elevator: " + command);
        } catch (IOException e) {
            log("Error sending command to elevator: " + e.getMessage());
            e.printStackTrace();
        }
    }




    /**
     * Receives and processes the response from the elevator subsystem.
     */
    private void receiveElevatorResponse() {
        try {
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String response = new String(packet.getData(), 0, packet.getLength());
            log("Response from elevator: " + response);
            // Add logic here for what to do after receiving the response from the elevator.
        } catch (IOException e) {
            log("Error receiving response from elevator: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void log(String message) {
        System.out.println("[SchedulerSubsystem] " + message);
    }

    /**
     * The main method that starts the scheduler subsystem thread.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        Scheduler schedulerSubsystem = new Scheduler();
        log("Starting SchedulerSubsystem thread.");
        new Thread(schedulerSubsystem).start();
    }

}
