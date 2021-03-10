package tests;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import FloorSubsystem.Floor;
import FloorSubsystem.FloorButton;
import FloorSubsystem.FloorDirection;
import FloorSubsystem.FloorLamp;

/**
 * @author Alec Jeffery
 */
public class FloorTest {
	public Floor bottomFloor, middleFloor, topFloor;

	@Before
	public void setUp() throws Exception {
		bottomFloor = new Floor(1, 1);
		middleFloor = new Floor(2, 2);
		topFloor = new Floor(3, 1);
	}

	@Test
	public final void testConstructor() {
		FloorButton bottomFloorButtons[] = bottomFloor.getButtons();
		FloorButton middleFloorButtons[] = middleFloor.getButtons();
		FloorButton topFloorButtons[] = topFloor.getButtons();
		
		// top and bottom floors should have 1 button, 
		// any other floor will have 2
		assertEquals(bottomFloor.getButtons().length, 1);
		assertEquals(middleFloorButtons.length, 2);
		assertEquals(topFloorButtons.length, 1);
	}
	
	/**
	 * Tests both the arrival and departure handlers in the
	 * Floor class.
	 */
	@Test
	public final void testHandler() {
		FloorLamp floorLamp = middleFloor.getLamp();
		FloorButton[] floorButtons = middleFloor.getButtons();
		
		// Test initial state
		assertEquals(floorLamp.getState(), false);
		assertEquals(floorLamp.getDirection(), null);
		assertEquals(floorButtons[0].isPressed(), false);
		assertEquals(floorButtons[1].isPressed(), false);
		
		// Test states after pressButton and handleDearture
		middleFloor.pressButton(FloorDirection.UP);
		middleFloor.handleDeparture();
		assertEquals(floorLamp.getState(), false);
		assertEquals(floorLamp.getDirection(), FloorDirection.UP);
		assertEquals(floorButtons[0].isPressed(), true);
		assertEquals(floorButtons[1].isPressed(), false);

		// Test states after handleArrival
		middleFloor.handleArrival();
		assertEquals(floorLamp.getState(), true);
		assertEquals(floorLamp.getDirection(), FloorDirection.UP);
		assertEquals(floorButtons[0].isPressed(), false);
		assertEquals(floorButtons[1].isPressed(), false);
	}
}