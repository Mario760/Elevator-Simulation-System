package main;
import java.util.ArrayList;

/**
 * This class is the Elevator subsystem.
 *
 * @author Peyman Tajadod & Jiawei Ma
 */
public class Elevator implements Runnable {

	private Scheduler scheduler;
	private int elevatorNum;
	private int floorNumber; // the floor that the elevator is on
	private ArrayList<ElevatorButton> buttons; // number of buttons/floors in the system
	private MotorDirection motor;
	private boolean door;

	public Elevator(Scheduler scheduler, int elevatorNum, int buttonsNum) {
		this.elevatorNum = elevatorNum;
		this.floorNumber = 1;
		this.buttons = new ArrayList<>();
		for (int i = 1; i <= buttonsNum; i++) {
			buttons.add(new ElevatorButton(i, false));
		}
		this.motor = MotorDirection.STOPPED;
		this.door = false;
		this.scheduler = scheduler;

	}

	/**
	 * This method is to get the elevator number
	 * 
	 * @return
	 */
	public int getElevatorNum() {
		return this.elevatorNum;
	}

	/**
	 * This method is to change the direction of the elevator or make it stop
	 * 
	 * @param dir
	 */
	public void move(MotorDirection dir) {
		this.motor = dir;
	}

	/**
	 * This method is to toggle the enabled button of the elevator
	 * 
	 * @param btnNum
	 */
	public void toggleButton(int btnNum) {
		for (ElevatorButton btn : this.buttons) {
			if (btn.getNumber() == btnNum) {
				if (!btn.getEnabledLamp()) {
					btn.toggleLamp(true);
				} else {
					btn.toggleLamp(false);
				}
			}
		}
	}

	/**
	 * This method is to change the motor direction and go to the destination floor
	 * 
	 * @param floorNum, direction 
	 */

	public void goToFloor(byte floorNum, byte direction) {
		// close doors

		if (direction == (byte) 1) {
			move(MotorDirection.UP);
			// assuming it takes 3 second to travel between each floor
			int floorsToTravel = floorNum - this.floorNumber;
			System.out.println("Elevator moving UP to floor " + floorNum + "... takes 3 seonds each floor...");
			for (int i = 1; i <= floorsToTravel; i++) {
				try {
					System.out.println(".");
					System.out.println(this.floorNumber + i);
					Thread.sleep(3000);
				} catch (InterruptedException e) {
				}

			}

		} else if (direction == (byte) 2) {
			move(MotorDirection.DOWN);
			int floorsToTravel = this.floorNumber - floorNum;
			System.out.println("Elevator moving DOWN to floor " + floorNum + "... takes 3 seonds each floor.....");
			for (int i = 1; i <= floorsToTravel; i++) {
				try {
					System.out.println(".");
					System.out.println(this.floorNumber + i);
					Thread.sleep(3000);
				} catch (InterruptedException e) {
				}

			}

		}
		move(MotorDirection.STOPPED);
		this.floorNumber = (int) floorNum;
		int[] data = new int[2];
		data[0] = this.floorNumber;
		data[1] = 0;

		scheduler.putElevatorData(data);

		// open door
		this.door = true;
		System.out.println("Elevator arrived");
		System.out.println("Opening doors... takes 3 seconds");

		scheduler.reachedDepartureFloor(FloorTask.ARRIVAL);
		// wait 9 seconds
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
		System.out.println("Closing doors");
		this.door = false;
		
		// moved these to run() so testing was possible without Threads
//		scheduler.reachedDepartureFloor(FloorTask.DEPARTURE);
//		scheduler.getElevatorData();

	}

	/**
	 * This method is to get current floor number.
	 * 
	 * @return flooNumber, current floor number.
	 */
	public int getFloorNumber() {
		return floorNumber;
	}

	/**
	 * This method is to get motor direction
	 * 
	 * @return the motor direction
	 */
	public MotorDirection getMotor() {
		return motor;
	}

	/**
	 * This is the implementation of the run method from interface Runnable. This
	 * implementation is only for the purpose of Iteration 1 and it will be modified
	 *
	 */
	@Override
	public void run() {

		while (true) {

			System.out.println("Sending Elevator data to scheduler " + this.floorNumber);
			int[] data = new int[2];
			data[0] = this.floorNumber;
			if (this.motor == MotorDirection.STOPPED) {
				data[1] = 0;
			} else if (this.motor == MotorDirection.UP) {
				data[1] = 1;
			} else {
				data[1] = 2;
			}
			scheduler.putElevatorData(data);
			byte task[] = scheduler.getNextTask(0);
			goToFloor(task[0], task[1]);
			
			// moved these from goToFloor() so testing was possible without Threads
			scheduler.reachedDepartureFloor(FloorTask.DEPARTURE);
			scheduler.getElevatorData();

		}

	}

}

/**
 * This class is to represent the buttons of the elevator
 * 
 * @author Peyman Tajadod
 *
 */
class ElevatorButton {
	private int number;
	private boolean lamp;

	public ElevatorButton(int number, boolean enabledLamp) {
		this.number = number;
		this.lamp = enabledLamp;
	}

	public int getNumber() {
		return this.number;
	}

	public void toggleLamp(boolean toggle) {
		this.lamp = toggle;
	}

	public boolean getEnabledLamp() {
		return this.lamp;
	}

}
