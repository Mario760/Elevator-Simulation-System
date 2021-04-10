package Scheduler;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.TimeoutException;

import FloorSubsystem.FloorDirection;

public class SchedulerFloorReceive implements Runnable {
	
	private DatagramPacket send, receive;
	private DatagramSocket toFloor;
	private Scheduler scheduler;
	private boolean firstReceive = true;
	
	public SchedulerFloorReceive(Scheduler scheduler) {
		
		this.scheduler = scheduler;
		
		try {
			this.toFloor = new DatagramSocket(49152);
		}catch(SocketException se) {
			se.printStackTrace();
		}

	}
	
	public void run() {
		
		while(true) {
			
			byte recByte[] = new byte[7];
			receive = new DatagramPacket(recByte, recByte.length);
			System.out.println("receiving floor instruction");
			try {
				toFloor.receive(receive);
			} catch (IOException e) {
				e.printStackTrace();
			}

			byte receiveByte[] = receive.getData();
			
			byte checkByte[] = {(byte) 1};
			
			try {
				send = new DatagramPacket(checkByte, checkByte.length, InetAddress.getLocalHost(), 2);
			}catch(UnknownHostException ee) {
				ee.printStackTrace();
				
			}	
		
			try {
				toFloor.send(send);
			} catch(IOException eee) {
				eee.printStackTrace();
				
			}

			if(firstReceive){
				scheduler.setStartTime(System.nanoTime());
				firstReceive=false;
			}
			parseByte(receiveByte);
			
		}
		
	}
	
	public void parseByte(byte[] input) {
		
		String time = (int) input[4] + ":" + (int) input[5] + ":" + input[6];
		int floor = input[0];
		int carButton = input[2];
		int faultType = input[3];
		FloorDirection floorButton = FloorDirection.DOWN;
		
		if(input[1] == (byte) 1) {
			floorButton = FloorDirection.UP;
		}

		if(faultType==-2){
			scheduler.setFloorReceiveDone(true);
		}else{
			scheduler.receiveInstruction(new Instruction(time, floor, floorButton, carButton, faultType));
		}
	}
	

}
