package dataviewerfinal;

import java.util.SortedMap;
import java.util.TreeMap;

import edu.du.dudraw.Draw;

// Abstract for state patterns
abstract class StatePattern {
	// Bunch of variables that keep track of current data
	protected GUI theGUI;
	protected Draw theWindow;
	protected DataViewerApp theViewer;
	protected Country m_selectedCountry;
	protected Year m_selectedStartYear;
	protected State m_selectedState;
	protected Year m_selectedEndYear;
	protected String m_selectedVisualization;
	protected TreeMap<Integer, Double> m_plotMonthlyMaxValue;
	protected TreeMap<Integer, Double> m_plotMonthlyMinValue;
	protected TreeMap<Integer, SortedMap<Integer, Double>> m_plotData;

	// Constructor that connects to the GUI, the window, and the data viewer app
	public StatePattern(GUI theGUI, Draw theWindow, DataViewerApp theViewer){
		this.theGUI = theGUI;
		this.theWindow = theWindow;
		this.theViewer = theViewer;
	}

	// Abstract for draw
	public abstract void draw();
	
	// Abstract for what state does with keys
	public abstract void acccessKey(char k);

	
	// Setters
	public void setCountry(Country country) {
		m_selectedCountry = country;
	}
	
	public void setStartYear(Year year) {
		m_selectedStartYear = year;
	}
	
	public void setEndYear(Year year) {
		m_selectedEndYear = year;	
	}
	
	public void setState(State state) {
		m_selectedState = state;
	}
	
	public void setVisualization(String v) {
		m_selectedVisualization = v;
	}
	
	public void setMonthlyMax(TreeMap<Integer,Double> monthlyMax) {
		m_plotMonthlyMaxValue = monthlyMax;
	}
	
	public void setMonthlyMin(TreeMap<Integer,Double> monthlyMin) {
		m_plotMonthlyMinValue = monthlyMin;
	}
	
	public void setPlotData(TreeMap<Integer, SortedMap<Integer,Double>> plotData) {
		m_plotData = plotData;
	}
}
