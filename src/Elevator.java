import java.util.ArrayList;

/**
 * This class is the Elevator subsystem.
 *
 * @author Peyman Tajadod
 */
public class Elevator implements Runnable {
	
	private Scheduler scheduler;
	private int elevatorNum;
	private int flooNumber; // the floor that the elevator is on
	private ArrayList<ElevatorButton> buttons; //number of buttons/floors in the system
	private MotorDirection motor;
	private boolean door;
	
	public Elevator(Scheduler scheduler, int elevatorNum, int buttonsNum) {
		this.elevatorNum = elevatorNum;
		this.flooNumber = 1;
		this.buttons = new ArrayList<> ();
		for(int i=1; i <= buttonsNum; i++ ) {
			buttons.add(new ElevatorButton(i, false));			
		}
		this.motor = MotorDirection.STOPPED;
		this.door = false;
		this.scheduler = scheduler;
		
	}
	/**
	 * This method is to get the elevator number
	 * @return
	 */
	public int getElevatorNum() {
		return this.elevatorNum;
	}
	
	/**
	 * This method is to change the direction of the elevator or make it stop
	 * @param dir
	 */
	public void move(MotorDirection dir) {
		this.motor = dir;
	}
	
	/**
	 * This method is to toggle the enabled button of the elevator
	 * @param btnNum
	 */
	public void toggleButton(int btnNum) {
		for(ElevatorButton btn : this.buttons) {
			if(btn.getNumber() == btnNum) {
				if(!btn.getEnabledLamp()) {
					btn.toggleLamp(true);
				}else {
					btn.toggleLamp(false);
				}
			}
		}
	}

	/**
	 * This method is to change the floor number where the elevator is at.
	 * @param num
	 */
	
	public void goToFloor(int num) {
		this.flooNumber = num;
		move(MotorDirection.STOPPED);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}
	}

	/**
	 * This method is to get current floor number.
	 * @return flooNumber, current floor number.
	 */
	public int getFlooNumber() {
		return flooNumber;
	}


	/**
	 * This method is to get motor direction
	 * @return the motor direction
	 */
	public MotorDirection getMotor() {
		return motor;
	}

	/**
	 * This is the implementation of the run method from interface Runnable.
	 * This implementation is only for the purpose of Iteration 1 and it will be modified
	 *
	 */
	@Override
	public void run() {
		
		while(true) {
			
			System.out.println("Sending Elevator info to scheduler");
			scheduler.put(0, this.flooNumber);
			int floorNum = scheduler.get(0);
			toggleButton(floorNum);
			goToFloor(floorNum);
			System.out.println("Elevator moved to the floor requesting");
			scheduler.put(0, this.flooNumber);
			int floorNum2 = scheduler.get(0);
			toggleButton(floorNum2);
			goToFloor(floorNum2);
			System.out.println("Elevator moved to the destination floor");
			scheduler.put(0,this.flooNumber);
			
		}
		
		
	}
	
		
}


/**
 * This class is to represent the buttons of the elevator
 * @author Peyman Tajadod
 *
 */
class ElevatorButton{
	private int number;
	private boolean lamp;

	public ElevatorButton(int number, boolean enabledLamp) {
		this.number = number;
		this.lamp = enabledLamp;
	}
	
	public int getNumber() {
		return this.number;
	}
	
	public void toggleLamp(boolean toggle) {
		this.lamp = toggle;
	}
	
	public boolean getEnabledLamp(){
		return this.lamp;
	}
	
}

/**
 * Enum class to represent the state of the motor
 * @author Peyman Tajadod
 *
 */
enum MotorDirection{
	STOPPED,
	UP,
	DOWN
}
