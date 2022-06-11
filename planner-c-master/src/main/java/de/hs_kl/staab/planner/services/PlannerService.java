package de.hs_kl.staab.planner.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.hs_kl.staab.planner.Appointment;
import de.hs_kl.staab.planner.AppointmentComparator;
import de.hs_kl.staab.planner.CarMechanicUser;
import de.hs_kl.staab.planner.CleaningAppointment;
import de.hs_kl.staab.planner.CleaningAppointmentType;
import de.hs_kl.staab.planner.ClientAdvisorUser;
import de.hs_kl.staab.planner.ConsultingAppointment;
import de.hs_kl.staab.planner.Customer;
import de.hs_kl.staab.planner.DispatcherUser;
import de.hs_kl.staab.planner.User;
import de.hs_kl.staab.planner.Vehicle;
import de.hs_kl.staab.planner.Work;
import de.hs_kl.staab.planner.WorkingAppointment;
import de.hs_kl.staab.planner.WorkingAppointmentStatus;
import de.hs_kl.staab.planner.WorkingPlatform;
import de.hs_kl.staab.planner.data.PlanningCalendarData;

/**
 * Verwaltet den Planungskalender
 */

public class PlannerService {

	private static PlannerService PLANNER_SERVICE;
	private final static PlanningCalendarData PLANNING_CALENDAR_DATA = new PlanningCalendarData();
	private final static CustomerService CUSTOMER_Service = CustomerService.getInstance();
	private final static WorkingPlatformService PLATFORM_SERVICE = WorkingPlatformService.getInstance();
	private final static VehicleService VEHICLE_SERVICE = VehicleService.getInstance();
	private final static WorkService WORK_SERVICE = WorkService.getInstance();
	private final static UserService USER_SERVICE = UserService.getInstance();

	/**
	 * Singleton-Pattern: Der Konstruktor kann nicht aufgerufen werden, sondern
	 * {@link #getInstance()} muss aufgerufen werden. So kann sichergestellt werden,
	 * dass nur eine einzige Instanz dieser Klasse erstellt wird.
	 */

	public PlannerService() {
	}

	/**
	 * Teil des Singleton-Patterns
	 * 
	 * @return Die einzige Instanz des PlannerService.
	 */
	public static PlannerService getInstance() {
		if (PLANNER_SERVICE == null) {
			PLANNER_SERVICE = new PlannerService();

		}
		return PLANNER_SERVICE;
	}

	public Set<Appointment> getAllAppointments() {
		return PLANNING_CALENDAR_DATA.getAllAppointments();
	}

	public void printAppointmentsDetailedFor(Set<Appointment> appointments) {
		appointments.forEach(appointment -> System.out.println(appointment));
	}

	public void printAllAppointmentsDetailed() {
		this.printAppointmentsDetailedFor(PLANNING_CALENDAR_DATA.getAllAppointments());
	}

	public Set<WorkingAppointment> getAllWorkingAppointments() {
		Set<Appointment> allAppointments = PLANNING_CALENDAR_DATA.getAllAppointments();
		Set<WorkingAppointment> allWorkingAppointments = new HashSet<>();
		for (Appointment appointment : allAppointments) {
			if (appointment instanceof WorkingAppointment) {
				allWorkingAppointments.add((WorkingAppointment) appointment);
			}
		}
		return allWorkingAppointments;
	}

	public Set<ConsultingAppointment> getAllConsultingAppointments() {
		Set<Appointment> allAppointments = PLANNING_CALENDAR_DATA.getAllAppointments();
		Set<ConsultingAppointment> allConsultingAppointments = new HashSet<>();
		for (Appointment appointment : allAppointments) {
			if (appointment instanceof ConsultingAppointment) {
				allConsultingAppointments.add((ConsultingAppointment) appointment);
			}
		}
		return allConsultingAppointments;
	}

	public Set<CleaningAppointment> getAllCleaningAppointments() {
		Set<Appointment> allAppointments = PLANNING_CALENDAR_DATA.getAllAppointments();
		Set<CleaningAppointment> allCleaningAppointments = new HashSet<>();
		for (Appointment appointment : allAppointments) {
			if (appointment instanceof CleaningAppointment) {
				allCleaningAppointments.add((CleaningAppointment) appointment);
			}
		}
		return allCleaningAppointments;
	}

	/**
	 * MLP020
	 * 
	 * This method intercepts that appointments overlap and its purpose is to check
	 * if a given working appointment is available. If check fails, the method
	 * returns a negative value of boolean and prints an ERORR message.
	 * 
	 * 
	 * 
	 * @param desiredWorksToPerform               - A set<Work> of works to perform
	 * @param desiredWorkingPlatform              - Desired working platform of
	 *                                            WorkingPlatform
	 * @param beginOfNewDesiredWorkingAppointment - LocalDateTime value of begin of
	 *                                            desired working appointment
	 * @param desiredResponsibleCarMechanicUser   - A username of CarMechanicUser
	 *                                            performing the work
	 * 
	 * @return - Boolean value if working appointment is available
	 */
	private boolean checkIfWorkingAppointmentIsAvailable(Set<Work> desiredWorksToPerform,
			WorkingPlatform desiredWorkingPlatform, LocalDateTime beginOfNewDesiredWorkingAppointment,
			CarMechanicUser desiredResponsibleCarMechanicUser) {
		boolean workingAppointmentIsAvailable = true;
		int durationOfAllWorks = 0;
		for (Work work : desiredWorksToPerform) {
			durationOfAllWorks += work.getWorkDuration();
		}
		LocalDateTime endOfNewDesiredWorkingAppointment = beginOfNewDesiredWorkingAppointment
				.plusMinutes(durationOfAllWorks);
		Set<WorkingAppointment> allWorkingAppointments = this.getAllWorkingAppointments();
		LocalDateTime beginOfExistingAppointment;
		LocalDateTime endOfExistingAppointment;
		CarMechanicUser responsibleCarMechanicUserOfExistingWorkingAppointment;
		WorkingPlatform workingPlatformOfExistingAppointment;

		for (WorkingAppointment workingAppointment : allWorkingAppointments) {
			beginOfExistingAppointment = workingAppointment.getAppointmentBeginDateAndTime();
			endOfExistingAppointment = workingAppointment.getAppointmentEndDateAndTime();
			responsibleCarMechanicUserOfExistingWorkingAppointment = workingAppointment.getResponsibleCarMechanicUser();
			workingPlatformOfExistingAppointment = workingAppointment.getWorkingPlatformRelatedToWorkingAppointment();
			if ((beginOfNewDesiredWorkingAppointment.isEqual(beginOfExistingAppointment)
					|| beginOfNewDesiredWorkingAppointment.isAfter(beginOfExistingAppointment))
					&& beginOfNewDesiredWorkingAppointment.isBefore(endOfExistingAppointment)
					&& (desiredResponsibleCarMechanicUser.equals(responsibleCarMechanicUserOfExistingWorkingAppointment)
							|| desiredWorkingPlatform.equals(workingPlatformOfExistingAppointment))) {
				workingAppointmentIsAvailable = false;
				System.err.println(
						"ERROR: New working appointment consolidates (beginning) with another one either with the same car mechanic or the same working platform or both of them. Please check the calendar of all appointments.");
				break;
			} else if (endOfNewDesiredWorkingAppointment.isAfter(beginOfExistingAppointment)
					&& (endOfNewDesiredWorkingAppointment.isBefore(endOfExistingAppointment)
							|| endOfNewDesiredWorkingAppointment.isEqual(endOfExistingAppointment))
					&& (desiredResponsibleCarMechanicUser.equals(responsibleCarMechanicUserOfExistingWorkingAppointment)
							|| desiredWorkingPlatform.equals(workingPlatformOfExistingAppointment))) {
				workingAppointmentIsAvailable = false;
				System.err.println(
						"ERROR: New working appointment consolidates (ends) with another one either with the same car mechanic or the same working platform or both of them. Please check the calendar of all appointments.");
				break;
			}
		}
		// Check if working platform is already occupied for cleaning appointments
		if (!workingAppointmentIsAvailable) {
			Set<CleaningAppointment> allCleaningAppointments = this.getAllCleaningAppointments();
			for (CleaningAppointment cleaningAppointment : allCleaningAppointments) {
				beginOfExistingAppointment = cleaningAppointment.getAppointmentBeginDateAndTime();
				endOfExistingAppointment = cleaningAppointment.getAppointmentEndDateAndTime();
				workingPlatformOfExistingAppointment = cleaningAppointment
						.getWorkingPlatformRelatedToCleaningAppointment();
				if ((beginOfNewDesiredWorkingAppointment.isAfter(beginOfExistingAppointment)
						|| beginOfNewDesiredWorkingAppointment.isEqual(beginOfExistingAppointment))
						&& beginOfNewDesiredWorkingAppointment.isBefore(endOfExistingAppointment)
						&& desiredWorkingPlatform.equals(workingPlatformOfExistingAppointment)) {
					workingAppointmentIsAvailable = false;
					System.err.println(
							"ERROR: New working appointment consolidates (beginning) with a cleaning appointment, that occupies the same working platform. Please check the calendar of all appointments.");
					break;
				} else if (endOfNewDesiredWorkingAppointment.isAfter(beginOfExistingAppointment)
						&& (endOfNewDesiredWorkingAppointment.isBefore(endOfExistingAppointment)
								|| endOfNewDesiredWorkingAppointment.isEqual(endOfExistingAppointment))
						&& desiredWorkingPlatform.equals(workingPlatformOfExistingAppointment)) {
					workingAppointmentIsAvailable = false;
					System.err.println(
							"ERROR: New working appointment consolidates (end) with a cleaning appointment, that occupies the same working platform. Please check the calendar of all appointments.");
					break;
				}
			}
		}
		return workingAppointmentIsAvailable;

	}

	public void createAndAddNewWorkingAppointment(Set<String> idsOfworksToPerform,
			String customerIdRelatedToWorkingAppointment, String vehicleLicensePlateOfVehicleToWorkOn,
			String workingPlatformIdRelatedToWorkingAppointment, LocalDateTime appointmentBeginDateAndTime,
			String usernameOfResponsibleCarMechanic) {
		Optional<WorkingPlatform> optionalWorkingPlatformRelatedToAppointment = PLATFORM_SERVICE
				.getWorkingPlatformById(workingPlatformIdRelatedToWorkingAppointment);
		if (optionalWorkingPlatformRelatedToAppointment.isPresent()) {
			WorkingPlatform workingPlatformRelatedToAppointment = optionalWorkingPlatformRelatedToAppointment.get();
			Optional<Customer> optionalCustomerRelatedToAppointment = CUSTOMER_Service
					.getCustomerById(customerIdRelatedToWorkingAppointment);
			if (optionalCustomerRelatedToAppointment.isPresent()) {
				Customer customerRelatedToAppointment = optionalCustomerRelatedToAppointment.get();
				Optional<Vehicle> optionalVehicleRelatedToAppointment = VEHICLE_SERVICE
						.getVehicleByLicensePlate(vehicleLicensePlateOfVehicleToWorkOn);
				if (optionalVehicleRelatedToAppointment.isPresent()) {
					Vehicle vehicleRelatedToAppointment = optionalVehicleRelatedToAppointment.get();
					if (customerRelatedToAppointment.getVehiclesOfCustomer()
							.contains(vehicleRelatedToAppointment)) {
						Set<Work> worksToPerform = new HashSet<>();
						boolean workInWorkSetIncorrect = false;
						Optional<Work> optionalWorkToPerform;
						for (String string : idsOfworksToPerform) {
							optionalWorkToPerform = WORK_SERVICE.getWorkById(string);
							if (optionalWorkToPerform.isPresent()) {
								worksToPerform.add(optionalWorkToPerform.get());
							} else {
								System.err.println("ERROR: A work with the ID(" + string
										+ ") of your set of works doesn´t exist. Working appointment NOT created!");
								workInWorkSetIncorrect = true;
								break;
							}
						}
						if (!workInWorkSetIncorrect) {
							Optional<User> optionalResponsibleCarMechanicUser = USER_SERVICE
									.getUserByUsername(usernameOfResponsibleCarMechanic);
							if (optionalResponsibleCarMechanicUser.isPresent()) {
								if (optionalResponsibleCarMechanicUser.get() instanceof CarMechanicUser) {
									CarMechanicUser responsibleCarMechanicUser = (CarMechanicUser) optionalResponsibleCarMechanicUser
											.get();
									if (checkIfWorkingAppointmentIsAvailable(worksToPerform,
											workingPlatformRelatedToAppointment, appointmentBeginDateAndTime,
											responsibleCarMechanicUser)) {
										PLANNING_CALENDAR_DATA.addAppointmentToPlanningCalendar(new WorkingAppointment(
												worksToPerform, customerRelatedToAppointment,
												vehicleRelatedToAppointment, workingPlatformRelatedToAppointment,
												appointmentBeginDateAndTime, responsibleCarMechanicUser));
									}
								} else {
									System.err.println("ERROR: User with the username "
											+ usernameOfResponsibleCarMechanic
											+ " isn´t a CarMechanic, please choose a username realted to a CarMechanic.");
								}
							} else {
								System.err.println("ERROR: User with the username " + usernameOfResponsibleCarMechanic
										+ " does not exist.");
							}

						}

					} else {
						System.err.println("ERROR: The vehicle exists, but it does not belong to the customer(ID: "
								+ customerIdRelatedToWorkingAppointment + "). Working appointment NOT created!");
					}

				} else {
					System.err.println(
							"ERROR: The vehicle with the license plate: " + vehicleLicensePlateOfVehicleToWorkOn
									+ " does not exist. Working appointment NOT created!");
				}
			} else {
				System.err.println("ERROR: The customer with the ID: " + customerIdRelatedToWorkingAppointment
						+ " does not exist. Working appointment NOT created!");
			}
		} else {
			System.err
					.println("ERROR: The working platform with the ID: " + workingPlatformIdRelatedToWorkingAppointment
							+ " does not exist. Working appointment NOT created!");
		}
	}

	/**
	 * MLP020
	 * 
	 * This method intercepts that appointments overlap and its purpose is to check
	 * if a given consulting appointment is available. If check fails, the method
	 * returns a negative value of boolean and prints an ERORR message.
	 * 
	 * 
	 * @param consultingAppointmentDuration          - int value of appointment
	 *                                               duration
	 * @param beginOfNewDesiredConsultingAppointment - LocalDateTime value of
	 *                                               beginning of the appointment
	 * @param responsibleDesiredClientAdvisor        - a responsible client advisor
	 *                                               of consulting appointment
	 * 
	 * @return - Boolean value if consulting appointment is available
	 */
	private boolean checkIfConsultingAppointmentIsAvailable(int consultingAppointmentDuration,
			LocalDateTime beginOfNewDesiredConsultingAppointment, ClientAdvisorUser responsibleDesiredClientAdvisor) {
		boolean consultingAppointmentIsAvailable = true;
		LocalDateTime endOfDesiredConsultingAppointment = beginOfNewDesiredConsultingAppointment
				.plusMinutes(consultingAppointmentDuration);
		Set<ConsultingAppointment> allConsultingAppointments = this.getAllConsultingAppointments();
		LocalDateTime beginOfExistingConsultingAppointment;
		LocalDateTime endOfExistingConsultingAppointment;
		ClientAdvisorUser responsibleClientAdvisorOfExistingConsultingAppointment;
		for (ConsultingAppointment consultingAppointment : allConsultingAppointments) {
			beginOfExistingConsultingAppointment = consultingAppointment.getAppointmentBeginDateAndTime();
			endOfExistingConsultingAppointment = consultingAppointment.getAppointmentEndDateAndTime();
			responsibleClientAdvisorOfExistingConsultingAppointment = consultingAppointment
					.getResponsibleClientAdvisorUser();
			if (beginOfNewDesiredConsultingAppointment.isEqual(beginOfExistingConsultingAppointment)
					|| (beginOfNewDesiredConsultingAppointment.isAfter(beginOfExistingConsultingAppointment))
							&& beginOfNewDesiredConsultingAppointment.isBefore(endOfExistingConsultingAppointment)
							&& responsibleDesiredClientAdvisor
									.equals(responsibleClientAdvisorOfExistingConsultingAppointment)) {
				consultingAppointmentIsAvailable = false;
				System.err.println(
						"ERROR: New consulting appointment with the same advisor consolidates (beginning) with another one. Please check the calendar of all appointments.");
				break;
			} else if (endOfDesiredConsultingAppointment.isAfter(beginOfExistingConsultingAppointment)
					&& (endOfDesiredConsultingAppointment.isBefore(endOfExistingConsultingAppointment)
							|| endOfDesiredConsultingAppointment.isEqual(endOfExistingConsultingAppointment))
					&& responsibleDesiredClientAdvisor
							.equals(responsibleClientAdvisorOfExistingConsultingAppointment)) {
				consultingAppointmentIsAvailable = false;
				System.err.println(
						"ERROR: New consulting appointment with the same advisor consolidates (end) with another one. Please check the calendar of all appointments.");
				break;
			}
		}

		return consultingAppointmentIsAvailable;
	}

	public void createAndAddNewConsultingAppointment(String customerIdRelatedToAppointment,
			int consultingAppointmentDuration, LocalDateTime appointmentBeginDateAndTime,
			String usernameOfResponsibleClientAdvisor) {
		Optional<Customer> optionalCustomerRelatedToAppointment = CUSTOMER_Service
				.getCustomerById(customerIdRelatedToAppointment);
		if (optionalCustomerRelatedToAppointment.isPresent()) {
			Customer customerRelatedToAppointment = optionalCustomerRelatedToAppointment.get();
			Optional<User> optionalResponsibleClientAdvisorUser = USER_SERVICE
					.getUserByUsername(usernameOfResponsibleClientAdvisor);
			if (optionalResponsibleClientAdvisorUser.isPresent()) {
				if (optionalResponsibleClientAdvisorUser.get() instanceof ClientAdvisorUser) {
					ClientAdvisorUser responsibleClientAdvisorUser = (ClientAdvisorUser) optionalResponsibleClientAdvisorUser
							.get();
					if (checkIfConsultingAppointmentIsAvailable(consultingAppointmentDuration,
							appointmentBeginDateAndTime, responsibleClientAdvisorUser)) {
						PLANNING_CALENDAR_DATA.addAppointmentToPlanningCalendar(
								new ConsultingAppointment(customerRelatedToAppointment, consultingAppointmentDuration,
										appointmentBeginDateAndTime, responsibleClientAdvisorUser));
					}
				} else {
					System.err.println("ERROR: User with the username " + usernameOfResponsibleClientAdvisor
							+ " isn´t a ClientAdvisor, please choose a username realted to a ClientAdvisor.");
				}
			} else {
				System.err.println(
						"ERROR: User with the username " + usernameOfResponsibleClientAdvisor + " does not exist.");
			}

		} else {
			System.err.println("ERROR: The customer with the ID: " + customerIdRelatedToAppointment
					+ " does not exist. Consulting appointment NOT created!");
		}
	}

	//
	/**
	 * MLP020
	 * 
	 * This method intercepts that appointments overlap and its purpose is to check
	 * if a given cleaning appointment is available. If check fails, the method
	 * returns a negative value of boolean and prints an ERORR message.
	 * 
	 * 
	 * @param cleaningAppointmentType                    - ENUM of appointment type
	 * @param desiredWorkingPlatformRelatedToAppointment - Desired working platform
	 *                                                   of WorkingPlatform Class
	 * @param beginOfNewDesiredCleaningAppointment       - LocalDateTime value of
	 *                                                   beginning of cleaning
	 *                                                   appointment
	 * @param responsibleDesiredDispatcherUser           -
	 * @return
	 */
	private boolean checkIfCleaningAppointmentIsAvailable(CleaningAppointmentType cleaningAppointmentType,
			WorkingPlatform desiredWorkingPlatformRelatedToAppointment,
			LocalDateTime beginOfNewDesiredCleaningAppointment, DispatcherUser responsibleDesiredDispatcherUser) {
		boolean cleaningAppointmentIsAvailable = true;
		LocalDateTime endOfNewDesiredCleaningAppointment;
		switch (cleaningAppointmentType) {
		case QUICK:
			endOfNewDesiredCleaningAppointment = beginOfNewDesiredCleaningAppointment.plusMinutes(30);
			break;
		case INTENSIVE:
			endOfNewDesiredCleaningAppointment = beginOfNewDesiredCleaningAppointment.plusMinutes(60);
			break;
		default:
			throw new IllegalArgumentException();
		}
		Set<CleaningAppointment> allCleaningAppointments = this.getAllCleaningAppointments();
		LocalDateTime beginOfExistingAppointment;
		LocalDateTime endOfExistingAppointment;
		DispatcherUser responsibleDispatcherUserOfExistingCleaningAppointment;
		WorkingPlatform workingPlatformOfExistingAppointment;

		for (CleaningAppointment cleaningAppointment : allCleaningAppointments) {
			beginOfExistingAppointment = cleaningAppointment.getAppointmentBeginDateAndTime();
			endOfExistingAppointment = cleaningAppointment.getAppointmentEndDateAndTime();
			responsibleDispatcherUserOfExistingCleaningAppointment = cleaningAppointment.getResponsibleDispatcherUser();
			workingPlatformOfExistingAppointment = cleaningAppointment.getWorkingPlatformRelatedToCleaningAppointment();
			if ((beginOfNewDesiredCleaningAppointment.isAfter(beginOfExistingAppointment)
					|| beginOfNewDesiredCleaningAppointment.isEqual(beginOfExistingAppointment))
					&& beginOfNewDesiredCleaningAppointment.isBefore(endOfExistingAppointment)
					&& (responsibleDesiredDispatcherUser.equals(responsibleDispatcherUserOfExistingCleaningAppointment)
							|| desiredWorkingPlatformRelatedToAppointment
									.equals(workingPlatformOfExistingAppointment))) { // either dispatcher
																						// or platform is already
																						// occupied
				cleaningAppointmentIsAvailable = false;
				System.err.println(
						"ERROR: New cleaning appointment consolidates (beginning) with another one and it is either because of the same dispatcher or the same working platform or both. Please check a calendar of all appointments.");
				break;
			} else if (endOfNewDesiredCleaningAppointment.isAfter(beginOfExistingAppointment)
					&& (endOfNewDesiredCleaningAppointment.isBefore(endOfExistingAppointment)
							|| endOfNewDesiredCleaningAppointment.isEqual(endOfExistingAppointment))
					&& (responsibleDesiredDispatcherUser.equals(responsibleDispatcherUserOfExistingCleaningAppointment)
							|| desiredWorkingPlatformRelatedToAppointment
									.equals(workingPlatformOfExistingAppointment))) {
				cleaningAppointmentIsAvailable = false;
				System.err.println(
						"ERROR: New cleaning appointment consolidates (end) with another one and it is either because of the same dispatcher or the same working platform or both. Please check a calendar of all appointments.");
				break;
			}
		}
		// Check if working platform is already occupied for working appointments
		if (!cleaningAppointmentIsAvailable) {
			Set<WorkingAppointment> allWorkingAppointments = this.getAllWorkingAppointments();
			for (WorkingAppointment workingAppointment : allWorkingAppointments) {
				workingPlatformOfExistingAppointment = workingAppointment
						.getWorkingPlatformRelatedToWorkingAppointment();
				beginOfExistingAppointment = workingAppointment.getAppointmentBeginDateAndTime();
				endOfExistingAppointment = workingAppointment.getAppointmentEndDateAndTime();
				if ((beginOfNewDesiredCleaningAppointment.isAfter(beginOfExistingAppointment)
						|| beginOfNewDesiredCleaningAppointment.isEqual(beginOfExistingAppointment))
						&& beginOfNewDesiredCleaningAppointment.isBefore(endOfExistingAppointment)
						&& desiredWorkingPlatformRelatedToAppointment.equals(workingPlatformOfExistingAppointment)) {
					cleaningAppointmentIsAvailable = false;
					System.err.println(
							"ERROR: New working appointment consolidates (beginning) with another one on the same working platform. Please check a calendar of all appointments. ");

					break;
				} else if (endOfNewDesiredCleaningAppointment.isAfter(beginOfExistingAppointment)
						&& (endOfNewDesiredCleaningAppointment.isBefore(endOfExistingAppointment)
								|| endOfNewDesiredCleaningAppointment.isEqual(endOfExistingAppointment))
						&& desiredWorkingPlatformRelatedToAppointment.equals(workingPlatformOfExistingAppointment)) {
					cleaningAppointmentIsAvailable = false;
					System.err.println(
							"ERROR: New working appointment consolidates (end) with another one on the same working platform. Please check a calendar of all appointments.");
					break;
				}
			}
		}
		return cleaningAppointmentIsAvailable;

	}

	public void createAndAddNewCleaningAppointment(CleaningAppointmentType cleaningAppointmentType,
			String workingPlatformIdRelatedToAppointment, LocalDateTime appointmentBeginDateAndTime,
			String usernameOfResponsibleDispatcher) {
		Optional<WorkingPlatform> optionalWorkingPlatformRelatedToAppointment = PLATFORM_SERVICE
				.getWorkingPlatformById(workingPlatformIdRelatedToAppointment);
		if (optionalWorkingPlatformRelatedToAppointment.isPresent()) {
			WorkingPlatform workingPlatformRelatedToAppointment = optionalWorkingPlatformRelatedToAppointment.get();
			Optional<User> optionalResponsibleDispatcher = USER_SERVICE
					.getUserByUsername(usernameOfResponsibleDispatcher);
			if (optionalResponsibleDispatcher.isPresent()) {
				if (optionalResponsibleDispatcher.get() instanceof DispatcherUser) {
					DispatcherUser responsibleDispatcherUser = (DispatcherUser) optionalResponsibleDispatcher.get();
					if (checkIfCleaningAppointmentIsAvailable(cleaningAppointmentType,
							workingPlatformRelatedToAppointment, appointmentBeginDateAndTime,
							responsibleDispatcherUser)) {
						PLANNING_CALENDAR_DATA.addAppointmentToPlanningCalendar(
								new CleaningAppointment(cleaningAppointmentType, workingPlatformRelatedToAppointment,
										appointmentBeginDateAndTime, responsibleDispatcherUser));
					}
				} else {
					System.err.println("ERROR: User with the username " + usernameOfResponsibleDispatcher
							+ " isn´t a Dispatcher, please choose a username realted to a Dispatcher.");
				}
			} else {
				System.err.println(
						"ERROR: User with the username " + usernameOfResponsibleDispatcher + " does not exist.");
			}

		} else {
			System.err.println("ERROR: The working platform with the ID: " + workingPlatformIdRelatedToAppointment
					+ " does not exist. Cleaning appointment NOT created!");
		}
	}

	public void changeWorkingAppointmentStatusOf_To(String idOfWorkingAppointmentToChangeStatus,
			WorkingAppointmentStatus newWorkingAppointmentStatus) {
		Optional<Appointment> optionalWorkingAppointment = this
				.getAppointmentById(idOfWorkingAppointmentToChangeStatus);
		if (optionalWorkingAppointment.isPresent() && optionalWorkingAppointment.get() instanceof WorkingAppointment) {
			WorkingAppointment workingAppointmentToChangeStatus = (WorkingAppointment) optionalWorkingAppointment.get();
			workingAppointmentToChangeStatus.setStatus(newWorkingAppointmentStatus);
		} else {
			System.err.println("WorkingAppointment with the ID " + idOfWorkingAppointmentToChangeStatus
					+ " does not exist, or the ID isn´t related to a WorkingAppointment.");
		}
	}

	public Optional<Appointment> getAppointmentById(String appointmentId) {
		Set<Appointment> allAppointments = PLANNING_CALENDAR_DATA.getAllAppointments();
		Appointment foundAppointment = null;
		for (Appointment appointment : allAppointments) {
			if (appointment.getAppointmentId()
					.equals(appointmentId)) {
				foundAppointment = appointment;
				break;
			}
		}
		return Optional.ofNullable(foundAppointment);
	}

	/**
	 * Allows you to update an existing working appointment. You can only change the
	 * working platform or the date, if you want to change the works to perform or
	 * the vehicle, create a new appointment and remove the old one! Update method
	 * for everything would have been too much not demanded work.
	 */
	public void updateWorkingAppointment(String appointmentIdOfAppointmentToUpdate,
			String idOfNewWorkingPlatformRelatedToWorkingAppointment, LocalDateTime newAppointmentBeginDateAndTime) {
		Optional<Appointment> optionalAppointmentToUpdate = getAppointmentById(appointmentIdOfAppointmentToUpdate);
		Optional<WorkingPlatform> optionalNewWorkingPlatform = PLATFORM_SERVICE
				.getWorkingPlatformById(idOfNewWorkingPlatformRelatedToWorkingAppointment);
		if (optionalAppointmentToUpdate.isPresent()) {
			Appointment appointmentToUpdate = optionalAppointmentToUpdate.get();
			if (appointmentToUpdate instanceof WorkingAppointment) {
				WorkingAppointment workingAppointmentToUpdate = (WorkingAppointment) appointmentToUpdate;
				if (optionalNewWorkingPlatform.isPresent()) {
					WorkingPlatform newWorkingPlatform = optionalNewWorkingPlatform.get();
					workingAppointmentToUpdate.setWorkingPlatformRelatedToWorkingAppointment(newWorkingPlatform);
					appointmentToUpdate.setAppointmentBeginDateAndTime(newAppointmentBeginDateAndTime);
				} else {
					System.err.println("ERROR: The new working platform does not exist. "
							+ "Create the platform first using the workingPlatformService or "
							+ "use the getAllWorkingPlatforms() method of the workingPlatformService to see all the platforms and their ID.");
				}
			} else {
				System.err.println("ERROR: Appointment with the ID: " + appointmentIdOfAppointmentToUpdate
						+ " isn't connected to a working appointment. Use updateCleaningAppointment(...) "
						+ "or updateConsultingAppointment(...) instead.");
			}
		} else {
			System.err.println("ERROR: Appointment with the ID: " + appointmentIdOfAppointmentToUpdate
					+ " was not found. Use getAllWorkingAppointments() to see all the working appointments with their ID.");
		}

	}

	/**
	 * Allows you to update an existing consulting appointment. You can only change
	 * the duration of the consulting and-/or the date.
	 */

	public void updateConsultingAppointment(String appointmentIdOfAppointmentToUpdate,
			int newConsultingAppointmentDuration, LocalDateTime newAppointmentBeginDateAndTime) {
		Optional<Appointment> optionalAppointmentToUpdate = getAppointmentById(appointmentIdOfAppointmentToUpdate);
		if (optionalAppointmentToUpdate.isPresent()) {
			Appointment appointmentToUpdate = optionalAppointmentToUpdate.get();
			if (appointmentToUpdate instanceof ConsultingAppointment) {
				ConsultingAppointment consultingAppointmentToUpdate = (ConsultingAppointment) appointmentToUpdate;
				consultingAppointmentToUpdate.setConsultingAppointmentDuration(newConsultingAppointmentDuration);
				consultingAppointmentToUpdate.setAppointmentBeginDateAndTime(newAppointmentBeginDateAndTime);

			} else {
				System.err.println("ERROR: Appointment with the ID: " + appointmentIdOfAppointmentToUpdate
						+ " isn't connected to a consulting appointment. Use updateWorkingAppointment(...) or updateCleaningAppointment(...) instead.");
			}
		} else {
			System.err.println("ERROR: Appointment with the ID: " + appointmentIdOfAppointmentToUpdate
					+ " was not found. Use getAllConsultingAppointments() to see all the consulting appointments with their ID.");
		}

	}

	/**
	 * Allows you to update an existing cleaning appointment. You can only change
	 * the type of cleaning appointment, set new working platform or the date.
	 */
	public void updateCleaningAppointment(String appointmentIdOfAppointmentToUpdate,
			CleaningAppointmentType newCleaningAppointmentType,
			String idOfNewWorkingPlatformRelatedToCleaningAppointment, LocalDateTime newAppointmentBeginDateAndTime) {
		Optional<Appointment> optionalAppointmentToUpdate = getAppointmentById(appointmentIdOfAppointmentToUpdate);
		Optional<WorkingPlatform> optionalNewWorkingPlatform = PLATFORM_SERVICE
				.getWorkingPlatformById(idOfNewWorkingPlatformRelatedToCleaningAppointment);
		if (optionalAppointmentToUpdate.isPresent()) {
			Appointment appointmentToUpdate = optionalAppointmentToUpdate.get();
			if (appointmentToUpdate instanceof CleaningAppointment) {
				CleaningAppointment cleaningAppointmentToUpdate = (CleaningAppointment) appointmentToUpdate;
				if (optionalNewWorkingPlatform.isPresent()) {
					WorkingPlatform newWorkingPlatform = optionalNewWorkingPlatform.get();
					cleaningAppointmentToUpdate.setWorkingPlatformRelatedToCleaningAppointment(newWorkingPlatform);
					cleaningAppointmentToUpdate.setCleaningAppointmentType(newCleaningAppointmentType);
					cleaningAppointmentToUpdate.setAppointmentBeginDateAndTime(newAppointmentBeginDateAndTime);
				} else {
					System.err.println("ERROR: The new working platform does not exist. "
							+ "Create the platform first using the workingPlatformService or "
							+ "use the getAllWorkingPlatforms() method of the workingPlatformService to see all the platforms and their ID.");
				}
			} else {
				System.err.println("ERROR: Appointment with the ID:" + appointmentIdOfAppointmentToUpdate
						+ " isn't connected to a cleaning appointment. Use updateWorkingAppointment(...) or updateConsultingAppointment(..) instead.");
			}
		} else {
			System.err.println("ERROR: Appointment with the ID: " + appointmentIdOfAppointmentToUpdate
					+ " was not found. Use getAllCleaningAppointments() to see all the cleaning appointments with their ID.");
		}

	}

	public void removeAppointment(String appointmentIdOfAppointmentToRemove) {
		Optional<Appointment> optionalAppointmentToRemove = getAppointmentById(appointmentIdOfAppointmentToRemove);
		if (optionalAppointmentToRemove.isPresent()) {
			PLANNING_CALENDAR_DATA.removeAppointmentFromAppointmentData(optionalAppointmentToRemove.get());
		} else {
			System.err.println("ERROR: The appointment with the ID: " + appointmentIdOfAppointmentToRemove
					+ " does not exist in the PLANNING_CALENDAR_DATA. Try a different ID, check the appointment type of the ID with"
					+ " getTypeOfAppointmentRelatedToId(...) or use getAllCustomers(...) to see all the customers with their ID.");
		}
	}

	public void printTypeOfAppointmentRelatedToId(String idOfAppointment) {
		Optional<Appointment> optionalAppointment = getAppointmentById(idOfAppointment);
		if (optionalAppointment.isPresent()) {
			Appointment appointment = optionalAppointment.get();
			if (appointment instanceof WorkingAppointment) {
				System.out.println("The ID: " + idOfAppointment + " is connected to a working appointment.");
			} else if (appointment instanceof CleaningAppointment) {
				System.out.println("The ID: " + idOfAppointment + " is connected to a cleaning appointment.");
			} else if (appointment instanceof ConsultingAppointment) {
				System.out.println("The ID: " + idOfAppointment + " is connected to a consulting appointment.");
			}
		} else {
			System.err.println("ERROR: Appointment with the ID: " + idOfAppointment + " does not exist!");
		}
	}

	private Set<Appointment> getSortedSetOfGivenSetOfAppointments(Set<Appointment> setOfAppointmentsToSort) {
		Set<Appointment> sortedAppointments = new LinkedHashSet<>(setOfAppointmentsToSort.size());
		if (setOfAppointmentsToSort.isEmpty())
			;
		else {
			sortedAppointments = setOfAppointmentsToSort.stream()
					.sorted(new AppointmentComparator())
					.collect(Collectors.toCollection(LinkedHashSet::new));
		}
		return sortedAppointments;

	}

	/**
	 * Returns a SORTED ({@link AppointmentComparator}) set of all the appointments
	 * in the given calendar week. (may be empty)
	 * 
	 * @param calendarWeek - Integer of the calendarWeek you want to get an overview
	 *                     to
	 * @return - Set of all appointments in the given week or an empty Set!
	 */
	public Set<Appointment> getSortedAppointmentOverviewOfWeek(int calendarWeek) {
		Set<Appointment> allAppointments = PLANNING_CALENDAR_DATA.getAllAppointments();
		Set<Appointment> allAppointmentsOfSearchedCalendarWeek = new HashSet<>();
		LocalDateTime dateOfAppointment;
		LocalDate dateNeededToGetCalendarWeek;

		for (Appointment appointment : allAppointments) {
			dateOfAppointment = appointment.getAppointmentBeginDateAndTime();
			dateNeededToGetCalendarWeek = LocalDate.of(dateOfAppointment.getYear(), dateOfAppointment.getMonth(),
					dateOfAppointment.getDayOfMonth());
			int weekOfAppointment = dateNeededToGetCalendarWeek.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
			if (weekOfAppointment == calendarWeek) {
				allAppointmentsOfSearchedCalendarWeek.add(appointment);
			}
		}
		return getSortedSetOfGivenSetOfAppointments(allAppointmentsOfSearchedCalendarWeek);
	}

	public void printSortedAppointmentOverviewOfWeek(int calendarWeek) {
		Set<Appointment> allAppointmentsOfSearchedCalendarWeek = this.getSortedAppointmentOverviewOfWeek(calendarWeek);
		if (allAppointmentsOfSearchedCalendarWeek.isEmpty()) {
			System.err.println("No appointments in the calendar week " + calendarWeek + ".");
		} else {
			this.printAppointmentsDetailedFor(allAppointmentsOfSearchedCalendarWeek);
		}
	}

	private Set<WorkingAppointment> getSortedSetOfGivenSetOfGivenWorkingAppointments(
			Set<WorkingAppointment> setOfAppointmentsToSort) {
		Set<WorkingAppointment> sortedAppointments = new LinkedHashSet<>(setOfAppointmentsToSort.size());
		if (setOfAppointmentsToSort.isEmpty())
			;
		else {
			sortedAppointments = setOfAppointmentsToSort.stream()
					.sorted(new AppointmentComparator())
					.collect(Collectors.toCollection(LinkedHashSet::new));
		}
		return sortedAppointments;

	}

	/**
	 * Returns a SORTED ({@link AppointmentComparator}) set of all the appointments
	 * of yesterday (may be empty).
	 * 
	 * @return - Set of all appointments of yesterday or an empty Set!
	 */
	public Set<WorkingAppointment> getSortedFinishedWorkingAppointmentsOfYesterday() {
		Set<WorkingAppointment> allWorkingAppointments = PLANNER_SERVICE.getAllWorkingAppointments();
		Set<WorkingAppointment> allWorkingAppointmentsOfYesterday = new HashSet<>();
		LocalDateTime beginDateAndTimeOfAppointment;
		LocalDate dateOfAppointment;
		LocalDate dateOfYesterday = LocalDate.now()
				.minusDays(1);
		String appointmentStatus;
		for (WorkingAppointment appointment : allWorkingAppointments) {
			beginDateAndTimeOfAppointment = appointment.getAppointmentBeginDateAndTime();
			dateOfAppointment = LocalDate.of(beginDateAndTimeOfAppointment.getYear(),
					beginDateAndTimeOfAppointment.getMonth(), beginDateAndTimeOfAppointment.getDayOfMonth());
			if (dateOfYesterday.equals(dateOfAppointment)) {
				appointmentStatus = appointment.getStatus();
				if (appointmentStatus.equals("FINISHED")) {
					{
						allWorkingAppointmentsOfYesterday.add(appointment);
					}
				}
			}
		}
		return getSortedSetOfGivenSetOfGivenWorkingAppointments(allWorkingAppointmentsOfYesterday);

	}

	private static void printWorkingAppointmentsDetailedFor(Set<WorkingAppointment> workingAppointments) {
		workingAppointments.forEach(workingAppointment -> System.out.println(workingAppointment));
	}

	public void printAllWorkingAppointmentsOfYesterday() {
		Set<WorkingAppointment> allAppointmentsOfYesterday = this.getSortedFinishedWorkingAppointmentsOfYesterday();
		if (allAppointmentsOfYesterday.isEmpty()) {
			System.err.println("No working appointments were scheduled yesterday.");
		} else {
			printWorkingAppointmentsDetailedFor(allAppointmentsOfYesterday);
		}
	}

	public Set<WorkingAppointment> getAllOpenWorkingAppointmentsOfTodayFor(String carMechanicUsernameToLookup) {
		Set<WorkingAppointment> allWorkingAppointments = PLANNER_SERVICE.getAllWorkingAppointments();
		Set<WorkingAppointment> allOpenWorkingAppointmentsOfToday = new HashSet<>();
		LocalDateTime beginDateAndTimeOfWorkingAppointment;
		LocalDate dateOfWorkingAppointment;
		LocalDate dateOfToday = LocalDate.now();
		String workingAppointmentStatus;
		User carMechanicUser;
		String carMechanicUsername;
		for (WorkingAppointment appointment : allWorkingAppointments) {
			beginDateAndTimeOfWorkingAppointment = appointment.getAppointmentBeginDateAndTime();
			dateOfWorkingAppointment = LocalDate.of(beginDateAndTimeOfWorkingAppointment.getYear(),
					beginDateAndTimeOfWorkingAppointment.getMonth(),
					beginDateAndTimeOfWorkingAppointment.getDayOfMonth());
			carMechanicUser = appointment.getResponsibleCarMechanicUser();
			carMechanicUsername = carMechanicUser.getUsername();
			if (dateOfToday.equals(dateOfWorkingAppointment)) {
				if (carMechanicUsername.equals(carMechanicUsernameToLookup)) {
					workingAppointmentStatus = appointment.getStatus();
					if (workingAppointmentStatus.equals("OPEN")) {
						allOpenWorkingAppointmentsOfToday.add(appointment);
					}
				}
			}
		}
		return getSortedSetOfGivenSetOfGivenWorkingAppointments(allOpenWorkingAppointmentsOfToday);

	}

	public void printAllSortedOpenWorkingAppointmentsOfTodayFor(String carMechanicUsernameToLookup) {
		Set<WorkingAppointment> allOpenAppointmentsOfToday = this
				.getAllOpenWorkingAppointmentsOfTodayFor(carMechanicUsernameToLookup);
		if (allOpenAppointmentsOfToday.isEmpty()) {
			System.err.println("No open working appointments for a given car mechanic (" + carMechanicUsernameToLookup
					+ ") user are scheduled for today.");
		} else {
			printWorkingAppointmentsDetailedFor(allOpenAppointmentsOfToday);
		}
	}

	public Set<WorkingAppointment> getAllOpenWorkingAppointmentsByWorkingPlatformId(
			String idOfWorkingPlatformToLookup) {
		Set<WorkingAppointment> allWorkingAppointments = PLANNER_SERVICE.getAllWorkingAppointments();
		Set<WorkingAppointment> allOpenWorkingAppointments = new HashSet<>();
		LocalDateTime dateAndTimeOfNow = LocalDateTime.now();
		for (WorkingAppointment appointment : allWorkingAppointments) {
			if ((appointment.getAppointmentBeginDateAndTime()
					.isAfter(dateAndTimeOfNow)
					|| appointment.getAppointmentBeginDateAndTime()
							.isEqual(dateAndTimeOfNow))
					|| appointment.getAppointmentEndDateAndTime()
							.isAfter(dateAndTimeOfNow)) {
				if (appointment.getWorkingPlatformRelatedToWorkingAppointment()
						.getWorkingPlatformId()
						.equals(idOfWorkingPlatformToLookup)) {
					if (appointment.getStatus()
							.equals("OPEN")) {
						allOpenWorkingAppointments.add(appointment);
					}
				}
			}
		}
		return allOpenWorkingAppointments;
	}

	public Set<CleaningAppointment> getAllOpenCleaningAppointmentsByWorkingPlatformIdOrDispatcher(
			String idOfWorkingPlatformToLookup, String usernameOfDesiredDispatcher) {
		Set<CleaningAppointment> allCleaningAppointments = PLANNER_SERVICE.getAllCleaningAppointments();
		Set<CleaningAppointment> allOpenCleaningAppointments = new HashSet<>();
		LocalDateTime dateAndTimeOfNow = LocalDateTime.now();
		for (CleaningAppointment appointment : allCleaningAppointments) {
			if ((appointment.getAppointmentBeginDateAndTime()
					.isAfter(dateAndTimeOfNow)
					|| appointment.getAppointmentBeginDateAndTime()
							.isEqual(dateAndTimeOfNow))
					|| appointment.getAppointmentEndDateAndTime()
							.isAfter(dateAndTimeOfNow)) {
				if (appointment.getWorkingPlatformRelatedToCleaningAppointment()
						.getWorkingPlatformId()
						.equals(idOfWorkingPlatformToLookup)
						|| appointment.getResponsibleDispatcherUser()
								.getUsername()
								.equals(usernameOfDesiredDispatcher)) {
					allOpenCleaningAppointments.add(appointment);
				}
			}
		}
		return allOpenCleaningAppointments;
	}

	private Set<Appointment> mergeSetsToSortedSet(Set<WorkingAppointment> workingAppointment,
			Set<CleaningAppointment> cleaningAppointment) {
		Set<Appointment> combinedAppointments = Stream.of(workingAppointment, cleaningAppointment)
				.flatMap(x -> x.stream())
				.collect(Collectors.toSet());
		return getSortedSetOfGivenSetOfAppointments(combinedAppointments);
	}

	public Set<Appointment> getAllOpenSortedAppointmentsOnWorkingPlatformAfterNow(String idOfWorkingPlatformToLookup) {
		return mergeSetsToSortedSet(getAllOpenWorkingAppointmentsByWorkingPlatformId(idOfWorkingPlatformToLookup),
				this.getAllOpenCleaningAppointmentsByWorkingPlatformIdOrDispatcher(idOfWorkingPlatformToLookup,
						"doesn´t matter"));
	}

	// observing operation hours and times between appointments would be useful,
	// but we left it out for now because it is not demanded in the requirements
	public void createAndAddNextAvailableCleaningAppointment(CleaningAppointmentType cleaningAppointmentType,
			String workingPlatformIdOfPlatformRelatedToAppointment,
			String dispatcherUsernameOfResponsibleDispatcherUser) {
		int appointmentDurationInMinutes;
		switch (cleaningAppointmentType) {
		case QUICK:
			appointmentDurationInMinutes = 30;
			break;
		case INTENSIVE:
			appointmentDurationInMinutes = 60;
			break;
		default:
			throw new IllegalArgumentException();
		}
		Set<WorkingAppointment> allOpenWorkingAppointmentsByGivenWorkingPlatformIdAfterNow = this
				.getAllOpenWorkingAppointmentsByWorkingPlatformId(workingPlatformIdOfPlatformRelatedToAppointment);
		Set<CleaningAppointment> allOpenCleaningAppointmentsByGivenWorkingPlatformIdAfterNow = this
				.getAllOpenCleaningAppointmentsByWorkingPlatformIdOrDispatcher(
						workingPlatformIdOfPlatformRelatedToAppointment, dispatcherUsernameOfResponsibleDispatcherUser);
		Set<Appointment> allAppointmentsAfterNow = mergeSetsToSortedSet(
				allOpenWorkingAppointmentsByGivenWorkingPlatformIdAfterNow,
				allOpenCleaningAppointmentsByGivenWorkingPlatformIdAfterNow);
		ArrayList<Appointment> arrayAllAppointmentsAfterNow = new ArrayList<>();
		allAppointmentsAfterNow.forEach(appointment -> arrayAllAppointmentsAfterNow.add(appointment));
		LocalDateTime dateAndTimeOfNow = LocalDateTime.now();
		if (arrayAllAppointmentsAfterNow.isEmpty()) {
			System.out.println("Appointment created NOW at " + dateAndTimeOfNow + "!");
			this.createAndAddNewCleaningAppointment(cleaningAppointmentType,
					workingPlatformIdOfPlatformRelatedToAppointment, dateAndTimeOfNow,
					dispatcherUsernameOfResponsibleDispatcherUser);
		} else {
			int differenceBetweenNowAndFirstAppointment = (int) ChronoUnit.MINUTES.between(dateAndTimeOfNow,
					arrayAllAppointmentsAfterNow.get(0)
							.getAppointmentBeginDateAndTime());
			if (differenceBetweenNowAndFirstAppointment >= appointmentDurationInMinutes) {
				System.out.println("Appointment created NOW at " + dateAndTimeOfNow + "!");
				this.createAndAddNewCleaningAppointment(cleaningAppointmentType,
						workingPlatformIdOfPlatformRelatedToAppointment, dateAndTimeOfNow,
						dispatcherUsernameOfResponsibleDispatcherUser);
			} else {
				for (int i = 0; i < arrayAllAppointmentsAfterNow.size(); i++) {
					if (i + 1 != arrayAllAppointmentsAfterNow.size()) {
						Appointment appointment1 = arrayAllAppointmentsAfterNow.get(i);
						Appointment nextAppointment = arrayAllAppointmentsAfterNow.get(i + 1);
						int difference = (int) ChronoUnit.MINUTES.between(appointment1.getAppointmentEndDateAndTime(),
								nextAppointment.getAppointmentBeginDateAndTime());
						if (difference >= appointmentDurationInMinutes) {
							System.out.println(
									"Appointment created at " + appointment1.getAppointmentEndDateAndTime() + "!");
							this.createAndAddNewCleaningAppointment(cleaningAppointmentType,
									workingPlatformIdOfPlatformRelatedToAppointment,
									appointment1.getAppointmentEndDateAndTime(),
									dispatcherUsernameOfResponsibleDispatcherUser);
							break;
						}
					} else {
						System.out.println("Appointment created after all existing appointments at "
								+ arrayAllAppointmentsAfterNow.get(arrayAllAppointmentsAfterNow.size() - 1)
										.getAppointmentEndDateAndTime()
								+ "!");
						this.createAndAddNewCleaningAppointment(cleaningAppointmentType,
								workingPlatformIdOfPlatformRelatedToAppointment,
								arrayAllAppointmentsAfterNow.get(arrayAllAppointmentsAfterNow.size() - 1)
										.getAppointmentEndDateAndTime(),
								dispatcherUsernameOfResponsibleDispatcherUser);
					}
				}
			}
		}
	}

	// MLP040

	public Set<WorkingAppointment> getAllOpenWorkingAppointmentsAfterNow() {
		Set<WorkingAppointment> allWorkingAppointments = PLANNER_SERVICE.getAllWorkingAppointments();
		Set<WorkingAppointment> allOpenWorkingAppointments = new HashSet<>();
		LocalDateTime dateAndTimeOfNow = LocalDateTime.now();
		for (WorkingAppointment appointment : allWorkingAppointments) {
			if ((appointment.getAppointmentBeginDateAndTime()
					.isAfter(dateAndTimeOfNow)
					|| appointment.getAppointmentBeginDateAndTime()
							.isEqual(dateAndTimeOfNow))
					|| appointment.getAppointmentEndDateAndTime()
							.isAfter(dateAndTimeOfNow)) {
				{
					if (appointment.getStatus()
							.equals("OPEN")) {
						allOpenWorkingAppointments.add(appointment);
					}
				}
			}
		}
		return allOpenWorkingAppointments;
	}

	public Set<CleaningAppointment> getAllOpenCleaningAppointmentsAfterNow() {
		Set<CleaningAppointment> allCleaningAppointments = PLANNER_SERVICE.getAllCleaningAppointments();
		Set<CleaningAppointment> allOpenCleaningAppointments = new HashSet<>();
		LocalDateTime dateAndTimeOfNow = LocalDateTime.now();
		for (CleaningAppointment appointment : allCleaningAppointments) {
			if ((appointment.getAppointmentBeginDateAndTime()
					.isAfter(dateAndTimeOfNow)
					|| appointment.getAppointmentBeginDateAndTime()
							.isEqual(dateAndTimeOfNow))
					|| appointment.getAppointmentEndDateAndTime()
							.isAfter(dateAndTimeOfNow)) {
				{
					allOpenCleaningAppointments.add(appointment);
				}
			}
		}
		return allOpenCleaningAppointments;
	}

	public Set<Appointment> getAllOpenSortedAppointmentsAfterNow() {
		return mergeSetsToSortedSet(getAllOpenWorkingAppointmentsAfterNow(),
				this.getAllOpenCleaningAppointmentsAfterNow());
	}

	/**
	 * A method that suggests the user three available dates for a given working
	 * appointment. It also takes in account other appointments occupying a working
	 * platform. Method checks for different possible gaps between date of now and a
	 * list of all open appointments.
	 * 
	 * Note: observing operation hours and times between appointments would be
	 * useful, but we left it out for now because it is not demanded in the
	 * requirements
	 * 
	 * 
	 * @param idsOfWorksToPerformDuringNewWorkingAppointment
	 * @param workingPlatformIdOfPlatformRelatedToAppointment
	 * @return
	 */
	public ArrayList<LocalDateTime> getSuggestionOfThreeNextAvailableWorkingAppointmentDatesOnWorkingPlatform(
			Set<String> idsOfWorksToPerformDuringNewWorkingAppointment,
			String workingPlatformIdOfPlatformRelatedToAppointment) {
		Set<Appointment> allAppointmentsAfterNow = getAllOpenSortedAppointmentsOnWorkingPlatformAfterNow(
				workingPlatformIdOfPlatformRelatedToAppointment);
		LocalDateTime dateAndTimeOfNow = LocalDateTime.now();
		int appointmentDurationInMinutes = 0;
		Set<Work> worksToPerform = new HashSet<>();
		Optional<Work> optionalWorkToPerform;
		for (String string : idsOfWorksToPerformDuringNewWorkingAppointment) {
			optionalWorkToPerform = WORK_SERVICE.getWorkById(string);
			if (optionalWorkToPerform.isPresent()) {
				worksToPerform.add(optionalWorkToPerform.get());
			}
		}
		for (Work work : worksToPerform) {
			appointmentDurationInMinutes += work.getWorkDuration();
		}
		ArrayList<LocalDateTime> nextThreeSuggestionDatesForWorkingAppointment = new ArrayList<>();
		int anzahlFreiePlätze;
		int sizeOfTheThreeSugestionList = 0;
		if (allAppointmentsAfterNow.isEmpty()) {
			anzahlFreiePlätze = 3;
			nextThreeSuggestionDatesForWorkingAppointment.add(dateAndTimeOfNow);
			// System.out.println("First Suggestion is now at " + dateAndTimeOfNow + "!");
			sizeOfTheThreeSugestionList += 1;
			anzahlFreiePlätze -= 1;
			if (anzahlFreiePlätze >= 1) {
				while (anzahlFreiePlätze > 0) {
					LocalDateTime lastSuggestion = nextThreeSuggestionDatesForWorkingAppointment
							.get(nextThreeSuggestionDatesForWorkingAppointment.size() - 1);
					LocalDateTime endOfLastSuggestion = lastSuggestion.plusMinutes(appointmentDurationInMinutes);
					nextThreeSuggestionDatesForWorkingAppointment.add(endOfLastSuggestion);
					// System.out.println("Suggestion after suggestion: " + endOfLastSuggestion);
					sizeOfTheThreeSugestionList += 1;
					anzahlFreiePlätze -= 1;
				}
			}
		} else {
			ArrayList<Appointment> arrayAllAppointmentsAfterNow = new ArrayList<>();
			allAppointmentsAfterNow.forEach(appointment -> arrayAllAppointmentsAfterNow.add(appointment));

			// Look if one or more are free right now
			int differenceBetweenNowAndFirstAppointment = (int) ChronoUnit.MINUTES.between(dateAndTimeOfNow,
					arrayAllAppointmentsAfterNow.get(0)
							.getAppointmentBeginDateAndTime())
					+ 1;

			if (differenceBetweenNowAndFirstAppointment >= appointmentDurationInMinutes) {
				anzahlFreiePlätze = differenceBetweenNowAndFirstAppointment / appointmentDurationInMinutes;
				if (anzahlFreiePlätze > 3)
					anzahlFreiePlätze = 3;
				nextThreeSuggestionDatesForWorkingAppointment.add(dateAndTimeOfNow);
				// System.out.println("First Suggestion is now at " + dateAndTimeOfNow + "!");
				sizeOfTheThreeSugestionList += 1;
				anzahlFreiePlätze -= 1;
				if (anzahlFreiePlätze >= 1) {
					while (anzahlFreiePlätze > 0 && sizeOfTheThreeSugestionList < 3) {
						LocalDateTime lastSuggestion = nextThreeSuggestionDatesForWorkingAppointment
								.get(nextThreeSuggestionDatesForWorkingAppointment.size() - 1);
						LocalDateTime endOfLastSuggestion = lastSuggestion.plusMinutes(appointmentDurationInMinutes);
						nextThreeSuggestionDatesForWorkingAppointment.add(endOfLastSuggestion);
						// System.out.println("Suggestion after suggestion: " + endOfLastSuggestion);
						sizeOfTheThreeSugestionList += 1;
						anzahlFreiePlätze -= 1;
					}
				}
			}
			for (int i = 0; i < arrayAllAppointmentsAfterNow.size(); i++) {
				if (sizeOfTheThreeSugestionList < 3) {
					if (i + 1 != arrayAllAppointmentsAfterNow.size()) {
						Appointment appointment1 = arrayAllAppointmentsAfterNow.get(i);
						Appointment nextAppointment = arrayAllAppointmentsAfterNow.get(i + 1);
						int difference = (int) ChronoUnit.MINUTES.between(appointment1.getAppointmentEndDateAndTime(),
								nextAppointment.getAppointmentBeginDateAndTime()) + 1;
						if (difference >= appointmentDurationInMinutes && !nextThreeSuggestionDatesForWorkingAppointment
								.contains(appointment1.getAppointmentEndDateAndTime())) {
							anzahlFreiePlätze = difference / appointmentDurationInMinutes;
							if (anzahlFreiePlätze > 3)
								anzahlFreiePlätze = 3;
							nextThreeSuggestionDatesForWorkingAppointment
									.add(appointment1.getAppointmentEndDateAndTime());
							// System.out.println("Suggestion at: " +
							// appointment1.getAppointmentEndDateAndTime());
							sizeOfTheThreeSugestionList += 1;
							anzahlFreiePlätze -= 1;
							if (anzahlFreiePlätze >= 1 && sizeOfTheThreeSugestionList < 3) {
								while (anzahlFreiePlätze > 0 && sizeOfTheThreeSugestionList < 3) {
									LocalDateTime lastSuggestion = nextThreeSuggestionDatesForWorkingAppointment
											.get(nextThreeSuggestionDatesForWorkingAppointment.size() - 1);
									LocalDateTime endOfLastSuggestion = lastSuggestion
											.plusMinutes(appointmentDurationInMinutes);
									nextThreeSuggestionDatesForWorkingAppointment.add(endOfLastSuggestion);
									// System.out.println("Suggestion after suggestion: " + endOfLastSuggestion);
									sizeOfTheThreeSugestionList += 1;
									anzahlFreiePlätze -= 1;
								}
							}
						}
					}
				} else {
					// System.out.println("All suggestions found");
					break;
				}
			}
			// Attach to the last element
			if (sizeOfTheThreeSugestionList < 3) {
				LocalDateTime endOfLastAppointment = arrayAllAppointmentsAfterNow
						.get(arrayAllAppointmentsAfterNow.size() - 1)
						.getAppointmentEndDateAndTime();
				if (!nextThreeSuggestionDatesForWorkingAppointment.contains(endOfLastAppointment)) {
					nextThreeSuggestionDatesForWorkingAppointment.add(endOfLastAppointment);
					// System.out.println("Suggestion at end of existing appointments: " +
					// endOfLastAppointment);
					sizeOfTheThreeSugestionList += 1;
				}
			}
			// If lists size is still smaller than 3, attach suggestion to the end of the
			// last suggestion
			while (sizeOfTheThreeSugestionList < 3) {
				LocalDateTime lastSuggestion = nextThreeSuggestionDatesForWorkingAppointment
						.get(nextThreeSuggestionDatesForWorkingAppointment.size() - 1);
				LocalDateTime endOfLastSuggestion = lastSuggestion.plusMinutes(appointmentDurationInMinutes);
				nextThreeSuggestionDatesForWorkingAppointment.add(endOfLastSuggestion);
				// System.out.println("Suggestion after last suggestion: " +
				// endOfLastSuggestion);
				sizeOfTheThreeSugestionList += 1;
			}
		}
		return nextThreeSuggestionDatesForWorkingAppointment;
	}

	public void printSuggestionOfThreeNextAvailableWorkingAppointmentDatesOnWorkingPlatform(
			Set<String> idsOfWorksToPerformDuringNewWorkingAppointment,
			String workingPlatformIdOfPlatformRelatedToAppointment) {
		ArrayList<LocalDateTime> allNextThreeSuggestionDatesAndTimesForWorkingAppointment = this
				.getSuggestionOfThreeNextAvailableWorkingAppointmentDatesOnWorkingPlatform(
						idsOfWorksToPerformDuringNewWorkingAppointment,
						workingPlatformIdOfPlatformRelatedToAppointment);
		int counter = 1;
		System.out.println();
		for (LocalDateTime localDateTime : allNextThreeSuggestionDatesAndTimesForWorkingAppointment) {
			System.out
					.println("Suggestion " + counter + ": On " + DateTimeFormatter.ISO_LOCAL_DATE.format(localDateTime)
							+ " at " + DateTimeFormatter.ISO_LOCAL_TIME.format(localDateTime));
			counter += 1;
		}
	}

	public void printSuggestionOfThreeNextAvailableWorkingAppointmentDatesForEachWorkingPlatform(
			Set<String> idsOfWorksToPerformDuringNewWorkingAppointment) {
		Set<WorkingPlatform> allWorkingPlatforms = PLATFORM_SERVICE.getAllWorkingPlatforms();
		Set<String> idOfAllWorkingPlatforms = new HashSet<>();
		allWorkingPlatforms.forEach(platform -> idOfAllWorkingPlatforms.add(platform.getWorkingPlatformId()));
		System.out.println("\n---Suggestions for each working platform:---");
		for (String string : idOfAllWorkingPlatforms) {
			System.out.print("Suggestions for " + string);
			this.printSuggestionOfThreeNextAvailableWorkingAppointmentDatesOnWorkingPlatform(
					idsOfWorksToPerformDuringNewWorkingAppointment, string);
			System.out.println();
		}
	}

}