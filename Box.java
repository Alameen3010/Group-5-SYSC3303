public class Box {

    private String content;

    private int state = 0;

    public void printContents()
    {
        System.out.println("Items Present:");
        System.out.println(this.content);
        //System.out.println("Floor#: " + this.floorNumber);
        //System.out.println("Time: " + this.time);
        //System.out.println("direction: " + this.direction);
        //System.out.println("carButtonNumber " + this.carButtonNumber);
    }

    public synchronized void getFromRecipient(String content)
    {   //System.out.println(this.state + Thread.currentThread().getName());
        while(this.state != 0)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        this.content = content;
        this.state = 1;
        notifyAll();
    }

    public synchronized String sendToDestination()
    {   //System.out.println(this.state + Thread.currentThread().getName());
        while(this.state != 1)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        this.state = 2;
        notifyAll();
        return this.content;

    }

    public synchronized void getFromDestination(String content)
    {
        while(this.state != 2)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        this.content = content;
        this.state = 3;
        notifyAll();

    }

    public synchronized String sendToRecipient()
    {
        while(this.state != 3)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        this.state = 0;
        notifyAll();
        return this.content;

    }
}
