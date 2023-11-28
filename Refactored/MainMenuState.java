package dataviewerfinal;

import java.awt.Color;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import edu.du.dudraw.Draw;

// Main menu state for GUI
public class MainMenuState extends StatePattern{
	private final static String[] 	VISUALIZATION_MODES = { "Raw", "Extrema (within 10% of min/max)" };

	private final static double		MENU_STARTING_X = 40.0;
	private final static double 	MENU_STARTING_Y = 90.0;
	private final static double 	MENU_ITEM_SPACING = 5.0;

	
	// user selections
	//draw main menu

	//tests to debug, error, info, trace
	private Tests test;

	// Constructor
	public MainMenuState(GUI theGUI, Draw window, DataViewerApp theViewer) {
		super(theGUI, window, theViewer);
			this.test = new Tests();
			
			}

	// Draw the main menu
	public void draw() {
		theWindow.clear(Color.WHITE);

		String[] menuItems = {
				"Type the menu number to select that option:",
				"",
				String.format("C     Set country: [%s]", m_selectedCountry.getName()),
				String.format("T     Set state: [%s]", m_selectedState.getName()),
				String.format("S     Set start year [%d]", m_selectedStartYear.getName()),
				String.format("E     Set end year [%d]", m_selectedEndYear.getName()),
				String.format("V     Set visualization [%s]", m_selectedVisualization),
				String.format("P     Plot data"),
				String.format("Q     Quit"),
		};

		// enable drawing by "percentage" with the menu drawing
		theWindow.setXscale(0, 100);
		theWindow.setYscale(0, 100);

		// draw the menu
		theWindow.setPenColor(Color.BLACK);

		drawMenuItems(menuItems);
	}

	// Draw menu items
	public void drawMenuItems(String[] menuItems) {
		double yCoord = MENU_STARTING_Y;

		for(int i=0; i<menuItems.length; i++) {
			theWindow.textLeft(MENU_STARTING_X, yCoord, menuItems[i]);
			yCoord -= MENU_ITEM_SPACING;
		}
	}

	// Access key method to determine what to do with key commands
	@Override
	public void acccessKey(char key) {
		// Make a list of commands
		ArrayList<Command> commandsList = new ArrayList<Command>();

		if(key == 'P') {
			// Change state to data viewer and add update plot command
			theGUI.changeState(new DataViewerState(theGUI, theWindow, theViewer));
			commandsList.add(new UpdatePlotCommand(theViewer));
		}
		else if(key == 'C') {
			// Add command to set the Country
			Object selectedValue = JOptionPane.showInputDialog(null,
					"Choose a Country", "Input",
					JOptionPane.INFORMATION_MESSAGE, null,
					theViewer.getCountryArray(), m_selectedCountry);
			
			commandsList.add(new CountryCommand(theViewer, (Country) selectedValue));
		}

		else if(key == 'T') {
			// Add command to set the state
			Object selectedValue = JOptionPane.showInputDialog(null,
					"Choose a State", "Input",
					JOptionPane.INFORMATION_MESSAGE, null,
					m_selectedCountry.getStates().toArray(), m_selectedState);
			
			commandsList.add(new StateCommand(theViewer, (State) selectedValue));
		}
		else if(key == 'S') {
			// Add command set the start year
			Object selectedValue = JOptionPane.showInputDialog(null,
					"Choose the start year", "Input",
					JOptionPane.INFORMATION_MESSAGE, null,
					m_selectedState.getYears().toArray(), m_selectedStartYear);

			commandsList.add(new StartYearCommand(theViewer, (Year) selectedValue));
		}
		else if(key == 'E') {
			// Add command to set the end year
			Object selectedValue = JOptionPane.showInputDialog(null,
					"Choose the end year", "Input",
					JOptionPane.INFORMATION_MESSAGE, null,
					m_selectedState.getYears().toArray(), m_selectedEndYear);

			commandsList.add(new EndYearCommand(theViewer, (Year) selectedValue));
		}
		else if(key == 'V') {
			// Add command to set the visualization
			Object selectedValue = JOptionPane.showInputDialog(null,
					"Choose the visualization mode", "Input",
					JOptionPane.INFORMATION_MESSAGE, null,
					VISUALIZATION_MODES, m_selectedVisualization);

			commandsList.add(new VisualizationCommand(theViewer, (String) selectedValue));
		}
		
		// Execute commands
		for (Command command : commandsList) {
			command.execute();
		}

	}
	
	
	}
