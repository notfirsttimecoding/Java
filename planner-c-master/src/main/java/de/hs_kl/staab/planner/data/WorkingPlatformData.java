package de.hs_kl.staab.planner.data;

import java.util.HashSet;
import java.util.Set;

import de.hs_kl.staab.planner.WorkingPlatform;

public class WorkingPlatformData {

	private Set<WorkingPlatform> allWorkingPlatforms = new HashSet<>();

	public Set<WorkingPlatform> getAllWorkingPlatforms() {
		return allWorkingPlatforms;
	}

	public void addWorkingPlatformToWorkingPlatformData(WorkingPlatform workingPlatformToAdd) {
		this.allWorkingPlatforms.add(workingPlatformToAdd);
	}

	public void removeWorkingPlatformFromWorkingPlatformData(WorkingPlatform workingPlatformToRemove) {
		this.allWorkingPlatforms.remove(workingPlatformToRemove);
	}
}
