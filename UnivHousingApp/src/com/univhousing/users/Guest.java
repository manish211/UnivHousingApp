package com.univhousing.users;

import java.sql.ResultSet;

public class Guest {
	
	public int approvalId;
	public int personId;

	/**
	 * @param approvalId
	 * @return PersonId for a given Approval Id
	 */
	public int getPersonIdFromApprovalId(int approvalId) {
		/*Write SQL Query to get PersonId for the given Approval Id*/
		int personId = 0;
		ResultSet getPersonId = null;

		return personId;
	}


}
