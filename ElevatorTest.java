import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for Elevator
 *
 * @author Taran Basati (101161332)
 * @version February 3, 2023
 */
class ElevatorTest {

    @Test
    void testReceiveFromScheduler() {
        // Create a mock Box
        Box mockBox = new Box();

        // Create an Elevator instance with the mock Box
        Elevator elevator = new Elevator(mockBox);

        // Create a Box instance for testing
        Box testBox = new Box("10:30", 5, true, 3);

        // Verify that the buffer is updated with the content of testBox
        assertEquals("10:30", elevator.getBuffer().getTime());
        assertEquals(5, elevator.getBuffer().getFloorNumber());
        assertTrue(elevator.getBuffer().isDirectionUp());
        assertEquals(3, elevator.getBuffer().getCarButtonNumber());
    }

    @Test
    void testSendToSchedulerResponse() {
        // Create a mock Box
        Box mockBox = new Box();

        // Create an Elevator instance with the mock Box
        Elevator elevator = new Elevator(mockBox);

        // Create a Box instance for testing
        Box testBox = new Box("10:30", 5, true, 3);

        // Set the buffer of the elevator to the testBox
        elevator.setBuffer(testBox);

    }
}
