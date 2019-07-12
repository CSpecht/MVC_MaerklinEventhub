package Ressources;

import Ressources.ResourceGet.ResourceGetCOIL;
import Ressources.ResourceGet.ResourceGetSAND;
import Ressources.ResourceGet.ResourceGetWATER;

import java.io.IOException;
import java.net.Socket;
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
    protected static int coaches;
    protected static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public LinkedList<String> payload = new LinkedList<String>();
    protected ArrayList<String> SQLstment = new ArrayList<String>();
    public LinkedList<String> jsonPayload = new LinkedList<String>();



    public Resource(String ip, int port, boolean stop){}

    public Resource(String ip, int port) {
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

    public void getResource (String i) throws IOException, ParseException {
        ResourceGetWATER w_resource = new ResourceGetWATER(ip,port,stop);
        ResourceGetCOIL c_resource = new ResourceGetCOIL(ip,port,stop);
        ResourceGetSAND s_resource = new ResourceGetSAND(ip,port,stop);

        switch (i) {
            case "water":
                w_resource.getWater();

                break;
            case "coil":
                c_resource.getCOIL();
                break;
            case "sand":
                s_resource.getSAND();
                break;
        }

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