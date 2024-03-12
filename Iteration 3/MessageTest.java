import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void testGetDate() {
        // Testing getDate method
        Message message = new Message("2024-03-11", 1, "Up", 5, false);
        assertEquals("2024-03-11", message.getDate());
    }

    @Test
    void testSetDate() {
        // Testing setDate method
        Message message = new Message("2024-03-11", 1, "Up", 5, false);
        message.setDate("2024-03-12");
        assertEquals("2024-03-12", message.getDate());
    }

    @Test
    void testGetSource() {
        // Testing getSource method
        Message message = new Message("2024-03-11", 1, "Up", 5, false);
        assertEquals(1, message.getSource());
    }

    @Test
    void testSetSource() {
        // Testing setSource method
        Message message = new Message("2024-03-11", 1, "Up", 5, false);
        message.setSource(2);
        assertEquals(2, message.getSource());
    }

    @Test
    void testGetDirection() {
        // Testing getDirection method
        Message message = new Message("2024-03-11", 1, "Up", 5, false);
        assertEquals("Up", message.getDirection());
    }

    @Test
    void testSetDirection() {
        // Testing setDirection method
        Message message = new Message("2024-03-11", 1, "Up", 5, false);
        message.setDirection("Down");
        assertEquals("Down", message.getDirection());
    }

    @Test
    void testGetDestination() {
        // Testing getDestination method
        Message message = new Message("2024-03-11", 1, "Up", 5, false);
        assertEquals(5, message.getDestination());
    }

    @Test
    void testSetDestination() {
        // Testing setDestination method
        Message message = new Message("2024-03-11", 1, "Up", 5, false);
        message.setDestination(8);
        assertEquals(8, message.getDestination());
    }

    @Test
    void testGetConfirmation() {
        // Testing getConfirmation method
        Message message = new Message("2024-03-11", 1, "Up", 5, false);
        assertFalse(message.getConfirmation());
    }

    @Test
    void testSetConfirmation() {
        // Testing setConfirmation method
        Message message = new Message("2024-03-11", 1, "Up", 5, false);
        message.setConfirmation(true);
        assertTrue(message.getConfirmation());
    }
}
