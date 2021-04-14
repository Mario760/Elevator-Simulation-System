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
	private boolean running = true;
	private long movingTime = 3000;
	private long doorTime = 3000;
	byte[] data;
	private ElevatorPanel panel;

	public Elevator(int elevatorNum, int buttonsNum, ElevatorPanel panel) {
		this.elevatorNum = elevatorNum;
		this.floorNumber = 1;
		this.panel = panel; // GUI stuff :O
		this.buttons = new ArrayList<>();
		data = new byte[3];
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
	 * This method is to get the state of running
	 * 
	 * @return
	 */
	public boolean getRunning() {
		return this.running;
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

	public void goToFloor(byte floorNum, byte direction, String task, byte fault) {
		data[0] = (byte) elevatorNum;


		if (direction == (byte) 1) {
			move(MotorDirection.UP);
			panel.setElevatorMoving(MotorDirection.UP);
			// assuming it takes 3 second to travel between each floor
			int floorsToTravel = floorNum - this.floorNumber;
			long delay = floorsToTravel * movingTime + 1000;
			if (fault == (byte) 1) {
				delay = 2000;
			}
			System.out.println("Elevator " + this.elevatorNum + " at floor "+ this.floorNumber + " moving UP to " + task + " floor " + floorNum
					+ "... takes " + movingTime/1000 + " seconds each floor...");
			
			//timer for fault 1 case
			long start = System.currentTimeMillis();

			for (int i = 1; i <= floorsToTravel; i++) {
				try {
					System.out.println(".");
					System.out.println(this.floorNumber + i);
					panel.setElevatorFloor(this.floorNumber + i);
					Thread.sleep(movingTime);
					if ((System.currentTimeMillis() - start) > delay) {
						running = false;
						System.out.println("!!!Fault 1 occurred (timer went off). Shutting down elevator " + elevatorNum +"!!!\n");
						panel.setElevatorFault("Elevator timer failure!");
						data[1] = -1;
						data[2] = -1;
						try {//sending a packet to notify scheduler about this fault and elevator shutdown
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
						return;
					}
				} catch (InterruptedException e) {
				}

			}

		} else if (direction == (byte) 2) {
			move(MotorDirection.DOWN);
			panel.setElevatorMoving(MotorDirection.DOWN);
			int floorsToTravel = this.floorNumber - floorNum;
			long delay = floorsToTravel * movingTime + 1000;
			if (fault == (byte) 1) {
				delay = 2000;
			}
			System.out.println("Elevator " + this.elevatorNum + " at floor "+ this.floorNumber + " moving DOWN to " + task + " floor " + floorNum
					+ "... takes " + movingTime/1000 + " seconds each floor.....");

			long start = System.currentTimeMillis();

			for (int i = 1; i <= floorsToTravel; i++) {
				try {
					System.out.println(".");
					System.out.println(this.floorNumber - i);
					panel.setElevatorFloor(this.floorNumber - i);
					Thread.sleep(movingTime);
					if ((System.currentTimeMillis() - start) > delay) {
						running = false;
						System.out.println("!!!Fault 1 occured (timer went off). Shutting down elevator " + elevatorNum +"!!!\n");
						panel.setElevatorFault("Elevator timer failure!");
						data[1] = -1;
						data[2] = -1;
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
						return;
					}
				} catch (InterruptedException e) {
				}

			}

		}

		// scheduler.putElevatorData(data);
		if (task == "pickup") {
			move(MotorDirection.PAUSED);
			panel.setElevatorMoving(MotorDirection.PAUSED);
		} else {
			move(MotorDirection.STOPPED);
			panel.setElevatorMoving(MotorDirection.STOPPED);
		}
		this.floorNumber = (int) floorNum;
		panel.setElevatorFloor(this.floorNumber);

		// open door
		this.door = true;
		System.out.println("Elevator arrived at " + task);

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
		System.out.println("Opening doors... takes " + doorTime/1000 + "seconds");

//		scheduler.reachedDepartureFloor(FloorTask.ARRIVAL);// udp
		// wait 9 seconds
		try {
			Thread.sleep(doorTime);
		} catch (InterruptedException e) {
		}

		System.out.println("Closing doors");
		if(fault==(byte) 2 ) {
			System.out.println("!!!Fault 2 occured (Door is not closed). Trying again... " + elevatorNum +"!!!\n");
			panel.setElevatorFault("Door failed to close");
			data[1] = (byte)floorNum;
			data[2] = -2;
			try {//sending a packet to scheduler so it would notify the floor doors
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
			System.out.println("Closing doors again...");
			panel.setElevatorFault("Everything is fine");
		}
		this.door = false;


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

		while (running) {

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
					e1.printStackTrace();
					System.exit(1);
				}

				try {
					sendSocket.send(sendPacket);

				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				}
				initial = false;
			}

			byte task[] = new byte[4];
			receivePacket = new DatagramPacket(task, task.length);
			try {
				// Block until a datagram is received via sendReceiveSocket.
				receiveSocket.receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			// byte task[] = scheduler.getNextTask(0);
			if((int)task[2] < this.floorNumber) {
				goToFloor(task[2], (byte)2, "pickup", task[3]);
			}else if((int)task[2] > this.floorNumber) {
				goToFloor(task[2], (byte)1, "pickup", task[3]);
			}
			if (running) {
				goToFloor(task[0], task[1], "destination", task[3]);
			}


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
