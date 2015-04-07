package com.univhousing.agreement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

import com.univhousing.main.ConnectionUtils;
import com.univhousing.main.Constants;
import com.univhousing.main.Utils;
import com.univhousing.users.Person;

public class LeaseRequest_Relation {

	public static final int A = 5;
	public static final int B = 5;
	public static final int C = 2;
	public int applicationRequestNo;
	public String accomodationType;
	public int personId;
	public int staffNo;
	public ArrayList<String> residenceHallNames = null;
	Person personObj ;
	/**
	 * @param personId
	 * @throws ParseException
	 * @action Generates a new Lease request and Inserts the new data into
	 *         respective tables
	 */
	public void generateNewLeaseRequest(int personId) throws ParseException {

		Scanner inputObj = new Scanner(System.in);
		personObj = new Person();
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {

			/* Checking if the student is FRESHMEN OR OTHERS */

			boolean isFreshmen = false;
			String housingOption;
			String paymentGateway = "";
			String hallPreference1 = "";
			String hallPreference2 = "";
			String hallPreference3 = "";
			String paymentGatewayOption;
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

				if (tempStr.equals(Constants.FRESHMEN)) {
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

			// System.out.println("IMPORTANT INFORMATION : One semester is equal to 4 months");

			System.out.println("Enter the semesters you want to live ");
			System.out.println("a> Sem1: [01 Aug - 31 Dec]");
			System.out.println("b>Sem 2: 01 Jan - 31 May");
			System.out.println("c>Summer: 01 Jun - 31 Jul");
			System.out
					.println("For multiple semesters please enter optios seperated by comma[for example: a,b,c] ");

			String sem = inputObj.nextLine();
			String[] tempStr = sem.split(",");
			int periodOfLeaseInt = 0;
			int totalDuration = 0;
			for (int i = 0; i < tempStr.length; i++) {

				if (tempStr[i].equalsIgnoreCase("a")) {
					periodOfLeaseInt = periodOfLeaseInt + A;
				} else if (tempStr[i].equalsIgnoreCase("b")) {
					periodOfLeaseInt = periodOfLeaseInt + B;
				} else if (tempStr[i].equalsIgnoreCase("c")) {
					periodOfLeaseInt = periodOfLeaseInt + C;
				}

				totalDuration = periodOfLeaseInt;

			}

			int periodOfLeasing = totalDuration;		

			if (isFreshmen == false) {
				System.out.println("Welcome " + studentName + "!");
				System.out.println("Enter your housing preference\n"
						+ "a) Private Housing\n" + "b) Residence Hall\n"
						+ "c) General Apartment\n" + "d) Family Apartment\n"
						+ "Choose appropriate option");

				housingOption = inputObj.next();

				if (housingOption.equals("a")) {
					housingOption = "Private Housing";
					ArrayList<String> privateAccoTypes = new ArrayList<String>();
					privateAccoTypes.add("Private Shared House");
					privateAccoTypes.add("Private General Apartment");
					privateAccoTypes.add("Private Family Apartment");
					System.out.println("Enter your 3 preferences :\n");
					String[] prefs = new String[100];
					int count = 0;
					while(count<3)
					{
						System.out.println("Enter preference "+(count+1)+": ");
						for (int i = 0; i < privateAccoTypes.size(); i++) {
							System.out.println(i+1+") "+privateAccoTypes.get(i));
						}
						int choice = inputObj.nextInt();
						prefs[count] = privateAccoTypes.get(choice-1);
						count++;
					}
					count = 0;
					//inputObj.nextLine();
					
					hallPreference1 = prefs[0];
					hallPreference2 = prefs[1];
					hallPreference3 = prefs[2];
				} 
				else if (housingOption.equals("b")) {
					housingOption = Constants.RESIDENCE_HALL;

					residenceHallNames = getHallNameList();

					hallPreference1 = residenceHallNames.get(0);
					hallPreference2 = residenceHallNames.get(1);
					hallPreference3 = residenceHallNames.get(2);

				} else if (housingOption.equals("c")) {
					housingOption.replaceAll("c", Constants.GENERAL_APARTMENT);
					housingOption = Constants.GENERAL_APARTMENT;
					hallPreference1 = "NA";
					hallPreference2 = "NA";
					hallPreference3 = "NA";
				} else if (housingOption.equals("d")) {
					housingOption = Constants.FAMILY_APARTMENT;
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
					housingOption = Constants.RESIDENCE_HALL;

					residenceHallNames = getHallNameList();
					hallPreference1 = residenceHallNames.get(0);
					hallPreference2 = residenceHallNames.get(1);
					hallPreference3 = residenceHallNames.get(2);
				} else if (housingOption.equals("b")) {
					housingOption = Constants.GENERAL_APARTMENT;
					hallPreference1 = "NA";
					hallPreference2 = "NA";
					hallPreference3 = "NA";
				} else if (housingOption.equals("c")) {
					housingOption = Constants.FAMILY_APARTMENT;
					hallPreference1 = "NA";
					hallPreference2 = "NA";
					hallPreference3 = "NA";
				}

			}
			
			System.out.println("Please choose your payment gateway");
			System.out.println("a. Cash\n"
					+ "b. Credit Card\n"
					+ "c. Debit Card\n"
					+ "d. Money Order\n");
			
			paymentGatewayOption = inputObj.next();
			if (paymentGatewayOption.equalsIgnoreCase("a")) {
				paymentGateway = "Cash";
			} else if (paymentGatewayOption.equalsIgnoreCase("b")) {
				paymentGateway = "Credit Card";
			} else if (paymentGatewayOption.equalsIgnoreCase("c")) {
				paymentGateway = "Debit Card";
			} else if (paymentGatewayOption.equalsIgnoreCase("d")) {
				paymentGateway = "Money Order";
			} else {
				System.out.println("Please enter a proper option");
			}
			
			System.out.println("Selected payment gateway: " + paymentGateway);
			/*SELECTING STAFF NO BLOCK START*/
			String query3 = "SELECT MAX(staff_no) AS LastStaff FROM Housing_Staff";
			int staffNoMax = 0;
			int staffNoMin=0;
			try 
			{
				preparedStatement = dbConnection.prepareStatement(query3);
				ResultSet max = preparedStatement.executeQuery();
				while(max.next())
				{
					staffNoMax = max.getInt("LastStaff");
				}
				
				max.close();
				preparedStatement.close();
			}
			catch(SQLException e)
			{
				try {
					ConnectionUtils.closeConnection(dbConnection);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			
			//PreparedStatement preparedStatement = null;
			//Connection conn1 = ConnectionUtils.getConnection();
			String query4 = "SELECT MIN(staff_no) AS FirstStaff FROM Housing_Staff";
			
			try 
			{
				preparedStatement = dbConnection.prepareStatement(query4);
				ResultSet min = preparedStatement.executeQuery();
				while(min.next())
				{
					staffNoMin = min.getInt("FirstStaff");
				}
			}
			catch(SQLException e)
			{
				try {
					ConnectionUtils.closeConnection(dbConnection);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			
			// Getting a random staff number between First and the Last staff
			staffNo = Utils.randomizeStaff(staffNoMax, staffNoMin);
			
			
			
			/*SELECTING STAFF NI BLOCK END*/

			System.out.println("Date you want to enter in MM/dd/YYYY format: ");
			String moveInDate = inputObj.next();
			java.sql.Date sqlMoveInDate = Utils
					.convertStringToSQLDateFormat(moveInDate);

			System.out.println("Enter the payment options: \n" + "a) Monthly\n"
					+ "b) Semester\n");
			String paymentOption = inputObj.next();
			if (paymentOption.equals("a"))
				paymentOption = Constants.PAYMENTOPTION_MONTHLY;
			else if (paymentOption.equals("b"))
				paymentOption = Constants.PAYMENTOPTION_SEMESTER;

			System.out.println("Do you want to: \n" + "1. Submit\n"
					+ "2. Back ");

			int choice = inputObj.nextInt();
			if (choice == 1) {

				String reqStatus = Constants.PENDING_STATUS;
				String selectQuery3 = "INSERT INTO PERSON_ACC_STAFF (application_request_no,accomodation_type,person_id,request_status, "
						+ "mode_of_payment,lease_move_in_date,duration,PREFERENCE1,PREFERENCE2,PREFERENCE3,staff_no,payment_gateway) "
						+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

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
				preparedStatement.setInt(11, staffNo);
				preparedStatement.setString(12, paymentGateway);
				preparedStatement.executeUpdate();

				System.out
						.println("Success!!!  Please Note down the Request Number for future References=>"
								+ newApplicationRequestNumber);
				
				// Now updating the housing_status for this person to Waiting
				personObj.updateHousingStatus(personId,Constants.WAITING_STATUS);

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
