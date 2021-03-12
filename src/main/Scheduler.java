package main;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

/**
 * This is the scheduler class that follows the mutual exclusion and condition synchronization
 * @author Peyman Tajadod & Alex Tasseron
 *
 */
//test
public class Scheduler {
	
	private int elevatorData[];
	private boolean elevatorEmpty = true;
	private FloorTask floorTask = FloorTask.NOTHING;
	private Instruction instruction;
	private boolean instructionEmpty = true;
	private boolean firstTask = false;
	private DatagramPacket send, receive;
	private DatagramSocket FloorSocket, elevatorSocket;
	
	public Scheduler() {
		
		try {
			this.FloorSocket = new DatagramSocket(); 
			//other datagram socket uses port 2 
		}catch(SocketException se) {
			se.printStackTrace();
			
		}
		
		
	}
	
	
	/**
	 * This method will be called by the FloorSubsystem to populate the instructions list
	 * @param instruction an Instruction
	 */
	public synchronized void receiveInstruction(Instruction instruction) {
		while(!instructionEmpty) {
			try {
				wait();
				System.out.println("wait() instruction put ");
			} catch (InterruptedException e) {
				return;
			}
		}
		this.instruction = instruction;
		instructionEmpty = false;
		notifyAll();
		
	}
	
	
	public synchronized byte[] getNextElevatorTask() {

		while (instructionEmpty) {
			try {
				System.out.println("wait() elevator get instruction ");
				wait();
			} catch (InterruptedException e) {
				return null;
			}
		}
		byte elevatorTask[] = new byte[2];
		getElevatorData();

		if (!firstTask) {
			if (this.elevatorData[1] == 0) {// if stopped

				if (this.elevatorData[0] < instruction.getFloor()) {
					elevatorTask[0] = (byte) this.instruction.getFloor();
					elevatorTask[1] = (byte) 1; // byte 1 is direction Up
					firstTask = true;
					return elevatorTask;
				}
				if (this.elevatorData[0] > instruction.getFloor()) {
					elevatorTask[0] = (byte) this.instruction.getFloor();
					elevatorTask[1] = (byte) 2;// byte 2 is direction Down
					firstTask = true;
					return elevatorTask;
				}

			} else { // moving
				// to be implemented next iteration
				firstTask = true;
				return elevatorTask;
			}

		} else {
			if (this.elevatorData[1] == 0) {// if stopped
				instructionEmpty = true;
				if (this.elevatorData[0] < instruction.getCarButton()) {
					elevatorTask[0] = (byte) this.instruction.getCarButton();
					elevatorTask[1] = (byte) 1; // byte 1 is direction Up
					firstTask = false;
					return elevatorTask;
				}
				if (this.elevatorData[0] > instruction.getCarButton()) {
					elevatorTask[0] = (byte) this.instruction.getCarButton();
					elevatorTask[1] = (byte) 2;// byte 2 is direction Down
					firstTask = false;
					return elevatorTask;
				}

			} else { // moving
				// to be implemented next iteration
				firstTask = false;
				return elevatorTask;
			}
			

		}
		return elevatorTask;

	}		
	
	
	public synchronized void putElevatorData(int[] data) {
		while (!elevatorEmpty) {
			try {
				System.out.println("wait() elevator put data ");
				wait();
				
			} catch (InterruptedException e) {
				return;
			}

		}
		this.elevatorData = data;
//		System.out.println("elevator put data");
		this.elevatorEmpty = false;
		notifyAll();
		
	}
	
	public synchronized int[] getElevatorData() {
		while (elevatorEmpty) {
			try {
				wait();
				System.out.println("wait() elevator get data ");
			} catch (InterruptedException e) {
				return null;
			}

		}
		int[] eDataTemp = this.elevatorData;
		elevatorEmpty = true;
		notifyAll();
//		System.out.println("elevator get data ");
		return eDataTemp;

	}

	
	public synchronized void reachedDepartureFloor(FloorTask state) {
		while (floorTask != FloorTask.NOTHING) {
			try {
				wait();
				System.out.println("wait() elevator reach/departure floor ");
			} catch (InterruptedException e) {
				return;
			}
		}
		
		floorTask = state;
		notifyAll();
	}

	
	/**
	 * This method determines the next floor task by using the floorTask enum
	 * @return a byte[] containing the next instruction for the floor
	 */
	public synchronized void sendNextFloorTask() {
		
		while(floorTask == FloorTask.NOTHING) {
			try {
				wait();
			} catch (InterruptedException e) {
				byte[] failure = {(byte) 0, (byte) 0};
				
				try {
					send = new DatagramPacket(failure, failure.length, InetAddress.getLocalHost(), 420);
				}catch(UnknownHostException ee) {
					ee.printStackTrace();
					
				}	
			
				try {
					FloorSocket.send(send);
				} catch(IOException eee) {
					eee.printStackTrace();
				}
				
			}
				
		}
		
		if (floorTask == FloorTask.ARRIVAL) {
			floorTask = FloorTask.NOTHING;
			byte[] task = {(byte) elevatorData[0], (byte) 0}; // 0 means arrival
			try {
				send = new DatagramPacket(task, task.length, InetAddress.getLocalHost(), 420);
			}catch(UnknownHostException ee) {
				ee.printStackTrace();
				
			}	
		
			try {
				FloorSocket.send(send);
			} catch(IOException eee) {
				eee.printStackTrace();
			}
			notifyAll();
			
		} else {
			floorTask = FloorTask.NOTHING;
			byte[] task = {(byte) elevatorData[0], (byte) 1}; // 1 means departure
			try {
				send = new DatagramPacket(task, task.length, InetAddress.getLocalHost(), 420);
			}catch(UnknownHostException ee) {
				ee.printStackTrace();
				
			}	
		
			try {
				FloorSocket.send(send);
			} catch(IOException eee) {
				eee.printStackTrace();
			}
			notifyAll();
			
		}
		
	}
	
	/**
	 * This method is purely for testing purposes, to reset the floorTask value, so 
	 * a Thread isn't needed to be used in the tests
	 */
	public void resetFloorTaskAndElevatorEmpty() {
		this.floorTask = FloorTask.NOTHING;
		this.elevatorEmpty = true;
	}
	
}
