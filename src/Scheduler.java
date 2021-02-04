
public class Scheduler {
	
	
	/**
	 * This method will receive a floor from the floorSubsystem (it will wait until the elevator has verified that it is not moving)
	 * It will be used to tell the elevator where to go next
	 * @param floor the floor that the elevator should go next
	 */
	public synchronized void giveFloor(int floor) {
		
	}
	
	/**
	 * This method will be called by the FloorSubsystem to wait for the elevator to stop at the floor requested by the give floor method
	 * @return the floor that the elevator stopped at
	 */
	public synchronized int waitForArrival() {
		return 0;
	}
}
