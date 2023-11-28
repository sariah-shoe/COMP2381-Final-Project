package dataviewerfinal;

// Command to change country
public class CountryCommand extends Command {
	Country selectedValue;

	// Does the super constructor along with the selected value for the command
	public CountryCommand(DataViewerApp theReciever, Country selectedValue) {
		super(theReciever);
		this.selectedValue = selectedValue;
	}

	// To execute, tell the reciever to change the country to selectedValue
	@Override
	public void execute() {
		theReciever.selectedCountryChange(selectedValue);
	}

}
