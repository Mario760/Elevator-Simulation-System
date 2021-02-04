import java.util.ArrayList;
import java.util.Vector;
/**
 * This class is the Elevator subsystem.
 * @author Peyman Tajadod
 *
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
		
	}
	/**
	 * This method is to get the number of the elevator
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
	 * This method is to
	 * @param btnNum
	 */
	public void pressButton(int btnNum) {
		for(ElevatorButton btn : this.buttons) {
			if(btn.getNumber() == btnNum) {
				btn.setEnabledLamp(true);
			}
		}
	}
	
	public void goToFloor(int num) {
		this.flooNumber = num;
		move(MotorDirection.STOPPED);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}
	}
	

	@Override
	public void run() {
		
		while(true) {
			
			System.out.println("Sending Elevator info to scheduler");
			scheduler.put(0, this.flooNumber);
			
			Vector<Integer> info = scheduler.get(0);
			
			int floorNum = info.get(1);
			goToFloor(floorNum);
			System.out.println("Elevator moved to the floor requesting");
			scheduler.put(0, this.flooNumber);
			int floorNum2 = scheduler.get(0);
			goToFloor(floorNum2);
			System.out.println("Elevator moved to the destination floor");
			scheduler.put(0,this.flooNumber);
			
		}
		
		
	}
	
		
}



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
	
	public void setEnabledLamp(boolean enable) {
		this.lamp = enable;
	}
	
	public boolean getEnabledLamp(){
		return this.lamp;
	}
	
}


enum MotorDirection{
	STOPPED,
	UP,
	DOWN
}
