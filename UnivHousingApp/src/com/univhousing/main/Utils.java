package com.univhousing.main;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Utils {

	public String convertToString(int number) {
		return String.valueOf(number);
	}
	
	public int convertToInteger(String value) {
		return Integer.parseInt(value);
	}
	
	public String changeUtilDateToString(java.util.Date utilDate)
	{
		DateFormat df = getDateFormat();
		return df.format(utilDate);
	}
	
	public String convertSQLDateToString(java.util.Date sqlDate)
	{
		DateFormat df = getDateFormat();
		return df.format(sqlDate);
	}
	
	public java.util.Date convertStringToUtilDateFormat(String date) throws ParseException
	{
		DateFormat df = getDateFormat();
		java.util.Date dateValue = df.parse(date);
		return dateValue;
	}
	
	public java.sql.Date convertStringToSQLDateFormat(String date) throws ParseException
	{
		java.sql.Date sqlDate = convertUtilDateToSQLDate(date);
		return sqlDate;
	}
	
	public java.sql.Date convertUtilDateToSQLDate(String date) throws ParseException
	{
		DateFormat df = getDateFormat();
		java.util.Date dateValue = df.parse(date);
		java.sql.Date sqlDate = new Date(dateValue.getTime()); 
		return sqlDate;
	}
	
	public DateFormat getDateFormat()
	{
		return new SimpleDateFormat("MM/dd/yyyy");
	}
	
	
	
}
