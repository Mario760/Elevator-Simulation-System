import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.List;

/*
* git good or git gone
*/
public class FloorSubsystem implements Runnable{
	private int floorNumber;
	private FloorButton[] buttons;
	private FloorButton[] lamps;
	
	public FloorSubsystem(int floorNumber, int topFloor) { // a top floor variable is needed to know if the sub system is on the top floor
		this.floorNumber = floorNumber;
		
		if (floorNumber == 1) {
			buttons = new FloorButton[] {FloorButton.UP};
		} else if (floorNumber == topFloor) { 
			buttons = new FloorButton[] {FloorButton.DOWN};
		} else {
			buttons = new FloorButton[] {FloorButton.UP, FloorButton.DOWN};
		}
	}
	
	private String[] readInputFile(String filePath) {
		String[] instructions = null;
		
		try {
		    LineNumberReader line = new LineNumberReader(new FileReader(filePath));
		    String lineText = null;
		    while ((lineText = line.readLine()) != null) {
		        instructions = lineText.split(" ");
		        if (Integer.parseInt(instructions[1]) == floorNumber) {
		        	
		        }
		    }
		    line.close();
		} catch (IOException e) {
		    System.out.println(e);
		}
		
		return instructions;
	}
	
	@Override
	public void run() {
		while (true) {
			
		}
		
	}
	
}
