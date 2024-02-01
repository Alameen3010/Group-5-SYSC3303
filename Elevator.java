
    /**
     *The thread for the elevator class where data is collected from the scheduler,
     *processed and sent back o the scheduler
     */

    public class Elevator implements Runnable {
        private Box boxFromScheduler; //Box used to receive message from the Schedule
        private Box boxToScheduler; // Box used to communicate with the Scheduler


        public Elevator(Box boxFromScheduler, Box boxToScheduler) {
            this.boxFromSchduler = boxFromScheduler;
            this.boxToScheduler = boxToScheduler;
            this.currentFloor = 1;

        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                // Check for new messages from the Scheduler
                EventData eventData = boxFromScheduler.getEventData();

                if (eventData != null) {
                    // Process the message and take action
                    processCommand(eventData);

                    // After processing, send a response back to the Scheduler
                    ProcessedData processedData = new ProcessedData(/* data */);
                    boxToScheduler.setProcessedData(processedData);
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        private void processCommand(EventData eventData) {
            // For example, if the command is to move to a floor
            moveToFloor(eventData.getFloorNumber());
        }

        private void moveToFloor(int floor) {
            // Logic to move the elevator to the specified floor
            this.currentFloor = floor; // Update the current floor


        }

        // Additional methods as needed...

    }
