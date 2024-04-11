/**
 * Script which will be responsible for providing updates. Will give the real time component to the GUI.
 *
 * @author Al-ameen Alliu, Ali Zaid
 * @version April 7, 2024,
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ElevatorGUIUpdater implements ActionListener {
    private final ElevatorManager elevatorManager;
    private final JPanel[] elevatorPanels;
    private final Timer timer;

    public ElevatorGUIUpdater(ElevatorManager elevatorManager, JPanel[] elevatorPanels) {
        this.elevatorManager = elevatorManager;
        this.elevatorPanels = elevatorPanels;
        this.timer = new Timer(1000, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < elevatorPanels.length; i++) {
            Elevator elevator = elevatorManager.getElevator(i); // Assuming getElevator(i) correctly fetches the elevator instance
            if (elevator != null) {
                int currentFloor = elevator.getCurrentFloor();
                String direction = elevator.getDirection();
                int transientFaults = elevator.getTransientFaults();
                String status = elevator.getStatus();
                int numPassengers = elevator.getNumberOfPassengers();
                String doorStatus = elevator.getDoorStatus();

                // Fetch measurements from the elevator instance
                String measurements = elevator.measurements(); // Assuming this method exists and returns the formatted measurement data

                // Use the index 'i' which is effectively final within this loop block
                int finalI = i;

                SwingUtilities.invokeLater(() -> {
                    // Update the state information
                    ElevatorUserInterface.updateElevatorState(finalI, currentFloor, direction, transientFaults, status,numPassengers, doorStatus);

                    // Update the measurement information
                    ElevatorUserInterface.updateMeasurements(finalI, measurements);
                });
            }
        }
    }
}
