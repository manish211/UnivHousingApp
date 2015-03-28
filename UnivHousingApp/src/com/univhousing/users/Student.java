package com.univhousing.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.univhousing.main.ConnectionUtils;

public class Student {
	
	public int studentId;
	public int personId;
	public String studentType;
	
	public String studentDesignation = "";
	/**
	 * @param personId
	 * @return
	 */
	public int getStudentIdForPersonId(int personId)
	{
		int studentId = 0 ;
		/*Write SQL Query to fetch the student Id for personId
		 * Use the instance Variable to save the student's ID and return it;*/
		
//		SELECT student_id FROM student WHERE person_id = ? 
				
		ResultSet rs = null;
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		try{
			dbConnection = ConnectionUtils.getConnection();
			
			String selectQuery = "SELECT student_id FROM student WHERE person_id = ? " ;
			
			preparedStatement = dbConnection.prepareStatement(selectQuery);
			
			preparedStatement.setInt(1,personId);
			
			rs = preparedStatement.executeQuery();
			
			//If record exists , rs.next() will evaluate to true
			if(rs.isBeforeFirst())
				{
					rs.next();
					System.out.print(rs.getInt("student_id")+"\t\t");
					studentId = rs.getInt("student_id");
				}
			
		}catch(SQLException e1){
			System.out.println("getStudentIdForPersonId SQLException: "+ e1.getMessage());
			System.out.println("getStudentIdForPersonId VendorError: "+ e1.getErrorCode());
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
		return studentId;
	}
	
	
	/**
	 * @param studentId
	 * @action Checks if the studentId s enrolled in University Housing
	 * @return True is the studentId is enrolled in university housing else returns False
	 */
	public boolean checkStudentInUnivHousing(int personId)
	{
		System.out.println("PERSON ID : "+personId);
		boolean isStudentHavingAccomodation = false;
		String requestStatus;
		/*Write SQL Query to check if the studentId has an accommodation and only then
		 * will the student be allowed parking spot
		 * Then set isStudentAccomodated as true*/
		
		ResultSet rs = null;
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		try {
			dbConnection = ConnectionUtils.getConnection();

			String selectQuery = "SELECT request_status FROM PERSON_ACC_STAFF WHERE person_id = ? ";

			preparedStatement = dbConnection.prepareStatement(selectQuery);

			preparedStatement.setInt(1, personId);

			rs = preparedStatement.executeQuery();
			
			// If record exists , rs.next() will evaluate to true
			if (rs.isBeforeFirst()) {System.out.println("After execute!! MARKER1");
				rs.next();
				System.out.print(rs.getString("request_status") + "\t\t");
				requestStatus = rs.getString("request_status");
				
				System.out.println("requestStatus="+requestStatus);
				
				if(requestStatus.equals("APPROVED"))
				{
					isStudentHavingAccomodation = true;
//					System.out.println("IT IS TRUE: "+personId);
				}
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
		
		System.out.println("isStudentHavingAccomodation: "+isStudentHavingAccomodation);
		return isStudentHavingAccomodation;
	}
	
	
	/**
	 * @param personId
	 * @return
	 */
	public String getStudentDesignation(int personId)
	{
		/*Write SQL Query to get the student's designation from login_credentials table i think*/
		return studentDesignation;
	}

}


