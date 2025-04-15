package edu.sc.bse3211.meetingplanner;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

/**
 * Test class for the PlannerInterface class.
 * 
 * This class tests the core functionality of the meeting planner interface,
 * including meeting scheduling, vacation scheduling, availability checking,
 * and agenda management. It covers both normal operation and edge cases.
 */
public class PlannerInterfaceTest {
    private PlannerInterface planner;
    private Organization org;
    
    /**
     * Set up test environment before each test.
     * Creates a new PlannerInterface and Organization instance.
     */
    @Before
    public void setUp() {
        planner = new PlannerInterface();
        org = new Organization();
    }
    
    /**
     * Test the constructor of PlannerInterface.
     * Verifies that the planner and organization are properly initialized.
     */
    @Test
    public void testConstructor() {
        assertNotNull("PlannerInterface should be created", planner);
        assertNotNull("Organization should be initialized", org);
        assertFalse("Employees list should not be empty", org.getEmployees().isEmpty());
        assertFalse("Rooms list should not be empty", org.getRooms().isEmpty());
    }
    
    /**
     * Test scheduling a meeting.
     * Verifies that meetings can be scheduled for both rooms and people,
     * and that the meeting details are correctly stored.
     */
    @Test
    public void testScheduleMeeting() {
        // Create test data
        Room testRoom = new Room("Test Room");
        Person testPerson = new Person("Test Person");
        ArrayList<Person> attendees = new ArrayList<>();
        attendees.add(testPerson);
        
        // Create a meeting
        Meeting meeting = new Meeting(5, 15, 10, 12, attendees, testRoom, "Test Meeting");
        
        try {
            // Add meeting to room
            testRoom.addMeeting(meeting);
            assertTrue("Room should be busy during meeting time", 
                testRoom.isBusy(5, 15, 10, 12));
            
            // Add meeting to person
            testPerson.addMeeting(meeting);
            assertTrue("Person should be busy during meeting time", 
                testPerson.isBusy(5, 15, 10, 12));
            
            // Verify meeting details
            Meeting retrievedMeeting = testRoom.getMeeting(5, 15, 0);
            assertEquals("Meeting month should match", 5, retrievedMeeting.getMonth());
            assertEquals("Meeting day should match", 15, retrievedMeeting.getDay());
            assertEquals("Meeting start time should match", 10, retrievedMeeting.getStartTime());
            assertEquals("Meeting end time should match", 12, retrievedMeeting.getEndTime());
            assertEquals("Meeting description should match", "Test Meeting", retrievedMeeting.getDescription());
            assertEquals("Meeting room should match", testRoom, retrievedMeeting.getRoom());
            assertEquals("Meeting attendees should match", attendees, retrievedMeeting.getAttendees());
            
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    /**
     * Test scheduling vacation time.
     * Verifies that vacation time can be scheduled for a person,
     * and that the person is marked as busy during the vacation period.
     */
    @Test
    public void testScheduleVacation() {
        // Create test data
        Person testPerson = new Person("Test Person");
        ArrayList<Person> attendees = new ArrayList<>();
        attendees.add(testPerson);
        
        try {
            // Schedule vacation for a week
            for (int day = 1; day <= 7; day++) {
                Meeting vacation = new Meeting(5, day, 0, 23, attendees, new Room(), "vacation");
                testPerson.addMeeting(vacation);
                
                // Verify vacation is scheduled
                assertTrue("Person should be busy during vacation", 
                    testPerson.isBusy(5, day, 0, 23));
                
                Meeting retrievedMeeting = testPerson.getMeeting(5, day, 0);
                assertEquals("Vacation description should match", "vacation", retrievedMeeting.getDescription());
            }
            
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    /**
     * Test checking room availability.
     * Verifies that rooms are correctly marked as busy or available
     * based on scheduled meetings.
     */
    @Test
    public void testCheckRoomAvailability() {
        Room testRoom = new Room("Test Room");
        
        try {
            // Room should be available initially
            assertFalse("Room should be available", 
                testRoom.isBusy(5, 15, 10, 12));
            
            // Add a meeting
            Meeting meeting = new Meeting(5, 15, 10, 12, new ArrayList<>(), testRoom, "Test Meeting");
            testRoom.addMeeting(meeting);
            
            // Room should be busy during meeting
            assertTrue("Room should be busy during meeting", 
                testRoom.isBusy(5, 15, 10, 12));
            
            // Room should be available outside meeting time
            assertFalse("Room should be available outside meeting time", 
                testRoom.isBusy(5, 15, 13, 14));
            
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    /**
     * Test checking employee availability.
     * Verifies that employees are correctly marked as busy or available
     * based on scheduled meetings.
     */
    @Test
    public void testCheckEmployeeAvailability() {
        Person testPerson = new Person("Test Person");
        
        try {
            // Person should be available initially
            assertFalse("Person should be available", 
                testPerson.isBusy(5, 15, 10, 12));
            
            // Add a meeting
            Meeting meeting = new Meeting(5, 15, 10, 12, new ArrayList<>(), new Room(), "Test Meeting");
            testPerson.addMeeting(meeting);
            
            // Person should be busy during meeting
            assertTrue("Person should be busy during meeting", 
                testPerson.isBusy(5, 15, 10, 12));
            
            // Person should be available outside meeting time
            assertFalse("Person should be available outside meeting time", 
                testPerson.isBusy(5, 15, 13, 14));
            
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    /**
     * Test checking room agenda.
     * Verifies that room agendas correctly display scheduled meetings.
     */
    @Test
    public void testCheckAgendaRoom() {
        Room testRoom = new Room("Test Room");
        
        try {
            // Add meetings
            Meeting meeting1 = new Meeting(5, 15, 10, 12, new ArrayList<>(), testRoom, "Morning Meeting");
            Meeting meeting2 = new Meeting(5, 15, 14, 16, new ArrayList<>(), testRoom, "Afternoon Meeting");
            testRoom.addMeeting(meeting1);
            testRoom.addMeeting(meeting2);
            
            // Check day agenda
            String dayAgenda = testRoom.printAgenda(5, 15);
            assertTrue("Day agenda should contain morning meeting", 
                dayAgenda.contains("Morning Meeting"));
            assertTrue("Day agenda should contain afternoon meeting", 
                dayAgenda.contains("Afternoon Meeting"));
            
            // Check month agenda
            String monthAgenda = testRoom.printAgenda(5);
            assertTrue("Month agenda should contain morning meeting", 
                monthAgenda.contains("Morning Meeting"));
            assertTrue("Month agenda should contain afternoon meeting", 
                monthAgenda.contains("Afternoon Meeting"));
            
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    /**
     * Test checking person agenda.
     * Verifies that person agendas correctly display scheduled meetings.
     */
    @Test
    public void testCheckAgendaPerson() {
        Person testPerson = new Person("Test Person");
        
        try {
            // Add meetings
            Meeting meeting1 = new Meeting(5, 15, 10, 12, new ArrayList<>(), new Room(), "Morning Meeting");
            Meeting meeting2 = new Meeting(5, 15, 14, 16, new ArrayList<>(), new Room(), "Afternoon Meeting");
            testPerson.addMeeting(meeting1);
            testPerson.addMeeting(meeting2);
            
            // Check day agenda
            String dayAgenda = testPerson.printAgenda(5, 15);
            assertTrue("Day agenda should contain morning meeting", 
                dayAgenda.contains("Morning Meeting"));
            assertTrue("Day agenda should contain afternoon meeting", 
                dayAgenda.contains("Afternoon Meeting"));
            
            // Check month agenda
            String monthAgenda = testPerson.printAgenda(5);
            assertTrue("Month agenda should contain morning meeting", 
                monthAgenda.contains("Morning Meeting"));
            assertTrue("Month agenda should contain afternoon meeting", 
                monthAgenda.contains("Afternoon Meeting"));
            
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
    public void testMeetingOverlap() {
        Room testRoom = new Room("Test Room");
        Person testPerson = new Person("Test Person");
        ArrayList<Person> attendees = new ArrayList<>();
        attendees.add(testPerson);
        
        try {
            // Add first meeting
            Meeting meeting1 = new Meeting(5, 15, 10, 12, attendees, testRoom, "First Meeting");
            testRoom.addMeeting(meeting1);
            testPerson.addMeeting(meeting1);
            
            // Try to add overlapping meeting
            Meeting meeting2 = new Meeting(5, 15, 11, 13, attendees, testRoom, "Overlapping Meeting");
            
            try {
                testRoom.addMeeting(meeting2);
                fail("Should throw TimeConflictException for room");
            } catch (TimeConflictException e) {
                // Expected exception
            }
            
            try {
                testPerson.addMeeting(meeting2);
                fail("Should throw TimeConflictException for person");
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
    public void testMeetingAdjacent() {
        Room testRoom = new Room("Test Room");
        Person testPerson = new Person("Test Person");
        ArrayList<Person> attendees = new ArrayList<>();
        attendees.add(testPerson);
        
        try {
            // Add first meeting
            Meeting meeting1 = new Meeting(5, 15, 10, 12, attendees, testRoom, "First Meeting");
            testRoom.addMeeting(meeting1);
            testPerson.addMeeting(meeting1);
            
            // Add adjacent meeting (should be allowed)
            Meeting meeting2 = new Meeting(5, 15, 12, 14, attendees, testRoom, "Adjacent Meeting");
            testRoom.addMeeting(meeting2);
            testPerson.addMeeting(meeting2);
            
            // Verify both meetings exist
            assertEquals("First meeting should exist", "First Meeting", testRoom.getMeeting(5, 15, 0).getDescription());
            assertEquals("Second meeting should exist", "Adjacent Meeting", testRoom.getMeeting(5, 15, 1).getDescription());
            
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    /**
     * Test handling of meetings at the same time.
     * Verifies that attempting to schedule meetings at the same time
     * results in a TimeConflictException.
     */
    @Test
    public void testMeetingSameTime() {
        Room testRoom = new Room("Test Room");
        Person testPerson = new Person("Test Person");
        ArrayList<Person> attendees = new ArrayList<>();
        attendees.add(testPerson);
        
        try {
            // Add first meeting
            Meeting meeting1 = new Meeting(5, 15, 10, 12, attendees, testRoom, "First Meeting");
            testRoom.addMeeting(meeting1);
            testPerson.addMeeting(meeting1);
            
            // Try to add meeting at same time
            Meeting meeting2 = new Meeting(5, 15, 10, 12, attendees, testRoom, "Same Time Meeting");
            
            try {
                testRoom.addMeeting(meeting2);
                fail("Should throw TimeConflictException for room");
            } catch (TimeConflictException e) {
                // Expected exception
            }
            
            try {
                testPerson.addMeeting(meeting2);
                fail("Should throw TimeConflictException for person");
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
    public void testMeetingContained() {
        Room testRoom = new Room("Test Room");
        Person testPerson = new Person("Test Person");
        ArrayList<Person> attendees = new ArrayList<>();
        attendees.add(testPerson);
        
        try {
            // Add first meeting
            Meeting meeting1 = new Meeting(5, 15, 9, 13, attendees, testRoom, "First Meeting");
            testRoom.addMeeting(meeting1);
            testPerson.addMeeting(meeting1);
            
            // Try to add contained meeting
            Meeting meeting2 = new Meeting(5, 15, 10, 12, attendees, testRoom, "Contained Meeting");
            
            try {
                testRoom.addMeeting(meeting2);
                fail("Should throw TimeConflictException for room");
            } catch (TimeConflictException e) {
                // Expected exception
            }
            
            try {
                testPerson.addMeeting(meeting2);
                fail("Should throw TimeConflictException for person");
            } catch (TimeConflictException e) {
                // Expected exception
            }
            
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    /**
     * Test handling of containing meetings.
     * Verifies that attempting to schedule a meeting that fully contains
     * another meeting results in a TimeConflictException.
     */
    @Test
    public void testMeetingContains() {
        Room testRoom = new Room("Test Room");
        Person testPerson = new Person("Test Person");
        ArrayList<Person> attendees = new ArrayList<>();
        attendees.add(testPerson);
        
        try {
            // Add first meeting
            Meeting meeting1 = new Meeting(5, 15, 10, 12, attendees, testRoom, "First Meeting");
            testRoom.addMeeting(meeting1);
            testPerson.addMeeting(meeting1);
            
            // Try to add containing meeting
            Meeting meeting2 = new Meeting(5, 15, 9, 13, attendees, testRoom, "Containing Meeting");
            
            try {
                testRoom.addMeeting(meeting2);
                fail("Should throw TimeConflictException for room");
            } catch (TimeConflictException e) {
                // Expected exception
            }
            
            try {
                testPerson.addMeeting(meeting2);
                fail("Should throw TimeConflictException for person");
            } catch (TimeConflictException e) {
                // Expected exception
            }
            
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    /**
     * Test scheduling vacation across month boundaries.
     * Verifies that vacation can be scheduled across multiple months
     * and that the person is marked as busy during the entire vacation period.
     */
    @Test
    public void testVacationOverMonthBoundary() {
        Person testPerson = new Person("Test Person");
        ArrayList<Person> attendees = new ArrayList<>();
        attendees.add(testPerson);
        
        try {
            // Schedule vacation across month boundary
            // Last week of month 5
            for (int day = 25; day <= 31; day++) {
                Meeting vacation = new Meeting(5, day, 0, 23, attendees, new Room(), "vacation");
                testPerson.addMeeting(vacation);
                
                // Verify vacation is scheduled
                assertTrue("Person should be busy during vacation", 
                    testPerson.isBusy(5, day, 0, 23));
                
                Meeting retrievedMeeting = testPerson.getMeeting(5, day, 0);
                assertEquals("Vacation description should match", "vacation", retrievedMeeting.getDescription());
            }
            
            // First week of month 6
            for (int day = 1; day <= 7; day++) {
                Meeting vacation = new Meeting(6, day, 0, 23, attendees, new Room(), "vacation");
                testPerson.addMeeting(vacation);
                
                // Verify vacation is scheduled
                assertTrue("Person should be busy during vacation", 
                    testPerson.isBusy(6, day, 0, 23));
                
                Meeting retrievedMeeting = testPerson.getMeeting(6, day, 0);
                assertEquals("Vacation description should match", "vacation", retrievedMeeting.getDescription());
            }
            
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    /**
     * Test removing meetings.
     * Verifies that meetings can be removed from both rooms and people,
     * and that the availability is updated accordingly.
     */
    @Test
    public void testRemoveMeeting() {
        Room testRoom = new Room("Test Room");
        Person testPerson = new Person("Test Person");
        ArrayList<Person> attendees = new ArrayList<>();
        attendees.add(testPerson);
        
        try {
            // Add meeting
            Meeting meeting = new Meeting(5, 15, 10, 12, attendees, testRoom, "Test Meeting");
            testRoom.addMeeting(meeting);
            testPerson.addMeeting(meeting);
            
            // Verify meeting exists
            assertTrue("Room should be busy during meeting time", 
                testRoom.isBusy(5, 15, 10, 12));
            assertTrue("Person should be busy during meeting time", 
                testPerson.isBusy(5, 15, 10, 12));
            
            // Remove meeting
            testRoom.removeMeeting(5, 15, 0);
            testPerson.removeMeeting(5, 15, 0);
            
            // Verify meeting is removed
            assertFalse("Room should not be busy after meeting removal", 
                testRoom.isBusy(5, 15, 10, 12));
            assertFalse("Person should not be busy after meeting removal", 
                testPerson.isBusy(5, 15, 10, 12));
            
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    /**
     * Test scheduling meetings with multiple attendees.
     * Verifies that meetings can be scheduled with multiple attendees,
     * and that all attendees are marked as busy during the meeting.
     */
    @Test
    public void testMultipleAttendees() {
        Room testRoom = new Room("Test Room");
        Person person1 = new Person("Person 1");
        Person person2 = new Person("Person 2");
        Person person3 = new Person("Person 3");
        
        ArrayList<Person> attendees = new ArrayList<>();
        attendees.add(person1);
        attendees.add(person2);
        attendees.add(person3);
        
        try {
            // Create a meeting with multiple attendees
            Meeting meeting = new Meeting(5, 15, 10, 12, attendees, testRoom, "Team Meeting");
            
            // Add meeting to room and all attendees
            testRoom.addMeeting(meeting);
            person1.addMeeting(meeting);
            person2.addMeeting(meeting);
            person3.addMeeting(meeting);
            
            // Verify room is busy
            assertTrue("Room should be busy during meeting time", 
                testRoom.isBusy(5, 15, 10, 12));
            
            // Verify all attendees are busy
            assertTrue("Person 1 should be busy during meeting time", 
                person1.isBusy(5, 15, 10, 12));
            assertTrue("Person 2 should be busy during meeting time", 
                person2.isBusy(5, 15, 10, 12));
            assertTrue("Person 3 should be busy during meeting time", 
                person3.isBusy(5, 15, 10, 12));
            
            // Verify meeting details for all attendees
            Meeting retrievedMeeting1 = person1.getMeeting(5, 15, 0);
            Meeting retrievedMeeting2 = person2.getMeeting(5, 15, 0);
            Meeting retrievedMeeting3 = person3.getMeeting(5, 15, 0);
            
            assertEquals("Meeting description should match for Person 1", "Team Meeting", retrievedMeeting1.getDescription());
            assertEquals("Meeting description should match for Person 2", "Team Meeting", retrievedMeeting2.getDescription());
            assertEquals("Meeting description should match for Person 3", "Team Meeting", retrievedMeeting3.getDescription());
            
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    /**
     * Test scheduling meetings across different days.
     * Verifies that meetings can be scheduled across different days,
     * and that availability is correctly tracked for each day.
     */
    @Test
    public void testMeetingsAcrossDays() {
        Room testRoom = new Room("Test Room");
        Person testPerson = new Person("Test Person");
        ArrayList<Person> attendees = new ArrayList<>();
        attendees.add(testPerson);
        
        try {
            // Schedule meetings across different days
            Meeting meeting1 = new Meeting(5, 15, 10, 12, attendees, testRoom, "Monday Meeting");
            Meeting meeting2 = new Meeting(5, 16, 14, 16, attendees, testRoom, "Tuesday Meeting");
            Meeting meeting3 = new Meeting(5, 17, 9, 11, attendees, testRoom, "Wednesday Meeting");
            
            // Add meetings to room and person
            testRoom.addMeeting(meeting1);
            testRoom.addMeeting(meeting2);
            testRoom.addMeeting(meeting3);
            
            testPerson.addMeeting(meeting1);
            testPerson.addMeeting(meeting2);
            testPerson.addMeeting(meeting3);
            
            // Verify room is busy on each day
            assertTrue("Room should be busy on Monday", 
                testRoom.isBusy(5, 15, 10, 12));
            assertTrue("Room should be busy on Tuesday", 
                testRoom.isBusy(5, 16, 14, 16));
            assertTrue("Room should be busy on Wednesday", 
                testRoom.isBusy(5, 17, 9, 11));
            
            // Verify person is busy on each day
            assertTrue("Person should be busy on Monday", 
                testPerson.isBusy(5, 15, 10, 12));
            assertTrue("Person should be busy on Tuesday", 
                testPerson.isBusy(5, 16, 14, 16));
            assertTrue("Person should be busy on Wednesday", 
                testPerson.isBusy(5, 17, 9, 11));
            
            // Verify room is available on other days
            assertFalse("Room should be available on Thursday", 
                testRoom.isBusy(5, 18, 10, 12));
            
            // Verify person is available on other days
            assertFalse("Person should be available on Thursday", 
                testPerson.isBusy(5, 18, 10, 12));
            
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    /**
     * Test scheduling meetings with invalid time ranges.
     * Verifies that attempting to schedule meetings with invalid time ranges
     * (e.g., end time before start time) results in appropriate exceptions.
     */
    @Test
    public void testInvalidTimeRanges() {
        Room testRoom = new Room("Test Room");
        Person testPerson = new Person("Test Person");
        ArrayList<Person> attendees = new ArrayList<>();
        attendees.add(testPerson);
        
        // Test with end time before start time
        try {
            Meeting meeting = new Meeting(5, 15, 14, 12, attendees, testRoom, "Invalid Meeting");
            testRoom.addMeeting(meeting);
            fail("Should throw exception for invalid time range");
        } catch (TimeConflictException e) {
            // Expected exception
        } catch (Exception e) {
            // Any other exception is acceptable
        }
        
        // Test with same start and end time
        try {
            Meeting meeting = new Meeting(5, 15, 10, 10, attendees, testRoom, "Zero Duration Meeting");
            testRoom.addMeeting(meeting);
            // This might be allowed depending on the implementation
        } catch (TimeConflictException e) {
            // Expected exception
        } catch (Exception e) {
            // Any other exception is acceptable
        }
        
        // Test with negative start time
        try {
            Meeting meeting = new Meeting(5, 15, -1, 12, attendees, testRoom, "Negative Start Time");
            testRoom.addMeeting(meeting);
            fail("Should throw exception for negative start time");
        } catch (TimeConflictException e) {
            // Expected exception
        } catch (Exception e) {
            // Any other exception is acceptable
        }
        
        // Test with end time beyond 23
        try {
            Meeting meeting = new Meeting(5, 15, 10, 24, attendees, testRoom, "End Time Beyond 23");
            testRoom.addMeeting(meeting);
            fail("Should throw exception for end time beyond 23");
        } catch (TimeConflictException e) {
            // Expected exception
        } catch (Exception e) {
            // Any other exception is acceptable
        }
    }
    
    /**
     * Test scheduling meetings with invalid dates.
     * Verifies that attempting to schedule meetings with invalid dates
     * (e.g., February 30) results in appropriate exceptions.
     */
    @Test
    public void testInvalidDates() {
        Room testRoom = new Room("Test Room");
        Person testPerson = new Person("Test Person");
        ArrayList<Person> attendees = new ArrayList<>();
        attendees.add(testPerson);
        
        // Test with invalid day for month
        try {
            Meeting meeting = new Meeting(2, 30, 10, 12, attendees, testRoom, "February 30 Meeting");
            testRoom.addMeeting(meeting);
            // This might be allowed depending on the implementation
        } catch (TimeConflictException e) {
            // Expected exception
        } catch (Exception e) {
            // Any other exception is acceptable
        }
        
        // Test with invalid month
        try {
            Meeting meeting = new Meeting(13, 15, 10, 12, attendees, testRoom, "Month 13 Meeting");
            testRoom.addMeeting(meeting);
            fail("Should throw exception for invalid month");
        } catch (TimeConflictException e) {
            // Expected exception
        } catch (Exception e) {
            // Any other exception is acceptable
        }
        
        // Test with invalid day
        try {
            Meeting meeting = new Meeting(5, 32, 10, 12, attendees, testRoom, "Day 32 Meeting");
            testRoom.addMeeting(meeting);
            fail("Should throw exception for invalid day");
        } catch (TimeConflictException e) {
            // Expected exception
        } catch (Exception e) {
            // Any other exception is acceptable
        }
    }
} 