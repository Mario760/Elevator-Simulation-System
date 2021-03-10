package FloorSubsystem;

/**
 * This class contains the FloorButton object to be used by the Floor
 * @author Campbell de Winter
 *
 */
public class FloorButton {
	private FloorDirection name; // the button name
	private boolean pressed; // whether or not the button has been pressed
	
	/**
	 * The FloorButton constructor
	 * It initializes pressed to false and sets the name to the passed parameter
	 * @param direction the name of the button (up button or down button)
	 */
	public FloorButton(FloorDirection direction) {
		this.name = direction;
		this.pressed = false;
	}
	
	/**
	 * This method is used to signify that the button has been pressed
	 * It sets the pressed variable to true
	 */
	public void pressButton() {
		pressed = true;
	}
	
	/**
	 * This method is used to set the pressed variable to false
	 */
	public void clearButton() {
		pressed = false;
	}
	
	/**
	 * This method returns the value of the pressed variable
	 * @return true if pressed false if not
	 */
	public boolean isPressed() {
		return pressed;
	}
	
	/**
	 * This method returns the floor name
	 * @return UP or DOWN depending on what button this is
	 */
	public FloorDirection getName() {
		return name;
	}
}