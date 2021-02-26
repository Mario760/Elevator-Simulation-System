/**
 * This is the Main class that acts as a client and will test and run the program.
 * @author Peyman Tajadod
 *
 */
public class Main {

	
	public static void main(String[] args) {
		
		String filePath = System.getProperty("user.dir") + "\\src\\input.txt";
		System.out.println(filePath);
		Thread elevator, floorSubsystem;
		Scheduler scheduler = new Scheduler();
		
		elevator = new Thread(new Elevator(scheduler, 1, 6),"Elevator");
		floorSubsystem = new Thread(new FloorSubsystem(6, scheduler, filePath), "FloorSubsystem");
		
		elevator.start();
		floorSubsystem.start();
		
		
	}

}