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
	private boolean elevatorStateEmpty = true;
	private boolean elevatorFloorEmpty = true;
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
			return getNextElevatorTask();			
		}
	}
	
	public synchronized byte[] getNextElevatorTask() {
		
		//Elevator code goes here!
		//when the elevator arrives at a floor change floorTask to FloorTask.ARRIVAL
		//when the elevator sends its a state of not moving it should peek the queue for where to go next
		/**
		 * When the elevator arrives at a floor, the scheduler should pop the queue and save it to a Instruction variable and compare it to the next instruction (peek).
		 * If the next instruction is within (9 + time to travel) seconds of the first instruction and they are both going in the same direction, 
		 * check if the floor is between the elevator's current floor and its destination.
		 * If so, the elevator will need to stop early and repeat this process
		 */
		
		// I'd suggest a list of floors the elevator needs to visit either on scheduler or elevator because the elevator does not care why its going to the floor just the floor its going too
		
		while(floorEmpty) {
			try {
				wait();
				System.out.println("wait() elevator put state ");
			} catch (InterruptedException e) {
				return null;
			}		
		}
		
		if(getElevatorState()==MotorDirection.STOPPED) {
			
			if(this.currentElevatorFloor < instructions.peek().getFloor() ) {
				
				byte elevatorTask[] = {(byte)this.instructions.poll().getFloor(),(byte)0}; //byte 0 is direction Up
				return elevatorTask;
				
			}

		}

			
		}
		
		
	}
	
	public synchronized void putElevatorState(MotorDirection direction) {
		while (!elevatorStateEmpty) {
			try {
				wait();
				System.out.println("wait() elevator put state ");
			} catch (InterruptedException e) {
				return;
			}

		}
		this.elevatorState = direction;
		System.out.println("elevator put state = " + direction);
		this.elevatorStateEmpty = false;
		notifyAll();
		
	}
	
	public synchronized MotorDirection getElevatorState() {
		while (elevatorStateEmpty) {
			try {
				wait();
				System.out.println("wait() elevator get state ");
			} catch (InterruptedException e) {
				return null;
			}

		}
		MotorDirection stateTemp = this.elevatorState;
		this.elevatorState = MotorDirection.STOPPED;
		elevatorStateEmpty = true;
		notifyAll();
		System.out.println("elevator get state = " + stateTemp);
		return stateTemp;

	}

	public synchronized void putElevatorFloor(int floorNum) {
		while (!elevatorFloorEmpty) {
			try {
				wait();
				System.out.println("wait() elevator put floor ");
			} catch (InterruptedException e) {
				return;
			}
		}
		this.currentElevatorFloor = floorNum;
		elevatorFloorEmpty = false;
		notifyAll();
	}

	public synchronized int getElevatorFloor() {
		while (elevatorFloorEmpty) {
			try {
				wait();
				System.out.println("wait() elevator get floor ");
			} catch (InterruptedException e) {
				return 0;
			}

		}
		
		int elevatorFloorTemp = this.currentElevatorFloor;
		elevatorFloorEmpty = true;
		notifyAll();
		return elevatorFloorTemp;
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
			floorTask = FloorTask.DEPARTURE;
			byte[] task = {(byte) currentElevatorFloor, (byte) 0}; // 0 means arrival
			notifyAll();
			return task;
		} else if (floorTask == FloorTask.DEPARTURE) {
			floorTask = FloorTask.NOTHING;
			byte[] task = {(byte) currentElevatorFloor, (byte) 1}; // 1 means departure
			notifyAll();
			return task;
		} else { // Button press
			floorTask = FloorTask.NOTHING;
			Instruction info = instructions.peek();
			if (info.getFloorButton() == FloorDirection.UP) {
				byte[] task = {(byte) info.getFloor(), (byte) 2}; // 2 means UP
				notifyAll();
				return task;
			} else {
				byte[] task = {(byte) info.getFloor(), (byte) 3}; // 3 means DOWN
				notifyAll();
				return task;
			}
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
