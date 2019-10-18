package specht.General;//import javax.xml.bind.annotation.adapters.HexBinaryAdapter;


import specht.Szenario3.SzenarioThree;

import java.io.IOException;
import java.sql.*;

public class CanMain implements Attribute{


	protected static int coaches;
	protected static boolean debug = true;
	protected static String GAMEMODE;




	public static void main(String[] args) throws IOException {


		getGameModeFromSQL();
		switch (GAMEMODE) {
			case "Szenario1":
				GetCan rec = new GetCan("Fred_1");
				break;
			case "Szenario2":
				SzenarioTwo sz2 = new SzenarioTwo();
				sz2.run();
				break;
			case "Szenario3":
				SzenarioThree sz3 = new SzenarioThree();
				sz3.run();
				break;
		}




		//General.ConstructCANFrame frame = new General.ConstructCANFrame();

		//General.SendCan send = new General.SendCan();



		/*UdpConnectionResponse udpConnect = new UdpConnectionResponse();
		udpConnect.start();
*/
		//GetCan recWater = new GetCan("Fred_1");
		//recWater.start();

		//PatternListener rc = new PatternListener();
	//	rc.start();
	//	int PatternListener = rc.getRoundCount();
		//System.out.println("MAINROUNDS: " + PatternListener);



		//RUN DAMPFLOK
		//GetCan rec = new GetCan("Fred_1");


		//SzenarioTwo sz2 = new SzenarioTwo();
		//sz2.run();






		//GetCan receiveWater = new GetCan("water");
		//GetCan receiveCoil = new GetCan("coil");
		//GetCan receiveSand = new GetCan("sand");
		//GetCan receiveCoil = new GetCan("coil");
		//receiveWater.start();
		//receiveSand.start();
	/*	try {
			receiveWater.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		//System.out.println("water: " + receiveWater.getRessourceAmmount());
		//System.out.println("sand: " + receiveSand.getRessourceAmmount());
		//System.out.println("coil: " + receiveCoil.getRessourceAmmount());

		//receiveCoil.start();

		//GetCan receiveSand = new GetCan("sand");
		//receiveSand.start();

		//byte[] water = receiveWater.getData();
		//byte[] coil = receiveCoil.getData();
		//byte[] sand = receiveSand.getData();

		//System.out.println("Water: " + receiveWater.getRessourceAmmount());
		//System.out.println("Coil: " + receiveCoil.getRessourceAmmount());
		//System.out.println("Sand: " + receiveSand.getRessourceAmmount());

/*		System.out.println("\nWATER");
		for (int i = 0; i < water.length; i++) {
			System.out.println("water["+i+"]: " + water[i]);
		}
		System.out.println("\nCOIL");
		for (int i = 0; i < coil.length; i++) {
			System.out.println("coil["+i+"]: " + coil[i]);
		}
		System.out.println("\nSAND");
		for (int i = 0; i < sand.length; i++) {
			System.out.println("sand["+i+"]: " + sand[i]);
		}
*/
		//UdpConnectionResponse con = new UdpConnectionResponse();
/*		Date now = new Date();
		int pkNr = 1;
		byte[] Frame = new byte[13];
		InetAddress ia = null;
		long mills = now.getTime();

		try {
			TestClass test = new TestClass();
			Frame = test.getWaterData();
		} catch (IOException e) {

		}
		//ConstructCANFrame cf = new ConstructCANFrame();
		//Frame = cf.getWater(Attribute._STEAM_ID);
		try {
			ia = InetAddress.getByName(sendingAddress);
			//DatagramSocket ds = new DatagramSocket(sendingPort);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		DatagramPacket dp = new DatagramPacket(Frame,Frame.length,ia, sendingPort);

		UdpPackage udpP = new UdpPackage(dp,pkNr,mills);
		System.out.println("UDP: " + udpP.toString());
*/

		/*	byte[] canId = { 003 };
		for (int i = 0; i < canId.length; i++) {
			System.out.println("canIdByte["+i+"]: "+canId[i]);
		}
	*/





		/*//START USERINTERFACE
		final UserInterfaceChart uic = new UserInterfaceChart();
		uic.go();
		*/

        //GET Data for Azure
		//General.GetCan DForAzure = new General.GetCan(ipAdress, 15731);
        //DForAzure.start();

		//uncomment to send Data

		//uncomment to GET Data
		//sendCanToCS3(ipAdress, connectionUrl, dType);

		//byte[] udpFrame = new byte[13];
		//String log = "";

		//Send Command to CS3
		//sendCommandToCS3();

	}


	public static void getGameModeFromSQL () {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Connection con = null;

		try {
			con = DriverManager.getConnection(Attribute.dbUrl);
			Statement stmt = con.createStatement();
			String SQL = "select TOP 1 GAMEMODE from T_CONFIGURATION WHERE RUN_YN = 1";
			//String SQL = "SELECT dbo.get_train_speed(" + GameID + "," + Second +") as 'rs'";
			System.out.println(SQL);
			ResultSet rs = stmt.executeQuery(SQL);

			if (rs.next() == false && debug) {
				System.out.println("NO GAME MODE FOUND");
			} else {
				do {
					setGameMode(rs.getString("GAMEMODE"));
					if (debug) {
						System.out.println("GAMEMODE: " + rs.getString("GAMEMODE"));
					}
				} while (rs.next());
			}


		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected static String getGameMode () {
		return GAMEMODE;
	}

	protected static void setGameMode (String GameMode) {
		GAMEMODE = GameMode;
	}










}
