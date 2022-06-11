package de.hs_kl.staab.planner;

import java.util.Comparator;

public class AppointmentComparator implements Comparator<Appointment> {
	/**
	 * APPOINTMENT COMPARATOR. Sorts given set of class Appointment by stored date
	 * of each appointment. The earliest appointments come first and the oldest ones
	 * come at the end of the set.
	 * 
	 */
	public int compare(Appointment o1, Appointment o2) {
		if (o1 == null) {
			if (o2 == null) {
				return 0;
			}

			return -1;
		}
		if (o2 == null) {
			return 1;
		}
		return o1.getAppointmentBeginDateAndTime()
				.compareTo(o2.getAppointmentBeginDateAndTime());
	}

}