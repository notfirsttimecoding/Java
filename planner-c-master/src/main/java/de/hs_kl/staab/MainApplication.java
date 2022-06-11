package de.hs_kl.staab;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.hs_kl.staab.planner.CleaningAppointmentType;
import de.hs_kl.staab.planner.WorkingAppointmentStatus;
import de.hs_kl.staab.planner.services.CustomerService;
import de.hs_kl.staab.planner.services.PlannerService;
import de.hs_kl.staab.planner.services.UserService;
import de.hs_kl.staab.planner.services.VehicleService;
import de.hs_kl.staab.planner.services.WorkService;
import de.hs_kl.staab.planner.services.WorkingPlatformService;

/**
 * <p>
 * This class contains the main function and is called upon start. It starts
 * Spring Boot, which starts an application server and runs your application in
 * this application server so that it can be reached in the Network.
 * </p>
 * 
 * <p>
 * If you now enter the following URL in the browser, you can access the
 * Swagger-Interface of the application: <a href=
 * "http://localhost:8080/swagger-ui/">http://localhost:8080/swagger-ui/</a>.
 * </p>
 *
 * @author Staab
 */
@SpringBootApplication
public class MainApplication {
	static WorkService workService = WorkService.getInstance();
	static WorkingPlatformService workingPlatformService = WorkingPlatformService.getInstance();
	static VehicleService vehicleService = VehicleService.getInstance();
	static CustomerService customerService = CustomerService.getInstance();
	static PlannerService plannerService = PlannerService.getInstance();
	static UserService userService = UserService.getInstance();

	public static void main(String[] args) {

		System.out.println("Now the application is started!\n");
		// Wenn Sie OHNE REST arbeiten wollen, können Sie die folgende Zeile
		// und die Klasse PlannerController löschen!
		// SpringApplication.run(MainApplication.class, args);

		// Wenn Sie MIT REST arbeiten wollen, können Sie die folgende Zeile
		// und die entsprechende Methode löschen!
		initializeObjects();
		runApplication();
	}

//Initialize objects needed for testing of all requirements
	private static void initializeObjects() {
		workService.createAndAddNewWork("Change Tires", 30);
		workService.createAndAddNewWork("Maintenance and inspection", 30);
		workService.createAndAddNewWork("Clean vehicle interior", 30);
		workService.createAndAddNewWork("Engine change", 120);
		workService.createAndAddNewWork("Oil change", 20);
		workService.createAndAddNewWork("Change Tires", 70); // different duration with same name is allowed
		workService.createAndAddNewWork("Chiptuning Engine", 60);

		workingPlatformService.createAndAddNewWorkingPlatform("Working Platform 1");
		workingPlatformService.createAndAddNewWorkingPlatform("Working Platform 2");
		workingPlatformService.createAndAddNewWorkingPlatform("WorkingPlatform 3");
		workingPlatformService.createAndAddNewWorkingPlatform("WorkingPlatform 4");

		vehicleService.createAndAddNewVehicle("Opel", "Corsa", 2020, "KL-TW-1906", LocalDateTime.of(2020, 6, 19, 0, 0));
		vehicleService.createAndAddNewVehicle("Opel", "Corsa-E", 2021, "KL-JW-123",
				LocalDateTime.of(2021, 11, 11, 0, 0));
		vehicleService.createAndAddNewVehicle("Audi", "e-tron GT", 2021, "ZW-CM-23",
				LocalDateTime.of(2021, 2, 23, 0, 0));
		vehicleService.createAndAddNewVehicle("Audi", "A3", 2008, "ZW-JN-156", LocalDateTime.of(2021, 11, 11, 0, 0));

		customerService.createAndAddNewCustomer("Tom", "Williard", "Blumenstraße", 8, 67697, "Otterberg", "0162-123456",
				"test@123.de");
		customerService.createAndAddNewCustomer("Jakub", "Wachowiak", "Musterstraße", 99, 99999, "Berlin",
				"0631-124562", "jw@gmail.com");
		customerService.createAndAddNewCustomer("Caroline", "Mock", "Stadtrand", 007, 66538, "Neunkirchen",
				"0172-13288456", "cm@mail.com");
		customerService.addVehicleToCustomersVehicles("C-2", "ZW-JN-156");
		customerService.addVehicleToCustomersVehicles("C-2", "KL-JW-123");
		customerService.addVehicleToCustomersVehicles("C-1", "KL-TW-1906");

		userService.createAndAddNewDispatcherUser("towi1001", "Tom", "Williard");
		userService.createAndAddNewClientadvisorUser("jawa1002", "Jakub", "Wachowiak");
		userService.createAndAddNewCarMechanicUser("camo1002", "Caroline", "Mock");
		userService.createAndAddNewCarMechanicUser("ABT Sportsline", "Daniel", "Abt");
		userService.createAndAddNewCarMechanicUser("JP Performance", "Jean Pierre", "Kraemer");
		userService.createAndAddNewDispatcherUser("UserToRemove", "Use", "Remover");

		Set<String> worksToPerform1 = new HashSet<>();
		worksToPerform1.add("W-3");
		worksToPerform1.add("W-2");
		Set<String> worksToPerform2 = new HashSet<>();
		worksToPerform2.add("W-4");
		worksToPerform2.add("W-5");
		plannerService.createAndAddNewWorkingAppointment(worksToPerform1, "C-2", "KL-JW-123", "WP-2",
				LocalDateTime.of(2021, 12, 28, 9, 30), "camo1002");
		plannerService.createAndAddNewWorkingAppointment(worksToPerform2, "C-1", "KL-TW-1906", "WP-3",
				LocalDateTime.of(2021, 12, 26, 10, 00), "camo1002");

		plannerService.createAndAddNewConsultingAppointment("C-1", 60, LocalDateTime.of(2021, 12, 22, 8, 00),
				"jawa1002");
		plannerService.createAndAddNewConsultingAppointment("C-2", 30, LocalDateTime.of(2021, 12, 24, 9, 30),
				"jawa1002");
		plannerService.createAndAddNewConsultingAppointment("C-3", 45, LocalDateTime.of(2021, 12, 23, 11, 00),
				"jawa1002");

		plannerService.createAndAddNewCleaningAppointment(CleaningAppointmentType.QUICK, "WP-4",
				LocalDateTime.of(2021, 12, 25, 8, 00), "towi1001");
		plannerService.createAndAddNewCleaningAppointment(CleaningAppointmentType.INTENSIVE, "WP-2",
				LocalDateTime.of(2021, 12, 22, 8, 0), "towi1001");
		plannerService.createAndAddNewCleaningAppointment(CleaningAppointmentType.INTENSIVE, "WP-3",
				LocalDateTime.of(2021, 12, 24, 8, 30), "towi1001");

	}

	// MVP 01
	@SuppressWarnings("unused")
	private static void testWorkClasses() {
		System.out.println("\n---Testing Work, WorkService and WorkData---");
		System.out.println("All works: " + workService.getAllWorks());
		workService.updateWorkName("W-2", "Test Change of the Work Name");
		workService.updateWorkDuration("W-3", 45);
		System.out.println("All works after updating name of W-2 and duration of W-3: " + workService.getAllWorks());
		workService.removeWork("W-1");
		System.out.println("After removing Work with ID W-1: " + workService.getAllWorks());

		System.out.println("\n---Error testing in Work, WorkService and WorkData---");
		workService.updateWorkName("W-100", "Wrong ID should not work");
		workService.createAndAddNewWork("Chiptuning Engine", 60);
	}

	// MVP 02
	@SuppressWarnings("unused")
	private static void testWorkingPlatformClasses() {
		System.out.println("\n---Testing WorkingPlatform, WorkingPlatformService and WorkingPlatformData---");
		System.out.println("All working platforms: " + workingPlatformService.getAllWorkingPlatforms());
		workingPlatformService.updateWorkingPlatformName("WP-1", "Test change Name of Working Platform 1");
		System.out.println("All working platforms after changing name of WP-1: "
				+ workingPlatformService.getAllWorkingPlatforms());
		workingPlatformService.removeWorkingPlatform("WP-1");
		System.out.println(
				"After removing WorkingPlatform with ID WP-1: " + workingPlatformService.getAllWorkingPlatforms());

		System.out.println("\n---Error Testing in WorkingPlatform, WorkingPlatformService and WorkingPlatformData---");
		workingPlatformService.updateWorkingPlatformName("Test", "Wrong ID should not work");
		workingPlatformService.createAndAddNewWorkingPlatform("Working Platform 2");
	}

	// MVP03
	@SuppressWarnings("unused")
	private static void testVehicleClasses() {
		System.out.println("\n---Testing Vehicle, Vehicle Service and VehicleData---");
		System.out.println("All vehicles: " + vehicleService.getAllVehicles());
		vehicleService.updateVehicleLicensePlate("KL-TW-1906", "KL-TW-196");
		System.out.println("All vehicles after updating a license plate: " + vehicleService.getAllVehicles());
		vehicleService.removeVehicle("ZW-JN-156");
		System.out.println("After removing Vehicle with License Plate KL-JW-123: " + vehicleService.getAllVehicles());

		System.out.println("\n---Error testing in Vehicle, Vehicle Service and VehicleData---");
		vehicleService.updateVehicleLicensePlate("Wrong License Plate should not work", "test");
		vehicleService.createAndAddNewVehicle("Test", "Same", 5, "KL-JW-123", LocalDateTime.of(2021, 11, 11, 0, 0));

	}

	// MVP04
	@SuppressWarnings("unused")
	private static void testCustomerClasses() {
		System.out.println("\n---Testing Customer---");
		System.out.println("All customers: " + customerService.getAllCustomers());
		System.out.println("Vehicles of customer C-1: " + customerService.getVehiclesOfTheCustomer("C-1"));
		System.out.println("Vehicles of customer C-2: " + customerService.getVehiclesOfTheCustomer("C-2"));

		System.out.println("\n---Error testing in Vehicle, Vehicle Service and VehicleData---");
		customerService.createAndAddNewCustomer("Jakub", "Wachowiak", "Virginiastraße", 14, 66482, "Zweibrücken",
				"0631-124562", "jw@gmail.com");

	}

	// MMP01
	@SuppressWarnings("unused")
	private static void testUserClasses() {
		System.out.println("\n---Testing Users---");
		System.out.println("All users: " + userService.getAllUsers());
		userService.updateNameOfUser("towi1001", "Tom", "Williard");
		userService.removeUser("UserToRemove");
		System.out.println("After removing user UserToRemove: " + userService.getAllUsers());

		System.out.println("---Error testing in User Classes---");
		userService.createAndAddNewDispatcherUser("towi1001", "Tom", "Williard");
	}

	// MVP05, MMP02, MMP03, MLP02
	@SuppressWarnings("unused")
	private static void testWorkingAppointment() {
		System.out.println("\n---Testing WorkingAppointment---");
		plannerService.changeWorkingAppointmentStatusOf_To("A-2", WorkingAppointmentStatus.CANCELLED);
		System.out.println("All Working Appointments until now: " + plannerService.getAllWorkingAppointments());
		plannerService.updateWorkingAppointment("A-1", "WP-4", LocalDateTime.of(2021, 12, 27, 16, 00));
		System.out.println("After updating of A-1: " + plannerService.getAllWorkingAppointments());
		plannerService.removeAppointment("A-1");
		System.out.println("After deleting of A-1: " + plannerService.getAllWorkingAppointments());

		System.out.println("All Working Appointments: " + plannerService.getAllWorkingAppointments() + "\n");

		System.out.println("---Error testing in WorkingAppointment---");
		Set<String> worksToPerform1 = new HashSet<>();
		worksToPerform1.add("W-3");
		worksToPerform1.add("W-2");
		Set<String> worksToPerform2 = new HashSet<>();
		worksToPerform2.add("W-4");
		worksToPerform2.add("W-5");
		plannerService.createAndAddNewWorkingAppointment(worksToPerform2, "C-1", "KL-JW-123", "WP-3",
				LocalDateTime.of(2021, 12, 26, 10, 00), "camo1002");
		plannerService.createAndAddNewWorkingAppointment(worksToPerform2, "C-1", "KL-TW-1", "WP-3",
				LocalDateTime.of(2021, 12, 26, 10, 00), "camo1002");
		plannerService.createAndAddNewWorkingAppointment(worksToPerform2, "C-100", "KL-TW-16", "WP-3",
				LocalDateTime.of(2021, 12, 26, 10, 00), "camo1002");
		plannerService.createAndAddNewWorkingAppointment(worksToPerform2, "C-100", "KL-TW-16", "WP-99",
				LocalDateTime.of(2021, 12, 26, 10, 00), "camo1002");
		plannerService.updateWorkingAppointment("A-100", "WP-2", LocalDateTime.of(2021, 12, 25, 10, 30));
		System.err.println("---Testing appointment overlaps---");
		plannerService.createAndAddNewWorkingAppointment(worksToPerform2, "C-1", "KL-TW-1906", "WP-2",
				LocalDateTime.of(2021, 12, 26, 10, 5), "camo1002");

	}

	// MVP05, MMP02
	@SuppressWarnings("unused")
	private static void testConsultingAppointment() {
		System.out.println("\n---Testing ConsultingAppointment---");
		System.out.println("All Consulting Appointments until now: " + plannerService.getAllConsultingAppointments());
		plannerService.updateConsultingAppointment("A-5", 30, LocalDateTime.of(2021, 12, 22, 14, 30));
		System.out.println("After updating of A-5: " + plannerService.getAllConsultingAppointments());
		plannerService.removeAppointment("A-5");
		System.out.println("After deleting of A-5: " + plannerService.getAllConsultingAppointments());

		System.out.println("All Consulting Appointments: " + plannerService.getAllConsultingAppointments() + "\n");

		System.out.println("---Error testing in ConsultingAppointment---");
		plannerService.createAndAddNewConsultingAppointment("C-100", 60, LocalDateTime.of(2021, 12, 22, 13, 30),
				"jawa1002");
		plannerService.updateConsultingAppointment("A-101", 15, LocalDateTime.of(2021, 12, 20, 10, 00));

		System.err.println("---Testing appointment overlaps---");
		plannerService.createAndAddNewConsultingAppointment("C-2", 30, LocalDateTime.of(2021, 12, 24, 9, 30),
				"jawa1002");
	}

	// MVP05, MMP02, MLP02
	@SuppressWarnings("unused")
	private static void testCleaningAppointment() {
		System.out.println("\n---Testing CleaningAppointment---");
		System.out.println("All Cleaning Appointments until now: " + plannerService.getAllCleaningAppointments());
		plannerService.updateCleaningAppointment("A-8", CleaningAppointmentType.QUICK, "WP-2",
				LocalDateTime.of(2021, 12, 24, 16, 00));
		System.out.println("After updating of A-9:" + plannerService.getAllCleaningAppointments());
		plannerService.removeAppointment("A-8");
		System.out.println("After removing of A-9:" + plannerService.getAllCleaningAppointments());

		System.out.println("All Cleaning Appointments: " + plannerService.getAllCleaningAppointments() + "\n");

		System.out.println("---Error testing in CleaningAppointment---");
		plannerService.createAndAddNewCleaningAppointment(CleaningAppointmentType.QUICK, "WP-200",
				LocalDateTime.of(2021, 12, 25, 8, 00), "towi1001");
		System.err.println("---Testing appointment overlaps---");
		plannerService.createAndAddNewCleaningAppointment(CleaningAppointmentType.QUICK, "WP-4",
				LocalDateTime.of(2021, 12, 25, 8, 10), "towi1001");
	}

	// MVP05
	@SuppressWarnings("unused")
	private static void testErrorOutputOfUpdateMethodsOfAppointmentClasses() {
		System.out.println("\n---Error testing of update Methods in Appointment Classes---");

		// Test: Update of WorkingAppointment on ConsultingAppointment
		plannerService.updateWorkingAppointment("A-4", "WP-2", LocalDateTime.of(2021, 12, 25, 10, 30));

		// Test: Update of ConsultingAppointment on CleaningAppointment
		plannerService.updateConsultingAppointment("A-8", 30, LocalDateTime.of(2021, 12, 20, 10, 00));

		// Test: Update of CleaningAppointment on WorkingAppointment
		plannerService.updateCleaningAppointment("A-3", CleaningAppointmentType.QUICK, "WP-2",
				LocalDateTime.of(2021, 12, 24, 16, 00));
	}

	// MVP06
	@SuppressWarnings("unused")
	private static void testWeeklyOverview() {
		System.out.println("\n--- Testing Weekly Overview:---");
		System.out.println("Testing for week 50:");
		plannerService.printSortedAppointmentOverviewOfWeek(50);
		plannerService.createAndAddNewConsultingAppointment("C-2", 30, LocalDateTime.of(2021, 12, 13, 0, 0),
				"jawa1002");
		System.out.println("Testing for week 50 after adding an Appointment on a Date in this week:");
		plannerService.printSortedAppointmentOverviewOfWeek(50);
		System.out.println("\nTesting for week 51:");
		plannerService.printSortedAppointmentOverviewOfWeek(51);
		System.out.println("\nTesting for week 52:");
		plannerService.printSortedAppointmentOverviewOfWeek(52);
	}

	// MMP04, MMP03
	@SuppressWarnings("unused")
	private static void testYesterdaysOverview() {
		System.out.println("\n---Testing Overview Of Yesterday---");
		plannerService.printAllWorkingAppointmentsOfYesterday();
		System.out.println("---Overview of yesterday after adding an appointment:");
		Set<String> worksToPerform2 = new HashSet<>();
		worksToPerform2.add("W-4");
		worksToPerform2.add("W-5");
		plannerService.createAndAddNewWorkingAppointment(worksToPerform2, "C-2", "KL-JW-123", "WP-2",
				LocalDateTime.now()
						.minusDays(1),
				"camo1002");
		plannerService.createAndAddNewWorkingAppointment(worksToPerform2, "C-2", "KL-JW-123", "WP-1",
				LocalDateTime.now()
						.minusDays(1)
						.minusMinutes(180),
				"camo1002");
		plannerService.changeWorkingAppointmentStatusOf_To("A-9", WorkingAppointmentStatus.FINISHED);
		plannerService.changeWorkingAppointmentStatusOf_To("A-10", WorkingAppointmentStatus.FINISHED);
		plannerService.printAllWorkingAppointmentsOfYesterday();
	}

	// MMP05
	@SuppressWarnings("unused")
	private static void testHistoryOfWorkingAppointmentsForSpecificVehicle() {
		System.out.println("\n---Testing History Of Working Appointments---");
		Set<String> worksToPerform2 = new HashSet<>();
		worksToPerform2.add("W-4");
		worksToPerform2.add("W-5");
		plannerService.createAndAddNewWorkingAppointment(worksToPerform2, "C-2", "KL-JW-123", "WP-2",
				LocalDateTime.now()
						.minusDays(1),
				"camo1002");
		plannerService.changeWorkingAppointmentStatusOf_To("A-9", WorkingAppointmentStatus.FINISHED);
		plannerService.createAndAddNewWorkingAppointment(worksToPerform2, "C-2", "KL-JW-123", "WP-3",
				LocalDateTime.now(), "camo1002");
		plannerService.changeWorkingAppointmentStatusOf_To("A-10", WorkingAppointmentStatus.FINISHED);
		plannerService.createAndAddNewWorkingAppointment(worksToPerform2, "C-2", "KL-JW-123", "WP-2",
				LocalDateTime.of(2022, 1, 2, 15, 0), "camo1002");
		plannerService.changeWorkingAppointmentStatusOf_To("A-11", WorkingAppointmentStatus.FINISHED);
		vehicleService.printHistoryOfFinishedWorkingAppointments("KL-JW-123");

		System.out.println("\n---Error testing for vehicle with no history of working appointments---");
		vehicleService.printHistoryOfFinishedWorkingAppointments("ZW-JN-156");
	}

	// MLP01
	@SuppressWarnings("unused")
	private static void testSortedDailyOverviewOfAllOpenWorkingAppointmentsFor() {
		System.out.println("\n---Testing Sorted Daily Overview Of All Open Working Appointments for Car Mechanic---");
		System.out.println("\nTesting for existing working appointments for car mechanic:");
		Set<String> worksToPerform1 = new HashSet<>();
		worksToPerform1.add("W-3");
		worksToPerform1.add("W-2");
		plannerService.createAndAddNewWorkingAppointment(worksToPerform1, "C-2", "KL-JW-123", "WP-2",
				LocalDateTime.now(), "JP Performance");
		plannerService.createAndAddNewWorkingAppointment(worksToPerform1, "C-2", "ZW-JN-156", "WP-3",
				LocalDateTime.now()
						.plusMinutes(60),
				"JP Performance"); // if checked after 23 o´clock only one will be
									// shown ;)
		plannerService.printAllSortedOpenWorkingAppointmentsOfTodayFor("JP Performance");
		System.out.println("\nError-Testing for non-existing working appointments for car mechanic:");
		plannerService.printAllSortedOpenWorkingAppointmentsOfTodayFor("camo1002");
	}

	// MLP030
	@SuppressWarnings("unused")
	private static void testCreateNextAvailableCleaningAppointment() {
		System.out.println("\n---Testing creation of next available cleaning appointment---");
		// test purpose only Appointments
		plannerService.createAndAddNewCleaningAppointment(CleaningAppointmentType.QUICK, "WP-2", LocalDateTime.now()
				.plusHours(1), "towi1001");
		plannerService.createAndAddNewCleaningAppointment(CleaningAppointmentType.INTENSIVE, "WP-2", LocalDateTime.now()
				.plusHours(2), "towi1001");
		Set<String> worksToPerform1 = new HashSet<>();
		worksToPerform1.add("W-3");
		worksToPerform1.add("W-2");
		plannerService.createAndAddNewWorkingAppointment(worksToPerform1, "C-2", "KL-JW-123", "WP-2",
				LocalDateTime.now(), "JP Performance");
		plannerService.createAndAddNewWorkingAppointment(worksToPerform1, "C-2", "KL-JW-123", "WP-2",
				LocalDateTime.now()
						.plusDays(1),
				"JP Performance");
		System.out.println("Before:");
		plannerService.printAppointmentsDetailedFor(
				plannerService.getAllOpenSortedAppointmentsOnWorkingPlatformAfterNow("WP-2"));
		System.out.print("\n--->Creating next available: ");
		plannerService.createAndAddNextAvailableCleaningAppointment(CleaningAppointmentType.QUICK, "WP-2", "towi1001");
		System.out.println("\nAfter:");
		plannerService.printAppointmentsDetailedFor(
				plannerService.getAllOpenSortedAppointmentsOnWorkingPlatformAfterNow("WP-2"));

	}

	// MLP040
	@SuppressWarnings("unused")
	private static void testSuggestionOfThreeNextAvailableWorkingAppointmentDates() {
		System.out.println(
				"\n---Testing suggestion of three next available working appointment dates for one working platform and for all working platforms---");
		workService.createAndAddNewWork("Adjusting lights", 15);
		workService.createAndAddNewWork("Refilling Oil", 15);
		workService.createAndAddNewWork("Gearbox change", 180);
		workService.createAndAddNewWork("Timing chain replacement", 300);
		workService.createAndAddNewWork("Fixing windshield", 120);
		workService.createAndAddNewWork("Change painting of the car to white", 420);
		workService.createAndAddNewWork("Deep cleaning the car", 60);
		workService.createAndAddNewWork("Troubleshooting the problem with blinking of high beam", 120);

		// 30 minutes
		Set<String> worksToPerformSuggestion1 = new HashSet<>();
		worksToPerformSuggestion1.add("W-8");
		worksToPerformSuggestion1.add("W-9");

		// 480 minutes
		Set<String> worksToPerformSuggestion2 = new HashSet<>();
		worksToPerformSuggestion2.add("W-10");
		worksToPerformSuggestion2.add("W-11");

		// 180 minutes
		Set<String> worksToPerformSuggestion3 = new HashSet<>();
		worksToPerformSuggestion3.add("W-12");
		worksToPerformSuggestion3.add("W-14");

		// 540 minutes
		Set<String> worksToPerformSuggestion4 = new HashSet<>();
		worksToPerformSuggestion4.add("W-13");
		worksToPerformSuggestion4.add("W-15");
		// 60 minutes
		Set<String> worksToPerformSuggestion5 = new HashSet<>();
		worksToPerformSuggestion5.add("W-14");

		plannerService.createAndAddNewWorkingAppointment(worksToPerformSuggestion1, "C-2", "KL-JW-123", "WP-1",
				LocalDateTime.now()
						.plusMinutes(30),
				"ABT Sportsline");
		plannerService.createAndAddNewWorkingAppointment(worksToPerformSuggestion1, "C-2", "KL-JW-123", "WP-1",
				LocalDateTime.now()
						.plusMinutes(180),
				"JP Performance");

		plannerService.createAndAddNewWorkingAppointment(worksToPerformSuggestion5, "C-2", "KL-JW-123", "WP-1",
				LocalDateTime.now()
						.plusMinutes(120),
				"camo1002");
		plannerService.createAndAddNewWorkingAppointment(worksToPerformSuggestion1, "C-2", "KL-JW-123", "WP-1",
				LocalDateTime.now()
						.plusMinutes(60),
				"camo1002");
		plannerService.createAndAddNewWorkingAppointment(worksToPerformSuggestion1, "C-2", "KL-JW-123", "WP-2",
				LocalDateTime.now()
						.plusMinutes(30),
				"camo1002");

		System.out.println("Existing Appointments: ");
		plannerService.printAppointmentsDetailedFor(
				plannerService.getAllOpenSortedAppointmentsOnWorkingPlatformAfterNow("WP-1"));
		System.out.print("\nSuggestions:\n");
		// This function returns the next three available dates on a specified
		// WorkingPlatform
		plannerService.printSuggestionOfThreeNextAvailableWorkingAppointmentDatesOnWorkingPlatform(
				worksToPerformSuggestion1, "WP-1");
		// this one is for the next available dates for EACH working platform
		plannerService.printSuggestionOfThreeNextAvailableWorkingAppointmentDatesForEachWorkingPlatform(
				worksToPerformSuggestion1);
		// We understood the task as in the output - if 3 dates were to be issued across
		// all platforms, you would only have to put all suggestions in a list and
		// select the 3 earliest ones.

	}

	// Always test each method above individually by calling it here
	private static void runApplication() {
		testSuggestionOfThreeNextAvailableWorkingAppointmentDates();
	}
}
