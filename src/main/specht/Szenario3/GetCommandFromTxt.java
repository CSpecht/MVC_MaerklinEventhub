package specht.Szenario3;

import specht.General.Attribute;
import specht.General.ConstructCANFrame;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;


public class GetCommandFromTxt {

    Queue<byte[]> commandQueue;
    Queue<String> testQueue = new LinkedList();
    Queue<Integer> durrQueue = new LinkedList();
    boolean debug = false;

    byte[] udpFrame = new byte[13];

    public GetCommandFromTxt(Queue<byte[]> cmdQueue) throws IOException {
        this.commandQueue = cmdQueue;
    }

    public void processFile() throws IOException {
        String fileName = Attribute.cmdFilePath;
        File file = new File(fileName);
        FileInputStream fs = null;
        String line = "";
        int lineCount = 0;
        String [] tmp = new String[4];

        try {
            fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(fs);
        BufferedReader br = new BufferedReader(isr);


        while ((line = br.readLine()) != null) {
            for (int j = 0; j < 4; j++) {
                tmp = line.split("\\s+");

            }
            //System.out.println(line);
            processCommandLine(tmp,lineCount);
            lineCount++;
        }

    }

    public void processCommandLine(String[] commandArgs, int lineNumber) {
        String s = "";
        int id = 0;
        int durr = 0;


        if ((commandArgs == null) || (commandArgs.length == 0) || (commandArgs[0] == null)) {
            System.out.println("Null command!");
        }

        String component = commandArgs[0];
        String identicator = commandArgs[1];
        String command = commandArgs[2];

        if (commandArgs.length == 3) {
            durr = 0;
        }
        else {
            if (isInteger(commandArgs[3].trim())) {
                durr = Integer.parseInt(commandArgs[3]);
            }
            else {
                System.out.println("No Integer for Duration");
            }
        }

        if (isInteger(identicator.trim())) {
            id = Integer.parseInt(identicator.trim());
        } else {
            System.out.println("identicator isn't a number");
        }

        if (component.trim().equalsIgnoreCase("weiche")) {
            int position = 0;
            if (command.trim().equalsIgnoreCase("rechts")) {
                position = 0;
                commandQueue.add(translateSwitch(id, position));
                durrQueue.add(durr);
                testQueue.add(translateByteInStr(translateSwitch(id, position)));
            } else if (command.trim().equalsIgnoreCase("links")) {
                position = 1;
                commandQueue.add(translateSwitch(id, position));
                durrQueue.add(durr);
                testQueue.add(translateByteInStr(translateSwitch(id, position)));
            }
        }

        else if (component.trim().equalsIgnoreCase("lok")) {
            int speed = 0;
            int go = 0;

            if (isInteger(command.trim())) {
                speed = Integer.parseInt(command);
                commandQueue.add(translateLok(id, go));
                durrQueue.add(durr);
                testQueue.add(translateByteInStr(translateLok(id,go)));

            } else if (command.trim().equalsIgnoreCase("start")) {
                go = 1;
                commandQueue.add(translateLok(id, go));
                durrQueue.add(durr);
                testQueue.add(translateByteInStr(translateLok(id,go)));
            } else if (command.trim().equalsIgnoreCase("stopp")) {
                go = 0;
                commandQueue.add(translateLok(id, go));
                //s = component.trim().toUpperCase()+ " " + id + " " +  command.trim().toUpperCase();
                durrQueue.add(durr);
                testQueue.add(translateByteInStr(translateLok(id,go)));
            }
        }


        else if (component.trim().equalsIgnoreCase("signal")) {
            int signal = 0;
            if (command.trim().equalsIgnoreCase("rot")) {
                signal = 0;
                commandQueue.add(translateSignal(id, signal));
                durrQueue.add(durr);
                testQueue.add(translateByteInStr(translateSignal(id,signal)));
            } else if (command.trim().equalsIgnoreCase("gruen")) {
                signal = 1;
                commandQueue.add(translateSignal(id, signal));
                durrQueue.add(durr);
                testQueue.add(translateByteInStr(translateSignal(id,signal)));
            }
        }

        else if (command.trim().equalsIgnoreCase("clear")) {

        } else {
            System.out.println("Invalid Command! on Line: " + lineNumber);
        }

    }
    /*FIXME: test switch Position, and add ID FOR communicating with the right switch! */
    /************************* ADD RIGHT CAN FRAME AND *************************/
    public byte[] translateSwitch (int id, int switchPosition) {
        //switch right
        if (switchPosition == 0) {
            testQueue.add(translateByteInStr(ConstructCANFrame.setSwitchRWRedOff()));
            commandQueue.add(ConstructCANFrame.setSwitchRWRedOff());
            udpFrame = ConstructCANFrame.setSwitchRWGreenOn();

        } else {
            testQueue.add(translateByteInStr(ConstructCANFrame.setSwitchRWGreenOff()));
            commandQueue.add(ConstructCANFrame.setSwitchRWGreenOff());
            udpFrame = ConstructCANFrame.setSwitchRWRedOn();
        }
        return udpFrame;
    }

    /*FIXME how to add durration to BYTE[] for QUEUE!!!! */
    /************************* ADD RIGHT CAN FRAME *************************/
    public byte[] translateLok(int lokID, int speed, int time) {
        udpFrame = ConstructCANFrame.setSpeed(Attribute._SMLSTEAM_ID, 0);
        for (int i = 0; i < udpFrame.length; i++) {
            System.out.println("udpFrame ["+i+"]: " + udpFrame[i]);
        }
        return udpFrame;
    }

    /*FIXME TEST if it's working */
    /************************* TRANSLATE STOP / GO COMMAND INTO CAN MESSAGE *************************/
    public byte[] translateLok(int lokID, int go) {
        if (go == 0) {
            udpFrame = ConstructCANFrame.stopTrain();
        } else {
            udpFrame = ConstructCANFrame.go();
        }

        return udpFrame;
    }

    /************************* ADD RIGHT CAN FRAME *************************/
    public byte[] translateSignal(int signalID, int signal) {
        if (signal == 0) {
            testQueue.add(translateByteInStr(ConstructCANFrame.setLightSignalRedOff()));
            commandQueue.add(ConstructCANFrame.setLightSignalRedOff());
            udpFrame = ConstructCANFrame.setLightSignalGreenOn();
        }
        else {
            testQueue.add(translateByteInStr(ConstructCANFrame.setLightSignalGreenOff()));
            commandQueue.add(ConstructCANFrame.setLightSignalGreenOff());
            udpFrame = ConstructCANFrame.setLightSignalRedOn();
        }

        return udpFrame;
    }

    public boolean isInteger (String str) {
        try{
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public Queue<byte[]> getCommandQueue() {
        return commandQueue;
    }

    public Queue<Integer> getDurrQueue() {
        return durrQueue;
    }

    public Queue<String> getTestQueue() {
        return testQueue;
    }

    public String translateByteInStr (byte[] array) {
        String s = "";
        for (int i = 0; i < array.length ; i++) {
            if (i%2 == 0) {
                s+="  " + (String.valueOf(array[i])) + "  ";
            } else {
                s += (String.valueOf(array[i]));
            }


        }

        return s;
    }




    public void showTestQueue () {
        Iterator it = testQueue.iterator();
        while (it.hasNext()) {
            String s = (String) it.next();
            System.out.println(s);
        }
    }
}