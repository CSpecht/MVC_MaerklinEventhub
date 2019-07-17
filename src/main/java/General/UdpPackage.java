package java.General;


import java.connect.TcpConnection;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class UdpPackage {

    private DatagramPacket datagramm;
    private long nanotime;
    private int packetnr;

    public UdpPackage(DatagramPacket packet, int nr, long timestamp) {
        this.datagramm = packet;
        setPacketnr(nr);
        setNanotime(timestamp);
    }

    public final InetAddress getAddress() { return this.datagramm.getAddress(); }

    public final int getPort() { return this.datagramm.getPort(); }

    public final byte[] getData() {
        byte[] data = this.datagramm.getData();
        return Arrays.copyOf(data, data.length);
    }

    public int getOffset() { return this.datagramm.getOffset(); }

    public final int getLength() { return this.datagramm.getLength(); }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getPacketnr());
        sb.append(":");
        sb.append(this.datagramm);
        return sb.toString();
    }

    public long getNanoTime() { return getNanotime(); }

    public void sendout() {
        if (this.datagramm == null) {
            System.err.println("leeres Datagramm - kann nix senden");
            throw new NullPointerException("leeres Datagramm - kann nix senden");
        }
        TcpConnection tcp = null;
        try {
            tcp = new TcpConnection(Attribute.sendingAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        tcp.doSend(this, this.datagramm);
    }

    public int getPacketNr() { return getPacketnr(); }

    public long getNanotime() { return this.nanotime; }

    public void setNanotime(long nanotime) { this.nanotime = nanotime; }

    public int getPacketnr() { return this.packetnr; }

    public void setPacketnr(int packetnr) { this.packetnr = packetnr; }


}
