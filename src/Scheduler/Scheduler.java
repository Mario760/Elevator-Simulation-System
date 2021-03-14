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
public class Scheduler{
	
	private int elevatorData1[];
	private int elevatorData2[];
	private FloorTask floorTask = FloorTask.NOTHING;
	private List<Instruction> instructions;
	private boolean instructionEmpty = true;
	private int initializeElevator =1;
	private DatagramPacket send;
	private DatagramSocket FloorSocket, elevatorSocket;
	
	public Scheduler() {

		instructions = new LinkedList<>();
		try {
			this.FloorSocket = new DatagramSocket();
			elevatorSocket = new DatagramSocket();
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
		System.out.println("something");
		this.instructions.add(instruction);

		instructionEmpty = false;
		notifyAll();
		
	}

	public synchronized void sendInstructionToElevator(){

		while(instructionEmpty){
			try{
				System.out.println("wait() floor put instruction");
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
		System.out.println("Car 1 location:"+elevatorLoc1+" and car 2 location:"+elevatorLoc2+" floor: "+floor);

		//If car1 is closer
		if(Math.abs(floor-elevatorLoc1)<=Math.abs(floor-elevatorLoc2)){
			return elevatorData1[0];
		}
		return elevatorData2[0];
	}

	public void updateInfoAndSend(int info[]){

		if(info[0] == 1){
			elevatorData1 = info;
		}else{
			elevatorData2 = info;
		}

		System.out.println("Info array:"+info[0]+info[1]+info[2]);

		floorTask = FloorTask.ARRIVAL;
		System.out.println("updateInfo done");

		if(initializeElevator>2) {
			getNextFloorTask((byte)info[1]);
			if (info[2] == 1) {
				floorTask = FloorTask.DEPARTURE;
				getNextFloorTask((byte) info[1]);
			}
		}

		if(info[2]==0 && initializeElevator>=2){
			sendInstructionToElevator();
		}
		initializeElevator++;
	}

	/**
	 * This method determines the next floor task by using the floorTask enum
	 */
	public synchronized void getNextFloorTask(byte elevatorData) {

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
			System.out.println("elevatorData"+elevatorData);
			floorTask = FloorTask.NOTHING;
			byte[] task = {elevatorData, (byte) 0}; // 0 means arrival
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
			byte[] task = {elevatorData, (byte) 1}; // 1 means departure
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
	
	/*
	 * Method used only for testing purposes, to update elevatorData1 or elevatorData 2.
	 */
	public void setElevatorData(int[] elevatorData) {
		if(elevatorData[0] == 1) {
			this.elevatorData1 = elevatorData;
		} else {
			this.elevatorData2 = elevatorData;
		}
	}
	
	public static void main(String[] args) {
		Scheduler scheduler = new Scheduler();
		Thread schedulerFloorReceive = new Thread(new SchedulerFloorReceive(scheduler),"schedulerFloorReceive");
		Thread schedulerElevatorReceive = new Thread(new SchedulerElevatorReceive(scheduler),"schedulerElevatorReceive");
		schedulerElevatorReceive.start();
		schedulerFloorReceive.start();
	}
}
