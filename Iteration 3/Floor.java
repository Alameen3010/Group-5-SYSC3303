/**
 * The Floor class represents a floor in a building and implements the Runnable interface, allowing it to be used
 * as a thread. It reads requests from a csv/txt file, processes them, and communicates with a shared Box object
 * that is shared with scheduler.
 * @author Rozba Hakam (101190098)
 * @author Ilyes Outaleb (101185290)
 * @version 2024-02-03
 */

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
import java.io.IOException;

public class Floor implements Runnable {

    /* The sharedBox is a reference to the Box object shared with the scheduler. */

    private InetAddress schedulerAddress;
    private int schedulerPort;

    private DatagramSocket socket;

    private int floorPort = 55000;

    private static final int BUFFER_SIZE = 1024;

    private boolean continueReading = true;
    /**
     * Constructs a FloorSubsystem with the given IP address and port for the SchedulerSubsystem.
     *
     * @param schedulerIP   The IP address of the SchedulerSubsystem.
     * @param schedulerPort The port number on which the SchedulerSubsystem is listening.
     */
    public Floor(String schedulerIP, int schedulerPort)
    {
        try {
            this.socket = new DatagramSocket(floorPort);
            this.schedulerAddress = InetAddress.getByName(schedulerIP);
            this.schedulerPort = schedulerPort;
        } catch (Exception e) {
            log("Error initializing InetAddress: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * The run method is called when a floor is executed as a thread. It reads requests
     * from a file, processes them, and terminates the program afterward.
     */
    public void run()
    {   log("FloorSubsystem is starting.");
        readFileAndSendRequests("Requests.txt");

        //System.exit(0);
    }



    private Message receiveUDPMessage() {
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


    /**
     * Reads requests from a csv/txt file, processes each line, and interacts with the shared Box.
     *
     * @param filename The name of the file containing the requests.
     */
    private void readFileAndSendRequests(String filename)
    {   Message message;
        log("Reading requests from file: " + filename);
        try {
            File requestFile = new File(filename);
            Scanner scanner = new Scanner(requestFile);
            DatagramSocket sendSocket = new DatagramSocket();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                log("Read line: " + line);
                message = createMessageFromCSV(line);
                sendRequestToScheduler(sendSocket, message);

            }
            Message temp = new Message("0",0,"0",0, true);
            sendRequestToScheduler(sendSocket, temp);
            scanner.close();
            message = receiveUDPMessage();
            sendSocket.close();

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (Exception e) {
            log("Error sending or receiving the request: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sends a request to the SchedulerSubsystem.
     *
     * @param sendSocket The DatagramSocket to use for sending the request
     */
    private void sendRequestToScheduler(DatagramSocket sendSocket, Message message) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(message);
            out.flush();
            byte[] serializedData = bos.toByteArray();

            DatagramPacket packet = new DatagramPacket(serializedData, serializedData.length, schedulerAddress, schedulerPort);
            sendSocket.send(packet);
        } catch (IOException e) {
            log("Error sending command to elevator: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * divide each CSV/txt line, creates a Box object from the data, and sends it to the shared Box.
     *
     * @param line The CSV line stores the requested information.
     */
    private Message createMessageFromCSV(String line)
    {
        /* Parse the CSV line and create a Box object */
        String[] data = line.split(",");
        String time = data[0].trim();
        int floorNumber = Integer.parseInt(data[1].trim());
        String direction = data[2].trim();
        int carButtonNumber = Integer.parseInt(data[3].trim());
        //boolean directionUp;

        /* ternary Operator to assign the direction up a boolean value */
        //directionUp = (direction.equals("Up")) ? true : false;

        Message temp = new Message(time, floorNumber, direction, carButtonNumber, false);
        return temp;
        //getFromCSV(time, floorNumber, direction, carButtonNumber);
    }


    /**
     * Logs a message to the standard output, prefixed with the class name.
     *
     * @param message The message to log.
     */
    private static void log(String message) {
        System.out.println("[FloorSubsystem] " + message);
    }


    public void closeSocket() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            log("Socket closed.");
        }
    }
    /**
     * The main method to start the FloorSubsystem.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        String schedulerIP = "localhost";
        int schedulerPort = 50000;

        log("Creating instance of FloorSubsystem.");

        Floor floorSubsystem = new Floor(schedulerIP, schedulerPort);
        new Thread(floorSubsystem).start();


        // Register shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            floorSubsystem.closeSocket();
            log("FloorSubsystem shutdown hook executed.");
        }));
    }
}
