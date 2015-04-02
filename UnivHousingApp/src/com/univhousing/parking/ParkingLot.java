package com.univhousing.parking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import com.univhousing.main.ConnectionUtils;
import com.univhousing.users.Student;
import com.univhousing.main.Constants;
import com.univhousing.users.Guest;

public class ParkingLot {
	
	public int lotNo;

	/**
	 * @param personId
	 * @action Creates a parking spot for a student Id if he is enrolled in University Housing
	 */
	Student studentObj = new Student();
	Scanner inputObj = new Scanner(System.in);
	Guest   guestObj = new Guest();

	public void generateAParkingSpot(int personId)
	{
		int studentId;
		boolean isStudentAccomodated = false;
		String vehicleType="";

		System.out.println("Showing Request New Parking Spot\n");
		System.out.println("Enter the following information\n");
		
		
		System.out.println("Handicapped? Y/N");
		String handicappedInfo = inputObj.next();
		
		if(handicappedInfo.equals("N"))
		{
			System.out.println("Please enter your vehicle type:\n" +
					"Bike, Compact Cars, Standard Cars, Large Cars");
			vehicleType = inputObj.next();
		}
		
		System.out.println("Do you want a nearby spot? Y/N");
		String nearbySpot = inputObj.next();
		
		System.out.println("1. Submit");
		System.out.println("2. Back");
		int flag = inputObj.nextInt();
		
		if(flag == 1)
		{			
			System.out.println("marker1");
			System.out.println("marker1");
			// Fetch the student Id for a particular Person Id
			studentObj.studentId = studentObj.getStudentIdForPersonId(personId);
			
			// Check if student is in University Housing
			isStudentAccomodated = studentObj.checkStudentInUnivHousing(personId);
			
			boolean isPersonGuest = guestObj.checkPersonIsGuest(personId);
			
			System.out.println("Person id: "+personId);
			
			System.out.println("isPersonGuest:"+isPersonGuest);
			
			if(isStudentAccomodated || isPersonGuest)
			{
				ResultSet createParkingSpot = null;
				/*Write SQL Query to use vehicleType, handicappedInfo, nearbySpot and 
				 * create a entry for the student Id by adding an entry in StudentParkingSpot_relation table
				 * and if approved then fill the permit ID in PSpotBelongsPLot_Realtion*/
				
				/*select spot_no,lot_no
				from(select p1.spot_no,plrh.hall_number,p1.lot_no,pl.zip_Code Parking_ZipCode,rh.zip_code as RESIDENCE_ZIPCODE,p1.availability
				from parkingSpot_belongs_parkingLot p1, parking_spot_has_class p2,parking_lot_residence_hall plrh,residence_hall rh,parking_lot pl
				where p2.type = 'handicapped'
				and p1.availability = 'YES'
				and p1.lot_no = plrh.lot_no
				and plrh.hall_number = rh.hall_number
				order by abs(rh.zip_code - pl.zip_code) asc)
				where rownum<2;*/
				
				//----------------------------------------------------------------------------------------------------------------------------
				ResultSet rs = null;
				Connection dbConnection = null;
				PreparedStatement preparedStatement = null ;
				
				try{
					dbConnection = ConnectionUtils.getConnection();
					
					String selectQuery = "select spot_no,lot_no from(select p1.spot_no,plrh.hall_number,p1.lot_no,pl.zip_Code Parking_ZipCode,rh.zip_code as RESIDENCE_ZIPCODE,p1.availability " ;
					selectQuery = selectQuery + " from parkingSpot_belongs_parkingLot p1, parking_spot_has_class p2,parking_lot_residence_hall plrh,residence_hall rh,parking_lot pl " ;
					selectQuery = selectQuery + " where p2.vehicle_type = ? and p1.availability = 'YES' and p1.lot_no = plrh.lot_no and plrh.hall_number = rh.hall_number" ;
					selectQuery = selectQuery + " order by abs(rh.zip_code - pl.zip_code) asc) where rownum<2" ;
							
					preparedStatement = dbConnection.prepareStatement(selectQuery);
					
					String handicappedField ;
					
					if(handicappedInfo.equals("Y"))
						preparedStatement.setString(1,"handicapped");
					else
						preparedStatement.setString(1,vehicleType);
						
					
					rs = preparedStatement.executeQuery();

					//If record exists , rs.next() will evaluate to true
					if(rs.isBeforeFirst())
						{
							rs.next();
							String insertQuery = "INSERT INTO studentparkingspot_relation values(?,?,?,?,ParkingRequest_seq.nextval)";
							preparedStatement = dbConnection.prepareStatement(insertQuery);
							preparedStatement.setInt(1,rs.getInt("lot_no"));
							preparedStatement.setInt(2,rs.getInt("spot_no"));
							preparedStatement.setInt(3,studentObj.studentId);
							preparedStatement.setString(4,"PENDING");
							
							int numOfRowsInserted = preparedStatement.executeUpdate();
							
							if(numOfRowsInserted == 0)
							{
								System.out.println("\n\n ==================================================================================================================\n");
								System.out.println("\n Insert Failed For Some Reason. Please Contact Administrator");
								System.out.println("==================================================================================================================\n");
							}
							else
							{
								System.out.println("\n\n ==================================================================================================================\n");
								System.out.println("\nOne record inserted into studentparkingspot_relation successfully\n");
								System.out.println("==================================================================================================================\n");
							}
						}
					
				}catch(SQLException e1){
					
					if(e1.getMessage().toLowerCase().contains("ORA-00001".toLowerCase()))
					{
						System.out.println("\n\n ==================================================================================================================\n");
						System.out.println("Request for this student-"+studentObj.studentId+" already exists in the database. Try updating the existing request");
						System.out.println("==================================================================================================================\n");
					}
					else
					{
						System.out.println("SQLException: "+ e1.getMessage());
						System.out.println("VendorError: "+ e1.getErrorCode());
					}
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
					//----------------------------------------------------------------------------------------------------------------------------
			}
			else
				System.out.println("Sorry only students in University Housing can request parking spot");
		}
		else
			return;
	}
	
	/**
	 * @action displays the information of Each parking lots i.e. 
	 * 1. How many spots are vacant in each parking lot
	 * 2. Which spots (if) allocated to a student
	 * 3. Availability of Parking Spots
	 */
	public void displayInfoForParkingLots(int personId)
	{
//		int studentId;
//		boolean isStudentAccomodated = false;
		// Fetch the student Id for a particular Person Id
//		studentId = studentObj.getStudentIdForPersonId(personId);
		
		// Check if student is in University Housing
//		isStudentAccomodated = studentObj.checkStudentInUnivHousing(studentId);
		
		/*Write an SQL query to show:
		 * 1. How many spots are vacant in each parking lot
		 * 2. Which spots (if) allocated to a student
		 * 3. Availability of Parking Spots*/
		
//		select lot_no,count(spot_no)
//		from parkingSpot_belongs_parkingLot
//		where availability = 'YES'
//		group by lot_no;
		
		ResultSet rs = null;
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null ;
		
		try{
			dbConnection = ConnectionUtils.getConnection();
			
			String selectQuery = "select lot_no,count(spot_no) as total " ;
			selectQuery = selectQuery + " from parkingSpot_belongs_parkingLot " ;
			selectQuery = selectQuery + " where availability = ?" ;
			selectQuery = selectQuery + " group by lot_no" ;
					
			preparedStatement = dbConnection.prepareStatement(selectQuery);
			
			preparedStatement.setString(1,"YES");
			
			rs = preparedStatement.executeQuery();

			//If record exists , rs.next() will evaluate to true
			if(!rs.isBeforeFirst())
			{
				System.out.println("No Parking Lot Information Found. Contact Administrator");
			}
			else
			{
				System.out.println("\n\n ==================================================================================================================");
				System.out.println("LOT_NO"+"\t\t"+"SPOT_NO");
				System.out.println(" ==================================================================================================================\n");
				
				while(rs.next())
				{
					System.out.print(rs.getInt("lot_no")+"\t\t");
					System.out.println(rs.getInt("total")+"\t\t");
				}
				
				System.out.println(" ==================================================================================================================\n");
			}	
			
		}catch(SQLException e1){
			{
				System.out.println("SQLException: "+ e1.getMessage());
				System.out.println("VendorError: "+ e1.getErrorCode());
			}
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
	}
	

	/**
	 * @param personId
	 * @action Finds the studentId for the personId and then gets the permit number for 
	 * that studentId and then displays the permit id for that studentID along with lot number
	 */
	public void viewCurrentParkingSpot(int personId)
	{
		int studentId;
		boolean isStudentAccomodated = false;
		// Fetch the student Id for a particular Person Id
//		studentId = studentObj.getStudentIdForPersonId(personId);
		
		// Check if student is in University Housing
		isStudentAccomodated = studentObj.checkStudentInUnivHousing(personId);
		
//		ResultSet parkingSpotInfo = null;
		/*Write SQL Query to show:
		 * Permit number, spot number and Lot number for a particular student id */
		
		if(!isStudentAccomodated)
		{
			System.out.println("Student is not in the university housing. Please check.");
			return;
		}
		
		/*select p1.permit_id,p2.spot_no,p2.lot_no
		from person_accomodation_lease p1,parkingSpot_belongs_parkinglot p2
		where p1.permit_id = p2.permit_id
		and p1.person_id =1001;*/
		
		ResultSet rs = null;
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null ;
		
		try{
			dbConnection = ConnectionUtils.getConnection();
			
			String selectQuery = "select p1.permit_id,p2.spot_no,p2.lot_no " ;
			selectQuery = selectQuery + " from person_accomodation_lease p1,parkingSpot_belongs_parkinglot p2 " ;
			selectQuery = selectQuery + " where p1.permit_id = p2.permit_id" ;
			selectQuery = selectQuery + " and p1.person_id =?" ;
					
			preparedStatement = dbConnection.prepareStatement(selectQuery);
			
			preparedStatement.setInt(1,personId);
			
			rs = preparedStatement.executeQuery();

			//If record exists , rs.next() will evaluate to true
			if(!rs.isBeforeFirst())
			{
				System.out.println("No Parking Lot Information Found. Contact Administrator");
			}
			else
			{
				System.out.println("\n\n ==================================================================================================================");
				System.out.println("PERMIT_ID"+"\t\t"+"SPOT_NO"+"\t\t"+"LOT_NO");
				System.out.println(" ==================================================================================================================\n");
				
				while(rs.next())
				{
					System.out.print(rs.getInt("permit_id")+"\t\t\t");
					System.out.print(rs.getInt("spot_no")+"\t\t");
					System.out.println(rs.getInt("lot_no")+"\t\t\t");
				}
				
				System.out.println(" ==================================================================================================================\n");
			}	
			
		}catch(SQLException e1){
			{
				System.out.println("SQLException: "+ e1.getMessage());
				System.out.println("VendorError: "+ e1.getErrorCode());
			}
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
		
		
		
	}

	/**
	 * @param personId
	 * @action Asks for a parking spot Id and then renews the parking spot ID for that Student Id
	 *  and updates the Permit number to a new value in the leases table and PspotBelongsPLot_Relation
	 */
	public void renewParkingSpot(int personId) 
	{
		int studentId;
		// Fetch the student Id for a particular Person Id
		studentId = studentObj.getStudentIdForPersonId(personId);
		
		System.out.println("Enter your Parking Spot Number:");
		int spotNumber = inputObj.nextInt();
		
		if(checkSpotValidity(spotNumber,personId) == false)
			return;
		
		System.out.println("Spot was valid ++++++");
		
		ResultSet renewSpot = null;
		/*Write query to renew the parking spot and then update the permit id in Person_accomodation_Lease table and 
		 *  PSpotBelongsPLot_Relation*/
		
		ResultSet rs = null;
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null ;
		int newPermitId;
		
		try{
			
			dbConnection = ConnectionUtils.getConnection();
			
			String selectQuery = "select permit_id from person_accomodation_lease where person_id = ?" ;
					
			preparedStatement = dbConnection.prepareStatement(selectQuery);
			
			preparedStatement.setInt(1,personId);
			
			rs = preparedStatement.executeQuery();

			//If record exists , rs.next() will evaluate to true
			if(!rs.isBeforeFirst())
			{
				System.out.println("This person does not have any parking permit. Please generate a new permit by using appropriate menu");
				rs.close();
				preparedStatement.close();
				return;
			}
			
			rs.next();
			int oldPermitId = rs.getInt("permit_id");
			
			//Get the max permit id and use it to generate new permit id
			String selectQueryMaxPermitId = "select max(permit_id) as max_permit_id from person_accomodation_lease " ;
					
			preparedStatement = dbConnection.prepareStatement(selectQueryMaxPermitId);
			
//			preparedStatement.setInt(1,personId);
			
			rs = preparedStatement.executeQuery();

			//If record exists , rs.next() will evaluate to true
			if(!rs.isBeforeFirst())
			{
				newPermitId = Constants.PERMIT_ID_START;
			}
			else
			{
				rs.next();
				newPermitId = rs.getInt("max_permit_id") + 1;
			}	
			
			//Update query to update existing record in parkingSpot_belongs_parkinglot with new permit id
			
			String updateSql2 = "update parkingSpot_belongs_parkinglot set permit_id = ? where permit_id = ? ";
			
			preparedStatement = dbConnection.prepareStatement(updateSql2);
			
			preparedStatement.setInt(1,newPermitId);
			preparedStatement.setInt(2,oldPermitId);
			
			preparedStatement.executeUpdate();
			
			//Update query to update existing record in person_accomodation_lease with new permit id
			
			String updateSql1 = "Update person_accomodation_lease set permit_id = ? where person_id = ? ";
			
			preparedStatement = dbConnection.prepareStatement(updateSql1);
			
			preparedStatement.setInt(1,newPermitId);
			preparedStatement.setInt(2,personId);
			
			preparedStatement.executeUpdate();
			
		}catch(SQLException e1){
			{
				System.out.println("SQLException: "+ e1.getMessage());
				System.out.println("VendorError: "+ e1.getErrorCode());
			}
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
	}

	/**
	 * @param personId
	 * @action Returns a parking spot back to a Parking lot and updates the PSpotBelongsPLot_Relation
	 * table's availability attribute for that spot number to vacant 
	 */
	public void returnParkingSpot(int personId) 
	{
		int studentId;
		// Fetch the student Id for a particular Person Id
		studentId = studentObj.getStudentIdForPersonId(personId);
		
		System.out.println("Enter your Parking Spot Number:");
		int spotNumber = inputObj.nextInt();
		
/*Update permit number to null in person_accomodation_lease_table
		
		update person_accomodation_lease
		set permit_id = null
		where person_id = ? 
				*/
		
		// Check if the student is actually the owner of this parking spot
		if(!checkSpotValidity(spotNumber,personId))
		{
			System.out.println("The spot does not belong to this student. Please check");
		}
		else
		{
			//Update  permit number
			
			ResultSet rsPermit = null;
			Connection dbConnectionPermit = null;
			PreparedStatement preparedStatementPermit = null ;
			
			try{
				dbConnectionPermit = ConnectionUtils.getConnection();
				
				String updatePermitQuery = "update person_accomodation_lease set permit_id = null" ;
				updatePermitQuery = updatePermitQuery + " where person_id = ? " ;
						
				preparedStatementPermit = dbConnectionPermit.prepareStatement(updatePermitQuery);
				
				preparedStatementPermit.setInt(1,personId);
				
				int rowsUpdatedPermit = preparedStatementPermit.executeUpdate();

				if(rowsUpdatedPermit == 0)
				{
					System.out.println("No rows updated. Please check the person id or contact Administrator");
					return;
				}
				else
				{
					System.out.println("Permit Id successfully udpated to null. Now updating the availability...");
					
					ResultSet rsAvail = null;
					Connection dbConnectionAvail = null;
					PreparedStatement preparedStatementAvail = null ;
					
					try{
						dbConnectionAvail = ConnectionUtils.getConnection();
						
						String updateAvailabilityQuery = "update parkingspot_belongs_parkinglot set availability = ? where spot_no = ? " ;
						
								
						preparedStatementAvail = dbConnectionAvail.prepareStatement(updateAvailabilityQuery);
						
						preparedStatementAvail.setString(1,"YES");
						preparedStatementAvail.setInt(2,spotNumber);
						
						int rowsUpdatedAvailability = preparedStatementAvail.executeUpdate();

						if(rowsUpdatedAvailability == 0)
							System.out.println("No rows updated in parkingspot_belongs_parkinglot. Please check the person id or contact Administrator"); //Pending - Update the message for the customer/end user
						else
							System.out.println("Availability set to YES for records in parkingspot_belongs_parkinglot table");
					}catch(SQLException e1){
							System.out.println("Error while updating the parkingspot_belongs_parkinglot table");
							System.out.println("SQLException: "+ e1.getMessage());
							System.out.println("VendorError: "+ e1.getErrorCode());
					}
					catch(Exception e3)
					{
						System.out.println("Error while updating the parkingspot_belongs_parkinglot table");
						System.out.println("General Exception Case. Printing stack trace below:\n");
						e3.printStackTrace();
					}
					finally{
							try {
							        preparedStatementAvail.close();
							        dbConnectionAvail.close();
						      	} catch (SQLException e) {
						        e.printStackTrace();
						      	}
					}
					
				}
			}catch(SQLException e1){
				System.out.println("Error while updating the permit id in person_accomodation_lease table");
				System.out.println("SQLException: "+ e1.getMessage());
				System.out.println("VendorError: "+ e1.getErrorCode());
		}
		catch(Exception e3)
		{
			System.out.println("Error while updating the permit id in person_accomodation_lease table");
			System.out.println("General Exception Case. Printing stack trace below:\n");
			e3.printStackTrace();
		}
		finally{
				try {
				        preparedStatementPermit.close();
				        dbConnectionPermit.close();
			      	} catch (SQLException e) {
			        e.printStackTrace();
			      	}
		}
			
		}
	}
	
	/**
	 * @param personId
	 * @action Retrieves the student id for the given person Id and gets the status of the request made by that student
	 */
	public void getRequestStatus(int personId) {

		int studentId;
		// Fetch the student Id for a particular Person Id
		studentId = studentObj.getStudentIdForPersonId(personId);
		
		ResultSet fetchRequestStatus = null;
		/*Write SQL query for fetching the status of Request for this student ID*/
		
		/*select request_status
		from studentParkingSpot_Relation s1,Student s2
		where s1.person_id = s2.person_id
		and s2.person_id = ? //can return multiple records . See the primary keys of both the tables.
*/				
		ResultSet rs = null;
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null ;
		
		try{
			dbConnection = ConnectionUtils.getConnection();
			
			String selectQuery = "select s1.student_id,s1.request_status,s1.lot_no,s1.spot_no from studentParkingSpot_Relation s1,Student s2" ;
			selectQuery = selectQuery + " where s1.student_id = s2.student_id and s2.person_id = ?" ;
					
			preparedStatement = dbConnection.prepareStatement(selectQuery);
			
			preparedStatement.setInt(1,personId);
			
			rs = preparedStatement.executeQuery();

			//If record exists , rs.next() will evaluate to true
			if(!rs.isBeforeFirst())
			{
				System.out.println("No Records were found for that student");
			}
			else
			{
				System.out.println("\n\n ==================================================================================================================");
				System.out.println("STUDENT_ID"+"\t\t"+"REQUEST_STATUS"+"\t\t"+"LOT_NO"+"\t\t"+"SPOT_NO");
				System.out.println(" ==================================================================================================================\n");
				
				while(rs.next())
				{
					System.out.print(rs.getInt("student_id")+"\t\t\t");
					System.out.print(rs.getString("request_status")+"\t\t\t");
					System.out.print(rs.getInt("lot_no")+"\t\t");
					System.out.println(rs.getInt("spot_no")+"\t\t\t");
				}
				
				System.out.println(" ==================================================================================================================\n");
			}	
			
		}catch(SQLException e1){
			{
				System.out.println("SQLException: "+ e1.getMessage());
				System.out.println("VendorError: "+ e1.getErrorCode());
			}
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
		
				
				
		
	}

	/**
	 * @param spotNumber
	 * @action This is a local method, which checks if a student is actually assigned to this spot number
	 */
	private boolean checkSpotValidity(int spotNumber,int personId) {
		
		boolean isSpotValid = false;
		ResultSet checkSpotValidity = null;
		/*Write SQL Query to check if SpotNumber entered by user is actually ssigned to him*/
		
		/*select count(*) from person_accomodation_lease p1,parkingspot_belongs_parkinglot p2
		where p1.person_id = 1001
		and p1.permit_id = p2.permit_id;*/
		
		ResultSet rs = null;
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		try {
			dbConnection = ConnectionUtils.getConnection();

			String selectQuery = "select count(*) as total from person_accomodation_lease p1,parkingspot_belongs_parkinglot p2";
			selectQuery = selectQuery + " where p1.person_id = ?";
			selectQuery = selectQuery + " and p1.permit_id = p2.permit_id";

			preparedStatement = dbConnection.prepareStatement(selectQuery);

			preparedStatement.setInt(1, personId);

			rs = preparedStatement.executeQuery();
			
			// If record exists , rs.next() will evaluate to true
			
			if (rs.isBeforeFirst()) 
			{
					System.out.println("After execute!! MARKER checkSpotValidity");
					isSpotValid = true;
					System.out.println("IT IS TRUE:checkSpotValidity ");
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
		
		return isSpotValid;
	}

	/**
	 * @param ArrayList<Integer> adminLevelParkingRequests
	 * @throws SQLException 
	 */
	public void getAllParkingRequests(ArrayList<Integer> parkingTicketsList) throws SQLException 
	{
		/*Write SQL Query to fetch all the Parking requests*/
		ResultSet allRequests = null;
		
		while(allRequests.next())
		{
			parkingTicketsList.add(allRequests.getInt("request_no"));
		}
		
		System.out.println("Displaying all the parking requests made:");
		for (int i = 0; i < parkingTicketsList.size(); i++) 
		{
			System.out.println((i+1)+". "+parkingTicketsList.get(i));
		}
		System.out.println("Please select the parking request:");
		int requestSelected = inputObj.nextInt();
		int requestNumber = parkingTicketsList.get(requestSelected-1);
		
		/*Write a SQL Query to fetch all details of the ticket number*/
		System.out.println("Do you want to approve request: Y/N");
		String requestApprovalStatus = inputObj.next();
		
		/* Note at this point of time, a request for parking has been generated only after
		 * it has been validated that student is indeed enrolled in university housing.
		 * The code to check this is present in this method: 
		 * public boolean checkStudentInUnivHosuing(int studentId) in Student.java
		 * 
		 * 
		 * NOTE: But the document states if all the criteria is verified then approve, so we have to 
		 * look what other criteria are*/
		
		if(requestApprovalStatus.equalsIgnoreCase("Y"))
		{
			/*Write SQL Query to change status of request to approved and 
			 * also generate a permit_id which will be updated in atleast two tables*/
		}
		else if(requestApprovalStatus.equalsIgnoreCase("N"))
		{
			/*Write SQL Query to change status of request to denied*/
		}
	}
}
