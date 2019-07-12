//import javax.xml.bind.annotation.adapters.HexBinaryAdapter;


public class CanMain implements Attribute{


	protected static int coaches;



	public static void main(String[] args) {

		//ConstructCANFrame frame = new ConstructCANFrame();

		//SendCan send = new SendCan();

		GetCan receive = new GetCan("water");





		/*//START USERINTERFACE
		final UserInterfaceChart uic = new UserInterfaceChart();
		uic.go();
		*/

        //GET Data for Azure
		//GetCan DForAzure = new GetCan(ipAdress, 15731);
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
