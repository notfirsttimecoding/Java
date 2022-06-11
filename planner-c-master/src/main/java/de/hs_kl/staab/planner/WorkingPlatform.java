package de.hs_kl.staab.planner;

public class WorkingPlatform {

	private static int WORKING_PLATFORM_ID_COUNTER = 1;
	private static final String WORKING_PLATFORM_PREFIX = "WP-";

	private final String workingPlatformId;

	private String workingPlatformName;

	public WorkingPlatform(String workingPlatformName) {
		this.workingPlatformId = WORKING_PLATFORM_PREFIX + WORKING_PLATFORM_ID_COUNTER++;
		this.workingPlatformName = workingPlatformName;
	}

	@Override
	public String toString() {
		return "WorkingPlatform ID: " + workingPlatformId + " (name: " + workingPlatformName + ")";
	}

	public String getWorkingPlatformId() {
		return workingPlatformId;
	}

	public String getWorkingPlatformName() {
		return workingPlatformName;
	}

	public void setWorkingPlatformName(String newWorkingPlatformName) {
		this.workingPlatformName = newWorkingPlatformName;
	}
}
