package dataviewerfinal;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JOptionPane;

import edu.du.dudraw.Draw;
import edu.du.dudraw.DrawListener;

public class GUI implements DrawListener{		
	//draw data
	private final static double		TEMPERATURE_MAX_C = 30.0;
	private final static double		TEMPERATURE_MIN_C = -10.0;
	private final static double		TEMPERATURE_RANGE = TEMPERATURE_MAX_C - TEMPERATURE_MIN_C;
	private final static String[] 	VISUALIZATION_MODES = { "Raw", "Extrema (within 10% of min/max)" };

	// GUI mode
	private StatePattern m_guiMode; // Menu by default

	// user selections
	private Country m_selectedCountry;
	private Year m_selectedEndYear;
	private State m_selectedState;
	private Year m_selectedStartYear;
	private String m_selectedVisualization = VISUALIZATION_MODES[0];

	// plot-related data
	private TreeMap<Integer, SortedMap<Integer,Double>> m_plotData = null;

	// Window-variables
	private Draw m_window;

	//tests to debug, error, info, trace
	private Tests test;

	//update plot information
	private DataViewerApp newPlot;
	
	// Window settings
	private final static int 		WINDOW_HEIGHT = 720;
	private final static String 	WINDOW_TITLE = "DataViewer Application";
	private final static int 		WINDOW_WIDTH = 1320; // should be a multiple of 12
	
	private final static String[] 	MONTH_NAMES = { "", // 1-based
			"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	private final static double 	EXTREMA_PCT = 0.1;
	private final static double 	DATA_WINDOW_BORDER = 50.0;


	private final static int		VISUALIZATION_EXTREMA_IDX = 1;
	
	// plot-related data
	private TreeMap<Integer,Double> m_plotMonthlyMaxValue;
	private TreeMap<Integer,Double> m_plotMonthlyMinValue;
	
	// Constructor for GUI
	public GUI(DataViewerApp app, String m_dataFile ) {
		// Connect to tests and to a dataviewerapp
		this.test = new Tests();
		this.newPlot = app;
		
		// Do the window stuff
		m_window = new Draw(WINDOW_TITLE);
		m_window.setCanvasSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		m_window.enableDoubleBuffering();
		m_window.addListener(this);
		
		// Set the state to main menu
		m_guiMode = new MainMenuState(this, m_window, this.newPlot);
		
		}


	// Method for updates
	@Override
	public void update() {
		// Set all my state variables to match my GUI variables
		m_guiMode.setCountry(m_selectedCountry);
		m_guiMode.setState(m_selectedState);
		m_guiMode.setEndYear(m_selectedEndYear);
		m_guiMode.setStartYear(m_selectedStartYear);
		m_guiMode.setVisualization(m_selectedVisualization);
		m_guiMode.setMonthlyMax(m_plotMonthlyMaxValue);
		m_guiMode.setMonthlyMin(m_plotMonthlyMinValue);
		m_guiMode.setPlotData(m_plotData);
		
		test.debug("Drawing current state", m_guiMode);
		
		// Draw whatever state my gui is in
		m_guiMode.draw();
		
		// for double-buffering
		m_window.show();
	}

	// Changes the state of the gui
	public void changeState(StatePattern c) {
		m_guiMode = c;
	}

	// Below are the mouse/key listeners
	/**
	 * Handle key press.  Q always quits.  Otherwise process based on GUI mode.
	 */
	@Override public void keyPressed(int key) {		
		test.trace("key pressed '%c'", (char)key);
		// regardless of draw mode, 'Q' or 'q' means quit:
		if(key == 'Q') {
			System.out.println("Bye");
			System.exit(0);
		}
		
		// Pass on the key to my gui mode for handling
		m_guiMode.acccessKey((char)key);
	}

	// Other listener commands I don't do anything with
	@Override
	public void keyReleased(int key) {}

	@Override
	public void keyTyped(char key) {}

	@Override
	public void mouseClicked(double x, double y) {}

	@Override
	public void mouseDragged(double x, double y) {}

	@Override
	public void mousePressed(double x, double y) {}

	@Override
	public void mouseReleased(double x, double y) {}


	// Setters for all the GUI data
	
	public void setCountry(Country country) {
		m_selectedCountry = country;
	}


	public void setState(State state) {
		m_selectedState = state;
	}


	public void setStartYear(Year year) {
		m_selectedStartYear = year;
	}


	public void setEndYear(Year year) {
		m_selectedEndYear = year;
	}


	public void setVisualization(String vis) {
		m_selectedVisualization = vis;
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

