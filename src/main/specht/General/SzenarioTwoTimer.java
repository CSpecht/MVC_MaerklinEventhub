package specht.General;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.*;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;


public class SzenarioTwoTimer extends TimerTask {

    int GameID;
    AtomicInteger Second;
    boolean debug = true;
    DatagramSocket ds;
    InetAddress ia;

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }



    int speed;



    public SzenarioTwoTimer(int GameID, AtomicInteger Second, DatagramSocket ds, InetAddress ia) {
        this.GameID = GameID;
        this.Second = Second;
        this.ds = ds;
        this.ia = ia;
    }

    public void run() {
        GetSpeedfromSQL();
        byte[] udpFrame;
        if (getSpeed() == 1) {
            udpFrame = ConstructCANFrame.setSpeed(Attribute._SMLSTEAM_ID, getSpeed()-1);
        } else {

            udpFrame = ConstructCANFrame.setSpeed(Attribute._SMLSTEAM_ID, getSpeed()); //getSpeed()
        }
        DatagramPacket sendPacket = new DatagramPacket(udpFrame, udpFrame.length,ia,Attribute.sendingPort);
        try {
            this.ds.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //setYinCADDoneInSQL();
    }



    public void GetSpeedfromSQL() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection con = null;
        try {
            con = DriverManager.getConnection(Attribute.dbUrl);
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
                con = DriverManager.getConnection(Attribute.dbUrl);
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

}
