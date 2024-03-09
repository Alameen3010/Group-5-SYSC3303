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

public class Floor implements Runnable {

    /* The sharedBox is a reference to the Box object shared with the scheduler. */

    private InetAddress schedulerAddress;
    private int schedulerPort;

    /**
     * Constructs a FloorSubsystem with the given IP address and port for the SchedulerSubsystem.
     *
     * @param schedulerIP   The IP address of the SchedulerSubsystem.
     * @param schedulerPort The port number on which the SchedulerSubsystem is listening.
     */
    public Floor(String schedulerIP, int schedulerPort)
    {
        try {
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
            //while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            log("Read line: " + line);
            message = createMessageFromCSV(line);
            sendRequestToScheduler(sendSocket, message);
                //receiveResponseFromScheduler(sendSocket);
                //getFromSchedulerResponse();
            //}
            scanner.close();
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
        message.printMessage();
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

        Message temp = new Message(time, floorNumber, direction, carButtonNumber);
        return temp;
        //getFromCSV(time, floorNumber, direction, carButtonNumber);
    }

    /**
     * Creates a Box object with the provided message information and sends it to the shared Box.
     *
     * @param time The time of the request.
     * @param floorNumber The floor number of the request.
     * @param directionUp The direction of the request (Up = true) or (Down = false).
     * @param carButtonNumber The car button number associated with the request.
     */
//    private void getFromCSV(String time, int floorNumber, String directionUp, int carButtonNumber)
//    {
//        Message temp = new Message(time, floorNumber, directionUp, carButtonNumber);
//        //sharedBox.getFromSource(temp);
//    }

    /**
     * gets a Box object from the shared Box and prints its contents.
     */
//    private void getFromSchedulerResponse()
//    {
//        Box temp = sharedBox.sendToSource();
//        temp.printContents();
//    }


    /**
     * Logs a message to the standard output, prefixed with the class name.
     *
     * @param message The message to log.
     */
    private static void log(String message) {
        System.out.println("[FloorSubsystem] " + message);
    }

    /**
     * The main method to start the FloorSubsystem.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        String schedulerIP = "localhost";
        int schedulerPort = 5000;
        log("Creating instance of FloorSubsystem.");
        new Thread(new Floor(schedulerIP, schedulerPort)).start();
    }
}