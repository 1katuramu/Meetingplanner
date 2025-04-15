package edu.sc.bse3211.meetingplanner;

import java.util.ArrayList;

public class Meeting {
	private int month;
	private int day;
	private int start;
	private int end;
	private ArrayList<Person> attendees; 
	private Room room;
	private String description;
	
	/**
	 * Default constructor
	 */
	public Meeting(){
		attendees = new ArrayList<>();
	}
	
	/**
	 * Constructor that can be used to block off a whole day -
	 * such as for a vacation
	 * @param month - The month of the meeting (1-12).
	 * @param day - The day of the meeting (1-31).
	 */
	public Meeting(int month, int day){
		this();
		this.month=month;
		this.day=day;
		this.start=0;
		this.end=23;
	}
	
	/**
	 * Constructor that can be used to block off a whole day -
	 * such as for a vacation
	 * @param month - The month of the meeting (1-12).
	 * @param day - The day of the meeting (1-31).
	 * @param description - A description of the meeting.
	 */
	public Meeting(int month, int day, String description){
		this(month, day);
		this.description = description;
	}
	
	/**
	 * More detailed constructor
	 * @param month - The month of the meeting (1-12).
	 * @param day - The day of the meeting (1-31).
	 * @param start - The time the meeting starts (0-23).
	 * @param end - The time the meeting ends (0-23).
	 */
	public Meeting(int month, int day, int start, int end){
		this();
		this.month=month;
		this.day=day;
		this.start=start;
		this.end=end;
	}
	
	/**
	 * More detailed constructor
	 * @param month - The month of the meeting (1-12).
	 * @param day - The day of the meeting (1-31).
	 * @param start - The time the meeting starts (0-23).
	 * @param end - The time the meeting ends (0-23).
	 * @param attendees - The people attending the meeting.
	 * @param room - The room that the meeting is taking place in.
	 * @param description - A description of the meeting.
	 */
	public Meeting(int month, int day, int start, int end, ArrayList<Person> attendees, Room room, String description){
		this(month, day, start, end);
		this.attendees = attendees != null ? attendees : new ArrayList<>();
		this.room = room;
		this.description = description != null ? description : "";
	}

	/**
	 * Add an attendee to the meeting.
	 * @param attendee - The person to add.
	 */
	public void addAttendee(Person attendee) {
		if (attendee != null) {
			if (this.attendees == null) {
				this.attendees = new ArrayList<>();
			}
			this.attendees.add(attendee);
		}
	}
	
	/**
	 * Removes an attendee from the meeting.
	 * @param attendee - The person to remove.
	 */
	public void removeAttendee(Person attendee) {
		if (this.attendees != null && attendee != null) {
			this.attendees.remove(attendee);
		}
	}
	
	/**
	 * Returns information about the meeting as a formatted string.
	 * @return String - Information about the meeting.
	 */
	public String toString(){
		StringBuilder info = new StringBuilder();
		info.append(month).append("/").append(day)
			.append(", ").append(start).append(" - ").append(end);
		
		if (room != null) {
			info.append(",").append(room.getID());
		}
		
		if (description != null) {
			info.append(": ").append(description);
		}
		
		info.append("\nAttending: ");
		
		if (attendees != null && !attendees.isEmpty()) {
			for (Person attendee : attendees) {
				if (attendee != null) {
					info.append(attendee.getName()).append(",");
				}
			}
			// Remove last comma
			info.setLength(info.length() - 1);
		} else {
			info.append("No attendees");
		}
		
		return info.toString();
	}
	
	/**
	 * Getters and Setters
	 */
	
	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getStartTime() {
		return start;
	}

	public void setStartTime(int start) {
		this.start = start;
	}

	public int getEndTime() {
		return end;
	}

	public void setEndTime(int end) {
		this.end = end;
	}

	public ArrayList<Person> getAttendees() {
		if (attendees == null) {
			attendees = new ArrayList<>();
		}
		return attendees;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
	
	public String getDescription() {
		return description != null ? description : "";
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
