package tests;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ElevatorSubsystem.Elevator;
import Scheduler.MotorDirection;
import Scheduler.Scheduler;

/**
 * Test for Elevator class
 * @author Jiawei Ma & Alec Jeffery
 */
public class ElevatorTest {
    public Elevator elevator;

    @Before
    public void setUp() throws Exception {
        elevator = new Elevator(1, 6);
    }

    @Test
    public final void testGoToFloor() {
        // Test the default floor number is at 1 and the motor is stopped.
        assertEquals(elevator.getFloorNumber(), 1);
        assertEquals(elevator.getMotor(),MotorDirection.STOPPED);

        // Several test cases to the elevator does go to the required floor.
        
        // Test to go up to floor 3 as destination
        elevator.goToFloor((byte) 3, (byte) 1, "destination");
        assertEquals(elevator.getFloorNumber(), 3);
        assertEquals(elevator.getMotor(),MotorDirection.STOPPED);
        
        // Test to go down to floor 2 as pick up
        elevator.goToFloor((byte) 2, (byte) 2, "pickup");
        assertEquals(elevator.getFloorNumber(), 2);
        assertEquals(elevator.getMotor(),MotorDirection.PAUSED);

        // Test to go up to floor 6 as pick up
        elevator.goToFloor((byte) 6, (byte) 1, "pickup");
        assertEquals(elevator.getFloorNumber(), 6);
        assertEquals(elevator.getMotor(),MotorDirection.PAUSED);

        // Test to go down to floor 5 as destination
        elevator.goToFloor((byte) 5, (byte) 2, "destination");
        assertEquals(elevator.getFloorNumber(), 5);
        assertEquals(elevator.getMotor(),MotorDirection.STOPPED);
    }
}