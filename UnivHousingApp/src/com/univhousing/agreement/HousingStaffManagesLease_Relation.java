package com.univhousing.agreement;

import java.sql.Date;

public class HousingStaffManagesLease_Relation {

	private int leaseNo;
	private int duration;
	private Date cutOffDate;
	private String modeOfPayment;
	private float depsoit;
	private Date date;
	private int staffNo;

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
