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
 *
 * @author Peyman Tajadod & Alex Tasseron & Jiawei Ma
 */
//test
public class Scheduler{

	private int elevatorData1[];
	private int elevatorData2[];
	private int elevatorData3[];
	private int elevatorData4[];
	private FloorTask floorTask = FloorTask.NOTHING;
	private List<Instruction> instructions;
	private boolean instructionEmpty = true;
	private int initializeElevator =1;
	private DatagramPacket send;
	private DatagramSocket FloorSocket, elevatorSocket;
	private Instruction instruction;
	private boolean e1Available = true, e2Available = true, e3Available = true, e4Available = true;

	/**
	 * Instantiates a new Scheduler.
	 */
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
	 *
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

	/**
	 * Send instruction to elevator.
	 */
	public synchronized void sendInstructionToElevator(int fault){

		if(fault!=-1) {
			while (instructionEmpty) {
				try {
					System.out.println("wait() floor put instruction");
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			instruction = instructions.remove(0);
		}else{
			instruction.setFaultType(0);
		}
		int car = arrangeCar(instruction);
		DatagramPacket datagramPacket;
		//For floor direction, byte 2 is down, byte 1 is up.
		byte direction = 1;
		if(instruction.getFloorButton()==FloorDirection.DOWN){
			direction = (byte) 2;
		}
		if(instructions.isEmpty()){
			instructionEmpty = true;
		}

		byte info[] = {(byte)instruction.getCarButton(),direction,(byte)instruction.getFloor(),(byte)instruction.getFaultType()};
		try {
			if (car == elevatorData1[0]) {
				datagramPacket = new DatagramPacket(info, info.length, InetAddress.getLocalHost(),1111);
				elevatorSocket.send(datagramPacket);
			}else if (car == elevatorData2[0]){
				datagramPacket = new DatagramPacket(info, info.length,InetAddress.getLocalHost(),2222);
				elevatorSocket.send(datagramPacket);
			}else if (car == elevatorData3[0]){
				datagramPacket = new DatagramPacket(info, info.length,InetAddress.getLocalHost(),3333);
				elevatorSocket.send(datagramPacket);
			}else{
				datagramPacket = new DatagramPacket(info, info.length,InetAddress.getLocalHost(),4444);
				elevatorSocket.send(datagramPacket);
			}
		}catch(IOException e){
			e.printStackTrace();
		}

		notifyAll();
	}

	/**
	 * Arrange the nearest car to take passenger.
	 *
	 * @param instruction the instruction
	 * @return the elevator number
	 */
	public int arrangeCar(Instruction instruction){
		int floor = instruction.getFloor();
		//Current location of two elevators
		int elevatorLoc1 = elevatorData1[1];
		int elevatorLoc2 = elevatorData2[1];
		int elevatorLoc3 = elevatorData3[1];
		int elevatorLoc4 = elevatorData4[1];
		HashMap<Integer,Integer> locations = new HashMap<>();
		locations.put(elevatorData1[0],elevatorLoc1);
		locations.put(elevatorData2[0],elevatorLoc2);
		locations.put(elevatorData3[0],elevatorLoc3);
		locations.put(elevatorData4[0],elevatorLoc4);


		HashMap<Integer,Boolean> availability = new HashMap<>();
		availability.put(elevatorData1[0],e1Available);
		availability.put(elevatorData2[0],e2Available);
		availability.put(elevatorData3[0],e3Available);
		availability.put(elevatorData4[0],e4Available);

		int closestCar = -1;
		int minDistance=22;
		for(int i : availability.keySet()){
			if(!availability.get(i)) continue;
			int distance = Math.abs(floor-locations.get(i));
			if(distance<minDistance){
				closestCar = i;
				minDistance = distance;
			}
		}
		return closestCar;
	}

	/**
	 * Update elevator info and send instruction to elevator and floor.
	 *
	 * @param info the info
	 */
	public  void updateInfoAndSend(int info[]){
		if(info[0] == 1){
			elevatorData1 = info;
			if(info[1]==-1){
				e1Available = false;
				sendInstructionToElevator(-1);
				return;
			}
		}else if(info[0]==2){
			elevatorData2 = info;
			if(info[1]==-1){
				e2Available = false;
				sendInstructionToElevator(-1);
				return;
			}
		}else if(info[0]==3){
			elevatorData3 = info;
			if(info[1]==-1){
				e3Available = false;
				sendInstructionToElevator(-1);
				return;
			}
		}
		else if(info[0]==4){
			elevatorData4 = info;
			if(info[1]==-1){
				e4Available = false;
				sendInstructionToElevator(-1);
				return;
			}
		}

		floorTask = FloorTask.ARRIVAL;
		System.out.println("updateInfo done");

		if(initializeElevator>4) {
			getNextFloorTask((byte)info[1]);
			if (info[2] == 1) {
				floorTask = FloorTask.DEPARTURE;
				getNextFloorTask((byte) info[1]);
			}
			//If received packet from elevator about door fault issue.
			if(info[2] == -2){
				floorTask = FloorTask.DOORFAULT;
				getNextFloorTask((byte) info[1]);
			}
		}

		if(info[2]==0 && initializeElevator>=4){
			sendInstructionToElevator(0);
		}
		initializeElevator++;
	}

	/**
	 * This method determines the next floor task by using the floorTask enum
	 *
	 * @param elevatorData the elevator data
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


		} else if(floorTask == FloorTask.DOORFAULT){
			floorTask = FloorTask.NOTHING;
			byte[] task = {elevatorData,(byte) -1};//-1 means door fault
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


		} else{
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

	/**
	 * Sets elevator data.
	 *
	 * @param elevatorData the elevator data
	 */
	/*
	 * Method used only for testing purposes, to update elevatorData1 or elevatorData 2 ,3,4.
	 */
	public void setElevatorData(int[] elevatorData) {
		if(elevatorData[0] == 1) {
			this.elevatorData1 = elevatorData;
		} else if(elevatorData[0] == 2){
			this.elevatorData2 = elevatorData;
		}else if(elevatorData[0] == 3){
			this.elevatorData3 = elevatorData;
		}else if(elevatorData[0] == 4){
			this.elevatorData4 = elevatorData;
		}
	}

	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		Scheduler scheduler = new Scheduler();
		Thread schedulerFloorReceive = new Thread(new SchedulerFloorReceive(scheduler),"schedulerFloorReceive");
		Thread schedulerElevatorReceive = new Thread(new SchedulerElevatorReceive(scheduler),"schedulerElevatorReceive");
		schedulerElevatorReceive.start();
		schedulerFloorReceive.start();
	}
}
