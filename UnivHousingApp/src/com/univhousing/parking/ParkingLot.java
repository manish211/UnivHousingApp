package com.univhousing.parking;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import com.univhousing.users.Student;

public class ParkingLot {
	
	public int lotNo;

	/**
	 * @param personId
	 * @action Creates a parking spot for a student Id if he is enrolled in University Housing
	 */
	Student studentObj = new Student();
	Scanner inputObj = new Scanner(System.in);

	public void generateAParkingSpot(int personId)
	{
		int studentId;
		boolean isStudentAccomodated = false;

		System.out.println("Showing Request New Parking Spot\n");
		System.out.println("Enter the following information\n");
		System.out.println("Please enter your vehicle type:\n" +
				"Bike, Compact Cars, Standard Cars, Large Cars");
		String vehicleType = inputObj.next();
		
		System.out.println("Handicapped? Y/N");
		boolean handicappedInfo = inputObj.nextBoolean();
		
		System.out.println("Do you want a nearby spot? Y/N");
		boolean nearbySpot = inputObj.nextBoolean();
		
		System.out.println("1. Submit");
		System.out.println("2. Back");
		int flag = inputObj.nextInt();
		
		if(flag == 1)
		{			
			// Fetch the student Id for a particular Person Id
			studentId = studentObj.getSutdentIdForPersonId(personId);
			
			// Check if student is in University Housing
			isStudentAccomodated = studentObj.checkStudentInUnivHosuing(studentId);
			
			if(isStudentAccomodated)
			{
				ResultSet createParkingSpot = null;
				/*Write SQL Query to use vehicleType, handicappedInfo, nearbySpot and 
				 * create a entry for the student Id by adding an entry in StudentParkingSpot_relation table
				 * and if approved then fill the permit ID in PSpotBelongsPLot_Realtion*/
			}
			else
			{
				System.out.println("Sorry only students in University Housing can request parking spot");
			}
		}
		else
		{
			return;
		}
	}
	
	/**
	 * @action displays the information of Each parking lots i.e. 
	 * 1. How many spots are vacant in each parking lot
	 * 2. Which spots (if) allocated to a student
	 * 3. Availability of Parking Spots
	 */
	public void displayInfoForParkingLots(int personId)
	{
		int studentId;
		boolean isStudentAccomodated = false;
		// Fetch the student Id for a particular Person Id
		studentId = studentObj.getSutdentIdForPersonId(personId);
		
		// Check if student is in University Housing
		isStudentAccomodated = studentObj.checkStudentInUnivHosuing(studentId);
		
		ResultSet showParkingLotInfo = null;
		/*Write an SQL query to show:
		 * 1. How many spots are vacant in each parking lot
		 * 2. Which spots (if) allocated to a student
		 * 3. Availability of Parking Spots*/
	}
	

	/**
	 * @param personId
	 * @action Finds the studentId for the personId and then gets the permit number for 
	 * that studentId and then displays the permit id for that studentID along with lot number
	 */
	public void viewCurrentParkingSpot(int personId)
	{
		int studentId;
		boolean isStudentAccomodated = false;
		// Fetch the student Id for a particular Person Id
		studentId = studentObj.getSutdentIdForPersonId(personId);
		
		// Check if student is in University Housing
		isStudentAccomodated = studentObj.checkStudentInUnivHosuing(studentId);
		
		ResultSet parkingSpotInfo = null;
		/*Write SQL Query to show:
		 * Permit number, spot number and Lot number for a particular student id */
	}

	/**
	 * @param personId
	 * @action Asks for a parking spot Id and then renews the parking spot ID for that Student Id
	 *  and updates the Permit number to a new value in the leases table and PspotBelongsPLot_Relation
	 */
	public void renewParkingSpot(int personId) 
	{
		int studentId;
		// Fetch the student Id for a particular Person Id
		studentId = studentObj.getSutdentIdForPersonId(personId);
		
		System.out.println("Enter your Parking Spot Number:");
		int spotNumber = inputObj.nextInt();
		
		checkSpotValidity(spotNumber);
		
		ResultSet renewSpot = null;
		/*Write query to renew the parking spot and then update the permit id in Person_accomodation_Lease table and 
		 *  PSpotBelongsPLot_Relation*/
	}

	/**
	 * @param personId
	 * @action Returns a parking spot back to a Parking lot and updates the PSpotBelongsPLot_Relation
	 * table's availability attribute for that spot number to vacant 
	 */
	public void returnParkingSpot(int personId) 
	{
		int studentId;
		// Fetch the student Id for a particular Person Id
		studentId = studentObj.getSutdentIdForPersonId(personId);
		
		System.out.println("Enter your Parking Spot Number:");
		int spotNumber = inputObj.nextInt();
		
		// Check if the student is actually the owner of this parking spot
		checkSpotValidity(spotNumber);
		
		ResultSet returnSpot = null;
		/*Write SQL Query to return parking spot i.e.
		 * 1. Delete information of Permit number for Person_accomodation_lease table
		 * 2. Set availability of spot number as vacant in PSpotBelongsPLot_Relation*/
	}
	
	/**
	 * @param personId
	 * @action Retrieves the student id for the given person Id and gets the status of the request made by that student
	 */
	public void getRequestStatus(int personId) {

		int studentId;
		// Fetch the student Id for a particular Person Id
		studentId = studentObj.getSutdentIdForPersonId(personId);
		
		ResultSet fetchRequestStatus = null;
		/*Write SQL query for fetching the status of Request for this student ID*/
	}

	/**
	 * @param spotNumber
	 * @action This is a local method, which checks if a student is actually assigned to this spot number
	 */
	private void checkSpotValidity(int spotNumber) {
		
		ResultSet checkSpotValidity = null;
		/*Write SQL Query to check if SpotNumber entered by user is actually ssigned to him*/
	}

	/**
	 * @param ArrayList<Integer> adminLevelParkingRequests
	 * @throws SQLException 
	 */
	public void getAllParkingRequests(ArrayList<Integer> parkingTicketsList) throws SQLException 
	{
		/*Write SQL Query to fetch all the Parking requests*/
		ResultSet allRequests = null;
		
		while(allRequests.next())
		{
			parkingTicketsList.add(allRequests.getInt("request_no"));
		}
		
		System.out.println("Displaying all the parking requests made:");
		for (int i = 0; i < parkingTicketsList.size(); i++) 
		{
			System.out.println((i+1)+". "+parkingTicketsList.get(i));
		}
		System.out.println("Please select the parking request:");
		int requestSelected = inputObj.nextInt();
		int requestNumber = parkingTicketsList.get(requestSelected-1);
		
		/*Write a SQL Query to fetch all details of the ticket number*/
		System.out.println("Do you want to approve request: Y/N");
		String requestApprovalStatus = inputObj.next();
		
		/* Note at this point of time, a request for parking has been generated only after
		 * it has been validated that student is indeed enrolled in university housing.
		 * The code to check this is present in this method: 
		 * public boolean checkStudentInUnivHosuing(int studentId) in Student.java
		 * 
		 * 
		 * NOTE: But the document states if all the criteria is verified then approve, so we have to 
		 * look what other criteria are*/
		
		if(requestApprovalStatus.equalsIgnoreCase("Y"))
		{
			/*Write SQL Query to change status of request to approved and 
			 * also generate a permit_id which will be updated in atleast two tables*/
		}
		else if(requestApprovalStatus.equalsIgnoreCase("N"))
		{
			/*Write SQL Query to change status of request to denied*/
		}
	}
}
