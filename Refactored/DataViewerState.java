package dataviewerfinal;

import java.awt.Color;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import edu.du.dudraw.Draw;

// The GUI state that shows the plot
public class DataViewerState extends StatePattern {
	//draw data
	private final static double		TEMPERATURE_MAX_C = 30.0;
	private final static double		TEMPERATURE_MIN_C = -10.0;
	private final static double		TEMPERATURE_RANGE = TEMPERATURE_MAX_C - TEMPERATURE_MIN_C;
	private final static String[] 	VISUALIZATION_MODES = { "Raw", "Extrema (within 10% of min/max)" };

	//draw main menu

	//tests to debug, error, info, trace
	private Tests test;

	// Window settings
	private final static int 		WINDOW_HEIGHT = 720;
	private final static String 	WINDOW_TITLE = "DataViewer Application";
	private final static int 		WINDOW_WIDTH = 1320; // should be a multiple of 12

	private final static String[] 	MONTH_NAMES = { "", // 1-based
			"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	private final static double 	EXTREMA_PCT = 0.1;
	private final static double 	DATA_WINDOW_BORDER = 50.0;


	private final static int		VISUALIZATION_EXTREMA_IDX = 1;

	// Constructor
	public DataViewerState(GUI theGUI, Draw theWindow, DataViewerApp theViewer) {
		super(theGUI, theWindow, theViewer);
		this.test = new Tests();
		
		// TODO Auto-generated constructor stub
	}


	// Draw the plot
	public void draw() {
		// Give a buffer around the plot window
		theWindow.setXscale(-DATA_WINDOW_BORDER, WINDOW_WIDTH+DATA_WINDOW_BORDER);
		theWindow.setYscale(-DATA_WINDOW_BORDER, WINDOW_HEIGHT+DATA_WINDOW_BORDER);

		// gray background
		theWindow.clear(Color.LIGHT_GRAY);

		// white plot area
		theWindow.setPenColor(Color.WHITE);
		theWindow.filledRectangle(WINDOW_WIDTH/2.0, WINDOW_HEIGHT/2.0, WINDOW_WIDTH/2.0, WINDOW_HEIGHT/2.0);  

		theWindow.setPenColor(Color.BLACK);

		double nCols = 12; // one for each month
		double nRows = m_selectedEndYear.getName() - m_selectedStartYear.getName() + 1; // for the years

		test.debug("nCols = %f, nRows = %f", nCols, nRows);

		double cellWidth = WINDOW_WIDTH / nCols;
		double cellHeight = WINDOW_HEIGHT / nRows;

		test.debug("cellWidth = %f, cellHeight = %f", cellWidth, cellHeight);

		boolean extremaVisualization = m_selectedVisualization.equals(VISUALIZATION_MODES[VISUALIZATION_EXTREMA_IDX]);
		test.info("visualization: %s (extrema == %b)", m_selectedVisualization, extremaVisualization);

		for(int month = 1; month <= 12; month++) {
			double fullRange = m_plotMonthlyMaxValue.get(month) - m_plotMonthlyMinValue.get(month);
			double extremaMinBound = m_plotMonthlyMinValue.get(month) + EXTREMA_PCT * fullRange;
			double extremaMaxBound = m_plotMonthlyMaxValue.get(month) - EXTREMA_PCT * fullRange;


			// draw the line separating the months and the month label
			theWindow.setPenColor(Color.BLACK);
			double lineX = (month-1.0)*cellWidth;
			theWindow.line(lineX, 0.0, lineX, WINDOW_HEIGHT);
			theWindow.text(lineX+cellWidth/2.0, -DATA_WINDOW_BORDER/2.0, MONTH_NAMES[month]);

			// there should always be a map for the month
			SortedMap<Integer,Double> monthData = m_plotData.get(month);

			for(int year = m_selectedStartYear.getName(); year <= m_selectedEndYear.getName(); year++) {

				// month data structure might not have every year
				if(monthData.containsKey(year)) {
					Double value = monthData.get(year);

					double x = (month-1.0)*cellWidth + 0.5 * cellWidth;
					double y = (year-m_selectedStartYear.getName())*cellHeight + 0.5 * cellHeight;

					Color cellColor = null;

					// get either color or grayscale depending on visualization mode
					if(extremaVisualization && value > extremaMinBound && value < extremaMaxBound) {
						cellColor = getDataColor(value, true);
					}
					else if(extremaVisualization) {
						// doing extrema visualization, show "high" values in red "low" values in blue.
						if(value >= extremaMaxBound) {
							cellColor = Color.RED;
						}
						else {
							cellColor = Color.BLUE;
						}
					}
					else {
						cellColor = getDataColor(value, false);
					}

					// draw the rectangle for this data point
					theWindow.setPenColor(cellColor);
					test.trace("month = %d, year = %d -> (%f, %f) with %s", month, year, x, y, cellColor.toString());
					theWindow.filledRectangle(x, y, cellWidth/2.0, cellHeight/2.0);
				}
			}
		}

		// draw the labels for the y-axis
		theWindow.setPenColor(Color.BLACK);

		double labelYearSpacing = (m_selectedEndYear.getName() - m_selectedStartYear.getName()) / 5.0;
		double labelYSpacing = WINDOW_HEIGHT/5.0;
		// spaced out by 5, but need both the first and last label, so iterate 6
		for(int i=0; i<6; i++) {
			int year = (int)Math.round(i * labelYearSpacing + m_selectedStartYear.getName());
			String text = String.format("%4d", year);

			theWindow.textRight(0.0, i*labelYSpacing, text);
			theWindow.textLeft(WINDOW_WIDTH, i*labelYSpacing, text);
		}

		// draw rectangle around the whole data plot window
		theWindow.rectangle(WINDOW_WIDTH/2.0, WINDOW_HEIGHT/2.0, WINDOW_WIDTH/2.0, WINDOW_HEIGHT/2.0);

		// put in the title
		String title = String.format("%s, %s from %d to %d. Press 'M' for Main Menu.  Press 'Q' to Quit.",
				m_selectedState.getName(), m_selectedCountry.getName(), m_selectedStartYear.getName(), m_selectedEndYear.getName());
		theWindow.text(WINDOW_WIDTH/2.0, WINDOW_HEIGHT+DATA_WINDOW_BORDER/2.0, title);
	}




		// Get the data colors
		public Color getDataColor(Double value, boolean doGrayscale) {
			if(null == value) {
				return null;
			}
			double pct = (value - TEMPERATURE_MIN_C) / TEMPERATURE_RANGE;
			test.trace("converted %f raw value to %f %%", value, pct);

			if (pct > 1.0) {
				pct = 1.0;
			}
			else if (pct < 0.0) {
				pct = 0.0;
			}
			int r, g, b;
			// Replace the color scheme with my own
			if (!doGrayscale) {
				r = (int)(255.0 * pct);
				g = 0;
				b = (int)(255.0 * (1.0-pct));

			} else {
				// Grayscale for the middle extema
				r = g = b = (int)(255.0 * pct);
			}

			test.trace("converting %f to [%d, %d, %d]", value, r, g, b);

			return new Color(r, g, b);
		}


		// When I'm passed in a key, decide what to do with it
		@Override
		public void acccessKey(char k) {
			// Create my list of commands
			ArrayList<Command> commandsList = new ArrayList<Command>();

			// If my key is M change the state
			if(k == 'M') {
				theGUI.changeState(new MainMenuState(theGUI, theWindow, theViewer));
				theGUI.update();
			}

			// Execute the commands I've added.
			for(Command command : commandsList) {
				command.execute();
			}
		}



	}
