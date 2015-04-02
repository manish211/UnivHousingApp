package com.univhousing.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.univhousing.main.ConnectionUtils;

public class NextOfKin {
	
	public int studentId;
	public String firstName;
	public String lastName;
	public String streetName;
	public String city;
	public int postCode;
	public int phoneNumber;
	public java.sql.Date DOB;
	public String gender;
	
	
	/**
	 * @param studentId
	 * @action Displays the next of kind details when the student is viewing his profile
	 */
	
	/*first_name VARCHAR2(40),
	last_name VARCHAR2(40),
	street_no INTEGER,
	city_name VARCHAR2(40),
	zip_code INTEGER,*/
	public void getNextOfKinDetails(int studentId) 
	{
		ResultSet studentKin = null;
		Connection dbConnection = ConnectionUtils.getConnection();
		PreparedStatement preparedStatement1 = null;
		String queryNextOfKin = "";
		
		try
		{
			queryNextOfKin = "SELECT K.first_name, K.last_name, K.street_name, K.city_name, K.zip_code, K.phone_number, K.gender, K.DOB FROM KIN_STUDENT K WHERE K.student_id = ?";
			preparedStatement1 = dbConnection.prepareStatement(queryNextOfKin);
			preparedStatement1.setInt(1, studentId);
			studentKin = preparedStatement1.executeQuery();
			
			while(studentKin.next())
			{
				firstName = studentKin.getString("first_name");
				lastName = studentKin.getString("last_name");
				streetName = studentKin.getString("street_name");
				city = studentKin.getString("city_name");
				postCode = studentKin.getInt("zip_code");
				phoneNumber = studentKin.getInt("phone_number");
				gender = studentKin.getString("gender");
				DOB = studentKin.getDate("DOB");
				
				System.out.println("Next of Kin's Name: "+firstName+lastName+"\n"+"Address: "+streetName+","+city+","+postCode+"\t");
				System.out.println("Phone Number: "+phoneNumber+"\n"+"Gender: "+gender+"\n"+"DOB: "+DOB+"\t");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}


}
