import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Scheduler
 *
 * @author Taran Basati (101161332)
 * @version February 17, 2023
 */
public class SchedulerTest {

    private Scheduler scheduler;
    private InputStream stdin;

    /**
     * Setup method that initializes Scheduler objects for the upcoming tests
     */
    @BeforeEach
    void setUp() {
        scheduler = new Scheduler();
        stdin = System.in; // Save the standard input stream
    }

    @Test
    void testProcessFloorRequest_WhenReceivingRequest_ExpectScheduling() {
        scheduler.state = Scheduler.State.RECEIVING_REQUEST;

        assertDoesNotThrow(() -> scheduler.processFloorRequest());

        assertEquals(Scheduler.State.SCHEDULING, scheduler.state);
    }

    @Test
    void testProcessFloorRequest_WhenScheduling_ExpectReceivingRequest() {
        scheduler.state = Scheduler.State.SCHEDULING;

        assertDoesNotThrow(() -> scheduler.processFloorRequest());

        assertEquals(Scheduler.State.RECEIVING_REQUEST, scheduler.state);
    }

    @Test
    void testProcessResponse_WhenUserInputYes_ExpectTrue() {
        String input = "Yes\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        boolean result = scheduler.processResponse();

        assertTrue(result);
    }

    @Test
    void testProcessResponse_WhenUserInputNo_ExpectFalse() {
        String input = "No\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        boolean result = scheduler.processResponse();

        assertFalse(result);
    }

    @Test
    void testProcessResponse_WhenUserInputInvalid_ExpectFalse() {
        String input = "Invalid\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        boolean result = scheduler.processResponse();

        assertFalse(result);
    }

    @Test
    void testProcessResponse_WhenScannerThrowsException_ExpectFalse() {
        System.setIn(new InputStream() {
            public int read() {
                throw new NoSuchElementException();
            }
        });

        boolean result = scheduler.processResponse();

        assertFalse(result);
    }

    @Test
    void testMain_WhenSimulatingFloorRequest_ExpectCorrectBehavior() {
        String input = "Yes\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        assertDoesNotThrow(() -> Scheduler.main(new String[0]));
    }

    @Test
    void testMain_WhenScannerThrowsException_ExpectNoExceptionThrown() {
        System.setIn(new InputStream() {
            public int read() {
                throw new NoSuchElementException();
            }
        });

        assertDoesNotThrow(() -> Scheduler.main(new String[0]));
    }

    @Test
    void testMain_WhenSimulatingFloorRequest_ExpectInfiniteLoop() {
        String input = "Yes\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        assertTimeoutPreemptively(
                java.time.Duration.ofSeconds(1),
                () -> {
                    assertDoesNotThrow(() -> Scheduler.main(new String[0]));
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
