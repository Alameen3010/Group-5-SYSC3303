import java.io.File;
import java.io.FileNotFoundException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**
 * @author Al-ameen Alliu
 * The FloorSubsystem handles reading elevator requests from a file and sending them to the SchedulerSubsystem.
 */
public class FloorSubsystem implements Runnable {
    private InetAddress schedulerAddress;
    private int schedulerPort;

    /**
     * Constructs a FloorSubsystem with the given IP address and port for the SchedulerSubsystem.
     *
     * @param schedulerIP   The IP address of the SchedulerSubsystem.
     * @param schedulerPort The port number on which the SchedulerSubsystem is listening.
     */
    public FloorSubsystem(String schedulerIP, int schedulerPort) {
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
     * The run method executed by the thread, starting the process of reading and sending requests.
     */
    public void run() {
        log("FloorSubsystem is starting.");
        readFileAndSendRequests("Requests.txt");
    }

    /**
     * Reads elevator requests from a file and sends them to the SchedulerSubsystem.
     *
     * @param filename The name of the file containing elevator requests.
     */
    private void readFileAndSendRequests(String filename) {
        log("Reading requests from file: " + filename);
        try (Scanner scanner = new Scanner(new File(filename));
             DatagramSocket sendSocket = new DatagramSocket()) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                log("Read line: " + line);
                sendRequestToScheduler(sendSocket, line);
                receiveResponseFromScheduler(sendSocket);
            }
        } catch (FileNotFoundException e) {
            log("File not found: " + e.getMessage());
        } catch (Exception e) {
            log("Error sending or receiving the request: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sends a request to the SchedulerSubsystem.
     *
     * @param sendSocket The DatagramSocket to use for sending the request.
     * @param request    The request string to be sent.
     */
    private void sendRequestToScheduler(DatagramSocket sendSocket, String request) {
        log("Sending request to Scheduler: " + request);
        try {
            byte[] buffer = request.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, schedulerAddress, schedulerPort);
            sendSocket.send(packet);
            log("Request sent to Scheduler: " + request);
        } catch (Exception e) {
            log("Error sending the request: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Receives a response from the SchedulerSubsystem.
     *
     * @param sendSocket The DatagramSocket to use for receiving the response.
     */
    private void receiveResponseFromScheduler(DatagramSocket sendSocket) {
        try {
            byte[] buffer = new byte[1024]; // Adjust the size based on the expected response length
            DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
            sendSocket.receive(responsePacket); // This will block until a response is received
            String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
            log("Response received from Scheduler: " + response);
            // Process the response here if needed
        } catch (Exception e) {
            log("Error receiving response from Scheduler: " + e.getMessage());
            e.printStackTrace();
        }
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
        new Thread(new FloorSubsystem(schedulerIP, schedulerPort)).start();
    }

    /**
     * Logs a message to the standard output, prefixed with the class name.
     *
     * @param message The message to log.
     */
    private static void log(String message) {
        System.out.println("[FloorSubsystem] " + message);
    }
}
