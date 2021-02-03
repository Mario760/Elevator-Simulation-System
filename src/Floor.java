import java.util.ArrayList;

public class Floor {
	private int floorNumber;
	private FloorLamp lamp;
	private FloorButton[] buttons;
	private boolean door;
	
	public Floor(int floorNumber, int buttonNumber) {
		this.floorNumber = floorNumber;
		this.lamp = new FloorLamp();
		this.door = false;
		if (buttonNumber == 1) {
			if (floorNumber == 1) {
				this.buttons = new FloorButton[] {new FloorButton(FloorDirection.UP)};
			} else { // assuming top floor
				this.buttons = new FloorButton[] {new FloorButton(FloorDirection.DOWN)};
			}
		} else {
			this.buttons = new FloorButton[] {new FloorButton(FloorDirection.UP), new FloorButton(FloorDirection.DOWN)};
		}
	}
	
	public void pressButton(FloorDirection direction) {
		buttons[direction.ordinal()].pressButton();
		lamp.setFloorDirection(direction);
	}
	
	public void handleArrival() {
		lamp.setState(true);
		door = true;
		for (FloorButton button : buttons) {
			button.clearButton();
		}
	}
	
	public void handleDeparture() {
		lamp.setState(false);
		door = false;
	}
	
	public Object[] getState(){
		return new Object[] {floorNumber, door, lamp, buttons}; 
	}
	
	
	
}

class FloorLamp {
	private FloorDirection direction;
	private boolean state;
	
	public FloorLamp() {
		this.state = false;
	}
	
	public void setState(boolean state) {
		this.state = state;
	}
	
	public boolean getState() {
		return state;
	}
	
	public void setFloorDirection(FloorDirection direction) {
		this.direction = direction;
	}
	
	public FloorDirection getDirection() {
		if (!state) {
			System.out.println("The lamp is off");
			return FloorDirection.DOWN;
		}
		return direction;
	}
	
	
}

class FloorButton {
	private FloorDirection name;
	private boolean pressed;
	
	public FloorButton(FloorDirection direction) {
		this.name = direction;
		this.pressed = false;
	}
	
	public void pressButton() {
		pressed = true;
	}
	public void clearButton() {
		pressed = false;
	}
	
	public boolean ispressed() {
		return pressed;
	}
	
	public FloorDirection getName() {
		return name;
	}
}
