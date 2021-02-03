
/**
 * This class contains the Floor object to be controlled by the FloorSubsystem
 * @author Campbell de Winter
 *
 */
public class Floor {
	private int floorNumber; // the floor number
	private FloorLamp lamp; // The floor lamp
	private FloorButton[] buttons; // the floor buttons
	private boolean door; // Whether or not the door is open (false = closed)
	
	/**
	 * Floor constructor
	 * Creates the buttons and the lamp and sets the door to closed
	 * @param floorNumber the floor number
	 * @param buttonNumber the number of buttons (Bottom and Top floor have 1 button)
	 */
	public Floor(int floorNumber, int buttonNumber) {
		this.floorNumber = floorNumber;
		this.lamp = new FloorLamp();
		this.door = false; // Initializes the door to closed
		if (buttonNumber == 1) { // either the top or bottom floor
			if (floorNumber == 1) {
				this.buttons = new FloorButton[] {new FloorButton(FloorDirection.UP)}; // bottom floor
			} else { // assuming top floor
				this.buttons = new FloorButton[] {new FloorButton(FloorDirection.DOWN)}; // top floor
			}
		} else {
			this.buttons = new FloorButton[] {new FloorButton(FloorDirection.UP), new FloorButton(FloorDirection.DOWN)}; // left over floors
		}
	}
	
	/**
	 * This method is used to get the floor number of the floor
	 * @return the floor number
	 */
	public int getFloorNumber() {
		return floorNumber;
	}
	
	/**
	 * This method is used to check if the floor's elevator doors are open
	 * @return true if open false if not
	 */
	public boolean isDoorOpen() {
		return door;
	}
	
	/**
	 * This method returns the floor's lamp so the Subsystem can check the state of the lamp
	 * @return the floor's lamp
	 */
	public FloorLamp getLamp() {
		return lamp;
	}
	
	/**
	 * This method returns the floor's buttons so that the sub system can check the state of each button
	 * @return
	 */
	public FloorButton[] getButtons() {
		return buttons;
	}
	
	/**
	 * This method handles when a button has been pressed on the floor
	 * It tells the pressed button to invoke its pressButton() method
	 * It tells the floor lamp what direction has been selected by the user
	 * @param direction the button that has been pressed
	 */
	public void pressButton(FloorDirection direction) {
		buttons[direction.ordinal()].pressButton();
		lamp.setFloorDirection(direction);
	}
	
	/**
	 * This method handles when the elevator arrives at the floor
	 * It resets the state of each button, turns on the floor lamp and opens the door
	 */
	public void handleArrival() {
		lamp.setState(true);
		door = true;
		for (FloorButton button : buttons) {
			button.clearButton();
		}
	}
	
	/**
	 * This method handles when the elevator leaves the floor
	 * It turns off the floor lamp and closes the door
	 */
	public void handleDeparture() {
		lamp.setState(false);
		door = false;
	}
	

	
	
	
}

/**
 * This class is contains the FloorLamp object to be controlled by the Floor
 * @author Campbell de Winter
 *
 */
class FloorLamp {
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

/**
 * This class contains the FloorButton object to be used by the Floor
 * @author Campbell de Winter
 *
 */
class FloorButton {
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
