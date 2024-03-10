import java.util.*;
import java.io.*;
import java.net.*;

public class Floor implements Runnable {

    private InetAddress schedulerAddress;
    private int schedulerPort;

    private DatagramSocket socket;

    private int floorPort = 55000;

    private static final int BUFFER_SIZE = 1024;

    private Queue<Message> requestQueue = new LinkedList<>();

    public Floor(String schedulerIP, int schedulerPort) {
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

    public void run() {
        log("FloorSubsystem is starting.");
        readFileAndSendRequests("Requests.txt");
        sendRequestsToScheduler();
    }

    private void sendRequestsToScheduler() {
        DatagramSocket sendSocket = null;
        try {
            sendSocket = new DatagramSocket();
            for (Message message : requestQueue) {
                sendRequestToScheduler(sendSocket, message);
            }
        } catch (IOException e) {
            log("Error sending requests to scheduler: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (sendSocket != null && !sendSocket.isClosed()) {
                sendSocket.close();
            }
        }
    }

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
            log("Error sending command to scheduler: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void readFileAndSendRequests(String filename) {
        log("Reading requests from file: " + filename);
        try {
            File requestFile = new File(filename);
            Scanner scanner = new Scanner(requestFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                log("Read line: " + line);
                Message message = createMessageFromCSV(line);
                requestQueue.offer(message);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (Exception e) {
            log("Error sending or receiving the request: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Message createMessageFromCSV(String line) {
        String[] data = line.split(",");
        String time = data[0].trim();
        int floorNumber = Integer.parseInt(data[1].trim());
        String direction = data[2].trim();
        int carButtonNumber = Integer.parseInt(data[3].trim());
        return new Message(time, floorNumber, direction, carButtonNumber, false);
    }

    private static void log(String message) {
        System.out.println("[FloorSubsystem] " + message);
    }

    public void closeSocket() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            log("Socket closed.");
        }
    }

    public static void main(String[] args) {
        String schedulerIP = "localhost";
        int schedulerPort = 50000;

        log("Creating instance of FloorSubsystem.");

        Floor floorSubsystem = new Floor(schedulerIP, schedulerPort);
        new Thread(floorSubsystem).start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            floorSubsystem.closeSocket();
            log("SchedulerSubsystem shutdown hook executed.");
        }));
    }
}
