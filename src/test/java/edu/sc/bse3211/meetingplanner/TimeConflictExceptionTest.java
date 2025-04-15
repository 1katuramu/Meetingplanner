package edu.sc.bse3211.meetingplanner;

import static org.junit.Assert.*;
import org.junit.Test;

public class TimeConflictExceptionTest {
    
    @Test
    public void testDefaultConstructor() {
        TimeConflictException exception = new TimeConflictException();
        assertNotNull("Exception should be created", exception);
        assertNull("Message should be null", exception.getMessage());
        assertNull("Cause should be null", exception.getCause());
    }
    
    @Test
    public void testConstructorWithMessage() {
        String message = "Test conflict message";
        TimeConflictException exception = new TimeConflictException(message);
        assertNotNull("Exception should be created", exception);
        assertEquals("Message should match", message, exception.getMessage());
        assertNull("Cause should be null", exception.getCause());
    }
    
    @Test
    public void testConstructorWithCause() {
        Throwable cause = new RuntimeException("Test cause");
        TimeConflictException exception = new TimeConflictException(cause);
        assertNotNull("Exception should be created", exception);
        assertNotNull("Cause should not be null", exception.getCause());
        assertEquals("Cause should match", cause, exception.getCause());
    }
    
    @Test
    public void testConstructorWithMessageAndCause() {
        String message = "Test conflict message";
        Throwable cause = new RuntimeException("Test cause");
        TimeConflictException exception = new TimeConflictException(message, cause);
        assertNotNull("Exception should be created", exception);
        assertEquals("Message should match", message, exception.getMessage());
        assertNotNull("Cause should not be null", exception.getCause());
        assertEquals("Cause should match", cause, exception.getCause());
    }
    
    @Test
    public void testConstructorWithAllParameters() {
        String message = "Test conflict message";
        Throwable cause = new RuntimeException("Test cause");
        TimeConflictException exception = new TimeConflictException(message, cause, true, true);
        assertNotNull("Exception should be created", exception);
        assertEquals("Message should match", message, exception.getMessage());
        assertNotNull("Cause should not be null", exception.getCause());
        assertEquals("Cause should match", cause, exception.getCause());
    }
    
    @Test
    public void testEmptyMessage() {
        TimeConflictException exception = new TimeConflictException("");
        assertNotNull("Exception should be created", exception);
        assertEquals("Empty message should be preserved", "", exception.getMessage());
    }
    
    @Test
    public void testNullMessage() {
        TimeConflictException exception = new TimeConflictException((String)null);
        assertNotNull("Exception should be created", exception);
        assertNull("Null message should be preserved", exception.getMessage());
    }
    
    @Test
    public void testNullCause() {
        TimeConflictException exception = new TimeConflictException("Message", null);
        assertNotNull("Exception should be created", exception);
        assertEquals("Message should match", "Message", exception.getMessage());
        assertNull("Null cause should be preserved", exception.getCause());
    }
    
    @Test
    public void testExceptionChaining() {
        // Create a chain of exceptions
        RuntimeException cause1 = new RuntimeException("Cause 1");
        TimeConflictException cause2 = new TimeConflictException("Cause 2", cause1);
        TimeConflictException exception = new TimeConflictException("Top level", cause2);
        
        assertNotNull("Exception should be created", exception);
        assertEquals("Message should match", "Top level", exception.getMessage());
        assertNotNull("Cause should not be null", exception.getCause());
        assertEquals("First cause should match", cause2, exception.getCause());
        assertNotNull("Second cause should not be null", exception.getCause().getCause());
        assertEquals("Second cause should match", cause1, exception.getCause().getCause());
    }
    
    @Test
    public void testStackTracePreservation() {
        RuntimeException cause = new RuntimeException("Test cause");
        TimeConflictException exception = new TimeConflictException("Test message", cause);
        
        assertNotNull("Exception should be created", exception);
        assertNotNull("StackTrace should be preserved", exception.getStackTrace());
        assertTrue("StackTrace should not be empty", exception.getStackTrace().length > 0);
    }
} 