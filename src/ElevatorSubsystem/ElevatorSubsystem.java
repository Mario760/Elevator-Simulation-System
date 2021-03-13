package ElevatorSubsystem;

public class ElevatorSubsystem {
	
	public static void main(String[] args) {
		Thread elevator1 = new Thread(new Elevator(1, 8),"Elevator1");
		Thread elevator2 = new Thread(new Elevator(2,8), "Elevator2");
		
		elevator1.start();
		elevator2.start();
	}

}
