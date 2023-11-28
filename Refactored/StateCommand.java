package dataviewerfinal;

// Command to change selected state
public class StateCommand extends Command{
	// Selectedvalue to change state to
	State selectedValue;

	// Constructor
	public StateCommand(DataViewerApp theReciever, State selectedValue) {
		super(theReciever);
		this.selectedValue = selectedValue;
	}

	// Execute by telling the receiver to change the state to selected value
	@Override
	public void execute() {
		theReciever.selectedStateChange(selectedValue);
	}

}
