
public class Floor {
	private int floorNumber;
	private FloorLamp lamp;
	private FloorButton[] buttons;
	
	public Floor(int floorNumber, int buttonNumber) { // a top floor variable is needed to know if the sub system is on the top floor
		this.floorNumber = floorNumber;
		this.lamp = new FloorLamp();
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
	
	
	
}

class FloorLamp {
	private FloorDirection direction;
	private int currentFloor;
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
	
	public void setCurrentFloor(int currentFloor) {
		if (!state) {
			System.out.println("The lamp is off");
			return;
		}
		this.currentFloor = currentFloor;
	}
	public int getCurrentFloor() {
		if (!state) {
			System.out.println("The lamp is off");
			return -1;
		}
		return currentFloor;
	}
	
	public void setFloorDirection(FloorDirection direction) {
		if (!state) {
			System.out.println("The lamp is off");
			return;
		}
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
