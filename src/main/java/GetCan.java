import javax.swing.text.MaskFormatter;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GetCan extends Thread{


	/***************************************************************************************
	 * GET SPEED OF TRAIN
	 ***************************************************************************************/
	public boolean trainRunning() throws IOException {
		boolean result = false;
		byte[] udpFrame = new byte[13];
		byte[] packatData;
		DatagramSocket ds = new DatagramSocket(Attribute.sendingPort);
		DatagramSocket dsReceive = new DatagramSocket(Attribute.receivePort);
		InetAddress ia = InetAddress.getByName(Attribute.sendingAddress);
		InetAddress ib = InetAddress.getByName(Attribute.receivingAddress);

		udpFrame = ConstructCANFrame.getSpeed();
		int i = 0;

		//System.out.println("I: " + i);
		DatagramPacket sendPacket = new DatagramPacket( udpFrame, udpFrame.length, ia, Attribute.sendingPort);
		//System.out.println("1");
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

		if (data[9] != 0 && data[10] != 0) {
			return result = true;
		} else {

			return result = false;
		}

	}



}
