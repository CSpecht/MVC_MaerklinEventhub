package java.connect;

import java.General.Attribute;
import java.General.UdpPackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;

public class TcpConnection extends Cs3Connection {

    private static ConcurrentSkipListMap<InetAddress, TcpConnection> tcpVerbindungen = new ConcurrentSkipListMap();

    private static WeakReference<TcpConnection> lastTcpVerbindung_weak = new WeakReference(null);

    private Socket tcp_socket;

    private InputStream tcp_inputStream;

    private ConcurrentLinkedQueue<UdpPackage> tcp_sendQueue;

    private OutputStream tcp_outputStream;
    private InetAddress tcp_ip;

    private boolean empfangIstEin = false;


    public TcpConnection(String ipAdresse) throws UnknownHostException  {
        InetAddress ip = null;
        try {
            ip = InetAddress.getByName(ipAdresse);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("No IP given");
        }
        this.tcp_ip = ip;

        try {
            System.out.println("verbinde per TCP mit " + Attribute.sendingAddress);
            this.tcp_socket = new Socket(InetAddress.getByName(Attribute.sendingAddress), Attribute.sendingPort);

        }
        //catch (UnknownHostException ignore) {
         //   System.err.println(String.valueOf(TcpConnection.class.getSimpleName()) + ignore.toString()); //+ " " + arg_ip.toString());
            //fireCs2ConnectionEvent(arg_ip.getInet(), false, null);
        //}
        catch (SocketException e) {
            System.err.println("TcpConnection" + e.toString() + " " + ip.toString());
            //fireCs2ConnectionEvent(arg_ip.getInet(), false, null);
        } catch (IOException f) {
            f.printStackTrace();
        }

        if (this.tcp_socket == null) throw new UnknownHostException("Konnte keinen Socket erzeugen (" + ip + ")");
        try {
            this.tcp_socket.setSoTimeout(5000);
            this.tcp_socket.setReuseAddress(true);
            this.tcp_socket.setReceiveBufferSize(16000);
            this.tcp_socket.setSendBufferSize(16000);
            InetAddress tcpadr = this.tcp_socket.getInetAddress();
            this.tcp_sendQueue = new ConcurrentLinkedQueue();
            this.tcp_inputStream = this.tcp_socket.getInputStream();
            this.tcp_outputStream = this.tcp_socket.getOutputStream();
            //fireCs3ConnectionEvent(tcpadr, true, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TcpConnection () {
        try {
            new TcpConnection(Attribute.sendingAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


/*    @Deprecated
    public static final TcpConnection getVerbindung(String ip) {
        TcpConnection connection = getVerbindung(ip);
        if (connection != null) return connection;

        return null;
    }*/

    public void getVerbindung (String testipAdress) {

        synchronized (lock) {
            InetAddress testip = null;
            try {
                testip = InetAddress.getByName(testipAdress);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            System.out.println(String.valueOf(TcpConnection.class.getSimpleName())
                    + ".getVerbindung " + testip.getHostAddress());
            TcpConnection connection = (TcpConnection)tcpVerbindungen.get(testip);

            if (connection != null && (
                    connection.tcp_socket == null || connection.tcp_socket.isClosed())) {
                System.out.println("geschlossene Verbindung " + connection + " ignoriert");
                connection = null;
            }

            if (connection == null) {
                try {
                    connection = new TcpConnection(testipAdress);
                    connection.start();
                    tcpVerbindungen.put(testip, connection);
                    lastTcpVerbindung_weak = new WeakReference(connection);
                } catch (UnknownHostException melde) {
                    System.err.println(String.valueOf(TcpConnection.class.getSimpleName()) + melde.toString() + testip);
                }
            }
        }
    }

    public void doSend(UdpPackage udpPackage, DatagramPacket dm) {
        if (this.tcp_sendQueue == null)
            return;  if (this.tcp_outputStream == null)
            return;  this.tcp_sendQueue.add(udpPackage);
        getExecutorService().execute(new Runnable()
        {
            public void run() {
               TcpConnection t = new TcpConnection();
               t.doSend();
            }
        });
    }

    protected void doSend() {
        if (this.tcp_sendQueue == null)
            return;
        while (!this.tcp_sendQueue.isEmpty()) {
            try {
                OutputStream os = this.tcp_socket.getOutputStream();
                if (os == null) {
                    System.err.println("nicht gesendet");
                    return;
                }
                UdpPackage udpPackage = (UdpPackage) this.tcp_sendQueue.poll();

                os.write(udpPackage.getData());
                udpPackage.setNanotime(System.nanoTime());
                udpPackage.setPacketnr(this.counterOut.incrementAndGet());

                fireUdpEvent(udpPackage, true);
            } catch (SocketException e) {
                close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

        public void close() {
            try {
                if (this.tcp_inputStream != null) this.tcp_inputStream.close();
                this.tcp_inputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (this.tcp_outputStream != null) this.tcp_outputStream.close();
                this.tcp_outputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (this.tcp_socket != null) {
                    fireCs3ConnectionEvent(this.tcp_socket.getInetAddress(), false, null);
                    this.tcp_socket.close();
                    this.tcp_socket = null;
                    System.out.println("TCP-Verbindung zu " + this.tcp_ip + " closed");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



   /* @Deprecated
    private TcpConnection(String[] args) throws UnknownHostException {
        if (args == null || args.length == 0) throw new UnknownHostException("Keine IP angegeben");  byte b; int i; int arrayOfString;
        for (i = arrayOfString = args.length, b = 0; b < i; ) { String arg = arrayOfString[b];
            try {
                if (this.tcp_socket != null) this.tcp_socket.close();
                this.tcp_socket = new Socket();
                InetSocketAddress endpoint = new InetSocketAddress(arg, '?');
                if (this.tcp_socket != null) {
                    this.tcp_socket.connect(endpoint);
                    InetAddress tcpadr = this.tcp_socket.getInetAddress();
                    this.cs2Adressen.add(tcpadr);
                    this.tcp_sendQueue = new ConcurrentLinkedQueue();
                    fireCs3ConnectionEvent(tcpadr, true, null);
                }
                break;
            } catch (UnknownHostException ignore) {
                System.err.println(String.valueOf(TcpConnection.class.getSimpleName()) + ignore);
            } catch (IOException e) {
                e.printStackTrace();
            }
            b++; }
        if (this.tcp_socket == null) throw new UnknownHostException("Keine passende IP angegeben");

    }




    public TcpConnection(String ipAdresse) throws UnknownHostException {
        InetAddress ip = InetAddress.getByName(ipAdresse);

        if (ip == null) throw new UnknownHostException("Keine IP angegeben");
        this.tcp_ip = ip;
        try {
            System.out.println("verbinde per TCP mit " + Attribute.sendingAddress);
            this.tcp_socket = new Socket(InetAddress.getByName(Attribute.sendingAddress), '?');
        }
        catch (UnknownHostException ignore) {
            System.err.println(String.valueOf(TcpConnection.class.getSimpleName()) + ignore.toString() + " " + arg_ip.toString());
            //fireCs2ConnectionEvent(arg_ip.getInet(), false, null);
        } catch (SocketException melde) {
            System.err.println("TcpVerbindung" + melde.toString() + " " + ip.toString());
            //fireCs2ConnectionEvent(arg_ip.getInet(), false, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.tcp_socket == null) throw new UnknownHostException("Konnte keinen Socket erzeugen (" + ip + ")");
        try {
            this.tcp_socket.setSoTimeout(5000);
            this.tcp_socket.setReuseAddress(true);
            this.tcp_socket.setReceiveBufferSize(16000);
            this.tcp_socket.setSendBufferSize(16000);
            InetAddress tcpadr = this.tcp_socket.getInetAddress();
            this.cs2Adressen.add(tcpadr);
            this.tcp_sendQueue = new ConcurrentLinkedQueue();
            this.tcp_inputStream = this.tcp_socket.getInputStream();
            this.tcp_outputStream = this.tcp_socket.getOutputStream();
            fireCs3ConnectionEvent(tcpadr, true, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Deprecated
    public static final TcpConnection getVerbindung(String... args) {
        byte b;
        int i;
        String[] arrayOfString;
        for (i = arrayOfString = args.length, b = 0; b < i; ) { String arg = arrayOfString[b];
            try {
                TcpConnection connection = getVerbindung(arg);
                if (connection != null) return connection;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (SocketException e) {
                e.printStackTrace();
            }  b++; }

        return null;
    }


*//*    public static final TcpConnection getVerbindung(String arg) throws UnknownHostException, SocketException {
        getVerbindung(arg);
        return (TcpConnection) tcpVerbindungen.get(testip);
    }*//*




    static void getVerbindung(String testipAdress) throws UnknownHostException {
        synchronized (lock) {
            InetAddress testip = InetAddress.getByName(testipAdress);
            System.out.println(String.valueOf(TcpConnection.class.getSimpleName())
                                + ".getVerbindung " + testip.getHostAddress());
            TcpConnection connection = (TcpConnection)tcpVerbindungen.get(testip);

            if (connection != null && (
                    connection.tcp_socket == null || connection.tcp_socket.isClosed())) {
                System.out.println("geschlossene Verbindung " + connection + " ignoriert");
                connection = null;
            }

            if (connection == null) {
                try {
                    connection = new TcpConnection(testipAdress);
                    connection.start();
                    tcpVerbindungen.put(testip, connection);
                    lastTcpVerbindung_weak = new WeakReference(connection);
                } catch (UnknownHostException melde) {
                    System.err.println(String.valueOf(TcpConnection.class.getSimpleName()) + melde.toString() + testip);
                }
            }
        }
    }


  *//* public static final TcpConnection getVerbindung(InetAddress arg) throws UnknownHostException {
        //InetAddress testip = arg;
        getVerbindung(arg);
        return (TcpConnection) tcpVerbindungen.get(arg);
    }*//*



    public static final TcpConnection getVerbindung() {
        TcpConnection connection = (TcpConnection)lastTcpVerbindung_weak.get();

        if (connection != null && connection.tcp_socket != null && !connection.tcp_socket.isClosed())
            return connection;


        for (TcpConnection conn : tcpVerbindungen.values()) {
            if (conn.tcp_socket == null || conn.tcp_socket.isClosed())
                continue;  return conn;
        }
        return null;
    }

    public void start() { super.start(); }

    public void run() {
        if (debug) System.out.println(String.valueOf(getClass().getSimpleName()) + ".run()");

        while (this.tcp_inputStream == null) {

            while (!this.empfangEinschalten) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException interruptedException) {}
            }


            try {
                if (this.tcp_socket != null) {
                    this.tcp_inputStream = this.tcp_socket.getInputStream();
                }

            }
            catch (BindException e) {
                //this.proxy++;
                //this.proxytimeout = 10;
            } catch (SocketException e) {
                e.printStackTrace();
                try {
                    sleep(60L);
                } catch (InterruptedException interruptedException) {}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (debug) System.out.println(String.valueOf(getClass().getSimpleName()) + ".empfang()"); //+ this.proxy);
        if (this.tcp_socket != null) fireCs3ConnectionEvent(this.tcp_socket.getInetAddress(), true, null);
        if (this.tcp_inputStream != null) {
            ////this.proxytimeout = 100;
            this.empfangIstEin = true;
            do {
                byte[] bytes = new byte[13];
                DatagramPacket packet = null;
                try {
                    long jetzt = System.nanoTime();
                    this.tcp_inputStream.read(bytes);
                    jetzt = System.nanoTime();
                    int nr = this.counterIn.incrementAndGet();
                    this.empfangIstEin = true;
                    //this.proxytimeout = 100;
                    if (this.tcp_socket != null) packet = new DatagramPacket(bytes, bytes.length, this.tcp_socket.getInetAddress(), this.tcp_socket.getPort());
                    fireUdpEvent(new UdpPackage(packet, nr, jetzt), false);
                } catch (SocketTimeoutException se) {
                    //this.proxytimeout--;
                    if (debug_inp) System.out.println("x");
                   // if (this.proxytimeout < 1) {
                   //     this.empfangIstEin = false;
                   // }
                } catch (SocketException se) {
                    System.err.println(String.valueOf(getClass().getSimpleName()) + se.toString());
                    this.empfangIstEin = false;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    System.err.println(String.valueOf(getClass().getSimpleName()) + e.getMessage());
                    if (this.tcp_inputStream == null) {
                        this.empfangIstEin = false;
                    }
                    if (packet != null) System.err.println(String.valueOf(getClass().getSimpleName()) + packet.toString());
                }
                if (!debug_inp) continue;  System.out.println(".");
            } while (this.empfangIstEin);



           // this.proxy = 0;
        }

        close();
        System.out.println("TCP-Verbindung zu " + this.tcp_ip + " beendet");
    }






    public void doSend(UdpPackage udpPackage, DatagramPacket datagramm) {
        if (this.tcp_sendQueue == null)
            return;  if (this.tcp_outputStream == null)
            return;  this.tcp_sendQueue.add(udpPackage);
        getExecutorService().execute(new Runnable()
        {
            public void run() {
                TcpConnection.this.doSend();
            }
        });
    }



    protected void doSend() {
        if (this.tcp_sendQueue == null)
            return;  while (!this.tcp_sendQueue.isEmpty()) {
            try {
                OutputStream os = getSender();
                if (os == null) {
                    System.err.println("nicht gesendet");
                    return;
                }
                UdpPackage udpPackage = (UdpPackage)this.tcp_sendQueue.poll();

                os.write(udpPackage.getData());
                udpPackage.setNanotime(System.nanoTime());
                udpPackage.setPacketnr(this.counterOut.incrementAndGet());

                fireUdpEvent(udpPackage, true);
            } catch (SocketException e) {
                close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    @Nullable
    private OutputStream getSender() throws IOException {
        if (this.tcp_outputStream == null && this.tcp_socket != null) this.tcp_outputStream = this.tcp_socket.getOutputStream();
        return this.tcp_outputStream;
    }



    public void close() {
        try {
            if (this.tcp_inputStream != null) this.tcp_inputStream.close();
            this.tcp_inputStream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (this.tcp_outputStream != null) this.tcp_outputStream.close();
            this.tcp_outputStream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (this.tcp_socket != null) {
                fireCs3ConnectionEvent(this.tcp_socket.getInetAddress(), false, null);
                this.tcp_socket.close();
                this.tcp_socket = null;
                System.out.println("TCP-Verbindung zu " + this.tcp_ip + " closed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public InetAddress getIP() {
        if (this.tcp_socket == null) return null;

        return this.tcp_ip;
    }*/

}
