package com.univhousing.invoice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import com.univhousing.main.ConnectionUtils;
import com.univhousing.main.Constants;
import com.univhousing.main.Utils;
import com.univhousing.users.Student;

public class TicketPersonStaff_Relation {
	
	public int staffNo;
	public int ticketNo;
	public String ticketStatus;
	public String ticketSeverity;
	public int personId;

	Scanner inputObj = new Scanner(System.in);
	Student studentObj = new Student();

	/**
	 * @param personId
	 * @action Raises a new ticket based on the information given by the user
	 * @dataSeverity Water, Electricity � High severity
					 Appliances, Internet � Medium severity
					 Cleaning, Miscellaneous � Low severity
	 */
	public void raiseNewTicket(int personId) {

		String severity = "", description = "";
		String ticketType = "";
		int ticketNo = 0, staffNoMax = 0, staffNoMin = 0;
		System.out.println("Select the ticket type:\n" +
				"1. Water\n" +
				"2. Electricity\n" +
				"3. Applicance\n" +
				"4. Internet\n" +
				"5. Cleaning\n" +
				"6. Miscellaneous\n");

		int type = inputObj.nextInt();

		switch (type) 
		{
			case 1:
				ticketType = Constants.WATER;
				severity = Constants.HIGH_SEVERITY;
				break;
			case 2:
				ticketType = Constants.ELECTRICITY;
				severity = Constants.HIGH_SEVERITY;
				break;
				
			case 3:
				ticketType = Constants.APPLIANCE;
				severity = Constants.MEDIUM_SEVERITY;
				break;
			case 4:
				ticketType = Constants.INTERNET;
				severity = Constants.MEDIUM_SEVERITY;
				break;
				
			case 5:
				ticketType = Constants.CLEANING;
				severity = Constants.LOW_SEVERITY;
				break;
			case 6:
				ticketType = Constants.MISCELLANEOUS;
				severity = Constants.LOW_SEVERITY;
				break;
		}
		
		// Consuming the  /n after previous input as nextInt(), doesn't consume the \n i.e. enter
		inputObj.nextLine();
		
		System.out.println("Enter the description for the issue:");
		description = inputObj.nextLine();
		
		/*Write SQL Query to fetch the maximum and minimum value of the staff no to randomize who gets the ticket*/
		PreparedStatement ps = null;
		Connection conn = ConnectionUtils.getConnection();
		String query3 = "SELECT MAX(staff_no) AS LastStaff FROM ticket_person_staff";
		try 
		{
			ps = conn.prepareStatement(query3);
			ResultSet max = ps.executeQuery();
			while(max.next())
			{
				staffNoMax = max.getInt("LastStaff");
			}
		}
		catch(SQLException e)
		{
			try {
				ConnectionUtils.closeConnection(conn);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		PreparedStatement ps1 = null;
		Connection conn1 = ConnectionUtils.getConnection();
		String query4 = "SELECT MIN(staff_no) AS FirstStaff FROM ticket_person_staff";
		try 
		{
			ps1 = conn1.prepareStatement(query4);
			ResultSet min = ps1.executeQuery();
			while(min.next())
			{
				staffNoMin = min.getInt("FirstStaff");
			}
		}
		catch(SQLException e)
		{
			try {
				ConnectionUtils.closeConnection(conn1);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		// Getting a random staff number between First and the Last staff
		staffNo = Utils.randomizeStaff(staffNoMax, staffNoMin);
		System.out.println("Assigning ticket to staff: "+staffNo);
		
		/*Write SQL Query to fetch the maximum value of the ticket number so that we can continue from there
		 * If the value is null then we will just assign value as default ticket number value*/
		
		PreparedStatement preparedStatement = null;
		Connection dbConnection = ConnectionUtils.getConnection();
		String query = "SELECT MAX(ticket_no) AS HighestTicket FROM ticket_person_staff";
		
		try 
		{
			preparedStatement = dbConnection.prepareStatement(query);
			ResultSet maxTicketNo = preparedStatement.executeQuery();
			
			while(maxTicketNo.next())
			{
				ticketNo = maxTicketNo.getInt("HighestTicket");
				if(ticketNo == 0)
				{
					ticketNo = Constants.TICKET_DEFAULT_VALUE;
				}
				else
				{
					ticketNo++;
				}
			}

			ConnectionUtils.closeConnection(dbConnection);
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/*Write SQL query to create a ticket with type, ticketNo, severity and description
		 * Ticket has to be raised with a staff no who it will be assigned to, so 
		 * we can do random generation of staff no. or we can write logic to choose staff no.*/
		
		PreparedStatement preparedStatement1 = null;
		Connection dbConnection1 = ConnectionUtils.getConnection();
		String query1 = "INSERT INTO ticket_person_staff (staff_no,ticket_no,ticket_status,ticket_severity,person_id,description,ticket_type) VALUES (?,?,?,?,?,?,?)";
		
		try 
		{
			preparedStatement1 = dbConnection1.prepareStatement(query1);
			preparedStatement1.setInt(1, staffNo);
			preparedStatement1.setInt(2, ticketNo);
			preparedStatement1.setString(3, Constants.DEFAULT_TICKET_STATUS);
			preparedStatement1.setString(4, severity);
			preparedStatement1.setInt(5, personId);
			preparedStatement1.setString(6, description);
			preparedStatement1.setString(7, ticketType);
			
			preparedStatement1.executeUpdate();
			ConnectionUtils.closeConnection(dbConnection1);
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	/**
	 * @param personId
	 * @throws SQLException 
	 * @action Lists all the tickets for a particular student id for a person Id
	 * and saves them in the ArrayList<Integer> ticketNumbers
	 */
	public void viewAllTicketStatuses(int personId, ArrayList<Integer> ticketNumbersList) throws SQLException 
	{
		int studentId;
		// Fetch the student Id for a particular Person Id
		studentId = studentObj.getStudentIdForPersonId(personId);
		
		/*Write SQL Query to fetch all the tickets*/
		ResultSet viewAllTickets = null;
		ticketNumbersList.clear();
		while(viewAllTickets.next())
		{
			ticketNumbersList.add(viewAllTickets.getInt("ticket_no"));
		}
		
		System.out.println("\nBelow are the list of tickets:");
		for (int i = 0; i < ticketNumbersList.size(); i++) 
		{
			System.out.println((i+1)+". "+ticketNumbersList.get(i));
		}
		System.out.println("\nSelect the ticket number you want to view details for:");
		int choice = inputObj.nextInt();
		
		/* Getting the ticket number form the index, but have to substract 1 because ArrayList has values from 0 to n-1
		 * but the options on screen are from 1 to n-1
		*/
		int ticketSelected = ticketNumbersList.get(choice-1);
		
		ResultSet getTicketDetails = null;
		/*Write SQL query for fetching the details with status for the ticket number (ticketSelected) by student*/
	}

	/**
	 * @param ArrayList<Integer> adminLevelMaintenanceRequests
	 * @throws SQLException 
	 */
	public void getAllMaintenanceTickets(ArrayList<Integer> maintenanceTicketsList) throws SQLException 
	{
		/*Write SQL Query to fetch all the maintenance tickets*/
		ResultSet allTickets = null;
		maintenanceTicketsList.clear();
		
		while(allTickets.next())
		{
			maintenanceTicketsList.add(allTickets.getInt("ticket_no"));
		}
		
		System.out.println("Displaying all Maintenance Tickets");
		for (int i = 0; i < maintenanceTicketsList.size(); i++) 
		{
			System.out.println((i+1)+". "+maintenanceTicketsList.get(i));
		}
		System.out.println("Select the ticket you want to: ");
		int ticketSelected = inputObj.nextInt();
		int ticketNumber = maintenanceTicketsList.get(ticketSelected-1);
		
		/*Write SQL Query to fetch all details for this ticket number 
		 * and set it's status to Processing*/
		
		/*Write SQL Trigger to change ticket status to Complete after 30 mins*/
		
	}	
	
	
	
	
}
