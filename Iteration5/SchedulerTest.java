import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;
 
public class SchedulerTest {
 
    private Scheduler scheduler;
    private InputStream stdin;
 
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
    void testProcessFloorRequest_WhenReceivingRequestWithElevatorFault_ExpectScheduling() {
        scheduler.state = Scheduler.State.RECEIVING_REQUEST;
        String input = "2024-02-03,4,Up,6,1,0\n"; // Elevator fault set to 1
        System.setIn(new ByteArrayInputStream(input.getBytes()));
 
        assertDoesNotThrow(() -> scheduler.processFloorRequest());
 
        assertEquals(Scheduler.State.SCHEDULING, scheduler.state);
    }
 
    @Test
    void testProcessFloorRequest_WhenSchedulingWithElevatorFault_ExpectReceivingRequest() {
        scheduler.state = Scheduler.State.SCHEDULING;
        String input = "2024-02-03,4,Up,6,0,1\n"; // Elevator fault set to 1
        System.setIn(new ByteArrayInputStream(input.getBytes()));
 
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
 
    @AfterEach
    void restoreSystemInputOutput() {
        System.setIn(stdin);
    }
}