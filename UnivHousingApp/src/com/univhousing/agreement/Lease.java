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
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		try {
			//System.out.println("Inside try. Breakpoint");
			dbConnection = ConnectionUtils.getConnection();
			
			/* Create and insert SQL Query here */
			
			String selectQuery = "SELECT T.termination_request_number, T.status "
					+ "FROM termination_requests T "
					+ "WHERE T.person_id = ? ";
			
			String selectQuery1 = "SELECT S.request_no, S.request_status "
					+ "FROM StudentParkingSpot_Relation S, Student S1 "
					+ "WHERE (S1.student_id=S.student_id AND s1.person_id = ?)";
			
			preparedStatement = dbConnection.prepareStatement(selectQuery);
			preparedStatement.setInt(1, personId);
			
			viewRequestsSet = preparedStatement.executeQuery();
			
			//System.out.println("Request type" + "\t Request Number" + "\t Status");
			System.out.println(String.format("%-15s%-20s%-15s",
					"Request type", "Request number", "Status"));
			while(viewRequestsSet.next()) {
				System.out.print(String.format("%-15s%-20s%-15s","Termination"
						,viewRequestsSet.getInt("termination_request_number")
						,viewRequestsSet.getString("status")));
				System.out.println("");
				//System.out.println(viewRequestsSet.getString("first_name") + " " + viewRequestsSet.getString("last_name"));

				/*Write an SQL Query for fetching details of each value of requestNumber variable*/
			}
			preparedStatement.close();
			viewRequestsSet.close();
			
			preparedStatement = dbConnection.prepareStatement(selectQuery1);
			preparedStatement.setInt(1, personId);
			
			viewRequestsSet = preparedStatement.executeQuery();
			//System.out.println("Request Number" + "\t Status");
			while (viewRequestsSet.next()) {
				System.out.print(String.format("%-15s%-20s%-15s","Parking"
						,viewRequestsSet.getInt("request_no")
						,viewRequestsSet.getString("request_status")));
				System.out.println("");
			}
			
		} catch (SQLException e1) {
			System.out.println("SQLException: " + e1.getMessage());
			System.out.println("Vendor Error: " + e1.getErrorCode());
		} catch (Exception e) {
			System.out.println("Geneal Error. Please see stack trace: ");
			e.printStackTrace();
		} finally {
			try {
				viewRequestsSet.close();
				dbConnection.close();
				preparedStatement.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * @param personId
	 * @param requestNumber
	 * @throws SQLException 
	 * @action Deletes the requestNumber for the personID  
	 */
	public void cancelRequest(int personId, int requestNumber) throws SQLException {
		
		//viewAllRequests(personId);
		
		ResultSet rs = null;
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		/*Write SQL Query to delete the requestNumber mentioned by the PersonId and set the status as "Cancelled"*/
		
		try {
			dbConnection = ConnectionUtils.getConnection();
			
			/*
			 * First check if the entry is in the termination table.
			 */
			String selectQuery = "SELECT * "
					+ "FROM Termination_Requests T "
					+ "WHERE T.person_id = ? AND T.termination_request_number = ?";
			
			preparedStatement = dbConnection.prepareStatement(selectQuery);
			preparedStatement.setInt(1, personId);
			preparedStatement.setInt(2, requestNumber);
			
			rs = preparedStatement.executeQuery();
			
			/*
			 * Check if the entry is in the Termination_requests table or
			 * the StudentParkingSpot_Relation table.
			 */
			if (rs.next()) {
				String updateQuery = "UPDATE Termination_Requests "
						+ "SET status = ? "
						+ "WHERE person_id = ? AND termination_request_number = ?";
				
				//System.out.println(rs.getInt("termination_request_number"));
				rs.close();
				preparedStatement.close();
				
				preparedStatement = dbConnection.prepareStatement(updateQuery);
				preparedStatement.setString(1, "CANCELED");
				preparedStatement.setInt(2, personId);
				preparedStatement.setInt(3, requestNumber);
				
				int update = preparedStatement.executeUpdate();
				//System.out.println("Update returned " + update);
				
			} else {
				
				/*
				 * Since the entry is not in the termination table,
				 * it must be in the termination_request_number table
				 */
				preparedStatement.close();
				rs.close();
				
				String selectQuery1 = "SELECT S.request_no, S1.student_id "
						+ "FROM StudentParkingSpot_Relation S, Student S1 "
						+ "WHERE (S1.student_id=S.student_id AND s1.person_id = ? "
						+ "AND S.request_no = ?)";
				preparedStatement = dbConnection.prepareStatement(selectQuery1);
				preparedStatement.setInt(1, personId);
				preparedStatement.setInt(2, requestNumber);
				
				rs = preparedStatement.executeQuery();
				
				//System.out.println("Executed with values: " + personId + " and " + requestNumber);
				if (rs.next()) {
					
					/*
					 * If the request id is in the parking table, 
					 * get the student id and update the table.
					 * Get the student id first
					 */
					//System.out.println("Executed with values: " + personId + " and " + requestNumber);
					preparedStatement.close();
					rs.close();
					
					String getStudentId = "SELECT S.student_id "
							+ "FROM Student S, Person P "
							+ "WHERE S.person_id = P.person_id "
							+ "AND P.person_id = ?";
					preparedStatement = dbConnection.prepareStatement(getStudentId);
					preparedStatement.setInt(1, personId);
					
					rs = preparedStatement.executeQuery();
					rs.next();
					int student_id = rs.getInt("student_id");
					
					/*
					 * Update the status with CANCELED
					 */
					String updateQuery1 = "UPDATE StudentParkingSpot_Relation "
							+ "SET request_status = ? "
							+ "WHERE student_id = ? AND request_no = ?";
					
					preparedStatement.close();
					preparedStatement = dbConnection.prepareStatement(updateQuery1);
					preparedStatement.setString(1, "CANCELED");
					preparedStatement.setInt(2, student_id);
					preparedStatement.setInt(3, requestNumber);
					int update = preparedStatement.executeUpdate();
				} else {
					
					/*
					 * The value is not present in the parking table either
					 * Print an error message saying that the user needs to check
					 */
					System.out.println("Please enter a valid request number");
				}
			}
		} catch (SQLException e1) {
			System.out.println("SQLException: " + e1.getMessage());
			System.out.println("Vendor Error: " + e1.getErrorCode());
		} catch (Exception e) {
			System.out.println("Geneal Error. Please see stack trace: ");
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				preparedStatement.close();
				dbConnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @action Pulls up all the accommodations that are vacant right now i.e. not occupied by any student or family
	 */
	public void viewAccomodationVacancies() {
		/*Write SQL Query to pull up all the vacancies to display the student*/
		ResultSet displayAllVancancies = null;
	}

}
