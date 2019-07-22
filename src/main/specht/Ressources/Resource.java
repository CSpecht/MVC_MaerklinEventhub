package specht.Ressources;

import specht.General.Attribute;
import specht.General.CanBefehlRaw;
import specht.General.ConstructCANFrame;
import specht.General.UdpPackage;
import specht.connect.UdpConnectionResponse;
import specht.connect.UdpConnectionSend;

import javax.swing.text.MaskFormatter;
import java.io.IOException;
import java.net.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

public class Resource {
    protected String ip;
    protected int port;
    protected Socket tcp_socket = null;
    protected boolean stop = false;
    protected Date startTime = null;
    protected String dataset = "";
    protected String resource = "water";
    protected String resultCSV = "";
    protected String resultJSON = "";
    protected int RoundCount = 0;
    protected byte[] data = new byte[13];
    protected static int coaches;
    protected static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public LinkedList<String> payload = new LinkedList<String>();
    protected ArrayList<String> SQLstment = new ArrayList<String>();
    public LinkedList<String> jsonPayload = new LinkedList<String>();

    boolean debug = false;
    int ressourceAmmountInt;

    DatagramSocket sending;
    DatagramSocket dsReceive;

    InetAddress ia;
    InetAddress ib;
    UdpConnectionResponse udpConnect;


    {
        try {
            ia = InetAddress.getByName(Attribute.sendingAddress);
            ib = InetAddress.getByName(Attribute.receivingAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }



    public Resource() {
        try {
            sending = new DatagramSocket(Attribute.sendingPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }


    /*   boolean stop;
    public ResourceWATER(String ip, int port, boolean stop) {
        super(ip, port, stop);
    }
*/



    public Resource(String ip, int port, boolean stop) throws UnknownHostException {}

    public Resource(String ip, int port) throws UnknownHostException {
        this.ip = ip;
        this.port = port;
    }


   /* public void run() {
        try {
            this.conn();
        } catch (IOException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }*/

    public void setResource()  {

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

    protected HashMap<String,String> translateToHashMap (String hex) {
        HashMap<String, String> map = new HashMap<String, String>();
        String metaData = hex.substring(0,8);
        String data = hex.substring(9,19);
        map.put(metaData, data);
        return map;
    }
    public void closeConn () {

        try {
            tcp_socket.shutdownInput();
            tcp_socket.close();

        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    //it starts conn()
    public void startListener() {

        stop = false;
    }

    //it stops conn()
    public void stopListener(){

        stop = true;
    }

    public byte[] getResource (String i) throws IOException, ParseException {

        switch (i) {
            case "water":

                data = GetWater();
                return data;
                //break;
            case "coil":
                //ResourceCOIL c_resource = new ResourceCOIL();//c_resource.getCoil();
                data = GetCoil();
                return data;
                //break;
            case "sand":
//                ResourceSAND s_resource = new ResourceSAND();//s_resource.getSand();
                data = GetSand();
                return data;
                //break;
        }
        return null;
    }

    public byte[] GetWater() throws IOException {

        boolean result = false;
        byte[] udpFrame = new byte[13];
        byte[] packatData;

        udpFrame = ConstructCANFrame.getWater(Attribute._STEAM_ID);

        UdpConnectionSend udpSend = new UdpConnectionSend(udpFrame);

        //DatagramPacket dpPacket = new DatagramPacket(udpFrame, udpFrame.length,ia,Attribute.sendingPort);

        //sending.send(dpPacket);

        UdpConnectionResponse udp = new UdpConnectionResponse();
        udp.start();
        //Cs3Connection cs3 = new Cs3Connection();
        //cs3.start();


        //Cs3Connection cs3 = udpConnect.getVerbindung();
        //TcpConnection tcp = new TcpConnection();
        //UdpConnectionResponse udp = new UdpConnectionResponse();
        //DatagramPacket dmPacket = new DatagramPacket(udpFrame,udpFrame.length,ia,Attribute.sendingPort);
       // UdpPackage udpP = new UdpPackage(dmPacket,0,new Date().getTime());

       // udp.doSend(udpP,dmPacket);


        //udpConnect.doSend(udpP,dmPacket);
       // ds = new DatagramSocket(Attribute.sendingPort);
       // dsReceive = new DatagramSocket(Attribute.receivePort);

        //GET WATER


        //TcpConnection tcpConnect = new TcpConnection();
        //tcpConnect.start();

        //tcpConnect.doSend(udpPackage, );



        if(debug) {
            System.out.println("GETWATER():");
            for (int i = 0; i < udpFrame.length; i++) {
                System.out.println("udpFrame[" + i + "]: " + udpFrame[i]);
            }
        }



 /*       DatagramPacket sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
        //ds.send(sendPacket);
        UdpPackage udpP = new UdpPackage(sendPacket,0,new Date().getTime());
        cs3.doSend(udpP,sendPacket);

        boolean empfang = true;
        long timestamp = new Date().getTime();
*//*
        int i = 0;
        while (empfang) {
            Date now = new Date();
            long mills = now.getTime();
            int pkNr = 1;

            sendPacket = new DatagramPacket(new byte[13], 13, ib, Attribute.receivePort);
            dsReceive.receive(sendPacket);
            // Empfaenger auslesen
            InetAddress address = sendPacket.getAddress();

            int port = sendPacket.getPort();
            int len = sendPacket.getLength();
            data = sendPacket.getData();

            setRessourceAmmount(data);
            //System.out.println("ressourceAmmount" + ressourceAmmountInt);


            CanBefehlRaw canRaw = null;
            UdpPackage udpP = new UdpPackage(sendPacket,pkNr,mills);
            try {
                canRaw = new CanBefehlRaw(udpP);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (canRaw.isResponse()) {
                empfang = false;
                ds.close();
                dsReceive.close();
            }

            i++;
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
                System.out.println("hexWater: " + hexFormatted);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
   */     return data;
    }

    public byte[] GetSand() throws IOException {
        boolean result = false;
        byte[] udpFrame = new byte[13];
        byte[] packatData;

        udpFrame = ConstructCANFrame.getSand(Attribute._STEAM_ID);

        UdpConnectionSend udpSend = new UdpConnectionSend(udpFrame);
        //DatagramPacket dpPacket = new DatagramPacket(udpFrame, udpFrame.length,ia,Attribute.sendingPort);

        //sending.send(dpPacket);

        UdpConnectionResponse udp = new UdpConnectionResponse();
        udp.start();

        /*
        boolean result = false;
        byte[] udpFrame = new byte[13];
        byte[] packatData;
        ds = new DatagramSocket(Attribute.sendingPort);
        dsReceive = new DatagramSocket(Attribute.receivePort);

        //GET SAND
        udpFrame = ConstructCANFrame.getSand(Attribute._STEAM_ID);

        if (debug) {
            System.out.println("GETSAND():");
            for (int i = 0; i < udpFrame.length; i++) {
                System.out.println("udpFrame[" + i + "]: " + udpFrame[i]);
            }
        }
        DatagramPacket sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
        ds.send(sendPacket);

        boolean empfang = true;
        long timestamp = new Date().getTime();

        int i = 0;
        while (empfang) {
            Date now = new Date();
            long mills = now.getTime();
            int pkNr = 1;

            sendPacket = new DatagramPacket(new byte[13], 13, ib, Attribute.receivePort);
            dsReceive.receive(sendPacket);

            // Empfaenger auslesen
            InetAddress address = sendPacket.getAddress();
            int port = sendPacket.getPort();
            int len = sendPacket.getLength();
            data = sendPacket.getData();

            setRessourceAmmount(data);
            CanBefehlRaw canRaw = null;
            UdpPackage udpP = new UdpPackage(sendPacket,pkNr,mills);
            try {
                canRaw = new CanBefehlRaw(udpP);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (canRaw.isResponse()) {
                empfang = false;
                ds.close();
                dsReceive.close();
            }

            i++;
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
                System.out.println("hexSand: " + hexFormatted);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
*/
        return data;


    }

    public byte[] GetCoil() throws IOException {

        boolean result = false;
        byte[] udpFrame = new byte[13];
        //byte[] packatData;
        //ds = new DatagramSocket(Attribute.sendingPort);
        dsReceive = new DatagramSocket(Attribute.receivePort);

        //GET COIL
        udpFrame = ConstructCANFrame.getCoil(Attribute._STEAM_ID);

        if (debug) {
            System.out.println("GETCOIL():");
            for (int i = 0; i < udpFrame.length; i++) {
                System.out.println("udpFrame[" + i + "]: " + udpFrame[i]);
            }
        }
        DatagramPacket sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
        //ds.send(sendPacket);

        boolean empfang = true;
        long timestamp = new Date().getTime();

        int i = 0;
        while (empfang) {
            Date now = new Date();
            long mills = now.getTime();
            int pkNr = 1;

            sendPacket = new DatagramPacket(new byte[13], 13, ib, Attribute.receivePort);
            dsReceive.receive(sendPacket);
            // Empfaenger auslesen
            InetAddress address = sendPacket.getAddress();

            int port = sendPacket.getPort();
            int len = sendPacket.getLength();
            data = sendPacket.getData();
            setRessourceAmmount(data);
            CanBefehlRaw canRaw = null;
            UdpPackage udpP = new UdpPackage(sendPacket,pkNr,mills);
            try {
                canRaw = new CanBefehlRaw(udpP);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (canRaw.isResponse()) {
                empfang = false;
                //ds.close();
                dsReceive.close();
            }

            i++;
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
                System.out.println("hexCoil: " + hexFormatted);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return data;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}