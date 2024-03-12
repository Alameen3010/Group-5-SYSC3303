/**
 * The Floor class represents a floor in a building and implements the Runnable interface, allowing it to be used
 * as a thread. It reads requests from a csv/txt file, processes them, and communicates with a shared Box object
 * that is shared with scheduler.
 * @author Rozba Hakam (101190098)
 * @author Ilyes Outaleb (101185290)
 * @version 2024-02-03
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Floor implements Runnable {

    /* The sharedBox is a reference to the Box object shared with the scheduler. */
    private Box sharedBox;

    /**
     * Constructor for the Floor class, initializes the sharedBox reference.
     *
     * @param box The Box object shared with the scheduler.
     */
    public Floor(Box box)
    {
        this.sharedBox = box;
    }

    /**
     * The run method is called when a floor is executed as a thread. It reads requests
     * from a file, processes them, and terminates the program afterward.
     */
    public void run()
    {
        readFileAndSendRequests("Requests.txt");
        System.exit(0);
    }

    /**
     * Reads requests from a csv/txt file, processes each line, and interacts with the shared Box.
     *
     * @param filename The name of the file containing the requests.
     */
    private void readFileAndSendRequests(String filename)
    {
        try {
            File requestFile = new File(filename);
            Scanner scanner = new Scanner(requestFile);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                createBoxFromCSV(line);
                getFromSchedulerResponse();
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }

    /**
     * divide each CSV/txt line, creates a Box object from the data, and sends it to the shared Box.
     *
     * @param line The CSV line stores the requested information.
     */
    private void createBoxFromCSV(String line)
    {
        /* Parse the CSV line and create a Box object */
        String[] data = line.split(",");
        String time = data[0].trim();
        int floorNumber = Integer.parseInt(data[1].trim());
        String direction = data[2].trim();
        int carButtonNumber = Integer.parseInt(data[3].trim());
        boolean directionUp;

        /* ternary Operator to assign the direction up a boolean value */
        directionUp = (direction.equals("Up")) ? true : false;

        getFromCSV(time, floorNumber, directionUp, carButtonNumber);
    }

    /**
     * Creates a Box object with the provided message information and sends it to the shared Box.
     *
     * @param time The time of the request.
     * @param floorNumber The floor number of the request.
     * @param directionUp The direction of the request (Up = true) or (Down = false).
     * @param carButtonNumber The car button number associated with the request.
     */
    private void getFromCSV(String time, int floorNumber, boolean directionUp, int carButtonNumber)
    {
        Box temp = new Box(time, floorNumber, directionUp, carButtonNumber);
        sharedBox.getFromSource(temp);
    }

    /**
     * gets a Box object from the shared Box and prints its contents.
     */
    private void getFromSchedulerResponse()
    {
        Box temp = sharedBox.sendToSource();
        temp.printContents();
    }
}

