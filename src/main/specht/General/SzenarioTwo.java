package specht.General;

import java.io.IOException;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class SzenarioTwo extends Thread {

    int GameID = 1;
    AtomicInteger Second = new AtomicInteger(0);
    boolean debug = true;
    DatagramSocket ds, dr;
    InetAddress ia, ib;

    SzenarioTwo() {
        try {
            ds = getDatagramSocketSending();
            dr = getDatagramSocketReceiving();
            ia = InetAddress.getByName(Attribute.sendingAddress);
            ib = InetAddress.getByName(Attribute.receivingAddress);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public DatagramSocket getDatagramSocketSending () throws SocketException {
        if (ds == null) {
            ds = new DatagramSocket(Attribute.sendingPort);
        }
        return ds;
    }

    public DatagramSocket getDatagramSocketReceiving () throws SocketException {
        if (dr == null) {
            dr = new DatagramSocket(Attribute.receivePort);
        }
        return dr;
    }

    public AtomicInteger getSecond() {
        return Second;
    }

    public void setSecond(AtomicInteger second) {
        Second = second;
    }
    public void run() {

        byte[] udpFrame = new byte[13];
        udpFrame = ConstructCANFrame.setLightSignalGreenOn();
        DatagramPacket packet = new DatagramPacket(udpFrame, udpFrame.length,ia,Attribute.sendingPort);
        try {
            ds.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask(){
            public void run(){
                if (Second.get() == 5 ) {

                    System.out.println("HELLO 5 SECONDS");
                    byte[] udpFrame = new byte[13];
                    //udpFrame = ConstructCANFrame.setLightSignalGreenOn();
                    udpFrame = ConstructCANFrame.setSwitchRWRedOff();
                    DatagramPacket packet = new DatagramPacket(udpFrame, udpFrame.length,ia,Attribute.sendingPort);
                    try {
                        ds.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    udpFrame = ConstructCANFrame.setSwitchRWGreenOn();
                    packet = new DatagramPacket(udpFrame, udpFrame.length,ia,Attribute.sendingPort);
                    try {
                        ds.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    udpFrame = ConstructCANFrame.setLightSignalRedOn();
                    packet = new DatagramPacket(udpFrame, udpFrame.length,ia,Attribute.sendingPort);
                    try {
                        ds.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                if (Second.get() >= 90 ) {
                    byte[] udpFrame = new byte[13];
                    //udpFrame = ConstructCANFrame.setLightSignalGreenOn();
                    udpFrame = ConstructCANFrame.setSpeed(Attribute._CARGO_ID,200);

                    DatagramPacket packet = new DatagramPacket(udpFrame, udpFrame.length,ia,Attribute.sendingPort);
                    try {
                        ds.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    udpFrame = ConstructCANFrame.setSwitchRWGreenOff();
                    packet = new DatagramPacket(udpFrame, udpFrame.length,ia,Attribute.sendingPort);
                    try {
                        ds.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    udpFrame = ConstructCANFrame.setSwitchRWRedOn();
                    packet = new DatagramPacket(udpFrame, udpFrame.length,ia,Attribute.sendingPort);
                    try {
                        ds.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                //elapsed time
                Second.incrementAndGet();
                if (debug) {
                    System.out.println("SECOND: " + getSecond());
                }

            }

        }, 0, 1000);
        try {
            t.schedule(new SzenarioTwoTimer(GameID, this.getSecond(), getDatagramSocketSending(),ia), 0, 1000);
        } catch (SocketException e) {
            e.printStackTrace();
        }


    }

}
