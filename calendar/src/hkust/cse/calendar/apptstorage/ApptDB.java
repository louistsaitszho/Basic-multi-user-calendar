package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.unit.Appointment;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Message;
import hkust.cse.calendar.unit.TimeMachine;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class ApptDB {
	private Connection c = null;
    private Statement stmt = null;
    private String sql = null;
    private ResultSet rs = null;
    private MessageDB mdb;

    public ApptDB()
	{
	c = null;
	stmt = null;
    mdb = new MessageDB();
	try 
    {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:appt.db");
	    stmt = c.createStatement();
	    sql = "CREATE TABLE IF NOT EXISTS APPOINTMENT " +
	   	  	  "(ID                   INTEGER            NOT NULL        PRIMARY KEY AUTOINCREMENT," +
	   	      " TITLE                TEXT               NOT NULL," +
	   	      " DESCRIPTION          TEXT               NOT NULL," +
	   	      " LOCATION             TEXT               NOT NULL," +
	          " START_TIME_HOUR      INT                NOT NULL," +
	          " START_TIME_MINUTE    INT                NOT NULL," +
	          " START_TIME_YEAR      INT                NOT NULL," +
	          " START_TIME_MONTH     INT                NOT NULL," +
	          " START_TIME_DAY       INT                NOT NULL," +
	          " END_TIME_HOUR        INT                NOT NULL," +
	          " END_TIME_MINUTE      INT                NOT NULL," +
	          " END_TIME_YEAR        INT                NOT NULL," +
	          " END_TIME_MONTH       INT                NOT NULL," +
	          " END_TIME_DAY         INT                NOT NULL," +
	          " REMINDER             INT                NOT NULL," +
	          " REMINDER_TIME        INT                		," +
	          " REMINDER_UNIT        INT                		," +
	          " USER                 INT                NOT NULL," +
	          " GOING				 TEXT						," +
	          " WAITING                  TEXT               )";
	      stmt.executeUpdate(sql);
	    } catch (Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	  }
    
    public ArrayList<Appointment> getAppointmentList()
    {
		ArrayList<Appointment> temp = new ArrayList<Appointment>();
		try {
			stmt = c.createStatement();
	        rs = stmt.executeQuery( "SELECT * FROM APPOINTMENT;" );
	        String TITLE = "";
	        String DESCRIPTION = "";
	        String LOCATION = "";
	        int START_TIME_HOUR;
	        int START_TIME_MINUTE;
	        int START_TIME_YEAR;
	        int START_TIME_MONTH;
	        int START_TIME_DAY;
	        int END_TIME_HOUR;
	        int END_TIME_MINUTE;
	        int END_TIME_YEAR;
	        int END_TIME_MONTH;
	        int END_TIME_DAY;
	        int REMINDER;
	        int REMINDER_TIME;
	        int REMINDER_UNIT;
	        int USER;
	        String GOING;
	        String WAITING;
	        int ID;
//	        ArrayList<String> attend = new ArrayList<String>();
//	        ArrayList<String> reject = new ArrayList<String>();
//	        ArrayList<String> waiting = new ArrayList<String>();
	        while ( rs.next() ) {
				TITLE = rs.getString("TITLE");
				DESCRIPTION = rs.getString("DESCRIPTION");
				START_TIME_HOUR = rs.getInt("START_TIME_HOUR");
				START_TIME_MINUTE =rs.getInt("START_TIME_MINUTE");
				START_TIME_YEAR =rs.getInt("START_TIME_YEAR");
				START_TIME_MONTH =rs.getInt("START_TIME_MONTH");
				START_TIME_DAY =rs.getInt("START_TIME_DAY");
				END_TIME_HOUR =rs.getInt("END_TIME_HOUR");
				END_TIME_MINUTE =rs.getInt("END_TIME_MINUTE");
				END_TIME_YEAR =rs.getInt("END_TIME_YEAR");
				END_TIME_MONTH =rs.getInt("END_TIME_MONTH");
				END_TIME_DAY =rs.getInt("END_TIME_DAY");
				REMINDER =rs.getInt("REMINDER");
				REMINDER_TIME =rs.getInt("REMINDER_TIME");
				LOCATION = rs.getString("LOCATION");
				REMINDER_UNIT = rs.getInt("REMINDER_UNIT");
				GOING = rs.getString("GOING");
				WAITING = rs.getString("WAITING");
				USER = rs.getInt("USER");
				ID = rs.getInt("ID");
				
				LinkedList<Integer> goingUIDLL = StringToLinkedList(GOING);
				LinkedList<Integer> waitingUIDLL = StringToLinkedList(WAITING);
				
				Appointment tempAppointment = new Appointment(TITLE, DESCRIPTION, LOCATION, START_TIME_HOUR, START_TIME_MINUTE, START_TIME_YEAR, START_TIME_MONTH, START_TIME_DAY, END_TIME_HOUR, END_TIME_MINUTE, END_TIME_YEAR, END_TIME_MONTH, END_TIME_DAY, REMINDER, REMINDER_TIME, REMINDER_UNIT, goingUIDLL,waitingUIDLL, ID, USER);
				temp.add(tempAppointment);
	        }
			return temp;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}
		return null;
	}
    
    public boolean addAppt(Appointment a)
	{
		try {
			stmt = c.createStatement();
			System.out.println(LinkedListToString(a.getGoingList()));
			System.out.println(LinkedListToString(a.getWaitingList()));
			
			sql = "INSERT INTO APPOINTMENT ("
				+ "TITLE, "
				+ "DESCRIPTION, "
				+ "LOCATION, "
				+ "START_TIME_HOUR, "
				+ "START_TIME_MINUTE, "
				+ "START_TIME_YEAR, "
				+ "START_TIME_MONTH, "
				+ "START_TIME_DAY, "
				+ "END_TIME_HOUR, "
				+ "END_TIME_MINUTE, "
				+ "END_TIME_YEAR, "
				+ "END_TIME_MONTH, "
				+ "END_TIME_DAY, "
				+ "REMINDER, "
				+ "REMINDER_TIME, "
				+ "REMINDER_UNIT, "
				+ "USER, "
				+ "GOING, " 
				+ "WAITING) " +
					"VALUES ('" 
				+ a.getTitle() + "','" 
				+ a.getDescription() + "','" 
				+ a.getLocation() + "',"
				+ a.getStartHour() + "," 
				+ a.getStartMin() + "," 
				+ a.getStartYear() + "," 
				+ a.getStartMonth() + "," 
				+ a.getStartDay() + "," 
				+ a.getEndHour() + "," 
				+ a.getEndMin() + "," 
				+ a.getEndYear() + "," 
				+ a.getEndMonth() + "," 
				+ a.getEndDay() + "," 
				+ a.getReminder() + "," 
				+ a.getReminderTime() + "," 
				+ a.getReminderUnit() + "," 
				+ a.getCreaterUID() + ",'" 
				+ LinkedListToString(a.getGoingList()) + "','"
				+ LinkedListToString(a.getWaitingList()) + "');";
			System.out.println(sql);
			stmt.executeUpdate(sql);
			
			//create messages if needed
			if (a.getWaitingList().size() > 0)
			{
				ArrayList<Integer> UIDAL = new ArrayList<Integer>();
				for (Integer i:a.getWaitingList())
				{
					UIDAL.add(i);
				}
				Message m = new Message(4, UIDAL, getApptID(a));
				mdb.addMessage(m);
			}
			
			
			return true;
		}
		catch (SQLException sqle) {
			System.err.println( sqle.getClass().getName() + ": " + sqle.getMessage() );
		}
		return false;
	}
    
	public boolean deleteAppt(int id)
	{
		try
		{
			stmt = c.createStatement();
		    String sql = "DELETE from APPOINTMENT WHERE ID=" + id + ";";
		    stmt.executeUpdate(sql);
			return true;
		}
		catch (SQLException e)
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return false;
		}
	}
	
	public boolean modifyAppt(int id, Appointment newAppt)
	{
		try
		{	
			stmt = c.createStatement();
	        sql = "UPDATE APPOINTMENT SET"
	    		  + "      TITLE = '"           + newAppt.getTitle()
	    		  + "' ,   DESCRIPTION = '"     + newAppt.getDescription()
	    		  + "' ,   LOCATION = '"        + newAppt.getLocation()
	    		  + "' ,   START_TIME_HOUR="    + newAppt.getStartHour()
	    		  + "  ,   START_TIME_MINUTE= " + newAppt.getStartMin()
	    		  + "  ,   START_TIME_YEAR= "   + newAppt.getStartYear()
	    		  + "  ,   START_TIME_MONTH= "  + newAppt.getStartMonth()
	    		  + "  ,   START_TIME_DAY= "    + newAppt.getStartDay()
	    		  + "  ,   END_TIME_HOUR= "     + newAppt.getEndHour()
	    		  + "  ,   END_TIME_MINUTE= "   + newAppt.getEndMin()
	    		  + "  ,   END_TIME_YEAR= "     + newAppt.getEndYear()
	    		  + "  ,   END_TIME_MONTH= "    + newAppt.getEndMonth()
	    		  + "  ,   END_TIME_DAY= "      + newAppt.getEndDay()
	    		  + "  ,   REMINDER= "          + newAppt.getReminder()
	    		  + "  ,   REMINDER_TIME= "     + newAppt.getReminderTime()
	    		  + "  ,   REMINDER_UNIT= "     + newAppt.getReminderUnit()
	    		  + "  ,   GOING = '"            + LinkedListToString(newAppt.getGoingList())
	    		  + "' ,   WAITING = '"          + LinkedListToString(newAppt.getWaitingList())
	    		  + "'  WHERE ID="               + id
	    		  + ";";
			stmt.executeUpdate(sql);

			return true;
		}
		catch (SQLException e)
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return false;
		}
	}
	
	public Appt[] getApptByLocationName(String locName)
	{
		ArrayList<Appointment> aal = getAppointmentList();
		
		aal.removeIf((Appointment tmpappt) -> !(tmpappt.getLocation().equals(locName)));
		System.out.println(aal);
		
		//		for (Appointment a:aal)
//		{
//			if (a.getLocation() != l)
//			{
//				aal.remove(a);
//			}
//		}
		Appt[] temparray = new Appt[aal.size()];
		for (int i = 0; i<aal.size(); i++)
		{
			Appt tempappt = new Appt(aal.get(i));
			temparray[i] = tempappt;
		}
		return temparray;
	}
	
	public Appt[] getApptByUserTime(User u, TimeSpan d)
	{
		Appt[] abt = getApptByTime(d);
		ArrayList<Appt> temp = new ArrayList<Appt>();
		ArrayList<Integer> addedApptID = new ArrayList<Integer>();
		for (Appt a:abt)
		{
			if (a.getAppointment().getCreaterUID() == u.getUID())
			{
				addedApptID.add(a.getID());
				temp.add(a);
			}
		}
		ArrayList<Appointment> allGoingAppt = getAppointmentListFromGoing(u.getUID(), abt);
		for (Appointment a:allGoingAppt)
		{
			if (addedApptID.contains(a.getID()) == false)
			{
				Appt temp2 = new Appt(a);
				temp.add(temp2);
			}
		}
		ArrayList<Appointment> allWaitingAppt = getAppointmentListFromWaiting(u.getUID(), abt);
		for (Appointment a:allWaitingAppt)
		{
			if (addedApptID.contains(a.getID()) == false)
			{
				Appt temp2 = new Appt(a);
				temp.add(temp2);
			}
		}
		Appt[] temparray = new Appt[temp.size()];
		for (int i = 0; i<temp.size(); i++)
		{
			temparray[i] = temp.get(i);
		}
		return temparray;
	}
	
	public Appt[] getFutureApptWithUser(int userid)
	{
		ArrayList<Appointment> result = getAppointmentList();
		System.out.println("Result: "+ result.size());
		result.removeIf((Appointment tmpappt) ->  
				!(
						(tmpappt.getGoingList().contains(userid))
						|| (tmpappt.getWaitingList().contains(userid))
						|| (tmpappt.getCreaterUID()==userid)
				)
				|| tmpappt.getTimeSpan().EndTime().toLocalDateTime().isBefore(TimeMachine.getInstance().getTMTimestamp().toLocalDateTime())
				);
//		for (int i = 0; i<result.size(); i++)//decrease when removed element so not out of bound and hence decrease num of checking
//		{
//			if (result.get(i).getCreaterUID() != u)
//			{
//				if (result.get(i).getTimeSpan().EndTime().after(TimeMachine.getInstance().getTMTimestamp()) == false)
//				{
//					result.remove(i);
//					System.out.println("remove from arraylist");
//				}
//				else
//				{
//					if (result.get(i).getGoingList().contains(result.get(i)) == false)
//					{
//						if (result.get(i).getWaitingList().contains(result.get(i)) == false)
//						{
//							result.remove(i);
//							System.out.println("remove from arraylist");
//						}
//					}
//				}
//
//			}
//		}
		Appt[] tempA = new Appt[result.size()];
		for (int i = 0; i<result.size(); i++)
		{
			tempA[i] = new Appt(result.get(i));
		}
		System.out.println("tempA: " + tempA.length);
		return tempA;
	}

	public Appt[] getApptByTime(TimeSpan d)
	{
		ArrayList<Appointment> temp = new ArrayList<Appointment>();
		try {
			stmt = c.createStatement();
		    int START_TIME_YEAR = d.StartTime().getYear()+1900;
		    int START_TIME_MONTH = d.StartTime().getMonth()+1;
		    int START_TIME_DAY = d.StartTime().getDate();
		    int END_TIME_YEAR = d.EndTime().getYear()+1900;
		    int END_TIME_MONTH = d.EndTime().getMonth()+1;
		    int END_TIME_DAY = d.EndTime().getDate();
		    String TITLE = "";
		    String DESCRIPTION = "";
		    String LOCATION = "";
		    int REMINDER;
		    int REMINDER_TIME;
		    int REMINDER_UNIT;
		    String GOING = "";
		    String WAITING = "";
		    int createrID;
		    int ID;
		    String sql = "SELECT * FROM APPOINTMENT WHERE ("
			    	+ " START_TIME_YEAR=" + START_TIME_YEAR
			    	+ " AND START_TIME_MONTH=" + START_TIME_MONTH
			    	+ " AND START_TIME_DAY=" + START_TIME_DAY
			    	+ " AND END_TIME_YEAR=" + END_TIME_YEAR
			    	+ " AND END_TIME_MONTH=" + END_TIME_MONTH
			    	+ " AND END_TIME_DAY=" + END_TIME_DAY
			    	+ ");" ;
		    rs = stmt.executeQuery(sql);
		    while ( rs.next() ) {
				TITLE = rs.getString("TITLE");
				DESCRIPTION = rs.getString("DESCRIPTION");
				int START_TIME_HOUR = rs.getInt("START_TIME_HOUR");
				int START_TIME_MINUTE =rs.getInt("START_TIME_MINUTE");
				START_TIME_YEAR =rs.getInt("START_TIME_YEAR");
				START_TIME_MONTH =rs.getInt("START_TIME_MONTH");
				START_TIME_DAY =rs.getInt("START_TIME_DAY");
				int END_TIME_HOUR =rs.getInt("END_TIME_HOUR");
				int END_TIME_MINUTE =rs.getInt("END_TIME_MINUTE");
				END_TIME_YEAR =rs.getInt("END_TIME_YEAR");
				END_TIME_MONTH =rs.getInt("END_TIME_MONTH");
				END_TIME_DAY =rs.getInt("END_TIME_DAY");
				REMINDER =rs.getInt("REMINDER");
				REMINDER_TIME =rs.getInt("REMINDER_TIME");
				LOCATION = rs.getString("LOCATION");
				REMINDER_UNIT = rs.getInt("REMINDER_UNIT");
				createrID = rs.getInt("USER");
				ID = rs.getInt("ID");
				GOING = rs.getString("GOING");
				WAITING = rs.getString("WAITING");
//				System.out.println("GOING: " + GOING);
//				System.out.println("WAITING: " + WAITING);
				LinkedList<Integer> goingUIDLL = StringToLinkedList(GOING);
				LinkedList<Integer> waitingUIDLL = StringToLinkedList(WAITING);
//				System.out.println("goingUIDLL.size(): " + goingUIDLL.size());
//				System.out.println("waitingUIDLL.size(): " + waitingUIDLL.size());
				Appointment tempAppointment = new Appointment(TITLE, DESCRIPTION, LOCATION, START_TIME_HOUR, START_TIME_MINUTE, START_TIME_YEAR, START_TIME_MONTH, START_TIME_DAY, END_TIME_HOUR, END_TIME_MINUTE, END_TIME_YEAR, END_TIME_MONTH, END_TIME_DAY, REMINDER, REMINDER_TIME, REMINDER_UNIT, goingUIDLL, waitingUIDLL, ID, createrID);
				temp.add(tempAppointment);
		    }
			Appt[] temparray = new Appt[temp.size()];
			for (int i = 0; i<temp.size(); i++)
			{
				Appt tempappt = new Appt(temp.get(i));
				temparray[i] = tempappt;
			}
			return temparray;
		} catch (SQLException e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		} catch (NullPointerException e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
		return null;
	}

	public Appt getApptByJID(int j)
	{
		//TODO [Phrase 2?] get Appt by JID
		try
		{
			ArrayList<Appointment> AppointmentALJID = new ArrayList<Appointment>();
			stmt = c.createStatement();
			sql = "SELECT * FROM APPOINTMENT WHERE JID=" + j;
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				//This part is pretty much the same as getApptByTime,
				//And since the structure of the DB is not finalized yet, 
				//Finish this part later
			}
		}
		catch (SQLException e)
		{
			//TODO what to do with this exception?
		}
		return null;
	}
	
	public Appt getApptByID(int i)
	{
		ArrayList<Appointment> ApptAL = getAppointmentList();
		for (Appointment a: ApptAL)
		{
			if(a.getID() == i)
			{
				return new Appt(a);
			}
		}
		return null;
	}
	
	public int getApptID(Appointment a)
	{
		ArrayList<Appointment> aal = getAppointmentList();
		System.out.println("aal.size(): " + aal.size());
		for (Appointment b:aal)
		{
			System.out.println("b: " + b.getTitle());
			System.out.println("a: " + a.getTitle());
			if (b.getTitle().compareTo(a.getTitle()) == 0)
			{
				System.out.println("Title matched");
				if (b.getDescription().compareTo(a.getDescription()) == 0)
				{
					System.out.println("Description matched");
					if (b.getLocation().compareTo(a.getLocation()) == 0)
					{
						System.out.println("Location matched");
							System.out.println("b.getID(): " + b.getID());
							return b.getID();
					}
				}
			}
		}
		return -1;
		
//		//return id iff every details match
//		try {
//			ArrayList<Integer> idAL = new ArrayList<Integer>();
//			stmt = c.createStatement();
//			rs = stmt.executeQuery("SELECT * FROM APPOINTMENT WHERE "
//					+ "TITLE='" + a.getTitle() + "'"
//					+ " ,DESCIRPTION='" + a.getDescription() + "' "
//					+ " ,LOCATION='" + a.getLocation() + "' "
//					+ " ,START_TIME_HOUR=" + a.getStartHour()
//					+ " ,START_TIME_MINUTE=" + a.getStartMin()
//					+ " ,START_TIME_YEAR=" + a.getStartYear()
//					+ " ,START_TIME_MONTH=" + a.getStartMonth()
//					+ " ,START_TIME_DAY=" + a.getStartDay()
//					+ " ,END_TIME_HOUR=" + a.getEndHour()
//					+ " ,END_TIME_MINUTE=" + a.getEndMin()
//					+ " ,END_TIME_YEAR=" + a.getEndYear()
//					+ " ,END_TIME_MONTH=" + a.getEndMonth()
//					+ " ,END_TIME_DAY=" + a.getEndDay()
//					+ " ;");
//			while (rs.next()) {
//				int ans = rs.getInt("ID");
//				idAL.add(ans);
//			}
//			switch (idAL.size()) {
//				case 0: return 0;			//does not exist
//				case 1:
//					return idAL.get(0);	//only 1 exist (ideal scenario)
//				default: return -1;			//exist multiple times (should never occur)
//			}
//		}
//		catch (SQLException e) {
//			JOptionPane.showMessageDialog(null, e.getClass().getName() + ": " + e.getMessage() );
//		    return -2;
//		}
	}
	
	public int getApptIDByTitle(String title)
	{
		try {
			ArrayList<Integer> idAL = new ArrayList<Integer>();
			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM APPOINTMENT WHERE "
					+ "TITLE='" + title
					+ "';");
			while (rs.next()) {
				int ans = rs.getInt("ID");
				idAL.add(ans);
			}
			switch (idAL.size()) {
			case 0: return 0;			//does not exist
			case 1:
				return idAL.get(0);	//only 1 exist (ideal scenario)
			default: return -1;			//exist multiple times (should never occur)
			}
		}
		catch (SQLException e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    return -2;
		}
	}

	public String LinkedListToString(LinkedList<Integer> list)
	{
		//e.g. String listS = "1/3/7/9/12/";
		if (list.size()>0)
		{
			String op = "";
			for (Integer a:list)
			{
				op = op + a + "/";
			}
			op = op.substring(0, op.length()-1);
			return op;
		}
		else
			return "";	
	}
	
	public LinkedList<Integer> StringToLinkedList(String listS)
	{
		if (listS.startsWith("[") == true)
		{
			listS = listS.substring(1, listS.length());
		}
		if (listS.endsWith("]") == true)
		{
			listS = listS.substring(0, listS.length()-1);
		}
		LinkedList<Integer> op = new LinkedList<Integer>();
		if (listS.length()>0)
		{
			String[] listA = listS.split("/");
			for (String a:listA)
			{
				op.add(Integer.parseInt(a));
			}
		}
		return op;
	}
	
	public ArrayList<Appointment> getAppointmentListFromGoing(int uid, Appt[] abt)
	{
		ArrayList<Appointment> apptIDAL = new ArrayList<Appointment>();
		ArrayList<Appointment> allAppointments = new ArrayList<Appointment>();
		for (Appt a:abt)
		{
			allAppointments.add(a.getAppointment());
		}
		for (Appointment a:allAppointments)
		{
			if (a.isThisUIDInGoing(uid) == true)
			{
				apptIDAL.add(a);
			}
		}
		return apptIDAL;
	}
	
	public ArrayList<Appointment> getAppointmentListFromWaiting(int uid, Appt[] abt)
	{
		ArrayList<Appointment> apptIDAL = new ArrayList<Appointment>();
		ArrayList<Appointment> allAppointments = new ArrayList<Appointment>();
		for (Appt a:abt)
		{
			allAppointments.add(a.getAppointment());
		}
		for (Appointment a:allAppointments)
		{
//			System.out.println("a.isThisUIDInWaiting(uid): " + a.isThisUIDInWaiting(uid));
//			System.out.println("uid: " + uid);
			if (a.isThisUIDInWaiting(uid) == true)
			{
				apptIDAL.add(a);
			}
		}
		return apptIDAL;
	}
	
	public int isThisUIDInCreateGoingWaiting(int uid, int id)
	{
		Appt thatAppt = getApptByID(id);
		if (thatAppt != null)
		{
			System.out.println(thatAppt.toString());
			System.out.println(thatAppt.getAppointment().getCreaterUID());
			System.out.println(thatAppt.getAppointment().getGoingList());
			System.out.println(thatAppt.getAppointment().getWaitingList());
			if (thatAppt.getAppointment().getCreaterUID() == uid)		//creator
			{
				return 1;
			}
			else if (thatAppt.getAppointment().getGoingList().contains(uid) == true)		//in going list
			{
				return 3;
			}
			else if (thatAppt.getAppointment().getWaitingList().contains(uid) == true)		//in waiting list
			{
				return 2;
			}
			else		//does not exist
			{
				System.out.println("Does not exist");
				return 0;
			}
		}
		else
		{
			System.out.println("Null appt");
			return 0;
		}
	}
	
	public boolean removeUIDFromGoingList(int uid, int id)
	{
		if (isThisUIDInCreateGoingWaiting(uid, id) == 3)
		{
			Appt toBeEdit = getApptByID(id);
			toBeEdit.getAppointment().deleteFromGoing(uid);
			modifyAppt(toBeEdit.getID(), toBeEdit.getAppointment());
			return true;
		}
		else	
		{
			return false;
		}
	}
	
	public boolean removeUIDFromWaitingList(int uid, int id)
	{
		if (isThisUIDInCreateGoingWaiting(uid, id) != 2)
		{
			Appt toBeEdit = getApptByID(id);
			toBeEdit.getAppointment().deleteFromWaiting(uid);
			modifyAppt(toBeEdit.getID(), toBeEdit.getAppointment());
			return true;
		}
		else	
		{
			return false;
		}
	}

	public boolean addUIDToGoingList(int uid, int id)
	{
		//TODO 
		System.out.print ("is this uid in going list: ");
		if (isThisUIDInCreateGoingWaiting(uid, id) != 3 )
		{
			System.out.println("yes");
			Appt toBeEdit = getApptByID(id);
			toBeEdit.getAppointment().addToGoing(uid);
			modifyAppt(toBeEdit.getID(), toBeEdit.getAppointment());
			return true;
		}
		else	
		{
			System.out.println("no");
			System.out.println("Value: " + isThisUIDInCreateGoingWaiting(uid, id));
			return false;
		}
	}

	public void delEventsWithUser(int userid){
		Appt[] futureApptWithUser = getFutureApptWithUser(userid);
		for(Appt tmpappt:futureApptWithUser){
			LocalDateTime currTime = TimeMachine.getInstance().getTMTimestamp().toLocalDateTime();
			LocalDateTime startTime = tmpappt.getAppointment().getTimeSpan().StartTime().toLocalDateTime();
			if (currTime.isBefore(startTime)){ //event have not happen
				deleteAppt(tmpappt.getID());
			}
		}
		
	}
	
	public void delEventsWithLocation(int locationid){
		Appt[] apptWithLocation = getApptByLocationName(LocationDB.getInstance().getLocationName(locationid)); 
		for(Appt tmpappt:apptWithLocation){
			LocalDateTime currTime = TimeMachine.getInstance().getTMTimestamp().toLocalDateTime();
			LocalDateTime startTime = tmpappt.getAppointment().getTimeSpan().StartTime().toLocalDateTime();
			if (currTime.isBefore(startTime)){ //event have not happen
				deleteAppt(tmpappt.getID());
			}
		}
	}
	
//	public String getNameByUID(int u)
//	{
//		ArrayList<Appointment> aal = getAppointmentList();
//		for (Appointment a:aal)
//		{
//			if (a.get)
//		}
//	}
}
