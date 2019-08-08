package specht.Ressources;

import specht.General.Attribute;
import specht.General.CanBefehlRaw;
import specht.General.ConstructCANFrame;
import specht.General.UdpPackage;
import specht.connect.UdpConnectionResponse;

import javax.swing.text.MaskFormatter;
import java.io.IOException;
import java.net.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Resource extends Thread {
//    protected String ip;
//    protected int port;
//    protected Socket tcp_socket = null;
    protected boolean stop = false;
//    protected Date startTime = null;
//    protected String dataset = "";
//    protected String resource = "water";
//    protected String resultCSV = "";
//    protected String resultJSON = "";


    protected byte[] data = new byte[13];
    protected byte[] dataSand = new byte[13];
    protected byte[] dataCoil = new byte[13];
    protected byte[] dataWater = new byte[13];
    protected byte[] dataSpeed = new byte[13];


    protected static int coaches;
    protected static final DateFormat sdf = new SimpleDateFormat(Attribute.DATEFORMAT);
    public LinkedList<String> payload = new LinkedList<String>();
    //protected ArrayList<String> SQLstment = new ArrayList<String>();
    public LinkedList<String> jsonPayload = new LinkedList<String>();

    boolean debugSpeed = true;
    boolean debug = false;

    AtomicInteger ressourceAmmountIntWater = new AtomicInteger();
    AtomicInteger ressourceAmmountIntCoil = new AtomicInteger();
    AtomicInteger ressourceAmmountIntSand = new AtomicInteger();
    AtomicInteger speedAmount = new AtomicInteger();

    boolean listen = true;
    DatagramSocket ds, dr;
    InetAddress ia, ib;

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

/*
    public Resource(String ip, int port, boolean stop) throws UnknownHostException {
    }

    public Resource(String ip, int port) throws UnknownHostException {
        this.ip = ip;
        this.port = port;
    }
*/

    public void run() {


        int i = 0;
       // while (!ds.isClosed() && !dr.isClosed()) {
           //LinkedList<String> newpayload = new LinkedList<String>();
            long now = new Date().getTime();
            try {
                Thread.sleep(1000);
                dataWater = this.getResource("water");
                Thread.sleep(1000);
                dataCoil = this.getResource("coil");
                Thread.sleep(1000);
                dataSand = this.getResource("sand");
                Thread.sleep(1000);
                dataSpeed = this.getResource("speed");


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

          /*  long end = new Date().getTime() - now;
            newpayload.add(transformData2CSV(dataWater));
            newpayload.add(transformData2CSV(dataCoil));
            newpayload.add(transformData2CSV(dataSand));
        */

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
/*
    protected HashMap<String, String> translateToHashMap(String hex) {
        HashMap<String, String> map = new HashMap<String, String>();
        String metaData = hex.substring(0, 8);
        String data = hex.substring(9, 19);
        map.put(metaData, data);
        return map;
    }
*/
/*    public void closeConn() {

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
*/
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
           case "speed":
                data = GetSpeed();
                return data;
        }
        return null;
    }


    public void setWater() throws IOException, InterruptedException {
        boolean result = false;
        byte[] udpFrame = new byte[13];

        //Say system that the refill will start()
//        udpFrame = ConstructCANFrame.resourceStart();
//        DatagramPacket sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
//        ds.send(sendPacket);

        //SET WATER
        udpFrame = ConstructCANFrame.setWater(Attribute._STEAM_ID);
        DatagramPacket sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
        ds.send(sendPacket);

        //GET WATER for Verification
//        GetWater();

        //Say system that the refill will stop()
//        udpFrame = ConstructCANFrame.resourceStop();
//        sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
//        ds.send(sendPacket);

//        Thread.sleep(5000);

//        udpFrame = ConstructCANFrame.setSpeed(Attribute._STEAM_ID, 1000);
//        sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
//        ds.send(sendPacket);
//
//        udpFrame = ConstructCANFrame.go();
//        sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
//        ds.send(sendPacket);
    }

    public void setCoil() throws IOException, InterruptedException {
        boolean result = false;
        byte[] udpFrame = new byte[13];

        //Say system that the refill will start()
//        udpFrame = ConstructCANFrame.resourceStart();
//        DatagramPacket sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
//        ds.send(sendPacket);

        //SET COIL
        udpFrame = ConstructCANFrame.setCoil(Attribute._STEAM_ID);
        DatagramPacket sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
        ds.send(sendPacket);

        //GET COIL for Verification
//        GetCoil();

        //Say system that the refill will stop()
//        udpFrame = ConstructCANFrame.resourceStop();
//        sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
//        ds.send(sendPacket);

//        Thread.sleep(5000);

//        udpFrame = ConstructCANFrame.setSpeed(Attribute._STEAM_ID, 1000);
//        sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
//        ds.send(sendPacket);
//
//        udpFrame = ConstructCANFrame.go();
//        sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
//        ds.send(sendPacket);
    }

    public void setSand() throws IOException, InterruptedException {
        boolean result = false;
        byte[] udpFrame = new byte[13];

        //Say system that the refill will start()
//        udpFrame = ConstructCANFrame.resourceStart();
//        DatagramPacket sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
//        ds.send(sendPacket);

        //SET SAND
        udpFrame = ConstructCANFrame.setSand(Attribute._STEAM_ID);
        DatagramPacket sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
        ds.send(sendPacket);

        //GET SAND for Verification
//        GetSand();

        //Say system that the refill will stop()
//        udpFrame = ConstructCANFrame.resourceStop();
//        sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
//        ds.send(sendPacket);

//        Thread.sleep(5000);

//        udpFrame = ConstructCANFrame.setSpeed(Attribute._STEAM_ID, 1000);
//        sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
//        ds.send(sendPacket);

//        udpFrame = ConstructCANFrame.go();
//        sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
//        ds.send(sendPacket);
    }

    public byte[] GetWater() throws IOException {

        boolean result = false;
        byte[] udpFrame = new byte[13];

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

            data = sendPacket.getData();
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
            }

            i++;
        }

        if (debug) {
            String hexOutput = convertByte2String(data);
            System.out.println(hexOutput);
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
            String hexOutput = convertByte2String(data);
            System.out.println(hexOutput);
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
            String hexOutput = convertByte2String(data);
            System.out.println(hexOutput);
        }

        return data;
    }

    public byte[] GetSpeed() throws IOException {
        boolean result = false;
        byte[] udpFrame = new byte[13];
        //byte[] packatData;
        //ds = new DatagramSocket(Attribute.sendingPort);
        //dsReceive = new DatagramSocket(Attribute.receivePort);

        //GET SPEED
        udpFrame = ConstructCANFrame.getSpeed(Attribute._STEAM_ID);

        DatagramPacket sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
        ds.send(sendPacket);

        boolean empfang = true;
        long timestamp = new Date().getTime();



 /*       while (empfang) {
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





            MaskFormatter mfHEX = null;
            String hexFormatted = "";
            String hexNr = "";

            try {
                mfHEX = new MaskFormatter("[HHHHHHHH:HH][HH,HH,HH,HH,HH,HH,HH,HH]");
                mfHEX.setValueContainsLiteralCharacters(false);
                hexNr = hexEncode(data);
                hexFormatted = mfHEX.valueToString(hexNr);
                System.out.println("hexSpeed: " + hexFormatted);
                System.out.println("Thread: " + this.ThreadName + " : intAmountSpeed: " + getSpeedAmount()
                );
            } catch (ParseException e) {
                e.printStackTrace();
            }




 //           for (int j = 0; j < data.length; j++) {
  //              System.out.println("s["+j+"]:"+data[j]);
    //        }


 //&& canRaw.getCanDlc() == 6

            //System.out.println("SPEED CMD: "+ canRaw.getCmd());
            if (canRaw.isResponse()  && data[1] == 9  && data[2] == -89 && data[3] == 6) { //&& data[7] == Attribute.firstByteOfSteamID && data[8] == Attribute.secondByteOfSteamID) {
                MaskFormatter mfHEX = null;
                String hexFormatted = "";
                String hexNr = "";

                try {
                    mfHEX = new MaskFormatter("[HHHHHHHH:HH][HH,HH,HH,HH,HH,HH,HH,HH]");
                    mfHEX.setValueContainsLiteralCharacters(false);
                    hexNr = hexEncode(data);
                    hexFormatted = mfHEX.valueToString(hexNr);
                    System.out.println("hexSpeedMatch: " + hexFormatted);
                    System.out.println("Thread: " + this.ThreadName + " : intAmountSpeed: " + getSpeedAmount()
                    );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                setSpeedAmount(data);
                empfang = false;
                //ds.close();
                // dsReceive.close();

                for (int j = 0; j < data.length; j++) {
                    System.out.println("matched["+j+"]:"+data[j]);
                }

            }

            i++;
        }
*/
  /*      if (debugSpeed) {
            MaskFormatter mfHEX = null;
            String hexFormatted = "";
            String hexNr = "";
            //DEBUG UDP FRAME

            //System.out.println("GETSand():");
            //for (int j = 0; j < udpFrame.length; j++) {
            //    System.out.println("udpFrame[" + j + "]: " + udpFrame[j]);
            //}

            try {
                mfHEX = new MaskFormatter("[HHHHHHHH:HH][HH,HH,HH,HH,HH,HH,HH,HH]");
                mfHEX.setValueContainsLiteralCharacters(false);
                hexNr = hexEncode(data);
                hexFormatted = mfHEX.valueToString(hexNr);
                System.out.println("hexSpeed: " + hexFormatted);
                System.out.println("Thread: " + this.ThreadName + " : intAmountSpeed: " + getSpeedAmount()
                );
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

   */

        return data;
    }

    public String convertByte2String (byte[] data) {
        MaskFormatter mfHEX = null;
        String hexFormatted = "";
        String hexNr = "";

        try {
            mfHEX = new MaskFormatter("[HHHHHHHH:HH][HH,HH,HH,HH,HH,HH,HH,HH]");
            mfHEX.setValueContainsLiteralCharacters(false);
            hexNr = hexEncode(data);
            hexFormatted = mfHEX.valueToString(hexNr);
            //System.out.println("hexSpeed: " + hexFormatted);
            //System.out.println("Thread: " + this.ThreadName + " : intAmountSpeed: " + getSpeedAmount());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return hexFormatted;
    }


    public synchronized void setSpeedAmount (byte[]data) {
        speedAmount.set(parseHex2IntSpeed(data));
    }

    public synchronized AtomicInteger getSpeedAmount() {
        return speedAmount;
    }


    public synchronized void setResourceAmountWater(byte[] data) {
        ressourceAmmountIntWater.set(parseHex2IntRessource(data));
    }

    public synchronized AtomicInteger getResourceAmountWater() {
        return ressourceAmmountIntWater;
    }

    public synchronized void setResourceAmountCoil(byte[] data) {
        ressourceAmmountIntCoil.set(parseHex2IntRessource(data));
    }

    public synchronized AtomicInteger getResourceAmountCoil() {
        return ressourceAmmountIntCoil;
    }

    public synchronized void setResourceAmountSand(byte[] data) {
        ressourceAmmountIntSand.set(parseHex2IntRessource(data));
    }

    public synchronized AtomicInteger getResourceAmountSand() {
        return ressourceAmmountIntSand;
    }


    public synchronized int parseHex2IntRessource (byte[] data) {
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

    public synchronized int parseHex2IntSpeed (byte[] data) {
        byte b2 = -100;
        byte[] speed = new byte[2];
        String hexNr ="";

        String r;
        String r1;
        if (data.length != 13) {
            System.out.println("not a CAN Message!");
            return 0;
        }
        speed[0] = data[9];
        speed[1] = data[10];

        hexNr = hexEncode(speed);
        System.out.println("R: " + hexNr);
        int erg = Integer.parseInt(hexNr,16);
        System.out.println("ERG: " + erg);
        return erg;
        /*r = Byte.toString(data[4]) ;// + (data[5] & 0xFF) ;
        r1 = Byte.toString(data[5]);
        System.out.println("Sped-R:" +r);
        System.out.println("Sped-R:" +r1);
        int erg =0;
        return erg;

         */
        //return data[i] Integer.parseInt(String.valueOf(data[11]));
    }

/*    public String getIp() {
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
*/
}