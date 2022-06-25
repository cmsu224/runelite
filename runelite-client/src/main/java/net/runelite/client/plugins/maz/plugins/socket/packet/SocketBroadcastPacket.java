/*
 * Decompiled with CFR 0.150.
 */
package net.runelite.client.plugins.maz.plugins.socket.packet;

import net.runelite.client.plugins.maz.plugins.socket.org.json.JSONObject;

public class SocketBroadcastPacket {
    private JSONObject payload;

    public SocketBroadcastPacket(JSONObject payload) {
        this.payload = payload;
    }

    public JSONObject getPayload() {
        return this.payload;
    }
}

