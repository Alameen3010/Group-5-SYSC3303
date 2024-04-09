import javax.swing.*;
import java.awt.*;

public class ElevatorUserInterface {
    private static JPanel[] elevatorPanels; // Array to store elevator panels

    public static void main(String[] args) {
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
        frame.setSize(800, 600); // Adjusted the size to potentially accommodate more information

        // Create a main panel with a GridLayout to hold four elevator panels
        JPanel mainPanel = new JPanel(new GridLayout(1, 4));

        elevatorPanels = new JPanel[4]; // Initialize array to hold elevator panels

        for (int i = 0; i < 4; i++) {
            elevatorPanels[i] = createElevatorPanel("Elevator " + (i + 1), 0, 0, "Normal",0, "Closed"); // Initialize with zero faults and normal status
            mainPanel.add(elevatorPanels[i]);
        }

        frame.add(mainPanel);

        frame.setVisible(true);

        updateCurrentFloor(2, 7); // Update Elevator 3's current floor to 7
        updateCurrentFloor(1, 3);

        new ElevatorGUIUpdater(elevatorManager, elevatorPanels);
    }

    private static JPanel createElevatorPanel(String title, int currentFloor, int numTransientFaults, String status, int numPassengers, String doorStatus) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);
        panel.setPreferredSize(new Dimension(200, 250)); // Adjusted dimensions for potential additional info
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);

        JLabel floorLabel = new JLabel("Current Floor: " + currentFloor);
        floorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        floorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        floorLabel.setForeground(Color.WHITE);
        panel.add(floorLabel);

        JLabel directionLabel = new JLabel("Direction: ");
        directionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        directionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        directionLabel.setForeground(Color.WHITE);
        panel.add(directionLabel);

        JLabel faultsLabel = new JLabel("Transient Faults: " + numTransientFaults);
        faultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        faultsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        faultsLabel.setForeground(Color.WHITE);
        panel.add(faultsLabel);

        JLabel statusLabel = new JLabel("Status: " + status);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(Color.WHITE);
        panel.add(statusLabel);

        JLabel passengersLabel = new JLabel("Passengers: " + numPassengers);
        passengersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passengersLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        passengersLabel.setForeground(Color.WHITE);
        panel.add(passengersLabel);

        JLabel doorStatusLabel = new JLabel("Door Status: Closed " + doorStatus);
        doorStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        doorStatusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        doorStatusLabel.setForeground(Color.GREEN);
        panel.add(doorStatusLabel);



        panel.putClientProperty("floorLabel", floorLabel);
        panel.putClientProperty("directionLabel", directionLabel);
        panel.putClientProperty("passengerLabel", passengersLabel);
        panel.putClientProperty("doorStatusLabel", doorStatusLabel);

        return panel;
    }

    private static void updateCurrentFloor(int elevatorIndex, int newFloor) {
        JLabel floorLabel = (JLabel) elevatorPanels[elevatorIndex].getClientProperty("floorLabel");
        if (floorLabel != null) {
            floorLabel.setText("Current Floor: " + newFloor);
        }
    }

    public static void updateElevatorState(int elevatorIndex, int currentFloor, String direction, int transientFaults, String status, int numPassengers, String doorStatus) {
        JLabel floorLabel = (JLabel) elevatorPanels[elevatorIndex].getClientProperty("floorLabel");
        if (floorLabel != null) {
            floorLabel.setText("Current Floor: " + currentFloor);

            JLabel directionLabel = (JLabel) elevatorPanels[elevatorIndex].getClientProperty("directionLabel");
            if (directionLabel != null) {
                directionLabel.setText("Direction: " + direction);

                // Update the passenger count label
                JLabel passengerLabel = (JLabel) elevatorPanels[elevatorIndex].getClientProperty("passengerLabel");
                if (passengerLabel != null) {
                    passengerLabel.setText("Passengers: " + numPassengers);

                    // Update the door status label
                    JLabel doorStatusLabel = (JLabel) elevatorPanels[elevatorIndex].getClientProperty("doorStatusLabel");
                    if (doorStatusLabel != null) {
                        doorStatusLabel.setText("Door Status: " + doorStatus);
                    }
                }
            }
        }

        JLabel faultsLabel = (JLabel) elevatorPanels[elevatorIndex].getComponent(3);
        faultsLabel.setText("Transient Faults: " + transientFaults);

        JLabel statusLabel = (JLabel) elevatorPanels[elevatorIndex].getComponent(4);
        statusLabel.setText("Status: " + status);


    }


    // Added method to update measurements for a specific elevator panel
    public static void updateMeasurements(int elevatorIndex, String measurements) {
        JLabel measurementsLabel = (JLabel) elevatorPanels[elevatorIndex].getClientProperty("measurementsLabel");
        if (measurementsLabel == null) {
            measurementsLabel = new JLabel("<html>" + measurements.replace("\n", "<br>") + "</html>");
            measurementsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            measurementsLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            measurementsLabel.setForeground(Color.WHITE);

            elevatorPanels[elevatorIndex].add(measurementsLabel);
            elevatorPanels[elevatorIndex].putClientProperty("measurementsLabel", measurementsLabel);
        } else {
            measurementsLabel.setText("<html>" + measurements.replace("\n", "<br>") + "</html>");
        }

        elevatorPanels[elevatorIndex].revalidate();
        elevatorPanels[elevatorIndex].repaint();
    }
}
