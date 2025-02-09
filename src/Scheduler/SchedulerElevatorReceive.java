package Scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * The type Scheduler elevator receive.
 *
 * @author Jiawei Ma
 */
public class SchedulerElevatorReceive implements Runnable{

    private DatagramPacket send, receive;
    private DatagramSocket elevatorSocket;
    private Scheduler scheduler;
    private long endTime;

    /**
     * Instantiates a new Scheduler elevator receive.
     * Using socket to receive from elevators
     *
     * @param scheduler the scheduler
     */
    public SchedulerElevatorReceive(Scheduler scheduler){
        this.scheduler = scheduler;
        try {
            elevatorSocket = new DatagramSocket(11);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        while (true){
            DatagramPacket elevatorReceivePacket = new DatagramPacket(new byte[3],3);
            try {
                elevatorSocket.receive(elevatorReceivePacket);
            }catch (IOException e) {
                e.printStackTrace();
            }
            byte[] tempInfo =  elevatorReceivePacket.getData();
            int[] info = new int[3];
            for(int i = 0; i<3; i++){
                info[i] = tempInfo[i];
            }
            scheduler.updateInfoAndSend(info);
        }
    }
}
