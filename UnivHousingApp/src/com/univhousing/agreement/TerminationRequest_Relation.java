package com.univhousing.agreement;

import java.sql.Date;

public class TerminationRequest_Relation {
	
	public String reason;
	public int terminationRequestNo;
	public String status;
	public Date terminationDate;
	public Date inspectionDate;
	public int personId;
	public int staffNo;

	/**
	 * @param personId
	 * @action Generates a new Termination request for a person id and Delete the related data entries from respective tables
	 */
	public void generateLeaseTerminationRequest(int personId) {

		/*Write SQL Query to generate a termination request, delete data from the tables that relates to person id */
	}
}
