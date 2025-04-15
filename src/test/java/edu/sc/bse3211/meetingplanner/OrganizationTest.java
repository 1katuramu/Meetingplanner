package edu.sc.bse3211.meetingplanner;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class OrganizationTest {
    private Organization organization;
    
    @Before
    public void setUp() {
        organization = new Organization();
    }
    
    @Test
    public void testDefaultConstructor() {
        assertNotNull("Organization should be created", organization);
        assertNotNull("Employees list should be initialized", organization.getEmployees());
        assertNotNull("Rooms list should be initialized", organization.getRooms());
    }
    
    @Test
    public void testGetEmployees() {
        ArrayList<Person> employees = organization.getEmployees();
        assertNotNull("Employees list should not be null", employees);
        assertTrue("Employees list should not be empty", employees.size() > 0);
        
        // Check that default employees were added
        boolean foundMartha = false;
        boolean foundCollins = false;
        boolean foundBrenda = false;
        boolean foundJulius = false;
        boolean foundLynn = false;
        
        for (Person employee : employees) {
            if (employee.getName().equals("Namugga Martha")) foundMartha = true;
            if (employee.getName().equals("Shema Collins")) foundCollins = true;
            if (employee.getName().equals("Acan Brenda")) foundBrenda = true;
            if (employee.getName().equals("Kazibwe Julius")) foundJulius = true;
            if (employee.getName().equals("Kukunda Lynn")) foundLynn = true;
        }
        
        assertTrue("Should contain Namugga Martha", foundMartha);
        assertTrue("Should contain Shema Collins", foundCollins);
        assertTrue("Should contain Acan Brenda", foundBrenda);
        assertTrue("Should contain Kazibwe Julius", foundJulius);
        assertTrue("Should contain Kukunda Lynn", foundLynn);
    }
    
    @Test
    public void testGetRooms() {
        ArrayList<Room> rooms = organization.getRooms();
        assertNotNull("Rooms list should not be null", rooms);
        assertTrue("Rooms list should not be empty", rooms.size() > 0);
        
        // Check that default rooms were added
        boolean foundLLT6A = false;
        boolean foundLLT6B = false;
        boolean foundLLT3A = false;
        boolean foundLLT2C = false;
        boolean foundLAB2 = false;
        
        for (Room room : rooms) {
            if (room.getID().equals("LLT6A")) foundLLT6A = true;
            if (room.getID().equals("LLT6B")) foundLLT6B = true;
            if (room.getID().equals("LLT3A")) foundLLT3A = true;
            if (room.getID().equals("LLT2C")) foundLLT2C = true;
            if (room.getID().equals("LAB2")) foundLAB2 = true;
        }
        
        assertTrue("Should contain LLT6A", foundLLT6A);
        assertTrue("Should contain LLT6B", foundLLT6B);
        assertTrue("Should contain LLT3A", foundLLT3A);
        assertTrue("Should contain LLT2C", foundLLT2C);
        assertTrue("Should contain LAB2", foundLAB2);
    }
    
    @Test
    public void testGetRoom() {
        try {
            Room room = organization.getRoom("LLT6A");
            assertNotNull("Room should be found", room);
            assertEquals("Room ID should match", "LLT6A", room.getID());
        } catch (Exception e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    @Test(expected = Exception.class)
    public void testGetRoomNonExistent() throws Exception {
        organization.getRoom("NonExistentRoom");
    }
    
    @Test
    public void testGetEmployee() {
        try {
            Person employee = organization.getEmployee("Namugga Martha");
            assertNotNull("Employee should be found", employee);
            assertEquals("Employee name should match", "Namugga Martha", employee.getName());
        } catch (Exception e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    @Test(expected = Exception.class)
    public void testGetEmployeeNonExistent() throws Exception {
        organization.getEmployee("NonExistentPerson");
    }
}
