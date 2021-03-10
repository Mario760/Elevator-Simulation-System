package FloorSubsystem;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import main.Scheduler;

/*
* git good or git gone
*/

/**
 * This class handles the FloorSubsytem Thread
 * It implements runnable
 * @author Campbell de Winter
 *
 */
public class FloorSubsystem {
	private List<Floor> floors; // list of floors
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket instructionSocket; // This socket will send packets to the scheduler
	
	/**
	 * The constructor for the FloorSubSystem
	 * @param numberOfFloors the number of floors to create
	 * @param scheduler the scheduler
	 * @param filePath the file path containing the input file
	 */
	public FloorSubsystem(int numberOfFloors, Scheduler scheduler, String filePath) {
		if (numberOfFloors <= 1) {
			System.out.print("No need for an elevator if the building has only 1 floor");
			System.exit(1);
			// throw SillyGooseException e;
		}
		
		this.floors = new ArrayList<Floor>();
		for (int i = 1; i <= numberOfFloors; i++) {
			if (i == 1) { // first floor
				floors.add(new Floor(i, 1));
			} else if (i == numberOfFloors) { // last floor
				floors.add(new Floor(i, 1));
			} else { // other floor
				floors.add(new Floor(i, 2));
			}
		}
		
		try {
			this.instructionSocket = new DatagramSocket(2);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * This method presses one of the floors buttons (by calling pressButton() on the floor) based on the info hashmap
	 */
	public synchronized void pressButton(int floor, FloorDirection direction) {
		floors.get(floor - 1).pressButton(direction);
	}
	
	/**
	 * This method handles the arrival condition of the elevator at a certain floor
	 * It calls the floor's handleArrival() method, Sleeps for 9 seconds (9.175 was our average load/unload time) and then calls the floor's handleDeparture() method
	 * @param floor the floor that the elevator arrived at
	 */
	public synchronized void handleArrival(int floor) {
		System.out.println("Elevator has arrived on Floor " + floor + "\nOpening doors now");
		floors.get(floor - 1).handleArrival();
	}
	
	public synchronized void handleDeparture(int floor) {
		System.out.println("Closing doors for elevator departure");
		floors.get(floor - 1).handleDeparture();
	}
	
	
	/**
	 * Handles the byte code used for tasks
	 * Also calls the appropriate 
	 * @param task the byte[] for the current task byte[0] = floor #, byte[1] = what is actually happening
	 * Currently for byte[1]
	 * 	0 = arrival
	 * 	1 = departure
	 * @return True of False depending if the task was valid
	 */
	public synchronized boolean handleTask(byte[] task) {
		if (task[0] == (byte) 0 && task[1] == (byte) 0) { // making sure the result is valid
			System.out.println("Invalid Task");
			return false;
		}
		int floor = (int) task[0];
//		System.out.println("floor number received from elevator: " + floor);
		switch (task[1]) {
		case (byte) 0: // elevator has arrived
			this.handleArrival(floor);
			break;
		case (byte) 1: // elevator is leaving 
			this.handleDeparture(floor);
			break;
		}
		return true;
		
	}
	
	/**
	 * This Method will convert it's parameters into a byte[] 
	 * Then it will send a datagram packet to the scheduler
	 * [0] = floor number
	 * [1] = direction
	 * [2] = destination
	 * [3] = fault type
	 * [4]-[6] = time
	 * 
	 * @param time the time the button was pressed
	 * @param floor the floor the button was pressed on
	 * @param direction the direction if the button (Up/Down)
	 * @param carButton the button pressed inside the elevator
	 */
	public synchronized void sendInstruction(String time, int floor, String direction, int carButton) {
		byte[] instruction = new byte[7];
		instruction[0] = (byte) floor;
		if (direction == "Up") {
			instruction[1] = (byte) 1; 
		} else {
			instruction[1] = (byte) 0; // defaulting to Down
		}
		instruction[2] = (byte) carButton;
		instruction[3] = (byte) 0; // Faults are not implemented this iteration;
		String[] timeArray = time.split(":"); // 3 numbers separated by 2 ':'
		for (int i = 0; i < timeArray.length; i++) {
			instruction[4 + i] = (byte) (Double.parseDouble(timeArray[i])); // the last # is a double
		}
		
		try { // creating the datagram packet to send to scheduler
			sendPacket = new DatagramPacket(instruction, instruction.length, InetAddress.getLocalHost(), 420);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		/*
		 * sending a packet and then waiting for a packet to confirm that the original packet was received
		 */
		try {
			instructionSocket.setSoTimeout(1000); // setting a timeout, if it fails to receive it will send again
		} catch (SocketException e) {
			e.printStackTrace();
		} 
		boolean done = false;
		while (!done) {
			try {
				instructionSocket.send(sendPacket); // sending the packet
			
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
			byte[] data = new byte[1]; // anything back is a good sign so just 1 byte is needed
			receivePacket = new DatagramPacket(data, data.length); // creating the receive packet
			
			try {
				instructionSocket.receive(receivePacket);
				done = true; // if a anything comes back then we know the message was received
			} catch (SocketTimeoutException e) {
				continue; // repeat the while if nothing is received after 1 second
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
