import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
class ElevatorTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }
    @Test
    public void MockFloorTest() {
        String schedulerIP = "localhost";
        int schedulerPort = 50000;
        Floor MockFloorSubsystem = new Floor(schedulerIP, schedulerPort);
        Floor.log("Creating instance of FloorSubsystem.");
        new Thread(MockFloorSubsystem).start();
        MockFloorSubsystem.run();
        assertEquals("[FloorSubsystem] Creating instance of FloorSubsystem.\n" +
                "[FloorSubsystem] FloorSubsystem is starting.\n" +
                "[FloorSubsystem] Reading requests from file: Requests.txt\n" +
                "[FloorSubsystem] Read line: 2024-02-03,4,Up,6\n" +
                "[FloorSubsystem] Read line: 2024-02-03,6,Down,4\n" +
                "[FloorSubsystem] Read line: 2024-02-03,15,Up,21\n" +
                "[FloorSubsystem] Read line: 2024-02-03,6,Down,4\n" +
                "[FloorSubsystem] Read line: 2024-02-03,9,Down,4\n" +
                "[FloorSubsystem] Read line: 2024-02-03,15,Up,23",outContent.toString().trim());
    }

    @Test
    public void MockSchedulerTest() {
        final int NUMBERELEVATORS = 4; // Can be changed
        Scheduler MockSchedulerSubsystem = new Scheduler(NUMBERELEVATORS);
        new Thread(MockSchedulerSubsystem).start();
        Scheduler.log("Starting SchedulerSubsystem thread.");
        MockSchedulerSubsystem.run();
        assertEquals("[SchedulerSubsystem] Scheduler DatagramSocket created on port 50000\n" +
                "[SchedulerSubsystem] Starting SchedulerSubsystem thread.\n" +
                "[SchedulerSubsystem] SchedulerSubsystem is running.\n" +
                "State: Receiving_Request\n" +
                "================ REQUESTS RECEIVED BY FLOOR ============================",outContent.toString().trim());
    }

    @Test
    public void SchedulerFloorTest() {
        final int NUMBERELEVATORS = 4; // Can be changed
        Scheduler MockSchedulerSubsystem = new Scheduler(NUMBERELEVATORS);
        new Thread(MockSchedulerSubsystem).start();
        Scheduler.log("Starting SchedulerSubsystem thread.");
        MockSchedulerSubsystem.run();
        String schedulerIP = "localhost";
        int schedulerPort = 50000;
        Floor MockFloorSubsystem = new Floor(schedulerIP, schedulerPort);
        Floor.log("Creating instance of FloorSubsystem.");
        new Thread(MockFloorSubsystem).start();
        MockFloorSubsystem.run();
        assertEquals("[SchedulerSubsystem] Scheduler DatagramSocket created on port 50000\n" +
                "[SchedulerSubsystem] Starting SchedulerSubsystem thread.\n" +
                "[SchedulerSubsystem] SchedulerSubsystem is running.\n" +
                "State: Receiving_Request\n" +
                "================ REQUESTS RECEIVED BY FLOOR ============================\n" +
                "2024-02-03,4,Up,6\n" +
                "2024-02-03,6,Down,4\n" +
                "2024-02-03,15,Up,21\n" +
                "2024-02-03,6,Down,4\n" +
                "2024-02-03,9,Down,4\n" +
                "2024-02-03,15,Up,23\n" +
                "0,0,0,0\n" +
                "================ END ============================\n" +
                "State: Scheduling\n" +
                "Sending Requests to Elevator 1\n" +
                "2024-02-03,4,Up,6\n" +
                "Sending Requests to Elevator 2\n" +
                "2024-02-03,6,Down,4\n" +
                "Sending Requests to Elevator 3\n" +
                "2024-02-03,15,Up,21\n" +
                "Sending Requests to Elevator 4\n" +
                "2024-02-03,6,Down,4\n" +
                "Sending Requests to Elevator 1\n" +
                "2024-02-03,9,Down,4\n" +
                "Sending Requests to Elevator 2\n" +
                "2024-02-03,15,Up,23\n" +
                "Sending Requests to Elevator 3\n" +
                "0,0,0,0",outContent.toString().trim());
    }

    @Test
    public void elevatorTest() {
        String schedulerIP = "localhost";
        int schedulerPort = 50000;
        Floor MockFloorSubsystem = new Floor(schedulerIP, schedulerPort);
        Floor.log("Creating instance of FloorSubsystem.");
        new Thread(MockFloorSubsystem).start();
        MockFloorSubsystem.run();
        Elevator elevatorSubsystem = new Elevator(schedulerIP, schedulerPort, 1);
        new Thread(elevatorSubsystem).start();
        Elevator elevatorSubsystem2 = new Elevator(schedulerIP, schedulerPort, 2);
        new Thread(elevatorSubsystem2).start();
        Elevator elevatorSubsystem3 = new Elevator(schedulerIP, schedulerPort, 3);
        new Thread(elevatorSubsystem3).start();
        Elevator elevatorSubsystem4 = new Elevator(schedulerIP, schedulerPort, 4);
        new Thread(elevatorSubsystem4).start();
        final int NUMBERELEVATORS = 4; // Can be changed
        Scheduler MockSchedulerSubsystem = new Scheduler(NUMBERELEVATORS);
        new Thread(MockSchedulerSubsystem).start();
        Scheduler.log("Starting SchedulerSubsystem thread.");
        MockSchedulerSubsystem.run();
        assertEquals("C:\\Users\\taran\\.jdks\\adopt-openjdk-14.0.2\\bin\\java.exe \"-javaagent:C:\\Program Files\\JetBrains\\IntelliJ IDEA 2023.3.3\\lib\\idea_rt.jar=52636:C:\\Program Files\\JetBrains\\IntelliJ IDEA 2023.3.3\\bin\" -Dfile.encoding=UTF-8 -classpath C:\\Users\\taran\\IdeaProjects\\sysc3303iter3\\out\\production\\sysc3303iter3;C:\\Users\\taran\\.m2\\repository\\org\\junit\\jupiter\\junit-jupiter\\5.8.1\\junit-jupiter-5.8.1.jar;C:\\Users\\taran\\.m2\\repository\\org\\junit\\jupiter\\junit-jupiter-api\\5.8.1\\junit-jupiter-api-5.8.1.jar;C:\\Users\\taran\\.m2\\repository\\org\\opentest4j\\opentest4j\\1.2.0\\opentest4j-1.2.0.jar;C:\\Users\\taran\\.m2\\repository\\org\\junit\\platform\\junit-platform-commons\\1.8.1\\junit-platform-commons-1.8.1.jar;C:\\Users\\taran\\.m2\\repository\\org\\apiguardian\\apiguardian-api\\1.1.2\\apiguardian-api-1.1.2.jar;C:\\Users\\taran\\.m2\\repository\\org\\junit\\jupiter\\junit-jupiter-params\\5.8.1\\junit-jupiter-params-5.8.1.jar;C:\\Users\\taran\\.m2\\repository\\org\\junit\\jupiter\\junit-jupiter-engine\\5.8.1\\junit-jupiter-engine-5.8.1.jar;C:\\Users\\taran\\.m2\\repository\\org\\junit\\platform\\junit-platform-engine\\1.8.1\\junit-platform-engine-1.8.1.jar Elevator Elevator1\n" +
                "ElevatorSubsystem 1 listening on port: 600001\n" +
                "2024-02-03,4,Up,6[Elevator 1]\n" +
                "2024-02-03,9,Down,4[Elevator 1]\n" +
                "================ END ============================\n" +
                "Count2\n" +
                "=========== Executing request: \n" +
                "2024-02-03,4,Up,6[Elevator 1]\n" +
                "The Elevator 1 is starting at floor: 0\n" +
                " State: Stop \n" +
                " Current Floor: 0 ->  State: Accelerating  ->  State: Cruising  -> State: Decelerating-> State: Stop \n" +
                " Current Floor: 4 ->  State: Door opening  ->  State: Door open \n" +
                "Passenger has entered at floor 4 and will be moving to floor: 6 ->  State: Door closing  ->  Door closed  ->  State: Accelerating  ->  State: Cruising  -> State: Decelerating-> State: Stop \n" +
                " Current Floor: 6 ->  State: Door opening  ->  State: Door open \n" +
                "Passenger has left elevator at floor 6 and Elevator now ready for new scheduler request  ->  State: Door closing  ->  Door closed  -> \n" +
                "Finished moving on\n" +
                " \n" +
                "=========== Executing request: \n" +
                "2024-02-03,9,Down,4[Elevator 1]\n" +
                "The Elevator 1 is starting at floor: 6\n" +
                " State: Stop \n" +
                " Current Floor: 6 ->  State: Accelerating  ->  State: Cruising  -> State: Decelerating-> State: Stop \n" +
                " Current Floor: 9 ->  State: Door opening  ->  State: Door open \n" +
                "Passenger has entered at floor 9 and will be moving to floor: 4 ->  State: Door closing  ->  Door closed  ->  State: Accelerating  ->  State: Cruising  -> State: Decelerating-> State: Stop \n" +
                " Current Floor: 4 ->  State: Door opening  ->  State: Door open \n" +
                "Passenger has left elevator at floor 4 and Elevator now ready for new scheduler request  ->  State: Door closing  ->  Door closed  -> \n" +
                "Finished moving on\n" +
                " \n" +
                "Finished list of requests. Now available for new.\n" +
                "[ElevatorSubsystem] Socket closed.\n" +
                "[ElevatorSubsystem] SchedulerSubsystem shutdown hook executed.\n" +
                "\n" +
                "Process finished with exit code 0\n",outContent.toString().trim());
    }
 
    @Test
    void testMeasurements() {
        String schedulerIP = "localhost";
        int schedulerPort = 50000;
        Floor MockFloorSubsystem = new Floor(schedulerIP, schedulerPort);
        Floor.log("Creating instance of FloorSubsystem.");
        Elevator elevatorSubsystem4 = new Elevator(schedulerIP, schedulerPort, 4);
        new Thread(elevatorSubsystem4).start();
        final int NUMBERELEVATORS = 4; // Can be changed
        Scheduler MockSchedulerSubsystem = new Scheduler(NUMBERELEVATORS);
        new Thread(MockSchedulerSubsystem).start();
        new Thread(MockFloorSubsystem).start();
        MockFloorSubsystem.run();
        Scheduler.log("Starting SchedulerSubsystem thread.");
        MockSchedulerSubsystem.run();
        assertEquals("Full Breakdown for Elevator 4:
                      Request [2024-02-04,2,Down,1,0,0] took 17.607 seconds
                      Request [2024-02-04,2,Down,1,0,0] traversed through 3 floors.
                      In total, Elevator 4 took 17.607 seconds and traversed through 3 floors",outContent.toString().trim());
    }

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
 
    @Test
    void testDoorFault_WhenDoorOpenWithFault_ExpectFalse() {
        elevator.state = Elevator.State.DOOR_OPEN;
        elevator.doorFault = 1;
 
        assertFalse(elevator.doorFault());
    }
 
    @Test
    void testDoorFault_WhenDoorOpenWithoutFault_ExpectTrue() {
        elevator.state = Elevator.State.DOOR_OPEN;
        elevator.doorFault = 0;
 
        assertTrue(elevator.doorFault());
    }
 
    @Test
    void testElevatorFault_WhenElevatorStopWithoutFault_ExpectTrue() {
        elevator.state = Elevator.State.STOP;
        elevator.elevatorFault = 0;
 
        assertTrue(elevator.elevatorFault());
    }
 
    @Test
    void testElevatorFault_WhenElevatorStopWithFault_ExpectFalse() {
        elevator.state = Elevator.State.STOP;
        elevator.elevatorFault = 1;
 
        assertFalse(elevator.elevatorFault());
    }

 
    /**
     * Clean up method to reset the system.in after each test finishes
     */
    @AfterEach
    void restoreSystemInputOutput() {
        System.setIn(stdin);
    }
}