package dataviewerfinal;

// Command that tells the receiver to change vis mode
public class VisualizationCommand extends Command {
	// Selected value that vis mode is being changed to
	String selectedValue;

	// Constructor
	public VisualizationCommand(DataViewerApp theReciever, String selectedValue) {
		super(theReciever);
		this.selectedValue = selectedValue;
	}

	// Tell the receiver to change its vis mode to selected value
	@Override
	public void execute() {
		theReciever.selectedVisualizationChange(selectedValue);
	}
	
	

}
