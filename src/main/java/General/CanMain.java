package General;//import javax.xml.bind.annotation.adapters.HexBinaryAdapter;


import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.Reader;
import java.net.*;
import java.text.ParseException;
import java.util.Scanner;

import Ressources.ResourceGet.*;
import General.ConstructCANFrame;


public class CanMain implements Attribute{


	protected static int coaches;



	public static void main(String[] args) throws IOException, ParseException {

		//General.ConstructCANFrame frame = new General.ConstructCANFrame();

		//General.SendCan send = new General.SendCan();
		Scanner in = new Scanner(System.in);

		//System.out.println("How many Coaches?");
		//int coaches = in.nextInt();

		Ressources.ResourceGet.ResourceWATER w = new Ressources.ResourceGet.ResourceWATER();
		DatagramPacket dp = w.getWater();

		SendCan sc = new SendCan(dp);
		sc.transformData2CSV(dp.getData());


		byte[] data = dp.getData();

	/*	System.out.println("DATA Received:");
		for (int j = 0; j<data.length; j++) {
			System.out.println("data[" + j + "]: " + data[j]);
		}*/

		//GetCan test = new GetCan("water");


		//while (true) {
		//	SendCan sc = new SendCan();


			//Ressources.ResourceGet.ResourceWATER receiveWater = new Ressources.ResourceGet.ResourceWATER();
			//Ressources.ResourceGet.ResourceCOIL receiveCoil = new Ressources.ResourceGet.ResourceCOIL();
			//Ressources.ResourceGet.ResourceSAND reveiveSsand = new Ressources.ResourceGet.ResourceSAND();

	/*		DatagramPacket dp = test.getRes();

			byte[] dpData = dp.getData();
			for (int i = 0; i < dpData.length; i++) {

				System.out.println("Data["+i+"]: " + dpData[i]);

			}*/

			//sc.sendToMSSQL(receiveWater.getWater());


		//}




		//ResourceCoil rc = new ResourceCOIL();



/*		ConstructCANFrame frame = new ConstructCANFrame();
		byte [] bFrame = new byte[13];
		bFrame = frame.stopTrain();

		for (int i = 0; i < bFrame.length ; i++) {
			System.out.print(bFrame[i]);

		}

		System.out.println("\nlength: " + bFrame.length);

		try {
			InetAddress ia = InetAddress.getByName(Attribute.sendingAddress);
			DatagramSocket ds = new DatagramSocket(Attribute.sendingPort);
			DatagramPacket dp = new DatagramPacket(bFrame, bFrame.length,ia,Attribute.sendingPort);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}*/





		/*//START USERINTERFACE
		final View.UserInterfaceChart uic = new View.UserInterfaceChart();
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
