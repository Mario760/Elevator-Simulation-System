package ElevatorSubsystem;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Scheduler.MotorDirection;

public class ElevatorPanel extends JPanel{
	
	private JLabel labelElevatorNumber = new JLabel("Elevator #: ");
	private JLabel labelElevatorFloor = new JLabel("Floor: ");
	private JLabel labelElevatorMoving = new JLabel("Elevator is currently: ");
	private JLabel labelFault = new JLabel("Fault Code: ");
	private JPanel newPanel = new JPanel(new GridBagLayout());
	
	/**
	 * 
	 * @param myElevator
	 * 
	 * Basic constructor for the Elevator panel. 
	 * 
	 */
	public ElevatorPanel(int elevatorNumber, int floorNumber, MotorDirection direction) {
		labelElevatorNumber.setText("Elevator #: " + elevatorNumber);
		labelElevatorFloor.setText("Floor: " + floorNumber);
		labelElevatorMoving.setText("Elevator is currently: " + direction);
		labelFault.setText("Fault Code: " + "Everything is fine"); //TODO: insert fault bit here
	
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.CENTER;
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		this.newPanel.add(labelElevatorNumber, constraints);
		
		
		constraints.gridy = 1;
		this.newPanel.add(labelElevatorFloor, constraints);
		
		constraints.gridy = 2;
		this.newPanel.add(labelElevatorMoving, constraints);
		
		constraints.gridy = 3;
		this.newPanel.add(labelFault,constraints);
		
		this.newPanel.setBorder(BorderFactory.createEtchedBorder());
		this.add(newPanel); // adding everything to this object!!!
		
		
	}
	
	public void setElevatorNumber(int elevatorNumber) {
		labelElevatorNumber.setText("Elevator #: " + elevatorNumber);
	}
	
	public void setElevatorFloor(int floorNumber) {
		labelElevatorFloor.setText("Floor: " + floorNumber);
	}
	
	public void setElevatorMoving(MotorDirection direction) {
		labelElevatorMoving.setText("Elevator is currently: " + direction);
	}
	
	public void setElevatorFault(String fault) {
		labelFault.setText("Fault Code: " + fault); //TODO: insert fault bit here
	}
}
