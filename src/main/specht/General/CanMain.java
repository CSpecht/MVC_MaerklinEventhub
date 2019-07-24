package specht.General;//import javax.xml.bind.annotation.adapters.HexBinaryAdapter;


import java.net.SocketException;

public class CanMain implements Attribute{


	protected static int coaches;



	public static void main(String[] args) throws SocketException {

		//General.ConstructCANFrame frame = new General.ConstructCANFrame();

		//General.SendCan send = new General.SendCan();



		/*UdpConnectionResponse udpConnect = new UdpConnectionResponse();
		udpConnect.start();
*/
		GetCan recWater = new GetCan("Fred_1");
		recWater.start();

		//GetCan rec1 = new GetCan("Fred_2");
		//rec1.start();





		//GetCan rec = new GetCan("Fred_2");
		//rec.start();





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













}
