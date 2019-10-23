package specht.Szenario3;

import specht.General.Attribute;

import java.io.IOException;
import java.net.*;
import java.util.Iterator;
import java.util.TimerTask;

public class SzenarioThreeTimer extends TimerTask {
    int sss, finalS;
    Iterator cmdIterator;
    byte[] element = new byte[13];
    DatagramSocket ds,dr;
    InetAddress ia, ib;

    SzenarioThreeTimer(int s, Iterator cmdIterator, DatagramSocket ds, InetAddress ia) {
        this.finalS = s;
        this.cmdIterator = cmdIterator;
        this.ds = ds;
        this.ia = ia;
    }


    @Override
    public void run() {
        sss++;

        System.out.println("s: " + finalS);
        System.out.println("sss: " + sss);

        if (sss <= finalS) {
            if (sss == 1 && cmdIterator.hasNext()) {
                element = (byte[]) cmdIterator.next();
            }
            //if (cmdIterator.hasNext()) {

            for (int i = 0; i < element.length; i++) {
                System.out.println("element["+i+"]: " + element[i] );
            }

            DatagramPacket packet = new DatagramPacket(element, element.length, ia, Attribute.sendingPort);

            try {
                ds.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //  }
        } else {
            cmdIterator.remove();
            this.cancel();
        }
    }


}
