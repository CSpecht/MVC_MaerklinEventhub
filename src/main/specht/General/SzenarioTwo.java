package specht.General;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;

public class SzenarioTwo extends Thread {

    //for test cases equal 16 otherwise delete = 16!
    int GameID = 16;
    AtomicInteger Second = new AtomicInteger(0);
    int SecondInt = 0;
    boolean debug = true;
    boolean startRun = false;
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

    public void setStartRun(boolean start) {
        this.startRun = start;
    }

    public boolean getStartRun() {
        return startRun;
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

