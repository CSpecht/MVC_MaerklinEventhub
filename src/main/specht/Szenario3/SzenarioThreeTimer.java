package specht.Szenario3;

import specht.General.Attribute;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.TimerTask;


public class SzenarioThreeTimer extends TimerTask {
    int actualSecond, duration;
    Iterator cmdIterator;
    byte[] element = new byte[13];
    DatagramSocket ds;
    InetAddress ia;
    

    SzenarioThreeTimer(int s, Iterator cmdIterator, DatagramSocket ds, InetAddress ia) {
        this.duration = s;
        this.cmdIterator = cmdIterator;
        this.ds = ds;
        this.ia = ia;
    }

    @Override
    public void run() {
        actualSecond++;

        System.out.println("durati: " + duration);
        System.out.println("actual: " + actualSecond);

        if (actualSecond <= duration) {
            if (actualSecond == 1 && cmdIterator.hasNext()) {
                element = (byte[]) cmdIterator.next();
            }
            //if (cmdIterator.hasNext()) {

            for (int i = 0; i < element.length; i++) {
                System.out.print("[" + i + "]: " + element[i] + " ");
            }

            DatagramPacket packet = new DatagramPacket(element, element.length, ia, Attribute.sendingPort);

            try {
                ds.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //  }
        } else {
            this.cancel();
        }
    }
}

