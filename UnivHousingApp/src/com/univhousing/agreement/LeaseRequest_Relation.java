package com.univhousing.agreement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

import com.univhousing.main.ConnectionUtils;
import com.univhousing.main.Utils;

public class LeaseRequest_Relation {

	public int applicationRequestNo;
	public String accomodationType;
	public int personId;
	public int staffNo;
	public ArrayList<String> residenceHallNames = null;

	/**
	 * @param personId
	 * @throws ParseException
	 * @action Generates a new Lease request and Inserts the new data into
	 *         respective tables
	 */
	public void generateNewLeaseRequest(int personId) throws ParseException {

		Scanner inputObj = new Scanner(System.in);

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {

			/* Checking if the student is FRESHMEN OR OTHERS */

			boolean isFreshmen = false;
			String housingOption;
			String hallPreference1 = "";
			String hallPreference2 = "";
			String hallPreference3 = "";
			String studentName = null;
			dbConnection = ConnectionUtils.getConnection();
			String SelectQuery1 = "SELECT S.Student_type, P.First_Name FROM STUDENT S,Person P WHERE S.Person_id=? AND P.person_id=S.person_id";

			preparedStatement = dbConnection.prepareStatement(SelectQuery1);
			preparedStatement.setInt(1, personId);
			rs = preparedStatement.executeQuery();

			if (rs.isBeforeFirst()) {
				rs.next();

				studentName = rs.getString("first_name");
				String tempStr = rs.getString("Student_type");

				if (tempStr.equals("FRESHMEN")) {
					isFreshmen = true;

				}

			}

			rs.close();
			preparedStatement.close();

			/***********************************************************
			 * Firing a query for getting the last Application_request_no so
			 * that we can add 1 and generate next request number
			 */

			String SelectQuery2 = "SELECT MAX(application_request_no)as application_request_no FROM PERSON_ACC_STAFF";

			preparedStatement = dbConnection.prepareStatement(SelectQuery2);
			rs = preparedStatement.executeQuery();

			rs.next();
			int newApplicationRequestNumber = rs
					.getInt("application_request_no") + 1;

			rs.close();
			preparedStatement.close();

			/************************************************************************/

			System.out.println("Enter the period of leasing: ");
			int periodOfLeasing = inputObj.nextInt();

			if (isFreshmen == false) {
				System.out.println("Welcome " + studentName + "!");
				System.out.println("Enter your housing preference\n"
						+ "a) Private Housing\n" + "b) Residence Hall\n"
						+ "c) General Apartment\n" + "d) Family Apartment\n"
						+ "Choose appropriate option");

				housingOption = inputObj.next();

				if (housingOption.equals("a")) {
					housingOption = "Private Housing";
					hallPreference1 = "NA";
					hallPreference2 = "NA";
					hallPreference3 = "NA";
				} else if (housingOption.equals("b")) {
					housingOption = "Residence Hall";

					residenceHallNames = getHallNameList();

					hallPreference1 = residenceHallNames.get(0);
					hallPreference2 = residenceHallNames.get(1);
					hallPreference3 = residenceHallNames.get(2);

				} else if (housingOption.equals("c")) {
					housingOption.replaceAll("c", "Apartment");
					housingOption = "Apartment";
					hallPreference1 = "NA";
					hallPreference2 = "NA";
					hallPreference3 = "NA";
				} else if (housingOption.equals("d")) {
					housingOption = "Family Apartment";
					hallPreference1 = "NA";
					hallPreference2 = "NA";
					hallPreference3 = "NA";
				}

			}

			else {

				System.out.println("Welcome " + studentName + "!");
				System.out
						.println("Enter your housing preference: \n"
								+ "a) Residence Hall\n"
								+ "b) General Apartment\n"
								+ "c) Family Apartment\n"
								+ "Choose appropriate option");
				housingOption = inputObj.next();

				if (housingOption.equals("a")) {
					housingOption = "Residence Hall";

					residenceHallNames = getHallNameList();
					hallPreference1 = residenceHallNames.get(0);
					hallPreference2 = residenceHallNames.get(1);
					hallPreference3 = residenceHallNames.get(2);
				} else if (housingOption.equals("b")) {
					housingOption = "Apartment";
					hallPreference1 = "NA";
					hallPreference2 = "NA";
					hallPreference3 = "NA";
				} else if (housingOption.equals("c")) {
					housingOption = "Family Apartment";
					hallPreference1 = "NA";
					hallPreference2 = "NA";
					hallPreference3 = "NA";
				}

			}

			System.out.println("Date you want to enter in MM/dd/YYYY format: ");
			String moveInDate = inputObj.next();
			java.sql.Date sqlMoveInDate = Utils
					.convertStringToSQLDateFormat(moveInDate);

			System.out.println("Enter the payment options: \n" + "a) Monthly\n"
					+ "b) Semester\n");
			String paymentOption = inputObj.next();
			if (paymentOption.equals("a"))
				paymentOption = "Monthly";
			else if (paymentOption.equals("b"))
				paymentOption = "Semester";

			System.out.println("Do you want to: \n" + "1. Submit\n"
					+ "2. Back ");

			int choice = inputObj.nextInt();
			if (choice == 1) {

				String reqStatus = "Pending";
				String selectQuery3 = "INSERT INTO PERSON_ACC_STAFF (application_request_no,accomodation_type,person_id,request_status, "
						+ "mode_of_payment,lease_move_in_date,duration,PREFERENCE1,PREFERENCE2,PREFERENCE3) "
						+ "VALUES (?,?,?,?,?,?,?,?,?,?)";

				preparedStatement = dbConnection.prepareStatement(selectQuery3);
				preparedStatement.setInt(1, newApplicationRequestNumber);
				preparedStatement.setString(2, housingOption);
				preparedStatement.setInt(3, personId);
				preparedStatement.setString(4, reqStatus);
				preparedStatement.setString(5, paymentOption);
				preparedStatement.setDate(6, sqlMoveInDate);
				preparedStatement.setInt(7, periodOfLeasing);
				preparedStatement.setString(8, hallPreference1);
				preparedStatement.setString(9, hallPreference2);
				preparedStatement.setString(10, hallPreference3);
				preparedStatement.executeUpdate();

				System.out
						.println("Success!!!  Please Note down the Request Number for future References=>"
								+ newApplicationRequestNumber);

			} else if (choice == 2) {
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

	/* RETRIEVING SELECTING THE SPECIFIC RESIDENCE HALLS */

	private ArrayList<String> getHallNameList() {
		Scanner inputObj = new Scanner(System.in);
		ArrayList<String> hallList = new ArrayList<String>();
		ArrayList<String> hallOptions = new ArrayList<String>(3);

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {

			dbConnection = ConnectionUtils.getConnection();

			String selectQuery1 = "SELECT HALL_NAME FROM RESIDENCE_HALL";
			int preferenceCounter = 1;
			preparedStatement = dbConnection.prepareStatement(selectQuery1);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {

				hallList.add(rs.getString("HALL_NAME"));
			}

			while (preferenceCounter < 4) {

				System.out.println("Enter Preference No. " + preferenceCounter
						+ "=> ");

				for (int i = 0; i < hallList.size(); i++) {

					System.out.println(i + 1 + "." + hallList.get(i));

				}

				int option = inputObj.nextInt();
				hallOptions.add(hallList.get(option - 1));

				preferenceCounter++;
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

		return hallOptions;
	}
}
