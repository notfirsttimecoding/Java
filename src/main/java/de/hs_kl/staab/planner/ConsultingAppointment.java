package de.hs_kl.staab.planner;

import java.time.LocalDateTime;

public class ConsultingAppointment extends Appointment {

	private int consultingAppointmentDuration;
	private Customer customerRelatedToConsultingAppointment;
	private ClientAdvisorUser responsibleClientAdvisorUser;

	public ConsultingAppointment(Customer customerRelatedToConsultingAppointment, int consultingAppointmentDuration,
			LocalDateTime appointmentBeginDateAndTime, ClientAdvisorUser responsibleClientAdvisorUser) {
		super(appointmentBeginDateAndTime);
		this.setConsultingAppointmentDuration(consultingAppointmentDuration);
		this.customerRelatedToConsultingAppointment = customerRelatedToConsultingAppointment;
		this.responsibleClientAdvisorUser = responsibleClientAdvisorUser;

	}

	@Override
	public String toString() {
		return "On " + getAppointmentBeginDateAndTimeFormatted() + " there is a " + consultingAppointmentDuration
				+ "-minute consultation scheduled with client " + customerRelatedToConsultingAppointment.getFullName()
				+ " (Until: " + appointmentEndDateAndTime + ") " + "by client advisor named "
				+ responsibleClientAdvisorUser.getUserFirstName() + " " + responsibleClientAdvisorUser.getUserLastName()
				+ " (" + responsibleClientAdvisorUser.getUsername() + ") ( Appointments ID: " + appointmentId + ").";

	}

	public int getConsultingAppointmentDuration() {
		return consultingAppointmentDuration;
	}

	public void setConsultingAppointmentDuration(int consultingAppointmentDuration) {
		this.consultingAppointmentDuration = consultingAppointmentDuration;
		this.appointmentEndDateAndTime = this.appointmentBeginDateAndTime.plusMinutes(consultingAppointmentDuration);
	}

	public Customer getCustomerRelatedToConsultingAppointment() {
		return customerRelatedToConsultingAppointment;
	}

	public ClientAdvisorUser getResponsibleClientAdvisorUser() {
		return responsibleClientAdvisorUser;
	}

	public void setResponsibleClientAdvisorUser(ClientAdvisorUser responsibleClientAdvisorUser) {
		this.responsibleClientAdvisorUser = responsibleClientAdvisorUser;
	}
}
