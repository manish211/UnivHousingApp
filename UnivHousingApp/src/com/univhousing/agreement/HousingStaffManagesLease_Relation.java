package com.univhousing.agreement;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import com.univhousing.main.ConnectionUtils;
import com.univhousing.main.Constants;
import com.univhousing.main.Utils;
import com.univhousing.users.Person;

public class HousingStaffManagesLease_Relation {

	public int leaseNo;
	public int duration;
	public Date cutOffDate;
	public String modeOfPayment;
	public float depsoit;
	public Date date;
	public int staffNo;

	boolean wasAccomodationApproved = false;
	Scanner inputObj = new Scanner(System.in);
	Person personObj ;
	int personID = 0;
	/**
	 * @param ArrayList
	 *            <Integer> allLeaseRequestsToMonitor
	 * @throws SQLException
	 * @action This fetches all the new lease requests submitted for approval
	 */
	public void getAllNewLeaseRequests(
		ArrayList<Integer> allLeaseRequestsToMonitor) throws SQLException {
		personObj =  new Person();
		/* Write SQL Query to fetch all the lease requests pending for approval */

		ResultSet rs = null;
		PreparedStatement preparedStatement = null;
		Connection dbConnection = null;
		String accomodationType = "";
		allLeaseRequestsToMonitor.clear();
		String status = Constants.PENDING_STATUS;
		String modeofPayment = "";
		Date moveInDate = null;
		String duration = "";
		int newLeaseNumber = 0;
		String accomodationTypeGiven = "";
		int accomodationIDGiven = 0;
		ArrayList<String> preferences = new ArrayList<String>();

		try {

			dbConnection = ConnectionUtils.getConnection();
			String selectQuery = "SELECT application_request_no "
					+ "FROM PERSON_ACC_STAFF " + "WHERE request_status = ?";

			preparedStatement = dbConnection.prepareStatement(selectQuery);
			preparedStatement.setString(1, status);

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				allLeaseRequestsToMonitor.add(rs
						.getInt("application_request_no"));
			}

			System.out.println("Displaying all the requests to approve: ");
			for (int i = 0; i < allLeaseRequestsToMonitor.size(); i++) {
				System.out.println((i + 1) + ". "
						+ allLeaseRequestsToMonitor.get(i));
			}
			int requestChosen = inputObj.nextInt();
			int requestNumber = allLeaseRequestsToMonitor
					.get(requestChosen - 1);

			/* Write SQL Query to fetch all the details of the requestNumber */
			// ResultSet requestDetails = null;
			String selectQueryDetails = "SELECT APPLICATION_REQUEST_NO, PERSON_ID, "
					+ "ACCOMODATION_TYPE, MODE_OF_PAYMENT, LEASE_MOVE_IN_DATE,"
					+ "DURATION, PREFERENCE1, PREFERENCE2, PREFERENCE3 "
					+ "FROM PERSON_ACC_STAFF "
					+ "WHERE application_request_no = ?";
			preparedStatement.close();
			rs.close();

			preparedStatement = dbConnection
					.prepareStatement(selectQueryDetails);
			preparedStatement.setInt(1, requestNumber);
			rs = preparedStatement.executeQuery();

			System.out.println(String.format(
					"%-13s%-11s%-20s%-15s%-15s%-9s%-15s%-15s%-15s",
					"App Req No.", "Person ID", "Accommodation Type",
					"Payment Mode", "Move in Date", "Duration", "Preference 1",
					"Preference 2", "Preference 3"));
			System.out
					.println("-----------------------------------------------"
							+ "---------------------------------------------------------"
							+ "--------------------");

			while (rs.next()) {
				System.out.println(String.format(
						"%-13s%-11s%-20s%-15s%-15s%-9s%-15s" + "%-15s%-15s", rs
								.getInt("APPLICATION_REQUEST_NO"), rs
								.getInt("PERSON_ID"), rs
								.getString("ACCOMODATION_TYPE"), rs
								.getString("MODE_OF_PAYMENT"), rs
								.getDate("LEASE_MOVE_IN_DATE"), rs
								.getString("DURATION"), rs
								.getString("PREFERENCE1"), rs
								.getString("PREFERENCE2"), rs
								.getString("PREFERENCE3")));

				/*
				 * Getting the preferences in case the user entered residence
				 * hall.
				 */

				modeofPayment = rs.getString("MODE_OF_PAYMENT");
				duration = rs.getString("DURATION");
				personID = rs.getInt("person_id");
				moveInDate = rs.getDate("LEASE_MOVE_IN_DATE");
				 
				String moveInDateStr = Utils.convertSQLDateToString(moveInDate);
				
				java.util.Date moveInDateUtil = Utils.convertStringToUtilDateFormat(moveInDateStr);
				
						
				
				Calendar c = Calendar.getInstance(); 
				c.setTime(moveInDateUtil); 
				c.add(Calendar.DATE, 15);
				java.util.Date cutOffDateUtils = c.getTime();
				
				String cutOffDateStr = Utils.changeUtilDateToString(cutOffDateUtils);
				
				cutOffDate = Utils.convertStringToSQLDateFormat(cutOffDateStr);
			
				preferences.add(rs.getString("ACCOMODATION_TYPE"));
				preferences.add(rs.getString("PREFERENCE1"));
				preferences.add(rs.getString("PREFERENCE2"));
				preferences.add(rs.getString("PREFERENCE3"));
				
			/*	rs.close();
				preparedStatement.close();
*/
			}

			System.out.println("Do you want to approve this request? Y/N");
			String approvalStatus = inputObj.next();
			
			
			
			
			/*	START: Check to see if the person is already living on some lease Currently*/

			

			//System.out.println("BEFORE>>>>>>>>"+approvalStatus);

			if (approvalStatus.equalsIgnoreCase("Y")) {
				PreparedStatement ps2 = null;
				ResultSet rs2 = null;
				String queryToFindIfAlreadyLiving = "SELECT * FROM person_accomodation_lease WHERE person_id=?";
				ps2 = dbConnection.prepareStatement(queryToFindIfAlreadyLiving);
				ps2.setInt(1, personID);
				rs2 = ps2.executeQuery();
				if (!rs2.isBeforeFirst()) {

					approvalStatus = "Y";
				} else {
					approvalStatus = "N";
					System.out
							.println("This is a duplicate Housing Request, Turn his/her status to waiting and inform the person!!!!");
				}
			}
			

		/*	rs.close();
			preparedStatement.close();
			*/
			
			
			
			
			/*END: Check to see if the person is already living on some lease Currently*/
			//System.out.println("AFTER>>>>>>>>"+approvalStatus);
			
			if (approvalStatus.equalsIgnoreCase("Y")) {

				/*
				 * You want to approve the request. There may or may not be
				 * accommodation available.
				 */

				/*
				 * Now we look whether the accomodation_type provided by the
				 * student is available or not If YES: Assign it to him and
				 * update the respective tables If NO: Request will already be
				 * approved, but the request status will be changed to PENDING
				 * NOTE: accomodationType has to be taken from the request
				 * details
				 */

				// boolean accAvailability =
				// checkIfAccomodationTypeAvailable(preferences);
				
				
				
				ArrayList<String> availableAcco = checkIfAccomodationTypeAvailable(preferences);
				selectQuery = "SELECT MAX(lease_no) AS lease_no "
					+ "FROM Lease";
				rs.close();
				preparedStatement.close();
	
				preparedStatement = dbConnection
						.prepareStatement(selectQuery);
				rs = preparedStatement.executeQuery();
				rs.next();
				newLeaseNumber = rs.getInt("lease_no") + 1;

				if (availableAcco.get(0).equalsIgnoreCase(
						Constants.RESIDENCE_HALL)) {
					accomodationTypeGiven = availableAcco.get(0);
					wasAccomodationApproved = true;
					System.out.println("Availability in: "
							+ availableAcco.get(1));
					// Now we will give him the accommodation he wanted
					/*
					 * Write SQL Query to update his records in the tables
					 * necessary Student should be alloted a room number and a
					 * palce number
					 */

					
				//	System.out.println("TEST1");
					
					String depositAmountResHall = Constants.RESIDENCE_HALL_DEPOSITE;

					String insertQuery = "INSERT INTO LEASE (lease_no,deposit,mode_of_payment,duration,cutoff_date) "
							+ "VALUES (?,?,?,?,?)";
					rs.close();
					preparedStatement.close();
					
					preparedStatement = dbConnection
							.prepareStatement(insertQuery);

					preparedStatement.setInt(1, newLeaseNumber);
					preparedStatement.setString(2, depositAmountResHall);
					preparedStatement.setString(3, modeofPayment);
					preparedStatement.setString(4, duration);
					preparedStatement.setDate(5, cutOffDate);
					preparedStatement.executeUpdate();
					preparedStatement.close();

				//	System.out.println("TEST2");	
					
					String selectQueryRes = "SELECT accomodation_id "
							+ "FROM residence_hall_provides_room "
							+ "WHERE hall_number = (SELECT hall_number "
							+ "						FROM residence_hall "
							+ "						WHERE hall_name = ?) "
							+ "AND accomodation_id NOT IN (SELECT accomodation_id "
							+ "							FROM person_accomodation_lease)";

					preparedStatement = dbConnection
							.prepareStatement(selectQueryRes);
					preparedStatement.setString(1, availableAcco.get(1));
					rs = preparedStatement.executeQuery();
					rs.next();
					int accID = rs.getInt("accomodation_id");
					accomodationIDGiven = accID;
					rs.close();
					preparedStatement.close();

					//System.out.println("TEST3");
					
					System.out.println(newLeaseNumber);
					String insertPerAccQuery = "INSERT INTO person_accomodation_lease (accomodation_id,person_id,lease_no,accomodation_type,lease_move_in_date) "
							+ "VALUES(?,?,?,?,?)";

					preparedStatement = dbConnection
							.prepareStatement(insertPerAccQuery);
					preparedStatement.setInt(1, accID);
					preparedStatement.setInt(2, personID);
					preparedStatement.setInt(3, newLeaseNumber);
					preparedStatement.setString(4, Constants.RESIDENCE_HALL);
					preparedStatement.setDate(5, moveInDate);
					preparedStatement.executeUpdate();

					String updateQuery = "UPDATE PERSON_ACC_STAFF SET request_status = ? "
							+ "WHERE application_request_no = ?";
					preparedStatement.close();

				//	System.out.println("TEST4");	
				
					preparedStatement = dbConnection
							.prepareStatement(updateQuery);
					preparedStatement.setString(1, Constants.PROCESSED_STATUS);
					preparedStatement.setInt(2, requestNumber);
					preparedStatement.executeUpdate();
					preparedStatement.close();

					System.out.println("The status for Request Id "
							+ requestNumber + " is APPROVED!!!!");

				} else if (availableAcco.get(0).equalsIgnoreCase(
						Constants.GENERAL_APARTMENT)) {
					accomodationTypeGiven = availableAcco.get(0);
					wasAccomodationApproved = true;
					
			

					String depositeApartment = Constants.GENERAL_APARTMENT_DEPOSITE; // deposite

					String SQL2 = "INSERT INTO LEASE (lease_no,deposit,mode_of_payment,duration,cutoff_date) "
							+ "VALUES (?,?,?,?,?)";

					preparedStatement = dbConnection.prepareStatement(SQL2);
					preparedStatement.setInt(1, newLeaseNumber);
					preparedStatement.setString(2, depositeApartment);
					preparedStatement.setString(3, modeofPayment);
					preparedStatement.setString(4, duration);
					preparedStatement.setDate(5, cutOffDate);
					preparedStatement.executeUpdate();

					preparedStatement.close();

					String SQL3 = "SELECT bedroom.accomodation_id FROM BEDROOM "
							+ "WHERE bedroom.accomodation_id NOT IN(SELECT PERSON_ACCOMODATION_LEASE.accomodation_id "
							+ "FROM PERSON_ACCOMODATION_LEASE)";

					preparedStatement = dbConnection.prepareStatement(SQL3);
					rs = preparedStatement.executeQuery();
					rs.next();

					int accomodationIDApartment = rs.getInt("accomodation_id");
					accomodationIDGiven = accomodationIDApartment;
					rs.close();
					preparedStatement.close();

					String SQL4 = "INSERT INTO PERSON_ACCOMODATION_LEASE (accomodation_id,person_id,lease_no,accomodation_type,lease_move_in_date) "
							+ "VALUES (?,?,?,?,?)";

					preparedStatement = dbConnection.prepareStatement(SQL4);
					preparedStatement.setInt(1, accomodationIDApartment);
					preparedStatement.setInt(2, personID);
					preparedStatement.setInt(3, newLeaseNumber);
					preparedStatement.setString(4, Constants.GENERAL_APARTMENT);
					preparedStatement.setDate(5, moveInDate);
					preparedStatement.executeUpdate();
					preparedStatement.close();

					String SQL5 = "UPDATE PERSON_ACC_STAFF SET request_status = ? "
							+ "WHERE application_request_no = ?";

					preparedStatement = dbConnection.prepareStatement(SQL5);
					preparedStatement.setString(1, Constants.PROCESSED_STATUS);
					preparedStatement.setInt(2, requestNumber);
					preparedStatement.executeUpdate();
					preparedStatement.close();

					System.out.println("The status for Request Id "
							+ requestNumber + " is APPROVED!!!!");

					/*
					 * Write SQL query to approve the general apartment request
					 * and allocate a room to the applicant
					 */

				} else if (availableAcco.get(0).equalsIgnoreCase(
						Constants.FAMILY_APARTMENT)) {
					accomodationTypeGiven = availableAcco.get(0);	
					wasAccomodationApproved = true;
					
					String depositeApartment = Constants.FAMILY_APARTMENT_DEPOSITE;

					String SQLF2 = "INSERT INTO LEASE (lease_no,deposit,mode_of_payment,duration,cutoff_date) "
							+ "VALUES (?,?,?,?,?)";

					preparedStatement = dbConnection.prepareStatement(SQLF2);
					preparedStatement.setInt(1, newLeaseNumber);
					preparedStatement.setString(2, depositeApartment);
					preparedStatement.setString(3, modeofPayment);
					preparedStatement.setString(4, duration);
					preparedStatement.setDate(5, cutOffDate);
					preparedStatement.executeUpdate();

					preparedStatement.close();

					String SQLF3 = "SELECT Family.accomodation_id FROM Family_Apartment Family "
							+ "WHERE Family.accomodation_id NOT IN(SELECT PERSON_ACCOMODATION_LEASE.accomodation_id "
							+ "FROM PERSON_ACCOMODATION_LEASE)";

					preparedStatement = dbConnection.prepareStatement(SQLF3);
					rs = preparedStatement.executeQuery();
					rs.next();

					int accomodationIDApartment = rs.getInt("accomodation_id");
					accomodationIDGiven = accomodationIDApartment;
					rs.close();
					preparedStatement.close();

					String SQL4 = "INSERT INTO PERSON_ACCOMODATION_LEASE (accomodation_id,person_id,lease_no,accomodation_type,lease_move_in_date) "
							+ "VALUES (?,?,?,?,?)";

					preparedStatement = dbConnection.prepareStatement(SQL4);
					preparedStatement.setInt(1, accomodationIDApartment);
					preparedStatement.setInt(2, personID);
					preparedStatement.setInt(3, newLeaseNumber);
					preparedStatement.setString(4, Constants.FAMILY_APARTMENT);
					preparedStatement.setDate(5, moveInDate);
					preparedStatement.executeUpdate();
					preparedStatement.close();

					String SQLF5 = "UPDATE PERSON_ACC_STAFF SET request_status = ? "
							+ "WHERE application_request_no = ?";

					preparedStatement = dbConnection.prepareStatement(SQLF5);
					preparedStatement.setString(1, Constants.PROCESSED_STATUS);
					preparedStatement.setInt(2, requestNumber);
					preparedStatement.executeUpdate();
					preparedStatement.close();

					System.out.println("The status for Request Id "
							+ requestNumber + " is APPROVED!!!!");

					/*
					 * Write an SQL query to approve the family apartment
					 * request and allocate an apartment to the applicant
					 */
					
					
					/*System.out.println("Please enter your family members name (max 4): ");
					inputObj.nextLine();
					System.out.println("Name of Family member 1: ");
					String memberOne = inputObj.nextLine();
					System.out.println("DOB Of member 1: ");
					String dobMemberOne = inputObj.nextLine();
					java.sql.Date dmo = Utils.convertStringToSQLDateFormat(dobMemberOne);

					System.out.println("Name of Family member 2: ");
					String memberTwo = inputObj.nextLine();
					System.out.println("DOB Of member 2: ");
					String dobMemberTwo = inputObj.nextLine();
					java.sql.Date dmto = Utils.convertStringToSQLDateFormat(dobMemberTwo);
					
					System.out.println("Name of Family member 3: ");
					String memberThree = inputObj.nextLine();
					System.out.println("DOB Of member 3: ");
					String dobMemberThree = inputObj.nextLine();
					java.sql.Date dmth = Utils.convertStringToSQLDateFormat(dobMemberThree);
					
					System.out.println("Name of Family member 4: ");
					String memberFour = inputObj.nextLine();
					System.out.println("DOB Of member 4: ");
					String dobMemberFour = inputObj.nextLine();
					java.sql.Date dmf = Utils.convertStringToSQLDateFormat(dobMemberFour);
					
					Write SQL Query to fill student's family details in students table
					PreparedStatement ps = null;
					Connection conn = ConnectionUtils.getConnection();
					String query = "UPDATE student set member_one = ?, member_two = ?, member_three = ?, member_four = ?" +
							"dob_member_one = ?  (), dob_member_two = ?, dob_member_three = ?, dob_member_four = ? WHERE ";
					
					ps = conn.prepareStatement(query);
					ps.setString(1, memberOne);
					ps.setString(2, memberTwo);
					ps.setString(3, memberThree);
					ps.setString(4, memberFour);
					ps.setDate(5, dmo);
					ps.setDate(6, dmto);
					ps.setDate(7, dmth);
					ps.setDate(8, dmf);
					ps.executeUpdate();
					ps.close();
					ConnectionUtils.closeConnection(conn);*/
				} else if (availableAcco.get(0).equalsIgnoreCase(
						Constants.NOTHING_AVAILABLE)) {
					wasAccomodationApproved = false;
					String selectQ1 = "UPDATE PERSON_ACC_STAFF SET request_status = ? "
							+ "WHERE application_request_no = ?";

					preparedStatement = dbConnection.prepareStatement(selectQ1);
					preparedStatement.setString(1, Constants.WAITING_STATUS);
					preparedStatement.setInt(2, requestNumber);
					preparedStatement.executeUpdate();
					preparedStatement.close();

					System.out.println("The status for Request Id "
							+ requestNumber + " is turned to Waiting!");

				}
			} else { // if availableAcco ="N"
				wasAccomodationApproved = false;
				String selectQ1 = "UPDATE PERSON_ACC_STAFF SET request_status = ? "
						+ "WHERE application_request_no = ?";

				preparedStatement = dbConnection.prepareStatement(selectQ1);
				preparedStatement.setString(1, Constants.WAITING_STATUS);
				preparedStatement.setInt(2, requestNumber);
				preparedStatement.executeUpdate();
				preparedStatement.close();

				System.out.println("The status for Request Id " + requestNumber
						+ " is not Approved and hence turned to Waiting!");
			}
			
			// If the request was approved
			if(wasAccomodationApproved)
			{	
				// Now updating the housing_status for this person to be placed
				personObj.updateHousingStatus(personID,Constants.PLACED_STATUS);
				
				// We will write the logic for invoice generation for this person
				//generateInvoices(modeofPayment,personID,duration,moveInDate,newLeaseNumber,accomodationIDGiven,accomodationTypeGiven);
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

	/**
	 * @param modeofPayment This is either month or semester
	 * @param personID for the person who has to be assigned this room
	 * @param duration The duration of his lease
	 * @param moveInDate When he wants to move in
	 * @throws SQLException 
	 */
	private void generateInvoices(String modeofPayment, int personID,String duration, Date moveInDate, int newLeaseNumber, int accomodationIdGiven, String accomodationTypeGiven) throws SQLException 
	{
		int numberOfInvoices = 0, livingRent = 0, parkingFees = 0, lateFees = 0;
		int incidentalCharges = 0, invoiceNo = 0, leaseNumber = 0, totalPaymentDue = 0, damageCharges = 0;
		String paymentStatus = null;
		String paymentDateString = null;
		
		if(modeofPayment.equalsIgnoreCase(Constants.PAYMENTOPTION_MONTHLY))
		{
			int temp = Integer.parseInt(duration);
			numberOfInvoices = temp*Constants.MONTHS_IN_SEMESTER;
		}
		else if(modeofPayment.equalsIgnoreCase(Constants.PAYMENTOPTION_SEMESTER))
		{
			numberOfInvoices = Integer.parseInt(duration);
		}
		
		PreparedStatement ps = null;
		Connection conn = ConnectionUtils.getConnection();
		String insertQuery = "INSERT INTO invoice_person_lease (monthly_housing_rent,monthly_parking_rent," +
				"late_fees,incidental_charges,invoice_no,payment_date,payment_method,lease_no,payment_status," +
				"payment_due,damage_charges,person_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		ps = conn.prepareStatement(insertQuery);
		ps.setInt(1, livingRent);
		ps.setInt(2, parkingFees);
		ps.setInt(3, lateFees);
		ps.setInt(4, incidentalCharges);
		ps.setInt(5, invoiceNo);
		ps.setString(6, paymentDateString);
		ps.setString(7, modeofPayment);
		ps.setInt(8, newLeaseNumber);
		ps.setString(9, paymentStatus);
		ps.setInt(10, totalPaymentDue);
		ps.setInt(11, damageCharges);
		ps.setInt(12, personID);
		
		getLivingRent(personID,leaseNumber);
		getParkingFees(personID,leaseNumber);
		generateInvoiceNo();
		
		for (int i = 0; i < numberOfInvoices; i++) 
		{
			
		}
	}

	private int generateInvoiceNo() {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		Connection conn = ConnectionUtils.getConnection();
		return 0;
	}

	private void getParkingFees(int personID, int leaseNumber) {
		// TODO Auto-generated method stub
		
	}

	private void getLivingRent(int personID, int leaseNumber) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param accomodationType
	 * @return True if accomodationType is present else False
	 */
	private ArrayList<String> checkIfAccomodationTypeAvailable(
			ArrayList<String> preferences) {
		/*
		 * Write SQL Query to check if the accommodation type student selected
		 * is actually available If YES: return True If NO: return False
		 */
		String type = preferences.get(0);
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;
		Connection dbConnection = null;
		ArrayList<String> availableAcco = new ArrayList<String>();
		try {
			dbConnection = ConnectionUtils.getConnection();

			if (type.equalsIgnoreCase(Constants.RESIDENCE_HALL)) {
				for (int i = 0; i < 3; i++) {
					String selectQueryPref = "SELECT COUNT(accomodation_id) AS count "
							+ "FROM residence_hall_provides_room "
							+ "WHERE hall_number = (SELECT hall_number "
							+ "						FROM residence_hall "
							+ "						WHERE hall_name = ?) "
							+ "AND accomodation_id NOT IN (SELECT accomodation_id "
							+ "							FROM person_accomodation_lease)";

					preparedStatement = dbConnection
							.prepareStatement(selectQueryPref);
					preparedStatement.setString(1, preferences.get(i + 1));
					rs = preparedStatement.executeQuery();

					while (rs.next()) {
						if (rs.getInt("count") > 0) {
							availableAcco.add("RESIDENCE HALL");
							availableAcco.add(preferences.get(i + 1));
							dbConnection.close();
							return availableAcco;
						}
					}
					preparedStatement.close();
					rs.close();
				}
				availableAcco.add(Constants.NOTHING_AVAILABLE);
				return availableAcco;
			} else if (type.equalsIgnoreCase(Constants.GENERAL_APARTMENT)) {
				String selectQueryGenApt = "SELECT COUNT (B.apt_place_no) AS rooms "
						+ "FROM bedroom B "
						+ "WHERE B.accomodation_id  NOT IN "
						+ "(SELECT accomodation_id "
						+ "FROM person_accomodation_lease)";

				preparedStatement = dbConnection
						.prepareStatement(selectQueryGenApt);
				rs = preparedStatement.executeQuery();

				while (rs.next()) {
					if (rs.getInt("rooms") > 0) {
						availableAcco.add(Constants.GENERAL_APARTMENT);
						availableAcco.add("NA");
						dbConnection.close();
						return availableAcco;
					}
					preparedStatement.close();
					rs.close();
				}
				availableAcco.add(Constants.NOTHING_AVAILABLE);
				return availableAcco;
			} else if (type.equalsIgnoreCase(Constants.FAMILY_APARTMENT)) {
				String selectQueryFamApt = "SELECT COUNT (F.apt_no) AS apartments "
						+ "FROM Family_Apartment F "
						+ "WHERE F.accomodation_id NOT IN "
						+ "(SELECT accomodation_id "
						+ "FROM person_accomodation_lease)";

				preparedStatement = dbConnection
						.prepareStatement(selectQueryFamApt);
				rs = preparedStatement.executeQuery();

				while (rs.next()) {
					if (rs.getInt("apartments") > 0) {
						availableAcco.add(Constants.FAMILY_APARTMENT);
						availableAcco.add("NA");
						dbConnection.close();
						return availableAcco;
					}
					preparedStatement.close();
					rs.close();
				}
				availableAcco.add(Constants.NOTHING_AVAILABLE);
				return availableAcco;
			} else {
				availableAcco.add(Constants.NOTHING_AVAILABLE);
				return availableAcco;
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
		availableAcco.add("NA");
		return availableAcco;
	}

	/**
	 * @param ArrayList
	 *            <Integer> adminLevelTerminationRequests
	 * @throws SQLException
	 */
	public void getAllNewTerminationRequests(
			ArrayList<Integer> allTerminationRequestsToMonitor)
			throws SQLException {
		
/*		
		BEFORE YOU SHOW ALL THE REQUESTS, UPDATE THE STATUS OF EXISTING RECORDS
		WHOSE STATUS IS PROCESSED AND THE INSPECTION DATE IS LESS THAN THE CURRENT DATE OR SYSDATE.
*/		
		String updateStatusQuery = "update termination_requests set status = ? where status = ? and inspection_date < sysdate";
		PreparedStatement psForUpdate = null;
		Connection connForUpdate= ConnectionUtils.getConnection();
		
		System.out.println("Updating the status of all requests under processing to complete with inspection_date less than today's date");
		psForUpdate = connForUpdate.prepareStatement(updateStatusQuery);
		psForUpdate.setString(1, Constants.COMPLETE_STATUS);
		psForUpdate.setString(2, Constants.PROCESSING_STATUS);
		psForUpdate.executeUpdate();
		
		ConnectionUtils.closeConnection(connForUpdate);
		
		System.out.println("\nUpdate successful...Fetching the termination requests. Please wait...\n");
		
		
		

		/* Write SQL Query to fetch all the termination requests */
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;
		Connection dbConnection = null;
		allTerminationRequestsToMonitor.clear();
		String status = Constants.PENDING_STATUS;
		ArrayList<Integer> allPersonID = new ArrayList<Integer>();
		allPersonID.clear();

		try {

			/*
			 * Query for getting termination request numbers whose status is
			 * PENDING
			 */
			dbConnection = ConnectionUtils.getConnection();
			String selectQuery = "SELECT termination_request_number, person_id "
					+ "FROM TERMINATION_REQUESTS " + "WHERE STATUS = ?";

			preparedStatement = dbConnection.prepareStatement(selectQuery);
			preparedStatement.setString(1, status);

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				allTerminationRequestsToMonitor.add(rs
						.getInt("termination_request_number"));
				allPersonID.add(rs.getInt("person_id"));
			}

			System.out.println("Displaying all the requests to approve: ");
			for (int i = 0; i < allTerminationRequestsToMonitor.size(); i++) {
				System.out.println((i + 1) + ". "
						+ allTerminationRequestsToMonitor.get(i) + " - "
						+ allPersonID.get(i));
			}
			int requestChosen = inputObj.nextInt();
			int requestNumber = allTerminationRequestsToMonitor
					.get(requestChosen - 1);

			/* Write SQL Query to fetch all the details of the requestNumber */
			String selectQueryDetails = "SELECT reason, termination_request_number,"
					+ "status, termination_date, inspection_date, person_id, staff_no "
					+ "FROM Termination_Requests "
					+ "WHERE termination_request_number = ?";
			preparedStatement.close();
			rs.close();

			preparedStatement = dbConnection
					.prepareStatement(selectQueryDetails);
			preparedStatement.setInt(1, requestNumber);
			rs = preparedStatement.executeQuery();

			System.out.println(String.format(
					"%-50s%-13s%-11s%-15s%-18s%-5s%-10s", "Reason",
					"Term_req_no", "Status", "Term Date", "Inspection Date",
					"P_ID", "Staff No."));
			System.out
					.println("-----------------------------------------------"
							+ "---------------------------------------------------------"
							+ "--------------------");

			while (rs.next()) {
				System.out.println(String.format(
						"%-50s%-13s%-11s%-15s%-18s%-5s%-10s", rs
								.getString("reason"), rs
								.getInt("termination_request_number"), rs
								.getString("status"), rs
								.getDate("termination_date"), rs
								.getDate("inspection_date"), rs
								.getInt("person_id"), rs.getInt("staff_no")));
			}

			System.out.println("Enter the inspection date in MM/DD/YYYY format");
			String inspectionDate = inputObj.next();
			java.sql.Date sqlInspectionDate = Utils.convertStringToSQLDateFormat(inspectionDate);
			
			String updateQueryDate = "UPDATE Termination_Requests "
					+ "SET inspection_date = ? "
					+ "WHERE termination_request_number = ?";
			PreparedStatement pAddInspecDate = null;
			pAddInspecDate = dbConnection.prepareStatement(updateQueryDate);
			pAddInspecDate.setDate(1, sqlInspectionDate);
			pAddInspecDate.setInt(2, requestNumber);
			
			pAddInspecDate.executeUpdate();
			
			pAddInspecDate.close();
			
			
			int damageFees = 0;
			System.out.println("Please enter the damage fees:");
			damageFees = inputObj.nextInt();

			/*
			 * Write SQL Trigger to change the status of request to Complete
			 * after the inspection date
			 */

			/*
			 * Write SQL Query to fetch the latest unpaid invoice and add the
			 * damageFees to already existing payment_due
			 */
			
			String updateQueryStatus = "UPDATE Termination_Requests "
					+ "SET status = ? "
					+ "WHERE termination_request_number = ?";
			
			PreparedStatement pUpdateStatus = null;
			pUpdateStatus = dbConnection.prepareStatement(updateQueryStatus);
			pUpdateStatus.setString(1, Constants.PROCESSED_STATUS);
			pUpdateStatus.setInt(2, requestNumber);
			pUpdateStatus.executeUpdate();
			
			pUpdateStatus.close();
			
			String selectPersonID = "SELECT person_id "
					+ "FROM termination_requests "
					+ "WHERE termination_request_number = ?";
			rs.close();
			preparedStatement.close();
			
			preparedStatement = dbConnection.prepareStatement(selectPersonID);
			preparedStatement.setInt(1, requestNumber);
			rs = preparedStatement.executeQuery();
			rs.next();
			int personID = rs.getInt("person_id");
			
			String selectQueryFees = "SELECT MAX(payment_date) as \"date\""
					+ "FROM invoice_person_lease "
					+ "WHERE (payment_status <> ?  "
					+ "AND person_id = ?) ";

			rs.close();
			preparedStatement.close();
			
			System.out.println("Getting date for person_id: " + personID 
					+ " and requestNumber: " + requestNumber);
			preparedStatement = dbConnection.prepareStatement(selectQueryFees);
			preparedStatement.setString(1, "'Paid'");
			preparedStatement.setInt(2, requestNumber);
			rs = preparedStatement.executeQuery();
			
			rs.next();
			Date maxDate = rs.getDate("date");
			System.out.println("Maximum date: " + maxDate);
			if (maxDate == null) {
				System.out.println("No outstanding charges");
				System.out.println("Damage fees: " + damageFees);
				
				if (damageFees > 0) {

					/*
					 * Query for getting invoice number from 
					 * invoice_person_lease
					 */
					String selectQueryInvoiceNum = "SELECT MAX(invoice_no) as invoice_no "
							+ "FROM invoice_person_lease ";
					rs.close();
					preparedStatement.close();

					preparedStatement = dbConnection.prepareStatement(selectQueryInvoiceNum);
					rs = preparedStatement.executeQuery();
					rs.next();
					System.out.println("Breakpoint1");
					int invoiceNumber = rs.getInt("invoice_no");
					invoiceNumber++;
					rs.close();
					
					String selectQueryLeaseNum = "SELECT lease_no "
							+ "FROM person_accomodation_lease "
							+ "WHERE person_id = ?";
					preparedStatement.close();

					preparedStatement = dbConnection.prepareStatement(selectQueryLeaseNum);
					preparedStatement.setInt(1, personID);

					rs = preparedStatement.executeQuery();
					boolean isNotEmpty = rs.next();
					if (isNotEmpty) {
						System.out.println("Breakpoint2");
						int leaseNumber = rs.getInt("lease_no");

						PreparedStatement p2 = null;
						Connection c2 = null;

						/*
						 * Write a query to insert an entry into the invoice_person_lease table
						 */
						String insertQueryLease = "INSERT INTO invoice_person_lease "
								+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.DATE, 30);
						java.util.Date justNowUtils = cal.getTime();
						String temp = Utils.changeUtilDateToString(justNowUtils);
						Date oneMonthLater = Utils.convertStringToSQLDateFormat(temp);

						c2 = ConnectionUtils.getConnection();
						p2 = c2.prepareStatement(insertQueryLease);
						p2.setInt(1, 0);
						p2.setInt(2, 0);
						p2.setInt(3, 0);
						p2.setInt(4, 0);
						p2.setInt(5, invoiceNumber);
						p2.setDate(6, oneMonthLater);
						p2.setString(7, "Credit");
						p2.setInt(8, leaseNumber);
						p2.setString(9, "Outstanding");
						p2.setInt(10, damageFees);
						p2.setInt(11, 0);
						p2.setInt(12, personID);

						int insertRes = p2.executeUpdate();

						p2.close();
						c2.close();
					} else {
						System.out.println("There is no lease entry for this person");
					}
				} else {
					
					/*
					 * There are no damage fees. Write a query 
					 * to remove the entry from person_accommodation_lease
					 */
					String getLease = "SELECT lease_no "
							+ "FROM person_accomodation_lease "
							+ "WHERE person_id = ?";
					rs.close();
					preparedStatement.close();
					
					preparedStatement = dbConnection.prepareStatement(getLease);
					preparedStatement.setInt(1, personID);
					
					rs = preparedStatement.executeQuery();
					rs.next();
					int leaseNumber = rs.getInt("lease_no");
					System.out.println("Lease number: " + leaseNumber
							+ ". PersonID: " + personID);
					
					/*
					 * Delete entry from person_accommodation_lease
					 */
					Connection connDeleteEntry = null;
					PreparedStatement pDeleteEntry = null;
					
					connDeleteEntry = ConnectionUtils.getConnection();
					String deleteQuery = "DELETE FROM person_accomodation_lease "
							+ "WHERE person_id = ?";
					
					pDeleteEntry = connDeleteEntry.prepareStatement(deleteQuery);
					pDeleteEntry.setInt(1, personID);
					pDeleteEntry.executeUpdate();
					
					System.out.println("After deleting from "
							+ "person_accomodation_lease");
					
					/*
					 * Delete entry from invoice_person_lease
					 */
					Connection connDeleteInvoice = null;
					PreparedStatement pDeleteInvoice = null;
					connDeleteInvoice = ConnectionUtils.getConnection();
					String deleteInvoice = "DELETE FROM invoice_person_lease "
							+ "WHERE (lease_no = ? AND person_id = ?)";
					pDeleteInvoice = connDeleteInvoice.prepareStatement(deleteInvoice);
					pDeleteInvoice.setInt(1, leaseNumber);
					pDeleteInvoice.setInt(2, personID);
					pDeleteInvoice.executeUpdate();
					System.out.println("After deleting from invoice_person_lease");
					
					
					/*
					 * Delete entry from lease
					 */
					Connection connDeleteLease = null;
					PreparedStatement pDeleteLease = null;
					
					connDeleteLease = ConnectionUtils.getConnection();
					String deleteLease = "DELETE FROM Lease "
							+ "WHERE lease_no = ?";
					
					pDeleteLease = connDeleteLease.prepareStatement(deleteLease);
					pDeleteLease.setInt(1, leaseNumber);
					pDeleteLease.executeUpdate();
					
					System.out.println("After deleting from "
							+ "lease");
					connDeleteEntry.close();
					connDeleteLease.close();
					connDeleteInvoice.close();
					pDeleteEntry.close();
					pDeleteLease.close();
					pDeleteInvoice.close();
				}
			} else {
				System.out.println("Maximum date: " + maxDate);
				Date maxDateCopy = maxDate;
				Date maxDateCopy1 = maxDateCopy;
				String a = "'";
				String maximumDate = maxDateCopy1.toString();
				maximumDate = a.concat(maximumDate);
				maximumDate	= maximumDate.concat(a);
				String dateFormat = "'yyyy-mm-dd'";
				String selectQueryDamageFields = "SELECT monthly_housing_rent,"
						+ "monthly_parking_rent,late_fees,incidental_charges,"
						+ "damage_charges "
						+ "FROM invoice_person_lease "
						+ "WHERE payment_date = to_date(?,?) "
						+ "AND person_id = (SELECT person_id FROM termination_requests "
						+ "WHERE termination_request_number = ?)";
				rs.close();
				preparedStatement.close();

				
				System.out.println("Maximum date and string and request number are: " 
						+ maximumDate + " " + dateFormat + " " + requestNumber);

				preparedStatement = dbConnection.prepareStatement(selectQueryDamageFields);
				preparedStatement.setString(1, maximumDate);
				preparedStatement.setString(2, dateFormat);
				preparedStatement.setInt(3, requestNumber);

				System.out.println("Before updating the table");
				rs = preparedStatement.executeQuery();
				System.out.println("After updating the table");
				rs.next();
				int monthlyHousingRent = rs.getInt("monthly_housing_rent");
				int monthlyParkingRent = rs.getInt("monthly_parking_rent");
				int lateFees = rs.getInt("late_fees");
				int incidentalCharges = rs.getInt("incidental_charges");
				int damageCharges = rs.getInt("damage_charges");

				dbConnection.commit();
				int totalCharges = monthlyHousingRent + monthlyParkingRent +
						lateFees + incidentalCharges + damageCharges;
				/*System.out.println(monthlyHousingRent + " " + monthlyParkingRent + " "
					+ lateFees + " " + incidentalCharges + " " + damageCharges);*/
				String updateQueryDamage = "UPDATE invoice_person_lease "
						+ "SET payment_due = ?"
						+ "WHERE (payment_date = to_date(?,?) "
						+ "AND person_id = ? )";

				rs.close();
				Connection c1 =ConnectionUtils.getConnection();
				PreparedStatement p1 = null;			
				p1 = c1.prepareStatement(updateQueryDamage);
				p1.setInt(1, totalCharges);
				p1.setString(2, maximumDate);
				p1.setString(3, dateFormat);
				p1.setInt(4, personID);

				//System.out.println(updateQueryDamage);
				int z = p1.executeUpdate();
				System.out.println("AFTER UPDATE");
				p1.close();
				c1.close();
				
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

	public void checkForLeaseCompletion() {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {

			dbConnection = ConnectionUtils.getConnection();

			String selectQuery = "SELECT * FROM PERSON_ACC_STAFF WHERE REQUEST_STATUS= ? OR  REQUEST_STATUS =? ";

			preparedStatement = dbConnection.prepareStatement(selectQuery);
			preparedStatement.setString(1, Constants.PROCESSED_STATUS);
			preparedStatement.setString(2, Constants.COMPLETED_LEASE_STATUS);

			// taking completed entries as well because if bill unpaid, next
			// time it should be checked
			rs = preparedStatement.executeQuery();
			System.out.println("");
			while (rs.next()) {

				java.util.Date currentDateUtil = new java.util.Date();
				int personID = rs.getInt("person_id");
				Date moveInDate = rs.getDate("lease_move_in_date");
				int duration = Integer.parseInt(rs.getString("Duration"));
				int requestNumber = rs.getInt("application_request_no");
				String moveInDateStr = Utils.changeUtilDateToString(moveInDate);
				java.util.Date moveInDateUtils = Utils
						.convertStringToUtilDateFormat(moveInDateStr);

				Calendar c = Calendar.getInstance();
				c.setTime(moveInDateUtils);
				c.add(Calendar.MONTH, duration * 4);
				java.util.Date moveOutDateUtils = c.getTime();

				if (currentDateUtil.compareTo(moveOutDateUtils) >= 0) {

					String selectQ1 = "UPDATE PERSON_ACC_STAFF SET request_status = ? "
							+ "WHERE application_request_no = ?";

					preparedStatement = dbConnection.prepareStatement(selectQ1);
					preparedStatement.setString(1,
							Constants.COMPLETED_LEASE_STATUS);
					preparedStatement.setInt(2, requestNumber);
					preparedStatement.executeUpdate();
					preparedStatement.close();

					System.out.println("Request number <" + requestNumber
							+ "> with Person Id [" + personID
							+ "] has completed the LEASE!");

					ResultSet rsToCheckPresentinLeaseAcc = null;
					String selectQ2 = "SELECT person_id from PERSON_ACCOMODATION_LEASE WHERE person_id=?";
					preparedStatement = dbConnection.prepareStatement(selectQ2);
					preparedStatement.setInt(1, personID);
					rsToCheckPresentinLeaseAcc = preparedStatement
							.executeQuery();

					if (rsToCheckPresentinLeaseAcc.isBeforeFirst()) {

						boolean flagUnpaid = false;
						boolean paid = false;
						rsToCheckPresentinLeaseAcc.next();
						System.out.println("Cheking if personID [" + personID
								+ "] has paid his/her dues... ");

						PreparedStatement preparedStatement2 = null;
						ResultSet rsToCheckIfInvoice = null;

						String selectQ3 = "SELECT PAYMENT_STATUS FROM INVOICE_PERSON_LEASE WHERE person_id= ? ";
						preparedStatement2 = dbConnection
								.prepareStatement(selectQ3);
						preparedStatement2.setInt(1, personID);
						rsToCheckIfInvoice = preparedStatement2.executeQuery();

						if (rsToCheckIfInvoice.isBeforeFirst()) {

							while (rsToCheckIfInvoice.next()) {

								if (rsToCheckIfInvoice.getString(
										"payment_status").equals("Outstanding")) {

									flagUnpaid = true;
									break;
								}

							}

							paid = true;
						}

						else {

							System.out
									.println("The person ["
											+ personID
											+ "] has not yet generated the Invioce. Sending Email reminder....\n");
						}

						if (flagUnpaid) {
							System.out
									.println("The person ["
											+ personID
											+ "] has still not paid his/her dues. Sending Email reminder....!");
						} else if (flagUnpaid == false && paid == true) {

							System.out
									.println("DELETING PID< "
											+ personID
											+ "> From lease table. Sending Exit email...");

							PreparedStatement preparedStatement3 = null;
							ResultSet rsToDelete = null;

							String deleteQuery = "DELETE FROM PERSON_ACCOMODATION_LEASE WHERE person_id=?";
							preparedStatement3 = dbConnection
									.prepareStatement(deleteQuery);
							preparedStatement3.setInt(1, personID);
							preparedStatement3.executeUpdate();

						}

					}

					else {
						System.out
								.println("The person ID ["
										+ personID
										+ "] has paid his dues and not present anymore");

					}					
					
					
					
					
				}

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
}
