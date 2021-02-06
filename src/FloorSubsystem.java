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
	 * It creates a list of floors, the info hashmap and sets its instance variables
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
	public void PressButton() {
		int floor = ((int) info.get("Floor")) - 1; // array list index starts at 0 but floors start at 1
		FloorDirection direction = (FloorDirection) info.get("Button");
		floors.get(floor).pressButton(direction);
	}
	
	/**
	 * This method handles the arrival condition of the elevator at a certain floor
	 * It calls the floor's handleArrival() method, Sleeps for 9 seconds (9.175 was our average load/unload time) and then calls the floor's handleDeparture() method
	 * @param floor the floor that the elevator arrived at
	 */
	public void handleArrival(int floor) {
		floors.get(floor - 1).handleArrival();
		try {
			System.out.println("Waiting for people to enter/leave");
			Thread.sleep(9175);
		} catch (InterruptedException e) {}
		floors.get(floor -1).handleDeparture();
	}
	
	/**
	 * This method is called by the run method and is used to read the input file so that it can send and receive relevant info to and from the scheduler
	 */
	public void readInputFile() {
		try {
			int floor;
			System.out.println("Just got the input file");
		    LineNumberReader line = new LineNumberReader(new FileReader(filePath)); // LineNumberReader allows for getting the line number. Might be useful in a future iteration
		    String lineText = null;
		    while ((lineText = line.readLine()) != null) { // It will read the next line every while iteration (until it reaches the end)
		        String[] instructions = lineText.split(" "); // splitting the line by whitespace due to the format of the input file
		        info.put("Time", instructions[0]); // adding the time string to the hashmap (will be useful in later iterations)
		        info.put("Floor", Integer.parseInt(instructions[1])); // adding the floor where the button was pushed into the hashmap
		        if (instructions[2] == "Up") { // getting the direction and making it a FloorDirection object
		        	info.put("Button", FloorDirection.UP);
		        } else {
		        	info.put("Button", FloorDirection.DOWN);
		        }
		        info.put("New Floor", Integer.parseInt(instructions[3])); // adding the new floor to the hashmap
		        PressButton(); // pressing the button before telling the scheduler
		        scheduler.put(1 ,Integer.parseInt(instructions[1])); // telling the scheduler that an elevator needs to arrive at the floor where the button was pressed
		        floor = scheduler.get(1); // waiting for the arrival of the elevator at a certain floor
		        handleArrival(floor); // handling the arrival of the elevator (the person who pressed the button would enter)
		        scheduler.put(1, Integer.parseInt(instructions[3])); // telling the elevator to go to the new floor
		        floor = scheduler.get(1); // waiting for the elevator to arrive at the new floor
		        handleArrival(floor); // handling the arrival of the elevator at the new floor (people would be exiting and entering)
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
	}
	
}
