
public class Main {

	
	public static void main(String[] args) {
		
		String filePath = "./input.txt";
		Thread elevator, floorSubsystem;
		Scheduler scheduler = new Scheduler();
		
		elevator = new Thread(new Elevator(scheduler, 1, 6),"Elevator");
		floorSubsystem = new Thread(new FloorSubsystem(6, scheduler,filePath), "FloorSubsystem");
		
		elevator.start();
		floorSubsystem.start();
		
		
	}

}