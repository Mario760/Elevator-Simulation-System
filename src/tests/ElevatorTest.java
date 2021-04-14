package tests;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ElevatorSubsystem.Elevator;
import ElevatorSubsystem.ElevatorPanel;
import Scheduler.MotorDirection;
import Scheduler.Scheduler;

/**
 * Test for Elevator class
 * @author Jiawei Ma & Alec Jeffery
 */
public class ElevatorTest {
    public Elevator elevator;
    public ElevatorPanel elevatorPanel;

    @Before
    public void setUp() throws Exception {
    	elevatorPanel = new ElevatorPanel(1, 1, MotorDirection.STOPPED);
        elevator = new Elevator(1, 6, elevatorPanel);
    }

    @Test
    public final void testGoToFloor() {
        // Test the default floor number is at 1 and the motor is stopped.
        assertEquals(elevator.getFloorNumber(), 1);
        assertEquals(elevator.getMotor(),MotorDirection.STOPPED);

        // Several test cases to the elevator does go to the required floor.
        
        // Test to go up to floor 3 as destination
        elevator.goToFloor((byte) 3, (byte) 1, "destination", (byte) 0);
        assertEquals(elevator.getFloorNumber(), 3);
        assertEquals(elevator.getMotor(),MotorDirection.STOPPED);
        
        // Test to go down to floor 2 as pick up
        elevator.goToFloor((byte) 2, (byte) 2, "pickup", (byte) 0);
        assertEquals(elevator.getFloorNumber(), 2);
        assertEquals(elevator.getMotor(),MotorDirection.PAUSED);

        // Test to go up to floor 6 as pick up
        elevator.goToFloor((byte) 6, (byte) 1, "pickup", (byte) 0);
        assertEquals(elevator.getFloorNumber(), 6);
        assertEquals(elevator.getMotor(),MotorDirection.PAUSED);

        // Test to go down to floor 5 as destination
        elevator.goToFloor((byte) 5, (byte) 2, "destination", (byte) 0);
        assertEquals(elevator.getFloorNumber(), 5);
        assertEquals(elevator.getMotor(),MotorDirection.STOPPED);
        
        
        
        // Fault test cases for fault's 1 & 2
        
        // Test to go down to floor 3 as destination with fault code 1
        elevator.goToFloor((byte) 3, (byte) 2, "destination", (byte) 1);
        assertEquals(elevator.getFloorNumber(), 5);
        assertEquals(elevator.getMotor(),MotorDirection.DOWN);
        assertEquals(elevator.getRunning(), false);
        
        // Test to go up to floor 6 as pick up with fault code 2
        elevator.goToFloor((byte) 6, (byte) 2, "pickup", (byte) 2);
        assertEquals(elevator.getFloorNumber(), 6);
        assertEquals(elevator.getMotor(),MotorDirection.PAUSED);
    }
}