package com.univhousing.agreement;

import java.sql.Date;
import java.text.ParseException;
import java.util.Scanner;

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
	}
}
