package FloorSubsystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * This thread will be used to receive udp packets from the scheduler
 * It will then perform the requires task on the FloorSubsystem
 * @author Campbell de Winter
 *
 */
public class FloorReceiver implements Runnable {
	private FloorSubsystem floorSubsystem;
	private DatagramPacket receivePacket; // a packet to receive info from the scheduler
	private DatagramSocket receiveSocket; // this packet will only receive from the scheduler
	private byte[] data; // the data sent from the scheduler
	
	/**
	 * The constructor for the FloorReceiver thread
	 * @param floorSubsystem the object the thread will be using
	 */
	public FloorReceiver(FloorSubsystem floorSubsystem) {
		this.floorSubsystem = floorSubsystem;
		data = new byte[2];
		try {
			this.receiveSocket = new DatagramSocket(420); // ideally this receives at blazing speeds
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
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
	private boolean handleTask(byte[] task) {
		if (task[0] == (byte) 0 || task[1] > (byte) 1) { // making sure the result is valid
			System.out.println("Invalid Task");
			return false;
		}
		int floor = (int) task[0];
		switch (task[1]) {
		case (byte) -1:
			System.out.println("Door fault occured attempting to reclose the door");
			floorSubsystem.handleDeparture(floor); // door didnt close so try again
		case (byte) 0: // elevator has arrived
			floorSubsystem.handleArrival(floor);
			break;
		case (byte) 1: // elevator is leaving 
			floorSubsystem.handleDeparture(floor);
			break;
		}
		return true;
		
	}
	
	public void run() {
		boolean valid;
		do {
			receivePacket = new DatagramPacket(data, data.length); // creating the receive packet
			
			try {
				receiveSocket.receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
		  valid = handleTask(data);
			
		} while(valid); // verifying that the received data is valid and preforming the required action
			
		
		
	}
	
}
