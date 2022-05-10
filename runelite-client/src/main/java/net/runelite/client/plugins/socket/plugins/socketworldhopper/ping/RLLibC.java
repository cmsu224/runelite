/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.jna.Native
 *  com.sun.jna.Pointer
 *  com.sun.jna.platform.unix.LibC
 */
package net.runelite.client.plugins.socket.plugins.socketworldhopper.ping;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.unix.LibC;

interface RLLibC
extends LibC {
    public static final RLLibC INSTANCE = (RLLibC)Native.loadLibrary((String)"c", RLLibC.class);
    public static final int AF_INET = 2;
    public static final int SOCK_DGRAM = 2;
    public static final int SOL_SOCKET = 1;
    public static final int IPPROTO_ICMP = 1;
    public static final int SO_RCVTIMEO = 20;

    public int socket(int var1, int var2, int var3);

    public int sendto(int var1, byte[] var2, int var3, int var4, byte[] var5, int var6);

    public int recvfrom(int var1, Pointer var2, int var3, int var4, Pointer var5, Pointer var6);

    public int setsockopt(int var1, int var2, int var3, Pointer var4, int var5);
}

