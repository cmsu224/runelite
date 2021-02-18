//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.socket.packet;

import net.runelite.client.plugins.socket.org.json.JSONObject;

public class SocketBroadcastPacket {
    private JSONObject payload;

    public SocketBroadcastPacket(JSONObject payload) {
        this.payload = payload;
    }

    public JSONObject getPayload() {
        return this.payload;
    }
}
