package specht.General;

import com.microsoft.azure.eventhubs.ConnectionStringBuilder;

import java.util.*;

public class Attribute {
    public static int receivePort;
    public static int sendingPort;
    public static String sendingAddress = "192.168.0.2";
    public static String receivingAddress = "192.168.0.104";
    public static double waterRatio = 31.3725;
    public static double oilRatio = 11.7647;
    public static double sandRatio = 0.9803;

    public static String RoundCountPattern = "(.[A-F0-9]{8}.[0-9]{2}..00,01,00,02,00,01,[A-F0-9]{2},[A-F0-9]{2}.)";
    public static String SpeedPattern = "(.0009A706.[0-9]{2}..00,00,40,07,[A-F0-9]{2},[A-F0-9]{2},00,00.)";
    public static String DirectionPatternFw = "(.000A0F72.[0-9]{2}..00,00,40,07,01,00,00,00.)";
    public static String DirectionPatternBw = "(.000A0F72.[0-9]{2}..00,00,40,07,02,00,00,00.)";

    public static String DATEFORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static String DBNAME = "TRAIN_IOTHUB";
    public static String DBUSER = "TRAIN_DBA";
    public static String DBPW = "Stuttgart01$";

    public static String dbUrl = "jdbc:sqlserver://dev.hdm-server.eu;databaseName="+DBNAME+";user="+DBUSER+";password="+DBPW;
    public static String SQLStatementHeader = "INSERT INTO [dbo].[T_RESOURCES_USAGE_DATASET] ([DATATYPE], [RECORDING_START_TIME], "
            + "[TIME_STAMP], [DATASET], [DELIMITER]) VALUES ('";
    public static  String sqlDataType = "STEAMDATA";



    final ConnectionStringBuilder azureConn = new ConnectionStringBuilder()
            .setNamespaceName("BIAcademyNS")
            .setEventHubName("eventhubmarklinsteamlok")
            .setSasKeyName("RootManageSharedAccessKey")
            .setSasKey("jiuer6fxPoEnrkrxzVwWVdRi1qw2+5A3rAoevEsiEVs=");


    public static final int _CARGO_ID = 0x4006;
    public static final int _STEAM_ID = 0x4007;
    public static final int _SMLSTEAM_ID = 0x4008;

    public static final int firstByteOfSteamID = ConstructCANFrame.getFirstByteOfId(_STEAM_ID);
    public static final int secondByteOfSteamID = ConstructCANFrame.getSecondByteOfId(_STEAM_ID);
    public static final String cmdFilePath = "C:\\Scripts\\lok_commands3.txt";

    public static Dictionary switches = new Hashtable();

    public int getReceivePort() {
        return receivePort;
    }

    public void setReceivePort(int receivePort) {
        this.receivePort = receivePort;
    }

    public int getSendingPort() {
        return sendingPort;
    }

    public void setSendingPort(int sendingPort) {
        this.sendingPort = sendingPort;
    }

    public String getSendingAddress() {
        return sendingAddress;
    }

    public void setSendingAddress(String sendingAddress) {
        this.sendingAddress = sendingAddress;
    }

    public String getReceivingAddress() {
        return receivingAddress;
    }

    public void setReceivingAddress(String receivingAddress) {
        this.receivingAddress = receivingAddress;
    }

    public double getWaterRatio() {
        return waterRatio;
    }

    public void setWaterRatio(double waterRatio) {
        this.waterRatio = waterRatio;
    }

    public double getOilRatio() {
        return oilRatio;
    }

    public void setOilRatio(double oilRatio) {
        this.oilRatio = oilRatio;
    }

    public double getSandRatio() {
        return sandRatio;
    }

    public void setSandRatio(double sandRatio) {
        this.sandRatio = sandRatio;
    }

    public String getRoundCountPattern() {
        return RoundCountPattern;
    }

    public void setRoundCountPattern(String roundCountPattern) {
        RoundCountPattern = roundCountPattern;
    }

    public String getSpeedPattern() {
        return SpeedPattern;
    }

    public void setSpeedPattern(String speedPattern) {
        SpeedPattern = speedPattern;
    }

    public String getDirectionPatternFw() {
        return DirectionPatternFw;
    }

    public void setDirectionPatternFw(String directionPatternFw) {
        DirectionPatternFw = directionPatternFw;
    }

    public String getDirectionPatternBw() {
        return DirectionPatternBw;
    }

    public void setDirectionPatternBw(String directionPatternBw) {
        DirectionPatternBw = directionPatternBw;
    }

    public String getDATEFORMAT() {
        return DATEFORMAT;
    }

    public String getDBNAME() {
        return DBNAME;
    }

    public String getDBUSER() {
        return DBUSER;
    }

    public String getDBPW() {
        return DBPW;
    }

    public void setDATEFORMAT(String DATEFORMAT) {
        this.DATEFORMAT = DATEFORMAT;
    }

    public void setDBNAME(String DBNAME) {
        this.DBNAME = DBNAME;
    }

    public void setDBUSER(String DBUSER) {
        this.DBUSER = DBUSER;
    }

    public void setDBPW(String DBPW) {
        this.DBPW = DBPW;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getSQLStatementHeader() {
        return SQLStatementHeader;
    }

    public void setSQLStatementHeader(String SQLStatementHeader) {
        this.SQLStatementHeader = SQLStatementHeader;
    }

    public String getSqlDataType() {
        return sqlDataType;
    }

    public void setSqlDataType(String sqlDataType) {
        this.sqlDataType = sqlDataType;
    }

    public static int getCargoId() {
        return _CARGO_ID;
    }

    public static int getSteamId() {
        return _STEAM_ID;
    }

    public static int getSmlsteamId() {
        return _SMLSTEAM_ID;
    }

    public static int getFirstByteOfSteamID() {
        return firstByteOfSteamID;
    }

    public static int getSecondByteOfSteamID() {
        return secondByteOfSteamID;
    }

    public static String getCmdFilePath() {
        return cmdFilePath;
    }

    public ConnectionStringBuilder getAzureConn() { return azureConn; }

    public static Dictionary getSwitches() {
        return switches;
    }

    public static void setSwitches(Dictionary switches) {
        Attribute.switches = switches;
    }
}
