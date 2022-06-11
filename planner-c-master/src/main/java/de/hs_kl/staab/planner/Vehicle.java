package de.hs_kl.staab.planner;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Vehicle {

	private final int vehicleYearOfConstruction;
	private final String vehicleBrand;
	private final String vehicleModel;
	private final LocalDateTime dateOfAdmission;

	private String vehicleLicensePlate;
	private Set<WorkingAppointment> historyOfWorks = new HashSet<>();

	public Vehicle(String vehicleBrand, String vehicleModel, int vehicleYearOfConstruction, String vehicleLicensePlate,
			LocalDateTime dateOfAdmission) {
		this.vehicleBrand = vehicleBrand;
		this.vehicleModel = vehicleModel;
		this.vehicleYearOfConstruction = vehicleYearOfConstruction;
		this.vehicleLicensePlate = vehicleLicensePlate;
		this.dateOfAdmission = dateOfAdmission;
	}

	@Override
	public String toString() {
		return "Vehicle license plate: " + vehicleLicensePlate + " (brand: " + vehicleBrand + ", model: " + vehicleModel
				+ ", year of construction: " + vehicleYearOfConstruction + ", date of admission: " + dateOfAdmission
				+ ")";
	}

	public String getVehicleLicensePlate() {
		return vehicleLicensePlate;
	}

	public void setVehicleLicensePlate(String carLicensePlate) {
		this.vehicleLicensePlate = carLicensePlate;
	}

	public String getVehicleBrand() {
		return vehicleBrand;
	}

	public String getVehicleModel() {
		return vehicleModel;
	}

	public int getVehicleYearOfConstruction() {
		return vehicleYearOfConstruction;
	}

	public LocalDateTime getDateOfAdmission() {
		return dateOfAdmission;
	}

	public Set<WorkingAppointment> getHistoryOfWorkingsAppointments() {
		return historyOfWorks;
	}

	public void addWorkToVehiclesHistoryOfWorkingAppointments(WorkingAppointment workingAppointmentToAdd) {
		this.historyOfWorks.add(workingAppointmentToAdd);
	}

	public void removeWorkFromVehiclesHistoryOfWorkingAppointments(
			WorkingAppointment workToRemoveFromVehiclesHistoryOfWorks) {
		this.historyOfWorks.remove(workToRemoveFromVehiclesHistoryOfWorks);
	}

}
