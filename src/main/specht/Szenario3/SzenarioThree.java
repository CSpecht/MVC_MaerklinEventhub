package specht.Szenario3;

import specht.General.Attribute;

import java.io.IOException;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class SzenarioThree {
    boolean debug = true;
    DatagramSocket ds, dr;
    InetAddress ia, ib;

    private Attribute attribute = null;

    public SzenarioThree () {
        if (attribute == null) {
            attribute = new Attribute();
        }

        try {
            ds = getDatagramSocketSending();
            dr = getDatagramSocketReceiving();
            ia = InetAddress.getByName(attribute.getSendingAddress());
            ib = InetAddress.getByName(attribute.getReceivingAddress());
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    public DatagramSocket getDatagramSocketSending() throws SocketException {
        if (ds == null) {
            ds = new DatagramSocket(attribute.getSendingPort());
        }
        return ds;
    }

    public DatagramSocket getDatagramSocketReceiving() throws SocketException {
        if (dr == null) {
            dr = new DatagramSocket(attribute.getReceivePort());
        }
        return dr;
    }

    public void completeTask(int s) {
        try {
            Thread.sleep(s*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void run () {
        try {
            //System.out.println("SAY HELLO");
            GetCommandFromTxt gcft = new GetCommandFromTxt();
            //gcft.processFile();
            gcft.getCommandFromSQLTable();
            gcft.showTestQueue();
            gcft.showCommandQueue();
            gcft.showDurrQueue();
            Queue<byte[]> cmdQueue = new LinkedList();
            Queue<Integer> durrQueue = new LinkedList();
            Queue<String> testQueue = new LinkedList();
            Queue<Integer> idQueue = new LinkedList<>();
            testQueue = gcft.getTestQueue();
            cmdQueue = gcft.getCommandQueue();
            durrQueue = gcft.getDurrQueue();
            idQueue = gcft.getIdQueue();
            Timer t = new Timer();


            Iterator durrIterator = durrQueue.iterator();
            int durrSecond = 0;
            Iterator cmdIterator = cmdQueue.iterator();
            int s = 0;
            Iterator testIterator = testQueue.iterator();
            byte[] element = null;

            Iterator idIterator = idQueue.iterator();
            int id;
/*
            while (cmdIterator.hasNext()) {

                element = (byte[]) cmdIterator.next();
                for (int i = 0; i < element.length; i++) {
                    System.out.print("["+i+"]: " + element[i] + " ");
                }
                System.out.println("");
                DatagramPacket packet = new DatagramPacket(element, element.length, ia, attribute.getSendingPort());

                try {
                    ds.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cmdIterator.remove();
            }
            */

            while (durrIterator.hasNext() && idIterator.hasNext()) {
                durrSecond = (int) durrIterator.next();
                id = (int) idIterator.next();
                System.out.println("durrSecond: " + durrSecond);
                if (durrSecond != 0) {
                    s = durrSecond;
                    t.schedule(new SzenarioThreeTimer(s, cmdIterator, getDatagramSocketSending(),ia),0,1000);
                    completeTask(s);
                    //cmdIterator.remove();
                    System.out.println("finish");
                    setExecutedInSql(id);
                    //t.cancel();

                } else {
                    if(cmdIterator.hasNext()) {

                        element = (byte[]) cmdIterator.next();
                        for (int i = 0; i < element.length; i++) {
                            System.out.print("["+i+"]: " + element[i] + " ");
                        }
                        DatagramPacket packet = new DatagramPacket(element, element.length, ia, attribute.getSendingPort());

                        try {
                            ds.send(packet);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        cmdIterator.remove();
                        setExecutedInSql(id);

                    }
                }

            }

            //cmdQueue = gcft.getCommandQueue();

            if (debug == false) {
                Iterator iterator = cmdQueue.iterator();
                int j = 0;
                while(iterator.hasNext()){
                    element = (byte[]) iterator.next();
                    for (int i = 0; i < element.length; i++) {
                        System.out.println("element["+j+"]["+ i + "]: " + element[i]);
                    }
                    j++;
                    //System.out.println("element: " + element);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void setExecutedInSql (int id) {
        Connection con = null;
        Attribute attribute = new Attribute();
        try {
            con = DriverManager.getConnection(attribute.getDbUrl());
            Statement stmt = con.createStatement();
            String SQL = "UPDATE [dbo].[T_COMMANDS] SET EXECUTED_YN = 1 WHERE ID =" + id;
            //String SQL = "SELECT dbo.get_train_speed(" + GameID + "," + Second +") as 'rs'";
            stmt.executeUpdate(SQL);
            if (debug) {
                System.out.println(SQL);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
