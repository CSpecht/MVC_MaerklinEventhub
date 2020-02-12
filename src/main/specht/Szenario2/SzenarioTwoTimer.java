package specht.Szenario2;

import specht.General.Attribute;
import specht.General.ConstructCANFrame;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.*;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;


public class SzenarioTwoTimer extends TimerTask {

    int GameID;
    int GameIDOld = 0;
    AtomicInteger Second;
    boolean debug = true;
    DatagramSocket ds;
    InetAddress ia;
    int speed;
    boolean newGame = false;
    private Attribute attribute = null;
    public SzenarioTwoTimer( AtomicInteger Second, DatagramSocket ds, InetAddress ia) {
        if(attribute == null) {
            attribute = new Attribute();
        }
        this.Second = Second;
        this.ds = ds;
        this.ia = ia;
    }

    public void run() {

        getGameIDfromSQL();

        if(getGameID() != getGameIDOld()) {
            newGame = true;
        }

        if (newGame) {
            byte[] udpFrame;

//SET THIS ON THAT EVERY SECOND GAME_ID A SWITCH WILL BE SWITCHED
 /*           if (getGameID() % 2 == 0) {
                udpFrame = ConstructCANFrame.setSwitchRWGreenOff();
                DatagramPacket packet = new DatagramPacket(udpFrame, udpFrame.length, ia, attribute.getSendingPort());
                try {
                    ds.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                udpFrame = ConstructCANFrame.setSwitchRWRedOn();
                packet = new DatagramPacket(udpFrame, udpFrame.length, ia, attribute.getSendingPort());
                try {
                    ds.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                udpFrame = ConstructCANFrame.setSwitchRWRedOff();
                DatagramPacket packet = new DatagramPacket(udpFrame, udpFrame.length, ia, attribute.getSendingPort());
                try {
                    ds.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                udpFrame = ConstructCANFrame.setSwitchRWGreenOn();
                packet = new DatagramPacket(udpFrame, udpFrame.length, ia, attribute.getSendingPort());
                try {
                    ds.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

*/
/*            udpFrame = ConstructCANFrame.setSwitchRWGreenOff();
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
            }*/



        }

        if (getGameID() != 0) {
            GetSpeedfromSQL();
            setGameIDOld(getGameID());
            byte[] udpFrame;
            if (getSpeed() == 1) {
                udpFrame = ConstructCANFrame.setSpeed(Attribute._SMLSTEAM_ID, getSpeed()-1);
            } else {

                udpFrame = ConstructCANFrame.setSpeed(Attribute._SMLSTEAM_ID, getSpeed());
            }
            DatagramPacket sendPacket = new DatagramPacket(udpFrame, udpFrame.length,ia,attribute.getSendingPort());
            protocolToSql(Attribute._SMLSTEAM_ID,"Lok", "Speed: " + getSpeed());
            try {
                this.ds.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            byte[] udpFrame;
            udpFrame = ConstructCANFrame.setSpeed(Attribute._SMLSTEAM_ID, 0);
            DatagramPacket sendPacket = new DatagramPacket(udpFrame, udpFrame.length,ia,attribute.getSendingPort());
            protocolToSql(Attribute._SMLSTEAM_ID,"Lok", "Speed: " + 0);
            try {
                this.ds.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setYinCADDoneInSQL();
        newGame = false;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getGameID() {
        return GameID;
    }

    public void setGameID(int GameID) {
        this.GameID = GameID;
    }

    public int getGameIDOld() { return GameIDOld; }

    public void setGameIDOld(int gameIDOld) { GameIDOld = gameIDOld; }

    public void getGameIDfromSQL() {

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection con = null;
        try {
            con = DriverManager.getConnection(attribute.getDbUrl());
            Statement stmt = con.createStatement();
            String SQL = "SELECT GAME_ID FROM " + attribute.getDBNAME() + ".dbo.T_GAME_INFO WHERE RUN_YN = 1 ";
            ResultSet rs = stmt.executeQuery(SQL);

            if (rs.next() == false && debug) {
                System.out.println("NO GAME ID FOUND");
            } else {
                do {
                    setGameID(rs.getInt("GAME_ID"));
                    if (debug) {
                        System.out.println("GAME_ID: " + rs.getInt("GAME_ID"));
                        System.out.println("OLD_GAME: " + getGameIDOld() );
                    }
                } while (rs.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void GetSpeedfromSQL() {

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection con = null;

        try {
            con = DriverManager.getConnection(attribute.getDbUrl());
            Statement stmt = con.createStatement();
            String SQL = "select TOP 1 VAL8_SPEED from D_GAME_RESOURCES WHERE GAME_ID = " + GameID + " AND RESTAPI_DONE_YN = 1 AND CAN_DONE_YN = 0 ORDER BY ROWID DESC";
            //String SQL = "SELECT dbo.get_train_speed(" + GameID + "," + Second +") as 'rs'";
            System.out.println(SQL);
            ResultSet rs = stmt.executeQuery(SQL);

            while (rs.next()) {
               setSpeed(rs.getInt("VAL8_SPEED"));
               if (debug) {
                   System.out.println("speed: " + rs.getInt("VAL8_SPEED"));
               }
           }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void setYinCADDoneInSQL () {

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection con = null;

        try {

            if (Second.get() != 0) {
                con = DriverManager.getConnection(attribute.getDbUrl());
                CallableStatement cs = con.prepareCall("EXECUTE dbo.upd_can_done_yn @myGAME_ID=?, @myMINUTE=?");
                cs.setEscapeProcessing(true);
                cs.setQueryTimeout(10);
                cs.setInt(1,GameID);
                System.out.println("GAMEID_SQL:"+GameID);
                cs.setInt(2,Second.get());
                System.out.println("GAMEMINUTE_SQL:"+Second.get());
                cs.execute();
                System.out.println(cs.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void protocolToSql (int id, String part, String message ) {
        Connection con = null;
        Attribute attribute = new Attribute();
        try {
            con = DriverManager.getConnection(attribute.getDbUrl());
            Statement stmt = con.createStatement();
            String SQL = "INSERT INTO [dbo].[T_PROTOCOL] (PART_ID, PART, MESSAGE) VALUES ("+ id + ",'" + part + "','" + message +"')";
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
