
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SchedulerTest {
	public Scheduler scheduler;	
	
	@Before
	public void setUp() throws Exception {
		scheduler = new Scheduler();
	}

	@Test
	public final void testPutAndGet() {
		scheduler.put(0, 0);
		assertEquals(scheduler.get(0), 0);
		
		scheduler.put(1, 1);
		scheduler.put(2, 2);
		assertEquals(scheduler.get(1), 1);
		
	}
}
