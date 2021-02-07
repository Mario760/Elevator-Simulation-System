
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
		assertEquals(bottomFloorButtons.length, 1);
		assertEquals(middleFloorButtons.length, 2);
		assertEquals(topFloorButtons.length, 1);
	}
}