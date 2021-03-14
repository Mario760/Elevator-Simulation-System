package ElevatorSubsystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import Scheduler.FloorTask;
import Scheduler.MotorDirection;
import Scheduler.Scheduler;

/**
 * This class is the Elevator subsystem.
 *
 * @author Peyman Tajadod & Jiawei Ma
 */
public class Elevator implements Runnable {

	private int elevatorNum;
	private int floorNumber; // the floor that the elevator is on
	private ArrayList<ElevatorButton> buttons; // number of buttons/floors in the system
	private MotorDirection motor;
	private boolean door;
	private DatagramSocket sendSocket, receiveSocket;
	private DatagramPacket sendPacket, receivePacket;

	public Elevator(int elevatorNum, int buttonsNum) {
		this.elevatorNum = elevatorNum;
		this.floorNumber = 1;
		this.buttons = new ArrayList<>();
		for (int i = 1; i <= buttonsNum; i++) {
			buttons.add(new ElevatorButton(i, false));
		}
		this.motor = MotorDirection.STOPPED;
		this.door = false;
		try {
			sendSocket = new DatagramSocket();
			receiveSocket = new DatagramSocket(elevatorNum * 1111);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}

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

	public void goToFloor(byte floorNum, byte direction, String task) {
		// close doors

		if (direction == (byte) 1) {
			move(MotorDirection.UP);
			// assuming it takes 3 second to travel between each floor
			int floorsToTravel = floorNum - this.floorNumber;
			System.out.println(
					"Elevator " +this.elevatorNum+ " moving UP to " + task + " floor " + floorNum + "... takes 3 seonds each floor...");
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
			System.out.println(
					"Elevator "+this.elevatorNum+" moving DOWN to " + task + " floor " + floorNum + "... takes 3 seonds each floor.....");
			for (int i = 1; i <= floorsToTravel; i++) {
				try {
					System.out.println(".");
					System.out.println(this.floorNumber + i);
					Thread.sleep(3000);
				} catch (InterruptedException e) {
				}

			}
 
		}

		// scheduler.putElevatorData(data);
		if (task == "pickup") {
			move(MotorDirection.PAUSED);
		} else {
			move(MotorDirection.STOPPED);
		}
		this.floorNumber = (int) floorNum;

		// open door
		this.door = true;
		System.out.println("Elevator arrived at " + task);
		byte[] data = new byte[3];
		data[0] = (byte) elevatorNum;
		data[1] = (byte) this.floorNumber;
		if (this.motor == MotorDirection.STOPPED) {
			data[2] = (byte) 0;
		} else if (this.motor == MotorDirection.PAUSED) {
			data[2] = (byte) 1;
		} else if (this.motor == MotorDirection.UP) {
			data[2] = (byte) 2;
		} else {
			data[2] = (byte) 3;
		}

		try {
			sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 11);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			System.exit(1);
		}

		try {
			sendSocket.send(sendPacket);

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Opening doors... takes 3 seconds");

//		scheduler.reachedDepartureFloor(FloorTask.ARRIVAL);// udp
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
		boolean initial = true;

		while (true) {

			if (initial == true) {
				System.out.println("Sending Elevator " + elevatorNum + " data to scheduler: " + "[" + this.elevatorNum
						+ ", " + this.floorNumber + ", " + motor.toString() + "]");
				byte[] data = new byte[3];
				data[0] = (byte) elevatorNum;
				data[1] = (byte) floorNumber;
				if (this.motor == MotorDirection.STOPPED) {
					data[2] = (byte) 0;
				} else if (this.motor == MotorDirection.UP) {
					data[2] = (byte) 1;
				} else {
					data[2] = (byte) 2;
				}

				try {
					sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 11);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				try {
					sendSocket.send(sendPacket);

				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				}
				initial = false;
			}

			byte task[] = new byte[3];
			receivePacket = new DatagramPacket(task, task.length);
			try {
				// Block until a datagram is received via sendReceiveSocket.
				receiveSocket.receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			// byte task[] = scheduler.getNextTask(0);
			goToFloor(task[2], task[1], "pickup");
			goToFloor(task[0], task[1], "destination");

			// moved these from goToFloor() so testing was possible without Threads
//			scheduler.reachedDepartureFloor(FloorTask.DEPARTURE);
//			scheduler.getElevatorData();

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
