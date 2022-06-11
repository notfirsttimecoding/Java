
package de.hs_kl.staab.planner;

import java.time.LocalDateTime;

public abstract class Appointment {

	private static int APPOINTMENT_ID = 1;

	private static final String APPOINTMENT_PREFIX = "A-";
	protected final String appointmentId;

	protected LocalDateTime appointmentBeginDateAndTime;
	protected LocalDateTime appointmentEndDateAndTime;

	protected Appointment(LocalDateTime appointmentBeginDateAndTime) {
		this.appointmentBeginDateAndTime = appointmentBeginDateAndTime;
		appointmentId = APPOINTMENT_PREFIX + APPOINTMENT_ID++;
	}

	public String getAppointmentId() {
		return appointmentId;
	}

	public LocalDateTime getAppointmentBeginDateAndTime() {
		return appointmentBeginDateAndTime;
	}

	public String getAppointmentBeginDateAndTimeFormatted() {
		return appointmentBeginDateAndTime.getDayOfMonth() + "." + appointmentBeginDateAndTime.getMonthValue() + "."
				+ appointmentBeginDateAndTime.getYear() + " at " + appointmentBeginDateAndTime.getHour() + ":"
				+ appointmentBeginDateAndTime.getMinute();
	}

	public void setAppointmentBeginDateAndTime(LocalDateTime appointmentBeginDateAndTime) {
		this.appointmentBeginDateAndTime = appointmentBeginDateAndTime;
	}

	public LocalDateTime getAppointmentEndDateAndTime() {
		return appointmentEndDateAndTime;
	}
}
