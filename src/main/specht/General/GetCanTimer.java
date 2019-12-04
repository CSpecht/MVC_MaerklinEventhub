package specht.General;

import specht.Ressources.PatternListener;
import specht.Ressources.Resource;

import java.io.IOException;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class GetCanTimer extends TimerTask {

    AtomicInteger Second = new AtomicInteger(0);
    DatagramSocket ds;
    InetAddress ia;
    Resource rs;
    AtomicInteger waterA, coilA, sandA;
    PatternListener pl;
    Timer t;
    Attribute attribute = null;
    GetCanTimer (Timer t, DatagramSocket s, InetAddress ia, Resource rs, AtomicInteger waterA, AtomicInteger coilA, AtomicInteger sandA, PatternListener pl ){
        if (attribute == null) {
            attribute = new Attribute();
        }
        try{
            this.ds = s;
            this.ia = InetAddress.getByName(attribute.getSendingAddress());
            this.rs = rs;
            this.waterA = waterA;
            this.coilA = coilA;
            this.sandA = sandA;
            this.pl = pl;
            this.t = t;


        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public AtomicInteger getSecond() { return Second; }

    public void setSecond(AtomicInteger second) {
        Second = second;
    }

    @Override
    public void run() {

        System.out.println("Second == " + Second.getAndIncrement());

        if (Second.get() <=10) {
            byte[] udpFrame = ConstructCANFrame.setSpeed(Attribute._STEAM_ID, 400);
            DatagramPacket sendPacket = new DatagramPacket(udpFrame, udpFrame.length,ia,attribute.getSendingPort());
            try{
                ds.send(sendPacket);
            }  catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else {
            byte[] udpFrame = ConstructCANFrame.setSpeed(Attribute._STEAM_ID, 0);
            DatagramPacket sendPacket = new DatagramPacket(udpFrame, udpFrame.length,ia,attribute.getSendingPort());
            try{
                ds.send(sendPacket);
            }  catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (Second.get() >= 15 && Second.get() < 25) {

                try {
                    rs.setWater();
                    rs.sleep(1000);
                    waterA = rs.getResourceAmountWater();

                    rs.setCoil();
                    rs.sleep(1000);
                    coilA = rs.getResourceAmountCoil();

                    rs.setSand();
                    rs.sleep(1000);
                    sandA = rs.getResourceAmountSand();

                    pl.setRoundCount(0);
                    rs.sleep(1000);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }

        if (Second.get() == 25) {
            byte[] udpFrame = ConstructCANFrame.go();
            DatagramPacket sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia,attribute.getSendingPort());
            try {
                ds.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            udpFrame = ConstructCANFrame.setSpeed(Attribute._STEAM_ID, 600);
            sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, attribute.getSendingPort());
            try {
                ds.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (Second.get() == 26) {
            //setSecond(new AtomicInteger(0));
            //System.out.println("cancelTimer" + this);
            this.cancel();
            this.t = null;




            //System.out.println("cancelTimer" + this);
        }

       // System.out.println("The purge of the task is "+t.purge());
    }

    public Timer rTimer () {
        return this.t;
    }


}

