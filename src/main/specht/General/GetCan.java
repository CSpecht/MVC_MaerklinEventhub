package specht.General;

import specht.Ressources.Resource;

import java.io.IOException;
import java.net.*;

public class GetCan implements Attribute{

	String resource = "";
	byte[] dataWater = new byte[13];
	byte[] dataCoil = new byte[13];
	byte[] dataSand = new byte[13];
	int resAmmountWater;
	int resAmmountCoil;
	int resAmmountSand;
	String name;

	DatagramSocket ds;
	DatagramSocket dr;
	InetAddress ia;
	InetAddress ib;

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



	GetCan (String name) throws SocketException {
		this.name = name;

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

		//System.out.println(resource);
		this.run();

	}

	GetCan() {

	}



	public void run() throws SocketException {


			System.out.println(this.name);
			Resource rsW = new Resource(getDatagramSocketSending(), getDatagramSocketReceiving(), ia, ib);
			rsW.start();

			Resource rsC = new Resource(getDatagramSocketSending(), getDatagramSocketReceiving(), ia, ib);
			rsC.start();

			Resource rsS = new Resource(getDatagramSocketSending(), getDatagramSocketReceiving(), ia, ib);
			rsS.start();

			dataWater = rsW.getDataWater();
			dataCoil = rsC.getDataCoil();
			dataSand = rsS.getDataSand();
			resAmmountWater = rsW.getRessourceAmmountWater();
			resAmmountCoil = rsC.getRessourceAmmountCoil();
			resAmmountSand = rsS.getRessourceAmmountSand();

			//System.out.println("w:" + resAmmountWater + " c: " + resAmmountCoil + " s: "+ resAmmountSand );

		//rs.closeConnection();

	}
/*	public byte[] getData () {
		return data;
	}
	public int getRessourceAmmount() { return resAmmount; }
*/
public void closeConnection () {
	try {
		getDatagramSocketSending().close();
		getDatagramSocketReceiving().close();
	} catch (SocketException e) {
		e.printStackTrace();
	}

}
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
