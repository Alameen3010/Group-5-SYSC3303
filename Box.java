/**
 * The Box class is an object that is shared between two subsystem to communicate. It represents a message flow from
 * one end to another. Its responsible for sychrnonizing its access by the two threads. One is
 * @author Ilyes Outaleb (101185290)
 * @version February 3 2023
 */

public class Box {

    private String time;

    private int floorNumber;

    private boolean directionUp;

    private int carButtonNumber;

    private int state = 0;

    public Box()
    {

    }

    public Box(String time, int floorNumber, boolean directionUp, int carButtonNumber)
    {
        this.time = time;
        this.floorNumber = floorNumber;
        this.directionUp = directionUp;
        this.carButtonNumber = carButtonNumber;

    }
    public void printContents()
    {   System.out.println("========== Contents of Response Message ==================");
        System.out.println("Time: " + this.time);
        System.out.println("Floor Number: " + this.floorNumber);
        System.out.println("Is Direction UP: " + this.directionUp);
        System.out.println("Car Button Number: " + this.carButtonNumber);
        System.out.println("========== End of Contents of Response Message ==================");
        //System.out.println("Floor#: " + this.floorNumber);
        //System.out.println("Time: " + this.time);
        //System.out.println("direction: " + this.direction);
        //System.out.println("carButtonNumber " + this.carButtonNumber);
    }

    public synchronized void getFromSource(Box content)
    {   //System.out.println(this.state + Thread.currentThread().getName());
        while(this.state != 0)
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
        this.carButtonNumber = content.carButtonNumber;
        this.state = 1;
        System.out.println("Data was sent from " + Thread.currentThread().getName());
        notifyAll();
    }

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
        Box temp = new Box(this.time, this.floorNumber, this.directionUp, this.carButtonNumber);
        this.state = 2;
        System.out.println("Data is received by " + Thread.currentThread().getName());
        notifyAll();
        return temp;

    }

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
        this.state = 0;
        System.out.println("Data is received from " + Thread.currentThread().getName());
        notifyAll();
        return temp;

    }
}

