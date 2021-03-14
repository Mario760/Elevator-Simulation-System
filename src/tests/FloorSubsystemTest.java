package tests;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import FloorSubsystem.FloorButton;
import FloorSubsystem.FloorLamp;
import FloorSubsystem.FloorSubsystem;
import FloorSubsystem.Floor;
import Scheduler.Scheduler;

/**
 * @author Alec Jeffery
 */
public class FloorSubsystemTest {
	public int numberOfFloors;
	public Scheduler scheduler;
	public FloorSubsystem floorSubsystem;
	public List<Floor> floors;
	
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
		scheduler = new Scheduler(); 
	}
	
	@Test
	public final void testHandlers() {
		floorSubsystem = new FloorSubsystem(numberOfFloors);
		floors = floorSubsystem.getFloors();
		
		FloorLamp floorLamp = floors.get(1).getLamp();
		FloorButton[] floorButtons = floors.get(1).getButtons();
		
		// Test initial state
		assertEquals(floorLamp.getState(), false);
		assertEquals(floorLamp.getDirection(), null);
		assertEquals(floorButtons[0].isPressed(), false);
		assertEquals(floorButtons[1].isPressed(), false);
		
		
		// Test arrival handler
		floorSubsystem.handleArrival(2);
		assertEquals(floorLamp.getState(), true);
		assertEquals(floorLamp.getDirection(), null);
		assertEquals(floorButtons[0].isPressed(), false);
		assertEquals(floorButtons[1].isPressed(), false);
		
		// Test departure handler
		floorSubsystem.handleDeparture(2);
		assertEquals(floorLamp.getState(), false);
		assertEquals(floorLamp.getDirection(), null);
		assertEquals(floorButtons[0].isPressed(), false);
		assertEquals(floorButtons[1].isPressed(), false);
	}
	
}

