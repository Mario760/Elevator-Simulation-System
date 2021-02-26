import java.util.*;

/**
 * This is the scheduler class that follows the mutual exclusion and condition synchronization
 * @author Peyman Tajadod
 *
 */

public class Scheduler {
	
	private int floorData;
	private MotorDirection elevatorState = MotorDirection.STOPPED;
	private int currentElevatorFloor = 0;
	private boolean elevatorArrived = false;
	private int elevatorData[];
	private boolean elevatorEmpty;
//	private boolean elevatorStateEmpty = true;
//	private boolean elevatorFloorEmpty = true;
	private boolean floorEmpty = true;
	private FloorTask floorTask = FloorTask.NOTHING;
	private Instruction instruction;
	private boolean instructionEmpty = false;
	
	
	/**
	 * This method will be called by the FloorSubsystem to populate the instructions list
	 * @param instruction an Instruction
	 */
	public synchronized void receiveInstruction(Instruction instruction) {
		while(!instructionEmpty) {
			try {
				wait();
				System.out.println("wait() instruction put ");
			} catch (InterruptedException e) {
				return;
			}
		}
		this.instruction = instruction;
		instructionEmpty = false;
		notifyAll();
		
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
			return getNextElevatorTask();			
		}
	}
	
	public synchronized byte[] getNextElevatorTask() {

		while (instructionEmpty) {
			try {
				wait();
				System.out.println("wait() elevator get instruction ");
			} catch (InterruptedException e) {
				return null;
			}
		}
		byte elevatorTask[] = {};

		if (this.elevatorData[1] == 0) {// if stopped

			if (this.elevatorData[0] < instruction.getFloor()) {
				elevatorTask[0] = (byte) this.instruction.getFloor();
				elevatorTask[1] = (byte) 1; // byte 1 is direction Up
				instruction = null;
				instructionEmpty = true;
				return elevatorTask;
			}
			if (this.elevatorData[0] > instruction.getFloor()) {
				elevatorTask[0] = (byte) this.instruction.getFloor();
				elevatorTask[1] = (byte) 2;// byte 2 is direction Down
				instruction = null;
				instructionEmpty = true;
				return elevatorTask;
			}

		} else if (this.elevatorData[1] != 0) { // moving
			//to be implemented next iteration
			return elevatorTask;
		}
		return elevatorTask;

	}		
	
	
	public synchronized void putElevatorData(int[] data) {
		while (!elevatorEmpty) {
			try {
				wait();
				System.out.println("wait() elevator put data ");
			} catch (InterruptedException e) {
				return;
			}

		}
		this.elevatorData = data;
		System.out.println("elevator put data");
		this.elevatorEmpty = false;
		notifyAll();
		
	}
	
	public synchronized int[] getElevatorData() {
		while (elevatorEmpty) {
			try {
				wait();
				System.out.println("wait() elevator get data ");
			} catch (InterruptedException e) {
				return null;
			}

		}
		int[] eDataTemp = this.elevatorData;
		elevatorEmpty = true;
		notifyAll();
		System.out.println("elevator get data ");
		return eDataTemp;

	}

	
	public synchronized void reachedDepartureFloor(FloorTask state) {
		while (floorTask != FloorTask.NOTHING) {
			try {
				wait();
				System.out.println("wait() elevator reach/departure floor ");
			} catch (InterruptedException e) {
				return;
			}
		}
		
		floorTask = state;
		notifyAll();
	}

	
	/**
	 * This method determines the next floor task by using the floorTask enum
	 * @return a byte[] containing the next instruction for the floor
	 */
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
			byte[] task = {(byte) currentElevatorFloor, (byte) 0}; // 0 means arrival
			notifyAll();
			return task;
		} else {
			floorTask = FloorTask.NOTHING;
			byte[] task = {(byte) currentElevatorFloor, (byte) 1}; // 1 means departure
			notifyAll();
			return task;
		}
		
	}
	
	
//	
//	/**
//	 * This method will receive a floor from the floorSubsystem 
//	 * It will be used to tell the elevator where to go next
//	 * @param id identifies what type of object is calling get (0 for elevator, 1 for floor)
//	 * @param param information being stored in the scheduler
//	 */
//	public synchronized void put(int id, int param) {//id 0 -> elevator sub system, 1 -> floor sub system
//		if(id == 0) {
//			while(!elevatorEmpty) {			
//				try {			
//					wait();	
//					System.out.println("wait() elevator put ");		
//					
//				} catch(InterruptedException e) {
//					return;
//				}
//				
//			}
//		
//			elevatorData = param;
//			System.out.println("elevator put data = " + elevatorData);
//			this.elevatorEmpty = false;
//			
//		} else {
//			while(!floorEmpty) {				
//				try {					
//					wait();
//					System.out.println("wait() floor put  ");
//					
//				} catch(InterruptedException e) {		
//					return;
//				}
//			}
//			
//			floorData = param;
//			System.out.println("floor put data = " + floorData);
//			this.floorEmpty = false;
//		}
//
//		notifyAll();
//	}
//	
//	/**
//	 * This method will be called by the FloorSubsystem and Elevator subsystem to get the relevant data
//	 * @return the floor that the elevator stopped at or the floorNumber that the elevator should go
//	 * @param id identifies what type of object is calling get (0 for elevator, 1 for floor)
//	 */
//	public synchronized int get(int id) {
//		
//		if(id == 0) { 
//			while(floorEmpty) {	
//				try {	
//					wait();
//					System.out.println("wait() elevator get ");				
//				} catch (InterruptedException e) {
//	
//				}
//				
//			}
//			int floorDataTemp = floorData;
//			System.out.println("elevator get floor data = " + floorDataTemp);
//			this.floorEmpty = true;
//			notifyAll();
//			return floorDataTemp;
//
//		} else { 
//			while(elevatorEmpty) {
//				try {
//					wait();
//					System.out.println("wait() floor get ");	
//				} catch (InterruptedException e) {	
//				}
//				
//			}
//			int elevatorDataTemp = elevatorData;
//			System.out.println("floor get elevator data = " + elevatorDataTemp);
//			this.elevatorEmpty = true;
//			notifyAll();
//			return elevatorData;
//
//		}
//
//	}
}
