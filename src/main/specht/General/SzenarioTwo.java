package specht.General;

import java.io.IOException;
import java.net.*;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class SzenarioTwo extends Thread {

    //for test cases equal 16 otherwise delete = 16!
    int GameID = 16;
    AtomicInteger Second = new AtomicInteger(0);
    int SecondInt = 0;
    boolean debug = true;
    boolean startRun = false;
    DatagramSocket ds, dr;
    InetAddress ia, ib;

    SzenarioTwo() {
        try {
            ds = getDatagramSocketSending();
            dr = getDatagramSocketReceiving();
            ia = InetAddress.getByName(Attribute.sendingAddress);
            ib = InetAddress.getByName(Attribute.receivingAddress);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void setStartRun(boolean start) {
        this.startRun = start;
    }

    public boolean getStartRun() {
        return startRun;
    }

    public int getGameID() {
        return GameID;
    }

    public void setGameID(int GameID) {
        this.GameID = GameID;
    }

    public void getGameIDfromSQL() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection con = null;
        try {
            con = DriverManager.getConnection(Attribute.dbUrl);
            Statement stmt = con.createStatement();
            String SQL = "SELECT GAME_ID FROM " + Attribute.DBNAME + ".dbo.T_GAME_INFO WHERE RUN_YN = 1 ";
            System.out.println(SQL);
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                setGameID(rs.getInt("GAME_ID"));
                setStartRun(true);
                if (debug) {
                    System.out.println("GAME_ID: " + rs.getInt("GAME_ID"));
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public DatagramSocket getDatagramSocketSending() throws SocketException {
        if (ds == null) {
            ds = new DatagramSocket(Attribute.sendingPort);
        }
        return ds;
    }

    public DatagramSocket getDatagramSocketReceiving() throws SocketException {
        if (dr == null) {
            dr = new DatagramSocket(Attribute.receivePort);
        }
        return dr;
    }

    public AtomicInteger getSecond() {
        return Second;
    }

    public void setSecond(AtomicInteger second) {
        Second = second;
    }

    public int getSecondInt() {
        return SecondInt;
    }

    public void setSecondInt() {
        this.SecondInt = getSecond().get();
    }

    public void run() {

        while (true && getStartRun() == false) {
            getGameIDfromSQL();
        }
        Timer t = new Timer();
        try {
            t.schedule(new SzenarioTwoTimer(GameID, this.getSecond(), getDatagramSocketSending(), ia), 0, 1000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        //THIS WAS THE FIRST SOLUTION!!!!
        /*       if (startRun) {
            GameID = getGameID();

            if (GameID == 0) {
                System.out.println("No GAME ID: " + getGameID() + " FOUND in SQL Server!");
            } else {
                byte[] udpFrame = new byte[13];
                udpFrame = ConstructCANFrame.setLightSignalGreenOn();
                for (int i = 0; i < udpFrame.length; i++) {
                    System.out.println("i["+i+"]: " +udpFrame[i]);
                }
                DatagramPacket packet = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
                try {
                    ds.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Timer t = new Timer();
                t.scheduleAtFixedRate(new TimerTask() {
                    public void run() {
                        if (Second.get() == 5) {

                            System.out.println("HELLO 5 SECONDS");
                            byte[] udpFrame = new byte[13];
                            //udpFrame = ConstructCANFrame.setLightSignalGreenOn();
                            udpFrame = ConstructCANFrame.setSwitchRWRedOff();
                            DatagramPacket packet = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
                            try {
                                ds.send(packet);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            udpFrame = ConstructCANFrame.setSwitchRWGreenOn();
                            packet = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
                            try {
                                ds.send(packet);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            udpFrame = ConstructCANFrame.setLightSignalRedOn();
                            packet = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
                            try {
                                ds.send(packet);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        if (Second.get() >= 90) {
                            byte[] udpFrame = new byte[13];
                            //udpFrame = ConstructCANFrame.setLightSignalGreenOn();
                            udpFrame = ConstructCANFrame.setSpeed(Attribute._SMLSTEAM_ID, 400);

                            DatagramPacket packet = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
                            try {
                                ds.send(packet);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            udpFrame = ConstructCANFrame.setSwitchRWGreenOff();
                            packet = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
                            try {
                                ds.send(packet);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            udpFrame = ConstructCANFrame.setSwitchRWRedOn();
                            packet = new DatagramPacket(udpFrame, udpFrame.length, ia, Attribute.sendingPort);
                            try {
                                ds.send(packet);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        //elapsed time
                        Second.incrementAndGet();
                        setSecondInt();
                        if (debug) {
                            System.out.println("SECOND: " + getSecond());
                            System.out.println("SECONDINT: " + getSecondInt());
                        }

                    }

                }, 0, 1000);

                if(getSecondInt() <= 90) {
                    try {
                        t.schedule(new SzenarioTwoTimer(GameID, this.getSecond(), getDatagramSocketSending(), ia), 0, 1000);
                    } catch (SocketException e) {
                        e.printStackTrace();
                    }
                } else {
                    t.cancel();
                    t.purge();
                }




            }

        }

        */
    }


}

