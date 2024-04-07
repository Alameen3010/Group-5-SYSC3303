import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for Box
 *
 * @author Taran Basati (101161332)
 * @version February 3, 2023
 */
class BoxTest {

    @Test
    void testGetFromSource() {
        Box sourceBox = new Box();
        Box content = new Box("10:30", 5, true, 3);

        sourceBox.getFromSource(content);

        assertEquals("10:30", sourceBox.getTime());
        assertEquals(5, sourceBox.getFloorNumber());
        assertTrue(sourceBox.isDirectionUp());
        assertEquals(3, sourceBox.getCarButtonNumber());
        assertEquals(1, sourceBox.getState());
    }

    @Test
    void testSendToDestination() {
        Box sourceBox = new Box("10:30", 5, true, 3);

        Box receivedBox = sourceBox.sendToDestination();

        assertEquals("10:30", receivedBox.getTime());
        assertEquals(5, receivedBox.getFloorNumber());
        assertTrue(receivedBox.isDirectionUp());
        assertEquals(3, receivedBox.getCarButtonNumber());
        assertEquals(2, sourceBox.getState());
    }

    @Test
    void testGetFromDestination() {
        Box destinationBox = new Box();
        Box content = new Box("10:35", 5, true, 3);

        destinationBox.getFromDestination(content);

        assertEquals("10:35", destinationBox.getTime());
        assertEquals(5, destinationBox.getFloorNumber());
        assertTrue(destinationBox.isDirectionUp());
        assertEquals(3, destinationBox.getCarButtonNumber());
        assertEquals(3, destinationBox.getState());
    }

    @Test
    void testSendToSource() {
        Box destinationBox = new Box("10:35", 5, true, 3);

        Box receivedBox = destinationBox.sendToSource();

        assertEquals("10:35", receivedBox.getTime());
        assertEquals(5, receivedBox.getFloorNumber());
        assertTrue(receivedBox.isDirectionUp());
        assertEquals(3, receivedBox.getCarButtonNumber());
        assertEquals(0, destinationBox.getState());
    }

    @Test
    void testPrintContents() {
        Box box = new Box("10:30", 5, true, 3);
        // Redirecting System.out to capture printed contents
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        box.printContents();

        // Resetting System.out
        System.setOut(System.out);

        String expectedOutput = "========== Contents of Response Message ==================\n" +
                "Time: 10:30\n" +
                "Floor Number: 5\n" +
                "Is Direction UP: true\n" +
                "Car Button Number: 3\n" +
                "========== End of Contents of Response Message ==================\n";
        assertEquals(expectedOutput, outputStream.toString());
    }
}
