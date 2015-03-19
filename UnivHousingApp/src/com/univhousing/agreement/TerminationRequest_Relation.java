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

	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public int getTerminationRequestNo() {
		return terminationRequestNo;
	}
	public void setTerminationRequestNo(int terminationRequestNo) {
		this.terminationRequestNo = terminationRequestNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getTerminationDate() {
		return terminationDate;
	}
	public void setTerminationDate(Date terminationDate) {
		this.terminationDate = terminationDate;
	}
	public Date getInspectionDate() {
		return inspectionDate;
	}
	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}
	public int getPersonId() {
		return personId;
	}
	public void setPersonId(int personId) {
		this.personId = personId;
	}
	public int getStaffNo() {
		return staffNo;
	}
	public void setStaffNo(int staffNo) {
		this.staffNo = staffNo;
	}
}
