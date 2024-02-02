public class Floor implements Runnable{

    private Integer floorNumber;

    // This is the second client. Which simulates the elevator
    private String time;

    // This is the message buffer that will be used by both floor and elevator.
    private boolean direction;

    //
    private Integer carButtonNumber;

    private Box sharedBox;

    //private boolean processed;
    public Floor (Box sharedBox)
    {
        this.sharedBox = sharedBox;
        floorNumber = 2;
        time = "2024-02-01";
        direction = false;
        carButtonNumber = 4;

    }

    public void run()
    {
        get();
        put();

        System.exit(0);


    }

    public synchronized void get()
    {
        // this will retrieve from csv

        while(sharedBox.getClientStatus()) // while no request sent
        {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }

        System.out.println("Extracted data from csv file");
        sharedBox.floorNumber = floorNumber;
        sharedBox.time = time;
        sharedBox.direction = direction;
        sharedBox.carButtonNumber = carButtonNumber;
        System.out.println("Sent it to the scheduler box");
        sharedBox.setClientStatus(true);
        //this.processed = true;
        System.out.println("from floor " + sharedBox.getClientStatus());
        notifyAll();
    }


    public synchronized void put()
    {
        /*
        while(!processed)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        */



        System.out.println("Received data from the scheduler box");
        sharedBox.printContents();
        sharedBox.setClientStatus(false);
        //this.processed = false;
        System.out.println("Finished moving on the next input");
        notifyAll();
    }

}
