/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.socket.packet;

import net.runelite.client.plugins.socket.org.json.JSONObject;

public class SocketReceivePacket {
    private JSONObject payload;

    public SocketReceivePacket(JSONObject payload) {
        this.payload = payload;
    }

    public JSONObject getPayload() {
        return this.payload;
    }
}

