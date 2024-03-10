import java.util.*;
import java.io.*;
import java.net.*;

public class Scheduler implements Runnable {

    private enum State {
        RECEIVING_REQUEST,
        SCHEDULING,
    }

    private State state;
    private DatagramSocket socket;
    private InetAddress elevatorAddress;
    private static final int SCHEDULER_PORT = 50000;
    private static final int ELEVATOR_PORT = 60000;
    private static final int FLOOR_PORT = 55000;
    private static final int BUFFER_SIZE = 1024;
    private Queue<Message> requestQueue = new LinkedList<>();

    //private Elevator elevator1 = new Elevator("localhost",90000);

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
        while (true) {
            receiveFloorRequests();
            processFloorRequests();
        }
    }

    private void receiveFloorRequests() {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
            ObjectInputStream in = new ObjectInputStream(bis);
            Message message = (Message) in.readObject();
            requestQueue.offer(message);
        } catch (Exception e) {
            System.err.println("SchedulerSubsystem failed to receive UDP message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void processFloorRequests() {
        while (!requestQueue.isEmpty()) {
            Message message = requestQueue.poll();
            //decideOrderOfRequests(message);
            sendCommandToElevator(message);
        }
    }
/**
    private void decideOrderOfRequests(Message message) {
        int destinationFloor = message.getDestination();
        String direction = message.getDirection();

        // If the elevator is moving up and the request is in the same direction
        if (elevator1.getCurrentDirection().equals("UP") && direction.equals("UP")) {
            if (destinationFloor >= elevator1.getCurrentFloor()) {
                sendCommandToElevator(message); // Send the command to the elevator immediately
            } else {
                // If the request is below the current floor, enqueue it for later processing
                requestQueue.offer(message);
            }
        }
        // If the elevator is moving down and the request is in the same direction
        if (elevator1.getCurrentDirection().equals("DOWN") && direction.equals("DOWN")) {
            if (destinationFloor <= elevator1.getCurrentFloor()) {
                sendCommandToElevator(message); // Send the command to the elevator immediately
            } else {
                // If the request is above the current floor, enqueue it for later processing
                requestQueue.offer(message);
            }
        }
        // If the elevator is currently stopped, prioritize requests based on current position
        else if (elevator1.getCurrentDirection().equals("STOP")) {
            // If the request is at the same floor as the elevator, send the command immediately
            if (destinationFloor == elevator1.getCurrentFloor()) {
                sendCommandToElevator(message);
            } else {
                // If the request is not at the same floor, enqueue it for later processing
                requestQueue.offer(message);
            }
        }
        // If the elevator is moving in the opposite direction, enqueue the request for later processing
        else {
            requestQueue.offer(message);
        }
    }
*/
    private void sendCommandToElevator(Message command) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(command);
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

    private static void log(String message) {
        System.out.println("[SchedulerSubsystem] " + message);
    }

    public void closeSocket() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            log("Socket closed.");
        }
    }

    public static void main(String[] args) {
        Scheduler schedulerSubsystem = new Scheduler();
        log("Starting SchedulerSubsystem thread.");
        new Thread(schedulerSubsystem).start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            schedulerSubsystem.closeSocket();
            log("SchedulerSubsystem shutdown hook executed.");
        }));
    }
}
