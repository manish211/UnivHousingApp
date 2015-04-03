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

public class HousingStaffManagesLease_Relation {

	public int leaseNo;
	public int duration;
	public Date cutOffDate;
	public String modeOfPayment;
	public float depsoit;
	public Date date;
	public int staffNo;

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
		String status = "PENDING";
		String modeofPayment = "";
		Date moveInDate = null;
		String duration = "";
		int personID = 0;
		ArrayList<String> preferences = new ArrayList<String>();

		try {

			dbConnection = ConnectionUtils.getConnection();
			String selectQuery = "SELECT application_request_no "
					+ "FROM PERSON_ACC_STAFF "
					+ "WHERE UPPER(request_status) = ?";

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
						"%-13s%-11s%-20s%-15s%-15s%-9s%-15s" + "%-15s%-15s",
						rs.getInt("APPLICATION_REQUEST_NO"),
						rs.getInt("PERSON_ID"),
						rs.getString("ACCOMODATION_TYPE"),
						rs.getString("MODE_OF_PAYMENT"),
						rs.getDate("LEASE_MOVE_IN_DATE"),
						rs.getString("DURATION"), rs.getString("PREFERENCE1"),
						rs.getString("PREFERENCE2"),
						rs.getString("PREFERENCE3")));

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

				// if(accAvailability)

				if (availableAcco.get(0).equalsIgnoreCase("RESIDENCE HALL")) {
					System.out.println("Availability in: "
							+ availableAcco.get(1));
					// Now we will give him the accommodation he wanted
					/*
					 * Write SQL Query to update his records in the tables
					 * necessary Student should be alloted a room number and a
					 * palce number
					 */
					ResultSet approveAndAssignAccomodation = null;

				}

				else if (availableAcco.get(0).equalsIgnoreCase("Apartment")) {

					String SQL1 = "SELECT MAX(LEASE_NO) as Lease_no from LEASE";

					preparedStatement = dbConnection.prepareStatement(SQL1);
					rs = preparedStatement.executeQuery();
					rs.next();
					int leaseNumApartment = rs.getInt("Lease_No") + 1; // Lease
																		// Number
					rs.close();
					preparedStatement.close();

					String depositeApartment = "1000"; // deposite

					String SQL2 = "INSERT INTO LEASE (lease_no,deposit,mode_of_payment,duration) "
							+ "VALUES (?,?,?,?)";

					preparedStatement = dbConnection.prepareStatement(SQL2);
					preparedStatement.setInt(1, leaseNumApartment);
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

					rs.close();
					preparedStatement.close();

					String SQL4 = "INSERT INTO PERSON_ACCOMODATION_LEASE (accomodation_id,person_id,lease_no,accomodation_type,lease_move_in_date) "
							+ "VALUES (?,?,?,?,?)";

					preparedStatement = dbConnection.prepareStatement(SQL4);
					preparedStatement.setInt(1, accomodationIDApartment);
					preparedStatement.setInt(2, personID);
					preparedStatement.setInt(3, leaseNumApartment);
					preparedStatement.setString(4, "Apartment");
					preparedStatement.setDate(5, moveInDate);
					preparedStatement.executeUpdate();
					preparedStatement.close();

					String SQL5 = "UPDATE PERSON_ACC_STAFF SET request_status = 'Approved' "
							+ "WHERE application_request_no = ?";

					preparedStatement = dbConnection.prepareStatement(SQL5);
					preparedStatement.setInt(1, requestNumber);
					preparedStatement.executeUpdate();
					preparedStatement.close();

					System.out.println("The status for Request Id "
							+ requestNumber + " is APPROVED!!!!");

					/*
					 * Write SQL query to approve the general apartment request
					 * and allocate a room to the applicant
					 */

				} else if (availableAcco.get(0).equalsIgnoreCase(
						"Family Apartment")) {
					
					

					String SQLF1 = "SELECT MAX(LEASE_NO) as Lease_no from LEASE";

					preparedStatement = dbConnection.prepareStatement(SQLF1);
					rs = preparedStatement.executeQuery();
					rs.next();
					int leaseNumApartment = rs.getInt("Lease_No") + 1; // Lease
																		// Number
					rs.close();
					preparedStatement.close();
					String depositeApartment = "2000";
					
					String SQLF2 = "INSERT INTO LEASE (lease_no,deposit,mode_of_payment,duration) "
							+ "VALUES (?,?,?,?)";

					preparedStatement = dbConnection.prepareStatement(SQLF2);
					preparedStatement.setInt(1, leaseNumApartment);
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

					rs.close();
					preparedStatement.close();
					

					
					String SQL4 = "INSERT INTO PERSON_ACCOMODATION_LEASE (accomodation_id,person_id,lease_no,accomodation_type,lease_move_in_date) "
							+ "VALUES (?,?,?,?,?)";

					preparedStatement = dbConnection.prepareStatement(SQL4);
					preparedStatement.setInt(1, accomodationIDApartment);
					preparedStatement.setInt(2, personID);
					preparedStatement.setInt(3, leaseNumApartment);
					preparedStatement.setString(4, "Family Apartment");
					preparedStatement.setDate(5, moveInDate);
					preparedStatement.executeUpdate();
					preparedStatement.close();

					

					String SQLF5 = "UPDATE PERSON_ACC_STAFF SET request_status = 'Approved' "
							+ "WHERE application_request_no = ?";

					preparedStatement = dbConnection.prepareStatement(SQLF5);
					preparedStatement.setInt(1, requestNumber);
					preparedStatement.executeUpdate();
					preparedStatement.close();

					System.out.println("The status for Request Id "
							+ requestNumber + " is APPROVED!!!!");

					
					
					
					
					/*
					 * Write an SQL query to approve the family apartment
					 * request and allocate an apartment to the applicant
					 */
				} else if (availableAcco.get(0).equalsIgnoreCase(
						"Nothing Available")) {

					String selectQ1 = "UPDATE PERSON_ACC_STAFF SET request_status = 'Waiting' "
							+ "WHERE application_request_no = ?";

					preparedStatement = dbConnection.prepareStatement(selectQ1);
					preparedStatement.setInt(1, requestNumber);
					preparedStatement.executeUpdate();
					preparedStatement.close();

					System.out.println("The status for Request Id "
							+ requestNumber + " is turned to Waiting!");
					/*
					 * First we will write a query to approve the status,
					 * irrespective of whether there is accommodation available
					 * for that accommodation type
					 */

					/*
					 * Write SQL Query to change the status of the request to
					 * waiting list, this can be done from table
					 * PERSON_ACC_STAFF (not sure)
					 */

					ResultSet changeRequestStatus = null;

				}
			} else { // if availableAcco ="N"
				/*
				 * You DO NOT want to approve the request. Put the student's
				 * request in the waiting list.
				 */
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

			if (type.toUpperCase().equals("RESIDENCE HALL")) {
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
			} else if (type.toUpperCase().equals("APARTMENT")) {
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
						availableAcco.add("APARTMENT");
						availableAcco.add("NA");
						dbConnection.close();
						return availableAcco;
					}
					preparedStatement.close();
					rs.close();
				}
			} else if (type.toUpperCase().equals("FAMILY APARTMENT")) {
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
						availableAcco.add("FAMILY APARTMENT");
						availableAcco.add("NA");
						dbConnection.close();
						return availableAcco;
					}
					preparedStatement.close();
					rs.close();
				}
			} else {
				availableAcco.add("Nothing Available");
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
		ResultSet allRequests = null;
		allTerminationRequestsToMonitor.clear();
		while (allRequests.next()) {
			allTerminationRequestsToMonitor.add(allRequests
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
		ResultSet requestDetails = null;

		int damageFees = 0;
		System.out.println("Please enter the damage fees:");
		damageFees = inputObj.nextInt();

		/*
		 * Write SQL Trigger to change the status of request to Complete after
		 * the inspection date
		 */

		/*
		 * Write SQL Query to fetch the latest unpaid invoice and add the
		 * damageFees to already existing payment_due
		 */

	}

}
