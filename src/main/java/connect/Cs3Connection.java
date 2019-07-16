package connect;
import General.ConstructCANFrame;
import General.UdpPackage;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.SwingWorker;

public abstract class Cs3Connection extends Thread {

    protected static final int CAN_EMPFANGS_TIMEOUT_MS = 5000;
    public static final int CAN_TELEGRAMM_LEN = 13;
    public static final int CAN_TELEGRAMM_LEN2 = 16;
    protected static final int CAN_EMPFANGSPUFFER = 16000;
    protected static final int CAN_SENDEPUFFER = 16000;
    protected static final int UDP15730_CS2_out = 15730;
    public static final int UDP15731_CS2_in = 15731;
    protected static final long CS2TIMEOUT_NS = 30000000000L;
    private static Cs3Connection cs3Connection;
    protected static boolean nurTcpVerbindungenErlauben = false;
    private static final ExecutorService pool = Executors.newWorkStealingPool();
    protected static Boolean lock = new Boolean(false);

    public static boolean debug = true;

    public static boolean debug_inp = false;

    public static boolean debug_out = false;

    public static boolean debug_bytes = false;

    public boolean empfangEinschalten = false;

    private boolean empfangIstEin;

    public static Cs3Connection getVerbindung(String... args) {
        byte b;
        int i;
        String[] arrayOfString;
        for (i = arrayOfString = args.length, b = 0; b < i; ) { String ip = arrayOfString[b];
            try {
                cs3Connection = TcpConnection.getVerbindung(ip);
                if (cs3Connection != null) return cs3Connection;
            } catch (Exception ignore) {
                System.err.println("Cs2Verbindung: " + ignore);
            }  b++; }
        if (nurTcpVerbindungenErlauben) return null;

        if (cs3Connection == null) cs3Connection = UdpConnection.getVerbindung();
        return cs3Connection;
    }

    public static Cs3Connection getVerbindung() {
        cs3Connection = TcpConnection.getVerbindung();
        if (cs3Connection != null) return cs3Connection;
        if (nurTcpVerbindungenErlauben) return null;
        cs3Connection = UdpConnection.getVerbindung();
        return cs3Connection;
    }

    public static ExecutorService getExecutorService() { return pool; }

    protected static WeakEventListenerList listenerList = new WeakEventListenerList();

    protected AtomicInteger counterIn = new AtomicInteger();
    protected AtomicInteger counterOut = new AtomicInteger();

    protected CopyOnWriteArrayList<InetAddress> cs2Adressen = new CopyOnWriteArrayList();

    public void setEmpfangAktiv(boolean aktiv) { this.empfangEinschalten = aktiv; }

    public abstract void doSend(UdpPackage paramUdpPacket, DatagramPacket paramDatagramPacket);

    //public static void addUdpListener(CanListener l) { listenerList.add(CanListener.class, l); }

    //public static void removeUdpListener(CanListener l) { listenerList.remove(CanListener.class, l); }

    //public static void addConListener(Cs2ConnectionListener l) { listenerList.add(Cs2ConnectionListener.class, l); }

    //public static void removeConListener(Cs2ConnectionListener l) { listenerList.remove(Cs2ConnectionListener.class, l); }

    public boolean isEmpfangEin() { return this.empfangIstEin; }

    public void start() {
        if (debug) System.out.println(String.valueOf(getName()) + ".start()");
        this.empfangEinschalten = true;
        super.start();
    }
    protected final void fireUdpEvent(UdpPackage udpPacket, boolean out) throws Exception {
        if (out ? debug_out : debug_inp) {
            System.out.println(new CanBefehlRaw(udpPacket));
            if (debug_bytes) System.out.println(Arrays.toString(udpPacket.getData()));

        }
        final CanBefehl befehl = CanProtokoll.Command.translate(new CanBefehl(udpPacket)); byte b; int i; CanListener[] arrayOfCanListener;


        for (i = arrayOfCanListener = (CanListener[])listenerList.getListeners(CanListener.class).length, b = 0; b < i; ) { final CanListener empfaenger = arrayOfCanListener[b];

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
            b++; }

    }

    public int getCounter_in() { return this.counterIn.get(); }

    public int getCounter_out() { return this.counterOut.get(); }

    public CopyOnWriteArrayList<InetAddress> getCS2_IPs() { return this.cs2Adressen; }

    protected final void fireCs3ConnectionEvent(final InetAddress cs2Adress, final boolean connected, final DatagramPacket packet) {
        final long jetzt = System.nanoTime(); byte b; int i; Cs2ConnectionListener[] arrayOfCs2ConnectionListener;
        for (i = arrayOfCs2ConnectionListener = (Cs2ConnectionListener[])listenerList.getListeners(Cs2ConnectionListener.class).length, b = 0; b < i; ) { final Cs2ConnectionListener empfaenger = arrayOfCs2ConnectionListener[b];
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
            b++; }

    }

    public InetAddress getCS2_IP(int index) { return (InetAddress)getCS2_IPs().get(index); }

    public static boolean isNurTcpVerbindungenErlauben() { return nurTcpVerbindungenErlauben; }

    public static void setNurTcpVerbindungenErlauben(boolean nurTcpVerbindungenErlauben) { Cs3Connection.nurTcpVerbindungenErlauben = nurTcpVerbindungenErlauben; }

}
