import org.w3c.dom.html.HTMLImageElement;

public class Box {
    /**
     * This will be the box class acting as a message channel between two adjacent threads (subsystem)
     * @Version January 30 2023
     *
     */
    public Integer floorNumber;

    // This is the second client. Which simulates the elevator
    public String time;

    // This is the message buffer that will be used by both floor and elevator.
    public boolean direction;

    //
    public Integer carButtonNumber;

    private boolean clientSentRequest; // "Request" "No Request"

    private boolean serverResponded; // "Read Request" "Provided Response"


    public Box()
    {
        clientSentRequest = false; // initialize too true to begin process.
        serverResponded = false;
    }

    public void printContents()
    {
        System.out.println("Items Present:");
        System.out.println("Floor#: " + this.floorNumber);
        System.out.println("Time: " + this.time);
        System.out.println("direction: " + this.direction);
        System.out.println("carButtonNumber " + this.carButtonNumber);
    }

    public boolean getClientStatus()
    {
        return clientSentRequest;
    }

    public void setClientStatus(boolean status)
    {
        clientSentRequest = status;
    }

    public boolean getServerStatus()
    {
        return serverResponded;
    }

    public void setServerStatus(boolean status)
    {
        serverResponded = status;

    }
}
