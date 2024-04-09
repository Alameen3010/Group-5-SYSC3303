import javax.swing.*;
import java.awt.*;

public class ElevatorUserInterface {
    private static JPanel[] elevatorPanels; // Array to store elevator panels

    public static void main(String[] args) {
        // You need to pass the correct parameters to the ElevatorManager constructor.
        // For example, if ElevatorManager's constructor requires an IP address and a port, you should pass those:
        String schedulerIP = "localhost"; // or whatever the IP should be
        int schedulerPort = 50000; // or whatever port should be used
        int numberOfElevators = 4; // if you need to specify the number of elevators

        ElevatorManager elevatorManager = new ElevatorManager(numberOfElevators, schedulerIP, schedulerPort);

        SwingUtilities.invokeLater(() -> {
            // Create and show the GUI
            createAndShowGUI(elevatorManager);
        });
    }


    public static void createAndShowGUI(ElevatorManager elevatorManager) {
        JFrame frame = new JFrame("Elevator Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 300);

        // Create a main panel with a GridLayout to hold four elevator panels
        JPanel mainPanel = new JPanel(new GridLayout(1, 4));

        elevatorPanels = new JPanel[4]; // Initialize array to hold elevator panels

        // Create four elevator panels and add them to the main panel
        for (int i = 0; i < 4; i++) {
            elevatorPanels[i] = createElevatorPanel("Elevator " + (i + 1), 0, 0, "Normal"); // Initialize with zero faults and normal status
            mainPanel.add(elevatorPanels[i]);
        }

        // Add the main panel to the frame
        frame.add(mainPanel);

        // Display the frame
        frame.setVisible(true);

        // Simulate updating the current floor to 7 for Elevator 3 (index 2 in zero-based index)
        updateCurrentFloor(2, 7); // Update Elevator 3's current floor to 7
        updateCurrentFloor(1, 3);

        new ElevatorGUIUpdater(elevatorManager, elevatorPanels);
    }

    // Method to create an elevator panel
    private static JPanel createElevatorPanel(String title, int currentFloor, int numTransientFaults, String status) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);
        panel.setPreferredSize(new Dimension(100, 100));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // Elevator number label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);

        // Current floor label
        JLabel floorLabel = new JLabel("Current Floor: " + currentFloor);
        floorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        floorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        floorLabel.setForeground(Color.WHITE);
        panel.add(floorLabel);

        // Direction label
        JLabel directionLabel = new JLabel("Direction: ");
        directionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        directionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        directionLabel.setForeground(Color.WHITE);
        panel.add(directionLabel);

        // Number of transient faults label
        JLabel faultsLabel = new JLabel("Transient Faults: " + numTransientFaults);
        faultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        faultsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        faultsLabel.setForeground(Color.WHITE);
        panel.add(faultsLabel);

        // Status label
        JLabel statusLabel = new JLabel("Status: " + status);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(Color.WHITE);
        panel.add(statusLabel);

        // Store the references to the floor and direction labels in the client property of the panel for later reference
        panel.putClientProperty("floorLabel", floorLabel);
        panel.putClientProperty("directionLabel", directionLabel);

        return panel;
    }

    // Method to update current floor for a specific elevator panel
    private static void updateCurrentFloor(int elevatorIndex, int newFloor) {
        // Get the current floor label for the specified elevator
        JLabel floorLabel = (JLabel) elevatorPanels[elevatorIndex].getClientProperty("floorLabel");
        if (floorLabel != null) {
            // Update the text of the current floor label
            floorLabel.setText("Current Floor: " + newFloor);
        }
    }

    // Method to update the elevator state
    public static void updateElevatorState(int elevatorIndex, int currentFloor, String direction, int transientFaults, String status) {
        // Get the current floor label for the specified elevator
        JLabel floorLabel = (JLabel) elevatorPanels[elevatorIndex].getClientProperty("floorLabel");
        if (floorLabel != null) {
            // Update the text of the current floor label
            floorLabel.setText("Current Floor: " + currentFloor);

            // Get the direction label for the specified elevator
            JLabel directionLabel = (JLabel) elevatorPanels[elevatorIndex].getClientProperty("directionLabel");
            if (directionLabel != null) {
                // Update the text of the direction label
                directionLabel.setText("Direction: " + direction);
            }
        }

        // Update the transient faults and status labels
        JLabel faultsLabel = (JLabel) elevatorPanels[elevatorIndex].getComponent(3);
        faultsLabel.setText("Transient Faults: " + transientFaults);

        JLabel statusLabel = (JLabel) elevatorPanels[elevatorIndex].getComponent(4);
        statusLabel.setText("Status: " + status);
    }
}