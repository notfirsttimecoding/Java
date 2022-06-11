package de.hs_kl.staab.planner;

public class User {

	protected String userName;
	protected String userFirstName;
	protected String userLastName;

	protected User(String userName, String userFirstName, String userLastName) {
		this.userName = userName;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
	}

	public String toString() {
		return "Username: " + userName + " (First Name: " + userFirstName + " ,Last Name: " + userLastName + ")";
	}

	public String getUsername() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
}
