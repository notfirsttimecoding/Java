package de.hs_kl.staab.planner.data;

import java.util.HashSet;
import java.util.Set;

import de.hs_kl.staab.planner.Customer;

public class CustomerData {

	private Set<Customer> allCustomers = new HashSet<>();

	public Set<Customer> getAllCustomers() {
		return allCustomers;
	}

	public void addCustomerToCustomerData(Customer customerToAdd) {
		this.allCustomers.add(customerToAdd);
	}

	public void removeCustomerFromCustomerData(Customer customerToRemove) {
		this.allCustomers.remove(customerToRemove);
	}
}
