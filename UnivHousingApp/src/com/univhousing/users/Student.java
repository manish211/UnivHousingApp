package com.univhousing.users;

import java.sql.ResultSet;

public class Student {
	
	public int studentId;
	public int personId;
	public String studentType;
	
	public String studentDesignation = "";
	/**
	 * @param personId
	 * @return
	 */
	public int getSutdentIdForPersonId(int personId)
	{
		ResultSet getStudentId = null;
		/*Write SQL Query to fetch the student Id for personId
		 * Use the instance Variable to save the student's ID and return it;*/

		return studentId;
	}
	
	
	/**
	 * @param studentId
	 * @action Checks if the studentId s enrolled in University Housing
	 * @return True is the studentId is enrolled in university housing else returns False
	 */
	public boolean checkStudentInUnivHosuing(int studentId)
	{
		ResultSet checkStudentAccomodation = null;
		/*Write SQL Query to check if the studentId has an accommodation and only then
		 * will the student be allowed parking spot
		 * Then set isStudentAccomodated as true*/
		return false;
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
