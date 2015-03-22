package com.univhousing.agreement;

import java.util.Scanner;

public class LeaseRequest_Relation {
	/**
	 * @param personId
	 * @action Generates a new Lease request and Inserts the new data into respective tables
	 */
	public void generateNewLeaseRequest(int personId) {

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
			System.out.println("Enter your housing preference " +
					"a) Residence Hall\n" +
					"b) General Apartment\n" +
					"c) Family Apartment\n" +
					"Choose a or b or c or d");
			housingOption = inputObj.next();
		}
		
		System.out.println("Date you want to enter: ");
		
		
		
		/*Write SQL Query to generate a new Lease Request for a particular person Id*/		
	}
}
