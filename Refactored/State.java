package dataviewerfinal;

import java.util.TreeSet;

// Class to hold state data
public class State implements Comparable<State> {
	// A state has a name and a treeset of years
	private TreeSet<Year> years;
	private String name;
	
	// Constructor
	public State(String name) {
		this.name = name;
		this.years = new TreeSet<Year>();
	}
	
	// Add year to treeset
	public void addYear(Year year) {
		this.years.add(year);
	}
	
	// Get treeset of years
	public TreeSet<Year> getYears(){
		return(this.years);
	}
	
	// Get the name of the state
	public String getName() {
		return(this.name);
	}

	// Compare the names of the states
	@Override
	public int compareTo(State o) {
		return(this.name.compareTo(o.name));
	}
	
	// String representation of a state
	@Override
	public String toString() {
		return(this.name);
	}
}
