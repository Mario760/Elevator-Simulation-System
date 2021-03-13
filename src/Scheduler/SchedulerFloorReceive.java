package Scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import FloorSubsystem.FloorDirection;
//test
public class SchedulerFloorReceive implements Runnable {
	
	private DatagramPacket send, receive;
	private DatagramSocket toFloor;
	private Scheduler scheduler;
	
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
			
			try {
				toFloor.receive(receive);
			}catch(IOException e) {
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
			
			parseByte(receiveByte);
			
		}
		
	}
	
	public void parseByte(byte[] input) {
		
		String time = (int) input[4] + ":" + (int) input[5] + ":" + input[6];
		int floor = input[0];
		int carButton = input[2];
		FloorDirection floorButton = FloorDirection.DOWN;
		
		if(input[1] == (byte) 1) {
			floorButton = FloorDirection.UP;
		}
		
		scheduler.receiveInstruction(new Instruction(time, floor, floorButton, carButton));
		
	}
	

}
