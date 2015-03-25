package hkust.cse.calendar.unit;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.LinkedList;

public class Appt implements Serializable {

	private Appointment a;
	
	public Appt() {								// A default constructor used to set all the attribute to default values
		a = new Appointment();
	}

	// Getter of the mTimeSpan
	public TimeSpan TimeSpan() {
		return a.getTimeSpan();
	}
	
	// Getter of the appointment title
	public String getTitle() {
		return a.getTitle();
	}

	// Getter of appointment description
	public String getInfo() {
		return a.getDescription();
	}

	// Getter of the appointment id
	public int getID() {
		return a.getID();
	}
	
	// Getter of the join appointment id
	public int getJoinID(){
		return a.getJID();
	}

	public void setJoinID(int joinID){
		a.setJID(joinID);
	}
	
	// Getter of the attend LinkedList<String>
	public LinkedList<String> getAttendList(){
		return a.getAttend();
	}
	
	// Getter of the reject LinkedList<String>
	public LinkedList<String> getRejectList(){
		return a.getReject();
	}
	
	// Getter of the waiting LinkedList<String>
	public LinkedList<String> getWaitingList(){
		return a.getWaiting();
	}
	
	public LinkedList<String> getAllPeople(){
		LinkedList<String> allList = new LinkedList<String>();
		allList.addAll(getAttendList());
		allList.addAll(getRejectList());
		allList.addAll(getWaitingList());
		return allList;
	}
	
	public void addAttendant(String addID){
		if (a.getAttend() == null)
			a.initiateAttend();
		a.addToAttend(addID);
	}
	
	public void addReject(String addID){
		if (a.getReject() == null)
			a.initiateReject();
		a.addToReject(addID);
	}
	
	public void addWaiting(String addID){
		if (a.getWaiting() == null)
			a.initiateWaiting();
		a.addWaiting(addID);
	}
	
	public void setWaitingList(LinkedList<String> waitingList){
		a.setWaiting(waitingList);
	}
	
	public void setWaitingList(String[] waitingList){
		LinkedList<String> tempLinkedList = new LinkedList<String>();
		if (waitingList !=null){
			for (int a=0; a<waitingList.length; a++){
				tempLinkedList.add(waitingList[a].trim());
			}
		}
		a.setWaiting(tempLinkedList);
	}
	
	public void setRejectList(LinkedList<String> rejectLinkedList) {
		a.setReject(rejectLinkedList);
	}
	
	public void setRejectList(String[] rejectList){
		LinkedList<String> tempLinkedList = new LinkedList<String>();
		if (rejectList !=null){
			for (int a=0; a<rejectList.length; a++){
				tempLinkedList.add(rejectList[a].trim());
			}
		}
		a.setReject(tempLinkedList);
	}
	
	public void setAttendList(LinkedList<String> attendLinkedList) {
		a.setAttend(attendLinkedList);
	}
	
	public void setAttendList(String[] attendList){
		LinkedList<String> tempLinkedList = new LinkedList<String>();
		if (attendList !=null){
			for (int a=0; a<attendList.length; a++){
				tempLinkedList.add(attendList[a].trim());
			}
		}
		a.setAttend(tempLinkedList);
	}
	// Getter of the appointment title
	public String toString() {
		return a.getTitle();
	}

	// Setter of the appointment title
	public void setTitle(String t) {
		a.setTitle(t);
	}

	// Setter of the appointment description
	public void setInfo(String in) {
		a.setDescription(in);
	}

	// Setter of the mTimeSpan
	@SuppressWarnings("deprecation")
	public void setTimeSpan(TimeSpan d) {
		a.setStartDateTime(d.StartTime().getHours(), d.StartTime().getMinutes(), d.StartTime().getYear(), d.StartTime().getMonth(), d.StartTime().getDay());
		a.setEndDateTime(d.EndTime().getHours(), d.EndTime().getMinutes(), d.EndTime().getYear(), d.EndTime().getMonth(), d.EndTime().getDay());
	}

	// Setter if the appointment id
	public void setID(int id) {
		a.setID(id);
	}
	
	// check whether this is a joint appointment
	public boolean isJoint(){
		return a.getIsJoint();
	}

	// setter of the isJoint
	public void setJoint(boolean isjoint){
		a.setIsJoint(isjoint);
	}
}
