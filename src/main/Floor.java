package main;

/**
 * This class contains the Floor object to be controlled by the FloorSubsystem
 * @author Campbell de Winter & Alex Tasseron
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
