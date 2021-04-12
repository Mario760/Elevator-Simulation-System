package ElevatorSubsystem;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

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
	private Elevator elevator;
	
	/**
	 * 
	 * @param myElevator
	 * 
	 * Basic constructor for the Elevator panel. 
	 * 
	 */
	public ElevatorPanel(Elevator myElevator) {
		
		this.elevator = myElevator;
		
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
		
	}
	
	public void setElevatorNumber() {
		labelElevatorNumber.setText("Elevator #: " + elevator.getElevatorNum());
	}
	
	public void setElevatorFloor() {
		labelElevatorFloor.setText("Floor: " + elevator.getFloorNumber());
	}
	
	public void setElevatorMoving(MotorDirection direction) {
		labelElevatorMoving.setText("Elevator is currently: " + elevator.getMotor());
	}
	
	public void setElevatorFault(byte[] data) {
		labelFault.setText("Fault Code: " + data[1]); //TODO: insert fault bit here
	}
}
