package com.univhousing.agreement;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import com.sun.corba.se.pept.encoding.InputObject;
import com.univhousing.main.ConnectionUtils;
import com.univhousing.main.Constants;

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

	/**
	 * @param ArrayList
	 *            <Integer> allLeaseRequestsToMonitor
	 * @throws SQLException
	 * @action This fetches all the new lease requests submitted for approval
	 */
	public void getAllNewLeaseRequests(
			ArrayList<Integer> allLeaseRequestsToMonitor) throws SQLException {

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
		int personID = 0;
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

				preferences.add(rs.getString("ACCOMODATION_TYPE"));
				preferences.add(rs.getString("PREFERENCE1"));
				preferences.add(rs.getString("PREFERENCE2"));
				preferences.add(rs.getString("PREFERENCE3"));

			}

			System.out.println("Do you want to approve this request? Y/N");
			String approvalStatus = inputObj.next();
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

					
					String depositAmountResHall = "500";

					String insertQuery = "INSERT INTO LEASE "
							+ "VALUES (?,?,?,?)";
					rs.close();
					preparedStatement.close();
					preparedStatement = dbConnection
							.prepareStatement(insertQuery);

					preparedStatement.setInt(1, newLeaseNumber);
					preparedStatement.setString(1, depositAmountResHall);
					preparedStatement.setString(2, modeofPayment);
					preparedStatement.setString(4, duration);
					preparedStatement.executeUpdate();
					preparedStatement.close();

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

					String insertPerAccQuery = "INSERT INTO person_accomodation_lease "
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

					preparedStatement = dbConnection
							.prepareStatement(updateQuery);
					preparedStatement.setString(1, Constants.APPROVED_STATUS);
					preparedStatement.setInt(2, requestNumber);
					preparedStatement.executeUpdate();
					preparedStatement.close();

					System.out.println("The status for Request Id "
							+ requestNumber + " is APPROVED!!!!");

				} else if (availableAcco.get(0).equalsIgnoreCase(
						Constants.GENERAL_APARTMENT)) {
					accomodationTypeGiven = availableAcco.get(0);
					wasAccomodationApproved = true;
					
			

					String depositeApartment = "1000"; // deposite

					String SQL2 = "INSERT INTO LEASE (lease_no,deposit,mode_of_payment,duration) "
							+ "VALUES (?,?,?,?)";

					preparedStatement = dbConnection.prepareStatement(SQL2);
					preparedStatement.setInt(1, newLeaseNumber);
					preparedStatement.setString(2, depositeApartment);
					preparedStatement.setString(3, modeofPayment);
					preparedStatement.setString(4, duration);
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
					preparedStatement.setString(1, Constants.APPROVED_STATUS);
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
					
					String depositeApartment = "2000";

					String SQLF2 = "INSERT INTO LEASE (lease_no,deposit,mode_of_payment,duration) "
							+ "VALUES (?,?,?,?)";

					preparedStatement = dbConnection.prepareStatement(SQLF2);
					preparedStatement.setInt(1, newLeaseNumber);
					preparedStatement.setString(2, depositeApartment);
					preparedStatement.setString(3, modeofPayment);
					preparedStatement.setString(4, duration);
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
					preparedStatement.setString(1, Constants.APPROVED_STATUS);
					preparedStatement.setInt(2, requestNumber);
					preparedStatement.executeUpdate();
					preparedStatement.close();

					System.out.println("The status for Request Id "
							+ requestNumber + " is APPROVED!!!!");

					/*
					 * Write an SQL query to approve the family apartment
					 * request and allocate an apartment to the applicant
					 */
				} else if (availableAcco.get(0).equalsIgnoreCase(
						Constants.NOTHING_AVAILABLE)) {
					wasAccomodationApproved = false;
					String selectQ1 = "UPDATE PERSON_ACC_STAFF SET request_status = ? "
							+ "WHERE application_request_no = ?";

					preparedStatement = dbConnection.prepareStatement(selectQ1);
					preparedStatement.setString(1, Constants.PENDING_STATUS);
					preparedStatement.setInt(2, requestNumber);
					preparedStatement.executeUpdate();
					preparedStatement.close();

					System.out.println("The status for Request Id "
							+ requestNumber + " is turned to Waiting!");

				}
			} else { // if availableAcco ="N"

				String selectQ1 = "UPDATE PERSON_ACC_STAFF SET request_status = ? "
						+ "WHERE application_request_no = ?";

				preparedStatement = dbConnection.prepareStatement(selectQ1);
				preparedStatement.setString(1, Constants.PENDING_STATUS);
				preparedStatement.setInt(2, requestNumber);
				preparedStatement.executeUpdate();
				preparedStatement.close();

				System.out.println("The status for Request Id " + requestNumber
						+ " is not Approved and hence turned to Waiting!");
			}
			
			// If the request was approved
			if(wasAccomodationApproved)
			{
				// We will write the logic for invoice generation for this person
				generateInvoices(modeofPayment,personID,duration,moveInDate,newLeaseNumber,accomodationIDGiven,accomodationTypeGiven);
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

		/* Write SQL Query to fetch all the termination requests */
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;
		Connection dbConnection = null;
		allTerminationRequestsToMonitor.clear();
		String status = Constants.PENDING_STATUS;

		try {

			/*
			 * Query for getting termination request numbers whose status is
			 * PENDING
			 */
			dbConnection = ConnectionUtils.getConnection();
			String selectQuery = "SELECT termination_request_number "
					+ "FROM TERMINATION_REQUESTS " + "WHERE STATUS = ?";

			preparedStatement = dbConnection.prepareStatement(selectQuery);
			preparedStatement.setString(1, status);

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				allTerminationRequestsToMonitor.add(rs
						.getInt("termination_request_number"));
			}

			System.out.println("Displaying all the requests to approve: ");
			for (int i = 0; i < allTerminationRequestsToMonitor.size(); i++) {
				System.out.println((i + 1) + ". "
						+ allTerminationRequestsToMonitor.get(i));
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

			String selectQueryFees = "SELECT MAX(payment_date) as \"date\""
					+ "FROM invoice_person_lease "
					+ "WHERE payment_status <> ? "
					+ "AND person_id = (SELECT person_id "
					+ "FROM termination_requests "
					+ "WHERE termination_request_number = ?)";
			rs.close();
			preparedStatement.close();

			preparedStatement = dbConnection.prepareStatement(selectQueryFees);
			preparedStatement.setString(1, "'Paid'");
			preparedStatement.setInt(2, requestNumber);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				System.out.println("Maximum date: " + rs.getDate("date"));
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
