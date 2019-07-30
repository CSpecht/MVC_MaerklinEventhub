package specht.General;

import specht.Ressources.Resource;
import specht.Ressources.RoundCount;

import java.io.IOException;
import java.net.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class GetCan implements Attribute{

	String resource = "";
	byte[] dataWater = new byte[13];
	byte[] dataCoil = new byte[13];
	byte[] dataSand = new byte[13];
	int resAmmountWater, resAmmountCoil, resAmmountSand;
	String name;
	AtomicInteger RoundCount, SpeedAmount;

	DatagramSocket ds, dr;

	InetAddress ia, ib;


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
		try {
			this.run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	GetCan() {

	}



	public void run() throws IOException, InterruptedException {
		RoundCount rc = new RoundCount();
		rc.start();
		RoundCount = rc.getRoundCount();
		SpeedAmount = rc.getSpeed();

		System.out.println(this.name);
		int i = 0;
			while (true) {
				LinkedList<String> payload = new LinkedList<String>();
				Resource rs = new Resource(getDatagramSocketSending(), getDatagramSocketReceiving(), ia, ib, "t1");

				rs.start();
				rs.sleep(1000);

				AtomicInteger waterA = rs.getResourceAmountWater();
				AtomicInteger coilA = rs.getResourceAmountCoil();
				AtomicInteger sandA = rs.getResourceAmountSand();

				rs.sleep(5000);

				Date d = new Date();

				payload.add(Long.toString(d.getTime()) + ";" + Attribute._STEAM_ID + ";"
						+ "Water" + ";" + waterA.get() + ";" + RoundCount + ";" + SpeedAmount);
				payload.add(Long.toString(d.getTime()) + ";" + Attribute._STEAM_ID + ";"
						+ "Coil" + ";" + coilA.get() + ";" + RoundCount + ";" + SpeedAmount);
				payload.add(Long.toString(d.getTime()) + ";" + Attribute._STEAM_ID + ";"
						+ "Sand" + ";" + sandA.get() + ";" + RoundCount + ";" + SpeedAmount);


				SendCan.sendToMSSQL(payload);
				//System.out.println("SPEED: " + rs.getSpeedAmount().get());

				if(waterA.get() <= 40 || coilA.get() <= 40 || sandA.get() <= 40) {
					System.out.println("!!!!WARNING!!!!");

					rs.setWater();
					rs.setCoil();
					rs.setSand();
					rc.setRoundCount(0);
				}



				//				int waterInt = water.get();
//				int coilInt = coil.get();
//				int sandInt = sand.get();

/*				if( waterInt <= 10)
				{
					System.out.println("!!!!!!!!!!!!!!!WAAAAARNING!!!!!!!!!!!!");
					System.out.println("RS WATER: " + rs.getResourceAmountWater());

				}
				if( coilInt <= 10)
				{
					System.out.println("!!!!!!!!!!!!!!!WAAAAARNING!!!!!!!!!!!!");
					System.out.println("RS COIL: " + rs.getResourceAmountCoil());
				}
				if( sandInt <= 10) {
					System.out.println("!!!!!!!!!!!!!!!WAAAAARNING!!!!!!!!!!!!");
					System.out.println("RS SAND: " + rs.getResourceAmountSand());
				}*/
				//rsC.sleep(1000);
				//rsS.sleep(1000);
				//dataWater = rsW.getDataWater();
				//dataCoil = rsC.getDataCoil();
				//dataSand = rsS.getDataSand();
				//resAmmountWater = rs.getResourceAmountWater();
				//rs.sleep(2000);
				//resAmmountCoil = rs.getResourceAmountCoil();
				//rs.sleep(2000);
				//resAmmountSand = rs.getResourceAmountSand();

				/*
				System.out.println("GETCANWATER: " + resAmmountWater);
				System.out.println("GETCANCOIL: " + resAmmountWater);
				System.out.println("GETCANSAND: " + resAmmountWater);
				*/


			}





			//System.out.println("GETCANAmWater: " + resAmmountWater);
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

		//udpFrame = ConstructCANFrame.getSpeed();
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
