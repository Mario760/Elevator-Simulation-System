

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FloorSubsystemTest {
		String filePath;
		Scheduler scheduler;
		Thread floorSubsystem;
	@Before
	public void setUp() throws Exception {
		filePath = "testPath";
		scheduler = new Scheduler();
	}

	@Test
	public final void testConstructor() {
		floorSubsystem = new Thread(new FloorSubsystem(6, scheduler, filePath), "FloorSubsystem");
		assertTrue(true);
	}
}