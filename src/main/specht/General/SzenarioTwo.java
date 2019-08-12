package specht.General;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
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
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask(){
            public void run(){
                //elapsed time
                Second.incrementAndGet();
                if (debug) {
                    System.out.println("SECOND: " + getSecond());
                }

            }

        }, 0, 1000);
        try {
            t.schedule(new SzenarioTwoTimer(GameID, this.getSecond(), getDatagramSocketSending(),ia), 0, 5000);
        } catch (SocketException e) {
            e.printStackTrace();
        }


    }

}
