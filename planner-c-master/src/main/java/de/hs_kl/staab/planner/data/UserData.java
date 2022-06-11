package de.hs_kl.staab.planner.data;

import java.util.HashSet;
import java.util.Set;

import de.hs_kl.staab.planner.User;

public class UserData {

	private Set<User> allUsers = new HashSet<>();

	public Set<User> getAllUsers() {
		return allUsers;
	}

	public void addUserToUserData(User userToAdd) {
		this.allUsers.add(userToAdd);
	}

	public void removeUserFromUserData(User userToRemove) {
		this.allUsers.remove(userToRemove);
	}

}
