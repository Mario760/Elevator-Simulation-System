package tests;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.FloorSubsystem;
import main.Scheduler;

/**
 * @author Alec Jeffery
 */
public class FloorSubsystemTest {
	public int numberOfFloors;
	public String filePath;
	public Scheduler scheduler;
	public FloorSubsystem floorSubsystem;
	
	/**
	 * Test method to create tasks used in the FloorSubystem tests.
	 * @param currentElevatorFloor
	 * @param action
	 * @returns byte[]
	 */
	public byte[] createTestTask(int currentElevatorFloor, int action) {
		byte[] testTask = {(byte) currentElevatorFloor, (byte) action};
		return testTask;
	}
		
	@Before
	public void setUp() throws Exception {
		numberOfFloors = 6;
		filePath = "testPath";
		scheduler = new Scheduler(); 
	}
	
	@Test
	public final void testHandleTask() {
		floorSubsystem = new FloorSubsystem(numberOfFloors, scheduler, filePath);
		
		// this task is created when a failure occurs in the system
		// within the getNextFloorTask action
		byte[] failureTask = createTestTask(0, 0);
		assertFalse(floorSubsystem.handleTask(failureTask));
		
		// task created on elevator arriving at the floor
		byte[] arrivalTask = createTestTask(1, 0);
		assertTrue(floorSubsystem.handleTask(arrivalTask));
		
		// task created on elevator departing the floor
		byte[] departureTask = createTestTask(1, 1);
		assertTrue(floorSubsystem.handleTask(departureTask));
	}
	
}

