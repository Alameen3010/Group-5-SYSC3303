public class Floor implements Runnable {

    private Box sharedBox;

    public Floor(Box box)
    {
        this.sharedBox = box;
    }


    public void run() {
        for (int i = 0; i < 2; i++)
        {
            getFromCSV("Message ", i, false, i + 2); //Will extract from CSV and put it into a new attribute call it message;

            getFromSchedulerResponse();

        }
        //getFromCSV("Message 1"); // get from csv and puts it to the box
        //putFromFloorToScheduler();
        //getFromSchedulerResponse();
        System.exit(0);


    }

    public void getFromCSV(String time, int floorNumber, boolean directionUp, int carButtonNumber)
    {
        Box temp = new Box(time, floorNumber, directionUp, carButtonNumber);
        sharedBox.getFromSource(temp);
        //System.out.println("Sendind date to Scheduler");
        //sharedBox.getFromRecipient(contnent);
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
        Box temp = sharedBox.sendToSource();
        temp.printContents();
        //sharedBox.printContents();
        //System.out.println("received response from Scheduler");
    }
}
