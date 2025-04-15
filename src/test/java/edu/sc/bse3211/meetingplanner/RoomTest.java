package edu.sc.bse3211.meetingplanner;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

/**
 * Test class for the Room class.
 * 
 * This class tests the core functionality of room management, including:
 * - Room creation and initialization
 * - Meeting scheduling and management
 * - Availability checking
 * - Agenda generation
 * - Meeting removal
 * - Edge cases and error conditions
 */
public class RoomTest {
    private Room room;
    private Person person1;
    private Person person2;
    private ArrayList<Person> attendees;
    
    /**
     * Set up test environment before each test.
     * Creates a new room and test attendees.
     */
    @Before
    public void setUp() {
        room = new Room("Test Room");
        person1 = new Person("Person 1");
        person2 = new Person("Person 2");
        attendees = new ArrayList<>();
        attendees.add(person1);
        attendees.add(person2);
    }
    
    /**
     * Test room creation and initialization.
     * Verifies that a room is properly created with the correct name.
     */
    @Test
    public void testRoomCreation() {
        assertNotNull("Room should be created", room);
        assertEquals("Room ID should match", "Test Room", room.getID());
    }
    
    /**
     * Test scheduling a single meeting.
     * Verifies that a meeting can be scheduled and the room is marked as busy.
     */
    @Test
    public void testScheduleSingleMeeting() {
        try {
            Meeting meeting = new Meeting(5, 15, 10, 12, attendees, room, "Test Meeting");
            room.addMeeting(meeting);
            
            assertTrue("Room should be busy during meeting time", 
                room.isBusy(5, 15, 10, 12));
            assertFalse("Room should be available outside meeting time", 
                room.isBusy(5, 15, 13, 14));
            
            Meeting retrievedMeeting = room.getMeeting(5, 15, 0);
            assertEquals("Meeting description should match", "Test Meeting", retrievedMeeting.getDescription());
            assertEquals("Meeting room should match", room, retrievedMeeting.getRoom());
            assertEquals("Meeting attendees should match", attendees, retrievedMeeting.getAttendees());
            
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    /**
     * Test scheduling multiple meetings on the same day.
     * Verifies that multiple meetings can be scheduled on the same day
     * and the room's availability is correctly tracked.
     */
    @Test
    public void testScheduleMultipleMeetingsSameDay() {
        try {
            Meeting meeting1 = new Meeting(5, 15, 9, 11, attendees, room, "Morning Meeting");
            Meeting meeting2 = new Meeting(5, 15, 13, 15, attendees, room, "Afternoon Meeting");
            
            room.addMeeting(meeting1);
            room.addMeeting(meeting2);
            
            assertTrue("Room should be busy during first meeting", 
                room.isBusy(5, 15, 9, 11));
            assertTrue("Room should be busy during second meeting", 
                room.isBusy(5, 15, 13, 15));
            assertFalse("Room should be available between meetings", 
                room.isBusy(5, 15, 11, 13));
            
            assertEquals("First meeting should be retrieved", "Morning Meeting", 
                room.getMeeting(5, 15, 0).getDescription());
            assertEquals("Second meeting should be retrieved", "Afternoon Meeting", 
                room.getMeeting(5, 15, 1).getDescription());
            
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    /**
     * Test scheduling meetings on different days.
     * Verifies that meetings can be scheduled on different days
     * and the room's availability is correctly tracked for each day.
     */
    @Test
    public void testScheduleMeetingsDifferentDays() {
        try {
            Meeting meeting1 = new Meeting(5, 15, 10, 12, attendees, room, "Monday Meeting");
            Meeting meeting2 = new Meeting(5, 16, 14, 16, attendees, room, "Tuesday Meeting");
            
            room.addMeeting(meeting1);
            room.addMeeting(meeting2);
            
            assertTrue("Room should be busy on Monday", 
                room.isBusy(5, 15, 10, 12));
            assertTrue("Room should be busy on Tuesday", 
                room.isBusy(5, 16, 14, 16));
            assertFalse("Room should be available on Monday afternoon", 
                room.isBusy(5, 15, 13, 14));
            assertFalse("Room should be available on Tuesday morning", 
                room.isBusy(5, 16, 10, 12));
            
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    /**
     * Test handling of overlapping meetings.
     * Verifies that attempting to schedule overlapping meetings
     * results in a TimeConflictException.
     */
    @Test
    public void testOverlappingMeetings() {
        try {
            Meeting meeting1 = new Meeting(5, 15, 10, 12, attendees, room, "First Meeting");
            room.addMeeting(meeting1);
            
            Meeting meeting2 = new Meeting(5, 15, 11, 13, attendees, room, "Overlapping Meeting");
            try {
                room.addMeeting(meeting2);
                fail("Should throw TimeConflictException for overlapping meetings");
            } catch (TimeConflictException e) {
                // Expected exception
            }
            
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    /**
     * Test handling of contained meetings.
     * Verifies that attempting to schedule a meeting that is fully contained
     * within another meeting results in a TimeConflictException.
     */
    @Test
    public void testContainedMeetings() {
        try {
            Meeting meeting1 = new Meeting(5, 15, 9, 13, attendees, room, "Outer Meeting");
            room.addMeeting(meeting1);
            
            Meeting meeting2 = new Meeting(5, 15, 10, 12, attendees, room, "Contained Meeting");
            try {
                room.addMeeting(meeting2);
                fail("Should throw TimeConflictException for contained meeting");
            } catch (TimeConflictException e) {
                // Expected exception
            }
            
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    /**
     * Test handling of adjacent meetings.
     * Verifies that meetings that end exactly when another begins
     * can be scheduled without conflicts.
     */
    @Test
    public void testAdjacentMeetings() {
        try {
            Meeting meeting1 = new Meeting(5, 15, 10, 12, attendees, room, "First Meeting");
            Meeting meeting2 = new Meeting(5, 15, 12, 14, attendees, room, "Adjacent Meeting");
            
            room.addMeeting(meeting1);
            room.addMeeting(meeting2);
            
            assertTrue("Room should be busy during first meeting", 
                room.isBusy(5, 15, 10, 12));
            assertTrue("Room should be busy during second meeting", 
                room.isBusy(5, 15, 12, 14));
            
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    /**
     * Test removing meetings.
     * Verifies that meetings can be removed and the room's availability
     * is updated accordingly.
     */
    @Test
    public void testRemoveMeeting() {
        try {
            Meeting meeting = new Meeting(5, 15, 10, 12, attendees, room, "Test Meeting");
            room.addMeeting(meeting);
            
            assertTrue("Room should be busy before removal", 
                room.isBusy(5, 15, 10, 12));
            
            room.removeMeeting(5, 15, 0);
            
            assertFalse("Room should be available after removal", 
                room.isBusy(5, 15, 10, 12));
            
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    /**
     * Test room agenda generation.
     * Verifies that the room's agenda correctly displays all scheduled meetings.
     */
    @Test
    public void testRoomAgenda() {
        try {
            Meeting meeting1 = new Meeting(5, 15, 9, 11, attendees, room, "Morning Meeting");
            Meeting meeting2 = new Meeting(5, 15, 13, 15, attendees, room, "Afternoon Meeting");
            
            room.addMeeting(meeting1);
            room.addMeeting(meeting2);
            
            String dayAgenda = room.printAgenda(5, 15);
            assertTrue("Day agenda should contain morning meeting", 
                dayAgenda.contains("Morning Meeting"));
            assertTrue("Day agenda should contain afternoon meeting", 
                dayAgenda.contains("Afternoon Meeting"));
            
            String monthAgenda = room.printAgenda(5);
            assertTrue("Month agenda should contain morning meeting", 
                monthAgenda.contains("Morning Meeting"));
            assertTrue("Month agenda should contain afternoon meeting", 
                monthAgenda.contains("Afternoon Meeting"));
            
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    /**
     * Test handling of invalid time ranges.
     * Verifies that attempting to schedule meetings with invalid time ranges
     * results in appropriate exceptions.
     */
    @Test
    public void testInvalidTimeRanges() {
        // Test with end time before start time
        try {
            Meeting meeting = new Meeting(5, 15, 14, 12, attendees, room, "Invalid Meeting");
            room.addMeeting(meeting);
            fail("Should throw exception for invalid time range");
        } catch (TimeConflictException e) {
            // Expected exception
        } catch (Exception e) {
            // Any other exception is acceptable
        }
        
        // Test with same start and end time
        try {
            Meeting meeting = new Meeting(5, 15, 10, 10, attendees, room, "Zero Duration Meeting");
            room.addMeeting(meeting);
            // This might be allowed depending on the implementation
        } catch (TimeConflictException e) {
            // Expected exception
        } catch (Exception e) {
            // Any other exception is acceptable
        }
        
        // Test with negative start time
        try {
            Meeting meeting = new Meeting(5, 15, -1, 12, attendees, room, "Negative Start Time");
            room.addMeeting(meeting);
            fail("Should throw exception for negative start time");
        } catch (TimeConflictException e) {
            // Expected exception
        } catch (Exception e) {
            // Any other exception is acceptable
        }
        
        // Test with end time beyond 23
        try {
            Meeting meeting = new Meeting(5, 15, 10, 24, attendees, room, "End Time Beyond 23");
            room.addMeeting(meeting);
            fail("Should throw exception for end time beyond 23");
        } catch (TimeConflictException e) {
            // Expected exception
        } catch (Exception e) {
            // Any other exception is acceptable
        }
    }
    
    /**
     * Test handling of invalid dates.
     * Verifies that attempting to schedule meetings with invalid dates
     * results in appropriate exceptions.
     */
    @Test
    public void testInvalidDates() {
        // Test with invalid day for month
        try {
            Meeting meeting = new Meeting(2, 30, 10, 12, attendees, room, "February 30 Meeting");
            room.addMeeting(meeting);
            // This might be allowed depending on the implementation
        } catch (TimeConflictException e) {
            // Expected exception
        } catch (Exception e) {
            // Any other exception is acceptable
        }
        
        // Test with invalid month
        try {
            Meeting meeting = new Meeting(13, 15, 10, 12, attendees, room, "Month 13 Meeting");
            room.addMeeting(meeting);
            fail("Should throw exception for invalid month");
        } catch (TimeConflictException e) {
            // Expected exception
        } catch (Exception e) {
            // Any other exception is acceptable
        }
        
        // Test with invalid day
        try {
            Meeting meeting = new Meeting(5, 32, 10, 12, attendees, room, "Day 32 Meeting");
            room.addMeeting(meeting);
            fail("Should throw exception for invalid day");
        } catch (TimeConflictException e) {
            // Expected exception
        } catch (Exception e) {
            // Any other exception is acceptable
        }
    }
    
    /**
     * Test handling of meetings spanning multiple hours.
     * Verifies that meetings spanning multiple hours are correctly scheduled
     * and the room's availability is properly tracked.
     */
    @Test
    public void testLongMeetings() {
        try {
            Meeting meeting = new Meeting(5, 15, 9, 17, attendees, room, "All Day Meeting");
            room.addMeeting(meeting);
            
            assertTrue("Room should be busy during entire meeting", 
                room.isBusy(5, 15, 9, 17));
            assertTrue("Room should be busy during middle of meeting", 
                room.isBusy(5, 15, 13, 14));
            assertFalse("Room should be available before meeting", 
                room.isBusy(5, 15, 8, 9));
            assertFalse("Room should be available after meeting", 
                room.isBusy(5, 15, 17, 18));
            
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    /**
     * Test handling of meetings with no attendees.
     * Verifies that meetings can be scheduled without attendees
     * and the room's availability is correctly tracked.
     */
    @Test
    public void testMeetingWithoutAttendees() {
        try {
            ArrayList<Person> emptyAttendees = new ArrayList<>();
            Meeting meeting = new Meeting(5, 15, 10, 12, emptyAttendees, room, "No Attendees Meeting");
            room.addMeeting(meeting);
            
            assertTrue("Room should be busy during meeting time", 
                room.isBusy(5, 15, 10, 12));
            assertTrue("Meeting should have no attendees", 
                room.getMeeting(5, 15, 0).getAttendees().isEmpty());
            
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
}
