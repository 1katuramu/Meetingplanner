package edu.sc.bse3211.meetingplanner;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class PersonTest {
    private Person person;
    private Meeting meeting;
    private Room room;
    
    @Before
    public void setUp() {
        person = new Person("Test Person");
        room = new Room("Test Room");
        ArrayList<Person> attendees = new ArrayList<>();
        attendees.add(person);
        meeting = new Meeting(5, 15, 10, 12, attendees, room, "Test Meeting");
    }
    
    @Test
    public void testDefaultConstructor() {
        Person emptyPerson = new Person();
        assertNotNull("Person should be created", emptyPerson);
        assertEquals("Name should be empty string", "", emptyPerson.getName());
    }
    
    @Test
    public void testConstructorWithName() {
        assertEquals("Name should match", "Test Person", person.getName());
    }
    
    @Test
    public void testAddMeeting() {
        try {
            person.addMeeting(meeting);
            assertTrue("Person should be busy during meeting time", 
                person.isBusy(5, 15, 10, 12));
            assertFalse("Person should not be busy outside meeting time", 
                person.isBusy(5, 15, 13, 14));
        } catch(TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    @Test(expected = TimeConflictException.class)
    public void testAddMeetingConflict() throws TimeConflictException {
        // Add first meeting
        person.addMeeting(meeting);
        
        // Try to add overlapping meeting
        Meeting conflictMeeting = new Meeting(5, 15, 11, 13, 
            new ArrayList<Person>(), room, "Conflict Meeting");
        person.addMeeting(conflictMeeting);
    }
    
    @Test
    public void testPrintAgendaMonth() {
        try {
            person.addMeeting(meeting);
            String agenda = person.printAgenda(5);
            assertTrue("Agenda should contain meeting", agenda.contains("Test Meeting"));
            assertTrue("Agenda should contain room", agenda.contains(room.getID()));
            assertTrue("Agenda should contain person name", agenda.contains(person.getName()));
        } catch(TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testPrintAgendaDay() {
        try {
            person.addMeeting(meeting);
            String agenda = person.printAgenda(5, 15);
            assertTrue("Agenda should contain meeting", agenda.contains("Test Meeting"));
            assertTrue("Agenda should contain room", agenda.contains(room.getID()));
            assertTrue("Agenda should contain person name", agenda.contains(person.getName()));
        } catch(TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testIsBusy() {
        try {
            person.addMeeting(meeting);
            assertTrue("Person should be busy during meeting time", 
                person.isBusy(5, 15, 10, 12));
            assertFalse("Person should not be busy outside meeting time", 
                person.isBusy(5, 15, 13, 14));
        } catch(TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetMeeting() {
        try {
            person.addMeeting(meeting);
            Meeting retrievedMeeting = person.getMeeting(5, 15, 0);
            assertEquals("Retrieved meeting should match added meeting", meeting, retrievedMeeting);
        } catch(TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testRemoveMeeting() {
        try {
            person.addMeeting(meeting);
            assertTrue("Person should be busy before removing meeting", 
                person.isBusy(5, 15, 10, 12));
            
            person.removeMeeting(5, 15, 0);
            assertFalse("Person should not be busy after removing meeting", 
                person.isBusy(5, 15, 10, 12));
        } catch(TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
}
