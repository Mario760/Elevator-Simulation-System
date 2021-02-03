
public class Floor {
	private int floorNumber;
	private FloorButton[] buttons;
	
	public Floor(int floorNumber, int buttonNumber) { // a top floor variable is needed to know if the sub system is on the top floor
		this.floorNumber = floorNumber;
		if (buttonNumber == 1) {
			if (floorNumber == 1) {
				this.buttons = new FloorButton[] {new FloorButton(Direction.UP)};
			} else { // assuming top floor
				this.buttons = new FloorButton[] {new FloorButton(Direction.DOWN)};
			}
		} else {
			this.buttons = new FloorButton[] {new FloorButton(Direction.UP), new FloorButton(Direction.DOWN)};
		}
	}
	
	
}

class FloorButton {
	private Direction name;
	private boolean pressed;
	
	public FloorButton(Direction direction) {
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
	
	public Direction getName() {
		return name;
	}
}
