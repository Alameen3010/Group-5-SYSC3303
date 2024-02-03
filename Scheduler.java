public class Scheduler implements Runnable{

    private Box sharedBox;

    private String buffer;

    public Scheduler(Box box)
    {
        this.sharedBox = box;
    }

    public void run() {
        for (int i = 0; i < 2; i++) {
            getFromFloor();
            sendToFloor();
        }
    }

    private void getFromFloor()
    {   //System.out.println("received message from Floor");
        this.buffer = sharedBox.sendToDestination();
        //System.out.println("received message from Floor");
    }

    private void sendToFloor()
    {   //System.out.println("Sending message to Floor");
        sharedBox.getFromDestination(buffer);
        //System.out.println("Sending message to Floor");

    }


}
