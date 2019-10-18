package specht.Szenario3;

import specht.General.Attribute;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class SzenarioThree extends Thread {
    boolean debug = true;
    DatagramSocket ds, dr;
    InetAddress ia, ib;
    Queue<byte[]> cmdQueue = new LinkedList();
    Queue<String> testQueue = new LinkedList();


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
            cmdQueue = gcft.getCommandQueue();

            //cmdQueue = gcft.getCommandQueue();

            if (debug) {
                Iterator iterator = cmdQueue.iterator();
                int j = 0;
                while(iterator.hasNext()){
                    byte[] element = (byte[]) iterator.next();
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
