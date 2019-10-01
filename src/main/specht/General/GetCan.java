package specht.General;

import specht.Ressources.PatternListener;
import specht.Ressources.Resource;

import java.io.IOException;
import java.net.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;

public class GetCan implements Attribute{


	byte[] dataWater = new byte[13];
	byte[] dataCoil = new byte[13];
	byte[] dataSand = new byte[13];
	int resAmmountWater, resAmmountCoil, resAmmountSand;

	boolean debug = true;

	String resource, name;
	AtomicInteger RoundCount, SpeedAmount, direction;
	AtomicInteger Second = new AtomicInteger(0);
	DatagramSocket ds, dr;
	InetAddress ia, ib;
	boolean runTimer = false;
	Timer t;
	GetCanTimer gct = null;

	GetCan() {}

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

		try {
			this.run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

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

	public Timer getTimer () {
		if (t == null) {
			t = new Timer();
		}
		return t;
	}

	public void restartTimer() {
		t = null;
	}

	public void cancelTimer() {

		getTimer().purge();
		t = null;
	}

	public void setRunTimer (boolean bol) {
		this.runTimer = bol;
	}

	public synchronized void run() throws IOException, InterruptedException {
		PatternListener pl = new PatternListener();
		pl.start();
		RoundCount = pl.getRoundCount();
		SpeedAmount = pl.getSpeed();
		direction = pl.getDirection();

		System.out.println(this.name);
		int i = 0;

		Resource rs;


			while (true) {

				LinkedList<String> payload = new LinkedList<String>();
				rs = new Resource(getDatagramSocketSending(), getDatagramSocketReceiving(), ia, ib, "t1");

				rs.start();
				rs.sleep(1000);

				AtomicInteger waterA = rs.getResourceAmountWater();
				AtomicInteger coilA = rs.getResourceAmountCoil();
				AtomicInteger sandA = rs.getResourceAmountSand();

				rs.sleep(2000);

				Date d = new Date();
				//!!!!!!!!!!!!!!!!!!!!!!ADD DIRECTION TO PAYLOAD TO UPLOAD DATA INTO DB!!!!!!!!!!!!!!!!!!!!!!!
				payload.add(Long.toString(d.getTime()) + ";" + Attribute._STEAM_ID + ";"
						+ "Water" + ";" + waterA.get() + ";" + RoundCount + ";" + SpeedAmount);
				payload.add(Long.toString(d.getTime()) + ";" + Attribute._STEAM_ID + ";"
						+ "Coil" + ";" + coilA + ";" + RoundCount + ";" + SpeedAmount);
				payload.add(Long.toString(d.getTime()) + ";" + Attribute._STEAM_ID + ";"
						+ "Sand" + ";" + sandA + ";" + RoundCount + ";" + SpeedAmount);

				System.out.println("payload(0): " + payload.get(0));
				if (debug) {
					System.out.println("Water: " + waterA);
					System.out.println("Coil:  " + coilA);
					System.out.println("Sand:  " + sandA);
					System.out.println("Round: " + RoundCount);
					//System.out.println("Direction: " + direction);

				}

				//System.out.println("SPEED: " + rs.getSpeedAmount().get());
				// < 200 RESSOURCE AMMOUNT HANDLING
				if (waterA.get() <= 200 || coilA.get() <= 200|| sandA.get() <= 200) {
					rs.sleep(1000);

					System.out.println("!!!!WARNING!!!!");

					System.out.println("WARN WATER: "+ waterA.get());
					System.out.println("WARN COIL: "+ coilA.get());
					System.out.println("WARN SAND: "+ sandA.get());

					if (t == null) {

						callTimer(getTimer(), getDatagramSocketSending(), ia, rs, waterA, coilA, sandA, pl);
						runTimer = true;

					}
					if (t != null) {
						t = gct.rTimer();
					}



				}


				/*********SEND PAYLOAD TO MSSQL SERVER********/
				//SendCan.sendToMSSQL(payload);
			}

		//closeConnection();
	}
/*	public byte[] getData () {
		return data;
	}
	public int getRessourceAmmount() { return resAmmount; }
*/

public synchronized void callTimer(Timer t, DatagramSocket s, InetAddress ia, Resource rs,AtomicInteger waterA, AtomicInteger coilA, AtomicInteger sandA, PatternListener pl ) {

	gct = new GetCanTimer(t, s, ia, rs, waterA, coilA, sandA, pl);
	t.schedule(gct, 0, 1000);

}

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

		ds = getDatagramSocketSending();
		dr = getDatagramSocketReceiving();

		ia = InetAddress.getByName(Attribute.sendingAddress);
		ib = InetAddress.getByName(Attribute.receivingAddress);

		DatagramPacket sendPacket = new DatagramPacket( udpFrame, udpFrame.length, ia, Attribute.sendingPort);
		ds.send( sendPacket );
		sendPacket = new DatagramPacket( new byte[13], 13, ib, Attribute.receivePort );
		dr.receive( sendPacket );

		// Receive Data
		InetAddress address = sendPacket.getAddress();
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
