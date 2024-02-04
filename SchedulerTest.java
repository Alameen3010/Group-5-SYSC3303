import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SchedulerTest {

    /**
     * Test class for Scheduler
     *
     * @author Taran Basati (101161332)
     * @version February 3, 2023
     */
    @Test
    void testGetFromFloor() {
        // Create mock Box instances
        Box mockBoxFloor = new Box();
        Box mockBoxElevator = new Box();

        // Create a Scheduler instance with the mock Box instances
        Scheduler scheduler = new Scheduler(mockBoxFloor, mockBoxElevator);

        // Prepare a test Box instance for the buffer
        Box testBox = new Box("10:30", 5, true, 3);

        // Call the getFromFloor method
        scheduler.getFromFloor();

        // Verify that the buffer is updated with the content of testBox
        assertEquals("10:30", scheduler.getBuffer().getTime());
        assertEquals(5, scheduler.getBuffer().getFloorNumber());
        assertTrue(scheduler.getBuffer().isDirectionUp());
        assertEquals(3, scheduler.getBuffer().getCarButtonNumber());
    }

    @Test
    void testSendToElevator() {
        // Create mock Box instances
        Box mockBoxFloor = new Box();
        Box mockBoxElevator = new Box();

        // Create a Scheduler instance with the mock Box instances
        Scheduler scheduler = new Scheduler(mockBoxFloor, mockBoxElevator);

        // Prepare a test Box instance for the buffer
        Box testBox = new Box("10:30", 5, true, 3);
        scheduler.setBuffer(testBox);

        // Call the sendToElevator method
        scheduler.sendToElevator();
    }

    @Test
    void testGetFromElevator() {
        // Create mock Box instances
        Box mockBoxFloor = new Box();
        Box mockBoxElevator = new Box();

        // Create a Scheduler instance with the mock Box instances
        Scheduler scheduler = new Scheduler(mockBoxFloor, mockBoxElevator);

        // Prepare a test Box instance for the buffer
        Box testBox = new Box("10:30", 5, true, 3);

        // Call the getFromElevator method
        scheduler.getFromElevator();

        // Verify that the buffer is updated with the content of testBox
        assertEquals("10:30", scheduler.getBuffer().getTime());
        assertEquals(5, scheduler.getBuffer().getFloorNumber());
        assertTrue(scheduler.getBuffer().isDirectionUp());
        assertEquals(3, scheduler.getBuffer().getCarButtonNumber());
    }

    @Test
    void testSendToFloor() {
        // Create mock Box instances
        Box mockBoxFloor = new Box();
        Box mockBoxElevator = new Box();

        // Create a Scheduler instance with the mock Box instances
        Scheduler scheduler = new Scheduler(mockBoxFloor, mockBoxElevator);

        // Prepare a test Box instance for the buffer
        Box testBox = new Box("10:30", 5, true, 3);
        scheduler.setBuffer(testBox);

        // Call the sendToFloor method
        scheduler.sendToFloor();
    }
}
