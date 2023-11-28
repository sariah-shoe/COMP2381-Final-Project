package dataviewerfinal;

// Command to change the start year
public class StartYearCommand extends Command {
	// Selected value to change to
	Year selectedValue;

	// Constructor
	public StartYearCommand(DataViewerApp theReciever, Year selectedValue) {
		super(theReciever);
		this.selectedValue = selectedValue;
	}

	// Tells the reciever to change start year to selectedValue
	@Override
	public void execute() {
		theReciever.selectedStartYearChange(selectedValue);
	}
	

}
