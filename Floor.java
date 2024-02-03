public class Floor implements Runnable {

    private Box sharedBox;

    public Floor(Box box)
    {
        this.sharedBox = box;
    }


    public void run() {
        for (int i = 0; i < 2; i++)
        {
            getFromCSV("Message " + i);
            getFromSchedulerResponse();

        }
        //getFromCSV("Message 1"); // get from csv and puts it to the box
        //putFromFloorToScheduler();
        //getFromSchedulerResponse();



    }

    public void getFromCSV(String contnent)
    {   //System.out.println("Sendind date to Scheduler");
        sharedBox.getFromRecipient(contnent);
        //System.out.println("Sendind date to Scheduler");

    }
    /*
    public void putFromFloorToScheduler()
    {
        schduler.getFromFloor(this.floorBox.getContent()); // sends a message to scheduler


    }
    */
    public void getFromSchedulerResponse()
    {   //System.out.println("received response from Scheduler");
        System.out.println(sharedBox.sendToRecipient());
        //System.out.println("received response from Scheduler");
    }
}
