package dataviewerfinal;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

// Class for getting data from the file
public class GetData {
	private DataViewerApp 			theDataViewer;
	private Tests					theTests;
	final String 					M_DATAFILE;
	private final static int 		FILE_COUNTRY_IDX = 4;
	private final static int 		FILE_DATE_IDX = 0;
	private final static int 		FILE_NUM_COLUMNS = 5;
	private final static int 		FILE_STATE_IDX = 3;
	private final static int 		FILE_TEMPERATURE_IDX = 1;
	

	private ArrayList<Country> 				m_dataRaw;
	private TreeSet<Country> 				m_dataCountries;

	// Constructor 
	public GetData(DataViewerApp tdv, String dataFile) {
		this.theDataViewer = tdv;
		this.M_DATAFILE = dataFile;
		this.theTests = new Tests();
		this.m_dataRaw = new ArrayList<Country>();
		m_dataCountries = new TreeSet<Country>();
	}

	/**
	 * Utility function to pull a year integer out of a date string.  Supports M/D/Y and Y-M-D formats only.
	 * 
	 * @param dateString
	 * @return
	 */
	public Integer parseYear(String dateString) {
		Integer ret = null;
		if(dateString.indexOf("/") != -1) {
			// Assuming something like 1/20/1823
			String[] parts = dateString.split("/");
			if(parts.length == 3) {
				ret = Integer.parseInt(parts[2]);
			}
		}
		else if(dateString.indexOf("-") != -1) {
			// Assuming something like 1823-01-20
			String[] parts = dateString.split("-");
			if(parts.length == 3) {
				ret = Integer.parseInt(parts[0]);
			}
		}
		else {
			throw new RuntimeException(String.format("Unexpected date delimiter: '%s'", dateString));
		}
		if(ret == null) {
			theTests.trace("Unable to parse year from date: '%s'", dateString);
		}
		return ret;
	}

	public Integer parseMonth(String dateString) {
		Integer ret = null;
		if(dateString.indexOf("/") != -1) {
			// Assuming something like 1/20/1823
			String[] parts = dateString.split("/");
			if(parts.length == 3) {
				ret = Integer.parseInt(parts[0]);
			}
		}
		else if(dateString.indexOf("-") != -1) {
			// Assuming something like 1823-01-20
			String[] parts = dateString.split("-");
			if(parts.length == 3) {
				ret = Integer.parseInt(parts[1]);
			}
		}
		else {
			throw new RuntimeException(String.format("Unexpected date delimiter: '%s'", dateString));
		}
		if(ret == null || ret.intValue() < 1 || ret.intValue() > 12) {
			theTests.trace("Unable to parse month from date: '%s'", dateString);
			return null;
		}
		return ret;
	}

	// Gets raw data from the file
	public ArrayList<Country> getRawData() throws FileNotFoundException {
		// Gets my list of countries to look for
		try (Scanner scanner = new Scanner(new File(M_DATAFILE))) {
			while(scanner.hasNextLine()) {
				getCountryList(scanner.nextLine());	
			}
		}


		// Iterate through each country and add its info
		for(Country country: m_dataCountries) {
			if(country.getName().compareTo("Country") == 0) {
				theTests.info("Skipping over country object", null);
			}
			else {
				try (Scanner scanner = new Scanner(new File(M_DATAFILE))) {
					while (scanner.hasNextLine()) {
						getRecordFromLine(scanner.nextLine(), country);
					}
					theTests.info("Added " + country + " data", null);
				}
			}
		}

		theTests.info("All data loaded", null);
		
		return(new ArrayList<Country>(m_dataCountries));
	}

	// Gets the list of all the countries
	private void getCountryList(String line) {
		// Gets raw data
		List<String> rawValues = new ArrayList<String>();
		try (Scanner rowScanner = new Scanner(line)) {
			rowScanner.useDelimiter(",");
			while (rowScanner.hasNext()) {
				rawValues.add(rowScanner.next());
			}
		}
		
		// Create a country object with the country name and add it to my list
		Country currentCountry = new Country(rawValues.get(FILE_COUNTRY_IDX));
		m_dataCountries.add(currentCountry);		
	}

	// Gets record from line
	private Country getRecordFromLine(String line, Country m_selectedCountry) {
		// Gets raw data
		List<String> rawValues = new ArrayList<String>();
		try (Scanner rowScanner = new Scanner(line)) {
			rowScanner.useDelimiter(",");
			while (rowScanner.hasNext()) {
				rawValues.add(rowScanner.next());
			}
		}
		
		// Creates the current country variable
		Country currentCountry = new Country(rawValues.get(FILE_COUNTRY_IDX));

		// Sets the current country to the matching object in m_dataCountries
		for (Country country : m_dataCountries) {
			if (country.getName().compareTo(rawValues.get(FILE_COUNTRY_IDX)) == 0){
				currentCountry = country;
			}
		}


		if(rawValues.size() != FILE_NUM_COLUMNS) {
			theTests.trace("malformed line '%s'...skipping", line);
			return null;
		}
		else if(!rawValues.get(FILE_COUNTRY_IDX).equals(m_selectedCountry.getName())) {
			theTests.trace("skipping non-USA record: %s", rawValues);
			return null;
		}
		else {
			theTests.trace("processing raw data: %s", rawValues.toString());
		}
		
		try {
			// Parse these into more useful objects than String
			List<Object> values = new ArrayList<Object>(4);

			Integer year = parseYear(rawValues.get(FILE_DATE_IDX));
			if(year == null) {
				return null;
			}
			values.add(year);

			Integer month = parseMonth(rawValues.get(FILE_DATE_IDX));
			if(month == null) {
				return null;
			}
			values.add(month);
			values.add(Double.parseDouble(rawValues.get(FILE_TEMPERATURE_IDX)));
			//not going to use UNCERTAINTY yet
			//values.add(Double.parseDouble(rawValues.get(FILE_UNCERTAINTY_IDX)));
			values.add(rawValues.get(FILE_STATE_IDX));
			// since all are the same country
			//values.add(rawValues.get(FILE_COUNTRY_IDX));

			// if we got here, add the state to the list of states
			currentCountry.addState(rawValues.get(FILE_STATE_IDX));
			currentCountry.getStates().last().addYear(new Year(year));
			currentCountry.getStates().last().getYears().last().putData(month, Double.parseDouble(rawValues.get(FILE_TEMPERATURE_IDX)));
			return m_selectedCountry;
		}
		catch(NumberFormatException e) {
			theTests.trace("unable to parse data line, skipping...'%s'", line);
			return null;
		}
	}


}
