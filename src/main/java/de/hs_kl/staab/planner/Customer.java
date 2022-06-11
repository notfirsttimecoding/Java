package de.hs_kl.staab.planner;

import java.util.HashSet;
import java.util.Set;

public class Customer {

	private static int CUSTOMER_ID_COUNTER = 1;
	private static final String CUSTOMER_ID_PREFIX = "C-";

	private final String customerId;

	private String customerFirstName;
	private String customerLastName;
	private String street;
	private String city;
	private String email;
	private String phoneNumber;
	private int houseNumber;
	private int postalCode;
	private Set<Vehicle> vehiclesOfTheCustomer = new HashSet<>();

	public Customer(String customerFirstName, String customerLastName, String street, int houseNumber, int postalCode,
			String city, String phoneNumber, String email) {
		this.customerId = CUSTOMER_ID_PREFIX + CUSTOMER_ID_COUNTER++;
		this.customerFirstName = customerFirstName;
		this.customerLastName = customerLastName;
		this.street = street;
		this.houseNumber = houseNumber;
		this.postalCode = postalCode;
		this.city = city;
		this.phoneNumber = phoneNumber;
		this.email = email;

	}

	@Override
	public String toString() {
		return "Customer ID: " + customerId + " (Name: " + customerFirstName + " " + customerLastName + ", address: "
				+ street + " " + houseNumber + ", " + postalCode + " " + city + ", email: " + email + ", phone number: "
				+ phoneNumber + ", vehicles of the customer: " + vehiclesOfTheCustomer + "]";
	}

	public void addVehicleToVehicleSet(Vehicle vehicleToAddToSet) {
		this.vehiclesOfTheCustomer.add(vehicleToAddToSet);
	}

	public void removeVehicleFromVehicleSet(Vehicle vehicleToRemoveFromSet) {
		this.vehiclesOfTheCustomer.remove(vehicleToRemoveFromSet);
	}

	public String getCustomerId() {
		return customerId;
	}

	public String getCustomerFirstName() {
		return customerFirstName;
	}

	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	public String getCustomerLastName() {
		return customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(int houseNumber) {
		this.houseNumber = houseNumber;
	}

	public int getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(int postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Set<Vehicle> getVehiclesOfCustomer() {
		return vehiclesOfTheCustomer;
	}

	public String getFullName() {
		return customerFirstName + " " + customerLastName;
	}

}
