package java.connect;


import java.General.Attribute;
import java.General.UdpPackage;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class UdpConnection
        extends Cs3Connection
{
    private static Cs3Connection UdpConnection;
    private DatagramSocket empfang;
    private ArrayList<Long> cs2LastPacketTime;
    private boolean empfangIstEin;

    public UdpConnection() {
        this.cs2LastPacketTime = new ArrayList();

        setDaemon(true);
        setName("UdpConnection");
    }

    public static final Cs3Connection getVerbindung() {
        if (nurTcpVerbindungenErlauben) return null;
        if (UdpConnection == null) {
            synchronized (lock) {
                if (UdpConnection == null) {
                    UdpConnection = new UdpConnection();
                    UdpConnection.start();
                }
            }
        }
        return UdpConnection;
    }

    @Override
    public void doSend(UdpPackage paramUdpPacket, DatagramPacket paramDatagramPacket) {
        getExecutorService().execute(new Runnable()
        {
            public void run() {
                try {
                    UdpConnection.this.getSender().send(paramDatagramPacket);

                    paramUdpPacket.setNanotime(System.nanoTime());
                    paramUdpPacket.setPacketnr(UdpConnection.this.counterOut.incrementAndGet());

                    UdpConnection.this.fireUdpEvent(paramUdpPacket, true);
                } catch (Exception e) {
                    System.err.println("UdpVerbindung: " + e.toString());
                }
            }
        });
    }

    public void start() { super.start(); }

    public void run() {
        if (debug) System.out.println(String.valueOf(getName()) + ".run()");

        while (this.empfang == null) {

            while (!this.empfangEinschalten) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException interruptedException) {}
            }
            try {
                this.empfang = new DatagramSocket(Attribute.receivePort);
                System.out.println(this.empfang.isBound());
                try {
                    this.empfang.setReuseAddress(true);
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                try {
                    this.empfang.setReceiveBufferSize(16000);
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                try {
                    this.empfang.setSendBufferSize(16000);
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                try {
                    this.empfang.setSoTimeout(5000);
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            }
            catch (SocketException e) {
                e.printStackTrace();
                try {
                    sleep(60L);
                } catch (InterruptedException interruptedException) {}
            }
        }

        if (debug) System.out.println(String.valueOf(getName()) + ".empfang()");
        while (!this.empfang.isClosed()) {
            do {
                DatagramPacket packet = new DatagramPacket(new byte[13], 13);
                try {
                    long jetzt = System.nanoTime();
                    for (Long timeout : this.cs2LastPacketTime) {
                        if (jetzt < timeout.longValue() ||
                                timeout.longValue() < 0L)
                            continue;  int index = this.cs2LastPacketTime.indexOf(timeout);
                        //InetAddress cs2Adresse = (InetAddress)this.cs2Adressen.get(index);
                        //fireCs3ConnectionEvent(cs2Adresse, false, null);
                        timeout = Long.valueOf(-timeout.longValue());
                    }
                    this.empfang.receive(packet);
                    jetzt = System.nanoTime();
                    int nr = this.counterIn.incrementAndGet();

                    InetAddress cs2Adress = packet.getAddress();
                    if (this.cs2Adressen.addIfAbsent(cs2Adress)) {
                        this.cs2LastPacketTime.add(Long.valueOf(System.nanoTime() + 30000000000L));
                        fireCs3ConnectionEvent(cs2Adress, true, packet);
                    } else {
                        this.cs2LastPacketTime.set(this.cs2Adressen.indexOf(cs2Adress), Long.valueOf(System.nanoTime() + 30000000000L));
                    }
                    this.empfangIstEin = true;
                    
                    fireUdpEvent(new UdpPackage(packet, nr, jetzt), false);
                } catch (SocketTimeoutException se) {
                    this.empfangIstEin = false;
                    if (debug_inp) System.out.println("x");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    System.err.println(packet.toString());
                }
                if (!debug_inp) continue;  System.out.println(".");
            } while (this.empfangIstEin);
        }
    }

   
    public final DatagramSocket getSender() {
        return this.empfang;
    }
}



