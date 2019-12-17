package specht.Szenario3;

import org.w3c.dom.Attr;
import specht.General.Attribute;
import specht.General.ConstructCANFrame;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import static java.lang.Thread.sleep;


public class GetCommandFromTxt {

    Queue<byte[]> commandQueue = new LinkedList<>();
    Queue<String> testQueue = new LinkedList();
    Queue<Integer> durrQueue = new LinkedList();

    boolean debug = false;

    //byte[] udpFrame = null;
/*
    public GetCommandFromTxt(Queue<byte[]> cmdQueue) throws IOException {
        this.commandQueue = cmdQueue;
    }
*/
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
            //4
            for (int j = 0; j < line.length(); j++) {
                tmp = line.split("\\s+");
            }
            //System.out.println(line);
            //udpFrame = new byte[13];
            try {
                processCommandLine(tmp,lineCount);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lineCount++;
        }

    }

    public void processCommandLine(String[] commandArgs, int lineNumber) throws InterruptedException {
        String s = "";
        int id = 0;
        int durr = 0;
        String component = "";
        String identicator = "";
        String command = "";


        if ((commandArgs == null) || (commandArgs.length == 0) || (commandArgs[0] == null)) {
            System.out.println("Null command!");
            throw new IllegalArgumentException("not a command on Line:" + lineNumber);

        } else if (commandArgs.length > 3) {
            System.out.println("command is too long");
            throw new IllegalArgumentException("command is too long on Line:" + lineNumber);
        } else if (commandArgs.length == 2) { //Only Command with Length of 2 --> Sleep
            if (isInteger(commandArgs[1].trim()) && commandArgs[0].equals("sleep")) {
                component = commandArgs[0];
                durr = Integer.parseInt(commandArgs[1]);
            } else {
                System.out.println("Not a recognized command!");
                throw new IllegalArgumentException("not a recognized command on Line: " + lineNumber);
            }
        } else if (commandArgs.length == 3) { //Normal Command with {Component}{Indicator}{Command}
            durr = 0;
            component = commandArgs[0];
            identicator = commandArgs[1];
            command = commandArgs[2];

            if (isInteger(identicator.trim())) {
                id = Integer.parseInt(identicator.trim());
            } else {
                System.out.println("identicator isn't a number");
                throw new IllegalArgumentException("identicator isn't a number on line: " + lineNumber);
            }


        }
        /*else if (commandArgs.length == 4) { //Command with Duration {Component}{Indicator}{Command}{durration}
            if (isInteger(commandArgs[3].trim())) {
                component = commandArgs[0];
                identicator = commandArgs[1];
                command = commandArgs[2];
                durr = Integer.parseInt(commandArgs[3]);
            }
            else {
                System.out.println("No Integer for Duration");
            }
        }
*/


        if (component.trim().equalsIgnoreCase("weiche")) {
            int position = 0;

            if (command.trim().equalsIgnoreCase("rechts")) {
                position = 0;
                commandQueue.add(translateSwitch(id,position));
                durrQueue.add(durr);
                testQueue.add(translateByteInStr(translateSwitch(id, position)));
            } else if (command.trim().equalsIgnoreCase("links")) {
                position = 1;
                commandQueue.add(translateSwitch(id, position));
                durrQueue.add(durr);
                testQueue.add(translateByteInStr(translateSwitch(id, position)));
            }
        }
        else if (component.trim().equalsIgnoreCase("sleep")) {
            int position = 0;
            durrQueue.add(durr);

        }

        else if (component.trim().equalsIgnoreCase("lok")) {
            int speed = 0;
            int go = 0;
            if(id == 0) {
                id = Attribute.getCargoId();
            } else {
                id = Attribute.getSmlsteamId();
            }
            if (isInteger(command.trim())) {
                speed = Integer.parseInt(command);
                commandQueue.add(translateLok(id, speed, 0));
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
            } else if (command.trim().equalsIgnoreCase("hupeOn")) {
                System.out.println("HELLO HORN!");
                commandQueue.add(translateHorn(id));
                durrQueue.add(durr);
            } else if (command.trim().equalsIgnoreCase("hupeOff")) {
                System.out.println("HELLO HORN!");
                commandQueue.add(translateHornOff(id));
                durrQueue.add(durr);
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
        } else if (component.trim().equalsIgnoreCase("fahrtrichtung")) {
            int direction = 0;
            int lokID = 0;
            if (id == 0) {
                lokID = Attribute.getCargoId();
            } else {
                lokID = Attribute.getSmlsteamId();
            }
            if (command.trim().equalsIgnoreCase("vorwaerts")) {
                direction = 1;
                System.out.println("direction: " + direction);
                commandQueue.add(translateDirection(lokID, direction));
                durrQueue.add(durr);
            } else if (command.trim().equalsIgnoreCase("rueckwaerts")) {
                direction = 2;
                System.out.println("direction: " + direction);
                System.out.println("id: " + lokID);
                commandQueue.add(translateDirection(lokID, direction));
                durrQueue.add(durr);
            }
        } else {
            System.out.println("Invalid Command! on Line: " + lineNumber);
            throw new IllegalArgumentException("Invalid Command on line: " + lineNumber);
        }

    }
    /*FIXME: test switch Position, and add ID FOR communicating with the right switch! */
    /************************* ADD RIGHT CAN FRAME AND *************************/
    public byte[] translateSwitch (int id, int switchPosition) {
        byte[] udpFrame = new byte[13];
        //switch right
        if (switchPosition == 0) {
            udpFrame = ConstructCANFrame.setSwitchRWGreenOn(id);
            System.out.println(translateByteInStr(udpFrame));

        } else {
            udpFrame = ConstructCANFrame.setSwitchRWRedOn(id);
            System.out.println(translateByteInStr(udpFrame));
        }
        return udpFrame;
    }

    /*FIXME how to add durration to BYTE[] for QUEUE!!!! */
    /************************* ADD RIGHT CAN FRAME *************************/
    public byte[] translateLok(int lokID, int speed, int time) {
        byte[] udpFrame = new byte[13];
        udpFrame = ConstructCANFrame.setSpeed(lokID, speed);
        for (int i = 0; i < udpFrame.length; i++) {
            System.out.println("udpFrame ["+i+"]: " + udpFrame[i]);
        }
        return udpFrame;
    }

    /*FIXME TEST if it's working */
    /************************* TRANSLATE STOP / GO COMMAND INTO CAN MESSAGE *************************/
    public byte[] translateLok(int lokID, int go) {
        byte[] udpFrame = new byte[13];
        if (go == 0) {
            udpFrame = ConstructCANFrame.stop(lokID);
        } else {
            udpFrame = ConstructCANFrame.go(lokID);
        }

        return udpFrame;
    }

    /************************* ADD RIGHT CAN FRAME *************************/
    public byte[] translateSignal(int signalID, int signal) {
        byte[] udpFrame = new byte[13];
        if (signal == 0) {
            //testQueue.add(translateByteInStr(ConstructCANFrame.setLightSignalRedOff()));
            //commandQueue.add(ConstructCANFrame.setLightSignalRedOff());
            udpFrame = ConstructCANFrame.setLightSignalRedOn();
        }
        else {
            //testQueue.add(translateByteInStr(ConstructCANFrame.setLightSignalGreenOff()));
            //commandQueue.add(ConstructCANFrame.setLightSignalGreenOff());
            udpFrame = ConstructCANFrame.setLightSignalGreenOn();
        }

        return udpFrame;
    }

    public byte[] translateDirection(int LokID, int direction) {
        byte[] udpFrame = new byte[13];
        udpFrame = ConstructCANFrame.setDirection(LokID, direction);
        return udpFrame;
    }

    public byte[] translateHorn (int LokID) {
        byte[] udpFrame = new byte[13];
        udpFrame = ConstructCANFrame.hornOn(LokID);
        return udpFrame;
    }

    public byte[] translateHornOff (int LokID) {
        byte[] udpFrame = new byte[13];
        udpFrame = ConstructCANFrame.hornOff(LokID);
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
            System.out.println("tstQ: " + s);
        }
    }

    public void showCommandQueue () {
        Iterator it = commandQueue.iterator();
        while (it.hasNext()) {
            String s = translateByteInStr((byte[])it.next());
            System.out.println("cmdQ: " + s);
        }
    }

    public void showDurrQueue () {
        Iterator it = durrQueue.iterator();
        int i = 0;
        while (it.hasNext()) {
            String s = Integer.toString((int)it.next());
            System.out.println("durr["+i+"]: " + s);
            i++;
        }
    }
}
