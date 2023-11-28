package dataviewerfinal;

// Abstract for the command pattern
public abstract class Command {
	// Each command needs the a receiver
	protected DataViewerApp theReciever;
	
	// Super constructor
	public Command(DataViewerApp theReciever) {
		this.theReciever = theReciever;
	}
	
	// Abstract class for execution
	public abstract void execute();
	
}
