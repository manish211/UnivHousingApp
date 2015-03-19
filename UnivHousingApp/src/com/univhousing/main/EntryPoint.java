package com.univhousing.main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.univhousing.users.Student;

public class EntryPoint {

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		Student studentOj = new Student();
		Credentials credentialObj = new Credentials();
		try {
			Connection connectionObj = null;
			Statement statementObj = null;
			ResultSet resultSetObj = null;
			
			try {
				boolean exitFlag = true;
				int choice;

				System.out.println("Welcome to University Housing");
				// Create scanner object for taking command line input
				Scanner inputObj = new Scanner(System.in);
				
				// Going into infinite loop until exited
				while(exitFlag) {
					System.out.println("1. Login");
					System.out.println("2. Guest Login");
					System.out.println("3. Exit");
					choice = inputObj.nextInt();
					
					switch (choice) {
					case 1:
						System.out.print("Enter University Id:");
						credentialObj.userName = inputObj.next();

						System.out.print("Enter Password:");
						credentialObj.password = inputObj.next();
						
						if(loginSuccessful(credentialObj.userName,credentialObj.password,statementObj))
						break;

					default: System.out.println("Invalid Choice");
						break;
					}	
				}
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		
		
	}

	private static boolean loginSuccessful(String userName, String password, Statement statement) throws SQLException {
		int userID;
		String designation;
		// Write SQL query to check if userName and password match the Credentials Table in Database
		// If matches retrieve designationa nd person_id
		ResultSet user = statement.executeQuery("SELECT .....");
		while(user.next())
		{
			
		}
		
		return false;
	}

}
