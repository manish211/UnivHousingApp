package com.univhousing.agreement;

import java.text.ParseException;
import java.util.Scanner;

import com.univhousing.main.Utils;

public class LeaseRequest_Relation {
	/**
	 * @param personId
	 * @throws ParseException 
	 * @action Generates a new Lease request and Inserts the new data into respective tables
	 */
	public void generateNewLeaseRequest(int personId) throws ParseException {

		Scanner inputObj = new Scanner(System.in);

		/*Write SQL Query to check if Student is Freshmen or not and set variable isFreshmen based on that
		 * By default isFreshmen is false*/
		boolean isFreshmen = false;


		System.out.println("Enter the period of leasing: ");
		int periodOfLeasing = inputObj.nextInt();

		String housingOption;

		if(isFreshmen)
		{
			System.out.println("Enter your housing preference " +
					"a) Private Housing\n" +
					"b) Residence Hall\n" +
					"c) General Apartment\n" +
					"d) Family Apartment\n" +
			"Choose a or b or c or d");
			housingOption = inputObj.next();
		}
		else
		{
			System.out.println("Enter your housing preference: \n" +
					"a) Residence Hall\n" +
					"b) General Apartment\n" +
					"c) Family Apartment\n" +
			"Choose a or b or c or d");
			housingOption = inputObj.next();
		}

		System.out.println("Date you want to enter in MM/dd/YYYY format: ");
		String moveInDate = inputObj.next();
		java.sql.Date sqlMoveInDate = Utils.convertStringToSQLDateFormat(moveInDate);

		System.out.println("Enter the payment options: \n" +
				"a) Monthly\n" +
		"b) Semester\n");
		String paymentOption = inputObj.next();

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
