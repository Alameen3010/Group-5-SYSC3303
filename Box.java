/**
 * The Box class is an object that is shared between two subsystem to communicate. It represents a message flow from
 * one end to another. It's responsible for synchronizing its access by the two threads. There will be two instances
 * of this class one for each end of the scheduler.
 * @author Ilyes Outaleb (101185290)
 * @version February 3, 2023
 */

public class Box {

    /* Attributes of Box which will the different components of the message sent between subsystems */
    private String time;

    private int floorNumber;

    private boolean directionUp;

    private int carButtonNumber;
    
    /*This variable allows to synchronize all the incoming and outgoing messages from box. There are 4 states.*/
    // 0 -> nothing(default) or message to Source
    // 1 -> message from source
    // 2 -> message to destination
    // 3 -> message from destination
    // Only one method can be executed for each state and all states will be executed for a cycle.
    private int state = 0;

    
    /**
     *
     * There are two constructors for the Box class. This is the default one.
     */
    public Box()
    {

    }

    /**
     *
     * There are two constructors for the Box class. This initializes all the components of the message.
     * @param time A string representing the time the request was made.
     * @param floorNumber An integer representing the floor of which the request was made.
     * @param directionUp A boolean representing whether the direction is up or down.
     * @param carButtonNumber An integer representing the destination floor or the button that has been clicked on the
     *                        elevator.
     */
    public Box(String time, int floorNumber, boolean directionUp, int carButtonNumber)
    {
        this.time = time;
        this.floorNumber = floorNumber;
        this.directionUp = directionUp;
        this.carButtonNumber = carButtonNumber;

    }

    /**
     *
     * Method that can be used at any stage of the communication process to see what each box contains at a given
     * step. Used for the final step to display the response of the system to the floor request.
     */
    public void printContents()
    {   System.out.println("========== Contents of Response Message ==================");
        System.out.println("Time: " + this.time);
        System.out.println("Floor Number: " + this.floorNumber);
        System.out.println("Is Direction UP: " + this.directionUp);
        System.out.println("Car Button Number: " + this.carButtonNumber);
        System.out.println("========== End of Contents of Response Message ==================");
    }

    /**
     *
     * Method associated with state 0 where there is a message from the source. It assigns the content to the message
     * elements.
     * @param content A box with a message to be sent.
     */
    public synchronized void getFromSource(Box content)
    {
        while(this.state != 0)                          /*Send to Wait unless at state 0 */
        {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        /*Assigning this box the contents of the message */
        this.time = content.time;
        this.floorNumber = content.floorNumber;
        this.directionUp = content.directionUp;
        this.carButtonNumber = content.carButtonNumber;

        this.state = 1;                                             /* Now ready to move to state 1*/
        System.out.println("Data was sent from " + Thread.currentThread().getName());           /* Print Transition*/
        notifyAll();                                                /* Notify all threads who are waiting */
    }
    /**
     *
     * Method associated with state 1 where a message is sent to the destination.
     * @return Returns a Box object containing the message sent by the source.
     */
    public synchronized Box sendToDestination()
    {   //System.out.println(this.state + Thread.currentThread().getName());

        while(this.state != 1)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        /* A temporary box object to send the content of the object */
        Box temp = new Box(this.time, this.floorNumber, this.directionUp, this.carButtonNumber);

        this.state = 2;
        System.out.println("Data is received by " + Thread.currentThread().getName());
        notifyAll();
        return temp;
    }

    /**
     *
     * Method associated with state 2 where a response is received from the destination.
     * @param content A Box which contains the message of the response.
     */
    public synchronized void getFromDestination(Box content)
    {
        while(this.state != 2)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        this.time = content.time;
        this.floorNumber = content.floorNumber;
        this.directionUp = content.directionUp;
        this.carButtonNumber = carButtonNumber;
        
        this.state = 3;
        System.out.println("Data was sent from " + Thread.currentThread().getName());
        notifyAll();
    }

    /**
     *
     * Method associated with state 3 where a response is sent to the source.
     * @return  A Box which contains the message of the response to the source.
     */
    public synchronized Box sendToSource()
    {
        while(this.state != 3)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        Box temp = new Box(this.time, this.floorNumber, this.directionUp, this.carButtonNumber);
        
        /* This signifies that the communication is IDLE and goes back to 0 waiting for a new message to be sent */
        //by the source.
        this.state = 0; 
        
        System.out.println("Data is received from " + Thread.currentThread().getName());
        notifyAll();
        return temp;
    }

    // *The following methods were added for testing purposes*

    /**
     * getter for Time
     * @return String, Time
     */
    public String getTime() {
        return time;
    }

    /**
     * getter for Floor Number
     * @return int, floor number
     */
    public int getFloorNumber() {
        return floorNumber;
    }

    /**
     * getter for direction
     * @return bool, direction of elevator
     */
    public boolean isDirectionUp() {
        return directionUp;
    }

    /**
     * getter for button number
     * @return int, elevator button number
     */
    public int getCarButtonNumber() {
        return carButtonNumber;
    }

    /**
     * getter for state
     * @return int, State
     */
    public int getState() {
        return state;
    }
}
