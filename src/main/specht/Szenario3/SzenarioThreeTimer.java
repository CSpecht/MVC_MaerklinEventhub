package specht.Szenario3;

import specht.General.Attribute;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.TimerTask;

import static java.lang.Thread.sleep;


public class SzenarioThreeTimer extends TimerTask {
    int actualSecond, duration;
    Iterator cmdIterator;
    byte[] element = new byte[13];
    DatagramSocket ds;
    InetAddress ia;
    private Attribute attribute = null;

    SzenarioThreeTimer(int s, Iterator cmdIterator, DatagramSocket ds, InetAddress ia) {
        if (attribute == null ) {
            attribute = new Attribute();
        }
        this.duration = s;
        this.cmdIterator = cmdIterator;
        this.ds = ds;
        this.ia = ia;
    }

    @Override
    public void run() {
        actualSecond++;

        //System.out.println("durati: " + duration);
        //System.out.println("actual: " + actualSecond);

        if (actualSecond <= duration) {
            try {
                System.out.println("actual: " + actualSecond);
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            /*if (actualSecond == 1 && cmdIterator.hasNext()) {
                element = (byte[]) cmdIterator.next();
            }
            //if (element == new BigInteger("")) {

            for (int i = 0; i < element.length; i++) {
                System.out.print("[" + i + "]: " + element[i] + " ");
            }

            DatagramPacket packet = new DatagramPacket(element, element.length, ia, attribute.getSendingPort());

            try {
                ds.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
            //  }
        } else {
            this.cancel();
        }
    }
}

