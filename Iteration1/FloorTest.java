import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for Floor
 *
 * @author Taran Basati (101161332)
 * @version February 3, 2023
 */
class FloorTest {

    @Test
    void testReadFileAndSendRequests() {
        // Create a mock Box
        Box mockBox = new Box();

        // Create a Floor instance with the mock Box
        Floor floor = new Floor(mockBox);

        // Prepare a test file with requests
        String testFilename = "Requests.txt";
        String testData = "2024-02-03,4,Up,2\n2024-02-03,5,Down,4\n";

        // Call the run method (which includes readFileAndSendRequests)
        floor.run();
    }

    @Test
    void testCreateBoxFromCSV() {
        // Create a mock Box
        Box mockBox = new Box();

        // Create a Floor instance with the mock Box
        Floor floor = new Floor(mockBox);

        // Call the createBoxFromCSV method with a test line
        floor.createBoxFromCSV("10:30,5,Up,3");

        // Verify the mockBox with the expected arguments
        assertEquals("10:30", mockBox.getTime());
        assertEquals(5, mockBox.getFloorNumber());
        assertTrue(mockBox.isDirectionUp());
        assertEquals(3, mockBox.getCarButtonNumber());
    }

    @Test
    void testGetFromSchedulerResponse() {
        // Create a mock Box
        Box mockBox = new Box();

        // Create a Floor instance with the mock Box
        Floor floor = new Floor(mockBox);

        // Redirect System.out to capture printed contents
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Call the getFromSchedulerResponse method
        floor.getFromSchedulerResponse();

        // Resetting System.out
        System.setOut(System.out);

        // Verify that the printed contents match the expected output
        assertEquals("========== Contents of Response Message ==================\n" +
                "Time: null\n" +
                "Floor Number: 0\n" +
                "Is Direction UP: false\n" +
                "Car Button Number: 0\n" +
                "========== End of Contents of Response Message ==================\n", outputStream.toString());
    }
}
