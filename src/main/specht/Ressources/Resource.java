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
    protected byte[] dataSand = new byte[13];
    protected byte[] dataCoil = new byte[13];
    protected byte[] dataWater = new byte[13];


    protected static int coaches;
    protected static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public LinkedList<String> payload = new LinkedList<String>();
    protected ArrayList<String> SQLstment = new ArrayList<String>();
    public LinkedList<String> jsonPayload = new LinkedList<String>();

    boolean debug = true;
    int ressourceAmmountIntWater;
    int ressourceAmmountIntCoil;
    int ressourceAmmountIntSand;

    boolean listen = true;
    static DatagramSocket ds;
    static DatagramSocket dr;

    public DatagramSocket getDatagramSocketSending () throws SocketException {
        if (ds == null) {
            ds = new DatagramSocket(Attribute.sendingPort);
        }
        return ds;
    }
    public DatagramSocket getDatagramSocketReceiving () throws SocketException {
        if (dr == null) {
            dr = new DatagramSocket(Attribute.receivePort);
        }
        return dr;
    }




    //DatagramSocket sending;
    //DatagramSocket dsReceive;

    InetAddress ia;
    InetAddress ib;
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


    public Resource() {
        try {
            ds = getDatagramSocketSending();
            dr = getDatagramSocketReceiving();
            ia = InetAddress.getByName(Attribute.sendingAddress);
            ib = InetAddress.getByName(Attribute.receivingAddress);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        try {
            this.run();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


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

    public synchronized byte[] getDataWater () {return dataWater;}
    public synchronized byte[] getDataCoil () {return dataCoil;}
    public synchronized byte[] getDataSand () {return dataSand;}

    public void setDataWater (byte[] data) {this.dataWater = data;}
    public void setDataCoil (byte[] data) {this.dataCoil = data;}
    public void setDataSand(byte[] data) {this.dataSand = data;}


    public Resource(String ip, int port, boolean stop) throws UnknownHostException {}

    public Resource(String ip, int port) throws UnknownHostException {
        this.ip = ip;
        this.port = port;
    }


    public void run() throws IOException, ParseException, InterruptedException {



            int i = 0;
            while(!ds.isClosed() && !dr.isClosed()) {
                LinkedList<String> newpayload = new LinkedList<String>();
                try {
                    long now = new Date().getTime();

                    Thread.sleep(2000);
                    long end = new Date().getTime() - now;
                    if (debug) {
                        System.out.println("now: " + now);
                        System.out.println("end: " + end);
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

/*
                dataWater = this.getResource("water");
                newpayload.add(transformData2CSV(dataWater));
                dataSand = this.getResource("sand");
                newpayload.add(transformData2CSV(dataSand));
                dataCoil = this.getResource("coil");
                newpayload.add(transformData2CSV(dataCoil));
*/
                data = this.getResource("round");

                //sendToMSSQL(newpayload);
                i++;

                //Thread.sleep(2000);
            }


    }

    public String transformData2CSV(byte[] df) {
        String byteStream  = Base64.getEncoder().encodeToString(df);
        System.out.println(byteStream);
        String hexFormatted ="";
        String hexNr = "";
        String csvFormatted = "";
        MaskFormatter mfHEX = null;
        MaskFormatter mfCSV = null;
        try {
            mfHEX = new MaskFormatter("[HHHHHHHH:HH][HH,HH,HH,HH,HH,HH,HH,HH]");
            mfHEX.setValueContainsLiteralCharacters(false);
            hexNr =  hexEncode(df); //"00" +
            hexFormatted = mfHEX.valueToString(hexNr);

            mfCSV = new MaskFormatter("HH;HH;HH;HH;HH;HH;HH;HH;HH;HH;HH;HH;HH");
            mfCSV.setValueContainsLiteralCharacters(false);
            csvFormatted = mfCSV.valueToString(hexNr);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (debug) {
            System.out.println("hexFo: " + hexFormatted);
            System.out.println("hexNr:  " + hexNr);
            System.out.println("heCSV:  " + csvFormatted);
        }

        //csvPayload.add(byteStream);
        return csvFormatted;
    }

    public void sendToMSSQL(LinkedList payload ) {
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



    public void closeConnection () {
        try {
            getDatagramSocketSending().close();
            getDatagramSocketReceiving().close();
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    public void setResource()  {

    }

    protected synchronized static String hexEncode(byte[] buf) {
        return hexEncode(buf, new StringBuilder()).toString();
    }

    //Encoding byte[] into hexadecimal Number, RETURN STRINGBUILDER
    public synchronized static StringBuilder hexEncode (byte[] buf, StringBuilder sb) {
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

    public synchronized byte[] getResource (String i) throws IOException, ParseException {

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
            case "round":
                data = GetRound();
                return data;
        }
        return null;
    }

    public byte[] GetRound() throws IOException {
        boolean result = false;
        byte[] udpFrame = new byte[13];
        //byte[] packatData;
        //ds = new DatagramSocket(Attribute.sendingPort);
        //dsReceive = new DatagramSocket(Attribute.receivePort);

        //GET COIL
        udpFrame = ConstructCANFrame.getRound();
        DatagramPacket sendPacket= new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
        ds.send(sendPacket);


        boolean empfang = true;
        long timestamp = new Date().getTime();

        int i = 0;
  /*      while (empfang) {
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
            setRessourceAmmountWater(data);
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
                // dsReceive.close();
            }

            i++;
        }

        */

        if (debug) {
            MaskFormatter mfHEX = null;
            String hexFormatted = "";
            String hexNr = "";

            //DEBUG UDP FRAME

            System.out.println("GETROUND():");
            for (int j = 0; j < udpFrame.length; j++) {
                System.out.println("udpFrame[" + j + "]: " + udpFrame[j]);
            }

            try {
                mfHEX = new MaskFormatter("[HHHHHHHH:HH][HH,HH,HH,HH,HH,HH,HH,HH]");
                mfHEX.setValueContainsLiteralCharacters(false);
                hexNr = hexEncode(udpFrame);
                hexFormatted = mfHEX.valueToString(hexNr);
                System.out.println("hexRound: " + hexFormatted);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    public synchronized byte[] GetWater() throws IOException {

        boolean result = false;
        byte[] udpFrame = new byte[13];
        //byte[] packatData;
        //ds = new DatagramSocket(Attribute.sendingPort);
        //dsReceive = new DatagramSocket(Attribute.receivePort);

        //GET COIL
        udpFrame = ConstructCANFrame.getWater(Attribute._STEAM_ID);

        if (debug) {

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
            dr.receive(sendPacket);

            //dsReceive.receive(sendPacket);
            // Empfaenger auslesen
            InetAddress address = sendPacket.getAddress();

            int port = sendPacket.getPort();
            int len = sendPacket.getLength();
            data = sendPacket.getData();
            setRessourceAmmountWater(data);
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
                System.out.println("intAmmountWater: " + getRessourceAmmountWater());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return data;
    }


    public synchronized byte[] GetSand() throws IOException {

        boolean result = false;
        byte[] udpFrame = new byte[13];
        //byte[] packatData;
        //ds = new DatagramSocket(Attribute.sendingPort);
        //dsReceive = new DatagramSocket(Attribute.receivePort);

        //GET COIL
        udpFrame = ConstructCANFrame.getSand(Attribute._STEAM_ID);

        if (debug) {

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
            dr.receive(sendPacket);
            // Empfaenger auslesen
            InetAddress address = sendPacket.getAddress();

            int port = sendPacket.getPort();
            int len = sendPacket.getLength();
            data = sendPacket.getData();
            setRessourceAmmountSand(data);
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
                System.out.println("intAmmountSand: " + getRessourceAmmountSand());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return data;
    }


    public synchronized byte[] GetCoil() throws IOException {

        boolean result = false;
        byte[] udpFrame = new byte[13];
        //byte[] packatData;
        //ds = new DatagramSocket(Attribute.sendingPort);
        //dsReceive = new DatagramSocket(Attribute.receivePort);

        //GET COIL
        udpFrame = ConstructCANFrame.getCoil(Attribute._STEAM_ID);

        if (debug) {

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
            dr.receive(sendPacket);
            // Empfaenger auslesen
            InetAddress address = sendPacket.getAddress();

            int port = sendPacket.getPort();
            int len = sendPacket.getLength();
            data = sendPacket.getData();
            setRessourceAmmountCoil(data);
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
                System.out.println("intAmmountCoil: " + getRessourceAmmountCoil());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    public synchronized void setRessourceAmmountWater(byte[] data) {
        ressourceAmmountIntWater = parseHex2Int(data);
    }

    public synchronized int getRessourceAmmountWater () {
        return ressourceAmmountIntWater;
    }

    public synchronized void setRessourceAmmountCoil(byte[] data) {
        ressourceAmmountIntCoil = parseHex2Int(data);
    }

    public synchronized int getRessourceAmmountCoil () {
        return ressourceAmmountIntCoil;
    }
    public synchronized void setRessourceAmmountSand(byte[] data) {
        ressourceAmmountIntSand = parseHex2Int(data);
    }

    public synchronized int getRessourceAmmountSand () {
        return ressourceAmmountIntSand;
    }


    public synchronized int parseHex2Int (byte[] data ) {
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