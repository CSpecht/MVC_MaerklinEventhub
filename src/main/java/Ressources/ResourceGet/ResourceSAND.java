package Ressources.ResourceGet;

import General.Attribute;
import General.ConstructCANFrame;
import Ressources.Resource;

import javax.swing.text.MaskFormatter;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

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
        DatagramSocket dsReceive = new DatagramSocket(General.Attribute.receivePort);
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
        DatagramSocket dsReceive = new DatagramSocket(General.Attribute.receivePort);
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

