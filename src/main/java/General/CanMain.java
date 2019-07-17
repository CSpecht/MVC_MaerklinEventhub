package java.General;//import javax.xml.bind.annotation.adapters.HexBinaryAdapter;


public class CanMain implements Attribute{


	protected static int coaches;



	public static void main(String[] args) {

		//General.ConstructCANFrame frame = new General.ConstructCANFrame();

		//General.SendCan send = new General.SendCan();

		GetCan receive = new GetCan("water");




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
