package com.univhousing.parking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import com.univhousing.main.ConnectionUtils;
import com.univhousing.main.Constants;
import com.univhousing.users.Guest;
import com.univhousing.users.Student;

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
		
		String handicappedInfo ="";
		
		while(!handicappedInfo.equals("Y") && !handicappedInfo.equals("N"))
		{
			System.out.println("Handicapped? Y/N");
			handicappedInfo = inputObj.next();
		}
		
		if(handicappedInfo.equals("N"))
		{
			boolean inputInvalid = true;
			
			while(inputInvalid)
			{
				System.out.println("Please enter your vehicle type:\n" +
						"Bike, Compact Cars, Standard Cars, Large Cars");
				vehicleType = inputObj.next();
//				vehicleType = vehicleType.replaceAll("[\n\r]", "");
				System.out.println("vehicleType:"+vehicleType.toLowerCase());
				
				if(vehicleType.toLowerCase().equals(Constants.BIKE))
				{
					vehicleType = Constants.BIKE;
					inputInvalid = false;
					System.out.println("here");
				}					
				else if(vehicleType.toLowerCase().equals(Constants.COMPACT_CARS))
				{
					vehicleType = Constants.COMPACT_CARS;
					inputInvalid = false;
				}
				else if(vehicleType.toLowerCase().equals(Constants.STANDARD_CARS))
				{
					vehicleType = Constants.STANDARD_CARS;
					inputInvalid = false;
				}
				else if(vehicleType.toLowerCase().equals(Constants.LARGE_CARS))
				{
					vehicleType = Constants.LARGE_CARS;
					inputInvalid = false;
				}
				else
					System.out.println("You entered invalid input.Try again as below");;
			}
			
			
		}
		
		System.out.println("Do you want a nearby spot? Y/N");
		String nearbySpot = inputObj.next();  //By default we are giving the nearby spot . that's why this variable not used so far
		
		System.out.println("1. Submit");
		System.out.println("2. Back");
		int flag = inputObj.nextInt();
		
		if(flag == 1)
		{			
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
				where p2.vehicle_type = 'handicapped'
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
					selectQuery = selectQuery + " where p2.vehicle_type = ? and p1.availability = ? and p1.lot_no = plrh.lot_no and plrh.hall_number = rh.hall_number" ;
					selectQuery = selectQuery + " order by abs(rh.zip_code - pl.zip_code) asc) where rownum<2" ;
							
					preparedStatement = dbConnection.prepareStatement(selectQuery);
					
					String handicappedField ;
					
					preparedStatement.setString(2,Constants.AVAILABLE);
					
					if(handicappedInfo.equals("Y"))
						preparedStatement.setString(1,Constants.HANDICAPPED);
					else
						preparedStatement.setString(1,vehicleType);
						System.out.println("Marker1");
						
					rs = preparedStatement.executeQuery();
					System.out.println("Marker2");
					//If record exists , rs.next() will evaluate to true
					System.out.println("selectQuery:="+selectQuery);
					System.out.println("preparedStatement:="+preparedStatement.toString());
					if(rs.isBeforeFirst())
						{
						System.out.println("Marker3");
							rs.next();
							String insertQuery = "INSERT INTO studentparkingspot_relation values(?,?,?,?,ParkingRequest_seq.nextval)";
							preparedStatement = dbConnection.prepareStatement(insertQuery);
							preparedStatement.setInt(1,rs.getInt("lot_no"));
							preparedStatement.setInt(2,rs.getInt("spot_no"));
							preparedStatement.setInt(3,studentObj.studentId);
							preparedStatement.setString(4,"PENDING");
							
							int numOfRowsInserted = preparedStatement.executeUpdate();
							
							System.out.println("Marker3");
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
				System.out.println("No Available Parking Lot Found for this person");
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
			boolean isPersonGuest = guestObj.checkPersonIsGuest(personId);
			
			if(!isPersonGuest)
			{
				System.out.println("Person is neither a guest nor a student who has an accomodation");
				return;
			}
			
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
		{
			System.out.println("This spot number does not belong to this person. Please check.");
			return;
		}
		
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
			
			System.out.println("personId:"+personId);
			
			System.out.println("isBeforeFirst: "+rs.isBeforeFirst());

			//If record exists , rs.next() will evaluate to true
			if(!rs.isBeforeFirst())
			{
				System.out.println("This person does not have any parking permit. Please generate a new permit by using appropriate menu");
				rs.close();
				preparedStatement.close();
				return;
			}
			
			rs.next();
			
			System.out.println("Marker++++++++++++++++==");
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
			
			String updateSql2 = "update parkingSpot_belongs_parkinglot set permit_id = ? where permit_id = ? and spot_no = ? ";
			
			preparedStatement = dbConnection.prepareStatement(updateSql2);
			
			preparedStatement.setInt(1,newPermitId);
			preparedStatement.setInt(2,oldPermitId);
			preparedStatement.setInt(3,spotNumber);
			
			preparedStatement.executeUpdate();
			
			//Update query to update existing record in person_accomodation_lease with new permit id
			
			String updateSql1 = "Update person_accomodation_lease set permit_id = ? where person_id = ? ";
			
			preparedStatement = dbConnection.prepareStatement(updateSql1);
			
			preparedStatement.setInt(1,newPermitId);
			preparedStatement.setInt(2,personId);
			
			preparedStatement.executeUpdate();
			
			System.out.println("Parking renewed for this person. New Permit Id is = "+newPermitId);
			
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
			System.out.println("The spot does not belong to this person. Please check");
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
					System.out.print(rs.getString("request_status")+"\t\t");
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
					rs.next();
					
					if(rs.getInt("total") == 0)
					{
						isSpotValid = false;
					}
					else
					{
						System.out.println("After execute!! MARKER checkSpotValidity");
						isSpotValid = true;
						System.out.println("IT IS TRUE:checkSpotValidity ");
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
		
		return isSpotValid;
	}

	/**
	 * @param ArrayList<Integer> adminLevelParkingRequests
	 * @throws SQLException 
	 */
	public void getAllParkingRequests(ArrayList<Integer> parkingTicketsList) throws SQLException 
	{
		int requestNumber = 0, permitId = 0, studentId = 0, spotNumber = 0;
		/*Write SQL Query to fetch all the Parking requests with status Pending*/
		PreparedStatement ps1 = null;
		Connection conn1 = ConnectionUtils.getConnection();
		ResultSet allRequests = null;
		String query = "SELECT request_no FROM StudentParkingSpot_Relation WHERE request_status = ?";
		try
		{
			ps1 = conn1.prepareStatement(query);
			ps1.setString(1, Constants.PENDING_STATUS);
			allRequests = ps1.executeQuery();
			parkingTicketsList.clear();
			while(allRequests.next())
			{
				parkingTicketsList.add(allRequests.getInt("request_no"));
			}
			
			ConnectionUtils.closeConnection(conn1);

			System.out.println("Displaying all the parking requests made:");
			for (int i = 0; i < parkingTicketsList.size(); i++) 
			{
				System.out.println((i+1)+". "+parkingTicketsList.get(i));
			}
			System.out.println("Please select the parking request:");
			int requestSelected = inputObj.nextInt();
			requestNumber = parkingTicketsList.get(requestSelected-1);
			System.out.println("Selected request number: "+requestNumber);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		// Consuming the new line 
		inputObj.nextLine();
		/*Write a SQL Query to fetch all details of the ticket number*/
		System.out.println("Do you want to approve request: Y/N");
		String requestApprovalStatus = inputObj.nextLine();
		
		/* Note at this point of time, a request for parking has been generated only after
		 * it has been validated that student is indeed enrolled in university housing.
		 * The code to check this is present in this method: 
		 * public boolean checkStudentInUnivHosuing(int studentId) in Student.java
		 * 
		 * 
		 * NOTE: But the document states if all the criteria is verified then approve, so we have to 
		 * look what other criteria are*/
		
		try
		{
			if(requestApprovalStatus.equalsIgnoreCase("Y") || requestApprovalStatus.equalsIgnoreCase("Yes"))
			{
				/*Write SQL Query to change status of request to approved and 
				 * also generate a permit_id which will be updated in atleast two tables*/
				
				PreparedStatement ps3 = null;
				Connection conn3 = ConnectionUtils.getConnection();
				String query3 = "UPDATE StudentParkingSpot_Relation SET request_Status = ? WHERE request_no = ?";
				ps3 = conn3.prepareStatement(query3);
				ps3.setString(1, Constants.APPROVED_STATUS);
				ps3.setInt(2, requestNumber);
				ps3.executeUpdate();
				
				
				PreparedStatement ps2 = null;
				Connection conn2 = ConnectionUtils.getConnection();
				String query2 = "SELECT MAX(permit_id) AS MAX_PERMIT_ID FROM person_accomodation_lease";
				ps2 = conn2.prepareStatement(query2);
				ResultSet maxPermit = ps2.executeQuery();
				
				while(maxPermit.next())
				{
					permitId = maxPermit.getInt("MAX_PERMIT_ID");
					break;
				}
				ConnectionUtils.closeConnection(conn2);
				
				 
				 /*Write SQL Query to update permit_id in parkingSpot_belongs_parkingLot and person_accomodation_lease*/
				 // Get the studentId for this request_no
				studentId = getStudentIdFromParkingRequest(requestNumber);
				
				// Get the personId
				int personId = studentObj.getPersonIdForStudentId(studentId);
				
				 // First we need to get a spot_no that has to be assigned to student based on his nearby location if possible
				spotNumber = getASpotForParkingRequest(personId, requestNumber, studentId, false, permitId);
				// If spotNumber is -1, that means we did not get a spot near the student's residence and will look for spot anywhere that's available
				if(spotNumber == -1)
				{
					System.out.println("No Spot available near your residence, finding another spot");
					spotNumber = getASpotForParkingRequest(personId, requestNumber, studentId, true, permitId);
				}
			}
			else if(requestApprovalStatus.equalsIgnoreCase("N") || requestApprovalStatus.equalsIgnoreCase("No"))
			{
				/*Write SQL Query to change status of request to denied*/
				System.out.println("Your Parking request has been denied");
				PreparedStatement ps3 = null;
				Connection conn3 = ConnectionUtils.getConnection();
				String query3 = "UPDATE StudentParkingSpot_Relation SET request_Status = ? WHERE request_no = ?";
				ps3 = conn3.prepareStatement(query3);
				ps3.setString(1, Constants.REJECTED_STATUS);
				ps3.setInt(2, requestNumber);
				ps3.executeUpdate();
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	private int getASpotForParkingRequest(int personId, int requestNumber, int studentId, boolean lookAnyWhere, int maxPermitId) {
		
		int spotNumber = -1, accomodation_id = 0;
		String accomodation_type = "";
		
		System.out.println("Student id: "+studentId);
		System.out.println("PersonId: "+personId);
		try
		{
			// Get the accommodation id and accommodation type of the person
			PreparedStatement ps4 = null;
			Connection conn4 = ConnectionUtils.getConnection();
			String query4 = "SELECT L.accomodation_id, L.accomodation_type FROM person_accomodation_lease L " +
					"WHERE L.lease_move_in_date = (SELECT MAX(L2.lease_move_in_date) FROM person_accomodation_lease L2 WHERE L2.person_id = ?" +
					" AND L2.person_id = L.person_id)";
			ps4 = conn4.prepareStatement(query4);
			ps4.setInt(1, personId);
			ResultSet getAccInfo = ps4.executeQuery();
			
			while(getAccInfo.next())
			{
				accomodation_id = getAccInfo.getInt("accomodation_id");
				accomodation_type = getAccInfo.getString("accomodation_type");
				System.out.println("Accomodation id: "+ accomodation_id+ " and accomodation type: "+accomodation_type);
			}
			
			String tableToQuery = "";
			if(accomodation_type.equalsIgnoreCase(Constants.GENERAL_APARTMENT))
				tableToQuery = Constants.TABLE_GENERAL_APARTMENT;
			else if(accomodation_type.equalsIgnoreCase(Constants.RESIDENCE_HALL))
				tableToQuery = Constants.TABLE_RESIDENCE_HALL;
			else if(accomodation_type.equalsIgnoreCase(Constants.FAMILY_APARTMENT))
				tableToQuery = Constants.TABLE_FAMILY_APARTMENT;
			
			PreparedStatement ps5 = null;
			Connection conn5 = ConnectionUtils.getConnection();
			String query5 = "SELECT P.spot_no FROM parkingSpot_belongs_parkingLot P WHERE P.availability = ? " +
					"AND P.zip_code = (SELECT zip_code FROM "+tableToQuery+"  WHERE accomodation_id = ?)";

			String query6 = "SELECT P.spot_no FROM parkingSpot_belongs_parkingLot P WHERE P.availability = ? ";

			// If the lookAnyWhere flag is false, this means that we have to find parking spot near to student's residence else not
			if(lookAnyWhere)
			{
				ps5 = conn5.prepareStatement(query6);
				ps5.setString(1, Constants.AVAILABLE);
			}
			else
			{
				if(tableToQuery.equalsIgnoreCase(Constants.TABLE_RESIDENCE_HALL))
				{
					String query7 = "SELECT P.spot_no FROM parkingSpot_belongs_parkingLot P WHERE P.availability = ? " +
					"AND P.zip_code = (SELECT zip_code FROM "+tableToQuery+"  WHERE hall_number = ?)";
					ps5 = conn5.prepareStatement(query7);
					ps5.setString(1, Constants.AVAILABLE);
					ps5.setInt(2, accomodation_id);
				}
				else
				{
					ps5 = conn5.prepareStatement(query5);
					ps5.setString(1, Constants.AVAILABLE);
					ps5.setInt(2, accomodation_id);
				}
			}
			
			
			ResultSet getSpot = ps5.executeQuery();
			
			while(getSpot.next())
			{
				spotNumber = getSpot.getInt("spot_no");
				break;
			}
			
			
			// This condition checks if there was no parking spot even available far from the student's residence
			if(spotNumber == -1)
			{
				System.out.println(Constants.PARKING_SPOTS_FULL);
				PreparedStatement ps10 = null;
				Connection conn10 = ConnectionUtils.getConnection();
				String query10 = "UPDATE StudentParkingSpot_Relation SET request_Status = ? WHERE request_no = ?";
				ps10 = conn10.prepareStatement(query10);
				ps10.setString(1, Constants.REJECTED_STATUS);
				ps10.setInt(2, requestNumber);
				ps10.executeUpdate();
			}
			else
			{
				// This is done so that we get the new permitId and not do permitId++ every time for each insert/update query
				int newPermitId = ++maxPermitId;

				System.out.println("Congratulations you have got a parking spot and your permit id is: "+newPermitId);
				// We will now update person_accomodation_lease table once the spot is found
				PreparedStatement ps8 = null;
				Connection conn8 = ConnectionUtils.getConnection();
				String query8 = "UPDATE person_accomodation_lease SET permit_id = ? WHERE person_id = ?";
				ps8 = conn8.prepareStatement(query8);
				ps8.setInt(1, newPermitId);
				ps8.setInt(2, personId);
				ps8.executeUpdate();
				System.out.println("Updated person_accomodation_lease table");
				
				// We will now update person_accomodation_lease table once the spot is found
				PreparedStatement ps9 = null;
				Connection conn9 = ConnectionUtils.getConnection();
				String query9 = "UPDATE parkingSpot_belongs_parkingLot SET permit_id = ?, availability = ? WHERE spot_no = ?";
				ps9 = conn9.prepareStatement(query9);
				ps9.setInt(1, newPermitId);
				ps9.setString(2, Constants.NOT_AVAILABLE);
				ps9.setInt(3, spotNumber);
				ps9.executeUpdate();
				System.out.println("Updated parkingSpot_belongs_parkingLot table");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		
		return spotNumber;
	}

	private int getStudentIdFromParkingRequest(int requestNumber) {
		int studentId = 0;
		try
		{
			PreparedStatement ps3 = null;
			Connection conn3 = ConnectionUtils.getConnection();
			String query3 = "SELECT student_id FROM StudentParkingSpot_Relation WHERE request_no = ?";
			ps3 = conn3.prepareStatement(query3);
			ps3.setInt(1, requestNumber);
			ResultSet getStudentID = ps3.executeQuery();
			
			while(getStudentID.next())
			{
				studentId = getStudentID.getInt("student_id");
				break;
			}
			System.out.println("Changing request status for student id : "+studentId);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return studentId;
	}
}
