package hkust.cse.calendar.apptstorage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class LocationDB {
	public LocationDB()
	  {
		Connection c = null;
	    Statement stmt = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:location.db");
	      JOptionPane.showMessageDialog(null, "Opened database successfully");

//	      stmt = c.createStatement();
//	      String sql = "CREATE TABLE COMPANY " +
//	                   "(ID             INT PRIMARY KEY     NOT NULL," +
//	                   " LOCATION       TEXT                NOT NULL)"; 
//	      stmt.executeUpdate(sql);
//	      stmt.close();
	      c.close();
	    } catch ( Exception e ) {
	      JOptionPane.showMessageDialog(null, e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    JOptionPane.showMessageDialog(null, "Table created successfully");
	  }
}