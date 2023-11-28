package dataviewerfinal;

import java.util.Map;
import java.util.TreeMap;

// A class that holds the data for a year
public class Year implements Comparable<Year> {
	// The name of the year and a map that holds months and temps.
	private Map<Integer, Double> monthTemps;
	private Integer name;
	
	// Constructor
	public Year(Integer name) {
		this.name = name;
		this.monthTemps = new TreeMap<Integer, Double>();
		// By default create each month with a temp of 0.0
		for(Integer i = 1; i <= 12; i++) {
			monthTemps.put(i, 0.0);
		}
	}
	
	// Put month temp data into the map
	public void putData(Integer month, Double temperature) {
		monthTemps.put(month, temperature);
	}
	
	// Get the month temp map
	public Map getMap(){
		return(this.monthTemps);
	}
	
	// Get the name of the year
	public Integer getName() {
		return(this.name);
	}

	// Compare year values
	@Override
	public int compareTo(Year o) {
		if(this.name > o.name) {
			return(1);
		}
		else if(this.name < o.name){
			return(-1);
		}
		else {
			return(0);
		}		
	}
	
	// String representation of the year
	@Override
	public String toString() {
		return(name.toString());
	}
}
