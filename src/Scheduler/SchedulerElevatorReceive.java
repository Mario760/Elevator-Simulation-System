package Scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class SchedulerElevatorReceive implements Runnable{

    private DatagramPacket send, receive;
    private DatagramSocket elevatorSocket;
    private Scheduler scheduler;

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
            }catch(IOException e) {
                e.printStackTrace();
            }
            byte[] tempInfo =  elevatorReceivePacket.getData();
            int[] info = new int[3];
            for(int i = 0; i<3; i++){
                info[i] = tempInfo[i];
                System.out.println(info[i]);
            }
            scheduler.updateInfoAndSend(info);
        }
    }
}
