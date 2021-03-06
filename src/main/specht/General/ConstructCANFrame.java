package specht.General;


/**
 * @author Cornelius Specht
 *
 */

public class ConstructCANFrame extends Thread{


    private static byte[] header = new byte[5];
    //uid 0x4006 cargo
    //uid 0x4007 dampf
    //dlc has to be 6 if you want to control the speed and uid has to be set
    private static int uid = 0;
    private static char response = 0;
    private static char command = 0;
    private static char prio = 0;
    private static char dlc = 5;
    private static char[] data = new char[dlc];
    private static char hash = 0;
    private static int cargoId = 0x4006;
    private static int steamId = 0x4007;
    private static boolean debug = false;



    /**
     * DEFAULT
     * Constructor
     */
    public ConstructCANFrame() {
        byte[] udpFrame= new byte[13];
        udpFrame[0] = (byte) prio; //(cargoId >> 24);
        udpFrame[1] = (byte) command;
        udpFrame[2] = (byte) 15; // >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;
    }

    /**
     * @param dataLength
     * Constructor
     */
    public ConstructCANFrame(int dataLength) {
        byte[] udpFrame= new byte[13];
        udpFrame[0] = (byte) (uid >> 24);
        udpFrame[1] = (byte) uid;
        udpFrame[2] = (byte) 15; // >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dataLength;
    }



//	private static final int sizeOfIntInHalfBytes = 8;
//	private static final int numberOfBitsInAHalfByte = 4;
//	private static final int halfByte = 0x0F;
//	private static final char[] hexDigits = {
//	    '0', '1', '2', '3', '4', '5', '6', '7',
//	    '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
//	};

    /**
     * @return udpFrame
     * send stop to all
     */
    public static byte[] stopTrain() {
        byte[] udpFrame= new byte[13];
        dlc = 5;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 0;
        udpFrame[2] = (byte) 15; // >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;
        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];

            if (i <= 4) {
                udpFrame[5+i] = (byte)0;
            }

            if (i >= 4) {
                udpFrame[5+i] = 0;
            }
        }
        return udpFrame;
    }

    /**
     * @param id
     * id of the train
     * @return udpFrame
     * send stop command to the train id
     */
    public static byte[] stop(int id)   {
        byte[] udpFrame= new byte[13];
        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];

            if (i == 0) {
                udpFrame[5+i] = 0;
            }
            if (i == 1) {
                udpFrame[5+i] = 0;
            }
            if (i == 2) {
                if (debug) {
                    System.out.println("id: " + getFirstByteOfId(id));
                }
                udpFrame[5+i] = (byte) getFirstByteOfId(id);
            }

            if (i == 3) {
                if (debug) {
                    System.out.println("id2: " + getSecondByteOfId(id));
                }
                udpFrame[5+i] = (byte) getSecondByteOfId(id);
            }

            if (i >= 4) {
                udpFrame[5+i] = 0;
            }
        }
        return udpFrame;
    }

    public static byte[] emergencyStop(int id) {
        byte[] udpFrame= new byte[13];
        dlc = 5;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 0;
        udpFrame[2] = (byte) 15; // >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;
        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
            if (i == 2) {
                udpFrame[5+i] = (byte) getFirstByteOfId(id);
            }

            if (i == 3) {
                udpFrame[5+i] = (byte) getSecondByteOfId(id);
            }

            if (i == 4) {
                udpFrame[5+i] = 3;
            }
        }
        return udpFrame;

    }

    /**
     * @param id
     * id of the train
     * @return udpFrame
     * send horn on at train id
     */
    public static byte[] hornOn(int id) {
        byte[] udpFrame= new byte[13];
        dlc = 6;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 12;
        udpFrame[2] = (byte) 15; // >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;
        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
            if (i == 2) {
                if (debug) {
                    System.out.println("id: " + getFirstByteOfId(id));
                }
                udpFrame[5+i] = (byte) getFirstByteOfId(id);
            }

            if (i == 3) {
                if (debug) {
                    System.out.println("id2: " + getSecondByteOfId(id));
                }
                udpFrame[5+i] = (byte) getSecondByteOfId(id);
            }

            if (i == 4) {
                udpFrame[5+i] = 3;
            }
            if (i == 5) {
                udpFrame[5+i] = 1;
            }
        }
        return udpFrame;
    }

    /**
     * @param id
     * id of the train
     * @return udpFrame
     * send horn off to a train ID
     */
    public static byte[] hornOff(int id) {
        byte[] udpFrame= new byte[13];
        dlc = 6;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 12;
        udpFrame[2] = (byte) 15; // >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;
        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
            if (i == 2) {
                if (debug) {
                    System.out.println("id: " + getFirstByteOfId(id));
                }
                udpFrame[5+i] = (byte) getFirstByteOfId(id);
            }

            if (i == 3) {
                if (debug) {
                    System.out.println("id2: " + getSecondByteOfId(id));
                }
                udpFrame[5+i] = (byte) getSecondByteOfId(id);
            }

            if (i == 4) {
                udpFrame[5+i] = 3;
            }
            if (i == 5) {
                udpFrame[5+i] = 0;
            }
        }
        return udpFrame;

    }

    /**
     * @param id
     * id of the train
     * @return udpFrame
     * Send Light On to a train Id
     */
    public static byte[] lightOn(int id) {
        byte[] udpFrame= new byte[13];
        dlc = 6;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 12;
        udpFrame[2] = (byte) 15; // >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;
        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
            if (i == 2) {
                if (debug) {
                    System.out.println("id: " + getFirstByteOfId(id));
                }
                udpFrame[5+i] = (byte) getFirstByteOfId(id);
            }

            if (i == 3) {
                if (debug) {
                    System.out.println("id2: " + getSecondByteOfId(id));
                }
                udpFrame[5+i] = (byte) getSecondByteOfId(id);
            }

            if (i == 4) {
                udpFrame[5+i] = 0;
            }
            if (i == 5) {
                udpFrame[5+i] = 1;
            }
        }
        return udpFrame;

    }

    /**
     * @param id
     * id of the train
     * @return udpFrame
     * Send Light off to a TrainID
     */
    public static byte[] lightOff(int id) {
        byte[] udpFrame= new byte[13];
        dlc = 6;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 12;
        udpFrame[2] = (byte) 15; // >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;
        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
            if (i == 2) {
                if (debug) {
                    System.out.println("id: " + getFirstByteOfId(id));
                }
                udpFrame[5+i] = (byte) getFirstByteOfId(id);
            }

            if (i == 3) {
                if (debug) {
                    System.out.println("id2: " + getSecondByteOfId(id));
                }
                udpFrame[5+i] = (byte) getSecondByteOfId(id);
            }

            if (i == 4) {
                udpFrame[5+i] = 0;
            }
            if (i == 5) {
                udpFrame[5+i] = 0;
            }
        }
        return udpFrame;

    }

    /**
     * @return udpFrame
     * Send go to All
     */
    public static byte[] go() {
        byte[] udpFrame= new byte[13];
        dlc = 5;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 0;
        udpFrame[2] = (byte) 15; // >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;
        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];

            if (i <= 3) {
                udpFrame[5+i] = (byte)0;
            }

            if (i > 3) {
                udpFrame[5+i] = 1;
            }
        }
        return udpFrame;
    }

    /**
     * @param id
     * id of the train
     * @return udpFrame
     * Give a go for the provided train ID
     */
    public static byte[] go(int id) {
        byte[] udpFrame= new byte[13];
        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
            if (i == 2) {
                udpFrame[5+i] = 0;//(byte) getFirstByteOfId(id);
            }
            if (i == 3) {
                udpFrame[5+i] = (byte) id;//getSecondByteOfId(id);
            }
            if (i >= 4) {
                udpFrame[5+i] = 1;
            }
        }
        return udpFrame;
    }

    /**
     * @param speed
     * The speed that we want to set up
     * @return udpFrame
     * The MAX SPEEED 1023!!!
     * DLC HAS TO BE 6 to SET THE SPEED Otherwise you can't set the SPEED, defined by Maerklin
     */
    public static byte[] setSpeed (int locID,int speed) {
        byte[] udpFrame= new byte[13];
        //dlc = 6 mandatory to set the speed!
        dlc = 6;
        data = new char[dlc];
        udpFrame[0] = (byte) prio;
        udpFrame[1] = (byte) 8;
        udpFrame[2] = (byte) 15; // >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;

        byte[] speedArr = toByteArray(speed );

        if (debug) {
            for (int i = 0; i < speedArr.length; i++) {
                System.out.println("speedArr[" + i + "]: " + speedArr[i]);
            }
        }
        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
        }

        for (int i = 0; i < data.length; i++) {

            if (i == 2) {
                udpFrame[5+i] = (byte)getFirstByteOfId(locID);
            }
            if (i == 3) {
                udpFrame[5+i] = (byte)getSecondByteOfId(locID);
                //System.out.println("LOOOOOOOOOOCID: " + (byte)getSecondByteOfId(locID));
            }
            if (i == 4) {
                udpFrame[5+i] = speedArr[2];//(byte)getFirstByteOfSpeed(speed);
            }
            if (i == 5) {
                udpFrame[5+i] = speedArr[3];//(byte) getSecondByteOfSpeed(newerGetByteOfSecondID(speed));

            }

            /*           if (i == 4 && hexData.length == 2) {
                udpFrame[5+i] = hexData[1];
            }
            if (i == 5) {
                udpFrame[5+i] = hexData[0];
            }
*/
        }
        return udpFrame;
    }

    /**
     * @return udpFrame
     * The MAX SPEEED 1023!!!
     * DLC HAS TO BE 4 to get THE SPEED Otherwise you can't set the SPEED, defined by Maerklin
     */
    public static byte[] getSpeed(int locID) {
        byte[] udpFrame= new byte[13];
        //dlc = 4 mandatory to get the speed!
        dlc = 4;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 8;
        udpFrame[2] = (byte) 15; // >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;

        //String s = intToHex(speed);
        //System.out.println("hexString :" + s);
        //byte[] hexData = hexStringToByteArray(s);
        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
        }

        for (int i = 0; i < data.length; i++) {

            if (i == 2) {
                udpFrame[5+i] = (byte)getFirstByteOfId(locID);
            }
            if (i == 3) {
                udpFrame[5+i] = (byte)getSecondByteOfId(locID);
            }
//			if (i == 4 && hexData.length == 2) {
//				udpFrame[5+i] = hexData[1];
//			}
//			if (i == 5) {
//				udpFrame[5+i] = hexData[0];
//			}

        }
        return udpFrame;
    }

    public static byte[] resourceStart() {
        byte[] udpFrame= new byte[13];
        dlc = 7;
        data = new char[8];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 14;
        udpFrame[2] = (byte) 10; // >> 8;//(uid >> 8); 160
        udpFrame[3] = (byte) 1798;
        udpFrame[4] = (byte) dlc;
        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
        }

        for (int i = 0; i < data.length; i++) {
            if (i == 0) {
                udpFrame[5+i] = (byte)99;
            }
            if (i == 1) {
                udpFrame[5 + i] = (byte) 115;
            }
            if (i == 2) {
                udpFrame[5+i] = (byte)81;
            }
            if (i == 3) {
                udpFrame[5+i] = (byte)44;
            }
            if (i == 4) {
                udpFrame[5+i] = (byte)48;
            }
            if (i == 5) {
                udpFrame[5+i] = (byte)2;
            }
            if (i == 6) {
                udpFrame[5+i] = (byte)4;
            }
        }
        return udpFrame;
    }
    public static byte[] resourceStop() {
        byte[] udpFrame= new byte[13];
        dlc = 6;
        data = new char[8];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 14;
        udpFrame[2] = (byte) 10; // >> 8;//(uid >> 8); 160
        udpFrame[3] = (byte) 1798;
        udpFrame[4] = (byte) dlc;
        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
        }

        for (int i = 0; i < data.length; i++) {
            if (i == 0) {
                udpFrame[5+i] = (byte)99;
            }
            if (i == 1) {
                udpFrame[5 + i] = (byte) 115;
            }
            if (i == 2) {
                udpFrame[5+i] = (byte)81;
            }
            if (i == 3) {
                udpFrame[5+i] = (byte)44;
            }
            if (i == 4) {
                udpFrame[5+i] = (byte)48;
            }
            if (i == 5) {
                udpFrame[5+i] = (byte)0;
            }
        }
        return udpFrame;
    }

    //SEND THIS COMMAND RESPOND WITH ALL RESSOURCES BECUASE OF THE IDX
    public static byte[] getWater(int locID) {
        byte[] udpFrame= new byte[13];
        dlc = 7;
        data = new char[8];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 14;
        udpFrame[2] = (byte) 15; // >> 8;//(uid >> 8); 160
        udpFrame[3] = (byte) 114; //1798
        udpFrame[4] = (byte) dlc;
	/*
		dlc = 6;
		data = new char[dlc];
		udpFrame[0] = (byte) prio ;
		udpFrame[1] = (byte) 7;
		udpFrame[2] = (byte) 15; // >> 8;//(uid >> 8);
		udpFrame[3] = (byte) 114;
		udpFrame[4] = (byte) dlc;
	*/
        //String s = intToHex(speed);
        //System.out.println("hexString :" + s);
        //byte[] hexData = hexStringToByteArray(s);
        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
        }

        for (int i = 0; i < data.length; i++) {

            if (i == 2) {
                udpFrame[5+i] = (byte)getFirstByteOfId(locID);
            }
            if (i == 3) {
                udpFrame[5+i] = (byte)getSecondByteOfId(locID);
            }
            if (i == 4) {
                udpFrame[5+i] = (byte)4; //4
            }
            if (i == 5) {
                udpFrame[5+i] = (byte)237;
            }
            if (i == 6) {
                udpFrame[5+i] = (byte)1; //3//0
            }
        }
        return udpFrame;
    }

    public static byte[] getSand(int locID) {
        byte[] udpFrame= new byte[13];
        dlc = 7;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 14;
        udpFrame[2] = (byte) 15; // >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;

        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
        }

        for (int i = 0; i < data.length; i++) {

            if (i == 2) {
                udpFrame[5+i] = (byte)getFirstByteOfId(locID);
            }
            if (i == 3) {
                udpFrame[5+i] = (byte)getSecondByteOfId(locID);
            }
            if (i == 4) {
                udpFrame[5+i] = (byte)12; //idx number
            }
            if (i == 5) {
                udpFrame[5+i] = (byte)237;
            }
            if (i == 6) {
                udpFrame[5+i] = (byte)1;
            }
        }
        return udpFrame;
    }

    public static byte[] getCoil(int locID) {
        byte[] udpFrame= new byte[13];
        dlc = 7;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 14;
        udpFrame[2] = (byte) 15; // >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;

        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
        }

        for (int i = 0; i < data.length; i++) {

            if (i == 2) {
                udpFrame[5+i] = (byte)getFirstByteOfId(locID);
            }

            if (i == 3) {
                udpFrame[5+i] = (byte)getSecondByteOfId(locID);
            }

            if (i == 4) {
                udpFrame[5+i] = (byte)8;
            }

            if (i == 5) {
                udpFrame[5+i] = (byte)237;
            }
            if (i == 6) {
                udpFrame[5+i] = (byte)1;
            }
        }
        return udpFrame;
    }
    public static byte[] getRound() {
        byte[] udpFrame= new byte[13];
        //dlc = 5 mandatory to set the speed!
        dlc = 4;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 34; //16
        udpFrame[2] = (byte) 167; //15// >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 6;
        udpFrame[4] = (byte) dlc;


        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
        }

        for (int i = 0; i < data.length; i++) {
            if (i == 0) {
                udpFrame[5+i] = (byte)0;
            }
            if (i == 1) {
                udpFrame[5+i] = (byte)1;
            }
            if (i == 2) {
                udpFrame[5+i] = (byte)0;
            }
            if (i == 3) {
                udpFrame[5+i] = (byte)9;
            }
         /*   if (i == 4) {
                udpFrame[5+i] = (byte)22;
            }
*/
        }
        return udpFrame;
    }
    /**
     //* @param locID
     * Locomotive ID to fill up water 100%
     * @return udpFrame
     */
    public static byte[] setWater (int locID) { //int oilAmount
        byte[] udpFrame= new byte[13];
        //dlc = 6 mandatory to set the speed!
        dlc = 8;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 16; //16
        udpFrame[2] = (byte) 15; //15// >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;

        String s = intToHex(255);
        if (debug) {
            System.out.println("hexString :" + s);
        }
        byte[] hexData = hexStringToByteArray(s);

        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
        }

        for (int i = 0; i < data.length; i++) {

            if (i == 2) {
                udpFrame[5+i] = (byte)getFirstByteOfId(locID);
            }
            if (i == 3) {
                udpFrame[5+i] = (byte)getSecondByteOfId(locID);
            }
            if (i == 4) {
                udpFrame[5+i] = (byte) 4;
            }
            if(i == 5) {
                udpFrame[5+i] = (byte) 237;
            }
            if(i == 6) {
                udpFrame[5+i] = (byte) 255;
            }
            if(i == 7) {
                udpFrame[5+i] = (byte) 0;
            }
/*			if (i == 7 && hexData.length == 2) {
				//08ED01
				udpFrame[5+i] = (byte) (hexData[0] + hexData[1]);
			}
*/
        }
        return udpFrame;
    }

    /**
     //* @param locID
     * Locomotive ID to fill up coil 100%
     * @return udpFrame
     */
    public static byte[] setCoil (int locID) { //int oilAmount
        byte[] udpFrame= new byte[13];
        //dlc = 6 mandatory to set the speed!
        dlc = 8;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 16; //16
        udpFrame[2] = (byte) 15; //15// >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;

        String s = intToHex(255);
        if (debug) {
            System.out.println("hexString :" + s);
        }
        byte[] hexData = hexStringToByteArray(s);

        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
        }

        for (int i = 0; i < data.length; i++) {

            if (i == 2) {
                udpFrame[5+i] = (byte)getFirstByteOfId(locID);
            }
            if (i == 3) {
                udpFrame[5+i] = (byte)getSecondByteOfId(locID);
            }
            if (i == 4) {
                udpFrame[5+i] = (byte) 8;
            }
            if(i == 5) {
                udpFrame[5+i] = (byte) 237;
            }
            if(i == 6) {
                udpFrame[5+i] = (byte) 255;
            }
            if(i == 7) {
                udpFrame[5+i] = (byte) 0;
            }
/*			if (i == 7 && hexData.length == 2) {
				//08ED01
				udpFrame[5+i] = (byte) (hexData[0] + hexData[1]);
			}
*/
        }
        return udpFrame;
    }

    /**
     //* @param locID
     * Locomotive ID to set the sand
     * @return udpFrame
     */
    public static byte[] setSand (int locID) { //int oilAmount
        byte[] udpFrame= new byte[13];
        //dlc = 6 mandatory to set the speed!
        dlc = 8;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 16; //16
        udpFrame[2] = (byte) 15; //15// >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;

        String s = intToHex(255);
        if (debug) {
            System.out.println("hexString :" + s);
        }
        byte[] hexData = hexStringToByteArray(s);

        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
        }

        for (int i = 0; i < data.length; i++) {

            if (i == 2) {
                udpFrame[5+i] = (byte)getFirstByteOfId(locID);
            }
            if (i == 3) {
                udpFrame[5+i] = (byte)getSecondByteOfId(locID);
            }
            if (i == 4) {
                udpFrame[5+i] = (byte) 12;
            }
            if(i == 5) {
                udpFrame[5+i] = (byte) 237;
            }
            if(i == 6) {
                udpFrame[5+i] = (byte) 255;
            }
            if(i == 7) {
                udpFrame[5+i] = (byte) 0;
            }
/*			if (i == 7 && hexData.length == 2) {
				//08ED01
				udpFrame[5+i] = (byte) (hexData[0] + hexData[1]);
			}
*/
        }
        return udpFrame;
    }

    /**
     * @param direction
     * sets the direction of the train based on the documentation rules
     * @return udpFrame
     * DLC HAS TO BE 5 TO SET THE DIRECTION (Maerklin)
     * DLC HAS TO BE 4 TO GET THE DIRECTION (Maerklin)
     * set the direction there are 4 options.
     * 0 = direction remain
     * 1 = direction forwards
     * 2 = direction backwards
     * 3 = direction switch
     */
    public static byte [] setDirection (int locID, int direction) {
        byte[] udpFrame= new byte[13];
        //dlc = 6 mandatory to set the speed!
        dlc = 5;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 10; //16
        udpFrame[2] = (byte) 15; //15// >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;


        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
        }

        for (int i = 0; i < data.length; i++) {
            if (i == 2) {
                udpFrame[5+i] = (byte)getFirstByteOfId(locID);
            }
            if (i == 3) {
                udpFrame[5+i] = (byte)getSecondByteOfId(locID);
            }
            if (i == 4) {
                udpFrame[5+i] = (byte)direction;
            }
        }
        return udpFrame;
    }


    /**
     * @return udpFrame
     * DLC HAS TO BE 8
     * switch the position of the Switch
     */
    public static byte [] setSwitchRWSidingRedOn () {
        byte[] udpFrame= new byte[13];
        //dlc = 6 mandatory to set the speed!
        dlc = 8;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 22; //16
        udpFrame[2] = (byte) 15; //15// >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;


        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
        }

        for (int i = 0; i < data.length; i++) {
            if (i == 2) {
                udpFrame[5+i] = (byte)56;
            }
            if (i == 3) {
                udpFrame[5+i] = (byte)4;
            }
            if (i == 4) {
                udpFrame[5+i] = (byte)0;
            }
            if (i == 5) {
                udpFrame[5+i] = (byte)1;
            }
        }
        return udpFrame;
    }

    /**
     * @return udpFrame
     * DLC HAS TO BE 8
     * switch the position of the Switch
     */

    public static byte [] setSwitchRWSidingRedOff () {
        byte[] udpFrame= new byte[13];
        //dlc = 6 mandatory to set the speed!
        dlc = 8;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 22; //16
        udpFrame[2] = (byte) 15; //15// >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;


        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
        }

        for (int i = 0; i < data.length; i++) {
            if (i == 2) {
                udpFrame[5+i] = (byte)56;
            }
            if (i == 3) {
                udpFrame[5+i] = (byte)4;
            }
            if (i == 4) {
                udpFrame[5+i] = (byte)0;
            }
            if (i == 5) {
                udpFrame[5+i] = (byte)0;
            }
        }
        return udpFrame;
    }
    /**
     * @return udpFrame
     * DLC HAS TO BE 8
     * switch the position of the Switch
     */

    public static byte [] setSwitchRWSidingGreenOff () {
        byte[] udpFrame= new byte[13];
        //dlc = 6 mandatory to set the speed!
        dlc = 8;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 22; //16
        udpFrame[2] = (byte) 15; //15// >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;


        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
        }

        for (int i = 0; i < data.length; i++) {
            if (i == 2) {
                udpFrame[5+i] = (byte)56;
            }
            if (i == 3) {
                udpFrame[5+i] = (byte)4;
            }
            if (i == 4) {
                udpFrame[5+i] = (byte)1;
            }
            if (i == 5) {
                udpFrame[5+i] = (byte)0;
            }
        }
        return udpFrame;
    }

    /**
     * @return udpFrame
     * DLC HAS TO BE 8
     * switch the position of the Switch
     */

    public static byte [] setSwitchRWSidingGreenOn () {
        byte[] udpFrame= new byte[13];
        //dlc = 6 mandatory to set the speed!
        dlc = 8;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 22; //16
        udpFrame[2] = (byte) 15; //15// >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;


        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
        }

        for (int i = 0; i < data.length; i++) {
            if (i == 2) {
                udpFrame[5+i] = (byte)56;
            }
            if (i == 3) {
                udpFrame[5+i] = (byte)4;
            }
            if (i == 4) {
                udpFrame[5+i] = (byte)1;
            }
            if (i == 5) {
                udpFrame[5+i] = (byte)1;
            }
        }
        return udpFrame;
    }

    /**
     * @return udpFrame
     * DLC HAS TO BE 8
     * switch the position of the Switch
     */
    public static byte [] setSwitchRWRedOn (int id) {
        byte[] udpFrame= new byte[13];
        //dlc = 6 mandatory to set the speed!
        dlc = 8;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 22; //16
        udpFrame[2] = (byte) 15; //15// >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;


        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
        }

        for (int i = 0; i < data.length; i++) {
            if (i == 2) {
                udpFrame[5+i] = (byte)56;
            }
            if (i == 3) {
                udpFrame[5+i] = (byte)id;
            }
            if (i == 4) {
                udpFrame[5+i] = (byte)0;
            }
            if (i == 5) {
                udpFrame[5+i] = (byte)1;
            }
        }
        return udpFrame;
    }

    /**
     * @return udpFrame
     * DLC HAS TO BE 8
     * switch the position of the Switch
     */

    public static byte [] setSwitchRWRedOff (int id) {
        byte[] udpFrame= new byte[13];
        //dlc = 6 mandatory to set the speed!
        dlc = 8;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 22; //16
        udpFrame[2] = (byte) 15; //15// >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;


        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
        }

        for (int i = 0; i < data.length; i++) {
            if (i == 2) {
                udpFrame[5+i] = (byte)56;
            }
            if (i == 3) {
                udpFrame[5+i] = (byte)id;
            }
            if (i == 4) {
                udpFrame[5+i] = (byte)0;
            }
            if (i == 5) {
                udpFrame[5+i] = (byte)0;
            }
        }
        return udpFrame;
    }
    /**
     * @return udpFrame
     * DLC HAS TO BE 8
     * switch the position of the Switch
     */

    public static byte [] setSwitchRWGreenOff (int id) {
        byte[] udpFrame= new byte[13];
        //dlc = 6 mandatory to set the speed!
        dlc = 8;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 22; //16
        udpFrame[2] = (byte) 15; //15// >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;


        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
        }

        for (int i = 0; i < data.length; i++) {
            if (i == 2) {
                udpFrame[5+i] = (byte)56;
            }
            if (i == 3) {
                udpFrame[5+i] = (byte)id;
            }
            if (i == 4) {
                udpFrame[5+i] = (byte)1;
            }
            if (i == 5) {
                udpFrame[5+i] = (byte)0;
            }
        }
        return udpFrame;
    }

    /**
     * @return udpFrame
     * DLC HAS TO BE 8
     * switch the position of the Switch
     */

    public static byte [] setSwitchRWGreenOn (int id) {
        byte[] udpFrame= new byte[13];
        //dlc = 6 mandatory to set the speed!
        dlc = 8;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 22; //16
        udpFrame[2] = (byte) 15; //15// >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;


        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
        }

        for (int i = 0; i < data.length; i++) {
            if (i == 2) {
                udpFrame[5+i] = (byte)56;
            }
            if (i == 3) {
                udpFrame[5+i] = (byte)id;
            }
            if (i == 4) {
                udpFrame[5+i] = (byte)1;
            }
            if (i == 5) {
                udpFrame[5+i] = (byte)1;
            }
        }
        return udpFrame;
    }


    public static byte [] setLightSignalRedOn () {
        byte[] udpFrame= new byte[13];
        //dlc = 6 mandatory to set the Signal!
        dlc = 6;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 22; //16
        udpFrame[2] = (byte) 15; //15// >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;


        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
        }

        for (int i = 0; i < data.length; i++) {
            if (i == 2) {
                udpFrame[5+i] = (byte)56;
            }
            if (i == 3) {
                udpFrame[5+i] = (byte)15;
            }
            if (i == 4) {
                udpFrame[5+i] = (byte)0;
            }
            if (i == 5) {
                udpFrame[5+i] = (byte)1;
            }
        }
        return udpFrame;
    }

    public static byte [] setLightSignalRedOff () {
        byte[] udpFrame= new byte[13];
        //dlc = 6 mandatory to set the speed!
        dlc = 8;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 22; //16
        udpFrame[2] = (byte) 15; //15// >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;


        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
        }

        for (int i = 0; i < data.length; i++) {
            if (i == 2) {
                udpFrame[5+i] = (byte)56;
            }
            if (i == 3) {
                udpFrame[5+i] = (byte)0;
            }
            if (i == 4) {
                udpFrame[5+i] = (byte)0;
            }
            if (i == 5) {
                udpFrame[5+i] = (byte)0;
            }
        }
        return udpFrame;
    }

    public static byte [] setLightSignalGreenOn () {
        byte[] udpFrame= new byte[13];
        //dlc = 6 mandatory to set the speed!
        dlc = 8;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 22; //16
        udpFrame[2] = (byte) 15; //15// >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;


        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
        }

        for (int i = 0; i < data.length; i++) {
            if (i == 2) {
                udpFrame[5+i] = (byte)56;
            }
            if (i == 3) {
                udpFrame[5+i] = (byte)15;
            }
            if (i == 4) {
                udpFrame[5+i] = (byte)1;
            }
            if (i == 5) {
                udpFrame[5+i] = (byte)1;
            }
        }
        return udpFrame;
    }

    public static byte [] setLightSignalGreenOff () {
        byte[] udpFrame= new byte[13];
        //dlc = 6 mandatory to set the speed!
        dlc = 8;
        data = new char[dlc];
        udpFrame[0] = (byte) prio ;
        udpFrame[1] = (byte) 22; //16
        udpFrame[2] = (byte) 15; //15// >> 8;//(uid >> 8);
        udpFrame[3] = (byte) 114;
        udpFrame[4] = (byte) dlc;


        for (int i = 0; i < data.length; i++) {
            udpFrame[5+i] = (byte)data[i];
        }

        for (int i = 0; i < data.length; i++) {
            if (i == 2) {
                udpFrame[5+i] = (byte)56;
            }
            if (i == 3) {
                udpFrame[5+i] = (byte)0;
            }
            if (i == 4) {
                udpFrame[5+i] = (byte)1;
            }
            if (i == 5) {
                udpFrame[5+i] = (byte)0;
            }
        }
        return udpFrame;
    }



    /**
     * @param value
     * the value that we will convert
     * @return hex String
     * Transform an Integer Value into an HEX Value
     */
    public static String intToHex(int value) {
        Integer i = value;
        String hex = Integer.toHexString(i); //"0" +
        int hexCount = hex.length();
        //ADD LEADING ZERO IF THE HEX is UNEVEN
        if(hexCount % 2 != 0) {
            String zero = "0";
            hex = zero + hex.substring(0);
        }
        //String hex = Integer.toHexString(i);
        //System.out.println("Test: " + hex);
        return hex;
    }

    /**
     * @param s
     * The string that we will transform into ByteArray
     * @return data
     * Get an Hex String and
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }


    /**
     * @param id
     *
     * @return result
     */
    public static int getFirstByteOfId (int id) {
        int res = (id >>> 8);
        //System.out.println("FirstByte: " + res);
        return res;
    }

    /**
     * @param id
     * @return result
     */
    public static int getSecondByteOfId (int id) {
        //Shift the bits to the left and then shift it to the right to get the last 4 bit
        int res = ((id << 27)>> 27);
        //System.out.println("SecondByte: " + res);
        return res;
    }

    public static byte[] toByteArray(int value) {
        return new byte[] {
                (byte)(value >> 24),
                (byte)(value >> 16),
                (byte)(value >> 8),
                (byte)value};
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

    }

}
