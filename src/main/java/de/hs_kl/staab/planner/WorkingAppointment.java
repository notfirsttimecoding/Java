package de.hs_kl.staab.planner;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class WorkingAppointment extends Appointment {

	private WorkingPlatform workingPlatformRelatedToWorkingAppointment;
	private Customer customerRelatedToWorkingAppointment;
	private Vehicle vehicleToWorkOn;
	private Set<Work> worksToPerform = new HashSet<>();
	private CarMechanicUser responsibleCarMechanicUser;
	private String status = "OPEN";
	private int durationOfAllWorks;

	public WorkingAppointment(Set<Work> worksToPerform, Customer customerRelatedToWorkingAppointment,
			Vehicle vehicleToWorkOn, WorkingPlatform workingPlatformRelatedToWorkingAppointment,
			LocalDateTime appointmentBeginDateAndTime, CarMechanicUser responsibleCarMechanicUser) {
		super(appointmentBeginDateAndTime);
		this.workingPlatformRelatedToWorkingAppointment = workingPlatformRelatedToWorkingAppointment;
		this.customerRelatedToWorkingAppointment = customerRelatedToWorkingAppointment;
		this.setWorksToPerform(worksToPerform);
		if (this.customerRelatedToWorkingAppointment.getVehiclesOfCustomer()
				.contains(vehicleToWorkOn)) {
			this.vehicleToWorkOn = vehicleToWorkOn;
			this.responsibleCarMechanicUser = responsibleCarMechanicUser;
		} else {
			throw new IllegalArgumentException(
					"ERROR: The vehicle is not in the set of cars of the customer! Please add the car to the set first using the addCarToCarSet(Vehicle carToAddToSet) method of the customer.");
		}

	}

	@Override
	public String toString() {
		return "On " + getAppointmentBeginDateAndTimeFormatted() + " following work/-s "
				+ this.getNamesOfWorksToPerform() + " on the car with following license plate "
				+ vehicleToWorkOn.getVehicleLicensePlate() + " on the working platform "
				+ workingPlatformRelatedToWorkingAppointment.getWorkingPlatformName() + " (ID: "
				+ workingPlatformRelatedToWorkingAppointment.getWorkingPlatformId() + ") have to be performed "
				+ "by car mechanic named " + responsibleCarMechanicUser.getUserFirstName() + " "
				+ responsibleCarMechanicUser.getUserLastName() + " (" + responsibleCarMechanicUser.getUsername() + ")."
				+ " The status of this working appointment is " + status + " (Appointment ID: " + appointmentId + ").";
	}

	public WorkingPlatform getWorkingPlatformRelatedToWorkingAppointment() {
		return workingPlatformRelatedToWorkingAppointment;
	}

	public void setWorkingPlatformRelatedToWorkingAppointment(
			WorkingPlatform workingPlatformRelatedToWorkingAppointment) {
		this.workingPlatformRelatedToWorkingAppointment = workingPlatformRelatedToWorkingAppointment;
	}

	public Customer getCustomerRelatedToWorkingAppointment() {
		return customerRelatedToWorkingAppointment;
	}

	public Vehicle getVehicleToWorkOn() {
		return vehicleToWorkOn;
	}

	public Set<Work> getWorksToPerform() {
		return worksToPerform;
	}

	public Set<String> getNamesOfWorksToPerform() {
		Set<String> namesOfWorksToPerform = new HashSet<>();
		worksToPerform.forEach(work -> namesOfWorksToPerform.add(work.getWorkName()));
		return namesOfWorksToPerform;
	}

	public void setWorksToPerform(Set<Work> worksToPerform) {
		this.worksToPerform.addAll(worksToPerform);
		this.setEndDateAndTime();
	}

	private void setEndDateAndTime() {
		worksToPerform.forEach(work -> durationOfAllWorks += (work.getWorkDuration()));
		this.appointmentEndDateAndTime = appointmentBeginDateAndTime.plusMinutes(durationOfAllWorks);
	}

	public CarMechanicUser getResponsibleCarMechanicUser() {
		return responsibleCarMechanicUser;
	}

	public void setResponsibleCarMechanicUser(CarMechanicUser responsibleCarMechanicUser) {
		this.responsibleCarMechanicUser = responsibleCarMechanicUser;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(WorkingAppointmentStatus newWorkingAppointmentStatus) {
		switch (newWorkingAppointmentStatus) {
		case OPEN:
			this.status = "OPEN";
			break;
		case CANCELLED:
			this.status = "CANCELLED";
			break;
		case FINISHED:
			this.status = "FINISHED";
			break;
		default:
			throw new IllegalArgumentException(
					"ERROR: Unspecified WorkingAppointment status! Use OPEN, FINISHED or CANCELLED instead.");

		}
	}

}
