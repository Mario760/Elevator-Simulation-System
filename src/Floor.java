
public class Floor {
	private int floorNumber;
	private FloorButton[] buttons;
	
	public Floor(int floorNumber, FloorButton[] buttons) { // a top floor variable is needed to know if the sub system is on the top floor
		this.floorNumber = floorNumber;
		this.buttons = buttons;
	}
}
