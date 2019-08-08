package specht.General;

import java.sql.*;
import java.util.TimerTask;

public class SzenarioTwoTimer extends TimerTask {

    int GameID;
    int Second;

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    int speed;



    public SzenarioTwoTimer(int GameID, int Second) {
        this.GameID = GameID;
        this.Second = Second;
    }

    public void run() {

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
            + GameID + "," + Second +")";

           ResultSet rs = stmt.executeQuery(SQL);
           setSpeed(rs.getInt(0));

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
