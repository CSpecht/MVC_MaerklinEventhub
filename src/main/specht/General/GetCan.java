package specht.General;

import specht.Ressources.Resource;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class GetCan extends Thread implements Attribute{

	String resource = "";
	byte[] dataWater = new byte[13];
	byte[] dataCoil = new byte[13];
	byte[] dataSand = new byte[13];
	int resAmmountWater;
	int resAmmountCoil;
	int resAmmountSand;
String name;

	GetCan (String name) {
		this.name = name;
		//System.out.println(resource);
		//this.run();

	}

	GetCan() {

	}



	public void run() {


			System.out.println(this.name);
			Resource rs = new Resource();

			dataWater = rs.getDataWater();
			dataCoil = rs.getDataCoil();
			dataSand = rs.getDataSand();
			resAmmountWater = rs.getRessourceAmmountWater();
			resAmmountCoil = rs.getRessourceAmmountCoil();
			resAmmountSand = rs.getRessourceAmmountSand();

			System.out.println("w:" + resAmmountWater + " c: " + resAmmountCoil + " s: "+ resAmmountSand );



		rs.closeConnection();






	}
/*	public byte[] getData () {
		return data;
	}
	public int getRessourceAmmount() { return resAmmount; }
*/

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
