package com.univhousing.accomodation;

public class FamilyApartment {

	public int apartmentNo;
	public String apartmentType;
	public int accomodationId;
	public String streetName;
	public String city;
	public int postcode;
	public float monthlyRent;

	public int getApartmentNo() {
		return apartmentNo;
	}
	public void setApartmentNo(int apartmentNo) {
		this.apartmentNo = apartmentNo;
	}
	public String getApartmentType() {
		return apartmentType;
	}
	public void setApartmentType(String apartmentType) {
		this.apartmentType = apartmentType;
	}
	public int getAccomodationId() {
		return accomodationId;
	}
	public void setAccomodationId(int accomodationId) {
		this.accomodationId = accomodationId;
	}
	public String getStreetName() {
		return streetName;
	}
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getPostcode() {
		return postcode;
	}
	public void setPostcode(int postcode) {
		this.postcode = postcode;
	}
	public void setMonthlyRent(float monthlyRent) {
		this.monthlyRent = monthlyRent;
	}
	public float getMonthlyRent() {
		return monthlyRent;
	}
	

}
