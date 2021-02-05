import java.util.ArrayList;
import java.util.Vector;

/**
 * 
 * @author Alexander Tasseron
 *
 */

public class Scheduler {
	
//	private ArrayList<Integer> contents; //0th -> floor number, 1st -> instructions... 
	private int floorData;
	private int elevatorData;
	private boolean elevatorEmpty = true;
	private boolean floorEmpty = true;
	
	/**
	 * This method will receive a floor from the floorSubsystem (it will wait until the elevator has verified that it is not moving)
	 * It will be used to tell the elevator where to go next
	 * @param id identifies what type of object is calling get (0 for elevator, 1 for floor)
	 * @param param information being stored in the vector, location dependent on id
	 */
	public synchronized void put(int id, int param) {//id 0 -> elevator sub system, 1 -> floor sub system
//		System.out.println(id + " : " + param);
		if(id == 0) {
			while(!elevatorEmpty) {			
				try {			
					wait();	
					System.out.println("wait() elevator put -> contents: ");		
					
				} catch(InterruptedException e) {
					return;
				}
				
			}
		
			elevatorData = param;
			System.out.println("elevator put -> ED = " + elevatorData);
			this.elevatorEmpty = false;
			
		} else { //else, instructions to 1st indice
			while(!floorEmpty) {				
				try {					
					wait();
					System.out.println("wait() floor put -> contents: ");
					
				} catch(InterruptedException e) {		
					return;
				}
			}
			
			floorData = param;
			System.out.println("floor put -> FD = " + floorData);
			this.floorEmpty = false;
		}

		notifyAll();
	}
	
	/**
	 * This method will be called by the FloorSubsystem to wait for the elevator to stop at the floor requested by the give floor method
	 * @return the floor that the elevator stopped at
	 * @param id identifies what type of object is calling get (0 for elevator, 1 for floor)
	 */
	public synchronized int get(int id) {
		
		int forFloor;
		int forElevator;

		if(id == 0) { 
			while(floorEmpty) {	
				try {	
					wait();
					System.out.println("wait() elevator get -> contents: ");				
				} catch (InterruptedException e) {
	
				}
				
			}
			int floorDataTemp = floorData;
			System.out.println("elevator get -> FD = " + floorDataTemp);
			this.floorEmpty = true;
			notifyAll();
			return floorDataTemp;

			
		} else { 
			while(elevatorEmpty) {
				try {
					wait();
					System.out.println("wait() floor get -> contents: ");	
				} catch (InterruptedException e) {	
				}
				
			}
			int elevatorDataTemp = elevatorData;
			System.out.println("floor get -> ED = " + elevatorDataTemp);
			this.elevatorEmpty = true;
			notifyAll();
			return elevatorData;

		}
			
		
	
	}
}
