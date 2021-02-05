import java.util.Vector;

/**
 * 
 * @author Alexander Tasseron
 *
 */

public class Scheduler {
	
	private Vector<Integer> contents = new Vector<Integer>(5); //0th -> floor number, 1st -> instructions... 
	private boolean empty = true;
	
	/**
	 * This method will receive a floor from the floorSubsystem (it will wait until the elevator has verified that it is not moving)
	 * It will be used to tell the elevator where to go next
	 * @param id identifies what type of object is calling get (0 for elevator, 1 for floor)
	 * @param param information being stored in the vector, location dependent on id
	 */
	
	public synchronized void put(int id, int param) {//id 0 -> elevator sub system, 1 -> floor sub system
		
		while(!empty) {
			
			try {
				
				wait();
				
				System.out.println("put -> contents: ");
				
				for(int i : this.contents) {
					
					System.out.println(i);
					
				}
				
			} catch(InterruptedException e) {
				
				
				return;
				
			}
			
			if(id == 0) { //if elevator, put floor number to 0th indice
				
				this.contents.set(0,param);
				System.out.println("put -> Contents(0) = " + this.contents.get(0));
				
			} else { //else, instructions to 1st indice
				
				this.contents.set(1, param);
				System.out.println("put ->Contents(1) = " + this.contents.get(1));
			}
			
			this.empty = false;
			notifyAll();
		}
	}
	
	/**
	 * This method will be called by the FloorSubsystem to wait for the elevator to stop at the floor requested by the give floor method
	 * @return the floor that the elevator stopped at
	 * @param id identifies what type of object is calling get (0 for elevator, 1 for floor)
	 */
	public synchronized Vector<Integer> get(int id) {
		
		Vector<Integer> temp = new Vector<Integer>(5);
		
		while(empty) {
			
			try {
				
				wait();
				
				System.out.println("get -> contents: ");
				
				for(int i : this.contents) {
					
					System.out.println(i);
					
				}
				
			} catch (InterruptedException e) {
				
				
			}
			
		}
		
		if(id == 0) { //if 0, return whatever is stored at 1st indice
			
			temp.set(1, this.contents.get(1));
			System.out.println("get -> Contents(1) = " + this.contents.get(1));
			System.out.println("get -> temp(1) = " + temp.get(1));
			
		} else { //else, return whatever is stored at 0th indice
			
			temp.set(0, this.contents.get(0));
			System.out.println("get -> Contents(0) = " + this.contents.get(1));
			System.out.println("get -> temp(1) = " + temp.get(0));
			
		}
		
		this.contents.clear();
		this.empty = true;
		notifyAll();
		return temp;
		
		
		
	}
}
