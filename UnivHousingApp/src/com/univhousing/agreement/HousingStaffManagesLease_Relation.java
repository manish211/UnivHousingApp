package com.univhousing.agreement;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HousingStaffManagesLease_Relation {

	public int leaseNo;
	public int duration;
	public Date cutOffDate;
	public String modeOfPayment;
	public float depsoit;
	public Date date;
	public int staffNo;

	/**
	 * @param personId
	 * @throws SQLException 
	 * @action Displays all the requests made by the student along with their status and request number
	 */
	public void viewAllRequests(int personId) throws SQLException
	{
		ResultSet viewRequestsSet = null;
		
		while(viewRequestsSet.next())
		{
			int requestNumber = viewRequestsSet.getInt("request_no");

			/*Write an SQL Query for fetching details of each value of requestNumber variable*/
		}
	}

	/**
	 * @param personId
	 * @param requestNumber
	 * @action Deletes the requestNumber for the personID  
	 */
	public void cancelRequest(int personId, int requestNumber) {
		
		/*Write SQL Query to delete the requestNumber mentioned by the PersonId and set the status as "Cancelled"*/
		ResultSet cancelRequest = null;
		
	}

	/**
	 * @action Pulls up all the accommodations that are vacant right now i.e. not occupied by any student or family
	 */
	public void viewAccomodationVacancies() {
		/*Write SQL Query to pull up all the vacancies to display the student*/
		ResultSet displayAllVancancies = null;
	}
}
