package edu.sc.bse3211.meetingplanner;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class MeetingTest {
    private Meeting meeting;
    private ArrayList<Person> attendees;
    private Room room;
    
    @Before
    public void setUp() {
        attendees = new ArrayList<>();
        attendees.add(new Person("Test Person 1"));
        attendees.add(new Person("Test Person 2"));
        
        room = new Room("Test Room");
        
        meeting = new Meeting(5, 15, 10, 12, attendees, room, "Test Meeting");
    }
    
    @Test
    public void testConstructorWithNoParameters() {
        Meeting emptyMeeting = new Meeting();
        assertNotNull("Meeting should be created", emptyMeeting);
    }
    
    @Test
    public void testConstructorForAllDayMeeting() {
        Meeting allDayMeeting = new Meeting(6, 20);
        assertEquals("Month should be 6", 6, allDayMeeting.getMonth());
        assertEquals("Day should be 20", 20, allDayMeeting.getDay());
        assertEquals("Start time should be 0", 0, allDayMeeting.getStartTime());
        assertEquals("End time should be 23", 23, allDayMeeting.getEndTime());
    }
    
    @Test
    public void testConstructorWithDescription() {
        Meeting describedMeeting = new Meeting(7, 25, "Description Test");
        assertEquals("Month should be 7", 7, describedMeeting.getMonth());
        assertEquals("Day should be 25", 25, describedMeeting.getDay());
        assertEquals("Start time should be 0", 0, describedMeeting.getStartTime());
        assertEquals("End time should be 23", 23, describedMeeting.getEndTime());
        assertEquals("Description should match", "Description Test", describedMeeting.getDescription());
    }
    
    @Test
    public void testConstructorWithStartEndTimes() {
        Meeting timeSpecificMeeting = new Meeting(8, 15, 13, 17);
        assertEquals("Month should be 8", 8, timeSpecificMeeting.getMonth());
        assertEquals("Day should be 15", 15, timeSpecificMeeting.getDay());
        assertEquals("Start time should be 13", 13, timeSpecificMeeting.getStartTime());
        assertEquals("End time should be 17", 17, timeSpecificMeeting.getEndTime());
    }
    
    @Test
    public void testFullConstructor() {
        assertEquals("Month should be 5", 5, meeting.getMonth());
        assertEquals("Day should be 15", 15, meeting.getDay());
        assertEquals("Start time should be 10", 10, meeting.getStartTime());
        assertEquals("End time should be 12", 12, meeting.getEndTime());
        assertEquals("Description should match", "Test Meeting", meeting.getDescription());
        assertEquals("Room should match", room, meeting.getRoom());
        assertEquals("Attendees list should match", attendees, meeting.getAttendees());
    }
    
    @Test
    public void testAddAttendee() {
        Person newPerson = new Person("New Attendee");
        meeting.addAttendee(newPerson);
        assertTrue("Attendees list should contain new person", 
            meeting.getAttendees().contains(newPerson));
        assertEquals("Attendees list should have increased by 1", 
            3, meeting.getAttendees().size());
    }
    
    @Test
    public void testRemoveAttendee() {
        Person personToRemove = attendees.get(0);
        meeting.removeAttendee(personToRemove);
        assertFalse("Attendees list should not contain removed person", 
            meeting.getAttendees().contains(personToRemove));
        assertEquals("Attendees list should have decreased by 1", 
            1, meeting.getAttendees().size());
    }
    
    @Test
    public void testToString() {
        String meetingString = meeting.toString();
        assertTrue("String should contain month/day", meetingString.contains("5/15"));
        assertTrue("String should contain times", meetingString.contains("10 - 12"));
        assertTrue("String should contain room ID", meetingString.contains(room.getID()));
        assertTrue("String should contain description", meetingString.contains("Test Meeting"));
        assertTrue("String should contain attendees", meetingString.contains("Test Person 1"));
        assertTrue("String should contain attendees", meetingString.contains("Test Person 2"));
    }
    
    @Test
    public void testSettersAndGetters() {
        // Test setters
        meeting.setMonth(9);
        meeting.setDay(22);
        meeting.setStartTime(8);
        meeting.setEndTime(10);
        meeting.setDescription("Updated Description");
        Room newRoom = new Room("New Room");
        meeting.setRoom(newRoom);
        
        // Verify with getters
        assertEquals("Month should be updated", 9, meeting.getMonth());
        assertEquals("Day should be updated", 22, meeting.getDay());
        assertEquals("Start time should be updated", 8, meeting.getStartTime());
        assertEquals("End time should be updated", 10, meeting.getEndTime());
        assertEquals("Description should be updated", "Updated Description", meeting.getDescription());
        assertEquals("Room should be updated", newRoom, meeting.getRoom());
    }
}
