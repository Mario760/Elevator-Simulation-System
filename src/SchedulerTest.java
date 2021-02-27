import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Alec Jeffery
 */
public class SchedulerTest {
	public Scheduler scheduler;	
//	public Thread elevator,  floorSubsystem;
	public Instruction testInstruction;
	public Elevator elevator;
	public FloorSubsystem floorSubsystem;
	
	@Before
	public void setUp() throws Exception {
		String testFilePath = "testPath";	
		
		scheduler = new Scheduler();
		elevator = new Elevator(scheduler, 1, 8);
		floorSubsystem = new FloorSubsystem(8, scheduler, testFilePath);
		testInstruction = new Instruction("14:14:14.0", 2, FloorDirection.UP, 4);
	}
	
	@Test
	public final void testGetNextElevatorTask() {
		// to be able to run getNextElevatorTask, the scheduler must first
		// receive an instruction and have elevator data be present
		int[] elevatorData = {2, 1};
		
		scheduler.receiveInstruction(testInstruction);
		scheduler.putElevatorData(elevatorData);
		
		byte[] elevatorTask = scheduler.getNextElevatorTask();

		assertEquals((int) elevatorTask[0], 0);
		assertEquals((int) elevatorTask[1], 0);
	}
	
	@Test
	public final void testGetNextFloorTask() {
		// to be able to run getNextFloorTask, the scheduler must first 
		// receive and instruction and have reachedDepartureFloor called
		int[] elevatorData = {2, 1};
		
		scheduler.receiveInstruction(testInstruction);
		scheduler.reachedDepartureFloor(FloorTask.ARRIVAL);
		scheduler.putElevatorData(elevatorData);
		
		byte[] floorTask = scheduler.getNextFloorTask();

		assertEquals((int) floorTask[0], 2);
		assertEquals((int) floorTask[1], 0);
	}
	
}