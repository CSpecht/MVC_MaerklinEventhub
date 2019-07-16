package General;

import com.microsoft.azure.eventhubs.ConnectionStringBuilder;

import java.net.InetAddress;

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

    String DATEFORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    String DBNAME = "TRAIN_IOTHUB";
    String DBUSER = "TRAIN_DBA";
    String DBPW = "Password123";
    String dbUrl = "jdbc:sqlserver://edu.hdm-server.eu;databaseName="+DBNAME+";user="+DBUSER+";password="+DBPW;
    String SQLStatementHeader = "INSERT INTO [dbo].[T_RESOURCES_USAGE_DATASET] ([DATATYPE], [RECORDING_START_TIME], "
            + "[TIME_STAMP], [DATASET], [DELIMITER]) VALUES ('";
    String sqlDataType = "STEAMDATA";

    ConnectionStringBuilder azureConn = new ConnectionStringBuilder()
            .setNamespaceName("BIAcademyNS")
            .setEventHubName("eventhubmarklinsteamlok")
            .setSasKeyName("RootManageSharedAccessKey")
            .setSasKey("jiuer6fxPoEnrkrxzVwWVdRi1qw2+5A3rAoevEsiEVs=");


    int _CARGO_ID = 0x4006;
    int _STEAM_ID = 0x4007;



}
