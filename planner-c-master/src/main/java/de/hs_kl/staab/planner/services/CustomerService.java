package de.hs_kl.staab.planner.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import de.hs_kl.staab.planner.Customer;
import de.hs_kl.staab.planner.Vehicle;
import de.hs_kl.staab.planner.data.CustomerData;

public class CustomerService {

	private static CustomerService CUSTOMER_SERVICE;
	private final static CustomerData CUSTOMER_DATA = new CustomerData();
	private final static VehicleService VEHICLE_SERVICE = VehicleService.getInstance();

	/**
	 * Singleton-Pattern: Der Konstruktor kann nicht aufgerufen werden, sondern
	 * {@link #getInstance()} muss aufgerufen werden. So kann sichergestellt werden,
	 * dass nur eine einzige Instanz dieser Klasse erstellt wird.
	 */
	private CustomerService() {
	}

	/**
	 * Teil des Singleton-Patterns
	 * 
	 * @return Die einzige Instanz des CustomerService.
	 */
	public static CustomerService getInstance() {
		if (CUSTOMER_SERVICE == null) {
			CUSTOMER_SERVICE = new CustomerService();
		}
		return CUSTOMER_SERVICE;
	}

	public Set<Customer> getAllCustomers() {
		return CUSTOMER_DATA.getAllCustomers();
	}

	private boolean checkIfCustomerDuplicateAlreadyExists(String customerFirstName, String customerLastName,
			String street, int houseNumber, int postalCode, String city) {
		Set<Customer> allCustomers = CUSTOMER_DATA.getAllCustomers();
		Boolean customerDetailsAlreadyExist = false;
		for (Customer customer : allCustomers) {
			if (customer.getCustomerFirstName()
					.equals(customerFirstName)
					&& customer.getCustomerLastName()
							.equals(customerLastName)
					&& customer.getStreet()
							.equals(street)
					&& customer.getHouseNumber() == houseNumber && customer.getPostalCode() == postalCode
					&& customer.getCity()
							.equals(city)) {
				customerDetailsAlreadyExist = true;
				break;
			}
		}
		return customerDetailsAlreadyExist;
	}

	public void createAndAddNewCustomer(String customerFirstName, String customerLastName, String street,
			int houseNumber, int postalCode, String city, String phoneNumber, String email) {
		if (!checkIfCustomerDuplicateAlreadyExists(customerFirstName, customerLastName, street, houseNumber, postalCode,
				city)) {
			CUSTOMER_DATA.addCustomerToCustomerData(new Customer(customerFirstName, customerLastName, street,
					houseNumber, postalCode, city, phoneNumber, email));
		} else {
			System.err.println(
					"ERROR: Customer with the same name and the same address already exists. Please choose different details or check if the customer you want to create already exists by using getAllCustomers()");
		}

	}

	public Optional<Customer> getCustomerById(String customerId) {
		Set<Customer> allCustomers = CUSTOMER_DATA.getAllCustomers();
		Customer foundCustomer = null;
		for (Customer customer : allCustomers) {
			if (customer.getCustomerId()
					.equals(customerId)) {
				foundCustomer = customer;
				break;
			}
		}
		return Optional.ofNullable(foundCustomer);
	}

	public Customer getCustomerToUpdate(String customerIdOfCustomerToUpdate) {
		Optional<Customer> optionalCustomerToUpdate = getCustomerById(customerIdOfCustomerToUpdate);
		if (optionalCustomerToUpdate.isPresent()) {
			return optionalCustomerToUpdate.get();
		} else {
			throw new IllegalAccessError("ERROR: ID does not exist");
		}
	}

	public void updateWholeCustomer(String customerIdOfCustomerToUpdate, String newCustomerFirstName,
			String newCustomerLastName, String newStreet, int newHouseNumber, int newPostalCode, String newCity,
			String newPhoneNumber, String newEmail) {
		Optional<Customer> optionalCustomerToUpdate = getCustomerById(customerIdOfCustomerToUpdate);
		if (optionalCustomerToUpdate.isPresent()) {
			Customer customerToUpdate = optionalCustomerToUpdate.get();
			customerToUpdate.setCustomerFirstName(newCustomerFirstName);
			customerToUpdate.setCustomerLastName(newCustomerLastName);
			customerToUpdate.setStreet(newStreet);
			customerToUpdate.setHouseNumber(newHouseNumber);
			customerToUpdate.setPostalCode(newPostalCode);
			customerToUpdate.setCity(newCity);
			customerToUpdate.setPhoneNumber(newPhoneNumber);
			customerToUpdate.setEmail(newEmail);
		} else {
			System.err.println("ERROR: Customer with the ID: " + customerIdOfCustomerToUpdate
					+ " was not found. Use getAllCustomers() to see all the customers with their ID.");
		}
	}

	public void updateNameOfCustomer(String customerIdOfCustomerToUpdate, String newCustomerFirstName,
			String newCustomerLastName) {
		Optional<Customer> optionalCustomerToUpdate = getCustomerById(customerIdOfCustomerToUpdate);
		if (optionalCustomerToUpdate.isPresent()) {
			Customer customerToUpdate = optionalCustomerToUpdate.get();
			customerToUpdate.setCustomerFirstName(newCustomerFirstName);
			customerToUpdate.setCustomerLastName(newCustomerLastName);
		} else {
			System.err.println("ERROR: Customer with the ID: " + customerIdOfCustomerToUpdate
					+ " was not found. Use getAllCustomers() to see all the customers with their ID.");
		}
	}

	public void updateAddressOfCustomer(String customerIdOfCustomerToUpdate, String newStreet, int newHouseNumber,
			int newPostalCode, String newCity) {
		Optional<Customer> optionalCustomerToUpdate = getCustomerById(customerIdOfCustomerToUpdate);
		if (optionalCustomerToUpdate.isPresent()) {
			Customer customerToUpdate = optionalCustomerToUpdate.get();
			customerToUpdate.setStreet(newStreet);
			customerToUpdate.setHouseNumber(newHouseNumber);
			customerToUpdate.setPostalCode(newPostalCode);
			customerToUpdate.setCity(newCity);
		} else {
			System.err.println("ERROR: Customer with the ID: " + customerIdOfCustomerToUpdate
					+ " was not found. Use getAllCustomers() to see all the customers with their ID.");
		}
	}

	public void updateContactDetailsOfCustomer(String customerIdOfCustomerToUpdate, String newPhoneNumber,
			String newEmail) {
		Optional<Customer> optionalCustomerToUpdate = getCustomerById(customerIdOfCustomerToUpdate);
		if (optionalCustomerToUpdate.isPresent()) {
			Customer customerToUpdate = optionalCustomerToUpdate.get();
			customerToUpdate.setPhoneNumber(newPhoneNumber);
			customerToUpdate.setEmail(newEmail);
		} else {
			System.err.println("ERROR: Customer with the ID: " + customerIdOfCustomerToUpdate
					+ " was not found. Use getAllCustomers() to see all the customers with their ID.");
		}
	}

	public void removeCustomer(String customerIdOfCustomerToRemove) {
		Optional<Customer> optionalCustomerToRemove = getCustomerById(customerIdOfCustomerToRemove);
		if (optionalCustomerToRemove.isPresent()) {
			CUSTOMER_DATA.removeCustomerFromCustomerData(optionalCustomerToRemove.get());
		} else {
			System.err.println("ERROR: The Customer with the ID: " + customerIdOfCustomerToRemove
					+ " does not exist in the CustomerData. Try a different ID or use getAllCustomers() to see all the customers with their ID.");
		}
	}

	public void addVehicleToCustomersVehicles(String customerIdToAddVehicleTo,
			String vehicleLicensePlateOfVehicleToAdd) {
		Optional<Customer> optionalCustomerToAddVehicleTo = getCustomerById(customerIdToAddVehicleTo);
		if (optionalCustomerToAddVehicleTo.isPresent()) {
			Customer customerToAddVehicleTo = optionalCustomerToAddVehicleTo.get();
			Optional<Vehicle> optionalVehicleToAdd = VEHICLE_SERVICE
					.getVehicleByLicensePlate(vehicleLicensePlateOfVehicleToAdd);
			if (optionalVehicleToAdd.isPresent()) {
				if (!customerToAddVehicleTo.getVehiclesOfCustomer()
						.contains(optionalVehicleToAdd.get())) {
					customerToAddVehicleTo.addVehicleToVehicleSet(optionalVehicleToAdd.get());
				} else {
					System.err.println(
							"ERROR: The Vehicle you wanted to add to the customers vehicles is already listed as an vehicle of this customer.");
				}
			} else {
				System.err.println("ERROR: Vehicle with the license plate: " + vehicleLicensePlateOfVehicleToAdd
						+ " was not found. Use the getAllVehicles of the VehicleService to see all the vehicles with their license plate or create the vehicle first.");
			}
		} else {
			System.err.println("ERROR: Customer with the ID: " + customerIdToAddVehicleTo
					+ " was not found. Use getAllCustomers() to see all the customers with their ID.");
		}
	}

	public void removeVehicleFromCustomersVehicles(String customerIdToRemoveVehicleFrom,
			String vehicleLicensePlateOfVehicleToRemove) {
		Optional<Customer> optionalCustomerToRemoveVehicleFrom = getCustomerById(customerIdToRemoveVehicleFrom);
		if (optionalCustomerToRemoveVehicleFrom.isPresent()) {
			Customer customerToRemoveVehicleFrom = optionalCustomerToRemoveVehicleFrom.get();
			Optional<Vehicle> optionalVehicleToRemove = VEHICLE_SERVICE
					.getVehicleByLicensePlate(vehicleLicensePlateOfVehicleToRemove);
			if (optionalVehicleToRemove.isPresent()) {
				customerToRemoveVehicleFrom.removeVehicleFromVehicleSet(optionalVehicleToRemove.get());
			} else {
				System.err.println("ERROR: Vehicle with the license plate: " + vehicleLicensePlateOfVehicleToRemove
						+ " was not found. Use the getAllVehicles of the VehicleService to see all the vehicles with their license plate.");
			}
		} else {
			System.err.println("ERROR: Customer with the ID: " + customerIdToRemoveVehicleFrom
					+ " was not found. Use getAllCustomers() to see all the customers with their ID.");
		}
	}

	/**
	 * @return - Set of all vehicles that belongs to a given customerId or an empty
	 *         set if the customer doesn´t exist
	 */

	public Set<Vehicle> getVehiclesOfTheCustomer(String customerId) {
		Optional<Customer> optionalCustomer = getCustomerById(customerId);
		if (optionalCustomer.isPresent()) {
			return optionalCustomer.get()
					.getVehiclesOfCustomer();
		} else {
			return new HashSet<Vehicle>();
		}
	}
}
