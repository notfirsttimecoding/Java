package de.hs_kl.staab.planner;

import java.time.LocalDateTime;

public class CleaningAppointment extends Appointment {

	private static final int DURATION_QUICK_CLEANING_APPOINTMENT = 30;
	private static final int DURATION_INTENSIVE_CLEANING_APPOINTMENT = 60;

	private int cleaningDuration;
	private String cleaningAppointmentType;
	private DispatcherUser responsibleDispatcherUser;

	private WorkingPlatform workingPlatformRelatedToCleaningAppointment;

	public CleaningAppointment(CleaningAppointmentType cleaningAppointmentType,
			WorkingPlatform workingPlatformRelatedToCleaningAppointment, LocalDateTime appointmentBeginDateAndTime,
			DispatcherUser responsibleDispatcherUser) {
		super(appointmentBeginDateAndTime);
		this.workingPlatformRelatedToCleaningAppointment = workingPlatformRelatedToCleaningAppointment;
		this.setCleaningAppointmentType(cleaningAppointmentType);
		this.responsibleDispatcherUser = responsibleDispatcherUser;

	}

	@Override
	public String toString() {
		return "On " + getAppointmentBeginDateAndTimeFormatted() + " working platform "
				+ workingPlatformRelatedToCleaningAppointment.getWorkingPlatformName() + " (ID: "
				+ workingPlatformRelatedToCleaningAppointment.getWorkingPlatformId() + ") is scheduled to be cleaned "
				+ cleaningAppointmentType + "(" + cleaningDuration + " minutes)" + " (Until: "
				+ appointmentEndDateAndTime + ") " + "by  dispatcher named "
				+ responsibleDispatcherUser.getUserFirstName() + " " + responsibleDispatcherUser.getUserLastName()
				+ " (" + responsibleDispatcherUser.getUsername() + ") (Appointments ID: " + appointmentId + ").";
	}

	public int getCleaningDuration() {
		return cleaningDuration;
	}

	public void setCleaningAppointmentType(CleaningAppointmentType newCleaningAppointmentType) {
		switch (newCleaningAppointmentType) {
		case QUICK:
			this.cleaningAppointmentType = "Quick";
			this.cleaningDuration = DURATION_QUICK_CLEANING_APPOINTMENT;
			this.appointmentEndDateAndTime = this.appointmentBeginDateAndTime.plusMinutes(cleaningDuration);
			break;
		case INTENSIVE:
			this.cleaningAppointmentType = "Intensive";
			this.cleaningDuration = DURATION_INTENSIVE_CLEANING_APPOINTMENT;
			this.appointmentEndDateAndTime = this.appointmentBeginDateAndTime.plusMinutes(cleaningDuration);
			break;
		default:
			throw new IllegalArgumentException("ERROR: Unspecified cleaning type! Use QUICK or INTENSIVE instead.");

		}
	}

	public String getCleaningAppointmentType() {
		return cleaningAppointmentType;
	}

	public WorkingPlatform getWorkingPlatformRelatedToCleaningAppointment() {
		return workingPlatformRelatedToCleaningAppointment;
	}

	public void setWorkingPlatformRelatedToCleaningAppointment(
			WorkingPlatform workingPlatformRelatedToCleaningAppointment) {
		this.workingPlatformRelatedToCleaningAppointment = workingPlatformRelatedToCleaningAppointment;
	}

	public DispatcherUser getResponsibleDispatcherUser() {
		return responsibleDispatcherUser;
	}

	public void setResponsibleDispatcherUser(DispatcherUser responsibleDispatcherUser) {
		this.responsibleDispatcherUser = responsibleDispatcherUser;
	}
}
