package dataviewerfinal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import edu.du.dudraw.Draw;

public class DataViewerApp {
	// Private constants (alphabetical)
	private final static String[] 	VISUALIZATION_MODES = { "Raw", "Extrema (within 10% of min/max)" };

	// Instance variables (alphabetized)

	// data storage
	private final String m_dataFile;
	private List<Country> m_dataRaw;

	// user selections
	private Country m_selectedCountry;
	private Year m_selectedEndYear;
	private State m_selectedState;
	private Year m_selectedStartYear;
	private String m_selectedVisualization = VISUALIZATION_MODES[0];

	// plot-related data
	private TreeMap<Integer, SortedMap<Integer,Double>> m_plotData = null;
	private TreeMap<Integer,Double> m_plotMonthlyMaxValue = null;
	private TreeMap<Integer,Double> m_plotMonthlyMinValue = null;

	// Variable for get data
	GetData dataHandler;
	GUI plot;
	Tests test;


	/**
	 * Constructor sets up the window and loads the specified data file.
	 */
	public DataViewerApp(String dataFile) throws FileNotFoundException {
		// save the data file name for later
		m_dataFile = dataFile;

		// Add my get data handler
		dataHandler = new GetData(this, this.m_dataFile);

		// Load data
		m_dataRaw = null;
		loadData();
		
		// Set my defaults
		m_selectedCountry = (Country) getCountryArray()[0];
		m_selectedState = (State) m_selectedCountry.getStates().first();
		m_selectedStartYear = m_selectedState.getYears().first();
		m_selectedEndYear = m_selectedState.getYears().last();
		
		// Add a GUI
		plot = new GUI(this, this.m_dataFile);
		
		// Add a testing class
		test = new Tests();

		// draw the screen for the first time -- this will be the main menu
		updateGUI();
	}

	// Method that loads data 
	public void loadData() throws FileNotFoundException {
		// Tells the data handler to get the raw data
		try (Scanner scanner = new Scanner(new File(m_dataFile))) {
			m_dataRaw = dataHandler.getRawData();
		}
	}

	// Method that updates plot data
	public void updatePlotData() {
		//debug("raw data: %s", m_rawData.toString());
		// plot data is a map where the key is the Month, and the value is a sorted map where the key
		// is the year. 
		m_plotData = new TreeMap<Integer,SortedMap<Integer,Double>>();
		for(int month = 1; month <= 12; month++) {
			// any year/months not filled in will be null
			m_plotData.put(month, new TreeMap<Integer,Double>());
		}
		// now run through the raw data and if it is related to the current state and within the current
		// years, put it in a sorted data structure, so that we 
		// find min/max year based on data 
		m_plotMonthlyMaxValue = new TreeMap<Integer,Double>();
		m_plotMonthlyMinValue = new TreeMap<Integer,Double>();
		// Iterates through each state in the country, each year in the state, and each month in the year
		for(State state : m_selectedCountry.getStates()) {
			for(Year year : state.getYears()) {
				Set<Integer> months = year.getMap().keySet();
				for(Integer month : months) {
					// Check to see if they are the state and year range we care about
					if (state.equals(m_selectedState) && 
							((year.compareTo(m_selectedStartYear) >= 0 && year.compareTo(m_selectedEndYear) <= 0))) {

						// Ok, we need to add this to the list of values for the month
						Double value = (Double) year.getMap().get(month);

						if(!m_plotMonthlyMinValue.containsKey(month) || value.compareTo(m_plotMonthlyMinValue.get(month)) < 0) {
							m_plotMonthlyMinValue.put((Integer)month, value);
						}
						if(!m_plotMonthlyMaxValue.containsKey(month) || value.compareTo(m_plotMonthlyMaxValue.get(month)) > 0) {
							m_plotMonthlyMaxValue.put((Integer)month, value);
						}

						m_plotData.get(month).put(year.getName(), value);
					}
				}


			}
		}
		
		// Set the GUIs variables to match the ones I just calculated
		plot.setMonthlyMax(m_plotMonthlyMaxValue);
		plot.setMonthlyMin(m_plotMonthlyMinValue);
		plot.setPlotData(m_plotData);
		
		updateGUI();
	}

	// Method to update the GUI
	private void updateGUI() {
		// Make all the GUI's variables match these variables
		plot.setCountry(m_selectedCountry);
		plot.setState(m_selectedState);
		plot.setStartYear(m_selectedStartYear);
		plot.setEndYear(m_selectedEndYear);
		plot.setVisualization(m_selectedVisualization);
	
		// Tell the GUI to update
		plot.update();
	}
	
	// Gets an array of the countries
	public Object[] getCountryArray() {
		return(m_dataRaw.toArray());
	}
	
	// Changes selected state
	public void selectedStateChange(State newState) {
		// Check to see if the state is null
		if(newState != null) {
			test.info("User selected: '%s'", newState);
			
			// Check to see if the state is already selected
			if(!newState.equals(m_selectedState)) {
				// Passed both tests so change in data
				m_selectedState = newState;
				m_selectedStartYear = m_selectedState.getYears().first();
				m_selectedEndYear = m_selectedState.getYears().last();
			}
		}	
		
		// Tell the GUI to update
		updateGUI();
	}
	
	// Change the start year
	public void selectedStartYearChange(Year startYear) {
		// Check to see if the year is null
		if(startYear != null) {
			test.info("User seleted: '%s'", startYear);
			// Check to see the year is within bounds
			if(startYear.compareTo(m_selectedEndYear) > 0) {
				test.error("new start year (%d) must not be after end year (%d)", startYear, m_selectedEndYear);
			}
			else {
				// Check to see if the year isn't the one currently selected
				if(!m_selectedStartYear.equals(startYear)) {
					// Change the start year
					m_selectedStartYear = startYear;
				}
			}
		}
		
		// Tell the GUI to update
		updateGUI();
	}
	
	// Change the selected country
	public void selectedCountryChange(Country newCountry) {
		// Check to see if the country is null
		if(newCountry != null) {
			test.info("User selected: '%s'", newCountry);
			// Check to see if the country is the current one selected
			if(!newCountry.equals(m_selectedCountry)) {
				// Passed both tests so change in data
				m_selectedCountry = (Country) newCountry;
				m_selectedState = m_selectedCountry.getStates().first();
				m_selectedStartYear = m_selectedState.getYears().first();
				m_selectedEndYear = m_selectedState.getYears().last();
			}
		}
		
		// Update the GUI
		updateGUI();
	}
	
	// Change the current year
	public void selectedEndYearChange (Year endYear) {
		// Check to see if the year is null
		if(endYear != null) {
			test.info("User seleted: '%s'", endYear);
			// Check to see if the year is in an acceptable range
			if(endYear.compareTo(m_selectedStartYear) < 0) {
				test.error("new end year (%d) must be not be before start year (%d)", endYear, m_selectedStartYear);
			}
			else {
				// Check to see if already selected
				if(!m_selectedEndYear.equals(endYear)) {
					// Passed all tests so change end year
					m_selectedEndYear = endYear;
				}
			}
		}
		
		// Update the GUI
		updateGUI();
	}
	
	// Change the visualization mode
	public void selectedVisualizationChange (String newVis) {
		// Check to see if the visualization mode is null
		if(newVis != null) {
			test.info("User seleted: '%s'", newVis);
			// Check to see if its the current visualization mode
			if(!m_selectedVisualization.equals(newVis)) {
				// Change the selected visualization
				m_selectedVisualization = newVis;
			}
		}
		
		updateGUI();
	}
	
}