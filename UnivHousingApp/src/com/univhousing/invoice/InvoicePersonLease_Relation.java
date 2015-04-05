package com.univhousing.invoice;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Scanner;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;
import com.univhousing.main.ConnectionUtils;
import com.univhousing.main.Constants;

public class InvoicePersonLease_Relation {

	public float monthlyHousingRent;
	public float monthlyPArkingRent;
	public float lateFees;
	public float incidentalCharges;
	public int invoiceNo;
	public Date paymentDate;
	public String paymentMethod;
	public int leaseNo;
	public String paymentStatus;
	public float paymentDue;
	public float damageCharges;
	public int personId;
	Scanner inputObj = new Scanner(System.in);

	/***********************************************************************************************
	 * @param personId
	 * @throws SQLException 
	 * @action Displays list of invoices for a particular person_id, and stores the data from Result set into an ArrayList<Integer> 
	 * Once the ArrayList is populated we then print all the invoices for a person and ask him to choose to print one particular invoice
	 * Based on his input we get the invoice_no from the Array List and then use that to fetch details from that particular invoice_no
	 *  
	 *  @Tables 
	 ***********************************************************************************************/
	public void displayListOfInvociesForAPerson(int personId, ArrayList<Integer> invoiceNumbersList) throws SQLException {
		/*Write SQL query to fetch:
		 * Invoice numbers for a particular person using his person_id*/
		
		System.out.println("PERSON ID PASSED : "+personId);
					
		invoiceNumbersList.clear();
	
		ResultSet rs = null;
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null ;
		
		try{
			dbConnection = ConnectionUtils.getConnection();
			
			String selectQuery = "SELECT * FROM invoice_person_lease WHERE person_id = ? " ;
								
			preparedStatement = dbConnection.prepareStatement(selectQuery);
			
			preparedStatement.setInt(1,personId);
			
			System.out.println("SELECT QUERY IS: "+selectQuery);
			
			rs = preparedStatement.executeQuery();
			int count = 1;

			//If record exists , rs.next() will evaluate to true
			if(rs.isBeforeFirst())
			{
				System.out.println("==============================================================================================");
				System.out.println("INVOICE_NO");
				System.out.println("==============================================================================================");
				
			
			
			while(rs.next())
				{
					
					invoiceNumbersList.add(rs.getInt("invoice_no"));
				}
			
			
				for(Integer item : invoiceNumbersList)
				{
					System.out.println(count+". "+item.intValue());
					count++;
				}
			}
			else
			{
				System.out.println("No Former Invoice Found");
			}
			count = 0;
			System.out.println("\n0. Back");
			System.out.println("\n");
			int choice = inputObj.nextInt();
			if(choice == 0)
				return;
			else
			{
				displayInvoiceDetails(personId, invoiceNumbersList.get(choice-1));
			}
			System.out.println("==============================================================================================\n\n");
			
		}catch(SQLException e1){
			System.out.println("SQLException: "+ e1.getMessage());
			System.out.println("VendorError: "+ e1.getErrorCode());
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

	/***********************************************************************************************
	 * @param personId
	 * @param invoiceNumber
	 * @action Displays Lease number, Payment due, Due date, Student's Full name, Student Id, 
	 *  Place number, Room number, Name of Hall, Apartment, Date of Invoice Paid, Method of Payment
	 *  for a particular Invoice number 
	 **********************************************************************************************/
	public void displayInvoiceDetails(int personId, Integer invoiceNumber) {
		/*Write SQL Query to find above mentioned details in @action in method description
		 *  for the given invoiceNumber*/
		
		ResultSet rs = null;
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null ;
		
		try{
			dbConnection = ConnectionUtils.getConnection();
			
			String selectQuery = "SELECT * FROM invoice_person_lease outer_table WHERE person_id = ? AND invoice_no = ?" ;
			
			System.out.println("SELECT QUERY IS: "+selectQuery);
					
			preparedStatement = dbConnection.prepareStatement(selectQuery);
			
			preparedStatement.setInt(1,personId);
			preparedStatement.setInt(2,invoiceNumber);
			
			rs = preparedStatement.executeQuery();

			//If record exists , rs.next() will evaluate to true
			while(rs.next())
				{
					System.out.println("==============================================================================================");
					System.out.print("monthly_housing_rent\t\tmonthly_parking_rent\t\tlate_fees\t\tincidental_charges\t\tinvoice_no");
					System.out.println("\t\tpayment_date\t\tpayment_method\t\tlease_no\t\tpayment_status\t\tpayment_due\t\tdamage_charges\t\tperson_id") ;
					System.out.println("==============================================================================================");
					
					System.out.print(rs.getDouble("monthly_housing_rent")+"\t\t");
					System.out.print(rs.getDouble("monthly_parking_rent")+"\t\t");
					System.out.print(rs.getDouble("late_fees")+"\t\t");
					System.out.print(rs.getDouble("incidental_charges")+"\t\t");
					System.out.print(rs.getInt("invoice_no")+"\t\t");
					System.out.print(rs.getDate("payment_date")+"\t\t");
					System.out.print(rs.getString("payment_method")+"\t\t");
					System.out.print(rs.getInt("lease_no")+"\t\t");
					System.out.print(rs.getString("payment_status")+"\t\t");
					System.out.print(rs.getDouble("payment_due")+"\t\t");
					System.out.print(rs.getDouble("damage_charges")+"\t\t");
					System.out.print(rs.getInt("person_id")+"\t\t");
				}
			System.out.println("==============================================================================================\n\n");
			
		}catch(SQLException e1){
			System.out.println("SQLException: "+ e1.getMessage());
			System.out.println("VendorError: "+ e1.getErrorCode());
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

	/***********************************************************************************************
	 *  @param personId
	 *  @action Displays Lease number, Payment due, Due date, Student's Full name, Student Id, 
	 *  Place number, Room number, Name of Hall, Apartment, Date of Invoice Paid, Method of Payment
	 *  
	 *  @Tables 
	 **********************************************************************************************/ 
	public void displayCurrentInvoice(int personId) {

		/* Write SQL query to fetch:
		 * Lease number, Payment due, Due date, Student's Full name, Student Id, 
		 * Place number, Room number, Name of Hall, Apartment, Date of Invoice Paid, Method of Payment
		 * 
		 * Once the ResultSet is filled with data, depending upon how many ResultSets are used, we 
		 * can go ahead with the logic	
		 */	
				
		ResultSet rs = null;
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null ;
		
		try{
			dbConnection = ConnectionUtils.getConnection();
			
			String selectQuery = "SELECT * FROM invoice_person_lease outer_table WHERE person_id = ? AND " ;
			
			selectQuery = selectQuery + " payment_date = (select max(payment_date) from invoice_person_lease inner_table " ;
			selectQuery = selectQuery + " where outer_table.person_id = inner_table.person_id) and rownum<2" ;
			
			System.out.println("SELECT QUERY IS: "+selectQuery);
					
			preparedStatement = dbConnection.prepareStatement(selectQuery);
			
			preparedStatement.setInt(1,personId);
			
			rs = preparedStatement.executeQuery();

			//If record exists , rs.next() will evaluate to true
			if(rs.isBeforeFirst())
				{
					rs.next();
					System.out.println("==============================================================================================");
					System.out.print("monthly_housing_rent\t\tmonthly_parking_rent\t\tlate_fees\t\tincidental_charges\t\tinvoice_no");
					System.out.println("\t\tpayment_date\t\tpayment_method\t\tlease_no\t\tpayment_status\t\tpayment_due\t\tdamage_charges\t\tperson_id") ;
					System.out.println("==============================================================================================");
					
					System.out.print(rs.getDouble("monthly_housing_rent")+"\t\t");
					System.out.print(rs.getDouble("monthly_parking_rent")+"\t\t");
					System.out.print(rs.getDouble("late_fees")+"\t\t");
					System.out.print(rs.getDouble("incidental_charges")+"\t\t");
					System.out.print(rs.getInt("invoice_no")+"\t\t");
					System.out.print(rs.getDate("payment_date")+"\t\t");
					System.out.print(rs.getString("payment_method")+"\t\t");
					System.out.print(rs.getInt("lease_no")+"\t\t");
					System.out.print(rs.getString("payment_status")+"\t\t");
					System.out.print(rs.getDouble("payment_due")+"\t\t");
					System.out.print(rs.getDouble("damage_charges")+"\t\t");
					System.out.print(rs.getInt("person_id")+"\t\t");
					
					System.out.println("==============================================================================================\n\n");
				}
			else
			{
				System.out.println("No invoice found.");
			}
			
		}catch(SQLException e1){
			System.out.println("SQLException: "+ e1.getMessage());
			System.out.println("VendorError: "+ e1.getErrorCode());
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
	 * @param invoiceGenerationDate
	 * @action This method will take a date and generate invoices for people whose invoices have to be generated on that date
	 */
	public void generateLeasesForGivenDate() 
	{
		int personId;
		java.sql.Date moveInDate;
		String meansOfPayment;
		String accomodationType;
		int accomodationId = 0;
		int dateIncrementValue = 0;
		final int counter = 30;
		String duration = "" ;
		int result = 0;
		String paymentGateway = "";
		
		PreparedStatement ps = null;
		Connection conn = ConnectionUtils.getConnection();
		ResultSet getRequestData = null;
		String query = "select table1.payment_gateway,table1.duration,table1.person_id,table1.lease_move_in_date,table1.mode_of_payment,table1.accomodation_type " +
				"from person_acc_staff table1,(select person_id,max(lease_move_in_date) max_lease_move_in_date from person_acc_staff inner_table" +
				" where request_status = ? group by person_id) table2 where table1.person_id = table2.person_id " +
				"and table1.lease_move_in_date = table2.max_lease_move_in_date";
		try
		{
			ps = conn.prepareStatement(query);
			ps.setString(1, Constants.PROCESSED_STATUS);
			getRequestData = ps.executeQuery();
			
			while(getRequestData.next())
			{
				personId = getRequestData.getInt("person_id");
				moveInDate = getRequestData.getDate("lease_move_in_date");
				meansOfPayment = getRequestData.getString("mode_of_payment");
				accomodationType = getRequestData.getString("accomodation_type");
				duration = getRequestData.getString("duration");
				paymentGateway = getRequestData.getString("payment_gateway");

				PreparedStatement ps1 = null;
				Connection conn1 = ConnectionUtils.getConnection();
				ResultSet getAccId = null;
				String query1 = "SELECT accomodation_id FROM person_accomodation_lease WHERE person_id = ?";
				ps1 = conn1.prepareStatement(query1);
				ps1.setInt(1, personId);
				getAccId = ps1.executeQuery();
			
				while(getAccId.next())
				{
					accomodationId = getAccId.getInt("accomodation_id");
					break;
				}
				int durationOfStay = Integer.parseInt(duration);
				durationOfStay = durationOfStay*Constants.MONTHS_IN_SEMESTER;
				for(int i = 0; i< durationOfStay; i++)
				{
					dateIncrementValue = i*counter + counter;
					System.out.println("Date incremented by : "+dateIncrementValue);
					// Calling the PL/SQL Statement
					//create_invoice(v_person_id,v_accomodation_id,v_increment,v_mode_payment,v_accomodation_type,v_output);
					CallableStatement cst = conn.prepareCall("{call create_invoice (?,?,?,?,?,?,?)}");
					cst.setInt(1, personId);
					cst.setInt(2, accomodationId);
					cst.setInt(3, dateIncrementValue);
					cst.setString(4, paymentGateway);
					cst.setString(5, accomodationType);
					cst.setInt(6, durationOfStay);
					// Now registering out parameter
					cst.registerOutParameter(7, Types.INTEGER,result);
					cst.execute();
					cst.close();
					ps1.close();
					getAccId.close();
					ConnectionUtils.closeConnection(conn1);
				}
				// Resetting dateIncrementValue to zero
				System.out.println("------------------");
				dateIncrementValue = 0;
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * @param personId
	 * @action This method will take be invoked when the person wants to pay all his receipts
	 * @assumption There is no more than one entry for a person in person_accomodation_lease i.e. one person is not active on two
	 * leases simultaneously.
	 */
	public void changeAllInvoicesToPaid(int personId)
	{
		PreparedStatement ps = null;
		Connection conn = ConnectionUtils.getConnection();
		String query = "Update invoice_person_lease set payment_status = ?, payment_due = 0 WHERE invoice_no = " +
				"(select invoice_no from invoice_person_lease where payment_due > 0 " +
				"and person_id = ? and lease_no = (select lease_no from person_accomodation_lease where person_id = ?))";
		try
		{
			ps = conn.prepareStatement(query);
			ps.setString(1, Constants.PAID_INVOICE);
			ps.setInt(2, personId);
			ps.setInt(3, personId);
			int result = ps.executeUpdate();
			if(result>0)
				System.out.println("Your invoices have been paid,Bravo now you can re sign another lease too!!");
			else
				System.out.println("No invoices were paid for, check if you have pending invoices with admin");
			ps.close();
			ConnectionUtils.closeConnection(conn);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
}