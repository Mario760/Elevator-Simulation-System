/**
 * This class will be the datatype used to transfer and handle the input file instructions
 * I decided on a custom datatype to make using a queue simpler and it allows for expansion when we reach the fault iteration.
 * @author Campbell de Winter
 *
 */
public class Instruction {
	private String time;
	private int floor;
	private FloorDirection floorButton;
	private int carButton;
	//private int faultType; // currently not needed
	
	public Instruction(String time, int floor, FloorDirection floorButton, int carButton) {
		this.time = time;
		this.floor = floor;
		this.floorButton = floorButton;
		this.carButton = carButton;
	}
	
	/**
	 * Get method for time
	 * @return time String
	 */
	public String getTime() {
		return time;
	}
	
	/**
	 * This method converts the time into seconds. This makes calculations easier
	 * @return time but represented in seconds
	 */
	public double getTimeInSeconds() {
		String[] times = time.split(":");
		if (times.length !=  4) {
			System.out.println("Invalid Time\n");
			return -1;
		}
		int hours = Integer.parseInt(times[0]);
		int mins = Integer.parseInt(times[1]);
		int seconds = Integer.parseInt(times[2]);
		return (hours * 3600) + (mins * 60) + seconds;
	}
	
	/**
	 * Get method for floor
	 * @return floor int
	 */
	public int getFloor() {
		return floor;
	}
	
	/**
	 * Get method for floorButton
	 * @return floorButton FloorDirection
	 */
	public FloorDirection getFloorButton() {
		return floorButton;
	}
	
	/**
	 * Get method for carButton
	 * @return carButton int
	 */
	public int getCarButton() {
		return carButton;
	}
	
	
}
