package com.univhousing.agreement;

import java.sql.Date;

public class HousingStaffManagesLease_Relation {

	public int leaseNo;
	public int duration;
	public Date cutOffDate;
	public String modeOfPayment;
	public float depsoit;
	public Date date;
	public int staffNo;

	public int getLeaseNo() {
		return leaseNo;
	}
	public void setLeaseNo(int leaseNo) {
		this.leaseNo = leaseNo;
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
	public String getModeOfPayment() {
		return modeOfPayment;
	}
	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}
	public float getDepsoit() {
		return depsoit;
	}
	public void setDepsoit(float depsoit) {
		this.depsoit = depsoit;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getStaffNo() {
		return staffNo;
	}
	public void setStaffNo(int staffNo) {
		this.staffNo = staffNo;
	}
}
