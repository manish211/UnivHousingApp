package com.univhousing.agreement;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import com.sun.corba.se.pept.encoding.InputObject;
import com.univhousing.main.ConnectionUtils;

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

		ResultSet rs = null;
		PreparedStatement preparedStatement = null;
		Connection dbConnection = null;
		String accomodationType = "";
		allLeaseRequestsToMonitor.clear();
		String status = "PENDING";
		ArrayList<String> preferences = new ArrayList<>();
		
		try {
			
			dbConnection = ConnectionUtils.getConnection();
			String selectQuery = "SELECT application_request_no "
					+ "FROM PERSON_ACC_STAFF "
					+ "WHERE UPPER(request_status) = ?";
		
			preparedStatement = dbConnection.prepareStatement(selectQuery);
			preparedStatement.setString(1, status);
			
			rs = preparedStatement.executeQuery();
			while(rs.next())
			{
				allLeaseRequestsToMonitor.add(rs.getInt("application_request_no"));
			}
			
			System.out.println("Displaying all the requests to approve: ");
			for (int i = 0; i < allLeaseRequestsToMonitor.size(); i++) 
			{
				System.out.println((i+1)+". "+allLeaseRequestsToMonitor.get(i));
			}
			int requestChosen = inputObj.nextInt();
			int requestNumber = allLeaseRequestsToMonitor.get(requestChosen-1);
			
			/*Write SQL Query to fetch all the details of the requestNumber*/
			//ResultSet requestDetails = null;
			
			String selectQueryDetails = "SELECT APPLICATION_REQUEST_NO, PERSON_ID, "
					+ "ACCOMODATION_TYPE, MODE_OF_PAYMENT, LEASE_MOVE_IN_DATE,"
					+ "DURATION, PREFERENCE1, PREFERENCE2, PREFERENCE3 "
					+ "FROM PERSON_ACC_STAFF "
					+ "WHERE application_request_no = ?";
			preparedStatement.close();
			rs.close();
			
			preparedStatement = dbConnection.prepareStatement(selectQueryDetails);
			preparedStatement.setInt(1, requestNumber);
			rs = preparedStatement.executeQuery();
			
			System.out.println(String.format("%-13s%-11s%-20s%-15s%-15s%-9s%-15s%-15s%-15s",
					"App Req No.","Person ID", "Accommodation Type",
					"Payment Mode","Move in Date","Duration","Preference 1",
					"Preference 2", "Preference 3"));
			System.out.println("-----------------------------------------------"
					+ "---------------------------------------------------------"
					+ "--------------------");
			
			while (rs.next()) {
				System.out.println(String.format("%-13s%-11s%-20s%-15s%-15s%-9s%-15s"
						+ "%-15s%-15s", rs.getInt("APPLICATION_REQUEST_NO")
						,rs.getInt("PERSON_ID"),rs.getString("ACCOMODATION_TYPE")
						,rs.getString("MODE_OF_PAYMENT"),rs.getDate("LEASE_MOVE_IN_DATE")
						,rs.getString("DURATION"),rs.getString("PREFERENCE1")
						,rs.getString("PREFERENCE2"),rs.getString("PREFERENCE3")));
				
				/*
				 * Getting the preferences in case the user entered 
				 * residence hall.
				 */
				preferences.add(rs.getString("ACCOMODATION_TYPE"));
				preferences.add(rs.getString("PREFERENCE1"));
				preferences.add(rs.getString("PREFERENCE2"));
				preferences.add(rs.getString("PREFERENCE3"));
			}
			
			
			System.out.println("Do you want to approve this request? Y/N");
			String approvalStatus = inputObj.next();
			
			/*Now we look whether the accomodation_type provided by the student is available or not
			 * If YES: Assign it to him and update the respective tables
			 * If NO: Request will already be approved, but the request status will be changed to PENDING
			 * NOTE: accomodationType has to be taken from the request details*/
			
			boolean accAvailability = checkIfAccomodationTypeAvailable(preferences);
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
		} catch (SQLException e1) {
			System.out.println("SQLException: " + e1.getMessage());
			System.out.println("VendorError: " + e1.getErrorCode());
		} catch (Exception e3) {
			System.out.println("General Exception Case. Printing stack trace below:\n");
			e3.printStackTrace();
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
	 * @param accomodationType
	 * @return True if accomodationType is present else False
	 */
	private boolean checkIfAccomodationTypeAvailable(ArrayList<String> preferences) {
		/* Write SQL Query to check if the accommodation type student selected is actually available
		 * If YES: return True
		 * If NO: return False*/
		String type = preferences.get(0);
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;
		Connection dbConnection = null;
		try {
			dbConnection = ConnectionUtils.getConnection();
			
			if (type.toUpperCase().equals("RESIDENCE HALL")) {
				for (int i = 0; i < 3 ; i++) {
					String selectQueryPref = "SELECT COUNT(accomodation_id) AS count "
							+ "FROM residence_hall_provides_room "
							+ "WHERE hall_number = (SELECT hall_number "
							+ "						FROM residence_hall "
							+ "						WHERE hall_name = ?) "
							+ "AND accomodation_id NOT IN (SELECT accomodation_id "
							+ "							FROM person_accomodation_lease)";

					preparedStatement = dbConnection.prepareStatement(selectQueryPref);
					preparedStatement.setString(1, preferences.get(i+1));
					rs = preparedStatement.executeQuery();

					while (rs.next()) {
						if (rs.getInt("count") > 0) {
							dbConnection.close();
							return true;
						}
					}
					preparedStatement.close();
					rs.close();
				}
			} else if (type.toUpperCase().equals("APARTMENT")) {
				String selectQueryGenApt = "SELECT COUNT (B.apt_place_no) AS rooms "
						+ "FROM bedroom B "
						+ "WHERE B.accomodation_id  NOT IN "
						+ "(SELECT accomodation_id "
						+ "FROM person_accomodation_lease)";
				
				preparedStatement = dbConnection.prepareStatement(selectQueryGenApt);
				rs = preparedStatement.executeQuery();
				
				while (rs.next()){
					if (rs.getInt("rooms") > 0) {
						dbConnection.close();
						return true;
					}
					preparedStatement.close();
					rs.close();
				}
			} else if (type.toUpperCase().equals("FAMILY APARTMENT")) {
				String selectQueryFamApt = "SELECT COUNT (F.apt_no) AS apartments "
						+ "FROM Family_Apartment F "
						+ "WHERE F.accomodation_id NOT IN "
						+ "(SELECT accomodation_id "
						+ "FROM person_accomodation_lease)";
				
				preparedStatement = dbConnection.prepareStatement(selectQueryFamApt);
				rs = preparedStatement.executeQuery();
				
				while (rs.next()){
					if (rs.getInt("apartments") > 0) {
						dbConnection.close();
						return true;
					}
					preparedStatement.close();
					rs.close();
				}
			} else {
				return false;
			}
		} catch (SQLException e1) {
			System.out.println("SQLException: " + e1.getMessage());
			System.out.println("VendorError: " + e1.getErrorCode());
		} catch (Exception e3) {
			System.out.println("General Exception Case. Printing stack trace below:\n");
			e3.printStackTrace();
		} finally {
			try {
				rs.close();
				preparedStatement.close();
				dbConnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
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
