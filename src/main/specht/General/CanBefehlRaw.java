package specht.General;

//package de.maerklin.te.gpl.kielkopf.cs2.udp.befehl;

//import de.maerklin.te.gpl.kielkopf.cs2.ImmutableObjectException;
//import de.maerklin.te.gpl.kielkopf.cs2.udp.Cs2Verbindung;
//import de.maerklin.te.gpl.kielkopf.cs2.udp.UdpPacket;
//import specht.connect.Cs3Connection;
//import specht.General.UdpPackage;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Arrays;

public class CanBefehlRaw
{
    private static final byte[] hashinit = { -83, -22 };
    //protected byte[] canId = { 003 };
    //private byte[] canIId = new byte[4];
    byte[] canHeader = new byte[5];
    byte[] canId = new byte[4];
    protected byte canDlc = 0;
    protected final byte[] canData = new byte[8];
    private volatile UdpPackage udpPacket = null;

    public byte[] getCanId()
    {
        return this.canId;
    }

    public int getCanIdInt()
    {
        int erg = this.canId[0] & 0x1F;
        erg <<= 8;
        erg |= this.canId[1] & 0xFF;
        erg <<= 8;
        erg |= this.canId[2] & 0xFF;
        erg <<= 8;
        erg |= this.canId[3] & 0xFF;
        return erg;
    }

    public byte getCanDlc()
    {
        return this.canDlc;
    }

    public byte[] getCanData()
    {
        return this.canDlc == 8 ? this.canData : Arrays.copyOf(this.canData, this.canDlc);
    }

    public byte getPrio()
    {
        return (byte)(this.canId[0] >>> 1 & 0xF);
    }

    public int getCmd()
    {
        return (this.canId[0] & 0x1) << 7 | (this.canId[1] & 0xFE) >>> 1;
    }

    public boolean isResponse()
    {
        //System.out.println("CANID_Length: "+ this.canId.length);
        return ( canHeader[1] & 0x1) == 1; //canId[0]
    }
    public int getIntOfByte (byte[] canId, int index) {
        int erg = 0;
        erg = (int)canId[index];
        return erg;
    }
    public boolean isAnfrage()
    {
        return !isResponse();
    }

    public int getHash()
    {
        int hashCan = (this.canId[2] & 0xFF) << 8 | this.canId[3] & 0xFF;
        int hashPrg = (hashCan & 0xFC00) >>> 3 | hashCan & 0x7F;
        return hashPrg;
    }

    public boolean isCsMessage()
    {
        int hashCan = (this.canId[2] & 0xFF) << 8 | this.canId[3] & 0xFF;
        int flag = hashCan >> 7 & 0x7;
        return 6 == flag;
    }

    public boolean hasHash0_App_PCSW_mDTool()
    {
        int hashCan = (this.canId[2] & 0xFF) << 8 | this.canId[3] & 0xFF;
        int flag = hashCan >> 7 & 0x7;
        return flag == 0;
    }

    public CanBefehlRaw(CanBefehlRaw befehl_raw)
            throws Exception
    {
        copyData(befehl_raw.udpPacket);
        setResp(true);
    }

    public CanBefehlRaw(CanBefehl befehl)
            throws Exception
    {
        copyData(befehl);
    }

    public CanBefehlRaw(UdpPackage packet)
            throws Exception
    {
        copyData(packet);
        this.udpPacket = packet;
    }

    private final void copyData(UdpPackage packet)
            throws Exception
    {
        //testImmutable();
        if ((packet.getLength() == 13) ||
                (packet.getLength() == 16))
        {
            //System.out.println("lengthPackage: " + packet.getLength());
            byte[] packetdata = packet.getData();
            for (int i = 0; i < packet.getData().length ; i++) {
                if (i<=4)
                {
                    canHeader[i] = packetdata[i];
                }
                //System.out.println("packetData["+i+"]:");
            }
            this.canDlc = canHeader[canHeader.length-1];
            int offset = packet.getOffset();
            //System.out.println("offset: " + offset);
            for (int i = 0; i < this.canId.length; i++) {
                this.canId[i] = packetdata[(offset++)];
                //System.out.println("this.canId["+i+"]: " +packetdata[offset]);//+ packetdata[(offset++)]);
            }
            //this.canDlc = packetdata[(offset++)];
           // System.out.println("DLC: " + canHeader[4]);
            //System.out.println();
            for (int i = 0; i < this.canData.length; i++) {
                if(i >= canHeader.length)
                this.canData[i] = packetdata[i];

            }
            canId = Arrays.copyOf(canHeader,canHeader.length-1);
            return;
        }
        throw new Exception("ungueltiges Can-Packet");
    }

    private final void copyData(CanBefehl befehl)
            throws Exception
    {
        //testImmutable();
        for (int i = 0; i < this.canId.length; i++) {
            this.canId[i] = befehl.canId[i];
        }
        this.canDlc = befehl.canDlc;
        for (int i = 0; i < this.canData.length; i++) {
            this.canData[i] = befehl.canData[i];
        }
    }

    public CanBefehlRaw()
    {
        setHash(44522);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("prio=");
        sb.append(getPrio());
        sb.append(" cmd=");
        appendInHex(sb, getCmd(), 2);
        sb.append(isResponse() ? " resp" : " ----");
        sb.append(" hash=");
        appendInHex(sb, getHash(), 4);
        sb.append(" dlc=");
        sb.append(this.canDlc);
        sb.append(" data=");
        appendInHex(sb, this.canData, 2, this.canDlc);
        return sb.toString();
    }

    public void appendInHex(StringBuilder sb, int i, int len)
    {
        int l = len;
        String hex = Integer.toHexString(i);
        l -= hex.length();
        while (l-- > 0) {
            sb.append("0");
        }
        sb.append(hex);
    }

    public void appendInDec(StringBuilder sb, int i, int len)
    {
        int l = len;
        String dec = Integer.toString(i, 10);
        l -= dec.length();
        while (l-- > 0) {
            sb.append(" ");
        }
        sb.append(dec);
    }

    public void appendInHex(StringBuilder sb, byte[] bytes, int len, int count)
    {
        int l = -2;
        int cnt = count;
        sb.append("[");
        byte[] arrayOfByte;
        int j = (arrayOfByte = bytes).length;
        for (int i = 0; i < j; i++)
        {
            byte b = arrayOfByte[i];
            if (cnt-- < 1) {
                break;
            }
            if (l != -2) {
                sb.append(",");
            }
            l = len;
            String hex = Integer.toHexString(b & 0xFF);
            l -= hex.length();
            while (l-- > 0) {
                sb.append("0");
            }
            sb.append(hex);
        }
        sb.append("]");
    }

    public void setCanId(byte[] newCanId)
    {
        //testImmutable();
        if ((newCanId == null) || (newCanId.length != this.canId.length)) {
            return;
        }
        for (int i = 0; i < this.canId.length; i++) {
            this.canId[i] = newCanId[i];
        }
    }

    public void setCanId(int newCanId)
    {
        //testImmutable();
        this.canId[0] = ((byte)(newCanId >>> 24 & 0x1F));
        this.canId[1] = ((byte)(newCanId >>> 16 & 0xFF));
        this.canId[2] = ((byte)(newCanId >>> 8 & 0xFF));
        this.canId[3] = ((byte)(newCanId & 0xFF));
    }

    public void setCanDlc(int newCanDlc)
    {
        //testImmutable();
        this.canDlc = ((byte)(newCanDlc < 0 ? 0 : newCanDlc > 8 ? 8 : newCanDlc));
        for (int i = 0; i < this.canData.length; i++) {
            if (i + 1 > this.canDlc) {
                this.canData[i] = 0;
            }
        }
    }

    public void setCanData(byte[] newCanData)
    {
        //testImmutable();
        if (newCanData == null) {
            return;
        }
        this.canDlc = ((byte)(newCanData.length > 8 ? 8 : newCanData.length));
        for (int i = 0; i < this.canData.length; i++) {
            this.canData[i] = (i + 1 > this.canDlc ? 0 : newCanData[i]);
        }
    }

    public void setPrio(int prio)
    {
        //testImmutable();
        int tmp9_8 = 0; byte[] tmp9_5 = this.canId;tmp9_5[tmp9_8] = ((byte)(tmp9_5[tmp9_8] & 0x1)); int
            tmp20_19 = 0; byte[] tmp20_16 = this.canId;tmp20_16[tmp20_19] = ((byte)(tmp20_16[tmp20_19] | prio << 1 & 0x1E));
    }

    public void setCmd(int cmd)
    {
        //testImmutable();
        int tmp9_8 = 0; byte[] tmp9_5 = this.canId;tmp9_5[tmp9_8] = ((byte)(tmp9_5[tmp9_8] & 0x1E)); int
            tmp21_20 = 0; byte[] tmp21_17 = this.canId;tmp21_17[tmp21_20] = ((byte)(tmp21_17[tmp21_20] | cmd >>> 7 & 0x1)); int
            tmp37_36 = 1; byte[] tmp37_33 = this.canId;tmp37_33[tmp37_36] = ((byte)(tmp37_33[tmp37_36] & 0x1)); int
            tmp48_47 = 1; byte[] tmp48_44 = this.canId;tmp48_44[tmp48_47] = ((byte)(tmp48_44[tmp48_47] | cmd << 1 & 0xFE));
    }

    public void setResp(boolean resp)
    {
        //testImmutable();
        int tmp9_8 = 1;
        byte[] tmp9_5 = this.canId;
        tmp9_5[tmp9_8] = ((byte)(tmp9_5[tmp9_8] & 0xFE));
        int tmp22_21 = 1;
        byte[] tmp22_18 = this.canId;
        tmp22_18[tmp22_21] = ((byte)(tmp22_18[tmp22_21] | (resp ? 1 : 0)));
    }

    public void setHash(int hashPrg)
    {
        //testImmutable();
        int hashCan = hashPrg & 0x7F | (hashPrg & 0x1F80) << 3 | 0x300;
        this.canId[3] = ((byte)(hashCan & 0xFF));
        this.canId[2] = ((byte)(hashCan >>> 8 & 0xFF));
    }

    private void createUdpPacket(InetAddress ip, int port)
    {
        if (this.udpPacket != null) {
            return;
        }
        byte[] packetdata = { this.canId[0], this.canId[1], this.canId[2], this.canId[3], this.canDlc,
                this.canData[0], this.canData[1], this.canData[2], this.canData[3], this.canData[4], this.canData[5], this.canData[6], this.canData[7] };
        InetAddress zielIP = ip;
        if (ip == null) {
            try
            {
               // zielIP = Cs3Connection.getVerbindung().getCS3_IP(0);
            }
            catch (Exception localException) {}
        }
        int zielPort = port == 0 ? 15731 : port;
        this.udpPacket = new UdpPackage(new DatagramPacket(packetdata, 0, packetdata.length, zielIP, zielPort), 0, 0L);
    }

    public void sendout(InetAddress ip, int port)
    {
        //testImmutable();
        createUdpPacket(ip, port);
        this.udpPacket.sendout();
    }

    public void sendout()
    {
        sendout(null, 15731);
    }

    public void sendout(InetAddress ip)
    {
        sendout(ip, 15731);
    }

    protected void testImmutable()
            throws ImmutableObjectException
    {
        if (isImmutable()) {
            throw new ImmutableObjectException(this);
        }
    }

    public boolean isSendt()
    {
        if (this.udpPacket == null) {
            return false;
        }
        return this.udpPacket.getNanoTime() != 0L;
    }

    public final boolean isImmutable()
    {
        return this.udpPacket != null;
    }

    public UdpPackage getUdpPacket()
    {
        return this.udpPacket;
    }

    public long getNanoTime()
    {
        return this.udpPacket.getNanoTime();
    }
}
