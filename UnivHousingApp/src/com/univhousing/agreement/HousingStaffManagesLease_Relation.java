package com.univhousing.agreement;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import com.sun.corba.se.pept.encoding.InputObject;

public class HousingStaffManagesLease_Relation {

	public int leaseNo;
	public int duration;
	public Date cutOffDate;
	public String modeOfPayment;
	public float depsoit;
	public Date date;
	public int staffNo;

	Scanner inputObj = new Scanner(System.in);

	/**
	 * @param ArrayList<Integer> allLeaseRequestsToMonitor
	 * @throws SQLException 
	 * @action This fetches all the new lease requests submitted for approval
	 */
	public void getAllNewLeaseRequests(ArrayList<Integer> allLeaseRequestsToMonitor) throws SQLException {

		/*Write SQL Query to fetch all the lease requests pending for approval*/

		ResultSet allRequests = null;
		String accomodationType = "";
		allLeaseRequestsToMonitor.clear();
		while(allRequests.next())
		{
			allLeaseRequestsToMonitor.add(allRequests.getInt("application_request_no"));
		}
		
		System.out.println("Displaying all the requests to approve: ");
		for (int i = 0; i < allLeaseRequestsToMonitor.size(); i++) 
		{
			System.out.println((i+1)+". "+allLeaseRequestsToMonitor.get(i));
		}
		int requestChosen = inputObj.nextInt();
		int requestNumber = allLeaseRequestsToMonitor.get(requestChosen-1);
		
		/*Write SQL Query to fetch all the details of the requestNumber*/
		ResultSet requestDetails = null;
		
		
		System.out.println("Do you want to approve this request? Y/N");
		String approvalStatus = inputObj.next();
		
		/*Now we look whether the accomodation_type provided by the student is available or not
		 * If YES: Assign it to him and update the respective tables
		 * If NO: Request will already be approved, but the request status will be changed to PENDING
		 * NOTE: accomodationType has to be taken from the request details*/
		
		boolean accAvailability = checkIfAccomodationTypeAvailable(accomodationType);
		if(accAvailability)
		{
			// Now we will give him the accommodation he wanted
			/*Write SQL Query to update his records in the tables necessary
			 * Student should be alloted a room number and a palce number*/
			ResultSet approveAndAssignAccomodation = null;
		}
		else
		{
			/* First we will write a query to approve the status, irrespective of whether
			   there is accommodation available for that accommodation type	*/

			/*Write SQL Query to change the status of the request to waiting list,
			 * this can be done from table PERSON_ACC_STAFF (not sure)*/
			ResultSet changeRequestStatus = null;
		}
	}

	/**
	 * @param accomodationType
	 * @return True if accomodationType is present else False
	 */
	private boolean checkIfAccomodationTypeAvailable(String accomodationType) {
		/* Write SQL Query to check if the accommodation type student selected is actually available
		 * If YES: return True
		 * If NO: return False*/
		return false;
	}

	/**
	 * @param ArrayList<Integer> adminLevelTerminationRequests
	 * @throws SQLException 
	 */
	public void getAllNewTerminationRequests(ArrayList<Integer> allTerminationRequestsToMonitor) throws SQLException {
		/* Write SQL Query to fetch all the termination requests  */
		ResultSet allRequests = null;
		allTerminationRequestsToMonitor.clear();
		while(allRequests.next())
		{
			allTerminationRequestsToMonitor.add(allRequests.getInt("termination_request_number"));
		}
		
		System.out.println("Displaying all the requests to approve: ");
		for (int i = 0; i < allTerminationRequestsToMonitor.size(); i++) 
		{
			System.out.println((i+1)+". "+allTerminationRequestsToMonitor.get(i));
		}
		int requestChosen = inputObj.nextInt();
		int requestNumber = allTerminationRequestsToMonitor.get(requestChosen-1);
		
		/*Write SQL Query to fetch all the details of the requestNumber*/
		ResultSet requestDetails = null;
		
		int damageFees = 0;
		System.out.println("Please enter the damage fees:");
		damageFees = inputObj.nextInt();
		
		/*Write SQL Trigger to change the status of request to Complete after the inspection date*/
		
		
		/*Write SQL Query to fetch the latest unpaid invoice and add the damageFees to already existing payment_due*/
		
	}	
	
	
	
}
