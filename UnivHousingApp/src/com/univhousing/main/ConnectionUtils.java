package com.univhousing.main;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtils {
	
	private Connection connectionObj = null;
	
	public Connection getConnection(){
		return connectionObj;
	}
	
	public void closeConnection(Connection obj) throws SQLException
	{
		obj.close();
	}

}
