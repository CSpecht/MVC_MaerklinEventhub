package Ressources.ResourceGet;

import Ressources.Resource;

import General.Attribute;
import General.ConstructCANFrame;

import javax.swing.text.MaskFormatter;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

public class ResourceWATER extends Resource {


    /*   boolean stop;
    public ResourceWATER(String ip, int port, boolean stop) {
        super(ip, port, stop);
    }
*/


    //TODO: IMPLEMENTATION WRONG THE RESULT ISN'T RIGHT!!! -> SENDING TO MUCH DATA
    public DatagramPacket getWater () throws IOException {

        boolean result = false;
        byte[] udpFrame = new byte[13];
        byte[] packatData;
        DatagramSocket ds = new DatagramSocket(Attribute.sendingPort);
        DatagramSocket dsReceive = new DatagramSocket(General.Attribute.receivePort);
        InetAddress ia = InetAddress.getByName(Attribute.sendingAddress);
        InetAddress ib = InetAddress.getByName(Attribute.receivingAddress);

        //GET WATER

        udpFrame = ConstructCANFrame.getWater(Attribute._STEAM_ID);

       System.out.println("GETWATER():");
        for (int i = 0; i < udpFrame.length; i++) {
            System.out.println("udpFrame[" + i + "]: " + udpFrame[i]);
        }

        int i = 0;

        System.out.println("I: " + i);
        DatagramPacket sendPacket = new DatagramPacket( udpFrame, udpFrame.length, ia, Attribute.sendingPort);
        System.out.println("1");
        ds.send( sendPacket );

        System.out.println("2");
        byte[] buf = new byte[13];
        // Auf Anfrage warten
        DatagramPacket receivePacket = new DatagramPacket(buf,buf.length);
        System.out.println("3");
        dsReceive.receive(receivePacket);
        System.out.println("4");
        //comment

        // Empf�nger auslesen
        InetAddress address = receivePacket.getAddress();
        System.out.println("5");
        int         port2    = receivePacket.getPort();
        int         len     = receivePacket.getLength();
        byte[]      data    = receivePacket.getData();

        System.out.println("DATA Received:");
        for (int j = 0; j<data.length; j++) {
            System.out.println("data[" + j + "]: " + data[j]);
        }

        return receivePacket;
    }

    public void setWater() throws IOException {
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

        //SET WATER
        udpFrame = ConstructCANFrame.setWater(Attribute._STEAM_ID);

        System.out.println("GETWATER():");
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

