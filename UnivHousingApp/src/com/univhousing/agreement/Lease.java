package com.univhousing.agreement;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.univhousing.main.ConnectionUtils;

public class Lease {

	public int leaseNo;
	public float deposit;
	public String modeOfPayment;
	public int duration;
	public Date cutOffDate;

	/**
	 * @param personId
	 * @param leaseNumber
	 * @action Display the lease details of a particular lease number corresponding to a person id
	 */ 
	public void displayLeaseDetails(int personId, Integer leaseNumber) {
		/*Write SQL Query to display the details of a particular lease according to Project requirments*/
	}

	public void displayListOfLeasesForAPerson(int personId,ArrayList<Integer> leaseNumbers) throws SQLException {

		/*Write SQL Query to display a list of leases for a person id*/

		/*ResultSet listOfLeases = null;
		 leaseNumbers.clear();
		while(listOfLeases.next())
		{
			leaseNumbers.add(listOfLeases.getInt("lease_no"));
		}*/

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

		ResultSet rs = null;
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		try {
			dbConnection = ConnectionUtils.getConnection();

			String selectQuery = "SELECT T2_lease_person.accomodation_type,T1_person.first_name, T2_lease_person.lease_no, T3_lease.duration "
					+ "FROM PERSON T1_person, PERSON_ACCOMODATION_LEASE T2_lease_person,LEASE T3_lease "
					+ "WHERE T1_person.person_id = ? AND T1_person.person_id = T2_lease_person.person_id "
					+ "AND	T3_lease.lease_no = T2_lease_person.lease_no ";

//Issue 1- There is no date to compare which is the latest record?? -- Schema change required!!!
//Issue 2- No Matriculation number provided
//Issue- As there is no date, we can'tget the latest lease number and hence we can;t reach the other table
	
			
			
			preparedStatement = dbConnection.prepareStatement(selectQuery);
			preparedStatement.setInt(1, personId);
			rs = preparedStatement.executeQuery();
			
		
			if(rs.isBeforeFirst()){
					
				rs.next();
				System.out.println("Name\tLease #\tDuration");

				System.out.print(rs.getString("first_name")+"\t");
				System.out.print(rs.getString("lease_no")+"\t");
				System.out.print(rs.getString("duration")+"\t");
				
			}


		} catch(SQLException e1){
			System.out.println("SQLException: "+ e1.getMessage());
			System.out.println("VendorError: "+ e1.getErrorCode());
		}
		catch(Exception e3)
		{
			System.out.println("General Exception Case. Printing stack trace below:\n");
			e3.printStackTrace();
		}
		finally{
			try {
				rs.close();
				preparedStatement.close();
				dbConnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}


		/*Write query for displaying :
		 *lease number, duration of the lease, name of student, matriculation number of student,
		 * place number, room number, Hall address or Student apartment address, Date of moving in and if 
		 * present date of leaving the room */

	}

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
