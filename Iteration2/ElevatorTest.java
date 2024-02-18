import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Elevator
 *
 * @author Taran Basati (101161332)
 * @version February 17, 2023
 */
public class ElevatorTest {

    private Elevator elevator;
    private InputStream stdin;

    /**
     * Setup method that initializes Elevator objects for the upcoming tests
     */
    @BeforeEach
    void setUp() {
        elevator = new Elevator();
        elevator.currentFloor = 0;
        stdin = System.in; // Save the standard input stream
    }

    @Test
    void testProcessSchedulerRequest_WhenDoorClosedAndButtonPressed_ExpectDoorOpening() {
        elevator.state = Elevator.State.DOOR_CLOSED;
        elevator.elevatorButtonPressed = true;
        elevator.floorRequested = 2;

        elevator.processSchedulerRequest();

        assertEquals(Elevator.State.DOOR_OPENING, elevator.state);
        assertEquals(2, elevator.floorRequested);
    }

    @Test
    void testProcessSchedulerRequest_WhenDoorClosedAndNoButtonPressed_ExpectExit() {
        elevator.state = Elevator.State.DOOR_CLOSED;
        elevator.elevatorButtonPressed = false;

        assertDoesNotThrow(() -> elevator.processSchedulerRequest());
    }

    @Test
    void testProcessResponse_WhenUserInputYes_ExpectTrue() {
        String input = "Yes\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        boolean result = elevator.processResponse();

        assertTrue(result);
    }

    @Test
    void testProcessResponse_WhenUserInputNo_ExpectFalse() {
        String input = "No\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        boolean result = elevator.processResponse();

        assertFalse(result);
    }

    @Test
    void testProcessResponse_WhenUserInputInvalid_ExpectFalse() {
        String input = "Invalid\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        boolean result = elevator.processResponse();

        assertFalse(result);
    }

    @Test
    void testProcessResponse_WhenScannerThrowsException_ExpectFalse() {
        System.setIn(new InputStream() {
            public int read() {
                throw new NoSuchElementException();
            }
        });

        boolean result = elevator.processResponse();

        assertFalse(result);
    }

    @Test
    void testMain_WhenSimulatingSchedulerRequest_ExpectCorrectBehavior() {
        String input = "2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        assertDoesNotThrow(() -> Elevator.main(new String[0]));
    }

    @Test
    void testMain_WhenScannerThrowsException_ExpectNoExceptionThrown() {
        System.setIn(new InputStream() {
            public int read() {
                throw new NoSuchElementException();
            }
        });

        assertDoesNotThrow(() -> Elevator.main(new String[0]));
    }

    @Test
    void testMain_WhenSimulatingSchedulerRequest_ExpectInfiniteLoop() {
        String input = "2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        assertTimeoutPreemptively(
                java.time.Duration.ofSeconds(1),
                () -> {
                    assertDoesNotThrow(() -> Elevator.main(new String[0]));
                }
        );
    }

    /**
     * Clean up method to reset the system.in after each test finishes
     */
    @AfterEach
    void restoreSystemInputOutput() {
        System.setIn(stdin);
    }
}
