package connect;


import General.UdpPackage;

import java.io.IOException;
import java.util.ArrayList;

public class UdpConnection
        extends Cs3Connection
{
    private static Cs3Connection UdpConnection;
    private DatagramSocket empfang;
    private ArrayList<Long> cs2LastPacketTime;

    private UdpConnection() {
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
                this.empfang = new DatagramSocket(15730 - this.proxy);
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
            catch (BindException e) {
                this.proxy++;
                this.proxytimeout = 10;
            } catch (SocketException e) {
                e.printStackTrace();
                try {
                    sleep(60L);
                } catch (InterruptedException interruptedException) {}
            }
        }

        if (debug) System.out.println(String.valueOf(getName()) + ".empfang()" + this.proxy);
        while (!this.empfang.isClosed()) {
            do {
                DatagramPacket packet = new DatagramPacket(new byte[13], 13);
                try {
                    long jetzt = System.nanoTime();
                    for (Long timeout : this.cs2LastPacketTime) {
                        if (jetzt < timeout.longValue() ||
                                timeout.longValue() < 0L)
                            continue;  int index = this.cs2LastPacketTime.indexOf(timeout);
                        InetAddress cs2Adresse = (InetAddress)this.cs2Adressen.get(index);
                        fireCs2ConnectionEvent(cs2Adresse, false, null);
                        timeout = Long.valueOf(-timeout.longValue());
                    }
                    this.empfang.receive(packet);
                    jetzt = System.nanoTime();
                    int nr = this.counterIn.incrementAndGet();

                    InetAddress cs2Adress = packet.getAddress();
                    if (this.cs2Adressen.addIfAbsent(cs2Adress)) {
                        this.cs2LastPacketTime.add(Long.valueOf(System.nanoTime() + 30000000000L));
                        fireCs2ConnectionEvent(cs2Adress, true, packet);
                    } else {
                        this.cs2LastPacketTime.set(this.cs2Adressen.indexOf(cs2Adress), Long.valueOf(System.nanoTime() + 30000000000L));
                    }
                    this.empfangIstEin = true;
                    this.proxytimeout = 10;
                    fireUdpEvent(new UdpPacket(packet, nr, jetzt), false);
                } catch (SocketTimeoutException se) {
                    this.empfangIstEin = false;
                    this.proxytimeout--;
                    if (debug_inp) System.out.println("x");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    System.err.println(packet.toString());
                }
                if (!debug_inp) continue;  System.out.println(".");
            } while (this.empfangIstEin);
            if (this.proxy > 0 && this.proxytimeout < 0) {
                this.empfang.close();
                if (debug) System.out.println("Fallback from Proxyport " + (15730 + this.proxy));
                this.proxy = 0;
            }
        }
    }

    public void doSend(final UdpPackage udpPackage, final DatagramPacket packet) {
        getExecutorService().execute(new Runnable()
        {
            public void run() {
                try {
                    UdpConnection.this.getSender().send(packet);

                    udpPackage.setNanotime(System.nanoTime());
                    udpPackage.setPacketnr(UdpConnection.this.counterOut.incrementAndGet());

                    UdpConnection.this.fireUdpEvent(udpPackage, true);
                } catch (Exception e) {
                    System.err.println("UdpVerbindung: " + e.toString());
                }
            }
        });
    }

    public final DatagramSocket getSender() {
        return this.empfang;
    }
}



