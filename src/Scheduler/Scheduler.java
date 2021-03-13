package Scheduler;
import FloorSubsystem.FloorButton;
import FloorSubsystem.FloorDirection;

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
public class Scheduler implements Runnable{
	
	private int elevatorData1[];
	private int elevatorData2[];
	private FloorTask floorTask = FloorTask.NOTHING;
	private List<Instruction> instructions;
	private boolean instructionEmpty = true;
	private DatagramPacket send;
	private DatagramSocket FloorSocket, elevatorSocket;
	
	public Scheduler() {

		instructions = new LinkedList<>();
		try {
			this.FloorSocket = new DatagramSocket();
			elevatorSocket = new DatagramSocket(11);
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
		this.instructions.add(instruction);

		instructionEmpty = false;
		notifyAll();
		
	}

	public synchronized void sendInstructionToElevator(){

		while(instructionEmpty){
			try{
				System.out.println("wait() elevator put instruction");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		Instruction instruction = instructions.remove(0);
		int car = arrangeCar(instruction);
		DatagramPacket datagramPacket;
		//For floor direction, byte 0 is down, byte 1 is up.
		byte direction = 0;
		if(instruction.getFloorButton()==FloorDirection.DOWN){
			direction = (byte) 1;
		}
		if(instructions.isEmpty()){
			instructionEmpty = true;
		}

		byte info[] = {(byte)instruction.getCarButton(),direction,(byte)instruction.getFloor()};
		try {
			if (car == elevatorData1[0]) {
				datagramPacket = new DatagramPacket(info, info.length, InetAddress.getLocalHost(),1111);
				elevatorSocket.send(datagramPacket);
			}else{
				datagramPacket = new DatagramPacket(info, info.length,InetAddress.getLocalHost(),2222);
				elevatorSocket.send(datagramPacket);
			}
		}catch(IOException e){
			e.printStackTrace();
		}

		notifyAll();
	}

	public int arrangeCar(Instruction instruction){
		int floor = instruction.getFloor();
		//Current location of two elevators
		int elevatorLoc1 = elevatorData1[1];
		int elevatorLoc2 = elevatorData2[1];

		//If car1 is closer
		if(Math.abs(floor-elevatorLoc1)>=Math.abs(floor-elevatorLoc2)){
			return elevatorData1[0];
		}
		return elevatorData2[0];
	}

	public void updateElevatorInfo(){
		byte[] byteArray = new byte[3];
		DatagramPacket elevatorReceivePacket = new DatagramPacket(byteArray,byteArray.length);
		try {
			elevatorSocket.receive(elevatorReceivePacket);
		}catch(IOException e) {
			e.printStackTrace();
		}
		byte[] tempInfo =  elevatorReceivePacket.getData();
		int[] info = new int[3];
		for(int i = 0; i<3; i++){
			info[i] = tempInfo[i];
		}
		if(info[0] == 1){
			elevatorData1 = info;
		}else{
			elevatorData2 = info;
		}

		if(info[2]==0){
			sendInstructionToElevator();
			floorTask = FloorTask.DEPARTURE;
		}else{
			floorTask = FloorTask.ARRIVAL;
		}

		getNextFloorTask();
	}

	
	/**
	 * This method determines the next floor task by using the floorTask enum
	 */
	public synchronized void getNextFloorTask() {

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
			byte[] task = {(byte) elevatorData1[0], (byte) 0}; // 0 means arrival
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
			byte[] task = {(byte) elevatorData1[0], (byte) 1}; // 1 means departure
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
		elevatorData1 = new int[3];
		elevatorData2 = new int[3];
	}

	@Override
	public void run() {

	}
}
