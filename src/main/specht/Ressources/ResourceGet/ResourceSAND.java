package specht.Ressources.ResourceGet;

import specht.Ressources.Resource;

import specht.General.Attribute;
import specht.General.ConstructCANFrame;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ResourceSAND extends Resource {
    boolean stop;
    /*public ResourceSAND(String ip, int port, boolean stop) {
        super(ip, port, stop);
    }

    */

    public void getSand () throws IOException {

        boolean result = false;
        byte[] udpFrame = new byte[13];
        byte[] packatData;
        DatagramSocket ds = new DatagramSocket(Attribute.sendingPort);
        DatagramSocket dsReceive = new DatagramSocket(Attribute.receivePort);
        InetAddress ia = InetAddress.getByName(Attribute.sendingAddress);
        InetAddress ib = InetAddress.getByName(Attribute.receivingAddress);

        //GET WATER
        udpFrame = ConstructCANFrame.getSand(Attribute._STEAM_ID);

        System.out.println("GETSAND():");
        for (int i = 0; i < udpFrame.length; i++) {
            System.out.println("udpFrame[" + i + "]: " + udpFrame[i]);
        }

        //int i = 0;

        //System.out.println("I: " + i);
        DatagramPacket sendPacket = new DatagramPacket( udpFrame, udpFrame.length, ia, Attribute.sendingPort);
        //System.out.println("1");
        ds.send( sendPacket );

        //System.out.println("2");
        // Auf Anfrage warten
        sendPacket = new DatagramPacket( new byte[13], 13, ib, Attribute.receivePort );
        dsReceive.receive( sendPacket );
        //System.out.println("3");
        //comment

        // Empf�nger auslesen
        InetAddress address = sendPacket.getAddress();
        //System.out.println("4");
        int         port2    = sendPacket.getPort();
        int         len     = sendPacket.getLength();
        byte[]      data    = sendPacket.getData();

        System.out.println("DATA Received:");
        for (int j = 0; j<data.length; j++) {
            System.out.println("data[" + j + "]: " + data[j]);
        }
    }

    public void setSand() throws IOException {
        boolean result = false;
        byte[] udpFrame = new byte[13];
        byte[] packatData;
        DatagramSocket ds = new DatagramSocket(Attribute.sendingPort);
        DatagramSocket dsReceive = new DatagramSocket(Attribute.receivePort);
        InetAddress ia = InetAddress.getByName(Attribute.sendingAddress);
        InetAddress ib = InetAddress.getByName(Attribute.receivingAddress);

        //ressource Start
        udpFrame = ConstructCANFrame.resourceStart();
        DatagramPacket sendPacket = new DatagramPacket( udpFrame, udpFrame.length, ia, Attribute.sendingPort);
        ds.send(sendPacket);

        //train STOP
        udpFrame = ConstructCANFrame.stopTrain();
        sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
        ds.send(sendPacket);

        //SET SAND
        udpFrame = ConstructCANFrame.setSand(Attribute._STEAM_ID);

        System.out.println("SETSAND():");
        for (int i = 0; i < udpFrame.length; i++) {
            System.out.println("udpFrame[" + i + "]: " + udpFrame[i]);
        }

        //int i = 0;

        //System.out.println("I: " + i);
        sendPacket = new DatagramPacket( udpFrame, udpFrame.length, ia, Attribute.sendingPort);
        //System.out.println("1");
        ds.send( sendPacket );


        //RESOURCE STOP
        udpFrame = ConstructCANFrame.resourceStop();
        sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
        ds.send( sendPacket );



        //System.out.println("2");
        // Auf Anfrage warten
        sendPacket = new DatagramPacket( new byte[13], 13, ib, Attribute.receivePort );
        dsReceive.receive( sendPacket );
        //System.out.println("3");
        //comment

        // Empf�nger auslesen
        InetAddress address = sendPacket.getAddress();
        //System.out.println("4");
        int         port2    = sendPacket.getPort();
        int         len     = sendPacket.getLength();
        byte[]      data    = sendPacket.getData();

        System.out.println("DATA Received:");
        for (int j = 0; j<data.length; j++) {
            System.out.println("data[" + j + "]: " + data[j]);
        }

        udpFrame = ConstructCANFrame.go();
        sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
    }



}

