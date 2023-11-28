package dataviewerfinal;

import java.util.TreeSet;

// A class to hold the data for a country
public class Country implements Comparable<Country> {
	// Instance variables name and treeset of states
	private String name;
	private TreeSet<State> m_dataStates;
	
	// Constructor for country with name and an empty list of states
	public Country(String name) {
		this.name = name;
		this.m_dataStates = new TreeSet<State>();
	}
	
	// Adds a state to the treeset
	public void addState(String state) {
		m_dataStates.add(new State(state));
	}
	
	// Returns the treeset of states
	public TreeSet<State> getStates(){
		return(m_dataStates);
	}
	
	// Gets the name of the country
	public String getName() {
		return(this.name);
	}
	
	// Compares one country to another using the name
	@Override 
	public int compareTo(Country c) {
		return(this.name.compareTo(c.name));
	}
	
	// String representation of the country
	@Override
	public String toString() {
		return(this.name);
	}

}
