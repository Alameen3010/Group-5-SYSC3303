
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;

/**
 * @author Al-ameen Alliu
 *
 * The SchedulerSubsystem is responsible for coordinating messages between the FloorSubsystem and ElevatorSubsystem.
 * It acts as a UDP server to receive requests from the Floor system and as a UDP client to send commands to the Elevator system.
 */
public class SchedulerSubsystem implements Runnable {

    /**
     * The State enum defines the different states of the SchedulerSubsystem during its operation.
     */
    private enum State {
        RECEIVING_REQUEST,
        SCHEDULING,
        SENDING_COMMAND,
        WAITING_FOR_ELEVATOR_RESPONSE,
    }

    private State state;
    private DatagramSocket socket;
    private InetAddress elevatorAddress;
    private static final int SCHEDULER_PORT = 5000;
    private static final int ELEVATOR_PORT = 6000;

    /**
     * Initializes a new SchedulerSubsystem and binds it to a port for listening.
     * It also sets the InetAddress for the elevator subsystem.
     */
    public SchedulerSubsystem() {
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

    /**
     * The main run loop of the SchedulerSubsystem thread, handling state transitions and actions.
     */
    public void run() {
        log("SchedulerSubsystem is running.");
        while (true) {
            switch (state) {
                case RECEIVING_REQUEST:
                    receiveFloorRequest();
                    break;
                case SCHEDULING:
                    // Add logic here to select which elevator to send the request to, based on the scheduling algorithm.
                    state = State.SENDING_COMMAND;
                    break;
                case SENDING_COMMAND:
                    // Example: sending the command "REQUEST,4,6" to the elevator.
                    // Work needs to be done here
                    sendCommandToElevator("REQUEST,4,Up,6");
                    break;
                case WAITING_FOR_ELEVATOR_RESPONSE:
                    receiveElevatorResponse();
                    break;
            }
        }
    }

    /**
     * Waits for and processes incoming requests from the floor subsystem.
     */
    private void receiveFloorRequest() {
        try {
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String message = new String(packet.getData(), 0, packet.getLength());
            log("Received floor request: " + message);
            // Add logic here for processing the floor request and transitioning to scheduling.
            state = State.SCHEDULING;
        } catch (IOException e) {
            log("Error receiving floor request: " + e.getMessage());
        }
    }

    /**
     * Sends a command to the elevator subsystem after processing the floor request.
     * @param command The command to be sent to the elevator subsystem.
     */
    private void sendCommandToElevator(String command) {
        try {
            byte[] buffer = command.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, elevatorAddress, ELEVATOR_PORT);
            socket.send(packet);
            log("Command sent to elevator: " + command);
            state = State.WAITING_FOR_ELEVATOR_RESPONSE;
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
            state = State.RECEIVING_REQUEST;
        } catch (IOException e) {
            log("Error receiving response from elevator: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Logs a message to the standard output, prefixed with the class name.
     * @param message The message to log.
     */
    private static void log(String message) {
        System.out.println("[SchedulerSubsystem] " + message);
    }

    /**
     * The main method that starts the scheduler subsystem thread.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        SchedulerSubsystem schedulerSubsystem = new SchedulerSubsystem();
        log("Starting SchedulerSubsystem thread.");
        new Thread(schedulerSubsystem).start();
    }
}
