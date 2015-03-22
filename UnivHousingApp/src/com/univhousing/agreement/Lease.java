package com.univhousing.agreement;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Lease {

	public int leaseNo;
	public float deposit;
	public String modeOfPayment;
	public int duration;
	public Date cutOffDate;

	

	

	public void displayLeaseDetails(int personId, Integer leaseNumber) {
		/*Write SQL Query to display the details of a particular lease according to Project requirments*/
	}

	public void displayListOfLeasesForAPerson(int personId,ArrayList<Integer> leaseNumbers) throws SQLException {
		
		/*Write SQL Query to display a list of leases for a person id*/
		
		/*ResultSet listOfLeases = null;
		leaseNumbers  = new ArrayList<Integer>();
		while(listOfLeases.next())
		{
			leaseNumbers.add(listOfLeases.getInt("lease_no"));
		}*/
		leaseNumbers.clear();
		for (int i = 10; i < 20; i++) {
			leaseNumbers.add(i);
		}
		
	}

	/***********************************************************************************************
	 * @param personId
	 * @action Displays lease number, duration of the lease, name of student, matriculation number of student,
	 * place number, room number, Hall address or Student apartment address, Date of moving in and if 
	 * present date of leaving the room
	 ***********************************************************************************************/
	public void displayCurrentLease(int personId) {
		ResultSet currentLease = null;
		/*Write query for displaying :
		 *ease number, duration of the lease, name of student, matriculation number of student,
		 * place number, room number, Hall address or Student apartment address, Date of moving in and if 
		 * present date of leaving the room */

	}
	
	
}
