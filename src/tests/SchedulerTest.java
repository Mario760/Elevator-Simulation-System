package tests;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ElevatorSubsystem.Elevator;
import FloorSubsystem.FloorDirection;
import FloorSubsystem.FloorSubsystem;
import Scheduler.Instruction;
import Scheduler.Scheduler;

/**
 * @author Alec Jeffery
 */
public class SchedulerTest {
	public Scheduler scheduler;	
	public Instruction testInstruction;
	public Elevator elevator;
	public FloorSubsystem floorSubsystem;
	
	@Before
	public void setUp() throws Exception {		
		scheduler = new Scheduler();
		testInstruction = new Instruction("14:14:14.0", 3, FloorDirection.UP, 4);
	}
	
	@Test
	public final void testArrangeCar() {
		// set elevatorData1 and elevatorData2, with the corresponding numbers in the 
		// first index of the elevatorData array
		int[] testElevatorData1 = {1, 1};
		int[] testElevatorData2 = {2, 2};
		
		scheduler.setElevatorData(testElevatorData1);
		scheduler.setElevatorData(testElevatorData2);
		
		assertEquals(scheduler.arrangeCar(testInstruction), 1);
	}
	
}