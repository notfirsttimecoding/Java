package de.hs_kl.staab.planner.data;

import java.util.HashSet;
import java.util.Set;

import de.hs_kl.staab.planner.Vehicle;

public class VehicleData {

	private Set<Vehicle> allVehicles = new HashSet<>();

	public Set<Vehicle> getAllVehicles() {
		return allVehicles;
	}

	public void addVehicleToVehicleData(Vehicle vehicleToAdd) {
		this.allVehicles.add(vehicleToAdd);
	}

	public void removeVehicleFromVehicleData(Vehicle vehicleToRemove) {
		this.allVehicles.remove(vehicleToRemove);

	}

}
