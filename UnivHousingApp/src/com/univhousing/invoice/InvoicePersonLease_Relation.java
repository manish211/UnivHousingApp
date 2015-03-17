package com.univhousing.invoice;

import java.sql.Date;

public class InvoicePersonLease_Relation {

	private float monthlyHousingRent;
	private float monthlyPArkingRent;
	private float lateFees;
	private float incidentalCharges;
	private int invoiceNo;
	private Date paymentDate;
	private String paymentMethod;
	private int leaseNo;
	private String paymentStatus;
	private float paymentDue;
	private float damageCharges;
	private int personId;

	public float getMonthlyHousingRent() {
		return monthlyHousingRent;
	}
	public void setMonthlyHousingRent(float monthlyHousingRent) {
		this.monthlyHousingRent = monthlyHousingRent;
	}
	public float getMonthlyPArkingRent() {
		return monthlyPArkingRent;
	}
	public void setMonthlyPArkingRent(float monthlyPArkingRent) {
		this.monthlyPArkingRent = monthlyPArkingRent;
	}
	public float getLateFees() {
		return lateFees;
	}
	public void setLateFees(float lateFees) {
		this.lateFees = lateFees;
	}
	public float getIncidentalCharges() {
		return incidentalCharges;
	}
	public void setIncidentalCharges(float incidentalCharges) {
		this.incidentalCharges = incidentalCharges;
	}
	public int getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(int invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public int getLeaseNo() {
		return leaseNo;
	}
	public void setLeaseNo(int leaseNo) {
		this.leaseNo = leaseNo;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public float getPaymentDue() {
		return paymentDue;
	}
	public void setPaymentDue(float paymentDue) {
		this.paymentDue = paymentDue;
	}
	public float getDamageCharges() {
		return damageCharges;
	}
	public void setDamageCharges(float damageCharges) {
		this.damageCharges = damageCharges;
	}
	public int getPersonId() {
		return personId;
	}
	public void setPersonId(int personId) {
		this.personId = personId;
	}
	
	

}
