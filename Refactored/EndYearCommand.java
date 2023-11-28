package dataviewerfinal;

// Command to change the end year
public class EndYearCommand extends Command {
	// Selected value to change to
	Year selectedValue;

	// Constructor
	public EndYearCommand(DataViewerApp theReciever, Year selectedValue) {
		super(theReciever);
		this.selectedValue = selectedValue;
	}

	// Tells the receiver to change the end year to the selected value
	@Override
	public void execute() {
		theReciever.selectedEndYearChange(selectedValue);
	}
	

}
