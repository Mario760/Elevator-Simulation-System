package tests;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.Elevator;
import main.Scheduler;
import main.MotorDirection;

/**
 *  Test for Elevator class
 * @author Jiawei Ma & Alec Jeffery
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
        // Test the default floor number is at 1 and the motor is stopped.
        assertEquals(elevator.getFloorNumber(),1);
        assertEquals(elevator.getMotor(),MotorDirection.STOPPED);//Test motor direction for future use.

        // Several test cases to the elevator does go to the required floor.
        
        // Test to go up to floor 3
        elevator.goToFloor((byte) 3, (byte) 1);
        scheduler.getNextFloorTask();
        assertEquals(elevator.getFloorNumber(),3);
        assertEquals(elevator.getMotor(),MotorDirection.STOPPED);
        

        // Test to go down to floor 2
        scheduler.resetFloorTaskAndElevatorEmpty();
        elevator.goToFloor((byte) 2, (byte) 2);
        assertEquals(elevator.getFloorNumber(),2);
        assertEquals(elevator.getMotor(),MotorDirection.STOPPED);

        // Test to go up to floor 6
        scheduler.resetFloorTaskAndElevatorEmpty();
        elevator.goToFloor((byte) 6, (byte) 1);
        assertEquals(elevator.getFloorNumber(),6);
        assertEquals(elevator.getMotor(),MotorDirection.STOPPED);

        // Test to go down to floor 5
        scheduler.resetFloorTaskAndElevatorEmpty();
        elevator.goToFloor((byte) 5, (byte) 2);
        assertEquals(elevator.getFloorNumber(),5);
        assertEquals(elevator.getMotor(),MotorDirection.STOPPED);
    }
}