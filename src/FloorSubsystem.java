import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
* git good or git gone
*/

/**
 * This class handles the FloorSubsytem Thread
 * It implements runnable
 * @author Campbell de Winter
 *
 */
public class FloorSubsystem implements Runnable{
	private List<Floor> floors; // list of floors
	private Scheduler scheduler; // the scheduler containing the synchronized methods
	private HashMap<String, Object> info; // This HashMap contains the info to be passed to the scheduler
	private String filePath; // the file path of the input file
	
	/**
	 * The constructor for the FloorSubSystem
	 * @param numberOfFloors the number of floors to create
	 * @param scheduler the scheduler
	 * @param filePath the file path containing the input file
	 */
	public FloorSubsystem(int numberOfFloors, Scheduler scheduler, String filePath) {
		if (numberOfFloors <= 1) {
			System.out.print("No need for an elvator if the building has only 1 floor");
			System.exit(1);
			//throw SillyGooseException e;
		}
		this.scheduler = scheduler;
		this.filePath = filePath;
		this.info = new HashMap<String, Object>(); // String, Object for Key, Value
		this.floors = new ArrayList<Floor>();
		for (int i = 1; i <= numberOfFloors; i++) {
			if (i == 1) { // first floor
				floors.add(new Floor(i, 1));
			} else if (i == numberOfFloors) { // last floor
				floors.add(new Floor(i, 1));
			} else { // other floor
				floors.add(new Floor(i, 2));
			}
		}
	}
	
	/**
	 * This method presses one of the floors buttons (by calling pressButton() on the floor) based on the info hashmap
	 */
	public void PressButton(int floor, FloorDirection direction) {
		floors.get(floor - 1).pressButton(direction);
	}
	
	/**
	 * This method handles the arrival condition of the elevator at a certain floor
	 * It calls the floor's handleArrival() method, Sleeps for 9 seconds (9.175 was our average load/unload time) and then calls the floor's handleDeparture() method
	 * @param floor the floor that the elevator arrived at
	 */
	public void handleArrival(int floor) {
		floors.get(floor - 1).handleArrival();
	}
	
	public void handleDeparture(int floor) {
		floors.get(floor - 1).handleDeparture();
	}
	
	/**
	 * This method is called by the run method and is used to read the input file so that it can send relevant info to the scheduler
	 */
	public void readInputFile() {
		try {
			System.out.println("Just got the input file");
		    LineNumberReader line = new LineNumberReader(new FileReader(filePath)); // LineNumberReader allows for getting the line number. Might be useful in a future iteration
		    String lineText = null;
		    while ((lineText = line.readLine()) != null) { // It will read the next line every while iteration (until it reaches the end)
		        String[] instructions = lineText.split(" "); // splitting the line by whitespace due to the format of the input file
		        FloorDirection direction = FloorDirection.DOWN;
		        if (instructions[2] == "Up") {
		        	direction = FloorDirection.UP;
		        }
		        if (instructions[2] == "Down") {
		        	direction = FloorDirection.DOWN;
		        }
		        scheduler.receiveInstruction(new Instruction(instructions[0], Integer.parseInt(instructions[1]), direction, Integer.parseInt(instructions[3])));
		    }
		    line.close(); // closing the file
		} catch (IOException e) { // safe coding practices only
		    System.out.println(e);
		}
	}
	
	/**
	 * This is the method that was implemented from the runnable interface
	 * currently it only calls readInputFile
	 */
	@Override
	public void run() {
		readInputFile();
		while(!scheduler.getDone()) {
			byte[] task = scheduler.getNextTask(1);
			if (task[0] == (byte) 0 && task[1] == (byte) 0) { // making sure the result is valid
				continue; 
			}
			int floor = (int) task[0];
			switch (task[1]) {
			case (byte) 0: // elevator has arrived
				this.handleArrival(floor);
				break;
			case (byte) 1: // elevator is leaving 
				this.handleDeparture(floor);
				break;
			case (byte) 2: // up button press
				this.PressButton(floor, FloorDirection.UP);
				break;
			case (byte) 3: // down button press
				this.PressButton(floor, FloorDirection.DOWN);
				break;
			}
					
				
		}
		
		
	}
	
}
