package edu.sc.bse3211.meetingplanner;

import java.util.ArrayList;

public class Calendar {
	// Indexed by Month, Day
	private ArrayList<ArrayList<ArrayList<Meeting>>> occupied;
	
	/**
	 * Default constructor, builds a calendar and initializes each day
	 * to an empty list.
	 */
	public Calendar(){
		/** Create an empty calendar
		 * Order of access is month, day, meetingNumber
		 * We want to tie 1 to Janaury, 2 to February, etc, 
		 * so we will index 1-12 for months, 1-31 for days. 
		 * Times are indexed 0 - 23.
		 * Need to check bounds when adding a meeting.
		 */
		occupied = new ArrayList<ArrayList<ArrayList<Meeting>>>();
		
		for(int i=0;i<=13;i++){
			// Initialize month
			occupied.add(new ArrayList<ArrayList<Meeting>>());
			for(int j=0;j<32;j++){
				// Initialize days
				occupied.get(i).add(new ArrayList<Meeting>());
			}
		}
		
		/** Not every month should have 31 days. 
		 * We can deal with this in a hack-ish manner by 
		 * putting in all-day meetings for those dates.
		 */
		occupied.get(2).get(29).add(new Meeting(2,29,"Day does not exist"));
		occupied.get(2).get(30).add(new Meeting(2,30,"Day does not exist"));
		occupied.get(2).get(31).add(new Meeting(2,31,"Day does not exist"));
		occupied.get(4).get(31).add(new Meeting(4,31,"Day does not exist"));
		occupied.get(6).get(31).add(new Meeting(6,31,"Day does not exist"));
		occupied.get(9).get(31).add(new Meeting(9,31,"Day does not exist"));
		occupied.get(11).get(30).add(new Meeting(11,31,"Day does not exist"));
		occupied.get(11).get(31).add(new Meeting(11,31,"Day does not exist"));
	}
	
	/**
	 * Used to check whether a meeting is scheduled during a particular 
	 * time frame.
	 * @param month - The month of the meeting (1-12)
	 * @param day - The day of the meeting (1-31)
	 * @param start - The time the meeting starts (0-23)
	 * @param end - The time the meeting ends (0-23)
	 * @return boolean - Whether the calendar has an entry in that timeframe.
	 * @throws TimeConflictException If an invalid date or time is entered.
	 */
	public boolean isBusy(int month, int day, int start, int end) throws TimeConflictException{
		checkTimes(month,day,start,end);
		
		for(Meeting toCheck : occupied.get(month).get(day)){
			// Skip placeholder meetings for non-existent days
			if(toCheck.getDescription() != null && toCheck.getDescription().equals("Day does not exist")) {
				throw new TimeConflictException("Day does not exist.");
			}
			
			// Check if the time ranges overlap
			if(start < toCheck.getEndTime() && end > toCheck.getStartTime()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Basic error checking on numbers.
	 * @param mMonth - The month of the meeting (1-12)
	 * @param mDay - The day of the meeting (1-31)
	 * @param mStart - The time the meeting starts (0-23)
	 * @param mEnd - The time the meeting ends (0-23)
	 * @throws TimeConflictException - If an invalid date or time is entered.
	 */
	public static void checkTimes(int mMonth,int mDay,int mStart, int mEnd) throws TimeConflictException{
		// Check for illegal months
		if(mMonth < 1 || mMonth > 12){
			throw new TimeConflictException("Month does not exist.");
		}

		// Check for illegal days based on month
		int maxDays;
		switch(mMonth) {
			case 2: // February
				maxDays = 28; // We'll treat all years as non-leap years for simplicity
				break;
			case 4: case 6: case 9: case 11: // 30-day months
				maxDays = 30;
				break;
			default: // 31-day months
				maxDays = 31;
				break;
		}
		
		if(mDay < 1 || mDay > maxDays){
			throw new TimeConflictException("Day does not exist for month " + mMonth);
		}

		// Check for illegal times
		if(mStart < 0 || mStart > 23){
			throw new TimeConflictException("Illegal start hour: " + mStart);
		}

		if(mEnd < 0 || mEnd > 23){
			throw new TimeConflictException("Illegal end hour: " + mEnd);
		}

		if(mStart >= mEnd){
			throw new TimeConflictException("Meeting starts before it ends.");
		}
	}
	
	/**
	 * Used to add a meeting to a calendar
	 * @param toAdd - A Meeting object to add to the calendar
	 * @throws TimeConflictException - If an invalid date or time is entered.
	 */
	public void addMeeting(Meeting toAdd) throws TimeConflictException{
		int mDay = toAdd.getDay();
		int mMonth = toAdd.getMonth();
		int mStart = toAdd.getStartTime();
		int mEnd = toAdd.getEndTime();
		
		checkTimes(mMonth,mDay,mStart,mEnd);
		
		// Check whether a meeting is already scheduled at this time.
		ArrayList<Meeting> thatDay = occupied.get(mMonth).get(mDay);
		boolean booked = false;
		Meeting conflict = null;
		
		for(Meeting toCheck : thatDay){
			if(!toCheck.getDescription().equals("Day does not exist")){
				// Check if the time ranges overlap
				if(mStart < toCheck.getEndTime() && mEnd > toCheck.getStartTime()) {
					booked = true;
					conflict = toCheck;
					break;
				}
			}
		}
		
		if(booked){
			throw new TimeConflictException("Overlap with another item - "+conflict.getDescription()
				+" - scheduled from "+conflict.getStartTime()+" and "+conflict.getEndTime());
		}else{
			occupied.get(mMonth).get(mDay).add(toAdd);
		}
	}
	
	/**
	 * Clears all meetings for a day.
	 * @param month - The month of the meeting (1-12)
	 * @param day - The day of the meeting (1-31)
	 */
	public void clearSchedule(int month, int day){
		occupied.get(month).set(day, new ArrayList<Meeting>());
	}
	
	/**
	 * Used to print the calendar for a month in string form.
	 * @param month - The month of the meeting (1-12)
	 * @return String - The agenda as a formatted string.
	 */
	public String printAgenda(int month){
		String agenda = "Agenda for "+month+":\n";
		for(ArrayList<Meeting> toPrint : occupied.get(month)){
			for(Meeting meeting: toPrint){
				if(meeting != null && !meeting.getDescription().equals("Day does not exist")) {
					agenda = agenda+meeting.toString()+"\n";
				}
			}
		}

		return agenda;
	}

	/**
	 * Used to print the calendar for a day in string form.
	 * @param month - The month of the meeting (1-12)
	 * @param day - The day of the meeting (1-31)
	 * @return String - The agenda as a formatted string.
	 */
	public String printAgenda(int month, int day){
		String agenda = "Agenda for "+month+"/"+day+":\n";
		for(Meeting toPrint : occupied.get(month).get(day)){
			if(toPrint != null && !toPrint.getDescription().equals("Day does not exist")) {
				agenda = agenda+toPrint.toString()+"\n";
			}
		}
		
		return agenda;
	}
	
	/**
	 * Retrieves a meeting from the calendar.
	 * @param month - The month of the meeting (1-12)
	 * @param day - The day of the meeting (1-31)
	 * @param index - The index in the list for the meeting
	 */
	public Meeting getMeeting(int month, int day, int index){
		return occupied.get(month).get(day).get(index);
	}
	
	/**
	 * Removes a meeting from the calendar.
	 * @param month - The month of the meeting (1-12)
	 * @param day - The day of the meeting (1-31)
	 * @param index - The index in the list for the meeting
	 */
	public void removeMeeting(int month, int day, int index){
		occupied.get(month).get(day).remove(index);
	}
}
