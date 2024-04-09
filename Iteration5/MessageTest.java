import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
 
public class MessageTest {
 
    @Test
    void testGetSetDoorFault() {
        Message message = new Message("2024-02-03", 4, "Up", 6, false, 1, 0);
        assertEquals(1, message.getDoor_fault());
        message.setDoor_fault(0);
        assertEquals(0, message.getDoor_fault());
    }
 
    @Test
    void testGetSetElevatorFault() {
        Message message = new Message("2024-02-03", 4, "Up", 6, false, 0, 1);
        assertEquals(1, message.getElevator_fault());
        message.setElevator_fault(0);
        assertEquals(0, message.getElevator_fault());
    }
 
    @Test
    void testGetDate() {
        Message message = new Message("2024-02-03", 4, "Up", 6, false, 0, 0);
        assertEquals("2024-02-03", message.getDate());
    }
 
    @Test
    void testSetDate() {
        Message message = new Message("2024-02-03", 4, "Up", 6, false, 0, 0);
        message.setDate("2024-02-04");
        assertEquals("2024-02-04", message.getDate());
    }
 
    @Test
    void testGetSource() {
        Message message = new Message("2024-02-03", 4, "Up", 6, false, 0, 0);
        assertEquals(4, message.getSource());
    }
 
    @Test
    void testSetSource() {
        Message message = new Message("2024-02-03", 4, "Up", 6, false, 0, 0);
        message.setSource(5);
        assertEquals(5, message.getSource());
    }
 
    @Test
    void testGetDirection() {
        Message message = new Message("2024-02-03", 4, "Up", 6, false, 0, 0);
        assertEquals("Up", message.getDirection());
    }
 
    @Test
    void testSetDirection() {
        Message message = new Message("2024-02-03", 4, "Up", 6, false, 0, 0);
        message.setDirection("Down");
        assertEquals("Down", message.getDirection());
    }
 
    @Test
    void testGetDestination() {
        Message message = new Message("2024-02-03", 4, "Up", 6, false, 0, 0);
        assertEquals(6, message.getDestination());
    }
 
    @Test
    void testSetDestination() {
        Message message = new Message("2024-02-03", 4, "Up", 6, false, 0, 0);
        message.setDestination(7);
        assertEquals(7, message.getDestination());
    }
 
    @Test
    void testGetConfirmation() {
        Message message = new Message("2024-02-03", 4, "Up", 6, false, 0, 0);
        assertFalse(message.getConfirmation());
    }
 
    @Test
    void testSetConfirmation() {
        Message message = new Message("2024-02-03", 4, "Up", 6, false, 0, 0);
        message.setConfirmation(true);
        assertTrue(message.getConfirmation());
    }
 
}