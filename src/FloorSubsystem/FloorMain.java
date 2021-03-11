package FloorSubsystem;

import main.Elevator;
import main.Scheduler;

public class FloorMain {

	public static void main(String[] args) {

		String filePath = System.getProperty("user.dir") + "\\src\\input.txt";
		System.out.println(filePath);
		Thread receiver, parser;
		FloorSubsystem floorSubsystem = new FloorSubsystem(8);
		receiver = new Thread(new FloorReceiver(floorSubsystem),"receiver");
		parser = new Thread(new inputScheduler(filePath, floorSubsystem), "parser");
		receiver.start();
		parser.start();
		

	}

}
