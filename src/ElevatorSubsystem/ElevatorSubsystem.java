package ElevatorSubsystem;

public class ElevatorSubsystem {
	
	public static void main(String[] args) {
		Thread elevator1 = new Thread(new Elevator(1, 22),"Elevator1");
		Thread elevator2 = new Thread(new Elevator(2,22), "Elevator2");
		Thread elevator3 = new Thread(new Elevator(3,22), "Elevator3");
		Thread elevator4 = new Thread(new Elevator(4,22), "Elevator4");
		
		elevator1.start();
		elevator2.start();
		elevator3.start();
		elevator4.start();
	}

}
