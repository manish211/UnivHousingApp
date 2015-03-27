package com.univhousing.invoice;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

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

		String severity, description, ticketType;
		int ticketNo;
		System.out.println("Select the ticket type:\n" +
				"1. Water\n" +
				"2. Electricity\n" +
				"3. Applicance\n" +
				"4. Internet\n" +
				"5. Cleaning\n" +
				"6. Miscellaneous\n");

		ticketType = inputObj.next();
		int type = Integer.parseInt(ticketType);

		switch (type) 
		{
			case 1:
			case 2:
				severity = "High";
				break;
				
			case 3:
			case 4:
				severity = "Medium";
				break;
				
			case 5:
			case 6:
				severity = "Low";
				break;
	
			default: System.out.println("Invalid Choice");
				break;
		}
		
		System.out.println("Enter the description for the issue:\n");
		description = inputObj.next();
		
		/*Write SQL query to create a ticket with type, ticketNo, severity and description
		 * Ticket has to be raised with a staff no who it will be assigned to, so 
		 * we can do random generation of staff no. or we can write logic to choose staff no.*/
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
