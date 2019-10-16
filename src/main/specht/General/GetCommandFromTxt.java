package specht.General;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;


public class GetCommandFromTxt {

    Queue<byte[]> commandQueue = new LinkedList<>();
    boolean debug = false;

    byte[] udpFrame = new byte[13];

    public void getFileString() throws IOException {
        String fileName = "C:\\Scripts\\lok_commands.txt";
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
            //System.out.println(line);
            for (int j = 0; j < 4; j++) {
                tmp = line.split("\\s+");
            }
            //processCommandLine(tmp,lineCount);
            lineCount++;
        }

        if (debug) {
            Iterator iterator = commandQueue.iterator();
            while(iterator.hasNext()){
                String element = (String) iterator.next();
                System.out.println("element: " + element);
            }
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
                //s = component.trim().toUpperCase()+ " " + id + " " +  command.trim().toUpperCase();
                //commandQueue.add(s);
            } else if (command.trim().equalsIgnoreCase("links")) {
                position = 1;
                commandQueue.add(translateSwitch(id, position));
                //s = component.trim().toUpperCase()+ " " + id + " " +  command.trim().toUpperCase();
                //commandQueue.add(s);
            }
        }

        else if (component.trim().equalsIgnoreCase("lok")) {
            int speed = 0;
            int go = 0;

            if (isInteger(command.trim())) {
                speed = Integer.parseInt(command);
                commandQueue.add(translateLok(id, go));
                //s = component.trim().toUpperCase() + " " + id + " " + speed + " " + durr;
                //commandQueue.add(s);
            } else if (command.trim().equalsIgnoreCase("start")) {
                go = 1;
                commandQueue.add(translateLok(id, go));
                //s = component.trim().toUpperCase()+ " " + id + " " + command.trim().toUpperCase();
                //commandQueue.add(s);
            } else if (command.trim().equalsIgnoreCase("stopp")) {
                go = 0;
                commandQueue.add(translateLok(id, go));
                s = component.trim().toUpperCase()+ " " + id + " " +  command.trim().toUpperCase();
                //commandQueue.add(s);
            }
        }


        else if (component.trim().equalsIgnoreCase("signal")) {
            int signal = 0;
            if (command.trim().equalsIgnoreCase("rot")) {
                signal = 0;
                commandQueue.add(translateSignal(id, signal));
                //s = component.trim().toUpperCase() + " " + id + " " + command.trim().toUpperCase();
                //commandQueue.add(s);
            } else if (command.trim().equalsIgnoreCase("gruen")) {
                signal = 0;
                commandQueue.add(translateSignal(id, signal));
                //s = component.trim().toUpperCase() + " " + id + " " + command.trim().toUpperCase();
                //commandQueue.add(s);
            }
        }

        else if (command.trim().equalsIgnoreCase("clear")) {

        } else {
            System.out.println("Invalid Command! on Line: " + lineNumber);
        }

    }
    /************************* ADD RIGHT CAN FRAME *************************/
    public byte[] translateSwitch (int switchID, int switchPosition) {
        udpFrame = ConstructCANFrame.setSpeed(Attribute._SMLSTEAM_ID, 0);
        return udpFrame;
    }

    /************************* ADD RIGHT CAN FRAME *************************/
    public byte[] translateLok(int lokID, int speed, int time) {
        udpFrame = ConstructCANFrame.setSpeed(Attribute._SMLSTEAM_ID, 0);
        return udpFrame;
    }

    /************************* ADD RIGHT CAN FRAME *************************/
    public byte[] translateLok(int lokID, int go) {
        udpFrame = ConstructCANFrame.setSpeed(Attribute._SMLSTEAM_ID, 0);
        return udpFrame;
    }
    /************************* ADD RIGHT CAN FRAME *************************/
    public byte[] translateSignal(int signalID, int light) {
        udpFrame = ConstructCANFrame.setSpeed(Attribute._SMLSTEAM_ID, 0);
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
}
