package FloorSubsystem;

import ElevatorSubsystem.Elevator;
import Scheduler.Scheduler;

public class FloorMain {

	public static void main(String[] args) {

		String filePath = System.getProperty("user.dir") + "\\src\\input.txt";
		System.out.println(filePath);
		Thread receiver, parser;
		FloorSubsystem floorSubsystem = new FloorSubsystem(8);
		receiver = new Thread(new FloorReceiver(floorSubsystem),"receiver"); // creating receiver thread
		parser = new Thread(new InputScheduler(filePath, floorSubsystem), "parser"); // creating input parser thread
		receiver.start(); // starting receiver
		parser.start(); // starting parser
		

	}

}
