package specht.General;

//package de.maerklin.te.gpl.kielkopf.cs2.udp.befehl;

//import de.maerklin.te.gpl.kielkopf.cs2.IllegalParameterException;
//import de.maerklin.te.gpl.kielkopf.cs2.udp.UdpPacket;

public class CanBefehl
        extends CanBefehlRaw
{
  /*  private static final int POS_SUBCMD = 4;
    static final int ADRESSLAENGE = 5;
    protected static int myUID = new Random(System.nanoTime()).nextInt();

    public CanBefehl() {}

    public CanBefehl(CanBefehlRaw befehl_raw)
            throws Exception
    {
        super(befehl_raw);
    }

    public CanBefehl(CanBefehl befehl)
            throws Exception
    {
        super(befehl);
    }

    public CanBefehl(UdpPackage packet)
            throws Exception
    {
        super(packet);
    }

    public CanProtokoll.GERAETE_TYP getGeraeteTyp(long uID)
    {
        return CanProtokoll.GERAETE_TYP.getGeraeteTyp((int)uID);
    }

    public int get_Byte(int pos)
    {
        if (!contains(pos, 1)) {
            throw new IllegalParameterException("get_Byte(" + pos + ")");
        }
        return (int)(Long.rotateLeft(get_Long(), (pos + 1) * 8) & 0xFF);
    }

    public String get_String()
            throws IllegalParameterException
    {
        if (!contains(0, 8)) {
            throw new IllegalParameterException("get_String(8)");
        }
        String s = new String(this.canData);
        return s;
    }

    public int get_Integer(int pos)
    {
        if (!contains(pos, 4)) {
            throw new IllegalParameterException("get_Integer(" + pos + ")");
        }
        long l = Long.rotateLeft(get_Long(), (pos + 4) * 8);
        l &= 0xFFFFFFFF;
        return (int)l;
    }

    private long get_Long()
    {
        long l = 0L;
        byte[] arrayOfByte;
        int j = (arrayOfByte = this.canData).length;
        for (int i = 0; i < j; i++)
        {
            byte b = arrayOfByte[i];
            l <<= 8;
            l |= b & 0xFF;
        }
        return l;
    }

    public int get_Short(int pos)
    {
        if (!contains(pos, 2)) {
            throw new IllegalParameterException("get_Short(" + pos + ")");
        }
        return (int)(Long.rotateLeft(get_Long(), (pos + 2) * 8) & 0xFFFF);
    }

    public int getAdresslaenge()
    {
        return get_Byte(5);
    }

    public int getSubCmd()
    {
        if (getCanDlc() == 4) {
            return 256;
        }
        return get_Byte(4);
    }

    public CanProtokoll.Command getCommand()
    {
        return CanProtokoll.Command.getCommand(getCmd());
    }

    public CanProtokoll.SubCommand getSubCommand()
    {
        return CanProtokoll.SubCommand.getSubCommand(getSubCmd());
    }

    public void set_Byte(int daten, int pos)
            throws IllegalParameterException
    {
        if ((pos < 0) || (pos > 7)) {
            throw new IllegalParameterException("Paramter pos ist ung�ltig (" + pos + ")");
        }
        testImmutable();
        if (this.canDlc < 1 + pos) {
            setCanDlc(1 + pos);
        }
        this.canData[pos] = ((byte)(daten & 0xFF));
    }

    public void set_Integer(int daten, int pos)
            throws IllegalParameterException
    {
        if ((pos < 0) || (pos > 4)) {
            throw new IllegalParameterException("Paramter pos ist ung�ltig (" + pos + ")");
        }
        //testImmutable();
        if (this.canDlc < 4 + pos) {
            setCanDlc(4 + pos);
        }
        int i = daten;
        this.canData[(3 + pos)] = ((byte)(i & 0xFF));
        i >>>= 8;
        this.canData[(2 + pos)] = ((byte)(i & 0xFF));
        i >>>= 8;
        this.canData[(1 + pos)] = ((byte)(i & 0xFF));
        i >>>= 8;
        this.canData[pos] = ((byte)(i & 0xFF));
    }

    public void set_Long(long daten)
    {
        //testImmutable();
        long l = daten;
        for (int i = 0; i < this.canData.length; i++)
        {
            l <<= 8;
            this.canData[i] = ((byte)(int)(l & 0xFF));
        }
    }

    public void set_Short(int daten, int pos)
    {
        if ((pos < 0) || (pos > 6)) {
            throw new IllegalParameterException("Paramter pos ist ung�ltig (" + pos + ")");
        }
        //testImmutable();
        if (this.canDlc < 2 + pos) {
            setCanDlc(2 + pos);
        }
        int i = daten;
        this.canData[(1 + pos)] = ((byte)(i & 0xFF));
        i >>>= 8;
        this.canData[pos] = ((byte)(i & 0xFF));
    }

    public void set_String(String text)
    {
        if ((text == null) || (text.isEmpty()) || (text.length() > 8)) {
            throw
                    new IllegalParameterException("Paramter pos ist ung�ltig (" + text + ")");
        }
        set_Block(text.getBytes(), 0);
    }

    public void set_Block(byte[] data, int offset)
    {
        //testImmutable();
        int len = data.length - offset < 8 ? data.length - offset : 8;
        if (this.canDlc < len) {
            setCanDlc(len);
        }
        System.arraycopy(data, offset, this.canData, 0, len);
    }

    public void setSubCmd(int subcmd)
    {
        //testImmutable();
        if (subcmd > 255) {
            setCanDlc(4);
        } else {
            set_Byte(subcmd, 4);
        }
    }

    public void setCommand(CanProtokoll.Command cmd)
    {
        //testImmutable();
        setCmd(cmd.command);
    }

    public void setCommand(CanProtokoll.SubCommand subCmd)
    {
        setCmd(subCmd.command);
        setSubCmd(subCmd.subCommand);
    }

    public String toString()
    {
        return "- Info fehlt noch -";
    }

    public boolean contains(int pos, int len)
    {
        return (pos >= 0) && (pos + len <= this.canDlc);
    }

    public String getHexInt(int uid)
    {
        long l = uid;
        l &= 0xFFFFFFFF;
        return String.format("0x%-8x", new Object[] { Long.valueOf(l) });
    }

    public boolean isOfType(CanBefehl befehl, EnumSet<Test> tests)
    {
        for (Test t : tests) {
            switch (t)
            {
                case CMD:
                    if (getPrio() != befehl.getPrio()) {
                        return false;
                    }
                    break;
                case D0:
                    if (getHash() != befehl.getHash()) {
                        return false;
                    }
                    break;
                case D1:
                    if (getCmd() != befehl.getCmd()) {
                        return false;
                    }
                    break;
                case D2:
                    if (isResponse() != befehl.isResponse()) {
                        return false;
                    }
                    break;
                case D3:
                    if (getCanDlc() != befehl.getCanDlc()) {
                        return false;
                    }
                    break;
                default:
                    int i = t.ordinal() - Test.D0.ordinal();
                    if ((i < befehl.getCanDlc()) && (this.canData[i] != befehl.canData[i])) {
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    public static enum Test
    {
        PRIO,  HASH,  CMD,  RESPONSE,  DLC,  D0,  D1,  D2,  D3,  D4,  D5,  D6,  D7;
    }


   */
}

