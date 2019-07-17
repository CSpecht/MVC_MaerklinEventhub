package java.Ressources.ResourceGet;

import Ressources.Resource;

import java.General.Attribute;
import java.General.ConstructCANFrame;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ResourceWATER extends Resource {


    /*   boolean stop;
    public ResourceWATER(String ip, int port, boolean stop) {
        super(ip, port, stop);
    }
*/


    //TODO: IMPLEMENTATION WRONG THE RESULT ISN'T RIGHT!!! -> SENDING TO MUCH DATA
    public void getWater () throws IOException {

        boolean result = false;
        byte[] udpFrame = new byte[13];
        byte[] packatData;
        DatagramSocket ds = new DatagramSocket(Attribute.sendingPort);
        DatagramSocket dsReceive = new DatagramSocket(Attribute.receivePort);
        InetAddress ia = InetAddress.getByName(Attribute.sendingAddress);
        InetAddress ib = InetAddress.getByName(Attribute.receivingAddress);

        //GET WATER
        udpFrame = ConstructCANFrame.getWater(Attribute._STEAM_ID);

        System.out.println("GETWATER():");
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

    public void setWater() throws IOException {
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

