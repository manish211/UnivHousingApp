package com.univhousing.main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.univhousing.users.Student;

public class EntryPoint {

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		Student studentOj = new Student();
		Credentials credentialObj = new Credentials();
		try {
			Connection connectionObj = null;
			Statement statementObj = null;
			ResultSet resultSetObj = null;
			
			try {
				boolean exitFlag = true;
				int choice;

				System.out.println("Welcome to University Housing");
				// Create scanner object for taking command line input
				Scanner inputObj = new Scanner(System.in);
				
				// Going into infinite loop until exited
				while(exitFlag) {
					System.out.println("1. Login");
					System.out.println("2. Guest Login");
					System.out.println("3. Exit");
					choice = inputObj.nextInt();
					
					switch (choice) {
					case 1:
						System.out.print("Enter University Id:");
						credentialObj.userName = inputObj.next();

						System.out.print("Enter Password:");
						credentialObj.password = inputObj.next();
						
						if(authenticateUser(credentialObj.userName,credentialObj.password,statementObj,credentialObj))
						{
							if(credentialObj.designation.equalsIgnoreCase(Constants.STUDENT))
							{
								// Show different menu items
								System.out.println("1. Housing Option");
								System.out.println("2. Parking Option");
								System.out.println("3. Maintenance");
								System.out.println("4. Profile");
								System.out.println("5. Back");
								
								/*This input shows whether the student chooses Housing, Parking,Maintenance or Profile*/
								choice = inputObj.nextInt();
								
								switch (choice) {
								case 1:
									System.out.println("View Invoices");
									System.out.println("View Leases");
									System.out.println("New Request");
									System.out.println("View/Cancel Requests");
									System.out.println("View Vacancy");
									System.out.println("Back");
									
									/*This input shows the student chose Housing options and now his options are to choose from:
									 * View Invoices, View Leases,New Request, View/Cancel Requests and View Vacancy*/ 
									choice = inputObj.nextInt();
									
										switch (choice) {
										case 1:
											
											break;

										default:
											break;
										}
									break;
								case 2:
									break;
									
								case 3:
									break;
									
								case 4:
									break;

								default:
									System.out.print("Invalid Choice");
									break;
								}
							}
							else if(credentialObj.designation.equalsIgnoreCase(Constants.SUPERVISOR))
							{
								// Do something
							}
							else if(credentialObj.designation.equalsIgnoreCase(Constants.GUEST))
							{
								// Do something
							}
						}
						else
						{
							System.out.println("Login Incorrect");
						}
						break;
					
					case 2:
						// Guest Login code will go here.	
						break;

					default: System.out.println("Invalid Choice");
						break;
					}	
				}
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	private static boolean authenticateUser(String userName, String password, Statement statement,Credentials object) throws SQLException 
	{
		/* Write SQL query to check if userName and password match the Credentials Table in Database
		 If matches retrieve designation and person_id
		 */		
		ResultSet user = statement.executeQuery("SELECT .....");
		while(user.next())
		{
			object.personId = user.getInt("person_id");
			object.designation = user.getString("designation");

			// Returning true, because if the 
			return true;
		}
		return false;
	}
}
