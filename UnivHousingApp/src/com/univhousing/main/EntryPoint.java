package com.univhousing.main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
	private static boolean mLevelFour = true;
	private static ArrayList<Integer> mInvoiceNumbers = null;
	private static ArrayList<Integer> mLeaseNumbers = null;

	public static void main(String[] args) {
		Student studentOj = null; 
		Credentials credentialObj = null;
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
					studentOj = new Student();
					credentialObj = new Credentials();

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
	
							if(authenticateUser(credentialObj.personId,credentialObj.password,credentialObj))
							{
								//credentialObj.designation.equalsIgnoreCase(Constants.STUDENT)
								if(true)
								{
									// This is the first Level of Student Hierarchy
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
																System.out.println("1. View Current Invoice");
																System.out.println("2. View Former Invoices");
																System.out.println("3. Back");
																choice = inputObj.nextInt();
																
																switch(choice)
																{
																	case 1:
																		displayCurrentInvoice(credentialObj.personId);
																		break;
																	case 2:
																		displayListOfInvociesForAPerson(credentialObj.personId);
																		int count = 1;
																		
																		while(mLevelFour)
																		{
																			// Checking if the ArrayList indeed has some invoice numbers in it else move out
																			if(!mInvoiceNumbers.isEmpty())
																			{
																				for(Integer item : mInvoiceNumbers)
																				{
																					System.out.println(count+". "+item.intValue());
																					count++;
																				}
																				System.out.println("0. Back");
																				choice = inputObj.nextInt();
																				if(choice == 0)
																				{
																					// Taking back to Level Three
																					mLevelFour = false;
																				}
																				else
																				{
																					displayInvoiceDetails(credentialObj.personId, mInvoiceNumbers.get(choice-1));
																				}
																			}
																			else
																			{
																				System.out.println("No previous invoices");
																			}
																		}
																		count = 0;
																		// Setting true so that we can come back to Level Four
																		mLevelFour = true;
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
															while(mLevelThree)
															{
																System.out.println("1. View Current Lease");
																System.out.println("2. View Former Leases");
																System.out.println("3. Back");
																choice = inputObj.nextInt();
																
																switch(choice)
																{
																	case 1:
																		displayCurrentLease(credentialObj.personId);
																		break;
																	case 2:
																		displayListOfLeasesForAPerson(credentialObj.personId);
																		int count = 1;
																		//asdasdas
																		while(mLevelFour)
																		{
																			// Checking if the ArrayList indeed has some lease numbers in it else move out
																			if(!mLeaseNumbers.isEmpty())
																			{
																				for(Integer item : mLeaseNumbers)
																				{
																					System.out.println(count+". "+item.intValue());
																					count++;
																				}
																				System.out.println("0. Back");
																				choice = inputObj.nextInt();
																				if(choice == 0)
																				{
																					// Taking back to Level Three
																					mLevelFour = false;
																				}
																				else
																				{
																					displayLeaseDetails(credentialObj.personId, mLeaseNumbers.get(choice-1));
																				}
																			}
																			else
																			{
																				System.out.println("No previous leases");
																			}
																			count = 0;
																		}
																		// Setting true so that we can come back to Level Four
																		mLevelFour = true;
																		break;
																	case 3:
																		// Taking back to Level Two
																		mLevelThree = false;
																		break;
																}
															}
															// Setting true so that we can come back in the third level
															mLevelThree = true;															break;
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

	private static void displayLeaseDetails(int personId, Integer leaseNumber) {
		System.out.println("The lease number for person ID "+personId+ " is "+leaseNumber);
	}

	private static void displayListOfLeasesForAPerson(int personId) {
		mLeaseNumbers = new ArrayList<Integer>();
		for (int i = 1; i < 11; i++) {
			mLeaseNumbers.add(i);
		}
		
	}

	/***********************************************************************************************
	 * @param personId
	 * @action Displays lease number, duration of the lease, name of student, matriculation number of student,
	 * place number, room number, Hall address or Student apartment address, Date of moving in and if 
	 * present date of leaving the room
	 ***********************************************************************************************/
	private static void displayCurrentLease(int personId) {
		ResultSet currentLease = null;
		/*Write query for displaying :
		 *ease number, duration of the lease, name of student, matriculation number of student,
		 * place number, room number, Hall address or Student apartment address, Date of moving in and if 
		 * present date of leaving the room */

	}

	/***********************************************************************************************
	 * @param personId
	 * @throws SQLException 
	 * @action Displays list of invoices for a particular person_id, and stores the data from Result set into an ArrayList<Integer> 
	 * Once the ArrayList is populated we then print all the invoices for a person and ask him to choose to print one particular invoice
	 * Based on his input we get the invoice_no from the Array List and then use that to fetch details from that particular invoice_no
	 *  
	 *  @Tables 
	 ***********************************************************************************************/
	private static void displayListOfInvociesForAPerson(int personId) throws SQLException {
		/*Write SQL query to fetch:
		 * Invoice numbers for a particular person using his person_id*/
	
		/*ResultSet listOfInvoices = null;
		mInvoiceNumbers = new ArrayList<Integer>();
		while(listOfInvoices.next())
		{
			mInvoiceNumbers.add(listOfInvoices.getInt("invoice_no"));
		}
		*/
		mInvoiceNumbers = new ArrayList<Integer>();
		for (int i = 11; i < 21; i++) {
			mInvoiceNumbers.add(i);
		}
		
	}

	/***********************************************************************************************
	 * @param personId
	 * @param invoiceNumber
	 * @action Displays Lease number, Payment due, Due date, Student's Full name, Student Id, 
	 *  Place number, Room number, Name of Hall, Apartment, Date of Invoice Paid, Method of Payment
	 *  for a particular Invoice number 
	 **********************************************************************************************/
	private static void displayInvoiceDetails(int personId, Integer invoiceNumber) {
		// TODO Auto-generated method stub
		/*Write SQL Query to find above mentioned details in @action in method description
		 *  for the given invoiceNumber*/
		System.out.println("Invoice number for person id "+personId+" is "+invoiceNumber);

	}

	/***********************************************************************************************
	 *  @param personId
	 *  @action Displays Lease number, Payment due, Due date, Student's Full name, Student Id, 
	 *  Place number, Room number, Name of Hall, Apartment, Date of Invoice Paid, Method of Payment
	 *  
	 *  @Tables 
	 **********************************************************************************************/ 
	private static void displayCurrentInvoice(int personId) {

		/* Write SQL query to fetch:
		 * Lease number, Payment due, Due date, Student's Full name, Student Id, 
		 * Place number, Room number, Name of Hall, Apartment, Date of Invoice Paid, Method of Payment
		 * 
		 * Once the ResultSet is filled with data, depending upon how many ResultSets are used, we 
		 * can go ahead with the logic	
		 */	


	}


	/***********************************************************************************************
	 * @param userId
	 * @param password
	 * @param statement
	 * @param object
	 * @return True if the user has been authenticated , False if the authentication failed
	 * @throws SQLException
	 ***********************************************************************************************/
	private static boolean authenticateUser(int userId, String password, Credentials object) throws SQLException 
	{
		/* Write SQL query to check if userName and password match the Credentials Table in Database
		 If matches retrieve designation and person_id
		 */		
		/*ResultSet user = null;
		
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
