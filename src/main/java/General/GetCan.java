package General;

import Ressources.Resource;

import java.io.IOException;
import java.net.*;
import java.text.ParseException;

public class GetCan implements Attribute{

	String resource = "";
	DatagramPacket dp;

	GetCan (String resource) {

		this.resource = resource;


		System.out.println(resource);
	}

	public GetCan() {

	}


	public void run() {



			System.out.println("THREAD!!");
		try {
			Resource rs = new Resource();
			rs.getResource(resource);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}


	}

	/***************************************************************************************
	 * GET SPEED OF TRAIN
	 ***************************************************************************************/
	public boolean isTrainRunning() throws IOException {
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

	public DatagramPacket getRes () throws IOException, ParseException {
		return dp;

	}






}
