package specht.connect;

import specht.General.Attribute;

import javax.swing.text.MaskFormatter;
import java.net.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class UdpConnectionSend extends Thread {
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
    InetAddress ia;

    {
        try {
            ia = InetAddress.getByName(Attribute.sendingAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public UdpConnectionSend(byte[] data) throws SocketException {
        //this.cs2LastPacketTime = new ArrayList();

        //setDaemon(true);
        setName("UdpConnectionSend");

        socket = new DatagramSocket(Attribute.sendingPort);
        socket.setReuseAddress(true);
        this.data = data;
    }

    public synchronized void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //byte[] buf = new byte[13];

        DatagramPacket packet = new DatagramPacket(data, data.length,ia,Attribute.sendingPort);

        while (listen) {
            try {
                long mills = new Date().getTime();
                int pkNr = 1;
                // receive request
                socket.send(packet);


                // figure out response
//                InetAddress address = packet.getAddress();
//                int port = packet.getPort();
//                int len = packet.getLength();
//                data = packet.getData();
//
//                setRessourceAmmount(data);
//                CanBefehlRaw canRaw = null;
//                UdpPackage udpP = new UdpPackage(packet, pkNr, mills);
//                try {
//                    canRaw = new CanBefehlRaw(udpP);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                if (canRaw.isResponse()) {
//                    listen = false;
//                    socket.close();
//                    //dsReceive.close();
//                }

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

    public int getRessourceAmmount() {
        return ressourceAmmountInt;
    }

    public int parseHex2Int(byte[] data) {
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
    public static StringBuilder hexEncode(byte[] buf, StringBuilder sb) {
        for (byte b : buf) {
            sb.append(String.format("%02x", b));
        }
        return sb;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }


}