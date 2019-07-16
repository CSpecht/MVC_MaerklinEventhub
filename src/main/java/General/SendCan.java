package General;

import Ressources.Resource;
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.EventHubException;
import org.apache.log4j.BasicConfigurator;

import javax.swing.text.MaskFormatter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class SendCan extends Thread implements Attribute {
    DatagramPacket dmp;
    byte[] dataFrame = new byte[13];
    LinkedList<String> csvPayload = new LinkedList<String>();
    LinkedList<String> jsonPayload = new LinkedList<String>();

    private static final ExecutorService pool = Executors.newWorkStealingPool();


    public SendCan (DatagramPacket packet) {
        dmp = packet;
    }

    public LinkedList<String> getCsvPayload() {
        return csvPayload;
    }

    public void setCsvPayload(LinkedList<String> csvPayload) {
        this.csvPayload = csvPayload;
    }

    public LinkedList<String> getJsonPayload() {
        return jsonPayload;
    }

    public void setJsonPayload(LinkedList<String> jsonPayload) {
        this.jsonPayload = jsonPayload;
    }

    public byte[] getData (DatagramPacket p) {
        dataFrame = p.getData();
        return dataFrame;
    }

    public void transformData2JSON(byte[] df){


        // jsonPayload.add(resultCSV);
    }

    public void transformData2CSV(byte[] df) {
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

        System.out.println("hexFo: " + hexFormatted);
        System.out.println("hexNr:  " + hexNr);
        System.out.println("heCSV:  " + csvFormatted);
        //csvPayload.add(byteStream);

    }

    //Encoding the byte[] into hexadecimal Number, RETURN STRING
    private static String hexEncode(byte[] buf) {

        return hexEncode(buf, new StringBuilder()).toString();
    }

    //Encoding byte[] into hexadecimal Number, RETURN STRINGBUILDER
    public static StringBuilder hexEncode (byte[] buf, StringBuilder sb) {
        for (byte b : buf) {
            sb.append(String.format("%02x",b));
        }
        return sb;
    }



    //----Send received Data from CAN to Azure----
    public static void sendToAzure(Resource DForAzure) throws EventHubException, IOException {
        //----UNCOMMENT TO CONNECT TO EVENTHUB----
        //This configures the log4j framework/package, necessary to send data to eventhub
        BasicConfigurator.configure();

        // The Executor handles all asynchronous tasks and this is passed to the EventHubClient instance.
        // This enables the user to segregate their thread pool based on the work load.
        // This pool can then be shared across multiple EventHubClient instances.
        // The following sample uses a single thread executor, as there is only one EventHubClient instance,
        // handling different flavors of ingestion to Event Hubs here.
        final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);

        // Each EventHubClient instance spins up a new TCP/SSL connection, which is expensive.
        // It is always a best practice to reuse these instances. The following sample shows this.
        final EventHubClient ehClient = EventHubClient.createSync(String.valueOf(Attribute.azureConn), executorService);


        //----SEND JSON FORMAT TO AZURE EVENTHUB----
        try{
            System.out.println("\t---PayloadJSON output---");

            //We will use this variable later to injest data into eventhub
            String payload = "";

            for(int i = 0; i<DForAzure.jsonPayload.size(); i++){ //ec.jsonPayload.size()
                payload  = DForAzure.payload.get(i);

                //(LinkedList <String>) DForAzure.jsonPayload.get(i);
                System.out.println("@ " + payload);
                byte[] payloadBytes = payload.getBytes(StandardCharsets.UTF_8.name());
                EventData sendEvent = EventData.create(payloadBytes);

                // Send - not tied to any partition
                // Event Hubs service will round-robin the events across all Event Hubs partitions.
                // This is the recommended & most reliable way to send to Event Hubs.
                ehClient.sendSync(sendEvent);

            }
        }finally {
            ehClient.closeSync();
            executorService.shutdown();
        }

    }

    //----Send received Data from CAN to MS SQL----
    public static void sendToMSSQL(DatagramPacket DForSQL) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // create DateFormatter for the right format of date for SQLServer.
        DateFormat sdf = new SimpleDateFormat(Attribute.DATEFORMAT);
        Date date = new Date();

        try (Connection con = DriverManager.getConnection(Attribute.dbUrl); Statement stmt = con.createStatement();) {
            for (int i = 0; i < DForSQL.getData().length; i++) {
                String SQL = "INSERT INTO [dbo].[T_RESOURCES_USAGE_DATASET] ([DATATYPE], [RECORDING_START_TIME], "
                        + "[TIME_STAMP], [DATASET], [DELIMITER])"
                        + "VALUES ('" + Attribute.sqlDataType + "','"
                        + sdf.format(date).toString() + "','"
                        + sdf.format(date).toString() + "','"
                        + DForSQL.getData().toString()
                        + "', ';')";
                System.out.println("SQL: " + SQL);
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


    //----Send Command to CS3----
    public static void sendCommandToCS3 () {

        byte[] udpFrame = new byte[13];
        char[] data = new char[8];
        int uid = 6168;
        char response = 0;
        char command = 0;
        char prio = 0;
        char dlc = 5;
        int[] testFrame = new int[13];

        //General.ConstructCANFrame frame = new General.ConstructCANFrame();
        //udpFrame = ConstructCANFrame.setOil();


        System.out.println("udpLength: " + udpFrame.length);
        for (int i = 0; i < udpFrame.length; i++) {
            System.out.println("udpFrame["+i+"]: " + udpFrame[i]);
        }
        sendTCP(udpFrame, 0, udpFrame.length);


    }

    /**************************************************************************************
     * SEND CAN MESSAGE
     * @throws UnknownHostException
     ***************************************************************************************/
    public static void sendCanToCS3 (String connectionUrl, String dType) throws IOException, InterruptedException {
        InetAddress addresse = InetAddress.getByName(Attribute.sendingAddress);
        //General.ConstructCANFrame udp = new General.ConstructCANFrame();
        //String ipAdress = "192.168.0.2";

        byte[] udpFrame = new byte[13];
        char[] data = new char[8];
        int uid = 6168;
        char response = 1;
        char command = 0;
        char prio = 0;
        char dlc = 6;
        int[] testFrame = new int[13];
        int cargoId = 0x4007;

        //If the variable is setted up as -1, Max Limit = 500
        //if(iterations == -1) iterations = 500;
        Resource DForSQL = new Resource(); //Attribute.sendingAddress,Attribute.sendingPort
        //TODO: IMPLEMENT THREADS!!!!
        //DForSQL.start();

        byte[] testData = new byte[ 13 ];
        String log = "";
        GetCan GC = new GetCan("water");


        //DForSQL.stopListener();
        //DForAzure.stopListener();
        //sendToAzure(DForAzure,ehClient,executorService);


        //sendTCP(udpFrame, 0, udpFrame.length);

        while (GC.isTrainRunning()) {
            long millis = System.currentTimeMillis();
            DForSQL.startListener();

            //ask status of water
            //udpFrame = ConstructCANFrame.getWater(Attribute._STEAM_ID);
            sendTCP(udpFrame, 0, udpFrame.length);
            //sendToMSSQL(DForSQL, connectionUrl, dType);
            //DForSQL.stopListener();

            //ask status of oil
            udpFrame = ConstructCANFrame.getCoil(Attribute._STEAM_ID);
            sendTCP(udpFrame, 0, udpFrame.length);
            //sendToMSSQL(DForSQL, connectionUrl, dType);
            //DForSQL.stopListener();

            //ask status of sand
            udpFrame = ConstructCANFrame.getSand(Attribute._STEAM_ID);
            sendTCP(udpFrame, 0, udpFrame.length);
            //sendToMSSQL(DForSQL, connectionUrl, dType);
            //DForSQL.stopListener();

            /////////////////DEBUG PRINT UDP-Package/////////////////
            System.out.println("udpLength: " + udpFrame.length);
            for (int i = 0; i < udpFrame.length; i++) {
                System.out.println("udpFrame["+i+"]: " + udpFrame[i]);
            }
            //sendToMSSQL(DForSQL);
            DForSQL.stopListener();
            Thread.sleep(1000 - millis % 1000);
        }
    }


    /***************************************************************************************
     * SEND UDP-FRAME via UDP to HOST
     ***************************************************************************************/
    public static void sendUDP (byte[] udpFrame,InetAddress adress) {
        try {

            DatagramSocket dms = new DatagramSocket();
            DatagramPacket dmp = new DatagramPacket(udpFrame, udpFrame.length, adress,Attribute.receivePort);
            dms.send(dmp);
            System.out.println("SEND!");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /***************************************************************************************
     * SEND TCP-FRAME via TCP to HOST
     ***************************************************************************************/
    public static void sendTCP (byte[] udpFrame, int start, int len) {
        try {

            Socket socket = new Socket(Attribute.sendingAddress, Attribute.sendingPort);
            OutputStream out = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);

            if (len > 0) {
                dos.write(udpFrame, start, len);
            }

            socket.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /***************************************************************************************
     * PING HOST
     ***************************************************************************************/
    public static void pingHost (InetAddress addresse) {
        try {
            if(addresse.isReachable(5000)) {
                System.out.println("Host is reachable!");
            }
            else {
                System.out.println("sorry not reachable");
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }


}
