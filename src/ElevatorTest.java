import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 *  Test for Elevator class
 * @Author: Jiawei Ma,101034173
 */
public class ElevatorTest {
    public Scheduler scheduler;
    public Elevator elevator;

    @Before
    public void setUp() throws Exception {
        scheduler = new Scheduler();
        elevator = new Elevator(scheduler, 1, 6);
    }

    @Test
    public final void testGoToFloor() {
        //Test the default floor number is at 1 and the motor is stopped.
        assertEquals(elevator.getFlooNumber(),1);
        assertEquals(elevator.getMotor(),MotorDirection.STOPPED);//Test motor direction for future use.

        //Several test cases to the elevator does go to the required floor.
        elevator.goToFloor(3);
        assertEquals(elevator.getFlooNumber(),3);
        assertEquals(elevator.getMotor(),MotorDirection.STOPPED);

        //Test to go to floor 2
        elevator.goToFloor(2);
        assertEquals(elevator.getFlooNumber(),2);
        assertEquals(elevator.getMotor(),MotorDirection.STOPPED);

        //Test to go to floor 6
        elevator.goToFloor(6);
        assertEquals(elevator.getFlooNumber(),6);
        assertEquals(elevator.getMotor(),MotorDirection.STOPPED);

        //Test to go to floor 5
        elevator.goToFloor(5);
        assertEquals(elevator.getFlooNumber(),5);
        assertEquals(elevator.getMotor(),MotorDirection.STOPPED);
    }

}