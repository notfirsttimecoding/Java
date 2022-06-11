package de.hs_kl.staab.planner;

public class Work {

	private static int WORK_ID_COUNTER = 1;
	private static final String WORK_ID_PREFIX = "W-";

	private final String workId;

	private String workName;
	private int workDurationMinutes;

	public Work(String workName, int workDurationMinutes) {
		this.workId = WORK_ID_PREFIX + WORK_ID_COUNTER++;
		this.workName = workName;
		this.workDurationMinutes = workDurationMinutes;

	}

	@Override
	public String toString() {
		return "Work ID: " + workId + " (name: " + workName + ", duration: " + workDurationMinutes + "min)";
	}

	public int getWorkDuration() {
		return workDurationMinutes;
	}

	public void setWorkDuration(int newWorkDuration) {
		this.workDurationMinutes = newWorkDuration;
	}

	public String getWorkId() {
		return workId;
	}

	public String getWorkName() {
		return workName;
	}

	public void setWorkName(String newWorkName) {
		this.workName = newWorkName;
	}

}
