package edu.sc.bse3211.meetingplanner;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class CalendarTest {
	private Calendar calendar;
	private Room testRoom;
	private ArrayList<Person> attendees;
	
	@Before
	public void setUp() {
		calendar = new Calendar();
		testRoom = new Room("Test Room");
		attendees = new ArrayList<>();
		attendees.add(new Person("Test Person"));
	}
	
	@Test
	public void testAddMeeting_holiday() {
		// Create Janan Luwum holiday
		try {
			Meeting janan = new Meeting(2, 16, "Janan Luwum");
			calendar.addMeeting(janan);
			// Verify that it was added
			Boolean added = calendar.isBusy(2, 16, 0, 23);
			assertTrue("Janan Luwum Day should be marked as busy on the calendar", added);
		} catch(TimeConflictException e) {
			fail("Should not throw exception: " + e.getMessage());
		}
	}
	
	@Test
	public void testAddMeeting_regularMeeting() {
		try {
			Meeting meeting = new Meeting(3, 15, 10, 12, attendees, testRoom, "Team Standup");
			calendar.addMeeting(meeting);
			// Verify that it was added
			Boolean busy = calendar.isBusy(3, 15, 10, 12);
			assertTrue("Calendar should be busy during meeting time", busy);
			
			// Check outside meeting hours
			assertFalse("Calendar should not be busy outside meeting time", 
				calendar.isBusy(3, 15, 13, 14));
		} catch(TimeConflictException e) {
			fail("Should not throw exception: " + e.getMessage());
		}
	}
	
	@Test
	public void testAddMeeting_conflictingMeetings() {
		try {
			// Add first meeting
			Meeting meeting1 = new Meeting(4, 20, 14, 16, attendees, testRoom, "First Meeting");
			calendar.addMeeting(meeting1);
			
			// Try to add overlapping meeting
			Meeting meeting2 = new Meeting(4, 20, 15, 17, attendees, testRoom, "Second Meeting");
			calendar.addMeeting(meeting2);
			fail("Should have thrown TimeConflictException for overlapping meetings");
		} catch(TimeConflictException e) {
			// Expected exception
			assertTrue("Exception message should mention overlap", 
				e.getMessage().contains("Overlap with another item"));
		}
	}
	
	@Test
	public void testAddMeeting_backToBackMeetings() {
		try {
			// Add first meeting
			Meeting meeting1 = new Meeting(5, 10, 9, 10, attendees, testRoom, "Morning Meeting");
			calendar.addMeeting(meeting1);
			
			// Add back-to-back meeting
			Meeting meeting2 = new Meeting(5, 10, 11, 12, attendees, testRoom, "Lunch Meeting");
			calendar.addMeeting(meeting2);
			
			// Verify both were added
			assertTrue("Calendar should be busy during first meeting", 
				calendar.isBusy(5, 10, 9, 10));
			assertTrue("Calendar should be busy during second meeting", 
				calendar.isBusy(5, 10, 11, 12));
			
			// Check gap between meetings
			assertFalse("Calendar should not be busy between meetings", 
				calendar.isBusy(5, 10, 10, 11));
		} catch(TimeConflictException e) {
			fail("Should not throw exception: " + e.getMessage());
		}
	}
	
	@Test(expected = TimeConflictException.class)
	public void testAddMeeting_invalidDateFeb30() throws TimeConflictException {
		// Try to create meeting on February 30 (invalid date)
		Meeting invalidMeeting = new Meeting(2, 30, 10, 12, attendees, testRoom, "Invalid Meeting");
		calendar.addMeeting(invalidMeeting);
	}
	
	@Test(expected = TimeConflictException.class)
	public void testAddMeeting_invalidTimeEndBeforeStart() throws TimeConflictException {
		// Try to create meeting where end time is before start time
		Meeting invalidMeeting = new Meeting(5, 15, 14, 12, attendees, testRoom, "Invalid Time");
		calendar.addMeeting(invalidMeeting);
	}
	
	@Test(expected = TimeConflictException.class)
	public void testAddMeeting_invalidHour() throws TimeConflictException {
		// Try to create meeting with hour > 23
		Meeting invalidMeeting = new Meeting(5, 15, 22, 25, attendees, testRoom, "Invalid Hour");
		calendar.addMeeting(invalidMeeting);
	}
	
	@Test
	public void testPrintAgenda() {
		try {
			// Add meetings
			Meeting meeting1 = new Meeting(6, 5, 9, 11, attendees, testRoom, "First Meeting");
			Meeting meeting2 = new Meeting(6, 7, 13, 15, attendees, testRoom, "Second Meeting");
			calendar.addMeeting(meeting1);
			calendar.addMeeting(meeting2);
			
			// Print agenda for month
			String monthAgenda = calendar.printAgenda(6);
			assertTrue("Month agenda should contain first meeting", 
				monthAgenda.contains("First Meeting"));
			assertTrue("Month agenda should contain second meeting", 
				monthAgenda.contains("Second Meeting"));
			
			// Print agenda for day
			String dayAgenda = calendar.printAgenda(6, 5);
			assertTrue("Day agenda should contain first meeting", 
				dayAgenda.contains("First Meeting"));
			assertFalse("Day agenda should not contain second meeting", 
				dayAgenda.contains("Second Meeting"));
		} catch(TimeConflictException e) {
			fail("Should not throw exception: " + e.getMessage());
		}
	}
	
	@Test
	public void testRemoveMeeting() {
		try {
			// Add meeting
			Meeting meeting = new Meeting(7, 10, 14, 16, attendees, testRoom, "Meeting to Remove");
			calendar.addMeeting(meeting);
			
			// Verify it was added
			assertTrue("Calendar should be busy after adding meeting", 
				calendar.isBusy(7, 10, 14, 16));
			
			// Remove meeting
			calendar.removeMeeting(7, 10, 0);
			
			// Verify it was removed
			assertFalse("Calendar should not be busy after removing meeting", 
				calendar.isBusy(7, 10, 14, 16));
		} catch(TimeConflictException e) {
			fail("Should not throw exception: " + e.getMessage());
		}
	}
	
	@Test
	public void testClearSchedule() {
		try {
			// Add multiple meetings on same day
			Meeting meeting1 = new Meeting(8, 20, 9, 10, attendees, testRoom, "Morning Meeting");
			Meeting meeting2 = new Meeting(8, 20, 14, 16, attendees, testRoom, "Afternoon Meeting");
			calendar.addMeeting(meeting1);
			calendar.addMeeting(meeting2);
			
			// Verify they were added
			assertTrue("Calendar should be busy in morning", 
				calendar.isBusy(8, 20, 9, 10));
			assertTrue("Calendar should be busy in afternoon", 
				calendar.isBusy(8, 20, 14, 16));
			
			// Clear schedule for day
			calendar.clearSchedule(8, 20);
			
			// Verify meetings were removed
			assertFalse("Calendar should not be busy in morning after clearing", 
				calendar.isBusy(8, 20, 9, 10));
			assertFalse("Calendar should not be busy in afternoon after clearing", 
				calendar.isBusy(8, 20, 14, 16));
		} catch(TimeConflictException e) {
			fail("Should not throw exception: " + e.getMessage());
		}
	}
	
	@Test
	public void testCheckTimes_ValidInput() {
		try {
			Calendar.checkTimes(6, 15, 9, 17);
			// If no exception thrown, test passes
		} catch(TimeConflictException e) {
			fail("Should not throw exception for valid times: " + e.getMessage());
		}
	}
}
