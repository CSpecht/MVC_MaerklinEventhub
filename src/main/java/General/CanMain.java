package java.General;//import javax.xml.bind.annotation.adapters.HexBinaryAdapter;


import java.net.*;
import java.util.Date;

public class CanMain implements Attribute{


	protected static int coaches;



	public static void main(String[] args) {

		//General.ConstructCANFrame frame = new General.ConstructCANFrame();

		//General.SendCan send = new General.SendCan();

		//GetCan receive = new GetCan("water");

		//UdpConnection con = new UdpConnection();
		Date now = new Date();
		int pkNr = 0;
		byte[] Frame = new byte[13];
		InetAddress ia = null;
		long mills = now.getTime();
		try {
			ia = InetAddress.getByName(Attribute.sendingAddress);
			DatagramSocket ds = new DatagramSocket(Attribute.sendingPort);

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		DatagramPacket dp = new DatagramPacket(Frame,Frame.length,ia,Attribute.sendingPort);

		UdpPackage udpP = new UdpPackage(dp,pkNr,mills);
		udpP.sendout();

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

		byte[] udpFrame = new byte[13];
		String log = "";

		//Send Command to CS3
		//sendCommandToCS3();

	}













}
