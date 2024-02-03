public class Scheduler implements Runnable{

    private Box sharedBox;
    private Box sharedBox2;
    private Box buffer;

    public Scheduler(Box box, Box box2)
    {
        this.sharedBox = box;
        this.sharedBox2 = box2;
    }

    public void run() {
        while(true){

            getFromFloor();
            sendToElevator();
            getFromElevator();
            sendToFloor();
        }
    }

    private void getFromFloor()
    {   //System.out.println("received message from Floor");
        this.buffer = sharedBox.sendToDestination();
        //System.out.println("received message from Floor");
    }

    private void sendToElevator()
    {
        sharedBox2.getFromSource(this.buffer);
    }

    private void getFromElevator()
    {
        this.buffer = sharedBox2.sendToSource();
    }

    private void sendToFloor()
    {   //System.out.println("Sending message to Floor");
        sharedBox.getFromDestination(this.buffer);
        //System.out.println("Sending message to Floor");

    }


}
