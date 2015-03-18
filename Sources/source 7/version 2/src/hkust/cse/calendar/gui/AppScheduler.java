package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorage;
import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.User;

import javax.swing.JDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;


public class AppScheduler extends JDialog implements ActionListener,
		ComponentListener {
	
	/*this variable is used to delete the old object when an object is modified*/
	private Appt temp;
	private boolean checkLocations;
	
	
	private JLabel yearL;
	private JTextField yearF;
	private JLabel monthL;
	private JTextField monthF;
	private JLabel dayL;
	private JTextField dayF;
	private JLabel sTimeHL;
	private JTextField sTimeH;
	private JLabel sTimeML;
	private JTextField sTimeM;
	private JLabel eTimeHL;
	private JTextField eTimeH;
	private JLabel eTimeML;
	private JTextField eTimeM;
	private String [] repeat= new String [] {"One-Off","Daily","Weekly","Monthly"};
	private String [] reminder= new String [] {"No", "Alert at start" ,"15mins","30mins","1Hr","2Hr"}; // to ask if reminder is needed or not
	private String [] appttype= new String [] {"Regular","Group"};
	private DefaultListModel model;
	private JTextField titleField;
	
	
	//private boolean delNo; 					// prevent an object from being deleted
	
	//private int reminderVar = 0;
	//private int repeatCode; // this String stores the type of the new appointment

	//private boolean delNo; // prevent an object from being deleted
	
	private JButton saveBut;
	private JButton CancelBut;
	private JButton inviteBut;
	private JButton rejectBut;
	
	private Appt NewAppt;
	private CalGrid parent;
	private boolean isNew = true;
	private boolean isChanged = true;
	private boolean isJoint = false;

	private JTextArea detailArea;

	private JSplitPane pDes;
	JPanel detailPanel;

//	private JTextField attendField;
//	private JTextField rejectField;
//	private JTextField waitingField;
	private int selectedApptId = -1;
	
	JComboBox locField; // added as required
	private JComboBox repeatBox; //added as required
	private JComboBox reminderBox;
	private JComboBox appointmenttype;
	private UserSelectDialog SelectUsers;
	
		
	
	
	
	
	
	private void commonConstructor(String title, CalGrid cal) {
		parent = cal;
		//pack();
		this.toFront();
		this.repaint();
		this.setSize(200, 400);
		this.setResizable(false);
		setTitle(title);
		setModal(false);

		temp = new Appt(); // initialize temp to be null first
		temp.setUpdate(false);
		
		Container contentPane;
		contentPane = getContentPane();
		
		JPanel pDate = new JPanel();
		Border dateBorder = new TitledBorder(null, "DATE");
		pDate.setBorder(dateBorder);

		yearL = new JLabel("YEAR: ");
		pDate.add(yearL);
		yearF = new JTextField(6);
		pDate.add(yearF);
		monthL = new JLabel("MONTH: ");
		pDate.add(monthL);
		monthF = new JTextField(4);
		pDate.add(monthF);
		dayL = new JLabel("DAY: ");
		pDate.add(dayL);
		dayF = new JTextField(4);
		pDate.add(dayF);

		JPanel psTime = new JPanel();
		Border stimeBorder = new TitledBorder(null, "START TIME");
		psTime.setBorder(stimeBorder);
		sTimeHL = new JLabel("Hour");
		psTime.add(sTimeHL);
		sTimeH = new JTextField(4);
		psTime.add(sTimeH);
		sTimeML = new JLabel("Minute");
		psTime.add(sTimeML);
		sTimeM = new JTextField(4);
		psTime.add(sTimeM);

		JPanel peTime = new JPanel();
		Border etimeBorder = new TitledBorder(null, "END TIME");
		peTime.setBorder(etimeBorder);
		eTimeHL = new JLabel("Hour");
		peTime.add(eTimeHL);
		eTimeH = new JTextField(4);
		peTime.add(eTimeH);
		eTimeML = new JLabel("Minute");
		peTime.add(eTimeML);
		eTimeM = new JTextField(4);
		peTime.add(eTimeM);

		JPanel pTime = new JPanel();
		pTime.setLayout(new BorderLayout());
		pTime.add("West", psTime);
		pTime.add("East", peTime);

		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		top.setBorder(new BevelBorder(BevelBorder.RAISED));
		top.add(pDate, BorderLayout.NORTH);
		top.add(pTime, BorderLayout.CENTER);

		contentPane.add("North", top);

		JPanel titleAndTextPanel = new JPanel();
		JLabel titleL = new JLabel("TITLE");
		titleField = new JTextField(15);
		titleAndTextPanel.add(titleL);
		titleAndTextPanel.add(titleField);

		// added by me
		Location[] locations = cal.controller.getLocationList();
		if(locations == null || locations.length == 0){ // if it's null or empty, do not allow to do anything
			locations = new Location[0];
		//	JOptionPane.showMessageDialog(null, "No locations");
		//	setVisible(false);
		//	return; // do not allow to set an appointment
			checkLocations = false;
		}
		JLabel locationL = new JLabel("LOCATION");
		JLabel appointmenttypelabel= new JLabel("APPOINTMENT TYPE");
		JLabel repeatR= new JLabel("APPOINTMENT FREQUENCY");
		JLabel notification= new JLabel("REMINDER NEEDED?");
		

		//delNo = false;
		
		locField = new JComboBox(locations);
		appointmenttype=new JComboBox(appttype);
		repeatBox= new JComboBox(repeat);
	    reminderBox= new JComboBox(reminder);
		titleAndTextPanel.add(locationL);
		titleAndTextPanel.add(locField);
		titleAndTextPanel.add(repeatR);
		
		appointmenttype.addActionListener(this);
	//	reminder =new JTextField("One-off");
		titleAndTextPanel.add(repeatBox);
		titleAndTextPanel.add(appointmenttypelabel);
		titleAndTextPanel.add(appointmenttype);
		titleAndTextPanel.add(notification);
		titleAndTextPanel.add(reminderBox);
		
		
		/*up to here*/
		
		
		detailPanel = new JPanel();
		detailPanel.setLayout(new BorderLayout());
		Border detailBorder = new TitledBorder(null, "Appointment Description");
		detailPanel.setBorder(detailBorder);
		detailArea = new JTextArea(20, 30);

		detailArea.setEditable(true);
		JScrollPane detailScroll = new JScrollPane(detailArea);
		detailPanel.add(detailScroll);

		pDes = new JSplitPane(JSplitPane.VERTICAL_SPLIT, titleAndTextPanel,
				detailPanel);

		top.add(pDes, BorderLayout.SOUTH);

		if (NewAppt != null) {
			detailArea.setText(NewAppt.getInfo());

		}
		
		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(FlowLayout.RIGHT));

//		inviteBut = new JButton("Invite");
//		inviteBut.addActionListener(this);
//		panel2.add(inviteBut);
		
		saveBut = new JButton("Save");
		saveBut.addActionListener(this);
		panel2.add(saveBut);

		rejectBut = new JButton("Reject");
		rejectBut.addActionListener(this);
		panel2.add(rejectBut);
		rejectBut.show(false);

		CancelBut = new JButton("Cancel");
		CancelBut.addActionListener(this);
		panel2.add(CancelBut);

		contentPane.add("South", panel2);
		NewAppt = new Appt();

		if (this.getTitle().equals("Join Appointment Content Change") || this.getTitle().equals("Join Appointment Invitation")){
			inviteBut.show(false);
			rejectBut.show(true);
			CancelBut.setText("Consider Later");
			saveBut.setText("Accept");
		}
		if (this.getTitle().equals("Someone has responded to your Joint Appointment invitation") ){
			inviteBut.show(false);
			rejectBut.show(false);
			CancelBut.show(false);
			saveBut.setText("confirmed");
		}
		if (this.getTitle().equals("Join Appointment Invitation") || this.getTitle().equals("Someone has responded to your Joint Appointment invitation") || this.getTitle().equals("Join Appointment Content Change")){
			allDisableEdit();
		}
		pack();
			

	}
	
	AppScheduler(String title, CalGrid cal, int selectedApptId) {
		checkLocations = true; // checks whether the locations exist
		if(cal.controller.getLocationList() == null || cal.controller.getLocationList().length == 0){
			JOptionPane.showMessageDialog(null, "No locations");
			checkLocations = false;
		}
		
		this.selectedApptId = selectedApptId;
		commonConstructor(title, cal);
	}

	AppScheduler(String title, CalGrid cal) {
		checkLocations = true; // checks whether the locations exist
		if(cal.controller.getLocationList() == null || cal.controller.getLocationList().length == 0){
			JOptionPane.showMessageDialog(null, "No locations");
			checkLocations = false;
		}
		
		commonConstructor(title, cal);
	}
	
	public void actionPerformed(ActionEvent e) {

		// distinguish which button is clicked and continue with require function
		if (e.getSource() == CancelBut) {
		
			setVisible(false);
			dispose();
			
		} else if (e.getSource() == saveBut) {
		
			saveButtonResponse();
			if(temp.getUpdate() == true && temp.canDelete() == true){//delete from mediator
				ApptStorage.mAppts.remove(temp.TimeSpan());
			temp.setUpdate(false);
		}
			

		} else if (e.getSource() == rejectBut){
			if (JOptionPane.showConfirmDialog(this, "Reject this joint appointment?", "Confirmation", JOptionPane.YES_NO_OPTION) == 0){
				NewAppt.addReject(getCurrentUser());
				NewAppt.getAttendList().remove(getCurrentUser());
				NewAppt.getWaitingList().remove(getCurrentUser());
				this.setVisible(false);
				dispose();
			}
		} else if(e.getSource() == appointmenttype) {
			if(appointmenttype.getSelectedIndex()==1) {
				NewAppt.setWaitingList((LinkedList) null);
				SelectUsers = new UserSelectDialog(this, parent, NewAppt, locField.getSelectedItem().toString());
			} else if(appointmenttype.getSelectedIndex()==0) {
				NewAppt.setWaitingList((LinkedList) null);
				SelectUsers.setVisible(false);
				SelectUsers.dispose();
			}
		}
		
		
		parent.getAppList().clear();
		parent.getAppList().setTodayAppt(parent.GetTodayAppt());
		parent.repaint();
	}

	private JPanel createPartOperaPane() {
		JPanel POperaPane = new JPanel();
		JPanel browsePane = new JPanel();
		JPanel controPane = new JPanel();

		POperaPane.setLayout(new BorderLayout());
		TitledBorder titledBorder1 = new TitledBorder(BorderFactory
				.createEtchedBorder(Color.white, new Color(178, 178, 178)),
				"Add Participant:");
		browsePane.setBorder(titledBorder1);

		POperaPane.add(controPane, BorderLayout.SOUTH);
		POperaPane.add(browsePane, BorderLayout.CENTER);
		POperaPane.setBorder(new BevelBorder(BevelBorder.LOWERED));
		return POperaPane;

	}

	private int[] getValidDate() {

		int[] date = new int[3];
		date[0] = Utility.getNumber(yearF.getText());
		date[1] = Utility.getNumber(monthF.getText());
		if (date[0] < 1980 || date[0] > 2100) {
			JOptionPane.showMessageDialog(this, "Please input proper year",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (date[1] <= 0 || date[1] > 12) {
			JOptionPane.showMessageDialog(this, "Please input proper month",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		date[2] = Utility.getNumber(dayF.getText());
		int monthDay = CalGrid.monthDays[date[1] - 1];
		if (date[1] == 2) {
			GregorianCalendar c = new GregorianCalendar();
			if (c.isLeapYear(date[0]))
				monthDay = 29;
		}
		if (date[2] <= 0 || date[2] > monthDay) {
			JOptionPane.showMessageDialog(this,
			"Please input proper month day", "Input Error",
			JOptionPane.ERROR_MESSAGE);
			return null;
			
		}
		return date;
	}

	private int getTime(JTextField h, JTextField min) {

		int hour = Utility.getNumber(h.getText());
		if (hour == -1)
			return -1;
		int minute = Utility.getNumber(min.getText());
		if (minute == -1)
			return -1;

		return (hour * 60 + minute);

	}

	private int[] getValidTimeInterval() {

		int[] result = new int[2];
		result[0] = getTime(sTimeH, sTimeM);
		result[1] = getTime(eTimeH, eTimeM);
		if ((result[0] % 15) != 0 || (result[1] % 15) != 0) {
			JOptionPane.showMessageDialog(this,
					"Minute Must be 0, 15, 30, or 45 !", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		if (!sTimeM.getText().equals("0") && !sTimeM.getText().equals("15") && !sTimeM.getText().equals("30") && !sTimeM.getText().equals("45") 
			|| !eTimeM.getText().equals("0") && !eTimeM.getText().equals("15") && !eTimeM.getText().equals("30") && !eTimeM.getText().equals("45")){
			JOptionPane.showMessageDialog(this,
					"Minute Must be 0, 15, 30, or 45 !", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		if (result[1] == -1 || result[0] == -1) {
			JOptionPane.showMessageDialog(this, "Please check time",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (result[1] <= result[0]) {
			JOptionPane.showMessageDialog(this,
					"End time should be bigger than \nstart time",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if ((result[0] < AppList.OFFSET * 60)
				|| (result[1] > (AppList.OFFSET * 60 + AppList.ROWNUM * 2 * 15))) {
			JOptionPane.showMessageDialog(this, "Out of Appointment Range !",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return result;
	}

	private void saveButtonResponse() {
		// Fix Me!
		// Save the appointment to the hard disk

		
		if((yearF.getText().length() == 0) || (monthF.getText().length() == 0) || (dayF.getText().length() == 0) || (titleField.getText().length() == 0) || (sTimeH.getText().length() == 0) || (sTimeM.getText().length() == 0) || (eTimeH.getText().length() == 0) || (eTimeM.getText().length() == 0)){
			System.out.println("PROBLEM");
			return;
		}
		
		
		/*check whether the time is appropriate*/
		int tempIntArr[] = getValidTimeInterval();
		if(tempIntArr == null){return;}
		
		
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		Timestamp current = new Timestamp(now.getTime());
		current.setYear((current.getYear()+1900)); // set the current year to tge real year
		
		Timestamp apptTime = new Timestamp(Integer.parseInt(yearF.getText()), (Integer.parseInt(monthF.getText())-1), Integer.parseInt(dayF.getText()), Integer.parseInt(sTimeH.getText()), Integer.parseInt(sTimeM.getText()), 0, 0);
		
		
		if(current.after(apptTime)) {
			JOptionPane.showMessageDialog(this, "Cannot add an appointment in the past.");
			NewAppt.setDelete(false);
			temp.setDelete(false);
			return;
		}
		/*Initialise the new appointment*/
		ApptStorage.mAssignedApptID +=1; // a new appointment is created
		
		//NewAppt = new Appt();
		
		//delNo = false;
		
		if (appointmenttype.getSelectedItem().equals("Group")){
			NewAppt.setJoint(true);
			NewAppt.setJoinID(ApptStorage.mAssignedApptID);
		}
		else {NewAppt.setID(ApptStorage.mAssignedApptID); }
		
		NewAppt.setInfo(detailArea.getText()); // description
		NewAppt.setTitle(titleField.getText()); // title
		NewAppt.setType(repeatBox.getSelectedIndex()); 		//repeat - daily,weekly etc
		NewAppt.setReminder(reminderBox.getSelectedIndex()); 	//whether or not the user needs a notification
		NewAppt.setLocation(locField.getSelectedItem().toString()); // set the Location of the place
		NewAppt.setUseName(parent.mCurrUser.ID()); // set the name of the current user as the owner of the appointment
		// a new appointment is created);
		System.out.println("============================The chosen location is : " + locField.getSelectedItem().toString() + "==============================");
		
		NewAppt.setLocation (locField.getSelectedItem().toString());
		
		Timestamp tempStart = new Timestamp((Integer.parseInt(yearF.getText())), (Integer.parseInt(monthF.getText())-1), Integer.parseInt(dayF.getText()), Integer.parseInt(sTimeH.getText()), Integer.parseInt(sTimeM.getText()), 0, 0);
		Timestamp tempEnd = new Timestamp((Integer.parseInt(yearF.getText())), (Integer.parseInt(monthF.getText())-1), Integer.parseInt(dayF.getText()), Integer.parseInt(eTimeH.getText()), Integer.parseInt(eTimeM.getText()), 0, 0);
		
		
		NewAppt.setTimeSpan(new TimeSpan(tempStart, tempEnd)); // add a timeSpan
		
		/*use the parent variable to save the appointment*/
		parent.controller.ManageAppt(NewAppt, 3); // add a new appointment
		/*inform the user, that she/he cannot have two overlapping appointments*/
		if(!(parent.controller.getStateOfSpan())){
			JOptionPane.showMessageDialog(this, "Please change the time span");
			NewAppt.setDelete(false);
			temp.setDelete(false);
			return;
		}
		
		
		
		System.out.println("============================The 8th hour appointment has been added  ==============================");
		
		
		setVisible(false); // make the Dialog to disappear
		dispose();
	}

	private Timestamp CreateTimeStamp(int[] date, int time) {
		Timestamp stamp = new Timestamp(0);
		stamp.setYear(date[0]);
		stamp.setMonth(date[1] - 1);
		stamp.setDate(date[2]);
		stamp.setHours(time / 60);
		stamp.setMinutes(time % 60);
		return stamp;
	}

	public void updateSetApp(Appt appt) {
		// Fix Me!
		/*set all the new values*/
		
		//System.out.println("Testing case to print out the text " + appt.TimeSpan());
		yearF.setText(new Integer(appt.TimeSpan().EndTime().getYear()+1900).toString());
		//System.out.println(appt.TimeSpan().StartTime().getYear());
		monthF.setText(new Integer(appt.TimeSpan().StartTime().getMonth()+1).toString());
		dayF.setText(new Integer(appt.TimeSpan().StartTime().getDate()).toString());
		sTimeH.setText(new Integer(appt.TimeSpan().StartTime().getHours()).toString());
		sTimeM.setText(new Integer(appt.TimeSpan().StartTime().getMinutes()).toString());
		eTimeH.setText(new Integer(appt.TimeSpan().EndTime().getHours()).toString());
		eTimeM.setText(new Integer(appt.TimeSpan().EndTime().getMinutes()).toString());
		
		detailArea.setText(appt.getInfo());
		titleField.setText(appt.getTitle());
		appt.setUpdate(true);
		
		// make a copy of appt
		temp = new Appt();
		temp.setTimeSpan(appt.TimeSpan()); // add a timeSpan
		temp.setUpdate(true);
		
		//ApptStorage.mAppts.remove(appt.TimeSpan(), appt);
		
	}

	public void componentHidden(ComponentEvent e) {

	}

	public void componentMoved(ComponentEvent e) {

	}

	public void componentResized(ComponentEvent e) {

		Dimension dm = pDes.getSize();
		double width = dm.width * 0.93;
		double height = dm.getHeight() * 0.6;
		detailPanel.setSize((int) width, (int) height);

	}

	public void componentShown(ComponentEvent e) {

	}
	
	public String getCurrentUser()		// get the id of the current user
	{
		return this.parent.mCurrUser.ID();
	}
	
	private void allDisableEdit(){
		yearF.setEditable(false);
		monthF.setEditable(false);
		dayF.setEditable(false);
		sTimeH.setEditable(false);
		sTimeM.setEditable(false);
		eTimeH.setEditable(false);
		eTimeM.setEditable(false);
		titleField.setEditable(false);
		detailArea.setEditable(false);
	}
	
	
	
	// added by me in order to handle the no locations problem
	
	public boolean noLocationsCheck(){
		return checkLocations;
	}
	
	public void setNewApptTimeSpan(String year, String month, String day, String hour1, String minutes1, String hour2, String minutes2) {
		
		if(year!="") {
			yearF.setText(year);
			monthF.setText(month);
			dayF.setText(day);
			sTimeH.setText(hour1);
			sTimeM.setText(minutes1);
			eTimeH.setText(hour2);
			eTimeM.setText(minutes2);
		}
	}
}