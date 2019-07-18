package specht.General;



//    package de.maerklin.te.gpl.kielkopf.cs2.udp;

//import de.maerklin.te.gpl.kielkopf.cs2.udp.befehl.CanBefehl;
//import de.maerklin.te.gpl.kielkopf.cs2.udp.befehl.CanProtokoll.Command;
//import de.maerklin.te.gpl.kielkopf.cs2.udp.befehl.CanProtokoll.SubCommand;

    public class CommandDecoder
    {
      /*  public static String getBefehlsname(CanBefehl befehl)
        {
            if ((befehl.getCmd() == 24) &&
                    (befehl.hasHash0_App_PCSW_mDTool())) {
                return "TeilnehmerPing> von App_PCSW_mDTool";
            }
            if (!befehl.isCsMessage()) {
                return "kein CS-Command";
            }
            CanProtokoll.Command command = CanProtokoll.Command.getCommand(befehl.getCmd());
            if (command == null) {
                return String.format("??? (0x%1$x)", new Object[] { Integer.valueOf(befehl.getCmd()) });
            }
            switch (command)
            {
                case AnfordernConfigData:
                    CanProtokoll.SubCommand subcommand = CanProtokoll.SubCommand.getSubCommand(befehl.getSubCmd());
                    return command.name() + " " + (
                            subcommand != null ? subcommand.name() : String.format("??? (0x%1$x)", new Object[] { Integer.valueOf(befehl.getSubCmd()) }));
            }
            return command.name();
        }

        public static boolean isBootloaderSchiene_FLASH_STOP_Frage(CanBefehl befehl)
        {
            if ((befehl.getCmd() == 28) &&
                    (!befehl.isResponse()) &&
                    (befehl.getCanDlc() == 7)) {
                return true;
            }
            return false;
        }

        public static boolean isBootloaderSchiene_DATA_nicht_BLOCKHEADER(CanBefehl befehl)
        {
            if ((befehl.getCmd() == 28) &&
                    (!befehl.isResponse()) &&
                    (befehl.getCanDlc() == 8) &&
                    (befehl.getHash() > 0)) {
                return true;
            }
            return false;
        }

        public static boolean isStatusDatKonfig_Kanal0Anfrage_an_GUI3defaultUID(CanBefehl befehl)
        {
            if ((befehl.getCmd() == 29) &&
                    (!befehl.isResponse()) &&
                    (befehl.getCanDlc() == 5))
            {
                byte[] bytes = befehl.getCanData();
                if ((bytes[0] == 0) &&
                        (bytes[1] == -68) &&
                        (bytes[2] == 97) &&
                        (bytes[3] == 78) &&
                        (bytes[4] == 0)) {
                    return true;
                }
                return false;
            }
            return false;
        }

        public static boolean isVerifyUID0SID1(CanBefehl befehl)
        {
            if ((befehl.getCmd() == 3) &&
                    (befehl.getCanDlc() == 6))
            {
                byte[] bytes = befehl.getCanData();
                if ((bytes[0] == 0) &&
                        (bytes[1] == 0) &&
                        (bytes[2] == 0) &&
                        (bytes[3] == 0) &&
                        (bytes[4] == 0) && (
                        (bytes[5] == 0) || (bytes[5] == 1))) {
                    return true;
                }
                return false;
            }
            return false;
        }
    }
*/
}
