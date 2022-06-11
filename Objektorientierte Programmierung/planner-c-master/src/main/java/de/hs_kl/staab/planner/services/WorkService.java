package de.hs_kl.staab.planner.services;

import java.util.Optional;
import java.util.Set;

import de.hs_kl.staab.planner.Work;
import de.hs_kl.staab.planner.data.WorkData;

public class WorkService {

	private static WorkService WORK_SERVICE;
	private final static WorkData WORK_DATA = new WorkData();

	/**
	 * Singleton-Pattern: Der Konstruktor kann nicht aufgerufen werden, sondern
	 * {@link #getInstance()} muss aufgerufen werden. So kann sichergestellt werden,
	 * dass nur eine einzige Instanz dieser Klasse erstellt wird.
	 */

	private WorkService() {
	}

	/**
	 * Teil des Singleton-Patterns
	 * 
	 * @return Die einzige Instanz des WorkService.
	 */
	public static WorkService getInstance() {
		if (WORK_SERVICE == null) {
			WORK_SERVICE = new WorkService();
		}
		return WORK_SERVICE;
	}

	public Set<Work> getAllWorks() {
		return WORK_DATA.getAllWorks();
	}

	private boolean checkIfWorkDuplicateAlreadyExists(String workName, int workDurationMinutes) {
		Set<Work> allWorks = WORK_DATA.getAllWorks();
		Boolean workNameAndWorkDurationAlreadyExist = false;
		for (Work work : allWorks) {
			if (work.getWorkName().equals(workName) && work.getWorkDuration() == workDurationMinutes) {
				workNameAndWorkDurationAlreadyExist = true;
				break;
			}
		}
		return workNameAndWorkDurationAlreadyExist;
	}

	public void createAndAddNewWork(String workName, int workDurationMinutes) {
		if (!checkIfWorkDuplicateAlreadyExists(workName, workDurationMinutes)) {
			WORK_DATA.addWorkToWorkData(new Work(workName, workDurationMinutes));
		} else
			System.err.println(
					"ERROR: A work with the same name(" + workName + ") and the same duration (" + workDurationMinutes
							+ ") already exists. Please choose a different name or duration! No new work added!");
	}

	public Optional<Work> getWorkById(String workId) {
		Set<Work> allWorks = WORK_DATA.getAllWorks();
		Work foundWork = null;
		for (Work work : allWorks) {
			if (work.getWorkId().equals(workId)) {
				foundWork = work;
				break;
			}
		}
		return Optional.ofNullable(foundWork);
	}

	public void updateWorkName(String workIdOfWorkToUpdate, String newWorkName) {
		Optional<Work> optionalWorkToUpdate = getWorkById(workIdOfWorkToUpdate);
		if (optionalWorkToUpdate.isPresent()) {
			optionalWorkToUpdate.get().setWorkName(newWorkName);
		} else {
			System.err.println("ERROR: Work with the ID: " + workIdOfWorkToUpdate
					+ " was not found. Use getAllWorks() to see all the works with their ID.");
		}
	}

	public void updateWorkDuration(String workIdOfWorkToUpdate, int newWorkDurationMinutes) {
		Optional<Work> optionalWorkToUpdate = getWorkById(workIdOfWorkToUpdate);
		if (optionalWorkToUpdate.isPresent()) {
			optionalWorkToUpdate.get().setWorkDuration(newWorkDurationMinutes);
		} else {
			System.err.println("ERROR: Work with the ID: " + workIdOfWorkToUpdate
					+ " was not found. Use getAllWorks() to see all the works with their ID.");
		}
	}

	public void removeWork(String workIdOfWorkToRemove) {
		Optional<Work> optionalWorkToRemove = getWorkById(workIdOfWorkToRemove);
		if (optionalWorkToRemove.isPresent()) {
			WORK_DATA.removeWorkFromWorkData(optionalWorkToRemove.get());
		} else {
			System.err.println("ERROR: The work with the ID: " + workIdOfWorkToRemove
					+ " does not exist in the WORK_DATA. Try a different ID or use getAllWorks() to see all the works with their ID.");
		}
	}

}
