package specht.Szenario3;

import specht.General.Attribute;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class SzenarioThree extends Thread {
    boolean debug = true;
    DatagramSocket ds, dr;
    InetAddress ia, ib;
    Queue<byte[]> cmdQueue = new LinkedList();
    Queue<Integer> durrQueue = new LinkedList();


    public SzenarioThree () {
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

    public DatagramSocket getDatagramSocketSending() throws SocketException {
        if (ds == null) {
            ds = new DatagramSocket(Attribute.sendingPort);
        }
        return ds;
    }

    public DatagramSocket getDatagramSocketReceiving() throws SocketException {
        if (dr == null) {
            dr = new DatagramSocket(Attribute.receivePort);
        }
        return dr;
    }

    public void run () {
        try {
            //System.out.println("SAY HELLO");
            GetCommandFromTxt gcft = new GetCommandFromTxt(cmdQueue);
            gcft.processFile();
            gcft.showTestQueue();
            cmdQueue = gcft.getCommandQueue();
            durrQueue = gcft.getDurrQueue();
            Timer t = new Timer();
            byte[] element = new byte[13];

            Iterator durrIterator = durrQueue.iterator();
            int durrSecond = 0;
            Iterator cmdIterator = cmdQueue.iterator();
            int s = 0;
            while (durrIterator.hasNext()) {
                durrSecond = (int) durrIterator.next();

                if (durrSecond != 0) {
                    s = durrSecond;
                    t.schedule(new SzenarioThreeTimer(s, cmdIterator, getDatagramSocketSending(),ia ),0,1000);
                } else {
                    if(cmdIterator.hasNext()) {
                        element = (byte[]) cmdIterator.next();
                        for (int i = 0; i < element.length; i++) {
                            System.out.println("element["+i+"]: " + element[i] );
                        }
                        DatagramPacket packet = new DatagramPacket(element, element.length, ia, Attribute.sendingPort);

                        try {
                            ds.send(packet);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        cmdIterator.remove();
                    }
                }

            }





            //cmdQueue = gcft.getCommandQueue();

            if (debug == false) {
                Iterator iterator = cmdQueue.iterator();
                int j = 0;
                while(iterator.hasNext()){
                    element = (byte[]) iterator.next();
                    for (int i = 0; i < element.length; i++) {
                        System.out.println("element["+j+"]["+ i + "]: " + element[i]);
                    }
                    j++;
                    //System.out.println("element: " + element);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
