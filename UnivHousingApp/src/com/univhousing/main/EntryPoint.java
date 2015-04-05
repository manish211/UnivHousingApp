package com.univhousing.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.ws.Action;

import com.univhousing.agreement.HousingStaffManagesLease_Relation;
import com.univhousing.agreement.Lease;
import com.univhousing.agreement.LeaseRequest_Relation;
import com.univhousing.agreement.TerminationRequest_Relation;
import com.univhousing.invoice.InvoicePersonLease_Relation;
import com.univhousing.invoice.TicketPersonStaff_Relation;
import com.univhousing.main.ConnectionUtils;
import com.univhousing.main.Constants;
import com.univhousing.main.Credentials;
import com.univhousing.parking.ParkingLot;
import com.univhousing.parking.StudentParkingSpot_Relation;
import com.univhousing.users.Guest;
import com.univhousing.users.Person;
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
	private static ArrayList<Integer> mTicketNumbers = null;
	private static ArrayList<Integer> mAdminLevelLeaseRequests = null;
	private static ArrayList<Integer> mAdminLevelTerminationRequests = null;
	private static ArrayList<Integer> mAdminLevelMaintenanceRequests = null;
	private static ArrayList<Integer> mAdminLevelParkingRequests = null;
	private static Scanner inputObj = new Scanner(System.in);
	private static int studentId;

	public static void main(String[] args) {
		Student studentObj = null; 
		Credentials credentialObj = null;
		Person personObj = null;
		try {
			Connection connectionObj = null;
			Statement statementObj = null;
			ResultSet resultSetObj = null;
			mInvoiceNumbers = new ArrayList<Integer>();
			mLeaseNumbers = new ArrayList<Integer>();
			mTicketNumbers = new ArrayList<Integer>();
			mAdminLevelLeaseRequests = new ArrayList<Integer>();
			mAdminLevelTerminationRequests = new ArrayList<Integer>();
			mAdminLevelMaintenanceRequests = new ArrayList<Integer>();
			mAdminLevelParkingRequests = new ArrayList<Integer>();

			try {
				int choice;

				System.out.println("Welcome to University Housing\n");				

				// Going into infinite loop until exited
				while(mLevelZero) {
					studentObj = new Student();
					credentialObj = new Credentials();
					personObj = new Person();

					System.out.println("\n1. Login");
					System.out.println("2. Guest Login");
					System.out.println("3. Exit\n");
					choice = inputObj.nextInt();

					switch (choice) 
					{
						case 1:
							System.out.print("\nEnter University Id++:");
							studentId = inputObj.nextInt();
	
							System.out.print("\nEnter Password:");
							credentialObj.password = inputObj.next();
							// Fetch the person id for the student id and use that for login
							credentialObj.personId = studentObj.getPersonIdForStudentId(studentId);
							if(authenticateUser(credentialObj.personId,credentialObj.password,credentialObj))
							{
								
								credentialObj.designation = studentObj.getStudentDesignation(credentialObj.personId);
								
								if(credentialObj.designation.equalsIgnoreCase(Constants.STUDENT))
								{
									// This is the first Level of Student Hierarchy
									
									System.out.println("mLevelOne:="+mLevelOne);
									while(mLevelOne)
									{
										System.out.println("Inside");
										// Show different menu items
										System.out.println("\n1. Housing Option");
										System.out.println("2. Parking Option");
										System.out.println("3. Maintenance");
										System.out.println("4. Profile");
										System.out.println("5. Back\n");
										
										choice = inputObj.nextInt();
										switch (choice) 
										{
											case 1:
												System.out.println("Showing Housing Option");
												// This is the second level of Student hierarchy where he is in Housing Options
												while(mLevelTwo)
												{
													/*Runtime.getRuntime().exec("clear");*/
													System.out.println("\n1. View Invoices");
													System.out.println("2. View Leases");
													System.out.println("3. New Request");
													System.out.println("4. View/Cancel Requests");
													System.out.println("5. View Vacancy");
													System.out.println("6. Back\n");
													choice = inputObj.nextInt();
													InvoicePersonLease_Relation invoicePersonObj = new InvoicePersonLease_Relation();
													switch(choice)
													{
														case 1:
															// This is the third level of Student hierarchy where he is checking Invoices
															while(mLevelThree)
															{
																/*Runtime.getRuntime().exec("clear");*/
																System.out.println("\n1. View Current Invoice");
																System.out.println("2. View Former Invoices");
																System.out.println("3. Back\n");
																choice = inputObj.nextInt();
																
																switch(choice)
																{
																	case 1:
																		invoicePersonObj.displayCurrentInvoice(credentialObj.personId);
																		break;
																	case 2:
																		invoicePersonObj.displayListOfInvociesForAPerson(credentialObj.personId, mInvoiceNumbers);
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
															/*Runtime.getRuntime().exec("clear");*/
															System.out.println("Showing Leases");
															while(mLevelThree)
															{
																System.out.println("\n1. View Current Lease");
																System.out.println("2. View Former Leases");
																System.out.println("3. Back\n");
																choice = inputObj.nextInt();
																Lease leaseObj = new Lease();
																switch(choice)
																{
																	case 1:
																		leaseObj.displayCurrentLease(credentialObj.personId);
																		break;
																	case 2:
																		
																		leaseObj.displayListOfLeasesForAPerson(credentialObj.personId,mLeaseNumbers);
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
															/*Runtime.getRuntime().exec("clear");*/
															while(mLevelFour)
															{
																System.out.println("\n1. New Lease Request");
																System.out.println("2. Terminate Lease Request");
																System.out.println("3. Back\n");

																LeaseRequest_Relation leaseRequestObj = new LeaseRequest_Relation();
																TerminationRequest_Relation terminationRequestObj = new TerminationRequest_Relation();
																choice = inputObj.nextInt();

																switch(choice)
																{
																	case 1:
																		leaseRequestObj.generateNewLeaseRequest(credentialObj.personId);
																		break;
																	case 2:
																		terminationRequestObj.generateLeaseTerminationRequest(credentialObj.personId);
																		break;
																	case 3:
																		mLevelFour = false;
																		break;
																}
															}
															mLevelFour = true;
															break;
														case 4:
															System.out.println("Showing/Viewing Cancel Requests");
															/*Runtime.getRuntime().exec("clear");*/
															while(mLevelFour)
															{
																System.out.println("\n1. View Request");	
																System.out.println("2. Cancel Request");
																System.out.println("3. Back\n");
																choice = inputObj.nextInt();
																Lease leaseObj = new Lease();
																
																switch(choice)
																{
																	case 1:
																		leaseObj.viewAllRequests(credentialObj.personId);
																		break;
																	case 2:
																		boolean isCancelSuccessful = false;
																		int requestNumber;
																		while(!isCancelSuccessful)
																		{
																			System.out.println("Enter the Request Number to cancel or press 0 for previous menu: \n");
																			System.out.println("0. Back\n");
																			requestNumber = inputObj.nextInt();
																			if(requestNumber == 0)
																				break;
																			isCancelSuccessful = leaseObj.cancelRequest(credentialObj.personId, requestNumber);
																		}
																		break;
																	case 3:
																		mLevelFour = false;
																		break;
																}
															}
															mLevelFour = true;
															break;
														case 5:
															System.out.println("Showing Vacancy\n");

															Lease leaseObj = new Lease();
															leaseObj.viewAccomodationVacancies();
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
												/*Runtime.getRuntime().exec("clear");*/
												ParkingLot parkingLotObj = new ParkingLot();
												while(mLevelTwo)
												{
													System.out.println("\n1. Request New Parking Spot");
													System.out.println("2. View Parking Lot Information");
													System.out.println("3. View Current Parking Spot");
													System.out.println("4. Renew Parking Spot");
													System.out.println("5. Return Parking Spot");
													System.out.println("6. View Request Status");
													System.out.println("7. Back\n");
													
													choice = inputObj.nextInt();
													switch (choice) 
													{
														case 1:System.out.println("Showing new Parking spot\n");
														
															parkingLotObj.generateAParkingSpot(credentialObj.personId);
															break;
															
														case 2:System.out.println("Showing View Parking Lot information\n");
															parkingLotObj.displayInfoForParkingLots(credentialObj.personId);
															break;
															
														case 3:System.out.println("Showing View Current Parking Spot\n");
															parkingLotObj.viewCurrentParkingSpot(credentialObj.personId);
															break;
															
														case 4:System.out.println("Showing Renew Parking Spot\n");
															parkingLotObj.renewParkingSpot(credentialObj.personId);
															break;
															
														case 5: System.out.println("Showing Return Parking Spot\n");
															parkingLotObj.returnParkingSpot(credentialObj.personId);
															break;

														case 6: System.out.println("Showing View Request Status\n");
															parkingLotObj.getRequestStatus(credentialObj.personId);
															break;
														case 7:
															mLevelTwo = false;
															break;
	
														default: System.out.println("Invalid Choice\n");
															break;
													}
												}
												mLevelTwo = true;
												break;
											case 3:
												System.out.println("Showing Maintenance");
												/*Runtime.getRuntime().exec("clear");*/
												TicketPersonStaff_Relation ticketObj = new TicketPersonStaff_Relation();
												while(mLevelTwo)
												{
													System.out.println("\n1. New Ticket");
													System.out.println("2. View Ticket Status");
													System.out.println("3. Back\n");
													choice = inputObj.nextInt();
													
													switch (choice) 
													{
														case 1:
															System.out.println("Showing New Ticket");
															ticketObj.raiseNewTicket(credentialObj.personId);
															break;
	
														case 2:
															System.out.println("Showing View Ticket Status");
															ticketObj.viewAllTicketStatuses(credentialObj.personId, mTicketNumbers);
															break;

														case 3:
															System.out.println("Back\n");
															mLevelTwo = false;
															break;

														default: System.out.println("Invalid Choice");
														break;
													}
												}
												mLevelTwo = true;
												break;
											case 4:
												System.out.println("Showing Profile\n");
												personObj = new Person();

												while(mLevelTwo)
												{
													System.out.println("\n1. View Profile");
													System.out.println("2. Update Profile");
													System.out.println("3. Back\n");
													choice = inputObj.nextInt();
													
													switch (choice) 
													{
														case 1: System.out.println("Showing View Profile");
															personObj.viewProfileDetails(studentId);
															break;
	
														case 2: System.out.println("Showing Update Profile");
														personObj.updateProfile(credentialObj.personId);
															break;
															
														case 3:
															System.out.println("Shwowing Back");
															mLevelTwo = false;

														default:
															break;
													}
													
												}
												mLevelTwo = true;
												break;
											case 5: 
												// Taking back to Level zero
												mLevelOne = false;
												break;
											default:
												break;
										}
									}
									mLevelOne =true;
								}
								else if(credentialObj.designation.equalsIgnoreCase(Constants.SUPERVISOR))
								{
									// Supervior/Admin code goes here.
									HousingStaffManagesLease_Relation housingLeaseObj = new HousingStaffManagesLease_Relation();
									TicketPersonStaff_Relation ticketStaffObj = new TicketPersonStaff_Relation();
									ParkingLot parkingObj = new ParkingLot();
									
									while(mLevelOne)
									{
										System.out.println("\n1. View New Lease Requests");
										System.out.println("2. View Terminate Lease Requests");
										System.out.println("3. View Maintenance Tickets");
										System.out.println("4. View Parking Requests");
										System.out.println("5. Profile");
										System.out.println("6. Generate Invoices");
										System.out.println("7. Check the Completed Leases");
										System.out.println("8. Back\n");
										
										choice = inputObj.nextInt();
										
										switch (choice) 
										{
											case 1:
												System.out.println("Showing New Lease Requests");
												housingLeaseObj.getAllNewLeaseRequests(mAdminLevelLeaseRequests);
												break;
											
											case 2:
												System.out.println("Showing Termination Lease Requests");
												housingLeaseObj.getAllNewTerminationRequests(mAdminLevelTerminationRequests);
												break;
												
											case 3:
												System.out.println("Showing Maintenance Tickets");
												ticketStaffObj.getAllMaintenanceTickets(mAdminLevelMaintenanceRequests);
												break;
											
											case 4:
												System.out.println("Showing Parking Requests");
												parkingObj.getAllParkingRequests(mAdminLevelParkingRequests);
												break;
											case 5:
												System.out.println("Showing Profile\n");
												personObj = new Person();

												while(mLevelTwo)
												{
													System.out.println("\n1. View Profile");
													System.out.println("2. Update Profile");
													System.out.println("3. Back\n");
													choice = inputObj.nextInt();
													
													switch (choice) 
													{
														case 1: System.out.println("Showing View Profile");
															personObj.viewProfileDetails(credentialObj.personId);
															break;
	
														case 2: System.out.println("Showing Update Profile");
														personObj.updateProfile(credentialObj.personId);
															break;
															
														case 3:
															System.out.println("Shwowing Back");
															mLevelTwo = false;
														default:
															break;
													}
													
												}
												mLevelTwo = true;
												 break;
											
											case 6:
												System.out.println("Showing Invoice lease generation");
												System.out.println("Enter the date you want to generate invoices for: ");
												String invoiceGenerationDate = inputObj.nextLine();
												InvoicePersonLease_Relation invoiceObj = new InvoicePersonLease_Relation();
												invoiceObj.generateLeasesForGivenDate(invoiceGenerationDate);
												break;
											
											case 7: System.out.println("Checking for the Completed leases");
													housingLeaseObj.checkForLeaseCompletion();
													break;
											
											case 8:
												mLevelOne = false;
												break;
	
											default: System.out.println("Invalid Choice");
												break;
										}
									}
								}
							}
							else
							{
								System.out.println("Login Incorrect\n");
							}
							// Setting true so that we can come back inside level one
							mLevelOne = true;
							break;
/*----------------------------------------------------------------------------------------------------------------------------------------------------
 * ----------------------------------------------------------------------------------------------------------------------------------------------------
 * ----------------------------------------------------------------------------------------------------------------------------------------------------
 * ----------------------------------------------------------------------------------------------------------------------------------------------------
 * ----------------------------------------------------------------------------------------------------------------------------------------------------
 * ----------------------------------------------------------------------------------------------------------------------------------------------------
 * ----------------------------------------------------------------------------------------------------------------------------------------------------
 * ----------------------------------------------------------------------------------------------------------------------------------------------------
 * ----------------------------------------------------------------------------------------------------------------------------------------------------
 * ----------------------------------------------------------------------------------------------------------------------------------------------------*/	
						case 2:

							// Do something
							System.out.println("Enter Approval Id or Press 2 to go Back:");
							int approvalId = inputObj.nextInt();
							if(approvalId == 2)
								return;
							else
							{
								Guest guestObj = new Guest();
								credentialObj.personId = guestObj.getPersonIdFromApprovalId(approvalId);
								//credentialObj.personId = 3;
								// This is the first Level of Guest Hierarchy
								while(mLevelOne)
								{
									// Show different menu items
									/*Runtime.getRuntime().exec("clear");*/
									System.out.println("\n1. Housing Option");
									System.out.println("2. Parking Option");
									System.out.println("3. Maintenance");
									System.out.println("4. Profile");
									System.out.println("5. Back\n");
									
									choice = inputObj.nextInt();
									switch (choice) 
									{
										case 1:
											System.out.println("Showing Housing Option");
											// This is the second level of Guest hierarchy where he is in Housing Options
											while(mLevelTwo)
											{
												/*Runtime.getRuntime().exec("clear");*/
												System.out.println("\n1. View Invoices");
												System.out.println("2. View Leases");
												System.out.println("3. New Request");
												System.out.println("4. View/Cancel Requests");
												System.out.println("5. View Vacancy");
												System.out.println("6. Back\n");
												choice = inputObj.nextInt();
												InvoicePersonLease_Relation invoicePersonObj = new InvoicePersonLease_Relation();
												switch(choice)
												{
													case 1:
														// This is the third level of Guest hierarchy where he is checking Invoices
														while(mLevelThree)
														{
															/*Runtime.getRuntime().exec("clear");*/
															System.out.println("\n1. View Current Invoice");
															System.out.println("2. View Former Invoices");
															System.out.println("3. Back\n");
															choice = inputObj.nextInt();
															
															switch(choice)
															{
																case 1:
																	invoicePersonObj.displayCurrentInvoice(credentialObj.personId);
																	break;
																case 2:
																	invoicePersonObj.displayListOfInvociesForAPerson(credentialObj.personId, mInvoiceNumbers);
																	int count = 1;
																	/*Runtime.getRuntime().exec("clear");*/
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
																			count = 0;
																			System.out.println("0. Back");
																			System.out.println("\n");
																			choice = inputObj.nextInt();
																			if(choice == 0)
																			{
																				// Taking back to Level Three
																				mLevelFour = false;
																			}
																			else
																			{
																				invoicePersonObj.displayInvoiceDetails(credentialObj.personId, mInvoiceNumbers.get(choice-1));
																			}
																		}
																		else
																		{
																			System.out.println("No previous invoices\n");
																			break;
																		}
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
														mLevelThree = true;
														break;
													case 2:
														/*Runtime.getRuntime().exec("clear");*/
														System.out.println("Showing Leases");
														while(mLevelThree)
														{
															System.out.println("\n1. View Current Lease");
															System.out.println("2. View Former Leases");
															System.out.println("3. Back\n");
															choice = inputObj.nextInt();
															Lease leaseObj = new Lease();
															switch(choice)
															{
																case 1:
																	leaseObj.displayCurrentLease(credentialObj.personId);
																	break;
																case 2:
																	
																	leaseObj.displayListOfLeasesForAPerson(credentialObj.personId,mLeaseNumbers);
																	int count = 1;
																	/*Runtime.getRuntime().exec("clear");*/
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
																			count = 0;
																			System.out.println("0. Back\n");
																			choice = inputObj.nextInt();
																			if(choice == 0)
																			{
																				// Taking back to Level Three
																				mLevelFour = false;
																			}
																			else
																			{
																				leaseObj.displayLeaseDetails(credentialObj.personId, mLeaseNumbers.get(choice-1));
																			}
																		}
																		else
																		{
																			System.out.println("No previous leases\n");
																		}
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
														/*Runtime.getRuntime().exec("clear");*/
														while(mLevelFour)
														{
															System.out.println("\n1. New Lease Request");
															System.out.println("2. Terminate Lease Request");
															System.out.println("3. Back\n");

															LeaseRequest_Relation leaseRequestObj = new LeaseRequest_Relation();
															TerminationRequest_Relation terminationRequestObj = new TerminationRequest_Relation();
															choice = inputObj.nextInt();

															switch(choice)
															{
																case 1:
																	leaseRequestObj.generateNewLeaseRequest(credentialObj.personId);
																	break;
																case 2:
																	terminationRequestObj.generateLeaseTerminationRequest(credentialObj.personId);
																	break;
																case 3:
																	mLevelFour = false;
																	break;
															}
														}
														mLevelFour = true;
														break;
													case 4:
														System.out.println("Showing/Viewing Cancel Requests");
														/*Runtime.getRuntime().exec("clear");*/
														while(mLevelFour)
														{
															System.out.println("\n1. View Request");	
															System.out.println("2. Cancel Request");
															System.out.println("3. Back\n");
															choice = inputObj.nextInt();
															Lease leaseObj = new Lease();
															
															switch(choice)
															{
																case 1:
																	leaseObj.viewAllRequests(credentialObj.personId);
																	break;
																case 2:
																	System.out.println("Enter the Request Number to cancel: \n");
																	int requestNumber = inputObj.nextInt();
																	leaseObj.cancelRequest(credentialObj.personId, requestNumber);
																	break;
																case 3:
																	mLevelFour = false;
																	break;
															}
														}
														mLevelFour = true;
														break;
													case 5:
														System.out.println("Showing Vacancy\n");

														Lease leaseObj = new Lease();
														leaseObj.viewAccomodationVacancies();
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
											/*Runtime.getRuntime().exec("clear");*/
											ParkingLot parkingLotObj = new ParkingLot();
											while(mLevelTwo)
											{
												System.out.println("\n1. Request New Parking Spot");
												System.out.println("2. View Parking Lot Information");
												System.out.println("3. View Current Parking Spot");
												System.out.println("4. Renew Parking Spot");
												System.out.println("5. Return Parking Spot");
												System.out.println("6. View Request Status");
												System.out.println("7. Back\n");
												
												choice = inputObj.nextInt();
												switch (choice) 
												{
													case 1:System.out.println("Showing new Parking spot\n");
													
														parkingLotObj.generateAParkingSpot(credentialObj.personId);
														break;
														
													case 2:System.out.println("Showing View Parking Lot information\n");
														parkingLotObj.displayInfoForParkingLots(credentialObj.personId);
														break;
														
													case 3:System.out.println("Showing View Current Parking Spot\n");
														parkingLotObj.viewCurrentParkingSpot(credentialObj.personId);
														break;
														
													case 4:System.out.println("Showing Renew Parking Spot\n");
														parkingLotObj.renewParkingSpot(credentialObj.personId);
														break;
														
													case 5: System.out.println("Showing Return Parking Spot\n");
														parkingLotObj.returnParkingSpot(credentialObj.personId);
														break;

													case 6: System.out.println("Showing View Request Status\n");
														parkingLotObj.getRequestStatus(credentialObj.personId);
														break;
													case 7:
														mLevelTwo = false;
														break;

													default: System.out.println("Invalid Choice\n");
														break;
												}
											}
											mLevelTwo = true;
											break;
										case 3:
											System.out.println("Showing Maintenance");
											/*Runtime.getRuntime().exec("clear");*/
											TicketPersonStaff_Relation ticketObj = new TicketPersonStaff_Relation();
											while(mLevelTwo)
											{
												System.out.println("\n1. New Ticket");
												System.out.println("2. View Ticket Status");
												System.out.println("3. Back\n");
												choice = inputObj.nextInt();
												
												switch (choice) 
												{
													case 1:
														System.out.println("Showing New Ticket");
														ticketObj.raiseNewTicket(credentialObj.personId);
														break;

													case 2:
														System.out.println("Showing View Ticket Status");
														ticketObj.viewAllTicketStatuses(credentialObj.personId, mTicketNumbers);
														break;

													case 3:
														System.out.println("Back\n");
														mLevelTwo = false;
														break;

													default:
														break;	
												}
											}
											mLevelTwo = true;
											break;
										case 4:
											System.out.println("Showing Profile\n");
											personObj = new Person();

											while(mLevelTwo)
											{
												System.out.println("\n1. View Profile");
												System.out.println("2. Update Profile");
												System.out.println("3. Back\n");
												choice = inputObj.nextInt();
												
												switch (choice) 
												{
													case 1: System.out.println("Showing View Profile");
														personObj.viewProfileDetails(credentialObj.personId);
														break;

													case 2: System.out.println("Showing Update Profile");
													personObj.updateProfile(credentialObj.personId);
														break;
														
													case 3:
														System.out.println("Shwowing Back");
														mLevelTwo = false;

													default:
														break;
												}
												
											}
											mLevelTwo = true;
											break;
										case 5: 
											// Taking back to Level zero
											mLevelOne = false;
											break;
										default:
											break;
									}
								}
								mLevelOne =true;
							}
							break;
	
						case 3: // Exit menu
							mLevelZero = false;
							break;
	
						default: System.out.println("Invalid Choice\n");
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

	/***********************************************************************************************
	 * @param userId
	 * @param password
	 * @param statement
	 * @param object
	 * @return True if the user has been authenticated , False if the authentication failed
	 * @throws SQLException
	 ***********************************************************************************************/
	private static boolean authenticateUser(int userId, String password, Credentials credentials) throws SQLException 
	{
		/* Write SQL query to check if userName and password match the Credentials Table in Database
		 If matches retrieve designation and person_id
		 */		
		/*ResultSet user = null;
		 * 
		
		while(user.next())
		{
			credentials.personId = user.getInt("person_id");
			credentials.designation = user.getString("designation");

			// Returning true, because if the 
			return true;
		}
		return false;*/
		
		
		Boolean isUserAuthenticated = false;
		
		Connection dbConnection = ConnectionUtils.getConnection();
		
		String selectQuery = "SELECT * FROM login_credentials WHERE person_id = ? AND password = ?" ;
		
		PreparedStatement preparedStatement = dbConnection.prepareStatement(selectQuery);
		
		preparedStatement.setInt(1,userId);
		preparedStatement.setString(2,password);
		
		ResultSet rs = preparedStatement.executeQuery();
		
		//If record exists , rs.next() will evaluate to true
		if(rs.next())
			isUserAuthenticated = true;
		
		return isUserAuthenticated;
			
	}
}