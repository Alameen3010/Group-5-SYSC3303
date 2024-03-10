/**
 * @author Ilyes Outaleb (101185290)
 * @version March 09, 2024
 *
 */

import java.io.Serializable;

public class Message implements Serializable{

    private String date;

    private int source;

    private String direction;

    private int destination;

    private boolean confirmation;

    public Message(String date, int source, String direction, int destination, boolean confirmation)
    {
        this.date = date;
        this.source = source;
        this.direction = direction;
        this.destination = destination;
        this.confirmation = confirmation;

    }
    public String getDate()
    {
        return date;
    }

    public void setDate(String new_date)
    {
        this.date = new_date;
    }

    public int getSource()
    {
        return this.source;
    }

    public void setSource(int new_source)
    {
        this.source = new_source;
    }
    public String getDirection()
    {
        return this.direction;
    }

    public void setDirection(String new_direction)
    {
        this.direction = new_direction;
    }
    public int getDestination()
    {
        return this.destination;
    }

    public void setDestination(int new_destination)
    {
        this.destination = new_destination;
    }

    public boolean getConfirmation()
    {
        return this.confirmation;
    }

    public void setConfirmation(boolean new_confirmation)
    {
        this.confirmation = new_confirmation;
    }

    public void printMessage()
    {
        System.out.println("Message contains the following:");
        System.out.println("Date: " + this.date);
        System.out.println("Source: " + this.source);
        System.out.println("Direction: " + this.direction);
        System.out.println("Destination: " + this.destination);
        System.out.println("Has been read: " + this.confirmation);
    }
}
