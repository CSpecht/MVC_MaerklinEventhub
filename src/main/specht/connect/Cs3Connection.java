package specht.connect;

import java.net.*;
import java.util.ArrayList;

public class Cs3Connection extends Thread {

    TcpConnection tcp;
    UdpConnectionResponse udp;
   // Cs3Connection cs3 = new Cs3Connection();
    ArrayList<InetAddress> ia = new ArrayList<InetAddress>();
    DatagramSocket ds;

    public Cs3Connection () {
        try {
            ds = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void getVerbindung() {
         //tcp = new TcpConnection();
        // udp = new UdpConnectionResponse();
         //tcp.getVerbindung(Attribute.sendingAddress);
         //udp.getVerbindung();
    }

    public void run() {
        while(true) {

            //DatagramPacket packet = ds.receive();
            //new Thread(new UdpConnectionResponse(ds,packet)).start();
        }
        //getVerbindung();
        //cs3.getVerbindung();
//
//        System.out.println("Cs3Connection...");
    }
    //protected


 /*   protected final void fireUdpEvent(UdpPackage udpPacket, boolean out) throws Exception {
        if (out ? debug_out : debug_inp) {
           // System.out.println(new CanBefehlRaw(udpPacket));
            if (debug_bytes) System.out.println(Arrays.toString(udpPacket.getData()));

        } */
       // final CanBefehl befehl = CanProtokoll.Command.translate(new CanBefehl(udpPacket)); byte b; int i; CanListener[] arrayOfCanListener;
      //  if (empfaenger != null) {

       // }

       /* for (i = arrayOfCanListener = (CanListener[])listenerList.getListeners(CanListener.class).length, b = 0; b < i; ) { final CanListener empfaenger = arrayOfCanListener[b];

            if (empfaenger != null) {
              //  CanFilter filter = empfaenger.getCanFilter();

                //if (filter == null || filter.isEnabled(befehl)) {

                    ArrayBlockingQueue<CanBefehl> queue = empfaenger.getCanQueue();
                    if (queue != null) queue.offer(befehl);

                    getExecutorService().execute(new Runnable()
                    {
                        public void run() {
                            CanEvent udpEvent = new CanEvent(befehl);

                            empfaenger.canPacketEmpfangen(udpEvent);
                        }
                    });
                }
            }
            b++; }*/



   // public int getCounter_in() { return this.counterIn.get(); }

   // public int getCounter_out() { return this.counterOut.get(); }

    //public CopyOnWriteArrayList<InetAddress> getCS2_IPs() { return this.cs2Adressen; }

   //protected final void fireCs3ConnectionEvent(final InetAddress cs2Adress, final boolean connected, final DatagramPacket packet) {
    //    final long jetzt = System.nanoTime();
    //    byte b;
    //    int i;
        //Cs2ConnectionListener[] arrayOfCs2ConnectionListener;
        /*for (i = arrayOfCs2ConnectionListener = (Cs2ConnectionListener[])listenerList.getListeners(Cs2ConnectionListener.class).length, b = 0; b < i; ) { final Cs2ConnectionListener empfaenger = arrayOfCs2ConnectionListener[b];
            if (empfaenger != null) {
                (new SwingWorker<Cs2ConnectionEvent, String>()
                {
                    public Cs2ConnectionEvent doInBackground() {
                        return new Cs2ConnectionEvent(cs2Adress, connected, packet, jetzt);
                    }

                    public void done() {
                        try {
                            empfaenger.cs2conEmpfangen((Cs2ConnectionEvent)get());
                        } catch (InterruptedException|java.util.concurrent.ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }).execute();
            }
            b++; }*/

   // }

    public InetAddress getCS3_IP(int index) {
        return (InetAddress)ia.get(index);

    }



/*
    public static boolean isNurTcpVerbindungenErlauben() { return nurTcpVerbindungenErlauben; }

    public static void setNurTcpVerbindungenErlauben(boolean nurTcpVerbindungenErlauben) { Cs3Connection.nurTcpVerbindungenErlauben = nurTcpVerbindungenErlauben; }
*/
}
