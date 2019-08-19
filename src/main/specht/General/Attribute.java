package specht.General;

import com.microsoft.azure.eventhubs.ConnectionStringBuilder;

public interface Attribute {
    int receivePort = 15730;
    int sendingPort = 15731;
    String sendingAddress = "192.168.0.2";
    String receivingAddress = "192.168.0.104";
    double waterRatio = 31.3725;
    double oilRatio = 11.7647;
    double sandRatio = 0.9803;

    String waterPattern = "(.[A-F0-9]{8}.[A-F0-9]{2}..00,00,40,07,04,ED,[A-F0-9]{2},[A-F0-9]{2}.)";
    String oilPattern = "(.[A-F0-9]{8}.[A-F0-9]{2}..00,00,40,07,08,ED,[A-F0-9]{2},[A-F0-9]{2}.)";
    String sandPattern = "(.[A-F0-9]{8}.[A-F0-9]{2}..00,00,40,07,0C,ED,[A-F0-9]{2},[A-F0-9]{2}.)";
    String waterBadPattern = "(.[A-F0-9]{8}.[A-F0-9]{2}..00,00,40,07,04,ED,04,[A-F0-9]{2}.)";
    String RoundCountPattern = "(.[A-F0-9]{8}.[0-9]{2}..00,01,00,02,00,01,[A-F0-9]{2},[A-F0-9]{2}.)";
    String SpeedPattern = "(.0009A706.[0-9]{2}..00,00,40,07,[A-F0-9]{2},[A-F0-9]{2},00,00.)";

    final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    final String DBNAME = "TRAIN_IOTHUB";
    final String DBUSER = "TRAIN_DBA";
    final String DBPW = "Stuttgart01$";
    String dbUrl = "jdbc:sqlserver://dev.hdm-server.eu;databaseName="+DBNAME+";user="+DBUSER+";password="+DBPW;
    String SQLStatementHeader = "INSERT INTO [dbo].[T_RESOURCES_USAGE_DATASET] ([DATATYPE], [RECORDING_START_TIME], "
            + "[TIME_STAMP], [DATASET], [DELIMITER]) VALUES ('";
    String sqlDataType = "STEAMDATA";

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

    public static final int firstByteOfCargoID = ConstructCANFrame.getFirstByteOfId(_CARGO_ID);
    public static final int secondByteOfCargoID = ConstructCANFrame.getSecondByteOfId(_CARGO_ID);



}
