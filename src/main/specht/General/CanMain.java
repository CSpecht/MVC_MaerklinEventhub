package specht.General;//import javax.xml.bind.annotation.adapters.HexBinaryAdapter;


import java.io.IOException;
import java.net.*;
import java.util.Date;

public class CanMain implements Attribute{


	protected static int coaches;



	public static void main(String[] args) {

		//General.ConstructCANFrame frame = new General.ConstructCANFrame();

		//General.SendCan send = new General.SendCan();

		GetCan receive = new GetCan("water");

		byte[] data = receive.getData();

		for (int i = 0; i < data.length; i++) {
			System.out.println("received_Data["+i+"]: " + data[i]);
		}

		//UdpConnection con = new UdpConnection();
		Date now = new Date();
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

		byte[] udpFrame = new byte[13];
		String log = "";

		//Send Command to CS3
		//sendCommandToCS3();

	}













}
