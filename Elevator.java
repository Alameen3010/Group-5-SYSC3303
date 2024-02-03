
    /**
     * @author Al-ameen Alliu, 101159780
     *The thread for the elevator class where data is collected from the scheduler,
     *processed and sent back o the scheduler
     */
    public class Elevator implements Runnable {

        private Box sharedBox;
        private int currentFloor;

        public Elevator(Box sharedBox) {
            this.sharedBox = sharedBox;
            this.currentFloor = 1; // Assume the elevator starts at floor 1
        }

        public void run() {
            while (true) {
                get();
                put();
            }
        }

        public synchronized void get() {
            while (!sharedBox.getSchedulerStatus()) { // Wait for the scheduler to send a command
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            // Retrieve data from the shared box
            Integer requestedFloor = sharedBox.getFloorNumber();
            if (requestedFloor != null) {
                moveToFloor(requestedFloor);
            }
            sharedBox.setSchedulerStatus(false); // Reset the status to indicate the command has been processed
            notifyAll();
        }

        public synchronized void put() {

            sharedBox.setFloorNumber(currentFloor);
            sharedBox.setElevatorStatus(true); // Indicate that the Elevator has updated its status
            notifyAll();
        }

        private void moveToFloor(int floor) {
            // Simulate the action of moving to a floor
            System.out.println("Moving to floor " + floor);
            this.currentFloor = floor; // Update the current floor
        }
    }
