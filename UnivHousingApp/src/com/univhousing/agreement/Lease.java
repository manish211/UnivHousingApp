package com.univhousing.agreement;

import java.sql.Date;

public class Lease {

	public int leaseNo;
	public float deposit;
	public String modeOfPayment;
	public int duration;
	public Date cutOffDate;

	public int getLeaseNo() {
		return leaseNo;
	}
	public void setLeaseNo(int leaseNo) {
		this.leaseNo = leaseNo;
	}
	public float getDeposit() {
		return deposit;
	}
	public void setDeposit(float deposit) {
		this.deposit = deposit;
	}
	public String getModeOfPayment() {
		return modeOfPayment;
	}
	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public Date getCutOffDate() {
		return cutOffDate;
	}
	public void setCutOffDate(Date cutOffDate) {
		this.cutOffDate = cutOffDate;
	}
	
	
}
