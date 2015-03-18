package hkust.cse.calendar.unit;

import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.unit.Location;

public class SimpleUser extends User{
	
	// Constructor of class 'User' which set up the user id and password
			public SimpleUser(String id, String pass) {
				mID = id;
				mPassword = pass;
				firstName = null;
				lastName = null;
				email = null;
				otherInformation = null;
				isAdmin = false;
				userLocations = null;
			}
			
			
			public SimpleUser(String id, String pass, String name, String surname, String mail){
				mID = id;
				mPassword = pass;
				firstName = name;
				lastName = surname;
				email = mail;
				isAdmin = false;
				otherInformation = null;
				userLocations = null;
			}
			
			
			// Setter of the user id
			
			public void setID(String newID){
				this.mID = newID;
			}
			
			// Getter of the user id
			public String ID() {		
				return this.mID;
			}
			
			// Another getter of the user id
			public String toString() {
				return ID();
			}

			// Getter of the user password
			public String Password() {
				return this.mPassword;
			}

			// Setter of the user password
			public void Password(String pass) {
				this.mPassword = pass;
			}
			
			// Getter of the firstName
			public String getFirstName(){
				return this.firstName;
			}
			
			// Setter of the firstName
			public void setFirstName(String name){
				this.firstName = name;
			}
			
			
			// Getter of the lastName
			
			public String getLastName(){
				return this.lastName;
			}
			
			
			// Setter of the lastName
			public void setLastName(String surname){
				this.lastName = surname;
			}
			
			// Getter of the email address
			public String getEmail(){
				return this.email;
			}
			
			// Setter of the email address
			public void setEmail(String mail){
				this.email = mail;
			}
			
			// Setter of the extra information
			public void setOtherInfmormation(String extra){
				this.otherInformation = extra;
			}
			
			// Getter of the extra information
			public String getOtherInformation(){
				return this.otherInformation;
			}
			
			
			// Getter of the admin status
			public boolean getAdminStatus(){
				return this.isAdmin;
			}

			
			//Getter of the list of locations
			public Location[] getUserLocations(){
				return userLocations;
			}
			
			
			//Setter of the list of locations
			public void setUserLocations(Location[] locations){
				userLocations = locations;
			}
}
