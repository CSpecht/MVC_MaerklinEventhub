package specht.Ressources;

import specht.General.Attribute;

import javax.swing.text.MaskFormatter;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class PatternListener extends Thread {

    protected AtomicInteger RoundCount = new AtomicInteger();
    protected AtomicInteger speed = new AtomicInteger();
    protected boolean debug = true;
    protected boolean stop = false;
    protected String ip = "";
    protected int port = 0;
    private Socket tcp_socket = null;
    protected AtomicInteger direction = new AtomicInteger();
    protected Attribute attribute;

    public PatternListener() throws IOException {
        if (attribute == null) {
            attribute = new Attribute();
        }
        this.ip = attribute.getSendingAddress();
        this.port = attribute.getSendingPort();
        InetSocketAddress endpoint = new InetSocketAddress(this.ip,this.port);
        tcp_socket = new Socket();
        tcp_socket.connect(endpoint);
    }


    public AtomicInteger getRoundCount() {
        return RoundCount;
    }

    public void setRoundCount(int round) {
        this.RoundCount.set(round);
    }

    public AtomicInteger getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed.set(speed);
    }

    public void setDirection (int direct) {this.direction.set(direct); }

    public AtomicInteger getDirection () {return direction;}

    public void run() {
        boolean result = false;
        byte[] udpFrame = new byte[13];
        //byte[] packatData;
        //ds = new DatagramSocket(Attribute.sendingPort);
        //dsReceive = new DatagramSocket(Attribute.receivePort);

        System.out.println("connected to: " +ip+ " port: " + port);



        byte[] bytes = new byte[13];
        byte[] data = new byte[13];

        InputStream tcp_inputStream;

        try {
            tcp_inputStream = tcp_socket.getInputStream();
            tcp_inputStream.read(bytes);
            byte test = (byte)tcp_socket.getInputStream().read();

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }



        while (stop == false) {
            boolean empfang = true;
            long timestamp = new Date().getTime();

            for (int i = 0; i < data.length; i++) {
                try {
                    data[i] = (byte)tcp_socket.getInputStream().read();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }



            MaskFormatter mf2 = null;
            try {
                mf2 = new MaskFormatter("[HHHHHHHH:HH][HH,HH,HH,HH,HH,HH,HH,HH]");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mf2.setValueContainsLiteralCharacters(false);
            String hexNr = "00" + hexEncode(data);
            String hexPattern = null;


            HashMap<String, String> dataMap = new HashMap();
            dataMap = translateToHashMap(hexNr);

            try {
                hexPattern = mf2.valueToString(hexNr);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            //System.out.println("hexPattern: " + hexPattern);
            //System.out.println(Attribute.SpeedPattern);
            if (Pattern.matches(attribute.getRoundCountPattern(), hexPattern)) {
                String lokId = hexPattern.substring(20 , 25).replace(",","");
                setRoundCount(getRoundCount().incrementAndGet());
                if (debug) {debugMethod(hexPattern, getRoundCount().toString(),"PatternRoundCount");}

            }

            if (Pattern.matches(attribute.getSpeedPattern(), hexPattern)) {
                //String lokId = hexPattern.substring(20 , 25).replace(",","");
                System.out.println("");
                String speedAm = hexPattern.substring(25,31).replace(",","");
                setSpeed(Integer.parseInt(speedAm,16));
                if (debug) {debugMethod(hexPattern, getSpeed().toString(), "PatternSpeed");}

            }

            if (Pattern.matches(attribute.getDirectionPatternFw(), hexPattern)) {
                setDirection(0);
                if (debug) {debugMethod(hexPattern, getDirection().toString(),"PatternFw");}

            }

            if (Pattern.matches(attribute.getDirectionPatternBw(), hexPattern)) {
                setDirection(1);
                if (debug) {debugMethod(hexPattern, getDirection().toString(), "PatternBw");}
            }
        }
        //GET COIL
       // udpFrame = ConstructCANFrame.getRound();
      //  DatagramPacket sendPacket= new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
      //  ds.send(sendPacket);

        closeConn();




 /*       if (debug) {
            MaskFormatter mfHEX = null;
            String hexFormatted = "";
            String hexNumber = "";

            //DEBUG UDP FRAME

            System.out.println("GETROUND():");
            for (int j = 0; j < udpFrame.length; j++) {
                System.out.println("udpFrame[" + j + "]: " + udpFrame[j]);
            }

            try {
                mfHEX = new MaskFormatter("[HHHHHHHH:HH][HH,HH,HH,HH,HH,HH,HH,HH]");
                mfHEX.setValueContainsLiteralCharacters(false);
                hexNumber = hexEncode(udpFrame);
                hexFormatted = mfHEX.valueToString(hexNumber);
                System.out.println("hexRound: " + hexFormatted);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
*/
    }


    public void debugMethod (String hexPattern, String value, String Desc) {

        System.out.println(Desc + " : " + hexPattern + " : " + value);

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

    private HashMap<String,String> translateToHashMap (String hex) {
        HashMap<String, String> map = new HashMap<String, String>();
        String metaData = hex.substring(0,8);
        String data = hex.substring(9,19);
        map.put(metaData, data);
        return map;
    }

    protected void closeConn () {

        try {
            tcp_socket.shutdownInput();
            tcp_socket.close();

        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

}
