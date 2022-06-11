package de.hs_kl.staab.planner.services;

import java.util.Optional;
import java.util.Set;

import de.hs_kl.staab.planner.WorkingPlatform;
import de.hs_kl.staab.planner.data.WorkingPlatformData;

public class WorkingPlatformService {

	private static WorkingPlatformService WORKING_PLATFORM_SERVICE;
	private final static WorkingPlatformData WORKING_PLATFORM_DATA = new WorkingPlatformData();

	/**
	 * Singleton-Pattern: Der Konstruktor kann nicht aufgerufen werden, sondern
	 * {@link #getInstance()} muss aufgerufen werden. So kann sichergestellt werden,
	 * dass nur eine einzige Instanz dieser Klasse erstellt wird.
	 */
	private WorkingPlatformService() {
	}

	/**
	 * Teil des Singleton-Patterns
	 * 
	 * @return Die einzige Instanz des WorkingPlatformService.
	 */
	public static WorkingPlatformService getInstance() {
		if (WORKING_PLATFORM_SERVICE == null) {
			WORKING_PLATFORM_SERVICE = new WorkingPlatformService();
		}
		return WORKING_PLATFORM_SERVICE;
	}

	public Set<WorkingPlatform> getAllWorkingPlatforms() {
		return WORKING_PLATFORM_DATA.getAllWorkingPlatforms();
	}

	private boolean checkIfWorkingPlatformDuplicateAlreadyExists(String workingPlatformName) {
		Set<WorkingPlatform> allWorkingPlatforms = WORKING_PLATFORM_DATA.getAllWorkingPlatforms();
		Boolean workingPlatformNameAlreadyExists = false;
		for (WorkingPlatform workingPlatform : allWorkingPlatforms) {
			if (workingPlatform.getWorkingPlatformName()
					.equals(workingPlatformName)) {
				workingPlatformNameAlreadyExists = true;
				break;
			}
		}
		return workingPlatformNameAlreadyExists;
	}

	public void createAndAddNewWorkingPlatform(String workingPlatformName) {
		if (!checkIfWorkingPlatformDuplicateAlreadyExists(workingPlatformName)) {
			WORKING_PLATFORM_DATA.addWorkingPlatformToWorkingPlatformData(new WorkingPlatform(workingPlatformName));
		} else {
			System.err.println("ERROR: A working platform with the same name(" + workingPlatformName
					+ ") already exists. Please choose a different name! No new working platform added!");
		}
	}

	public Optional<WorkingPlatform> getWorkingPlatformById(String workingPlatformId) {
		Set<WorkingPlatform> allWorkingPlatforms = WORKING_PLATFORM_DATA.getAllWorkingPlatforms();
		WorkingPlatform foundWorkingPlatform = null;
		for (WorkingPlatform workingPlatform : allWorkingPlatforms) {
			if (workingPlatform.getWorkingPlatformId()
					.equals(workingPlatformId)) {
				foundWorkingPlatform = workingPlatform;
				break;
			}
		}
		return Optional.ofNullable(foundWorkingPlatform);
	}

	public void updateWorkingPlatformName(String workingPlatformIdOfPlatformToUpdate, String newWorkingPlatformName) {
		Optional<WorkingPlatform> optionalWorkingPlatform = getWorkingPlatformById(workingPlatformIdOfPlatformToUpdate);
		if (optionalWorkingPlatform.isPresent()) {
			if (!checkIfWorkingPlatformDuplicateAlreadyExists(newWorkingPlatformName)) {
				optionalWorkingPlatform.get()
						.setWorkingPlatformName(newWorkingPlatformName);
			} else {
				System.err.println("ERROR: A working platform with the same name(" + newWorkingPlatformName
						+ ") already exists. Please choose a different name!");
			}
		} else {
			System.err.println("ERROR: WorkingPlatform with the ID: " + workingPlatformIdOfPlatformToUpdate
					+ " was not found. Use getAllWorkingPlatforms() to see all the working platforms with their ID.");
		}
	}

	public void removeWorkingPlatform(String workingPlatformIdToRemove) {
		Optional<WorkingPlatform> optionalWorkingPlatformToRemove = getWorkingPlatformById(workingPlatformIdToRemove);
		if (optionalWorkingPlatformToRemove.isPresent()) {
			WORKING_PLATFORM_DATA.removeWorkingPlatformFromWorkingPlatformData(optionalWorkingPlatformToRemove.get());
		} else {
			System.err.println("ERROR: The working platform with the ID: " + workingPlatformIdToRemove
					+ " does not exist in the WORKING_PLATFORM_DATA. Try a different ID or use getAllWorkingPlatforms() to see all the working platforms with their ID.");
		}
	}

}
