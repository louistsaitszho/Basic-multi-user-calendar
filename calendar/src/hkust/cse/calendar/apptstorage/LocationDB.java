package hkust.cse.calendar.apptstorage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class LocationDB {

	private Connection c = null;
	private Statement stmt = null;
	private String sql = null;
	private static LocationDB instance;

	public static LocationDB getInstance() {
	      if(instance == null) {
	         instance = new LocationDB();
	      }
	      return instance;
	   }
	
	//TODO can change to singleton
	private LocationDB() {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:location.db");
			stmt = c.createStatement();
			sql = "CREATE TABLE IF NOT EXISTS LOCATIONTABLE "
					+ "(ID   INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
					+ " LOCATION       TEXT                NOT NULL,"
					+ "CAPACITY INTEGER NOT NULL)";
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": "
					+ e.getMessage());
			System.exit(0);
		}
	}

	public boolean checkIfExists(String l) {
		try {
			ArrayList<Integer> idAL = new ArrayList<Integer>();
			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM LOCATIONTABLE WHERE LOCATION='"
							+ l + "';");
			while (rs.next()) {
				int ans = rs.getInt("ID");
				idAL.add(ans);
			}
			if (idAL.size() > 0)
				return true;
			else
				return false;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": "
					+ e.getMessage());
			System.exit(0);
			return false;
		}
	}

	public boolean addLocation(String l, int cap) throws SQLException {
		if (!checkIfExists(l)) {
			stmt = c.createStatement();
			sql = "INSERT INTO LOCATIONTABLE (LOCATION, CAPACITY) "
					+ "VALUES ( '" + l + "'" + ",'" + cap + "' );";
			stmt.executeUpdate(sql);
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "Location already exists",
					"Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	// a function to load location into some kind of map
	// return an array list of string
	public ArrayList<String> getLocationList() {
		ArrayList<String> temp = new ArrayList<String>();
		try {
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM LOCATIONTABLE;");
			while (rs.next()) {
				String name = rs.getString("LOCATION");
				temp.add(name);
			}
			return temp;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": "
					+ e.getMessage());
			System.exit(0);
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": "
					+ e.getMessage());
			System.exit(0);
		}
		return null;
	}

	public int getLocationID(String l) {
		try {
			ArrayList<Integer> idAL = new ArrayList<Integer>();
			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM LOCATIONTABLE WHERE LOCATION='"
							+ l + "';");
			while (rs.next()) {
				int ans = rs.getInt("ID");
				idAL.add(ans);
			}
			switch (idAL.size()) {
			case 0:
				return 0; // does not exist
			case 1:
				return idAL.get(0); // only 1 exist (ideal scenario)
			default:
				return -1; // exist multiple times (should never occur)
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": "
					+ e.getMessage());
			System.exit(0);
			return -2;
		}
	}

	public String getLocationName(int locID) {
		try {
			ArrayList<String> idAL = new ArrayList<String>();
			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM LOCATIONTABLE WHERE ID='"
							+ locID + "';");
			while (rs.next()) {
				String ans = rs.getString("LOCATION");
				idAL.add(ans);
			}
			switch (idAL.size()) {
			case 0:
				return null; // does not exist
			case 1:
				return idAL.get(0); // only 1 exist (ideal scenario)
			default:
				return null; // exist multiple times (should never occur)
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": "
					+ e.getMessage());
			System.exit(0);
			return null;
		}
	}
	
	
	
	public boolean deleteLocation(int id) {
		try {
			stmt = c.createStatement();
			String sql = "DELETE from LOCATIONTABLE WHERE ID=" + id + ";";
			stmt.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
	}

	public boolean modifyLocation(int id, String what) {
		try {
			stmt = c.createStatement();
			String sql = "UPDATE LOCATIONTABLE set LOCATION = " + what
					+ " where ID=" + id + ";";
			stmt.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
	}

	public int getCapacityByName(String locname) {
		try {
			ArrayList<Integer> idAL = new ArrayList<Integer>();
			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM LOCATIONTABLE WHERE LOCATION='"
							+ locname + "';");
			while (rs.next()) {
				int ans = rs.getInt("CAPACITY");
				idAL.add(ans);
			}
			switch (idAL.size()) {
			case 0:
				return 0; // does not exist
			case 1:
				return idAL.get(0); // only 1 exist (ideal scenario)
			default:
				return -1; // exist multiple times (should never occur)
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": "
					+ e.getMessage());
			System.exit(0);
			return -2;
		}

	}
}
