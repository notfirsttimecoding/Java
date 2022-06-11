package de.hs_kl.staab.planner.services;

import java.util.Optional;
import java.util.Set;

import de.hs_kl.staab.planner.CarMechanicUser;
import de.hs_kl.staab.planner.ClientAdvisorUser;
import de.hs_kl.staab.planner.DispatcherUser;
import de.hs_kl.staab.planner.User;
import de.hs_kl.staab.planner.data.UserData;

public class UserService {
	private static UserService USER_SERVICE;
	private final static UserData USER_DATA = new UserData();

	private UserService() {
	}

	public static UserService getInstance() {
		if (USER_SERVICE == null) {
			USER_SERVICE = new UserService();
		}
		return USER_SERVICE;
	}

	public Set<User> getAllUsers() {
		return USER_DATA.getAllUsers();
	}

	private boolean checkIfUsernameDuplicateAlreadyExists(String userName) {
		Set<User> allUsers = USER_DATA.getAllUsers();
		Boolean userDetailsAlreadyExist = false;
		for (User user : allUsers) {
			if (user.getUsername()
					.equals(userName)) {
				userDetailsAlreadyExist = true;
				break;
			}
		}
		return userDetailsAlreadyExist;
	}

	public void createAndAddNewDispatcherUser(String userName, String userFirstName, String userLastName) {
		if (!checkIfUsernameDuplicateAlreadyExists(userName)) {
			USER_DATA.addUserToUserData(new DispatcherUser(userName, userFirstName, userLastName));
		} else {
			System.err.println(
					"ERROR: Dispatcher-User with the same username already exists. Please choose a different username or check if the user you want to create already exists by using getAllUsers()");
		}
	}

	public void createAndAddNewClientadvisorUser(String userName, String userFirstName, String userLastName) {
		if (!checkIfUsernameDuplicateAlreadyExists(userName)) {
			USER_DATA.addUserToUserData(new ClientAdvisorUser(userName, userFirstName, userLastName));
		} else {
			System.err.println(
					"ERROR: Clientadvisor-User with the same username already exists. Please choose a different username or check if the user you want to create already exists by using getAllUsers()");
		}
	}

	public void createAndAddNewCarMechanicUser(String userName, String userFirstName, String userLastName) {
		if (!checkIfUsernameDuplicateAlreadyExists(userName)) {
			USER_DATA.addUserToUserData(new CarMechanicUser(userName, userFirstName, userLastName));
		} else {
			System.err.println(
					"ERROR: Carmechanic-User with the same username  already exists. Please choose a different username or check if the user you want to create already exists by using getAllUsers()");
		}
	}

	public Optional<User> getUserByUsername(String userName) {
		Set<User> allUsers = USER_DATA.getAllUsers();
		User foundUser = null;
		for (User user : allUsers) {
			if (user.getUsername()
					.equals(userName)) {
				foundUser = user;
				break;
			}
		}
		return Optional.ofNullable(foundUser);
	}

	public void updateNameOfUser(String userNameOfUserToUpdate, String newUserFirstName, String newUserLastName) {
		Optional<User> optionalUserToUpdate = getUserByUsername(userNameOfUserToUpdate);
		if (optionalUserToUpdate.isPresent()) {
			User userToUpdate = optionalUserToUpdate.get();
			userToUpdate.setUserFirstName(newUserFirstName);
			userToUpdate.setUserLastName(newUserLastName);
		} else {
			System.err.println("ERROR: User with the Username: " + userNameOfUserToUpdate
					+ " was not found. Use getAllUsers() to see all the users with their Username.");
		}
	}

	public void updateUserNameOfUser(String userNameOfUserToUpdate, String newUserName) {
		Optional<User> optionalUserToUpdate = getUserByUsername(userNameOfUserToUpdate);
		if (optionalUserToUpdate.isPresent()) {
			if (!checkIfUsernameDuplicateAlreadyExists(newUserName)) {
				optionalUserToUpdate.get()
						.setUserName(newUserName);
			} else {
				System.err.println("ERROR: The username you wanted to use is already used. Choose a different one.");
			}
		} else {
			System.err.println("ERROR: User with the username: " + userNameOfUserToUpdate
					+ " was not found. Use getAllUsers() to see all the users with their username.");
		}
	}

	public void removeUser(String userIdOfUserToRemove) {
		Optional<User> optionalUserToRemove = getUserByUsername(userIdOfUserToRemove);
		if (optionalUserToRemove.isPresent()) {
			USER_DATA.removeUserFromUserData(optionalUserToRemove.get());
		} else {
			System.err.println("ERROR: The User with the Username: " + userIdOfUserToRemove
					+ " does not exist in the UserData. Try a different ID or use getAllUsers() to see all the users with their Username.");
		}
	}
}
