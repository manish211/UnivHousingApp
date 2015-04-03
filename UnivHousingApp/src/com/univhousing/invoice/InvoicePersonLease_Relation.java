package com.univhousing.invoice;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import com.univhousing.main.ConnectionUtils;

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
}




































