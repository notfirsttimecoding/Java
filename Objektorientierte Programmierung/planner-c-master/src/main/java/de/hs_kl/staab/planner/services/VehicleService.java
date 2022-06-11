package de.hs_kl.staab.planner.services;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.hs_kl.staab.planner.AppointmentComparator;
import de.hs_kl.staab.planner.Vehicle;
import de.hs_kl.staab.planner.WorkingAppointment;
import de.hs_kl.staab.planner.data.VehicleData;

public class VehicleService {

	private static VehicleService VEHICLE_SERVICE;
	private final static VehicleData VEHICLE_DATA = new VehicleData();
	private final static PlannerService PLANNER_SERVICE = PlannerService.getInstance();

	/**
	 * Singleton-Pattern: Der Konstruktor kann nicht aufgerufen werden, sondern
	 * {@link #getInstance()} muss aufgerufen werden. So kann sichergestellt werden,
	 * dass nur eine einzige Instanz dieser Klasse erstellt wird.
	 */
	private VehicleService() {
	}

	/**
	 * Teil des Singleton-Patterns
	 * 
	 * @return Die einzige Instanz des VehicleService.
	 */
	public static VehicleService getInstance() {
		if (VEHICLE_SERVICE == null) {
			VEHICLE_SERVICE = new VehicleService();
		}
		return VEHICLE_SERVICE;
	}

	public Set<Vehicle> getAllVehicles() {
		return VEHICLE_DATA.getAllVehicles();
	}

	private boolean checkIfVehicleLicensePlateAlreadyExists(String carLicensePlate) {
		Set<Vehicle> allVehicles = VEHICLE_DATA.getAllVehicles();
		Boolean licensePlateAlreadyUsed = false;
		for (Vehicle vehicle : allVehicles) {
			if (vehicle.getVehicleLicensePlate()
					.equals(carLicensePlate)) {
				licensePlateAlreadyUsed = true;
				break;
			}
		}
		return licensePlateAlreadyUsed;
	}

	public void createAndAddNewVehicle(String carBrand, String carModel, int carYearOfConstruction,
			String carLicensePlate, LocalDateTime dateOfAdmission) {
		if (!checkIfVehicleLicensePlateAlreadyExists(carLicensePlate)) {
			VEHICLE_DATA.addVehicleToVehicleData(
					new Vehicle(carBrand, carModel, carYearOfConstruction, carLicensePlate, dateOfAdmission));
		} else {
			System.err
					.println("ERROR: The license plate (" + carLicensePlate + ") is already used on an other vehicle.");
		}
	}

	public Optional<Vehicle> getVehicleByLicensePlate(String vehicleLicensePlate) {
		Set<Vehicle> allVehicles = VEHICLE_DATA.getAllVehicles();
		Vehicle foundVehicle = null;
		for (Vehicle vehicle : allVehicles) {
			if (vehicle.getVehicleLicensePlate()
					.equals(vehicleLicensePlate)) {
				foundVehicle = vehicle;
				break;
			}
		}
		return Optional.ofNullable(foundVehicle);
	}

	public void updateVehicleLicensePlate(String vehicleLicensePlateOfVehicleToUpdate, String newCarLicensePlate) {
		Optional<Vehicle> optionalVehicleToUpdate = getVehicleByLicensePlate(vehicleLicensePlateOfVehicleToUpdate);
		if (optionalVehicleToUpdate.isPresent()) {
			if (!checkIfVehicleLicensePlateAlreadyExists(newCarLicensePlate)) {
				optionalVehicleToUpdate.get()
						.setVehicleLicensePlate(newCarLicensePlate);
			} else {
				System.err.println(
						"ERROR: The license plate you wanted to choose already belongs to a vehicle. Please choose a different license plate!");
			}
		} else {
			System.err.println("ERROR: Vehicle with the license plate: " + vehicleLicensePlateOfVehicleToUpdate
					+ " was not found. Use getAllVehicles() to see all the vehicles with their license plate.");
		}
	}

	public void removeVehicle(String vehicleLicensePlateOfVehicleToRemove) {
		Optional<Vehicle> optionalVehicleToRemove = getVehicleByLicensePlate(vehicleLicensePlateOfVehicleToRemove);
		if (optionalVehicleToRemove.isPresent()) {
			VEHICLE_DATA.removeVehicleFromVehicleData(optionalVehicleToRemove.get());
		} else {
			System.err.println("ERROR: The vehicle with the license plate: " + vehicleLicensePlateOfVehicleToRemove
					+ " does not exist in the VEHICLE_DATA. Try a different license plate or use getAllVehicles() to see all the vehicles with their license plate!");
		}
	}

	/**
	 * Adds working appointments with status "finished" to the history of works
	 * matching to the given license plate of vehicle. If no working appointment or
	 * if no finished working appointment have been found, an error message will be
	 * displayed.
	 * 
	 * @param vehicleLicensePlateToAddWorkingAppointmentTo - String of the vehicles
	 *                                                     license plate for which
	 *                                                     you want to have history
	 *                                                     of works for
	 */
	private void addFinishedWorkingAppointmentsToVehiclesHistoryOfWorks(
			String vehicleLicensePlateToAddWorkingAppointmentTo) {
		Optional<Vehicle> optionalVehicleToAddWorkingAppointmentTo = getVehicleByLicensePlate(
				vehicleLicensePlateToAddWorkingAppointmentTo);
		boolean workingAppointmentExists = false;
		boolean finishedWorkingAppointmentExists = false;
		if (optionalVehicleToAddWorkingAppointmentTo.isPresent()) {
			Vehicle vehicleToAddWorkingAppointmentTo = optionalVehicleToAddWorkingAppointmentTo.get();
			Set<WorkingAppointment> workingAppointmentsToAdd = PLANNER_SERVICE.getAllWorkingAppointments();
			for (WorkingAppointment workingAppointment : workingAppointmentsToAdd) {
				if (workingAppointment.getVehicleToWorkOn()
						.equals(vehicleToAddWorkingAppointmentTo)) {
					if (!workingAppointmentsToAdd.isEmpty()) {
						workingAppointmentExists = true;
						if (workingAppointment.getStatus()
								.equals("FINISHED")) {
							finishedWorkingAppointmentExists = true;
							if (!vehicleToAddWorkingAppointmentTo.getHistoryOfWorkingsAppointments()
									.contains(workingAppointment)) {
								vehicleToAddWorkingAppointmentTo
										.addWorkToVehiclesHistoryOfWorkingAppointments(workingAppointment);

							}
						}
					}

				}
			}

			if (!workingAppointmentExists)
				System.err.println("There are no working appointments on the vehicle with following license plate: "
						+ vehicleLicensePlateToAddWorkingAppointmentTo + ".");
			else if (!finishedWorkingAppointmentExists) {
				System.err.println(
						"There are no finished working appointments on the vehicle with following license plate: "
								+ vehicleLicensePlateToAddWorkingAppointmentTo + ".");
			}

		}
	}

	/**
	 * Returns a set of all the finished working appointments for a given vehicle's
	 * license plate. If set is empty or if the given license plate does not exist,
	 * throws an Exception.
	 * 
	 * @param vehicleLicensePlate - String of the vehicles license plate for which
	 *                            you want to have history of works for
	 * @return - Set of all finished working appointments
	 */
	public Set<WorkingAppointment> getHistoryOfFinishedWorkingAppointments(String vehicleLicensePlate) {
		addFinishedWorkingAppointmentsToVehiclesHistoryOfWorks(vehicleLicensePlate);
		Optional<Vehicle> optionalVehicle = getVehicleByLicensePlate(vehicleLicensePlate);
		if (optionalVehicle.isPresent()) {
			return optionalVehicle.get()
					.getHistoryOfWorkingsAppointments();
		} else {
			throw new IllegalArgumentException("ERROR: Vehicle with the license plate " + vehicleLicensePlate
					+ " was not found. Use getAllVehicles() to see all the vehicles with their license plates.");
		}
	}

	/**
	 * Prints a SORTED ({@link AppointmentComparator}) set of all the finished
	 * working appointments for a given vehicle's license plate. It is in a clearly
	 * arranged format.
	 * 
	 * @param vehicleLicensePlate - String of the vehicles license plate for which
	 *                            you want to have history of works for
	 */

	public void printHistoryOfFinishedWorkingAppointments(String vehicleLicensePlate) {
		Set<WorkingAppointment> historyOfWorks = this.getHistoryOfFinishedWorkingAppointments(vehicleLicensePlate);
		Set<WorkingAppointment> sortedAppointments = new LinkedHashSet<>(historyOfWorks.size());
		sortedAppointments = historyOfWorks.stream()
				.sorted(new AppointmentComparator())
				.collect(Collectors.toCollection(LinkedHashSet::new));
		if (!sortedAppointments.isEmpty()) {
			System.out.println("The following work-/s was/were performed on the vehicle with the license plate " + "'"
					+ vehicleLicensePlate + "':");
		}
		for (WorkingAppointment workingAppointment : sortedAppointments) {
			System.out.println("On " + workingAppointment.getAppointmentBeginDateAndTimeFormatted() + " "
					+ workingAppointment.getNamesOfWorksToPerform() + " have been performed" + " by "
					+ workingAppointment.getResponsibleCarMechanicUser()
							.getUserFirstName()
					+ " " + workingAppointment.getResponsibleCarMechanicUser()
							.getUserLastName()
					+ " (" + workingAppointment.getResponsibleCarMechanicUser()
							.getUsername()
					+ ").");
		}
	}
}