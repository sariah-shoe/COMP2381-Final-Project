package dataviewerfinal;

// Class that tells the reciever to update plot data
public class UpdatePlotCommand extends Command {

	// Constructor
	public UpdatePlotCommand(DataViewerApp theReciever) {
		super(theReciever);
	}
	
	// Execute by having the reciever update plot data
	@Override
	public void execute() {
		theReciever.updatePlotData();
	}

}
