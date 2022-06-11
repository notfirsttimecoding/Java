package de.hs_kl.staab.planner.data;

/* ******************************************************** */
/* * HIER KÖNNEN SIE IHREN PLANUNGSKALENDER PROGRAMMIEREN * */
/* * Der Planungskalender enthält den Kern ************** * */
/* * Ihrer Anwendung, und hält die Daten des Kalenders ** * */
/* * mit den Terminen, etc. ***************************** * */
/* ******************************************************** */

import java.util.HashSet;
import java.util.Set;

import de.hs_kl.staab.planner.Appointment;

public class PlanningCalendarData {

	private Set<Appointment> allAppointments = new HashSet<>();

	public Set<Appointment> getAllAppointments() {
		return allAppointments;
	}

	public void addAppointmentToPlanningCalendar(Appointment appointmentToAdd) {
		this.allAppointments.add(appointmentToAdd);
	}

	public void removeAppointmentFromAppointmentData(Appointment appointmentToRemove) {
		this.allAppointments.remove(appointmentToRemove);
	}
}
