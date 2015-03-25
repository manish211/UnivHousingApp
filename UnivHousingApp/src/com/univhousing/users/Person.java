package com.univhousing.users;

import java.sql.ResultSet;
import java.util.Scanner;

public class Person {

	public int personId;
	public String firstName;
	public String lastName;
	public String streetName;
	public String city;
	public int postcode;
	
	Student studentObj = new Student();
	Scanner inputObj = new Scanner(System.in);
	
	/**
	 * @param personId
	 * @action Displays name, number, address, dob, gender, category (freshman, sophomore, etc) family details
	 *  if needed, special needs etc (refer the description for a comprehensive list)
	 */
	public void viewProfileDetails(int personId) {
		
		int studentId;
		// Fetch the student Id for a particular Person Id
		studentId = studentObj.getSutdentIdForPersonId(personId);
		
		/*Write SQL Query for fetching:
		 * name, number, address, dob, gender, category (freshman, sophomore, etc) family details (from NextOfKin)
		 * if needed, special needs for a student.
		 * There might be more than these please check the project document for this*/
		
		ResultSet getStudentProfile = null;
	}

	/**
	 * @param personId
	 * @action First fetches all the profile details and then asks new values for all of them.
	 */
	public void updateProfile(int personId) 
	{

		int studentId;
		// Fetch the student Id for a particular Person Id
		studentId = studentObj.getSutdentIdForPersonId(personId);
		
		/*Write SQL Query for fetching:
		 * name, number, address, dob, gender, category (freshman, sophomore, etc) family details (from NextOfKin)
		 * if needed, special needs for a student.
		 * There might be more than these please check the project document for this*/
		
		ResultSet getStudentProfile = null;
		
		
		System.out.println("Enter new values for the fields you want," +
				" and leave the fields you don't want changes as Blank");
		
		System.out.println("First Name: ");
		String newFirstName = inputObj.next();
		
		System.out.println("Last Name: ");
		String newLastName = inputObj.next();
		
		System.out.println("Phone Number: ");
		int newPhone = inputObj.nextInt();
		
		System.out.println("Street Name.: ");
		String newStreetName = inputObj.next();
		
		System.out.println("City: ");
		String newCity = inputObj.next();
		
		System.out.println("ZipCode: ");
		int newZipCode = inputObj.nextInt();
		
		System.out.println("DOB: ");
		String newDOB = inputObj.next();
		
		System.out.println("Gender: ");
		String newGender = inputObj.next();

		System.out.println("Student Category: ");
		String newStudentType = inputObj.next();
		
		System.out.println("Special Needs");
		String newSpecialNeeds = inputObj.next();
		
		System.out.println("Please enter Family Details now: ");
		
		System.out.println("Next of Kin First Name:");
		String newNOKFirstName = inputObj.next();
		
		System.out.println("Next of Kin Last Name: ");
		String newNOKLastName = inputObj.next();
		
		System.out.println("Next of Kin Phone Number: ");
		int newNOKPhone = inputObj.nextInt();
		
		System.out.println("Next of Kin Street Name.: ");
		String newNOKStreetName = inputObj.next();
		
		System.out.println("Next of Kin City: ");
		String newNOKCity = inputObj.next();
		
		System.out.println("Next of Kin ZipCode: ");
		int newNOKZipCode = inputObj.nextInt();
		
		/*Write SQL Query to to update the above information for the retrieved Student ID
		 * Students and NextOfKin(NOK) both detials ahve to be updated.*/
		
		ResultSet updateProfile = null;
		
	}


}
