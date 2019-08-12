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
        MSSQL();
        byte[] udpFrame = ConstructCANFrame.setSpeed(Attribute._CARGO_ID, getSpeed()*20);
        DatagramPacket sendPacket = new DatagramPacket(udpFrame, udpFrame.length,ia,Attribute.sendingPort);
        try {
            this.ds.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void MSSQL () {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection con = null;
        try {
            con = DriverManager.getConnection(Attribute.dbUrl);
            Statement stmt = con.createStatement();
            String SQL = "SELECT dbo.get_train_speed("
            + GameID + "," + Second +") as 'rs'";
            System.out.println(SQL);
           ResultSet rs = stmt.executeQuery(SQL);
           while (rs.next()) {
               setSpeed(rs.getInt("rs"));
               if (debug) {
                   System.out.println("speed: " + rs.getInt("rs"));
               }

           }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
