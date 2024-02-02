

public class Scheduler implements Runnable{

    private Integer floorNumber;

    // This is the second client. Which simulates the elevator
    private String time;

    // This is the message buffer that will be used by both floor and elevator.
    private boolean direction;

    //
    private Integer carButtonNumber;




    private Box sharedBox;
    public Scheduler (Box sharedBox)
    {
        this.sharedBox = sharedBox;

    }

    public void run()
    {

        get();
        put();

    }

    public synchronized void get()
    {
        // this will retrieve from csv
        //System.out.println("from scheduler " + sharedBox.getClientStatus());
        //System.out.println("from scheduler " + sharedBox.getClientStatus());
        //System.out.println("from scheduler " + sharedBox.getClientStatus());
        //System.out.println("from scheduler " + sharedBox.getClientStatus());
        while(!sharedBox.getClientStatus())
        {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        System.out.println("Received a message from the floor box");
        floorNumber = sharedBox.floorNumber;
        time = sharedBox.time;
        direction = sharedBox.direction;
        carButtonNumber = sharedBox.carButtonNumber;
        //sharedBox.serverResponded = false;
        System.out.println("Saving it to the buffer");
        notifyAll();
    }

    public synchronized void put()
    {   /*
        while(sharedBox.getServerStatus())
        {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        */
        System.out.println("Finished processing the data");
        sharedBox.carButtonNumber = carButtonNumber;
        sharedBox.time = time;
        sharedBox.direction = direction;
        sharedBox.floorNumber = floorNumber;
        sharedBox.setServerStatus(true);
        System.out.println("Sending the new data to the floor Box");
        //notifyAll();
    }



}

