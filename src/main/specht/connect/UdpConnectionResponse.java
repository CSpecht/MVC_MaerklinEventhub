package specht.connect;


import specht.General.Attribute;
import specht.General.CanBefehlRaw;
import specht.General.UdpPackage;

import javax.swing.text.MaskFormatter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class UdpConnectionResponse extends Thread
{
    private static Cs3Connection UdpConnection;
    private DatagramSocket empfang;
    private ArrayList<Long> cs2LastPacketTime;
    private boolean empfangIstEin;
    private boolean debug = true;
    private boolean empfanEinschalten;

    protected DatagramSocket socket = null;
    protected boolean listen = true;
    protected int ressourceAmmountInt;
    protected byte[] data = new byte[13];

    public UdpConnectionResponse() throws SocketException {
        //this.cs2LastPacketTime = new ArrayList();

        //setDaemon(true);
        setName("UdpConnectionResponse");
        socket = new DatagramSocket(Attribute.receivePort);
    }

    public synchronized void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        byte[] buf = new byte[13];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        while (listen) {
            try {
                long mills = new Date().getTime();
                int pkNr = 1;
                // receive request
                socket.receive(packet);

                // figure out response
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                int len = packet.getLength();
                data = packet.getData();

                setRessourceAmmount(data);
                CanBefehlRaw canRaw = null;
                UdpPackage udpP = new UdpPackage(packet,pkNr,mills);
                try {
                    canRaw = new CanBefehlRaw(udpP);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (canRaw.isResponse()) {
                    listen = false;
                    socket.close();
                    //dsReceive.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
                listen = false;
            }
        }

        if (debug) {
            MaskFormatter mfHEX = null;
            String hexFormatted = "";
            String hexNr = "";
            try {
                mfHEX = new MaskFormatter("[HHHHHHHH:HH][HH,HH,HH,HH,HH,HH,HH,HH]");
                mfHEX.setValueContainsLiteralCharacters(false);
                hexNr = hexEncode(data);
                hexFormatted = mfHEX.valueToString(hexNr);
                System.out.println("hex: " + hexFormatted + "\nResAmount: " + getRessourceAmmount());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        socket.close();

    }
    public void setRessourceAmmount(byte[] data) {
        ressourceAmmountInt = parseHex2Int(data);
    }

    public int getRessourceAmmount () {
        return ressourceAmmountInt;
    }

    public int parseHex2Int (byte[] data ) {
        byte b2 = -100;
        int r;
        if (data.length != 13) {
            System.out.println("not a CAN Message!");
            return 0;
        }
        r = (data[11] & 0xFF);

        return r;
        //return data[i] Integer.parseInt(String.valueOf(data[11]));
    }

    protected static String hexEncode(byte[] buf) {
        return hexEncode(buf, new StringBuilder()).toString();
    }

    //Encoding byte[] into hexadecimal Number, RETURN STRINGBUILDER
    public static StringBuilder hexEncode (byte[] buf, StringBuilder sb) {
        for (byte b : buf) {
            sb.append(String.format("%02x",b));
        }
        return sb;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }


//    public static final Cs3Connection getConnection() {
//        if (UdpConnectionResponse == null) {
//           if(UdpConnectionResponse == null) {
//                    UdpConnectionResponse = new UdpConnectionResponse();
//                    UdpConnectionResponse.start();
//                }
//           }
//
//        return UdpConnectionResponse;
//    }

    //public void start() {super.start();}
    public final DatagramSocket getSender() {
        return this.empfang;
    }


    //@Override
 /*   public void doSend(UdpPackage paramUdpPacket, DatagramPacket paramDatagramPacket) {
        getExecutorService().execute(new Runnable()
        {
            public void run() {
                try {
                    UdpConnectionResponse.this.getSender().send(paramDatagramPacket);

                    paramUdpPacket.setNanotime(System.nanoTime());
                    paramUdpPacket.setPacketnr(UdpConnectionResponse.this.counterOut.incrementAndGet());
                    UdpConnectionResponse.this.doSend(paramUdpPacket,paramDatagramPacket);
                    //UdpConnectionResponse.this.fireUdpEvent(paramUdpPacket, true);
                    System.out.println("UDPCONNECTION");
                } catch (Exception e) {
                    System.err.println("UdpVerbindung: " + e.toString());
                }
            }
        });
    }*/


//   public void getVerbindung() {
//
//        if (UdpConnectionResponse == null) {
//            if (UdpConnectionResponse == null) {
//                UdpConnectionResponse = new UdpConnectionResponse();
//                UdpConnectionResponse.start();
//            }
//
//        }
//       //return UdpConnectionResponse;
//   }


    /*
        @Override
        public void doSend(UdpPackage paramUdpPacket, DatagramPacket paramDatagramPacket) {
            getExecutorService().execute(new Runnable()
            {
                public void run() {
                    try {
                        UdpConnectionResponse.this.getSender().send(paramDatagramPacket);

                        paramUdpPacket.setNanotime(System.nanoTime());
                        paramUdpPacket.setPacketnr(UdpConnectionResponse.this.counterOut.incrementAndGet());

                        //UdpConnectionResponse.this.fireUdpEvent(paramUdpPacket, true);
                    } catch (Exception e) {
                        System.err.println("UdpVerbindung: " + e.toString());
                    }
                }
            });
        }
    */
   // public void start() { super.start(); }

//    public void run() {
//        if (debug)
//        {
//            System.out.println(String.valueOf(getName()) + ".run()");
//            // System.out.println(this.udp.get);
//        }
//
//        if (this.empfang == null) {
//            try{
//                this.empfang = new DatagramSocket(Attribute.sendingPort);
//                System.out.println("empfang: " + this.empfang.getPort());
//                this.empfang.setReuseAddress(true);
//            } catch (SocketException e) {
//                e.printStackTrace();
//            }
//        }
//
//        while (true) {
//                DatagramPacket packet = empfang.receive();
//                new Thread(new Responder(socket, packet)).start();
//            }
//        }
//
//
//        synchronized (this) {
//
//        System.out.println(this.empfang);
//
//
//        while(this.empfang == null) {
//            while (!this.empfanEinschalten) {
//                try{
//                    this.empfang = new DatagramSocket(Attribute.sendingPort);
//                    System.out.println("empfang: " + this.empfang.getPort());
//                    this.empfang.setReuseAddress(true);
//                } catch (SocketException e) {
//                    e.printStackTrace();
//                }
//            }
//
//
//
//        }
//        }
//
//
//        while(!this.empfang.isClosed()) {
//            do {
//                DatagramPacket packet = null;
//                try {
//                    packet = new DatagramPacket(new byte[13], 13, InetAddress.getByName(Attribute.receivingAddress),Attribute.receivePort);
//                } catch (UnknownHostException e) {
//                    e.printStackTrace();
//                }
//                // try {
//                long jetzt = System.nanoTime();
//                try {
//                    this.empfang.receive(packet);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                jetzt = System.nanoTime();
//                //int nr = this.counterIn.incrementAndGet();
//
//                //InetAddress cs2Adress = packet.getAddress();
//                //if (this.cs2Adressen.addIfAbsent(cs2Adress)) {
//                //   this.cs2LastPacketTime.add(Long.valueOf(System.nanoTime() + 30000000000L));
//                //fireCs3ConnectionEvent(cs2Adress, true, packet);
//                //} else {
//                //  this.cs2LastPacketTime.set(this.cs2Adressen.indexOf(cs2Adress), Long.valueOf(System.nanoTime() + 30000000000L));
//                //}
//                //this.empfangIstEin = true;
//
//                //fireUdpEvent(new UdpPackage(packet, nr, jetzt), false);
//                //} //catch (SocketTimeoutException se) {
//                // this.empfangIstEin = false;
////                    if (debug_inp) System.out.println("x");
////                } catch (IOException e) {
////                    e.printStackTrace();
////                } catch (Exception e) {
////                    System.err.println(e.getMessage());
////                    System.err.println(packet.toString());
////                }
////                if (!debug_inp) continue;  System.out.println(".");
//                // }
//            } while (this.empfangIstEin);
//        }
//
///*        while (this.empfang == null) {
//
//         //   while (!this.empfangEinschalten) {
//                try {
//                    sleep(100L);
//                } catch (InterruptedException interruptedException) {}
//         //   }
//            try {
//                this.empfang = new DatagramSocket(Attribute.receivePort);
//                System.out.println("empfang1: " + this.empfang.isBound());
//                try {
//                    this.empfang.setReuseAddress(true);
//                } catch (SocketException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    this.empfang.setReceiveBufferSize(16000);
//                } catch (SocketException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    this.empfang.setSendBufferSize(16000);
//                } catch (SocketException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    this.empfang.setSoTimeout(5000);
//                } catch (SocketException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            catch (SocketException e) {
//                e.printStackTrace();
//                try {
//                    sleep(60L);
//                } catch (InterruptedException interruptedException) {}
//            }
//        }
//
//     //   if (debug) System.out.println(String.valueOf(getName()) + ".empfang()");
//        while (!this.empfang.isClosed()) {
//            do {
//                DatagramPacket packet = new DatagramPacket(new byte[13], 13);
//               // try {
//                    long jetzt = System.nanoTime();
//                    for (Long timeout : this.cs2LastPacketTime) {
//                        if (jetzt < timeout.longValue() ||
//                                timeout.longValue() < 0L)
//                            continue;
//                        int index = this.cs2LastPacketTime.indexOf(timeout);
//                        //InetAddress cs2Adresse = (InetAddress)this.cs2Adressen.get(index);
//                        //fireCs3ConnectionEvent(cs2Adresse, false, null);
//                        timeout = Long.valueOf(-timeout.longValue());
//                    }
//                //    this.empfang.receive(packet);
//                    jetzt = System.nanoTime();
//                    //int nr = this.counterIn.incrementAndGet();
//
//                    InetAddress cs2Adress = packet.getAddress();
//                    //if (this.cs2Adressen.addIfAbsent(cs2Adress)) {
//                    //   this.cs2LastPacketTime.add(Long.valueOf(System.nanoTime() + 30000000000L));
//                    //fireCs3ConnectionEvent(cs2Adress, true, packet);
//                    //} else {
//                    //  this.cs2LastPacketTime.set(this.cs2Adressen.indexOf(cs2Adress), Long.valueOf(System.nanoTime() + 30000000000L));
//                    //}
//                    //this.empfangIstEin = true;
//
//                    //fireUdpEvent(new UdpPackage(packet, nr, jetzt), false);
//                //} //catch (SocketTimeoutException se) {
//                   // this.empfangIstEin = false;
////                    if (debug_inp) System.out.println("x");
////                } catch (IOException e) {
////                    e.printStackTrace();
////                } catch (Exception e) {
////                    System.err.println(e.getMessage());
////                    System.err.println(packet.toString());
////                }
////                if (!debug_inp) continue;  System.out.println(".");
//               // }
//            } while (this.empfangIstEin); */
//            // }
//            // }
//
//
//        }

}





