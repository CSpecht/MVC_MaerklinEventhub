package Ressources.ResourceGet;

import Ressources.Resource;

import javax.swing.text.MaskFormatter;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

public class ResourceGetSAND extends Resource {
    boolean stop;
    public ResourceGetSAND(String ip, int port, boolean stop) {
        super(ip, port, stop);
    }

    public void getSAND() throws IOException, ParseException {
        System.out.println("connected to: " +ip+ " port: " + port);

        InetSocketAddress endpoint = new InetSocketAddress(ip,port);

        byte[] bytes = new byte[13];
        byte[] data = new byte[13];

        int rowCount = 0;

        InputStream tcp_inputStream;

        try {
            tcp_socket = new Socket();
            tcp_socket.connect(endpoint);

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

        //While trigger is false, it keeps listening
        while(stop == false) {

            StringBuilder water = new StringBuilder();

            for (int i = 0; i < data.length; i++) {
                data[i] = (byte)tcp_socket.getInputStream().read();
            }

            MaskFormatter mf2 = new MaskFormatter("[HHHHHHHH:HH][HH,HH,HH,HH,HH,HH,HH,HH]");
            mf2.setValueContainsLiteralCharacters(false);
            String hexNr = "00" + hexEncode(data);
            Date date = new Date();


            HashMap<String, String> dataMap = new HashMap();
            dataMap = translateToHashMap(hexNr);

//			for(int i = 0; i < dataMap.size(); i++) {
//				System.out.println("K/V: \t" + dataMap.keySet() + dataMap.values());
//			}

            String hexFormatted = mf2.valueToString(hexNr);
            //System.out.println("HEXFORMATTED: " + hexFormatted);
            //99m 11µ>[00000f72:5]       0 [00,00,00,00,00]

            //         [00000F72:0][50,00,00,00,00,00,00,00,000]

            water.append("[000e0f72:7][00,00,40,07,04,ed,04,00]");

            if (Pattern.matches("(.[A-F0-9]{8}.[A-F0-9]{2}..00,00,40,07,0C,ED,[A-F0-9]{2},[A-F0-9]{2}.)", hexFormatted)) {
                if(!Pattern.matches("(.[A-F0-9]{8}.[A-F0-9]{2}..00,00,40,07,0C,ED,01,[A-F0-9]{2}.)", hexFormatted)) {
                    String lokId = hexFormatted.substring(20, 25).replace(",", "");
                    //String Res = hexFormatted.substring(32,34);
                    int Res = data[10];
                    if (Res < 0)
                        Res += 256;

                    //System.out.println(hexFormatted);
                    resultCSV = rowCount + ";" + sdf.format(date) + ";" + lokId + ";" + "Sand" + ";" + Res + ";" + (int) (Res * 0.9803) + ";" + RoundCount + ";";
                    payload.add(resultCSV);
                    resultJSON = "{"
                            + "\"RowCount\":" + rowCount + ","
                            + "\"Date\":" + "\"" + sdf.format(date).toString() + "\"" + ","
                            + "\"LokID\":" + lokId + ","
                            + "\"Resource\":" + "\"Sand\"" + ","
                            + "\"ResValue\":" + Res + ","
                            + "\"ResCalc\":" + (int) (Res*0.9803) + ","
                            + "\"Round\":" + RoundCount
                            + "}";
                    jsonPayload.add(resultJSON);
                    dataset = lokId + ";" + coaches + ";Sand;" + Res + ";" + (int) (Res*0.9803) + ";" + RoundCount;
					/*SQLstment.add("INSERT INTO [dbo].[T_RESOURCES_USAGE_DATASET] ([DATATYPE], [RECORDING_START_TIME], " +
							"[TIME_STAMP], [DATASET], [DELIMITER])\n" +
							"VALUES (STEAMDATA, " + startTime + ", " + sdf.format(date) + ", " + dataset + ", " + ";");*/
                    //System.out.println(resultCSV);
                    rowCount++;
                }
            }
            if (Pattern.matches("(.[A-F0-9]{8}.[A-F0-9]{2}..00,01,00,09,00,01,[A-F0-9]{2},[A-F0-9]{2}.)", hexFormatted)) {
                String lokId = hexFormatted.substring(20 , 25).replace(",","");
                rowCount++;
                RoundCount++;
                //System.out.println(rowCount + ";" + "\t\tRound:" + RoundCount);

            }
        }
        closeConn();

    }





}

