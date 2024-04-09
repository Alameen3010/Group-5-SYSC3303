import javax.swing.*;

public class ElevatorManager {

    private Elevator[] elevators; // Array to manage Elevator objects

    // Constructor
    public ElevatorManager(int numberOfElevators, String schedulerIP, int schedulerPort) {
        elevators = new Elevator[numberOfElevators];
        for (int i = 0; i < numberOfElevators; i++) {
            elevators[i] = new Elevator(schedulerIP, schedulerPort, i + 1);
            new Thread(elevators[i]).start();
            // Register shutdown hook for each Elevator
            int finalI = i;
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                elevators[finalI].closeSocket();
                log("Elevator " + (finalI + 1) + " shutdown hook executed.");
                log(elevators[finalI].measurements());
            }));
        }
    }

    public Elevator getElevator(int index) {
        if (index >= 0 && index < elevators.length) {
            return elevators[index];
        } else {
            log("Elevator index out of bounds");
            return null;
        }
    }

    private static void log(String message) {
        System.out.println("[ElevatorManager] " + message);
    }

    // Main method to start the application
    public static void main(String[] args) {
        String schedulerIP = "localhost";
        int schedulerPort = 50000;
        int numberOfElevators = 4;

        // Initialize the ElevatorManager with the number of elevators, IP and port
        ElevatorManager elevatorManager = new ElevatorManager(numberOfElevators, schedulerIP, schedulerPort);

        // Start the GUI and pass the elevatorManager to it
        SwingUtilities.invokeLater(() -> {
            ElevatorUserInterface.createAndShowGUI(elevatorManager);
        });
    }


}
