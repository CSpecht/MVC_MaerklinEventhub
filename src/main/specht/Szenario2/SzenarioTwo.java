package specht.Szenario2;

import specht.General.Attribute;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;

public class SzenarioTwo extends Thread {

    AtomicInteger Second = new AtomicInteger(0);
    int SecondInt = 0;
    boolean debug = true;
    boolean startRun = false;
    DatagramSocket ds, dr;
    InetAddress ia, ib;
    private Attribute attribute = null;
    public SzenarioTwo() {
        if (attribute== null) {
            attribute = new Attribute();
        }

        try {
            ds = getDatagramSocketSending();
            dr = getDatagramSocketReceiving();
            ia = InetAddress.getByName(attribute.getSendingAddress());
            ib = InetAddress.getByName(attribute.getReceivingAddress());
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public DatagramSocket getDatagramSocketSending() throws SocketException {
        if (ds == null) {
            ds = new DatagramSocket(attribute.getSendingPort());
        }
        return ds;
    }

    public DatagramSocket getDatagramSocketReceiving() throws SocketException {
        if (dr == null) {
            dr = new DatagramSocket(attribute.getReceivePort());
        }
        return dr;
    }

    public AtomicInteger getSecond() {
        return Second;
    }

    public void setSecond(AtomicInteger second) {
        Second = second;
    }

    public int getSecondInt() {
        return SecondInt;
    }

    public void setSecondInt() {
        this.SecondInt = getSecond().get();
    }

    public void run() {

        Timer t = new Timer();
        try {
                t.schedule(new SzenarioTwoTimer(this.getSecond(), getDatagramSocketSending(), ia), 0, 1000);
        } catch (SocketException e) {
                e.printStackTrace();
        }

    }
}

