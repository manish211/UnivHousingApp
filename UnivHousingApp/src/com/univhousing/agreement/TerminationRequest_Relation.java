package com.univhousing.agreement;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;

import com.univhousing.main.ConnectionUtils;
import com.univhousing.main.Utils;

public class TerminationRequest_Relation {
	
	public String reason;
	public int terminationRequestNo;
	public String status;
	public Date terminationDate;
	public Date inspectionDate;
	public int personId;
	public int staffNo;

	Scanner inputObj = new Scanner(System.in);

	/**
	 * @param personId
	 * @throws ParseException 
	 * @action Generates a new Termination request for a person id and Delete the related data 
	 * entries from respective tables
	 */
	public void generateLeaseTerminationRequest(int personId) throws ParseException {

		/*Write SQL Query to fetch the lease of the person id and generate a termination request thus deleting data
		 *  from the tables that relates to person id */
		int leaseNumber = 0;
		int terminationRequestNumber = 0;
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		try {
			dbConnection = ConnectionUtils.getConnection();

			String SelectQuery1 = "SELECT T1.lease_no FROM person_accomodation_table T1 WHERE "
					+ "LEASE_MOVE_IN_DATE=(SELECT MAX(LEASE_MOVE_IN_DATE) FROM PERSON_ACCOMODATION_LEASE T2 WHERE T1.Person_id=T2.person_id) "
					+ "AND T!.person_id =?";

			preparedStatement = dbConnection.prepareStatement(SelectQuery1);
			preparedStatement.setInt(1, personId);
			rs = preparedStatement.executeQuery();
			
			if(rs.isBeforeFirst())
			{
				rs.next();
				leaseNumber = rs.getInt("lease_no");
			}
			
			rs.close();
			preparedStatement.close();
			
			String selectQuery2 = "SELECT MAX(termination_request_number) as termination_request_number FROM termination_requests";
			
			preparedStatement = dbConnection.prepareStatement(selectQuery2);
		    rs = preparedStatement.executeQuery();
			if (rs.isBeforeFirst()) {
				rs.next();
				terminationRequestNumber = rs
						.getInt("termination_request_number")+1;

			}
			
			System.out.println("Enter date you wnt to leave in MM/dd/YYYY format: ");
			String moveOutDate = inputObj.next();
			java.sql.Date sqlMoveOutDate = Utils.convertStringToSQLDateFormat(moveOutDate);
			
			System.out.println("Enter the reason for leaving:");
			String reasonToMoveOut = inputObj.next();
			
			System.out.println("Do you want to: \n" +
					"1. Submit\n" +
					"2. Back ");
			int choice = inputObj.nextInt();
			if(choice == 1)
			{
				/*Write SQL Query to generate a new Lease Request for a particular person Id
				 * using all the values i.e.
				 * periodOfLeasing, housingOption, sqlMoveInDate and paymentOption*/
			}
			else if(choice == 2)
			{
				System.out.println("Request Cancelled");
				
			}
		} catch (SQLException e1) {
			System.out.println("SQLException: " + e1.getMessage());
			System.out.println("VendorError: " + e1.getErrorCode());
		} catch (Exception e3) {
			System.out
					.println("General Exception Case. Printing stack trace below:\n");
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
}
