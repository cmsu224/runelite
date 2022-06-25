/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  com.google.common.primitives.Bytes
 *  com.sun.jna.Memory
 *  com.sun.jna.Pointer
 *  net.runelite.client.util.OSType
 *  net.runelite.http.api.worlds.World
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.maz.plugins.socket.plugins.socketworldhopper.ping;

import com.google.common.base.Charsets;
import com.google.common.primitives.Bytes;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import net.runelite.client.util.OSType;
import net.runelite.http.api.worlds.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ping {
    private static final Logger log = LoggerFactory.getLogger(Ping.class);
    private static final byte[] RUNELITE_PING = "RuneLitePing".getBytes(Charsets.UTF_8);
    private static final int TIMEOUT = 2000;
    private static final int PORT = 43594;
    private static short seq;

    public static int ping(World world) {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(world.getAddress());
        }
        catch (UnknownHostException var5) {
            log.warn("error resolving host for world ping", (Throwable)var5);
            return -1;
        }
        try {
            switch (OSType.getOSType()) {
                case Windows: {
                    return Ping.windowsPing(inetAddress);
                }
                case Linux: {
                    try {
                        return Ping.linuxPing(inetAddress);
                    }
                    catch (Exception var3) {
                        return Ping.tcpPing(inetAddress);
                    }
                }
            }
            return Ping.tcpPing(inetAddress);
        }
        catch (IOException var4) {
            log.warn("error pinging", (Throwable)var4);
            return -1;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static int windowsPing(InetAddress inetAddress) {
        int var8;
        IPHlpAPI ipHlpAPI = IPHlpAPI.INSTANCE;
        Pointer ptr = ipHlpAPI.IcmpCreateFile();
        try {
            byte[] address = inetAddress.getAddress();
            Memory data = new Memory((long)RUNELITE_PING.length);
            data.write(0L, RUNELITE_PING, 0, RUNELITE_PING.length);
            IcmpEchoReply icmpEchoReply = new IcmpEchoReply((Pointer)new Memory((long)IcmpEchoReply.SIZE + data.size()));
            assert (icmpEchoReply.size() == IcmpEchoReply.SIZE);
            int packed = address[0] & 0xFF | (address[1] & 0xFF) << 8 | (address[2] & 0xFF) << 16 | (address[3] & 0xFF) << 24;
            int ret = ipHlpAPI.IcmpSendEcho(ptr, packed, (Pointer)data, (short)data.size(), Pointer.NULL, icmpEchoReply, IcmpEchoReply.SIZE + (int)data.size(), 2000);
            if (ret != 1) {
                int var12;
                int n = var12 = -1;
                return n;
            }
            var8 = Math.toIntExact(icmpEchoReply.roundTripTime.longValue());
        }
        finally {
            ipHlpAPI.IcmpCloseHandle(ptr);
        }
        return var8;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static int linuxPing(InetAddress inetAddress) throws IOException {
        int var15;
        RLLibC libc = RLLibC.INSTANCE;
        byte[] address = inetAddress.getAddress();
        int sock = libc.socket(2, 2, 1);
        if (sock < 0) {
            throw new IOException("failed to open ICMP socket");
        }
        try {
            Timeval tv = new Timeval();
            tv.tv_sec = 2L;
            if (libc.setsockopt(sock, 1, 20, tv.getPointer(), tv.size()) < 0) {
                throw new IOException("failed to set SO_RCVTIMEO");
            }
            short var10000 = seq;
            seq = (short)(var10000 + 1);
            short seqno = var10000;
            byte[] request = new byte[]{8, 0, 0, 0, 0, 0, (byte)(seqno >> 8 & 0xFF), (byte)(seqno & 0xFF)};
            request = Bytes.concat((byte[][])new byte[][]{request, RUNELITE_PING});
            byte[] addr = new byte[]{2, 0, 0, 0, address[0], address[1], address[2], address[3], 0, 0, 0, 0, 0, 0, 0, 0};
            long start = System.nanoTime();
            if (libc.sendto(sock, request, request.length, 0, addr, addr.length) != request.length) {
                int var19;
                int n = var19 = -1;
                return n;
            }
            int size = 8 + RUNELITE_PING.length;
            Memory response = new Memory((long)size);
            if (libc.recvfrom(sock, (Pointer)response, size, 0, null, null) != size) {
                int var20;
                int n = var20 = -1;
                return n;
            }
            long end = System.nanoTime();
            short seq = (short)((response.getByte(6L) & 0xFF) << 8 | response.getByte(7L) & 0xFF);
            if (seqno == seq) {
                int var21;
                int n = var21 = (int)((end - start) / 1000000L);
                return n;
            }
            log.warn("sequence number mismatch ({} != {})", (Object)seqno, (Object)seq);
            var15 = -1;
        }
        finally {
            libc.close(sock);
        }
        return var15;
    }

    private static int tcpPing(InetAddress inetAddress) throws IOException {
        int var6;
        try (Socket socket = new Socket();){
            socket.setSoTimeout(2000);
            long start = System.nanoTime();
            socket.connect(new InetSocketAddress(inetAddress, 43594));
            long end = System.nanoTime();
            var6 = (int)((end - start) / 1000000L);
        }
        return var6;
    }
}

