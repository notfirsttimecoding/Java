package de.hs_kl.staab.planner.data;

import java.util.HashSet;
import java.util.Set;

import de.hs_kl.staab.planner.Work;

public class WorkData {

	private Set<Work> allWorks = new HashSet<>();

	public Set<Work> getAllWorks() {
		return allWorks;
	}

	public void addWorkToWorkData(Work workToAdd) {
		this.allWorks.add(workToAdd);
	}

	public void removeWorkFromWorkData(Work workToRemove) {
		this.allWorks.remove(workToRemove);
	}
}
