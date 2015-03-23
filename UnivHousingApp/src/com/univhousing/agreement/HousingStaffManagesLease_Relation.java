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
}
