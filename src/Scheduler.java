import java.util.*;

/**
 * This is the scheduler class that follows the mutual exclusion and condition synchronization
 * @author Peyman Tajadod
 *
 */

public class Scheduler {
	
	private int floorData;
	private int elevatorData;
	private int currentElevatorFloor;
	private boolean elevatorEmpty = true;
	private boolean floorEmpty = true;
	private boolean done = false;
	private FloorTask floorTask = FloorTask.NOTHING;
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
	 * This method figures out what is asking for the next task and returns the appropriate method
	 * @param id 0 for elevator and 1 for floor
	 * @return a byte[] array to be parsed and translated
	 */
	public synchronized byte[] getNextTask(int id) {
		if (id == 1) {
			return getNextFloorTask();
		} else {
			//Elevator code goes here!
		}
	}
	
	public synchronized byte[] getNextFloorTask() {
		while(floorTask == FloorTask.NOTHING) {
			try {
				wait();
			} catch (InterruptedException e) {
				byte[] failure = {(byte) 0, (byte) 0};
				return failure;
			}
		}
		
		if (floorTask == FloorTask.ARRIVAL) {
			floorTask = FloorTask.NOTHING;
			byte[] task = {(byte) currentElevatorFloor, (byte) 0};
			return task;
		} else if (floorTask == FloorTask.DEPARTURE) {
			floorTask = FloorTask.NOTHING;
			byte[] task = {(byte) currentElevatorFloor, (byte) 1};
			return task;
		} else { // Button
			floorTask = FloorTask.NOTHING;
			Instruction info = instructions.peek();
			if (info.getFloorButton() == FloorDirection.UP) {
				byte[] task = {(byte) info.getFloor(), (byte) 2};
				return task;
			} else {
				byte[] task = {(byte) info.getFloor(), (byte) 3};
				return task;
			}
		}
		
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
