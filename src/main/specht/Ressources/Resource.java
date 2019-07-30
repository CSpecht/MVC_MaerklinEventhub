package specht.Ressources;

import specht.General.Attribute;
import specht.General.CanBefehlRaw;
import specht.General.ConstructCANFrame;
import specht.General.UdpPackage;
import specht.connect.UdpConnectionResponse;

import javax.swing.text.MaskFormatter;
import java.io.IOException;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Resource extends Thread {
    protected String ip;
    protected int port;
    protected Socket tcp_socket = null;
    protected boolean stop = false;
    protected Date startTime = null;
    protected String dataset = "";
    protected String resource = "water";
    protected String resultCSV = "";
    protected String resultJSON = "";


    protected byte[] data = new byte[13];
    protected byte[] dataSand = new byte[13];
    protected byte[] dataCoil = new byte[13];
    protected byte[] dataWater = new byte[13];


    protected static int coaches;
    protected static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public LinkedList<String> payload = new LinkedList<String>();
    protected ArrayList<String> SQLstment = new ArrayList<String>();
    public LinkedList<String> jsonPayload = new LinkedList<String>();

    boolean debug = true;
    AtomicInteger ressourceAmmountIntWater = new AtomicInteger();
    AtomicInteger ressourceAmmountIntCoil = new AtomicInteger();
    AtomicInteger ressourceAmmountIntSand = new AtomicInteger();

    boolean listen = true;
    DatagramSocket ds;
    DatagramSocket dr;

    InetAddress ia;
    InetAddress ib;
    String ThreadName = "";

    //DatagramSocket sending;
    //DatagramSocket dsReceive;


    UdpConnectionResponse udpConnect;


    {
      /*
              try {
            ia = InetAddress.getByName(Attribute.sendingAddress);
            ib = InetAddress.getByName(Attribute.receivingAddress);
             } catch (UnknownHostException e) {
                 e.printStackTrace();
            }
            */
    }


    public Resource(DatagramSocket sending, DatagramSocket receiving, InetAddress ia, InetAddress ib, String name) {
        this.ds = sending;
        this.dr = receiving;
        this.ia = ia;
        this.ib = ib;
        this.ThreadName = name;


        /* try {
            //sending = new DatagramSocket(Attribute.sendingPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }*/
    }


    /*   boolean stop;
    public ResourceWATER(String ip, int port, boolean stop) {
        super(ip, port, stop);
    }
*/

    public byte[] getDataWater() {
        return dataWater;
    }

    public byte[] getDataCoil() {
        return dataCoil;
    }

    public byte[] getDataSand() {
        return dataSand;
    }

    public void setDataWater(byte[] data) {
        this.dataWater = data;
    }

    public void setDataCoil(byte[] data) {
        this.dataCoil = data;
    }

    public void setDataSand(byte[] data) {
        this.dataSand = data;
    }


    public Resource(String ip, int port, boolean stop) throws UnknownHostException {
    }

    public Resource(String ip, int port) throws UnknownHostException {
        this.ip = ip;
        this.port = port;
    }


    public void run() {


        int i = 0;
       // while (!ds.isClosed() && !dr.isClosed()) {
            LinkedList<String> newpayload = new LinkedList<String>();
            long now = new Date().getTime();
            try {
                Thread.sleep(1000);
                dataWater = this.getResource("water");
                Thread.sleep(1000);
                dataCoil = this.getResource("coil");
                Thread.sleep(1000);
                dataSand = this.getResource("sand");

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            long end = new Date().getTime() - now;
            newpayload.add(transformData2CSV(dataWater));
            newpayload.add(transformData2CSV(dataCoil));
            newpayload.add(transformData2CSV(dataSand));



               /* try {
                    data = this.getResource("round");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
*/
            //sendToMSSQL(newpayload);
            i++;

            //Thread.sleep(2000);
        //
        // }


    }

    public String transformData2CSV(byte[] df) {
        String byteStream = Base64.getEncoder().encodeToString(df);

        String hexFormatted = "";
        String hexNr = "";
        String csvFormatted = "";
        MaskFormatter mfHEX = null;
        MaskFormatter mfCSV = null;
        try {
            mfHEX = new MaskFormatter("[HHHHHHHH:HH][HH,HH,HH,HH,HH,HH,HH,HH]");
            mfHEX.setValueContainsLiteralCharacters(false);
            hexNr = hexEncode(df); //"00" +
            hexFormatted = mfHEX.valueToString(hexNr);

            mfCSV = new MaskFormatter("HH;HH;HH;HH;HH;HH;HH;HH;HH;HH;HH;HH;HH");
            mfCSV.setValueContainsLiteralCharacters(false);
            csvFormatted = mfCSV.valueToString(hexNr);

        } catch (ParseException e) {
            e.printStackTrace();
        }

/*        if (debug) {
            System.out.println("hexFo: " + hexFormatted);
            System.out.println("hexNr:  " + hexNr);
            System.out.println("heCSV:  " + csvFormatted);
            System.out.println(byteStream);
        }
*/
        //csvPayload.add(byteStream);
        return csvFormatted;
    }

    public void sendToMSSQL(LinkedList payload) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // create DateFormatter for the right format of date for SQLServer.
        DateFormat sdf = new SimpleDateFormat(Attribute.DATEFORMAT);
        Date date = new Date();

        try (Connection con = DriverManager.getConnection(Attribute.dbUrl); Statement stmt = con.createStatement();) {
            for (int i = 0; i < payload.size(); i++) {
                String SQL = "INSERT INTO [dbo].[T_RESOURCES_USAGE_DATASET] ([DATATYPE], [RECORDING_START_TIME], "
                        + "[TIME_STAMP], [DATASET], [DELIMITER])"
                        + "VALUES ('" + Attribute.sqlDataType + "','"
                        + sdf.format(date).toString() + "','"
                        + sdf.format(date).toString() + "','"
                        + payload.get(i)
                        + "', ';')";

                if (debug) {
                    System.out.println("SQL: " + SQL);
                }

                //ResultSet rs =
                try {
                    stmt.executeUpdate(SQL);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*   public void closeConnection () {
           try {
               getDatagramSocketSending().close();
               getDatagramSocketReceiving().close();
           } catch (SocketException e) {
               e.printStackTrace();
           }

       }
   */
    public void setResource() {

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

    protected HashMap<String, String> translateToHashMap(String hex) {
        HashMap<String, String> map = new HashMap<String, String>();
        String metaData = hex.substring(0, 8);
        String data = hex.substring(9, 19);
        map.put(metaData, data);
        return map;
    }

    public void closeConn() {

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
    public void stopListener() {

        stop = true;
    }

    public byte[] getResource(String i) throws IOException, ParseException {

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
//            case "round":
//                data = GetRound();
//                return data;
        }
        return null;
    }


    public void setWater() throws IOException {
        boolean result = false;
        byte[] udpFrame = new byte[13];
        //byte[] packatData;
        //ds = new DatagramSocket(Attribute.sendingPort);
        //dsReceive = new DatagramSocket(Attribute.receivePort);

        //GET COIL
        udpFrame = ConstructCANFrame.setWater(Attribute._STEAM_ID);

        DatagramPacket sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
        ds.send(sendPacket);
    }

    public void setCoil() throws IOException {
        boolean result = false;
        byte[] udpFrame = new byte[13];
        //byte[] packatData;
        //ds = new DatagramSocket(Attribute.sendingPort);
        //dsReceive = new DatagramSocket(Attribute.receivePort);

        //GET COIL
        udpFrame = ConstructCANFrame.setCoil(Attribute._STEAM_ID);

        DatagramPacket sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
        ds.send(sendPacket);
    }

    public void setSand() throws IOException {
        boolean result = false;
        byte[] udpFrame = new byte[13];
        //byte[] packatData;
        //ds = new DatagramSocket(Attribute.sendingPort);
        //dsReceive = new DatagramSocket(Attribute.receivePort);

        //GET COIL
        udpFrame = ConstructCANFrame.setSand(Attribute._STEAM_ID);

        DatagramPacket sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
        ds.send(sendPacket);
    }

    public byte[] GetWater() throws IOException {

        boolean result = false;
        byte[] udpFrame = new byte[13];
        //byte[] packatData;
        //ds = new DatagramSocket(Attribute.sendingPort);
        //dsReceive = new DatagramSocket(Attribute.receivePort);

        //GET COIL
        udpFrame = ConstructCANFrame.getWater(Attribute._STEAM_ID);

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
            dr.receive(sendPacket);

            //dsReceive.receive(sendPacket);
            // Empfaenger auslesen
            InetAddress address = sendPacket.getAddress();

            int port = sendPacket.getPort();
            int len = sendPacket.getLength();
            data = sendPacket.getData();
//            for (int j = 0; j < data.length; j++) {
//                System.out.println("data[" + j + "]: " + data[j]);
//            }
            CanBefehlRaw canRaw = null;
            UdpPackage udpP = new UdpPackage(sendPacket, pkNr, mills);
            try {
                canRaw = new CanBefehlRaw(udpP);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (canRaw.isResponse() && data[7] == Attribute.firstByteOfSteamID && data[8] == Attribute.secondByteOfSteamID
                && data[9] == 4) {
                setResourceAmountWater(data);
                empfang = false;

                //ds.close();
                // dsReceive.close();
            }

            i++;
        }

        if (debug) {
            MaskFormatter mfHEX = null;
            String hexFormatted = "";
            String hexNr = "";

            //DEBUG UDP FRAME
            /*
            System.out.println("GETWATER():");
            for (int j = 0; j < udpFrame.length; j++) {
                System.out.println("udpFrame[" + j + "]: " + udpFrame[j]);
            }
            */
            try {
                mfHEX = new MaskFormatter("[HHHHHHHH:HH][HH,HH,HH,HH,HH,HH,HH,HH]");
                mfHEX.setValueContainsLiteralCharacters(false);
                hexNr = hexEncode(data);
                hexFormatted = mfHEX.valueToString(hexNr);
                System.out.println("hexWater: " + hexFormatted);
                System.out.println("Thread: " + this.ThreadName + " : intAmountWater: " + getResourceAmountWater());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return data;
    }
    public byte[] GetCoil() throws IOException {

        boolean result = false;
        byte[] udpFrame = new byte[13];
        //byte[] packatData;
        //ds = new DatagramSocket(Attribute.sendingPort);
        //dsReceive = new DatagramSocket(Attribute.receivePort);

        //GET COIL
        udpFrame = ConstructCANFrame.getCoil(Attribute._STEAM_ID);
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
            dr.receive(sendPacket);
            // Empfaenger auslesen
            InetAddress address = sendPacket.getAddress();

            int port = sendPacket.getPort();
            int len = sendPacket.getLength();
            data = sendPacket.getData();

            CanBefehlRaw canRaw = null;
            UdpPackage udpP = new UdpPackage(sendPacket, pkNr, mills);
            try {
                canRaw = new CanBefehlRaw(udpP);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (canRaw.isResponse() && data[7] == Attribute.firstByteOfSteamID && data[8] == Attribute.secondByteOfSteamID
                    && data[9] == 8) {
                setResourceAmountCoil(data);
                empfang = false;

                //ds.close();
                // dsReceive.close();
            }

            i++;
        }

        if (debug) {
            MaskFormatter mfHEX = null;
            String hexFormatted = "";
            String hexNr = "";
            //UDP FRAME for GETCoil
            //System.out.println("GETCOIL():");
            //for (int j = 0; j < udpFrame.length; j++) {
            //System.out.println("udpFrame[" + j + "]: " + udpFrame[j]);
            //}

            try {
                mfHEX = new MaskFormatter("[HHHHHHHH:HH][HH,HH,HH,HH,HH,HH,HH,HH]");
                mfHEX.setValueContainsLiteralCharacters(false);
                hexNr = hexEncode(data);
                hexFormatted = mfHEX.valueToString(hexNr);
                System.out.println("hexCoil: " + hexFormatted);
                System.out.println("Thread: " + this.ThreadName + " : intAmountCoil: " + getResourceAmountCoil());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    public byte[] GetSand() throws IOException {

        boolean result = false;
        byte[] udpFrame = new byte[13];
        //byte[] packatData;
        //ds = new DatagramSocket(Attribute.sendingPort);
        //dsReceive = new DatagramSocket(Attribute.receivePort);

        //GET COIL
        udpFrame = ConstructCANFrame.getSand(Attribute._STEAM_ID);

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
            dr.receive(sendPacket);
            // Empfaenger auslesen
            InetAddress address = sendPacket.getAddress();

            int port = sendPacket.getPort();
            int len = sendPacket.getLength();
            data = sendPacket.getData();

            CanBefehlRaw canRaw = null;
            UdpPackage udpP = new UdpPackage(sendPacket, pkNr, mills);
            try {
                canRaw = new CanBefehlRaw(udpP);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (canRaw.isResponse() && data[7] == Attribute.firstByteOfSteamID && data[8] == Attribute.secondByteOfSteamID
                    && data[9] == 12) {
                setResourceAmountSand(data);
                empfang = false;
                //ds.close();
                // dsReceive.close();
            }

            i++;
        }

        if (debug) {
            MaskFormatter mfHEX = null;
            String hexFormatted = "";
            String hexNr = "";
            //DEBUG UDP FRAME
            /*
            System.out.println("GETSand():");
            for (int j = 0; j < udpFrame.length; j++) {
                System.out.println("udpFrame[" + j + "]: " + udpFrame[j]);
            }
            */
            try {
                mfHEX = new MaskFormatter("[HHHHHHHH:HH][HH,HH,HH,HH,HH,HH,HH,HH]");
                mfHEX.setValueContainsLiteralCharacters(false);
                hexNr = hexEncode(data);
                hexFormatted = mfHEX.valueToString(hexNr);
                System.out.println("hexSand: " + hexFormatted);
                System.out.println("Thread: " + this.ThreadName + " : intAmountSand: " + getResourceAmountSand());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return data;
    }




    public synchronized void setResourceAmountWater(byte[] data) {
        ressourceAmmountIntWater.set(parseHex2Int(data));
    }

    public synchronized AtomicInteger getResourceAmountWater() {
        return ressourceAmmountIntWater;
    }

    public synchronized void setResourceAmountCoil(byte[] data) {
        ressourceAmmountIntCoil.set(parseHex2Int(data));
    }

    public synchronized AtomicInteger getResourceAmountCoil() {
        return ressourceAmmountIntCoil;
    }

    public synchronized void setResourceAmountSand(byte[] data) {
        ressourceAmmountIntSand.set(parseHex2Int(data));
    }

    public synchronized AtomicInteger getResourceAmountSand() {
        return ressourceAmmountIntSand;
    }


    public synchronized int parseHex2Int(byte[] data) {
        byte b2 = -100;
        int r = 0;
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