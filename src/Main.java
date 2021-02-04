
public class Main {

	
	public static void main(String[] args) {
		
		String filePath = "./input.txxt";
		Thread elevator, floorSubsystem;
		Scheduler scheduler = new Scheduler();
		
		elevator = new Thread(new Elevator(scheduler, 1, 6),"Elevator");
		floor = new Thread(new FloorSubsystem(6, scheduler,filePath), "FloorSubsystem");
		
		elevator.start();
		floor.start();
		
		
	}

}