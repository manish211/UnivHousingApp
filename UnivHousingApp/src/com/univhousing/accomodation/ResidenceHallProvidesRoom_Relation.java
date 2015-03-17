package com.univhousing.accomodation;

public class ResidenceHallProvidesRoom_Relation {
	
	private int residencePlaceNo;
	private int accomodationId;
	private int hallNo;
	private int roomNo;
	private float monthlyRent;

	public int getResidencePlaceNo() {
		return residencePlaceNo;
	}
	public void setResidencePlaceNo(int residencePlaceNo) {
		this.residencePlaceNo = residencePlaceNo;
	}
	public int getAccomodationId() {
		return accomodationId;
	}
	public void setAccomodationId(int accomodationId) {
		this.accomodationId = accomodationId;
	}
	public int getHallNo() {
		return hallNo;
	}
	public void setHallNo(int hallNo) {
		this.hallNo = hallNo;
	}
	public int getRoomNo() {
		return roomNo;
	}
	public void setRoomNo(int roomNo) {
		this.roomNo = roomNo;
	}
	public float getMonthlyRent() {
		return monthlyRent;
	}
	public void setMonthlyRent(float monthlyRent) {
		this.monthlyRent = monthlyRent;
	}
	
}
