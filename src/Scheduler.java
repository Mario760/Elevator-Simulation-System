import java.util.*;

/**
 * This is the scheduler class that follows the mutual exclusion and condition synchronization
 * @author Peyman Tajadod
 *
 */

public class Scheduler {
	
	private int floorData;
	private int elevatorData;
	private boolean elevatorEmpty = true;
	private boolean floorEmpty = true;
	private boolean done = false;
	
	private Queue<Instruction> instructions;
	
	/**
	 * Constructor
	 * Used to set up the instructions queue
	 */
	public Scheduler() {
		instructions = new LinkedList<Instruction>();
	}
	
	/**
	 * This method will be called by the FloorSubsystem to populate the instructions list
	 * @param instruction an Instruction
	 */
	public void receiveInstruction(Instruction instruction) {
		instructions.add(instruction);
	}
	
	public boolean getDone() {
		return done;
	}
	
	
	
	/**
	 * This method will receive a floor from the floorSubsystem 
	 * It will be used to tell the elevator where to go next
	 * @param id identifies what type of object is calling get (0 for elevator, 1 for floor)
	 * @param param information being stored in the scheduler
	 */
	public synchronized void put(int id, int param) {//id 0 -> elevator sub system, 1 -> floor sub system
		if(id == 0) {
			while(!elevatorEmpty) {			
				try {			
					wait();	
					System.out.println("wait() elevator put ");		
					
				} catch(InterruptedException e) {
					return;
				}
				
			}
		
			elevatorData = param;
			System.out.println("elevator put data = " + elevatorData);
			this.elevatorEmpty = false;
			
		} else {
			while(!floorEmpty) {				
				try {					
					wait();
					System.out.println("wait() floor put  ");
					
				} catch(InterruptedException e) {		
					return;
				}
			}
			
			floorData = param;
			System.out.println("floor put data = " + floorData);
			this.floorEmpty = false;
		}

		notifyAll();
	}
	
	/**
	 * This method will be called by the FloorSubsystem and Elevator subsystem to get the relevant data
	 * @return the floor that the elevator stopped at or the floorNumber that the elevator should go
	 * @param id identifies what type of object is calling get (0 for elevator, 1 for floor)
	 */
	public synchronized int get(int id) {
		
		if(id == 0) { 
			while(floorEmpty) {	
				try {	
					wait();
					System.out.println("wait() elevator get ");				
				} catch (InterruptedException e) {
	
				}
				
			}
			int floorDataTemp = floorData;
			System.out.println("elevator get floor data = " + floorDataTemp);
			this.floorEmpty = true;
			notifyAll();
			return floorDataTemp;

			
		} else { 
			while(elevatorEmpty) {
				try {
					wait();
					System.out.println("wait() floor get ");	
				} catch (InterruptedException e) {	
				}
				
			}
			int elevatorDataTemp = elevatorData;
			System.out.println("floor get elevator data = " + elevatorDataTemp);
			this.elevatorEmpty = true;
			notifyAll();
			return elevatorData;

		}
			
		
	
	}
}
