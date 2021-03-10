package FloorSubsystem;

/**
 * This class is contains the FloorLamp object to be controlled by the Floor
 * @author Campbell de Winter
 *
 */
public class FloorLamp {
	private FloorDirection direction; // the direction the lamp is displaying
	private boolean state; // whether or not the lamp is on
	
	/**
	 * The FloorLamp constructor
	 * Sets the state to false to represent that the lamp is initialized to off
	 */
	public FloorLamp() {
		this.state = false;
	}
	
	/**
	 * This method sets the state of the FloorLamp
	 * @param state a boolean for on or off
	 */
	public void setState(boolean state) {
		this.state = state;
	}
	
	/**
	 * This method returns the current state of the FloorLamp
	 * @return the boolean value of state
	 */
	public boolean getState() {
		return state;
	}
	
	/**
	 * This method changes the direction the lamp is displaying
	 * @param direction either UP or DOWN
	 */
	public void setFloorDirection(FloorDirection direction) {
		this.direction = direction;
	}
	
	/**
	 * This method returns the current direction being displayed by the lamp
	 * @return the direction
	 */
	public FloorDirection getDirection() {
		if (!state) { // if the lamp is off the direction is not needed
			System.out.println("The lamp is off");
		}
		return direction;
	}
	
	
}