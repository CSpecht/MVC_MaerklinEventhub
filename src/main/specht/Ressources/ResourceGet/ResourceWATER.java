package specht.Ressources.ResourceGet;

import specht.General.Attribute;
import specht.General.ConstructCANFrame;
import specht.Ressources.Resource;

import javax.swing.text.MaskFormatter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

public class ResourceWATER extends Resource {

    boolean debug = false;
    HashMap<Integer, String> kvs = new HashMap<Integer, String>();
    HashMap hMap = new HashMap();
    /*   boolean stop;
    public ResourceWATER(String ip, int port, boolean stop) {
        super(ip, port, stop);
    }
*/


    //TODO: IMPLEMENTATION WRONG THE RESULT ISN'T RIGHT!!! -> SENDING TO MUCH DATA
    public void getWater () throws IOException {

        boolean result = false;
        byte[] udpFrame = new byte[13];
        byte[] packatData;
        DatagramSocket ds = new DatagramSocket(Attribute.sendingPort);
        DatagramSocket dsReceive = new DatagramSocket(Attribute.receivePort);
        InetAddress ia = InetAddress.getByName(Attribute.sendingAddress);
        InetAddress ib = InetAddress.getByName(Attribute.receivingAddress);

        //GET WATER
        udpFrame = ConstructCANFrame.getWater(Attribute._STEAM_ID);

        System.out.println("GETWATER():");
        for (int i = 0; i < udpFrame.length; i++) {
            System.out.println("udpFrame[" + i + "]: " + udpFrame[i]);
        }

        //int i = 0;

        //System.out.println("I: " + i);
        DatagramPacket sendPacket = new DatagramPacket( udpFrame, udpFrame.length, ia, Attribute.sendingPort);
        //System.out.println("1");
        ds.send( sendPacket );

        //System.out.println("2");
        // Auf Anfrage warten
        boolean empfang = true;
        long timestamp = new Date().getTime();
        System.out.println(timestamp);

        int i = 0;
        do{

            sendPacket = new DatagramPacket(new byte[13], 13, ib, Attribute.receivePort);
            dsReceive.receive(sendPacket);
            //System.out.println("3");
            //comment

            // Empf�nger auslesen
            InetAddress address = sendPacket.getAddress();
            //System.out.println("4");
            int port2 = sendPacket.getPort();
            int len = sendPacket.getLength();
            byte[] data = sendPacket.getData();

            int bytecounter = 1;
            for (int j = 0; j < data.length ; j++) {
                hMap.put(bytecounter,data[j]);

                if (29 == bytecounter) {
                    bytecounter = 1;
                }
            }

            MaskFormatter mfHEX = null;
            String hexFormatted = "";
            String hexNr = "";
            try {
                mfHEX = new MaskFormatter("[HHHHHHHH:HH][HH,HH,HH,HH,HH,HH,HH,HH]");
                mfHEX.setValueContainsLiteralCharacters(false);
                hexNr = hexEncode(data);
                hexFormatted = mfHEX.valueToString(hexNr);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            kvs.put(i, hexFormatted);

            //System.out.println("DATA Received:");
            /*for (int j = 0; j < data.length; j++) {
                System.out.println("data[" + j + "]: " + data[j]);
            }*/


            if (i == 20) {
                empfang = false;
            }
            System.out.println("i: " + i);
            i++;
        } while(empfang == true);


        System.out.println("KVS:");
        for (int j = 0; j <kvs.size(); j++) {

            System.out.println(kvs.get(j));

        }
        //System.out.println();


        if (debug == true) {
            System.out.println("__DEBUG__");
            MaskFormatter mfHEX = null;
            String hexFormatted = "";
            String hexNr = "";
            try {
                mfHEX = new MaskFormatter("[HHHHHHHH:HH][HH,HH,HH,HH,HH,HH,HH,HH]");
                mfHEX.setValueContainsLiteralCharacters(false);
                //hexNr = hexEncode(data);
                hexFormatted = mfHEX.valueToString(hexNr);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            System.out.println("data formatted: " + hexFormatted);
        }

    }

    private void handlePackage(HashMap packet) {
        // die Message ID
        int messageID = getValueFromBytes(packet, 9, 12, true).intValue();

        // Datenlänge (Die letzten 4 Bit des 7. Bytes)
        int dataLength = (int)packet.get(7) & 0xf;
        switch (messageID) {
            // hier behandelt man nun die Pakete je nach ID

        }
    }

    public static Double getValueFromBytes(HashMap<Integer, Byte> bytePacket, int startByte, int endByte, Boolean byteIsUnsigned) {
        Double result = new Double(0);

        // Die Bytes werden noch mit dem richtigen Faktor multipliziert
        // für das erste 2⁰, dann 2⁸, 2¹⁶
        for (int i = startByte; i <= endByte ; i++) {
            if(!byteIsUnsigned && i == endByte) {
                result += new Double(bytePacket.get(i)) * Math.pow(2, 8 * (i - startByte));
            } else {
                result += new Double(unsignedByteToInt(bytePacket.get(i))) * Math.pow(2, 8* (i - startByte));
            }
        }
        return result;
    }

    private static int unsignedByteToInt(byte b) {
        return (int) b & 0xFF;
    }

    //Encoding the byte[] into hexadecimal Number, RETURN STRING
    public static String hexEncode(byte[] buf) {

        return hexEncode(buf, new StringBuilder()).toString();
    }

    //Encoding byte[] into hexadecimal Number, RETURN STRINGBUILDER
    public static StringBuilder hexEncode (byte[] buf, StringBuilder sb) {
        for (byte b : buf) {
            sb.append(String.format("%02x",b));
        }
        return sb;
    }


    public void setWater() throws IOException {
        boolean result = false;
        byte[] udpFrame = new byte[13];
        byte[] packatData;
        DatagramSocket ds = new DatagramSocket(Attribute.sendingPort);
        DatagramSocket dsReceive = new DatagramSocket(Attribute.receivePort);
        InetAddress ia = InetAddress.getByName(Attribute.sendingAddress);
        InetAddress ib = InetAddress.getByName(Attribute.receivingAddress);

        //ressource Start
        udpFrame = ConstructCANFrame.resourceStart();
        DatagramPacket sendPacket = new DatagramPacket( udpFrame, udpFrame.length, ia, Attribute.sendingPort);
        ds.send(sendPacket);

        //train STOP
        udpFrame = ConstructCANFrame.stopTrain();
        sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
        ds.send(sendPacket);

        //SET WATER
        udpFrame = ConstructCANFrame.setWater(Attribute._STEAM_ID);

        System.out.println("GETWATER():");
        for (int i = 0; i < udpFrame.length; i++) {
            System.out.println("udpFrame[" + i + "]: " + udpFrame[i]);
        }

        //int i = 0;

        //System.out.println("I: " + i);
        sendPacket = new DatagramPacket( udpFrame, udpFrame.length, ia, Attribute.sendingPort);
        //System.out.println("1");
        ds.send( sendPacket );


        //RESOURCE STOP
        udpFrame = ConstructCANFrame.resourceStop();
        sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
        ds.send( sendPacket );



        //System.out.println("2");
        // Auf Anfrage warten
        sendPacket = new DatagramPacket( new byte[13], 13, ib, Attribute.receivePort );
        dsReceive.receive( sendPacket );
        //System.out.println("3");
        //comment

        // Empf�nger auslesen
        InetAddress address = sendPacket.getAddress();
        //System.out.println("4");
        int         port2    = sendPacket.getPort();
        int         len     = sendPacket.getLength();
        byte[]      data    = sendPacket.getData();

        System.out.println("DATA Received:");
        for (int j = 0; j<data.length; j++) {
            System.out.println("data[" + j + "]: " + data[j]);
        }

        udpFrame = ConstructCANFrame.go();
        sendPacket = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
    }


    }

