
    /**
     * @author Al-ameen Alliu, 101159780
     *The thread for the elevator class where data is collected from the scheduler,
     *processed and sent back o the scheduler
     */
    public class Elevator implements Runnable {
        private Box boxFromScheduler; // Box used to receive messages from the Scheduler
        private Box boxToScheduler; // Box used to send messages to the Scheduler
        private int currentFloor; // The current floor where the elevator is

        public Elevator(Box boxFromScheduler, Box boxToScheduler) {
            this.boxFromScheduler = boxFromScheduler;
            this.boxToScheduler = boxToScheduler;
            this.currentFloor = 1; // Starting on the first floor
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                // Assume Box class has synchronized get/set methods
                Integer floorRequest = boxFromScheduler.getFloorNumber();

                //Boolean directionRequest = boxFromScheduler.getDirection();
                //String timeRequest = boxFromScheduler.getTime();

                if (floorRequest != null) {
                    processFloorRequest(floorRequest);
                    boxFromScheduler.setFloorNumber(null); // Clear the request after processing
                }

                try {
                    Thread.sleep(1000); //
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        private void processFloorRequest(Integer floorRequest) {
            moveToFloor(floorRequest);
            // After moving, send confirmation back to Scheduler
            boxToScheduler.setCarButtonNumber(currentFloor);
        }

        private void moveToFloor(int floor) {

            this.currentFloor = floor; // Set the current floor to the requested floor
        }

        // Get and Set methods for currentFloor for possible external access
        public synchronized int getCurrentFloor() {
            return currentFloor;
        }

        public synchronized void setCurrentFloor(int currentFloor) {
            this.currentFloor = currentFloor;
        }
    }


    /**
     *   class Box {
     *         private Integer floorNumber;
     *         private Boolean direction;
     *         private Integer carButtonNumber;
     *         private String time; // Time in the format "hh:mm:ss.mmm"
     *
     *
     *         // Synchronized getter and setter for floorNumber
     *         public synchronized Integer getFloorNumber() {
     *             return floorNumber;
     *         }
     *
     *         public synchronized void setFloorNumber(Integer floorNumber) {
     *             this.floorNumber = floorNumber;
     *         }
     *
     *             // Synchronized getter and setter for direction
     *             public synchronized Boolean getDirection() {
     *                 return direction;
     *             }
     *
     *             public synchronized void setDirection(Boolean direction) {
     *                 this.direction = direction;
     *             }
     *
     *             // Synchronized getter and setter for carButtonNumber
     *             public synchronized Integer getCarButtonNumber() {
     *                 return carButtonNumber;
     *             }
     *
     *             public synchronized void setCarButtonNumber(Integer carButtonNumber) {
     *                 this.carButtonNumber = carButtonNumber;
     *             }
     *         // Synchronized getter for time
     *         public synchronized String getTime() {
     *             return time;
     *         }
     *
     *         // Synchronized setter for time
     *         public synchronized void setTime(String time) {
     *             this.time = time;
     *
     *         }
     *
     *     }
     */
