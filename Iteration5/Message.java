/**
 * @author Ilyes Outaleb (101185290)
 * @version March 09, 2024
  * Edited: Taran Basati
 * @version March 20, 2024
 * Edited: Ilyes Outaleb
 * @version April 8 2024
 */

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable{

    private String date;

    private int source;

    private String direction;

    private int destination;

    private int door_fault; // transient fault

    private int elevator_fault; // system fault
    private boolean buffer; // Anything can be used here



    public Message(String date, int source, String direction, int destination, boolean confirmation, int door_fault, int elevator_fault)
    {
        this.date = date;
        this.source = source;
        this.direction = direction;
        this.destination = destination;
        this.buffer = confirmation;
        this.door_fault = door_fault;
        this.elevator_fault = elevator_fault;

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

    public boolean getBuffer()
    {
        return this.buffer;
    }

    public void setBuffer(boolean new_buffer)
    {
        this.buffer = new_buffer;
    }

    public int getDoor_fault()
    {
        return this.door_fault;
    }

    public void setDoor_fault(int door_fault)
    {
        this.door_fault = door_fault;
    }

    public int getElevator_fault()
    {
        return this.elevator_fault;
    }

    public void setElevator_fault(int elevator_fault)
    {
        this.elevator_fault = elevator_fault;
    }

    public void printMessage()
    {
        System.out.println("Message contains the following:");
        System.out.println("Date: " + this.date);
        System.out.println("Source: " + this.source);
        System.out.println("Direction: " + this.direction);
        System.out.println("Destination: " + this.destination);
        System.out.println("Has been read: " + this.buffer);
    }

    public void horizontalPrint()
    {
        //System.out.println("Message contains the following:");
        System.out.println(this.date + "," + this.source + "," + this.direction + "," + this.destination + "," + this.door_fault
                + "," + this.elevator_fault);

    }

    public void horizontalPrint(int elevatorNumber)
    {

        System.out.println(this.date + "," + this.source + "," + this.direction + "," + this.destination + "," + this.door_fault + ","
                +  this.elevator_fault + "[Elevator " + elevatorNumber + "]");

    }

    @Override
    public String toString()
    {
        return this.date + "," + this.source + "," + this.direction + "," + this.destination + "," + this.door_fault
                + "," + this.elevator_fault;

    }
}
