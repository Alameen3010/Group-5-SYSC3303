import org.junit.jupiter.api.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Elevator and Scheduler together
 *
 * @author Taran Basati (101161332)
 * @version February 17, 2023
 */
public class ElevatorSchedulerIntegrationTest {

    private InputStream stdin;

    /**
     * Setup method that saves the input stream before tests
     */
    @BeforeEach
    void setUp() {
        stdin = System.in;
    }

    @Test
    void testElevatorSchedulerIntegration_WhenSimulatingInteraction_ExpectCorrectBehavior() {
        // Simulate user input for Scheduler
        String schedulerInput = "Yes\n";
        System.setIn(new ByteArrayInputStream(schedulerInput.getBytes()));

        // Simulate user input for Elevator
        String elevatorInput = "2\n";
        System.setIn(new ByteArrayInputStream(elevatorInput.getBytes()));

        assertDoesNotThrow(() -> {
            Scheduler.main(new String[0]);
            Elevator.main(new String[0]);
        });
    }

    @Test
    void testElevatorSchedulerIntegration_WhenScannerThrowsException_ExpectNoExceptionThrown() {
        System.setIn(new InputStream() {
            public int read() {
                throw new NoSuchElementException();
            }
        });

        assertDoesNotThrow(() -> {
            Scheduler.main(new String[0]);
            Elevator.main(new String[0]);
        });
    }

    /**
     * Clean up method to reset the system.in after each test finishes
     */
    @AfterEach
    void restoreSystemInputOutput() {
        System.setIn(stdin);
    }
}
