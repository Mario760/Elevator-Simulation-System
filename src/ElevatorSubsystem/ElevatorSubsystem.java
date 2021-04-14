package ElevatorSubsystem;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Scheduler.MotorDirection;

public class ElevatorSubsystem {
	
	public static void main(String[] args) {
		ElevatorPanel panel1 = new ElevatorPanel(1, 1, MotorDirection.STOPPED);
		ElevatorPanel panel2 = new ElevatorPanel(2, 1, MotorDirection.STOPPED);
		ElevatorPanel panel3 = new ElevatorPanel(3, 1, MotorDirection.STOPPED);
		ElevatorPanel panel4 = new ElevatorPanel(4, 1, MotorDirection.STOPPED);
		Thread elevator1 = new Thread(new Elevator(1, 22, panel1),"Elevator1");
		Thread elevator2 = new Thread(new Elevator(2, 22, panel2), "Elevator2");
		Thread elevator3 = new Thread(new Elevator(3, 22, panel3), "Elevator3");
		Thread elevator4 = new Thread(new Elevator(4, 22, panel4), "Elevator4");
		
		elevator1.start();
		elevator2.start();
		elevator3.start();
		elevator4.start();
		
		JFrame frame = new JFrame("Elevator Info");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel pane = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(panel1, c);
		c.gridx = 1;
		c.gridy = 0;
		pane.add(panel2, c);
		c.gridx = 0;
		c.gridy = 1;
		pane.add(panel3, c);
		c.gridx = 1;
		c.gridy = 1;
		pane.add(panel4, c);
		
		frame.add(pane, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}

}
