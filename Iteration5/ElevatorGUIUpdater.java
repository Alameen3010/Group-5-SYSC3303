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
        // Set up a timer to call actionPerformed every second
        this.timer = new Timer(1000, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // For each elevator, get the latest state and update the UI
        for (int i = 0; i < elevatorPanels.length; i++) {
            Elevator elevator = elevatorManager.getElevator(i); // This should be i, not i + 1
            if (elevator != null) {
                // Here you would retrieve the state from the elevator object
                int currentFloor = elevator.getCurrentFloor();
                String direction = elevator.getDirection();
                int transientFaults = elevator.getTransientFaults();
                String status = elevator.getStatus();

                //

                // Now, update the UI with the retrieved state
                final int finalI = i;
                SwingUtilities.invokeLater(() -> {
                    ElevatorUserInterface.updateElevatorState(finalI, currentFloor, direction, transientFaults, status);
                });
            }
        }
    }
}

