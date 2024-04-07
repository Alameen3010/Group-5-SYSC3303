/**
 * The  ElevatorSubsystem class simulates the operation of an elevator interacting with a floor request system.
 * It processes incoming UDP messages representing floor requests and simulates state transitions of the elevator
 * accordingly. The elevator states include stopping, opening doors, closing doors, accelerating, cruising, and decelerating.
 * Communication with the scheduler system is achieved through UDP packets.
 *
 * @author Al-ameen Alliu
 * @id 101159780
 */


import java.io.IOError;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ElevatorSubsystem implements Runnable {

    // Enumeration for the state of the elevator.
    private enum State {
        STOP, DOOR_OPENING, DOOR_OPEN, DOOR_CLOSING, DOOR_CLOSED, ACCELERATING, CRUISING, DECELERATING,
    }

    private int floorRequested;
    private int destinationFloor;
    private int currentFloor;
    private int stopFloor;
    private State state;
    private boolean elevatorHasPassenger;
    private DatagramSocket sendReceiveSocket;
    private int schedulerPort= 5000;
    private int listeningPort = 6000;
    private InetAddress schedulerAddress;
    private static final int BUFFER_SIZE = 1024;

    /**
     * Constructor for the ElevatorSubsystem.
     *
     * @param schedulerIP The IP address of the scheduler to connect to.
     * @param schedulerPort The port of the scheduler to connect to.
     */
    public ElevatorSubsystem(String schedulerIP, int schedulerPort) {
        this.schedulerPort = schedulerPort;
        try {
            this.schedulerAddress = InetAddress.getByName(schedulerIP);
            this.sendReceiveSocket = new DatagramSocket(listeningPort);
            System.out.println("ElevatorSubsystem listening on port: " + listeningPort);
        } catch (Exception e) {
            System.err.println("Error initializing DatagramSocket: " + e.getMessage());
            System.exit(1);
        }
        this.state = State.STOP;
        this.currentFloor = 0;
        this.elevatorHasPassenger = false;
    }

    /**
     * The main logic for the elevator's operation. This method runs in a separate thread and continuously
     * listens for incoming UDP messages, processes them, and updates the state of the elevator.
     */
    public void run() {
        while (true) {
            String message = receiveUDPMessage(); // Blocking call, waits for a message
            if (!message.isEmpty()) {
                String[] parts = message.split(":");
                if (parts[0].equals("REQUEST")) {
                    this.floorRequested = Integer.parseInt(parts[1]);
                    this.destinationFloor = Integer.parseInt(parts[2]);

                    while (!processSchedulerRequest()) {
                        if (this.currentFloor == this.floorRequested) {
                            this.stopFloor = this.destinationFloor;
                        } else {
                            this.stopFloor = this.floorRequested;
                        }
                    }
                    sendUDPMessage("STATE:" + this.currentFloor + ":" + this.state);
                }
            }
        }
    }
/**
 * Sends a UDP message to the scheduler.
 *
 * @param message The message to send.
  */
    public void sendUDPMessage(String message) {
        try {
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, schedulerAddress, schedulerPort);
            sendReceiveSocket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Receives a UDP message from the scheduler.
     *
     * @return The message received.
     */
    private String receiveUDPMessage() {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            System.out.println("ElevatorSubsystem ready to receive UDP message on port: " + listeningPort);

            sendReceiveSocket.receive(packet);
            String message = new String(packet.getData(), 0, packet.getLength());
            System.out.println("ElevatorSubsystem received message: " + message);
            // Parse the message immediately after receiving it
            parseMessage(message);
            return message;
        } catch (Exception e) {
            System.err.println("ElevatorSubsystem failed to receive UDP message: " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }


    /**
     * Parses a UDP message received from the scheduler. The message is expected to contain four parts:
     * a timestamp, the requested floor, the direction (up or down), and the destination floor. If the message
     * is valid, it updates the elevator's current request and destination floor and then initiates the
     * state transitions to process this request.
     *
     * @param message The raw UDP message received from the scheduler.
     */
    private void parseMessage(String message) {
        String[] parts = message.split(",");
        if (parts.length == 4) {
            String timestamp = parts[0].trim();
            int requestFloor = Integer.parseInt(parts[1].trim());
            boolean isDirectionUp = "Up".equals(parts[2].trim());
            int destinationFloor = Integer.parseInt(parts[3].trim());

            // Update the elevator's state based on the message
            this.floorRequested = requestFloor;
            this.destinationFloor = destinationFloor;

            //work to be done here   based on the state, act (e.g., moving the elevator, opening doors, etc.)
            processSchedulerRequest(); // Assuming this method will initiate the state transitions
        } else {
            System.err.println("Received an invalid message: " + message);
        }
    }

    /**
     * Processes the current request by transitioning through the elevator states to simulate picking up
     * a passenger from the requested floor and dropping them off at the destination floor. This method
     * will directly simulate the elevator's movement between floors and the opening and closing of doors
     * without actual time delays. It assumes an immediate transition between states for the purpose of
     * simulation.
     *
     * @return true if the process has completed and the elevator is ready for a new request, false otherwise.
     */
    public boolean processSchedulerRequest() {
        System.out.println("Processing request: Floor Requested: " + floorRequested + ", Destination Floor: " + destinationFloor);

        // Directly move to the floor requested, no gradual increase/decrease to simulate real-time movement
        if (currentFloor != floorRequested) {
            System.out.println("State: Accelerating");
            System.out.println("State: Cruising");
            System.out.println("State: Decelerating");
            currentFloor = floorRequested; // Simulate immediate arrival
            System.out.println("State: Stop");
            System.out.println("Current Floor:" + currentFloor);
        }

        // Open doors to let the passenger in
        System.out.println("State: Door opening");
        System.out.println("State: Door open");
        elevatorHasPassenger = true; // Simulate passenger entry
        System.out.println("Passenger has entered at floor " + floorRequested);
        System.out.println("State: Door closing");
        System.out.println("State: Door closed");

        // Directly move to the destination floor
        if (currentFloor != destinationFloor) {
            System.out.println("State: Accelerating");
            System.out.println("State: Cruising");
            System.out.println("State: Decelerating");
            currentFloor = destinationFloor; // Simulate immediate arrival at destination floor
            System.out.println("State: Stop");
            System.out.println("Current Floor:" + currentFloor);
        }

        // Open doors to let the passenger out
        System.out.println("State: Door opening");
        System.out.println("State: Door open");
        elevatorHasPassenger = false; // Simulate passenger exit
        System.out.println("Passenger has left elevator at floor " + destinationFloor);
        System.out.println("State: Door closing");
        System.out.println("State: Door closed");

        return true; // The process is finished
    }


    /**
     * Logs a message to the console. This is a utility method to centralize the logging mechanism for the
     * ElevatorSubsystem, allowing for easy modification or redirection of log output.
     *
     * @param message The message to log.
     */
    private void logState(String message) {
        System.out.println(message);
    }

    /**
     * The entry point of the program. This method creates an instance of  ElevatorSubsystem and starts
     * its thread to begin processing floor requests.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
            String schedulerIP = "localhost";
            int schedulerPort = 5000;

            ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem(schedulerIP, schedulerPort);

            // Start the ElevatorSubsystem in a new thread
            new Thread(elevatorSubsystem).start();
        }
    }


