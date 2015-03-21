package com.univhousing.main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.xml.ws.Action;

import com.univhousing.users.Student;

public class EntryPoint {

	/**
	 * @param args
	 */
	private static boolean mLevelZero = true;
	private static boolean mLevelOne = true;
	private static boolean mLevelTwo = true;
	private static boolean mLevelThree = true;

	public static void main(String[] args) {
		Student studentOj = new Student();
		Credentials credentialObj = new Credentials();
		try {
			Connection connectionObj = null;
			Statement statementObj = null;
			ResultSet resultSetObj = null;

			try {
				int choice;

				System.out.println("Welcome to University Housing");
				// Create scanner object for taking command line input
				Scanner inputObj = new Scanner(System.in);

				// Going into infinite loop until exited
				while(mLevelZero) {
					System.out.println("1. Login");
					System.out.println("2. Guest Login");
					System.out.println("3. Exit");
					choice = inputObj.nextInt();

					switch (choice) 
					{
						case 1:
							System.out.print("Enter University Id:");
							credentialObj.personId = inputObj.nextInt();
	
							System.out.print("Enter Password:");
							credentialObj.password = inputObj.next();
	
							if(authenticateUser(credentialObj.personId,credentialObj.password,statementObj,credentialObj))
							{
								//credentialObj.designation.equalsIgnoreCase(Constants.STUDENT)
								if(true)
								{
									// This is the first Level of Student Heirarchy
									while(mLevelOne)
									{
										// Show different menu items
										System.out.println("1. Housing Option");
										System.out.println("2. Parking Option");
										System.out.println("3. Maintenance");
										System.out.println("4. Profile");
										System.out.println("5. Back");
										
										choice = inputObj.nextInt();
										switch (choice) 
										{
											case 1:
												// This is the second level of Student hierarchy where he is in Housing Options
												while(mLevelTwo)
												{
													System.out.println("1. View Invoices");
													System.out.println("2. View Leases");
													System.out.println("3. New Request");
													System.out.println("4. View/Cancel Requests");
													System.out.println("5. View Vacancy");
													System.out.println("6. Back");
													choice = inputObj.nextInt();
													
													switch(choice)
													{
														case 1:
															// This is the third level of Student hierarchy where he is checking Invoices
															while(mLevelThree)
															{
																System.out.println("View Current Invoice");
																System.out.println("View Former Invoices");
																System.out.println("Back");
																choice = inputObj.nextInt();
																
																switch(choice)
																{
																	case 1:
																		System.out.println("Showing current Invoices");
																		break;
																	case 2:
																		System.out.println("Showing Former Invoices");
																		break;
																	case 3:
																		// Taking back to Level Two
																		mLevelThree = false;
																		break;
																}
															}
															// Setting true so that we can come back in the third level
															mLevelThree = true;
															break;
														case 2:
															System.out.println("Showing Leases");
															break;
														case 3:
															System.out.println("Generating New Request");
															break;
														case 4:
															System.out.println("Showing/Viewing Cancel Invoices");
															break;
														case 5:
															System.out.println("Showing Vacancy");
															break;
														case 6:
															// Taking back to Level One
															mLevelTwo = false;
															break;
													}
												}
												// Setting true so that you can come back inside Level two
												mLevelTwo = true;
												break;
											case 2:
												System.out.println("Showing Parking");
												break;
											case 3:
												System.out.println("Showing Maintenance");
												break;
											case 4:
												System.out.println("Showing Profile");
												break;
											case 5: 
												// Taking back to Level zero
												mLevelOne = false;
												break;
											default:
												break;
										}
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
							// Setting true so that we can come back inside level one
							mLevelOne = true;
							break;
	
						case 2:
							// Guest Login code will go here.	
							break;
	
						case 3: // Exit menu
							mLevelZero = false;
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

	/**
	 * @param personId
	 * @action Displays list of 
	 *  
	 *  @Tables 
	 */
	private static void displayListOfInvociesForAPerson(int personId) {
		// TODO Auto-generated method stub

	}

	/**
	 *  @param personId
	 *  @action Displays Lease number, Payment due, Due date, Student's Full name, Student Id, 
	 *  Place number, Room number, Name of Hall, Apartment, Date of Invoice Paid, Method of Payment
	 *  
	 *  @Tables 
	 */ 
	private static void displayCurrentInvoice(int personId) {

		/* Write SQL query to fetch:
		 * Lease number, Payment due, Due date, Student's Full name, Student Id, 
		 * Place number, Room number, Name of Hall, Apartment, Date of Invoice Paid, Method of Payment
		 * 
		 * Once the ResultSet is filled with data, depending upon how many ResultSets are used, we 
		 * can go ahead with the logic	
		 */	


	}


	/**
	 * @param userId
	 * @param password
	 * @param statement
	 * @param object
	 * @return True if the user has been authenticated , False if the authentication failed
	 * @throws SQLException
	 */
	private static boolean authenticateUser(int userId, String password, Statement statement,Credentials object) throws SQLException 
	{
		/* Write SQL query to check if userName and password match the Credentials Table in Database
		 If matches retrieve designation and person_id
		 */		
		/*ResultSet user = statement.executeQuery("SELECT .....");
		while(user.next())
		{
			object.personId = user.getInt("person_id");
			object.designation = user.getString("designation");

			// Returning true, because if the 
			return true;
		}*/
		return true;
	}
}
