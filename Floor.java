/**
 * 
 * @author Rozba Hakam (101190098)
 * @author Ilyes Outaleb (101185290)
 * @version 2024-02-03
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Floor implements Runnable {

    private Box sharedBox;

    public Floor(Box box)
    {
        this.sharedBox = box;
    }

    public void run()
    {
        readFileAndSendRequests("Requests.txt");
        System.exit(0);
    }

    public void readFileAndSendRequests(String filename)
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

    private void createBoxFromCSV(String line)
    {
        // Parse the CSV line and create a Box object
        String[] data = line.split(",");
        String time = data[0].trim();
        int floorNumber = Integer.parseInt(data[1].trim());
        String direction = data[2].trim();
        int carButtonNumber = Integer.parseInt(data[3].trim());
        boolean directionUp;

        // ternary Operator to assign the direction up a boolean value
        directionUp = (direction.equals("Up")) ? true : false;

        getFromCSV(time, floorNumber, directionUp, carButtonNumber);
    }

    public void getFromCSV(String time, int floorNumber, boolean directionUp, int carButtonNumber)
    {
        Box temp = new Box(time, floorNumber, directionUp, carButtonNumber);
        sharedBox.getFromSource(temp);
    }

    public void getFromSchedulerResponse()
    {
        Box temp = sharedBox.sendToSource();
        temp.printContents();
    }
}

