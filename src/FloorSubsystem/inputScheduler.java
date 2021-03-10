package FloorSubsystem;
import java.io.*;

/**
 * This class is a thread that will parse the input file and send instructions after a time delay (to better simulate the system)
 * @author Campbell de Winter
 *
 */
public class inputScheduler implements Runnable{
	private String filepath;
	private FloorSubsystem floorSubsystem;
	
	/**
	 * Constructor for the inputScheduler
	 * @param filepath the path of the file to be parsed
	 * @param floorSubsystem the object this thread will perform actions on
	 */
	public inputScheduler(String filepath, FloorSubsystem floorSubsystem) {
		this.filepath = filepath;
		this.floorSubsystem = floorSubsystem;
	}
	
	/**
	 * Converts the time string into seconds
	 * @param time a string representing the time at which the button was pressed
	 * @return the time in seconds as a double
	 */
	private double getTimeInSeconds(String time) {
		String[] times = time.split(":");
		if (times.length !=  4) {
			System.out.println("Invalid Time\n");
			return -1;
		}
		double hours = Double.parseDouble(times[0]);
		double mins = Double.parseDouble(times[1]);
		double seconds = Double.parseDouble(times[2]);
		return (hours * 3600) + (mins * 60) + seconds;
	}
	
	/**
	 * This method is called by the run method and is used to read the input file so that it can send relevant info to the scheduler
	 * It presses the required floor button (helper function) and receives the next floor instruction as well (which is dealt with by a helper function)
	 */
	private void readInputFile() {
		try {
			
		    LineNumberReader lineReader = new LineNumberReader(new FileReader(filepath)); // LineNumberReader allows for getting the line number. Might be useful in a future iteration
		    String line;
		    double prevTime = 0;
		    while ((line = lineReader.readLine()) != null) { // It will read the next line every while iteration (until it reaches the end)
		    	System.out.println("Parsing Line: " + line + " and sending the Instruction to Scheduler");
		        String[] instructions = line.split(" "); // splitting the line by whitespace due to the format of the input file
		        double currTime = getTimeInSeconds(instructions[0]);
		        if (prevTime != 0) {
		        	try {
						Thread.sleep((int)(( currTime - prevTime) * 1000)); // sleeping fro the difference
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		        prevTime = currTime;
		        floorSubsystem.sendInstruction(instructions[0], Integer.parseInt(instructions[1]), instructions[2], Integer.parseInt(instructions[3]));
		        
		    }
		    lineReader.close(); // closing the file
		} catch (IOException e) { // safe coding practices only
		    System.out.println(e);
		}
	}
	
	
	@Override
	public void run() {
		readInputFile();
	}

}
