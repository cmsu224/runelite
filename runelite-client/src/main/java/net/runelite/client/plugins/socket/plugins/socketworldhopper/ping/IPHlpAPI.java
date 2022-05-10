/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.jna.Library
 *  com.sun.jna.Native
 *  com.sun.jna.Pointer
 */
package net.runelite.client.plugins.socket.plugins.socketworldhopper.ping;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import net.runelite.client.plugins.socket.plugins.socketworldhopper.ping.IcmpEchoReply;

interface IPHlpAPI
extends Library {
    public static final IPHlpAPI INSTANCE = (IPHlpAPI)Native.loadLibrary((String)"IPHlpAPI", IPHlpAPI.class);

    public Pointer IcmpCreateFile();

    public boolean IcmpCloseHandle(Pointer var1);

    public int IcmpSendEcho(Pointer var1, int var2, Pointer var3, short var4, Pointer var5, IcmpEchoReply var6, int var7, int var8);
}

